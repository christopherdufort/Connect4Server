package business;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import controller.ServerGameController;
import datacomm.MessageType;
import datacomm.Network;

/**
 * C4ServerSession class is created for a single connection between the server
 * and a client for the purpose of playing a game of connect four. Each session
 * will communicate with an instance of the ServerGameController a class
 * containing all the rules and methods used for the server to play a game.
 * Sessions are responsible for deciphering messages from the client and
 * reacting accordingly, and sending response messages to client.
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 * @version MultiThreaded Phase 3
 * @since JDK 1.8
 */
public class C4ServerSession implements Runnable {

	private Socket clntSock;
	private boolean playSession;
	private boolean playGame;
	private byte[] messageReceived;
	private byte[] messageToSend;
	private ServerGameController myGame;

	/**
	 * Constructor for the C4ServerSession sets default values and creates
	 * instance of serverGameController.
	 *
	 * @param clntSock
	 *            Socket used to communicate with specific client within this
	 *            session.
	 */
	public C4ServerSession(Socket clntSock) {
		this.clntSock = clntSock;
		this.playSession = true;
		this.playGame = false;
		this.myGame = null;
	}

	/**
	 * Overridden run method from Runnable interface.
	 * Session responsible for receiving initial messages from client. 
	 * Options include beginning a game or ending the session.
	 * 
	 * @since Phase 3 changed to run() from startSession() 
	 * Now spins off multiple threads in order to provide a new session per client.
	 */
	@Override
	public void run() {
		System.out.println(Thread.currentThread().getName());
		try {
			while (playSession) {
				// receive initial message from client
				messageReceived = Network.receiveMessage(clntSock);

				switch (MessageType.fromValue(messageReceived[0])) {
				case NEW_GAME:
					myGame = new ServerGameController();
					playGame = true;
					System.out.println("Game started");
					messageToSend = new byte[] { MessageType.NEW_GAME.getCode(), 0, 0 };
					Network.sendMessage(clntSock, messageToSend);
					this.playGame();
					break;
				case END_SESSION:
					playSession = false;
					System.out.println("Session ended");
					break;
				default: // Default that should not be reached.
					System.out.println("Other Message Received.");
				}
			}
		} catch (IOException e) {
			System.out.println("Session ended.");
		}
		//Closing the socket may cause an exception.
		try  										
	    {
	      clntSock.close();
	    } catch (IOException e)
	    {
	      System.out.println("Exception = " +  e.getMessage());
	    }
	}

	/**
	 * playGame method is called when the client sends a message requesting to
	 * begin a game with the server. While playing a game this method will
	 * maintain a packet dance with the client while making use of the game
	 * class.
	 */
	private void playGame() {
		// Game class initialization
		MessageType checkGameEnded;
		try {
			while (playGame) {
				System.out.println("Waiting for move...");
				messageReceived = Network.receiveMessage(clntSock);
				System.out.println("Received Message was " + messageReceived[0]);
				switch (MessageType.fromValue(messageReceived[0])) {
				case MOVE: // Client move
					System.out.println("Client Move received.");
					messageToSend = myGame.gameLogic(messageReceived);
					Network.sendMessage(clntSock, messageToSend);
					checkGameEnded = MessageType.fromValue(messageToSend[0]);
					if (checkGameEnded == MessageType.TIE || checkGameEnded == MessageType.SERVER_WIN
							|| checkGameEnded == MessageType.USER_WIN)
						playGame = false;
					break;
				case END_GAME: // Client Requested to end the game.
					System.out.println("Request to end game received");
					playGame = false;
					System.out.println("Game ended");
					break;
				default: // Default that should not be reached.
					System.out.println("Unrecognized Message Received.");
				}
			}
		} catch (SocketException se){
			System.out.println("Client took too long to respond, connection may be lost, check internet connection.");
		}catch (IOException e) {
			System.out.println("Lost connection from the client. The current game is shut down");
		}
	}

}
