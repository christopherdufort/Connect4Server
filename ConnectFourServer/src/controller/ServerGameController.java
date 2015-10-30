package controller;

import datacomm.MessageType;

public class ServerGameController {
	// Game board used as internal representation
	// [0] = no move - [1] = Client move - [2] = server move;
	private int[][] gameBoard;
	// false = client turn |OR| true = server turn

	private byte[] returnMessage;
	private boolean clientsTurn;

	public ServerGameController() {
		this.gameBoard = new int[7][6];
		clientsTurn = true;
	}

	public byte[] findEmptyPos() {
		byte[] returner = new byte[2];

		for (int i = 0; i < 7; i++) {

			for (int ctr = 0; ctr < 6; ctr++) {
				
				if (gameBoard[i][ctr] == 0) {
					returner[0] = (byte) i;
					returner[1] = (byte) ctr;
					return returner;
				}
			}
		}

		return returner;
	}

	public byte[] aiMakeMove() {
		byte[] b = findEmptyPos();
		byte[] aiMove = {MessageType.MOVE.getCode(), b[0], b[1] }; // Delete this hard coded message
		// AI LOGIC is aware of board.
		// AI LOGIC HERE ONLY MAKE VALID MOVES
		updateArray(aiMove[1], aiMove[2]);
		System.out.println("AI move: " + aiMove[1] + ", " + aiMove[2]);
		return aiMove;
	}

	public byte[] gameLogic(byte[] moveMade) {
		updateArray(moveMade[1], moveMade[2]);

		if (checkIfGameIsOver() == 1) {
			if (clientsTurn) {
				// game is over message contains a client win (5.0.0)
				returnMessage = new byte[] { MessageType.USER_WIN.getCode(), 0, 0 };
			} else {
				// game is over message contains a server win (4.0.0)
				returnMessage = new byte[] { MessageType.SERVER_WIN.getCode(), 0, 0 };
			}

		} else if (checkIfGameIsOver() == 1) {
			// game is over message contains a tie (6.0.0)
			returnMessage = new byte[] { MessageType.TIE.getCode(), 0, 0 };
		} else {
			// game is not over ai makes a move and return that message
			returnMessage = aiMakeMove();
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

	public int checkIfGameIsOver() {
		// 0 = not over
		// 1 = win
		// 2 = tie? TODO do we want to do this or handle move count?
		int gameResult = 0;
		// Logic that checks if there is a winner
		return gameResult;
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
}
