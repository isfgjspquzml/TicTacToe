package com.example.tictactoe;

import junit.framework.Assert;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Basic Tic Tac Toe application
 * 
 * @author Tianyu Shi
 */
public class MainActivity extends Activity {

	// These variables won't change but I didn't want to have magic numbers
	public static final int X_VAL = 1;
	public static final int O_VAL = -1;
	public static final int BOARD_LENGTH = 3;

	// Game state variables
	private int[][] board;
	private int numMoves;
	private boolean xHasTurn;
	private boolean tapsEnabled = false;
	private boolean computerEnabled;
	private boolean computerTurn;
	private boolean gameFinished;
	private ComputerPlayer computer;

	// Game directions & status text
	private TextView status;
	private TextView cpuStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setup();
		reset();
	}

	/**
	 * Add onClick listeners to buttons on screen
	 */
	private void setup() {
		// Store TextView for faster access
		status = (TextView) findViewById(R.id.status);
		cpuStatus = (TextView) findViewById(R.id.cpu);

		// Initialize onClick listeners for the table
		TableLayout boardButtons = (TableLayout) findViewById(R.id.board);
		for (int y = 0; y < boardButtons.getChildCount(); y++) {
			if (boardButtons.getChildAt(y) instanceof TableRow) {
				TableRow boardRow = (TableRow) boardButtons.getChildAt(y);
				for (int x = 0; x < boardRow.getChildCount(); x++) {
					View view = boardRow.getChildAt(x);
					view.setOnClickListener(new tapBoard(x, y));
				}
			}
		}

		// Initialize other buttons
		Button newGame = (Button) findViewById(R.id.newgame);
		Button onePlayer = (Button) findViewById(R.id.one_player);
		Button twoPlayers = (Button) findViewById(R.id.two_players);
		Button easy = (Button) findViewById(R.id.easy);
		Button hard = (Button) findViewById(R.id.hard);

		newGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				reset();
			}
		});

		onePlayer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (gameFinished) {
					status.setText(R.string.start_new_game);
				} else {
					computerEnabled = true;
					status.setText(R.string.select_difficulty);
				}
			}
		});

		twoPlayers.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (gameFinished) {
					status.setText(R.string.start_new_game);
				} else {
					computerEnabled = false;
					status.setText(R.string.tap_to_start);
					tapsEnabled = true;
				}
			}
		});

		easy.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (gameFinished) {
					status.setText(R.string.start_new_game);
				} else {
					if (computerEnabled) {
						computer = new EasyComputerPlayer();
						status.setText(R.string.tap_to_start);
						cpuStatus.setText(R.string.easy_mode);
						tapsEnabled = true;
					}
				}
			}
		});

		hard.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (gameFinished) {
					status.setText(R.string.start_new_game);
				} else {
					if (computerEnabled) {
						computer = new HardComputerPlayer();
						status.setText(R.string.tap_to_start);
						cpuStatus.setText(R.string.hard_mode);
						tapsEnabled = true;
					}
				}
			}
		});
	}

	/**
	 * Reset the game state
	 */
	private void reset() {
		board = new int[BOARD_LENGTH][BOARD_LENGTH]; // Setup new board
		xHasTurn = true; // X goes first
		numMoves = 0;
		tapsEnabled = false;
		gameFinished = false;
		status.setText(R.string.select_num_players);
		computer = null;
		cpuStatus.setText("");

		// Reset button text
		TableLayout boardButtons = (TableLayout) findViewById(R.id.board);
		for (int y = 0; y < boardButtons.getChildCount(); y++) {
			if (boardButtons.getChildAt(y) instanceof TableRow) {
				TableRow boardRow = (TableRow) boardButtons.getChildAt(y);
				for (int x = 0; x < boardRow.getChildCount(); x++) {
					View view = boardRow.getChildAt(x);
					Assert.assertTrue("View incorrectly assigned",
							view instanceof Button);
					((Button) view).setText("");
				}
			}
		}
	}

	/**
	 * Class to implement taps on the Tic Tac Toe board
	 * 
	 * @author tianyushi
	 */
	private class tapBoard implements View.OnClickListener {
		int x;
		int y;

		public tapBoard(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public void onClick(View v) {
			computerTurn = true;
			makeMove(x, y, v);
		}
	}

	/**
	 * 1) Make changes to the game state after a move is played. 
	 * 2) Check if the move played ended the game.
	 * 
	 * @param x
	 *            int x-location of the TicTacToe board of move to be played
	 * @param y
	 *            int y-location of the TicTacToe board of move to be played
	 * @param v
	 *            view whose text value is to be modified
	 */
	private void makeMove(int x, int y, View v) {
		if (board[x][y] == 0 && tapsEnabled) {
			numMoves++; // Faster to check if 9 moves are reached than to iterate through board[][]

			Assert.assertTrue(getString(R.string.not_button_view),
					v instanceof Button);
			Button b = (Button) v;

			if (xHasTurn) {
				board[x][y] = X_VAL;
				b.setText("X");
			} else {
				board[x][y] = O_VAL;
				b.setText("O");
			}

			xHasTurn = !xHasTurn;

			if (checkForWinner(x, y)) {
				int winner = xHasTurn ? R.string.o_wins : R.string.x_wins;
				status.setText(winner);
				gameFinished = true;
				tapsEnabled = false;
				return;
			}

			if (numMoves >= 9) {
				status.setText(R.string.tie);
				gameFinished = true;
				tapsEnabled = false;
				return;
			}

			if (computerEnabled && computer != null) {
				if (computerTurn) {
					int[] move = computer.play(board, xHasTurn);
					String id = "button_" + move[2]; // Computer.play() is documented in ComputerPlayer class
					int resID = getResources().getIdentifier(id,
						    "id", getPackageName());
					View cpuView  = findViewById(resID); 
					computerTurn = false;
					makeMove(move[0], move[1], cpuView);
				} 
			}
		}
	}

	/**
	 * Check if the move played connected three in a row
	 * 
	 * @param x
	 *            int x-location of move played
	 * @param y
	 *            int y-location of move played
	 */
	private boolean checkForWinner(int x, int y) {
		int rowSum = 0;
		int colSum = 0;

		// With such a small board size, simply check both diagonals
		int bottomLeftToTopRightSum = 0;
		int topLeftToBottomRightSum = 0;

		for (int i = 0; i < BOARD_LENGTH; i++) {
			rowSum += board[x][i];
			colSum += board[i][y];
			bottomLeftToTopRightSum += board[i][2 - i];
			topLeftToBottomRightSum += board[i][i];
		}

		// X_VAL = 1 and O_VAL = -1,
		// 3 in a row would sum to +- 3
		if (Math.abs(rowSum) == 3 || Math.abs(colSum) == 3
				|| Math.abs(bottomLeftToTopRightSum) == 3
				|| Math.abs(topLeftToBottomRightSum) == 3) {
			return true;
		}

		return false;
	}
}
