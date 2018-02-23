package ku.util;

import java.util.*;

/** The three-in-a-row game for two human players. */
public class TicTacToe {

	/**
	 * Draws the state of board, including instructions or a game over message.
	 */
	public static void draw(char[][] board, char player) {
		StdDraw.clear();
		StdDraw.line(0.5, -0.5, 0.5, 2.5);
		StdDraw.line(1.5, -0.5, 1.5, 2.5);
		StdDraw.line(-0.5, 0.5, 2.5, 0.5);
		StdDraw.line(-0.5, 1.5, 2.5, 1.5);
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				StdDraw.text(x, y, board[x][y] + "");
			}
		}
		StdDraw.text(1.0, -0.5, player + " to play. Click in a square.");
		StdDraw.show(0);
	}

	/** Returns true if the game is over. */
	public static boolean gameOver(char[][] board) {
		return isFull(board);
	}

	/**
	 * Handles a mouse click, placing player's mark in the square on which the
	 * user clicks.
	 */
	public static void handleMouseClick(char[][] board, char player) {
		while (!StdDraw.mousePressed()) {
			// Wait for mouse press
		}
		double x = Math.round(StdDraw.mouseX());
		double y = Math.round(StdDraw.mouseY());
		while (StdDraw.mousePressed()) {
			// Wait for mouse release
		}
		int a = (int) x;
		int b = (int) y;
		if (board[a][b] == ' ') {
			board[a][b] = player;
		}
	}

	/** Returns true if board is full. */
	public static boolean isFull(char[][] board) {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				if (board[x][y] == ' ') {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean hasWon(char[][] board) {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
				return true;
			}
			if (board[0][i] == board[1][i] && board[2][i] == board[1][i] && board[0][i] != ' ') {
				return true;
			}
		}
		if (board[0][2] == board[1][1] && board[2][0] == board[1][1] && board[1][1] != ' ') {
			return true;
		}
		if (board[0][0] == board[1][1] && board[2][2] == board[1][1] && board[1][1] != ' ') {
			return true;
		}
		return false;
	}

	public static List<int[]> findEmpty(char[][] board) {
		List<int[]> empty = new ArrayList<int[]>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == ' ') {
					int[] keep = { i, j };
					empty.add(keep);
				}
			}
		}
		return empty;
	}

	public static int random(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static Best chooseMove(char[][] board, char side) {
		Best myBest = new Best();
		Best reply;

		if (side == 'X') {
			myBest.score = -1;
		} else {
			myBest.score = 1;
		}
		if (isFull(board) && !hasWon(board)) {
			myBest.score = 0;
			return myBest;
		}
		if (isFull(board) || hasWon(board))
			return myBest;
		List<int[]> list = findEmpty(board);
		int ran;
		if (list.size() != 0) {
			ran = random(0, list.size() - 1);
		} else {
			ran = 0;
		}
		if (list != null) {
			myBest.move = list.get(ran);
		}
		for (int[] arr : list) {
			int x = arr[0];
			int y = arr[1];
			board[x][y] = side;
			reply = chooseMove(board, opposite(side));
			board[x][y] = ' ';
			if ((side == 'X' && reply.score > myBest.score) || (side == 'O' && reply.score < myBest.score)) {
				myBest.move = arr;
				myBest.score = reply.score;
			}
		}
		return myBest;
	}

	/** Plays the game. */
	public static void main(String[] args) {
		char[][] board = new char[3][3];
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board[x].length; y++) {
				board[x][y] = ' ';
			}
		}
		StdDraw.setXscale(-0.5, 2.5);
		StdDraw.setYscale(-0.5, 2.5);
		char currentPlayer = 'X';
		Random rand = new Random();
		int x = rand.nextInt(2) + 0;
		int y = rand.nextInt(2) + 0;
		board[x][y] = 'O';
		draw(board, currentPlayer);
		while (!gameOver(board)) {
			if (currentPlayer == 'X') {
				Best best = chooseMove(board, currentPlayer);
				if (best.move[0] != -1 && best.move[1] != -1) {
					board[best.move[0]][best.move[1]] = currentPlayer;
				}
			}
			else {
				handleMouseClick(board, currentPlayer);
			}
			currentPlayer = opposite(currentPlayer);
			draw(board, currentPlayer);
		}
	}

	/** Returns the opposite player's character. */
	public static char opposite(char player) {
		if (player == 'X') {
			return 'O';
		} else {
			return 'X';
		}
	}
}
