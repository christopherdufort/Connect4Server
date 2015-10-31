package controller;

import datacomm.MessageType;

/**
 * Server side game controller that will handle game flow on the server.
 * 
 * @author Christopher Dufort
 * @author Elliot Wu
 * @author Nader Baydoun
 */
public class ServerGameController 
{
	// Game board used as internal representation
	// [0] = no move - [1] = Client move - [2] = server move;
	private int[][] gameBoard;
	
	// false = client turn |OR| true = server turn
	private boolean clientsTurn;
	
	private AI ai;
	
	/**
	 * Constructor for the ServerGameController.
	 */
	public ServerGameController() 
	{
		this.gameBoard = new int[7][6];
		clientsTurn = true;
		ai = new AI();
	}
	
	/**
	 * Method that calls the AI to retrieve a valid move for the server to play.
	 * 
	 * @return byte[] array that the server played
	 */
	public byte[] aiMakeMove() 
	{
		byte[] b = ai.returnMove(gameBoard);
		byte[] aiMove = { MessageType.MOVE.getCode(), b[0], b[1] }; 
																													
		// AI LOGIC is aware of board.
		// AI LOGIC HERE ONLY MAKE VALID MOVES
		System.out.println("AI move: " + aiMove[1] + ", " + aiMove[2]);
		return aiMove;
	}

	/**
	 * Method that controls state of game.
	 * Whether to continue playing or an end game state has been achieved.
	 * 
	 * @param moveMade byte[] array of the move played, containing a column and a row 
	 * @return byte[] Return message of what the server has determined to be the next step of the game
	 */
	public byte[] gameLogic(byte[] moveMade) 
	{
		updateArray(moveMade[1], moveMade[2]);
		byte[] returnMessage = {0, 0, 0};
		switch(validateGameEnd(moveMade[1], moveMade[2], clientsTurn))
		{
			case 0:
				System.out.println("TIE");
				returnMessage[0] = MessageType.TIE.getCode();
				break;
			case 1:
				System.out.println("SERVERWIN");
				returnMessage[0] = MessageType.SERVER_WIN.getCode();
				break;
			case 2:
				System.out.println("USERWIN");
				returnMessage[0] = MessageType.USER_WIN.getCode();
				break;
			case 3:
				System.out.println("CONTINUEGAME");
				returnMessage = aiMakeMove();		
				updateArray(returnMessage[1], returnMessage[2]);
				break;
		}
		
		return returnMessage;
	}

	/**
	 * Returns a boolean that signals whether it's the server or client's turn.
	 * If true then server turn.
	 * 
	 * @return boolean that represents if it's the client's or the server's turn
	 */
	public boolean isclientsTurn() 
	{
		return clientsTurn;
	}

	/**
	 * Method that prints out the game board.
	 */
	public void displayBoard() 
	{
		System.out.println("current internal board ----- ");
		//TODO check loop logic
		for (int i = 5; i > -1; i--) 
		{
			for (int j = 0; j < 7; j++) 
			{
				System.out.print(this.gameBoard[j][i] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Method that checks if the game ends.
	 * 0->tie
	 * 1->server wins
	 * 2->client wins
	 * 3->game not ended
	 * FIXME call this from server move also
	 * 
	 * @param board
	 *            The full array of the board
	 * @param column
	 *            The row in which the latest move was played
	 * @param row
	 *            The column in which the latest move was played
	 * @param player
	 *            An integer representing if the move was played by the client
	 *            or the server
	 * @return A boolean value if there is a win condition
	 */
	public int validateGameEnd(int columnMove, int rowMove, boolean isServer) 
	{
		int player = 0;
		int ctr = 1;
		System.out.println("isServer: " + isServer);
		if(isServer)
			player = 1;
		else
			player = 2;
		
		/***********************************check horizontal line*******************************************/
		for(int column = columnMove - 1; column >= 0; column--){
			if(gameBoard[column][rowMove] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
		}
		for(int column = columnMove + 1; column < gameBoard.length; column++){
			if(gameBoard[column][rowMove] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
		}
		
		ctr = 1;
		/******************************************************************************/
		/*************************************check vertical line*****************************************/
		for(int row = rowMove - 1; row >= 0; row--){
			if(gameBoard[columnMove][row] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
		}
		for(int row = rowMove + 1; row < gameBoard[0].length; row++){
			if(gameBoard[columnMove][row] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
		}
		
		ctr = 1;
		/******************************************************************************/
		/*************************************check diagonal line(/)*****************************************/
		int diagonalColumn = columnMove + 1;
		int diagonalRow = rowMove + 1;
		while(diagonalColumn < gameBoard.length && diagonalRow < gameBoard[0].length){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
			diagonalColumn++;
			diagonalRow++;
		}
		diagonalColumn = columnMove - 1;
		diagonalRow = rowMove - 1;
		while(diagonalColumn >= 0 && diagonalRow >= 0){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
			diagonalColumn--;
			diagonalRow--;
		}
		
		ctr = 1;
		/******************************************************************************/
		/*************************************check diagonal line(\)*****************************************/
		diagonalColumn = columnMove + 1;
		diagonalRow = rowMove - 1;
		while(diagonalColumn < gameBoard.length && diagonalRow >= 0){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
			diagonalColumn++;
			diagonalRow--;
		}
		diagonalColumn = columnMove - 1;
		diagonalRow = rowMove + 1;
		while(diagonalColumn >= 0 && diagonalRow < gameBoard[0].length){
			if(gameBoard[diagonalColumn][diagonalRow] == player)
				ctr++;
			else
				break;
			if(ctr >= 4)
				return player;
			diagonalColumn--;
			diagonalRow++;
		}
		/******************************************************************************/
		
		
		//if no win, checks for tie
		boolean full = true;
		for(int column = 0; column < gameBoard.length; column++){
			if(gameBoard[column][5] == 0)
				full = false;
		}
		if(full)
			return 0;
		
		return 3;
	}
	
	/*
	 * Method that updates the server's internal game board.
	 * 
	 * @param column Column to be updated
	 * @param row Row to be updated
	 */
	private void updateArray(int column, int row) 
	{
		if (clientsTurn) 
		{
			this.gameBoard[column][row] = 2;
			clientsTurn = false;
		} 
		
		else 
		{
			//FIXME: Fatal crash that is causing a ArrayIndexOutOfBoundsException at this line
			//If you play the game in a certain way i think the AI is trying to place something in 
			//an invalid position
			this.gameBoard[column][row] = 1;
			clientsTurn = true;
		}
		
		displayBoard();
	}
	
}
