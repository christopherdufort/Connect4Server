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
					this.playGame();
					break;
				case 9:
					playSession = false;
					
					break;
				default:
			}
			//case
			//if the client sends lets play 0.0.0
			//respond to lets play set playGame to true
			//if they send a 1.1.1
			//playsession = false;

			

		}
		
	}
	private void playGame() {
		while(playGame)
		{
			//listen for stuff
			//do stuff
			// when the game logic decides the game over
			//playgame = false
		}
		
	}

}
