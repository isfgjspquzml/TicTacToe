package com.example.tictactoe;

public abstract class ComputerPlayer {

	/**
	 * Method of determining where a computer should play next
	 * 
	 * @param board
	 *            int[][] used to see what's already been played
	 * @param xHasTurn
	 *            boolean used to see whether the computer is playing as X or O
	 * @return int[] array with 3 values, the first two values in the array
	 *         correspond to X and Y coordinate of the computer and the last
	 *         value corresponds to the Button id. Button id's are arranged from
	 *         1 to 9 going from left to right, top to bottom so with an x and y
	 *         value finding the id isn't a problem
	 */
	public abstract int[] play(int[][] board, boolean xHasTurn);
}
