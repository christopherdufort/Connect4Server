package business;

import java.io.IOException;
import java.net.Socket;

import controller.ServerGameController;
import datacomm.MessageType;
import datacomm.Network;

//FIXME handle connection reset

public class C4ServerSession {

	private Socket clntSock;
	private boolean playSession;
	private boolean playGame;
	private byte[] message;
	private ServerGameController myGame;
	
	public C4ServerSession(Socket clntSock) 
	{
		this.clntSock = clntSock;
		this.playSession = true;
		this.playGame = false;
		this.myGame = new ServerGameController();
	}
	public void startSession(){		
		try{
			while(playSession)
			{
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
	private void playGame(){
		//Game class initialization
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
					case END_GAME:
						System.out.println("request to end game received");
						playGame = false;
						System.out.println("Game ended");
						break;
					default:
						System.out.println("default");
				}
	
		}
		}catch(IOException e){
			System.out.println("The user shut down the game.");
		}
	}
}
