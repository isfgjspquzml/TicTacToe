package com.example.tictactoe;


public class EasyComputerPlayer extends ComputerPlayer {

	/**
	 * Selects the first non-X/O square
	 */
	@Override
	public int[] play(int[][] board, boolean xHasTurn) {
		int length = MainActivity.BOARD_LENGTH;
		int[] returnCoordinatesAndId = new int[3];

		for (int y = 0; y < length; y++) {
			for (int x = 0; x < length; x++) {
				if (board[x][y] == 0) {
					returnCoordinatesAndId[0] = x;
					returnCoordinatesAndId[1] = y;
					returnCoordinatesAndId[2] = 3 * y + x + 1;
					return returnCoordinatesAndId;
				}
			}
		}
		return null;
	}
}
