package business;

import java.io.IOException;
import java.net.Socket;

import controller.ServerGameController;
import datacomm.MessageType;
import datacomm.Network;

/**
 * C4ServerSession class is created for a single connection between the server and a client for the purpose of playing a game of connect four.
 * Each session will communicate with an instance of the ServerGameController a class containing all the rules and methods used for the server to play a game.
 * Sessions are responsible for deciphering messages from the client and reacting accordingly, and sending response messages to client.
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 * 
 * @version Java 1.8
 *
 */
public class C4ServerSession {

	private Socket clntSock;
	private boolean playSession;
	private boolean playGame;
	private byte[] message;
	private ServerGameController myGame;
	
	/**
	 * Constructor for the C4ServerSession sets default values and creates instance of serverGameController.
	 *
	 * @param clntSock
	 * 			Socket used to communicate with specific client within this session.
	 */
	public C4ServerSession(Socket clntSock) 
	{
		this.clntSock = clntSock;
		this.playSession = true;
		this.playGame = false;
		this.myGame = new ServerGameController();
	}
	/**
	 * Session responsible for receiving initial messages from client.
	 * Options include beginning a game or ending the session.
	 */
	public void startSession(){		
		try{
			while(playSession)
			{
				//receive initial message from client
				message = Network.receiveMessage(clntSock);
				System.out.println("Received Message was " + message[0]);
				
				switch(MessageType.values()[message[0]])
				{
					case NEW_GAME:
						playGame = true;						
						System.out.println("Game started");
						Network.sendMessage(clntSock, new byte[]{MessageType.NEW_GAME.getCode(), 0, 0});
						this.playGame();
						break;
					case END_SESSION:
						playSession = false;
						System.out.println("Session ended");
						break;
					default:
						System.out.println("default");
				}
			}
		}catch(IOException e){
			System.out.println("Session ended.");
		}
	}
	/**
	 * playGame method is called when the client sends a message requesting to begin a game with the server.
	 * While playing a game this method will maintain a packet dance with the client while making use of the game class.
	 */
	private void playGame(){
		try{
			while(playGame)
			{
				System.out.println("Waiting for move...");
				message = Network.receiveMessage(clntSock);
				System.out.println("Received Message was " + message[0]);
				switch(MessageType.fromValue(message[0]))
				{
					case MOVE: //Client move
						System.out.println("Client Move received.");
						Network.sendMessage(clntSock, myGame.gameLogic(message));
						break;
					case END_GAME: //Client Requested to end the game.
						System.out.println("Request to end game received");
						playGame = false;
						System.out.println("Game ended");
						break;
					case NEW_GAME: //Client requested to restart/begin a new game.
						gameReset();
						playGame = true;	
						System.out.println("Game started");
						Network.sendMessage(clntSock, new byte[]{MessageType.NEW_GAME.getCode(), 0, 0});
						this.playGame();
						break;
					default: //Default that should not be reached.
						System.out.println("Unrecognized Message Received.");
				}
		}
		}catch(IOException e){
			System.out.println("The user shut down the game.");
		}
	}
	
	/**
	 * This method is called when the client wishes to restart the current game.
	 * This method will create and set a new instance of the game controller.
	 */
	private void gameReset() 
	{
		this.myGame = new ServerGameController();
	}
}
