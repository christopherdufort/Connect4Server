package game;

public class Game {
	//Game board used as internal representation
	//[0] = no move - [1] = Client move - [2] = server move;
    private int[][] board;
    //false = client turn |OR| true = server turn
    
    private byte[] returnMessage;
	private boolean clientsTurn;
    
    public Game() {
    	this.board = new int[12][13];
    }
    
    public byte[] aiMakeMove(){  	
    	byte[] aiMove = new byte[]{2,0}; //Delete this hard coded message
    		//AI LOGIC is aware of board.
    	//AI LOGIC HERE ONLY MAKE VALID MOVES
    	this.clientsTurn = false; //its a server move now
    	updateArray(aiMove);
    	return aiMove;
    }
    
    public byte[] gameLogic(boolean clientsTurn, byte[] moveMade){
    	byte[] aiMove;
    	
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
    		aiMove = aiMakeMove();
    		returnMessage = new byte[]{3, aiMove[0] , aiMove[1]};
    	}
    	return returnMessage;
    	
    	
    }
    
    private void updateArray(byte[] moveMade) {
    	if (clientsTurn)
    	{
    		this.board[moveMade[0]][moveMade[1]]= 1;
    	}
    	else 
    	{
    		this.board[moveMade[0]][moveMade[1]]= 2;
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
