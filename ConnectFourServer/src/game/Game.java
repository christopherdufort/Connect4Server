package game;

import java.util.Random;

public class Game {
	//Game board used as internal representation
	//[0] = no move - [1] = Client move - [2] = server move;
    private int[][] board;
    //false = client turn |OR| true = server turn
    
    private byte[] returnMessage;
	private boolean clientsTurn;
    
    public Game() {
    	this.board = new int[6][7];
    }
    
    public byte[] findEmptyPos()
    {
		byte[] returner = new byte[2];	
		
		for(int i = 0; i < 6; i++)
		{		
			
			for(int ctr = 0; ctr < 7; ctr ++)
			{
				if (board[i][ctr] == 0)
				{
					returner[0] = (byte) i;
					returner[1] = (byte) ctr;
					return returner;
				}
			}
		}
		
		return returner;
    }
    
    public byte[] aiMakeMove()
    {  	
    	byte [] b = findEmptyPos();
    	byte[] aiMove = {0 , b[0], b[1]}; //Delete this hard coded message
    		//AI LOGIC is aware of board.
    	//AI LOGIC HERE ONLY MAKE VALID MOVES
    	this.clientsTurn = false; //its a server move now
    	updateArray(aiMove);
    	return aiMove;
    }
    
    public byte[] gameLogic(boolean clientsTurn, byte[] moveMade){
    	this.clientsTurn = clientsTurn;//this method is called by session
    	updateArray(moveMade);
    		
    	if (checkIfGameIsOver() == 1)
    	{
    		if (clientsTurn)
    		{
        		//game is over message contains a client win (5.0.0)
        		returnMessage = new byte[]{5,0,0};
    		}
    		else
    		{
        		//game is over message contains a server win (4.0.0)
        		returnMessage = new byte[]{4,0,0};
    		}

    	}
    	else if (checkIfGameIsOver() == 1)
    	{
    		//game is over message contains a tie (6.0.0)
    		returnMessage = new byte[]{6,0,0};
    	}
    	else
    	{
    		//game is not over ai makes a move and return that message
    		returnMessage = aiMakeMove();
    	}
    	return returnMessage;
    }
    
    private void updateArray(byte[] moveMade) {
    	if (clientsTurn)
    	{
    		this.board[moveMade[0]][moveMade[1]]= 2;
    	}
    	else 
    	{
    		this.board[moveMade[0]][moveMade[1]]= 1;
    	}
    	displayBoard(); //Delete this if we dont want?
	}

	public void updateTurn(){
    	if (clientsTurn)
    		clientsTurn = false;
    	else
    		clientsTurn = true;
    }
    
    //true = server turn
    public boolean isclientsTurn(){
    	return clientsTurn;
    }
    
    public int checkIfGameIsOver(){
    	//0 = not over
    	//1 = win
    	//2 = tie? TODO do we want to do this or handle move count?
    	int gameResult = 0;
    		//Logic that checks if there is a winner
		return gameResult;
    }
    
    public void displayBoard(){
    	System.out.println("current internal board ----- ");
    	//TODO check loop logic 
    	for(int row = 0; row < board[0].length - 1; row ++)
    	{	
    		for(int column = 0; column < board[1].length; column ++)
    		{
    			System.out.print(board[row][column] + "  ");
    		}
    		System.out.println();
    	}
    }
}
