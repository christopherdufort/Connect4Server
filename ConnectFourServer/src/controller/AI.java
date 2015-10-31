package controller;

import java.util.ArrayList;

public class AI 
{
	/**
	 * Main AI interface method that returns the 
	 * position at which the server should play it's move.
	 * 
	 * @param gameBoard The game board in it's current state
	 * @return byte[] Array that contains the column and row of the appropriate move
	 */
	public byte[] returnMove(int[][] gameBoard) 
	{
		byte[] returner = new byte[2];
		int bestStrategy = -1;
		int bestRow = -1;
		ArrayList<Integer> columns = new ArrayList<Integer>();
		
		displayBoard(gameBoard);
		
		for(int i = 0; i < 7; i++)
		{
			columns.add(findEmptyPos(gameBoard, i));
		}
		
		for(int i = 0; i < 7; i++)
		{
			if(columns.get(i) > bestStrategy)
			{
				bestStrategy = i;
			}
		}
		
		for (int row = 0; row < 6; row++) 
		{
			System.out.println("column: " + bestStrategy + " row: " + row);
			
			if (gameBoard[bestStrategy][row] == 0) 
			{
				bestRow = row;
				break;
			} 
		}
		
		returner[0] = (byte) bestStrategy;
		returner[1] = (byte) bestRow;
		
		return returner;
	}
	
	//TODO: Keep this comment?
	/**
	 * Method that given a certain column and the game board
	 * will determine what possible move can be played at each column.
	 * The method is based on a point system, if a column has a very 
	 * desirable state, such as a potential win condition, or a condition
	 * to block it will assign a higher number of points to it.
	 * 
	 * The AI logic mainly works by just observing the columns 
	 * which is a potential flaw since the client can easily out-smart the AI
	 * by connecting four horizontally or diagonally.
	 * 
	 * 100 -> Potential four in a row and a win
	 * 90 -> Potential to block a user win
	 * 80 -> Potential to build a three in a row for the server
	 * 70 -> Potential to block three in a row for the client
	 * 50 -> Potential to block the user from getting a two in a row 
	 * 
	 * @param gameBoard Current state of the game board
	 * @param column Column that is being analyzed for potential moves
	 * @return int That holds a certain number based on a point system
	 */
	public int findEmptyPos(int[][] gameBoard, int column) 
	{
		int result = -1;
		int serverCtr = 0;
		int clientCtr = 0;
		int serverLastPos = -1;
		int clientLastPos = -1;
		
		for (int row = 0; row < 6; row++) 
		{
			if (gameBoard[column][row] == 1) 
			{
				serverCtr++;
				serverLastPos = row;
			} 
			
			else if(gameBoard[column][row] == 2)
			{
				clientCtr++;
				clientLastPos = row;
			}
		}
		
		if(serverCtr == 3)
		{
			if(serverLastPos != 5)
			{
				return 100;
			}
		}
		
		if(clientCtr == 3)
		{
			if(clientLastPos != 5)
			{
				return 90;
			}
		} 
		
		if(serverCtr == 2)
		{
			if(serverLastPos != 5)
			{
				return 80;
			}
		}
		
		if(clientCtr == 2)
		{
			if(clientLastPos != 5)
			{
				return 70;
			}
		} 
		
		if(clientCtr == 1)
		{
			if(clientLastPos != 5)
			{
				return 50;
			}
		} 
		
		return result;
	}

	/*
	 * Method that prints out a text version of the board.
	 * 
	 * @param gameBoard Game board
	 */
	private void displayBoard(int[][] gameBoard) 
	{
		for (int i = 5; i > -1; i--) 
		{
			for (int j = 0; j < 7; j++) 
			{
				System.out.print(gameBoard[j][i] + " ");
			}
			
			System.out.println();
		}
	}
}
