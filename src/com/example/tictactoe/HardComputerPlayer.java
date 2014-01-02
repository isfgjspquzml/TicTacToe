package com.example.tictactoe;

public class HardComputerPlayer extends ComputerPlayer {

	/**
	 * "Hard" computer player is still very easy to beat but has some simple objectives
	 * 
	 * 1) For 2 in a row, go for the 3 in a row
	 * 2) If opponent has 2 in a row, block
	 * 3) Otherwise, take first available non-X, non-O square
	 */
	@Override
	public int[] play(int[][] board, boolean xHasTurn) {
		int[] returnCoordinatesAndId = new int[3];
		int length = MainActivity.BOARD_LENGTH;
		
		// Default return value will be first avaliable non-X, non-O square
		OUTERMOST: for (int y = 0; y < length; y++) {
			for (int x = 0; x < length; x++) {
				if (board[x][y] == 0) {
					returnCoordinatesAndId[0] = x;
					returnCoordinatesAndId[1] = y;
					returnCoordinatesAndId[2] = 3 * y + x + 1;
					break OUTERMOST;
				}
			}
		}
		
		// Check all possible 3 in a rows for 2 in a row
		int firstColumn = 0;
		int secondColumn = 0;
		int thirdColumn = 0;
		
		int firstRow = 0;
		int secondRow = 0;
		int thirdRow = 0;
		
		int bottomLeftToTopRightDiag = 0;
		int topLeftToBottomRightDiag = 0;
		
		for (int i = 0; i < length; i++) {
			firstColumn += board[0][i];
			secondColumn += board[1][i];
			thirdColumn += board[2][i];
			
			firstRow += board[i][0];
			secondRow += board[i][1];
			thirdRow += board[i][2];
			
			bottomLeftToTopRightDiag += board[i][2 - i];
			topLeftToBottomRightDiag += board[i][i];
		}

		int[] columns = { firstColumn, secondColumn, thirdColumn };
		int[] rows = { firstRow, secondRow, thirdRow };
		int[] diagonals = { bottomLeftToTopRightDiag, topLeftToBottomRightDiag };

		// Objective 1: Look for 2 in a row and go for 3 in a row
		for (int i = 0; i < columns.length; i++) {
			
			// If there are 2 X's in a column and it's X's turn
			// OR if there are 2 O's in a column and it's O's turn
			// Return that coordinate to win!
			if((columns[i] == 2 && xHasTurn) ||
					(columns[i] == -2 && !xHasTurn)) {
				return getEmptyPositionInColumn(board, i, returnCoordinatesAndId);
			}
		
			// If there are 2 X's in a row and it's X's turn
			// OR if there are 2 O's in a row and it's O's turn
			// Return that coordinate to win!
			if((rows[i] == 2 && xHasTurn) ||
					(rows[i] == -2 && !xHasTurn)) {
				return getEmptyPositionInRow(board, i, returnCoordinatesAndId);
			}
			
			// If there are 2 X's in a column and it's O's turn
			// OR if there are 2 O's in a column and it's X's turn
			// store it in case O or X cannot win on this turn
			if((columns[i] == 2 && !xHasTurn) ||
					(columns[i] == -2 && xHasTurn)) {
				getEmptyPositionInColumn(board, i, returnCoordinatesAndId); 
			}
			
			// If there are 2 X's in a row and it's O's turn
			// OR if there are 2 O's in a row and it's X's turn
			// store it in case O or X cannot win on this turn
			if((rows[i] == 2 && xHasTurn) ||
					(rows[i] == -2 && !xHasTurn)) {
				getEmptyPositionInRow(board, i, returnCoordinatesAndId);
			}
		}
		
		// If there are 2 X's in the bottom to top diagonal and it's X's turn
		// OR if there are 2 O's in the bottom to top diagonal and it's O's turn
		// Return that coordinate to win!
		if ((bottomLeftToTopRightDiag == 2 && xHasTurn) ||
				(bottomLeftToTopRightDiag == -2 && !xHasTurn)) {
			return getEmptyPositionInDiagonalBT(board, returnCoordinatesAndId);
		}
		
		// If there are 2 X's in the top to bottom diagonal and it's X's turn
		// OR if there are 2 O's in the top to bottom diagonal and it's O's turn
		// Return that coordinate to win!
		if ((topLeftToBottomRightDiag == 2 && xHasTurn) ||
				(topLeftToBottomRightDiag == -2 && !xHasTurn)) {
			return getEmptyPositionInDiagonalTB(board, returnCoordinatesAndId);
		}
		
		// If there are 2 X's in the bottom to top diagonal and it's O's turn
		// OR if there are 2 O's in the bottom to top diagonal and it's X's turn
		if ((bottomLeftToTopRightDiag == 2 && !xHasTurn) ||
				(bottomLeftToTopRightDiag == -2 && xHasTurn)) {
			return getEmptyPositionInDiagonalBT(board, returnCoordinatesAndId);
		}
		
		// If there are 2 X's in the top to bottom diagonal and it's O's turn
		// OR if there are 2 O's in the top to bottom diagonal and it's X's turn
		if ((topLeftToBottomRightDiag == 2 && !xHasTurn) ||
				(topLeftToBottomRightDiag == -2 && xHasTurn)) {
			return getEmptyPositionInDiagonalTB(board, returnCoordinatesAndId);
		}
		
		return returnCoordinatesAndId;
	}
	
	public int[] getEmptyPositionInColumn(int[][] board, int column,
			int[] returnCoordinatesAndId) {
		for (int i = 0; i < MainActivity.BOARD_LENGTH; i++) {
			if (board[column][i] == 0) {
				returnCoordinatesAndId[0] = column;
				returnCoordinatesAndId[1] = i;
				returnCoordinatesAndId[2] = 3 * i + column + 1;
				return returnCoordinatesAndId;
			}
		}

		return null;
	}
	
	public int[] getEmptyPositionInRow(int[][] board, int row,
			int[] returnCoordinatesAndId) {
		for (int i = 0; i < MainActivity.BOARD_LENGTH; i++) {
			if (board[i][row] == 0) {
				returnCoordinatesAndId[0] = i;
				returnCoordinatesAndId[1] = row;
				returnCoordinatesAndId[2] = 3 * row + i + 1;
				return returnCoordinatesAndId;
			}
		}
		
		return null;
	}
	
	// Diagonal bottom to top
	public int[] getEmptyPositionInDiagonalBT(int[][] board,
			int[] returnCoordinatesAndId) {
		for (int i = 0; i < MainActivity.BOARD_LENGTH; i++) {
			if (board[2 - i][i] == 0) {
				returnCoordinatesAndId[0] = 2 - i;
				returnCoordinatesAndId[1] = i;
				returnCoordinatesAndId[2] = 3 * i + (2 - i) + 1;
				return returnCoordinatesAndId;
			}
		}
		
		return null;
	}
	
	// Diagonal top to bottom
	public int[] getEmptyPositionInDiagonalTB(int[][] board,
			int[] returnCoordinatesAndId) {
		for (int i = 0; i < MainActivity.BOARD_LENGTH; i++) {
			if (board[i][i] == 0) {
				returnCoordinatesAndId[0] = i;
				returnCoordinatesAndId[1] = i;
				returnCoordinatesAndId[2] = 3 * i + i + 1;
				return returnCoordinatesAndId;
			}
		}
		
		return null;
	}
}
