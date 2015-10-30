package controller;

import java.util.ArrayList;

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
	public byte[] returnMove(int[][] gameBoard) 
	{
		byte[] returner = new byte[2];
		int bestStrategy = -1;
		int bestRow = -1;
		ArrayList<Integer> columns = new ArrayList<Integer>();
		
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

}
