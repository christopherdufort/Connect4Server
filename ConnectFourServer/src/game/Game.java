package game;

public class Game {
	//Game board used as internal representation
	//[0] = no move - [1] = Client move - [2] = server move;
    private int[][] board;
    //false = client turn |OR| true = server turn
    private boolean serverTurn = false; 
    
    public Game() {
    	this.board = new int[12][13];
    	this.serverTurn = false;
    }
    
    public byte[] aiMakeMove(){  	
    	byte[] aiMove = new byte[]{-1,-1};
    		//AI LOGIC is aware of board.
    	return aiMove;
    }
    
    public void updateArray(byte[] moveMade){
    	if (serverTurn)
    	{
    		this.board[moveMade[0]][moveMade[1]]= 2;
    	}
    	else 
    		this.board[moveMade[0]][moveMade[1]]= 1;
    	displayBoard();
    }
    
    public void updateTurn(){
    	if (serverTurn)
    		serverTurn = false;
    	else
    		serverTurn = true;
    }
    
    //true = server turn
    public boolean isServerTurn(){
    	return serverTurn;
    }
    
    public boolean checkIfGameIsOver(){
    	boolean gameResult = false;
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
    		System.out.print( " row count " + row);
    		System.out.println();
    	}
    }
}
