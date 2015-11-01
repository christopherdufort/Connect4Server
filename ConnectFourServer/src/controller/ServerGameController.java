package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

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
	
	/**
	 * Constructor for the ServerGameController.
	 */
	public ServerGameController() 
	{
		this.gameBoard = new int[7][6];
		clientsTurn = true;
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
		byte[] returnMessage = {-10, 0, 0};
		switch(validateGameEnd(moveMade[1], moveMade[2], clientsTurn))
		{
			case 0:
				System.out.println("TIE");
				returnMessage[0] = MessageType.TIE.getCode();
				break;
			case 1:
				//never reached
				System.out.println("SERVERWIN");
				returnMessage[0] = MessageType.SERVER_WIN.getCode();
				break;
			case 2:
				System.out.println("USERWIN");
				returnMessage[0] = MessageType.USER_WIN.getCode();
				break;
			case 3:
				System.out.println("CONTINUEGAME");
				returnMessage = easyAI();
				updateArray(returnMessage[1], returnMessage[2]);
				break;
		}
		
		return returnMessage;
	}

	/**
	 * Method that prints out the game board.
	 */
	private void displayBoard() 
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
	 * 
	 * @param column
	 *            The row in which the latest move was played
	 * @param row
	 *            The column in which the latest move was played
	 * @param player
	 *            A boolean that indicate who is making the move
	 * @return A boolean value if there is a win condition
	 */
	private int validateGameEnd(int columnMove, int rowMove, boolean isServer) 
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
	
	/**
	 * Method that will determine if a column has any more empty rows left by searching the gameBoard array
	 * 
	 * @param column to be checked for empty rows
	 * @return int either the first empty row in the column or a -1 if column is full
	 */
	private int findEmptyPosition(int column) 
	{

		int result = -1;

		for (int row = 0; row < 6; row++) 
		{
			//Checks if row is empty
			if (gameBoard[column][row] == 0) 
			{
				result = row;
				return result;
			}
		}
		return result;
	}
	
	/**
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
			this.gameBoard[column][row] = 1;
			clientsTurn = true;
		}
		
		displayBoard();
	}
	
	/**
	 * Method that calls the AI to retrieve a valid move for the server to play.
	 * 
	 * @return byte[] array that the server played
	 */
	private byte[] easyAI() 
	{
		ArrayList<byte[]> socreList = new ArrayList<>();
		int column, row;
		byte[] eachMove = new byte[3];
		Random ran = new Random();
		int randomResult;
		for(int i = 0; i < gameBoard.length; i++){
			column = i;
			row = findEmptyPosition(column);
			//if there is a spot left on this column
			if(row != -1){
				eachMove[1] = (byte)column;
				eachMove[2] = (byte)row;
				gameBoard[column][row] = 2;
				switch(validateGameEnd(column ,row, false)){
					//tie
					case 0:
						System.out.println("----------Tie!");
						eachMove[0] = (byte)2;
					//client win
					case 2:
						System.out.println("----------going to win:Client!");
						eachMove[0] = (byte)3;
						break;
					//game not end
					case 3:
						System.out.println("----------going to win:No win!");
						eachMove[0] = (byte)1;
						System.out.println("Column: "+eachMove[1]+" Row: "+eachMove[2]);
				}
	
				gameBoard[column][row] = 1;
				switch(validateGameEnd(column ,row, true)){
					//server win
					case 1:
						System.out.println("----------going to win:Server!");
						eachMove[0] = (byte)4;
						break;
				}
				
				gameBoard[column][row] = 0;
				socreList.add(new byte[]{eachMove[0], eachMove[1], eachMove[2]});
			}
		}
		
		//sort the list according to the scores
		Collections.sort(socreList, new Comparator<byte[]>() {
		    public int compare(byte[] a, byte[] b) {
		        return Byte.compare(a[0], b[0]);
		    }
		});

		//just to display the score list
		for(byte[] item : socreList){
			for(int i = 0; i < 3; i++)
				System.out.print(item[i]);
			System.out.println();
		}

		//if the highest score is 1, 
		//this move is not going to end the game
		//send a message with MOVE
		//randomly pick a move
		int lastIndex = socreList.size() - 1;
		if(socreList.get(lastIndex)[0] == 1){
			randomResult = ran.nextInt(lastIndex + 1);
			socreList.get(randomResult)[0] = MessageType.MOVE.getCode();
			return socreList.get(randomResult);
		}
		//if the highest score is 2 or 3, 
		//this move is either a tie or is needed to block the user
		//send a message with MOVE
		else if(socreList.get(lastIndex)[0] == 2 || socreList.get(lastIndex)[0] == 3){
			socreList.get(lastIndex)[0] = MessageType.MOVE.getCode();
			return socreList.get(lastIndex);
		}
		//if the highest score is 4, 
		//this is a winning move for the server
		//send a message with SERVERWIN
		else{
			socreList.get(lastIndex)[0] = MessageType.SERVER_WIN.getCode();
			return socreList.get(lastIndex);
		}
		
	}
	
}
