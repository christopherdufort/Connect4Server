package controller;

import datacomm.MessageType;

public class ServerGameController {
	// Game board used as internal representation
	// [0] = no move - [1] = Client move - [2] = server move;
	private int[][] gameBoard;
	// false = client turn |OR| true = server turn
	private boolean clientsTurn;
	
	AI ai;
	
	/**
	 * Constructor
	 */
	public ServerGameController() {
		this.gameBoard = new int[7][6];
		clientsTurn = true;
		ai = new AI();
	}

	//FIXME THIS NEEDS REAL AI
	public byte[] findEmptyPos() {
		byte[] returner = new byte[2];

		/*
		for (int i = 0; i < 7; i++) {

			for (int ctr = 0; ctr < 6; ctr++) {

				if (gameBoard[i][ctr] == 0) {
					returner[0] = (byte) i;
					returner[1] = (byte) ctr;
					return returner;
				}
			}
		}
		*/
		
		returner = ai.returnMove(gameBoard);

		return returner;
	}

	public byte[] aiMakeMove() {
		byte[] b = findEmptyPos();
		byte[] aiMove = { MessageType.MOVE.getCode(), b[0], b[1] }; 
																													
		// AI LOGIC is aware of board.
		// AI LOGIC HERE ONLY MAKE VALID MOVES
		System.out.println("AI move: " + aiMove[1] + ", " + aiMove[2]);
		return aiMove;
	}

	public byte[] gameLogic(byte[] moveMade) {
		updateArray(moveMade[1], moveMade[2]);
		byte[] returnMessage = {0, 0, 0};
		switch(validateGameEnd(moveMade[1], moveMade[2], clientsTurn)){
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

	private void updateArray(int column, int row) {
		if (clientsTurn) {
			this.gameBoard[column][row] = 2;
			clientsTurn = false;
		} else {
			this.gameBoard[column][row] = 1;
			clientsTurn = true;
		}
		displayBoard(); // Delete this if we dont want?
	}

	// true = server turn
	public boolean isclientsTurn() {
		return clientsTurn;
	}

	public void displayBoard() {
		System.out.println("current internal board ----- ");
		// TODO check loop logic
		for (int i = 5; i > -1; i--) {
			for (int j = 0; j < 7; j++) {
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
	public int validateGameEnd(int columnMove, int rowMove, boolean isServer) {
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
}
