package controller;

public class AI 
{

	/**
	 * 
	 * 
	 * Find a way to win and end the match.
	 * Find a way to block the client if they have a near win (three slots in a row)
	 * Find a way to stack three slots
	 * Find a way to block the client from having staking three slots
	 * Find a way to try and build a stack (two slots)
	 * Play a random slot. 
	 * 
	 * @return
	 */
	public byte[] returnMove(int[][] gameBoard, byte[] moveMade) 
	{
		byte[] returner = new byte[2];
		
		int client = 2;
		int server = 1;
		
		//TODO: call determineStatterrtg
		
		return returner;
	}

	private int determineStrategy(int[][] gameBoard, byte[] moveMade, int player) 
	{
		int strategy = -1;
		
		int ctr = 0;
		
		/***********************************check horizontal line*******************************************/
		for(int column = moveMade[0] - 1; column >= 0; column--)
		{
			if(gameBoard[column][moveMade[1]] == player)
			{
				ctr++;
			}
			
			else
			{
				break;
			}
			
		}
		
		
		for(int column = moveMade[0] + 1; column < gameBoard.length; column++)
		{
			if(gameBoard[column][moveMade[1]] == player)
			{
				ctr++;
			}
			
			else
			{
				break;
			}
		}
		
		if(ctr == 3)
		{
			strategy = 10;
			
			//if(gameBoard[][])
			
			return strategy;
		}
		
		if(ctr == 2)
		{
			strategy = 9;
			return strategy;
		}
		
		if(ctr == 1)
		{
			strategy = 8;
			return strategy;
		}
		
		ctr = 0;
		/******************************************************************************/
		/*************************************check vertical line*****************************************/
		for(int row = moveMade[1] - 1; row >= 0; row--)
		{
			if(gameBoard[moveMade[0]][row] == player)
				ctr++;
			
			else
			{
				break;
			}
		}
		
		for(int row = moveMade[1] + 1; row < gameBoard[0].length; row++)
		{
			if(gameBoard[moveMade[0]][row] == player)
				ctr++;
			
			else
			{
				break;
			}
			
		}
		
		if(ctr == 3)
		{
			strategy = 10;
			return strategy;
		}
		
		if(ctr == 2)
		{
			strategy = 9;
			return strategy;
		}
		
		if(ctr == 1)
		{
			strategy = 8;
			return strategy;
		}
		
		ctr = 0;
		/******************************************************************************/
		/*************************************check diagonal line(/)*****************************************/
		int diagonalColumn = moveMade[0] + 1;
		int diagonalRow = moveMade[1] + 1;
		while(diagonalColumn < gameBoard.length && diagonalRow < gameBoard[0].length){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			diagonalColumn++;
			diagonalRow++;
		}
		diagonalColumn = moveMade[0] - 1;
		diagonalRow = moveMade[1] - 1;
		while(diagonalColumn >= 0 && diagonalRow >= 0){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			diagonalColumn--;
			diagonalRow--;
		}
		
		if(ctr == 3)
		{
			strategy = 10;
			return strategy;
		}
		
		if(ctr == 2)
		{
			strategy = 9;
			return strategy;
		}
		
		ctr = 0;
		/******************************************************************************/
		/*************************************check diagonal line(\)*****************************************/
		diagonalColumn = moveMade[0] + 1;
		diagonalRow = moveMade[1] - 1;
		while(diagonalColumn < gameBoard.length && diagonalRow >= 0){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			diagonalColumn++;
			diagonalRow--;
		}
		diagonalColumn = moveMade[0] - 1;
		diagonalRow = moveMade[1] + 1;
		while(diagonalColumn >= 0 && diagonalRow < gameBoard[0].length){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			diagonalColumn--;
			diagonalRow++;
		}
		
		if(ctr == 3)
		{
			strategy = 10;
			return strategy;
		}
		
		if(ctr == 2)
		{
			strategy = 9;
			return strategy;
		}
		
		if(ctr == 1)
		{
			strategy = 8;
			return strategy;
		}
		
		return 0;
	}

}
