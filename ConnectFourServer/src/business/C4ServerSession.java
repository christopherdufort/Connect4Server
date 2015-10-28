package business;

import java.io.IOException;
import java.net.Socket;

import datacomm.Network;
import game.Game;
/*
 * First.Second.Third
 * 0.0.0	-	Let's play?
 * 1.0.0	-	Yes let's play
 * 2.0.0	-	Client Move [position column] , [position row]
 * 3.0.0	-	Server Move [position column] , [position row]
 * 4.0.0 	- 	Server Win
 * 5.0.0 	-	Client Win
 * 6.0.0	-	Tie
 * 
 * 9.0.0	-	GameOver Request from client.
 * 
 * @author Christopher
 * @author Elliot
 * @author Nader
 * 
 */

//FIXME handle connection reset

public class C4ServerSession {

	private Socket clntSock;
	private boolean playSession;
	private boolean playGame;
	private boolean clientsTurn;
	private byte[] message;
	private byte[] returnMessage;
	private Game myGame;
	
	public C4ServerSession(Socket clntSock) 
	{
		this.clntSock = clntSock;
		this.playSession = true;
		this.playGame = false;
		this.myGame = new Game();
		this.clientsTurn = true;
	}
	public void startSession() throws IOException {		
		while(playSession)
		{
			message = Network.receiveMessage(clntSock);
			System.out.println("Received Message was " + message[0]);
			switch(message[0])
			{
				case 0:
					playGame = true;
					System.out.println("Game started");
					Network.sendMessage(clntSock, new byte[]{1,0,0});
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
	private void playGame() throws IOException{
		//Game class initialization
		
		while(playGame)
		{
			System.out.println("Waiting for move...");
			message = Network.receiveMessage(clntSock);
			System.out.println("Received Message was " + message[0]);
				switch(message[0])
				{
					case 2: //Client move
						byte[] moveMade = new byte[]{message[1], message[2]};
						System.out.println("Client Move received.");
						returnMessage = myGame.gameLogic(clientsTurn, moveMade);
						break;
					case 9:
						System.out.println("request to end game received");
						playGame = false;
						break;
					default:
						System.out.println("default");
				}

		}
	}
}
