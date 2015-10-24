package business;

import java.io.IOException;
import java.net.Socket;

import datacomm.Network;
/*
 * First.Second.Third
 * 0.0.0	-	Let's play?
 * 1.0.0	-	Yes let's play
 * 
 * 
 * @author Christopher
 * @author Elliot
 * @author Nader
 * 
 */


public class C4ServerSession {

	private Socket clntSock;
	private boolean playSession;
	private boolean playGame;
	private byte[] message;
	
	public C4ServerSession(Socket clntSock) 
	{
		this.clntSock = clntSock;
		this.playSession = true;
		this.playGame = false;
	}
	public void startSession() throws IOException {		
		while(playSession)
		{
			message = Network.receiveMessage(clntSock);

			switch(message[0])
			{
				case 0:
					playGame = true;
					System.out.println("Game started");
					Network.sendMessage(clntSock, new byte[]{10,2,3});
					this.playGame();
					break;
				case 9:
					playSession = false;
					System.out.println("Game ended");
					break;
				default:
					System.out.println("default");
			}
		}
	}
	private void playGame(){
		//Game class initialization
		
		while(playGame)
		{
			//listen for stuff
			//do stuff
			// when the game logic decides the game over
			//playgame = false
			try{
				message = Network.receiveMessage(clntSock);
				if(message[0] == 1){
					System.out.println("Game resumed");
					//Network.sendMessage(clntSock, message);
				}
				else
					playGame = false;
			}catch(IOException e){
				playGame = false;
				playSession = false;
			}
		}
		
	}

}
