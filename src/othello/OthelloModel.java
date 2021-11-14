/* 
 * File name : OthelloModel.java
 * Author	 : Song Nguyen Nguyen, 040940830
 * Course	 : CST8221_310 – JAP, Lab Section: 313
 * Assignment: 2-part 2
 * Date		 : December 13, 2020
 * Professor : Karan Kalsi
 * Purpose	 : Model of the Othello game, it keeps track of the logic behind the scence, and makes sure the functionality works perfectly.
 * Class list: Othello.java, OthelloSplashScreen.java, OthelloViewController.java, OthelloModel.java, OthelloNetworkModalViewController.java, OthelloServer.java
 */

package othello;

import java.util.ArrayList;


/**
 * This class is responsible for handling the logic of Othello game,
 * and keeping track of the current state of the Othello game.
 * 
 * @author Song N Nguyen
 * @version 1.0
 * @see othello
 * @since 1.8.0_261
 */
public class OthelloModel {
	/** Board logic of 8x8. */
	private int[][] board = new int[7][7];

	/** Normal game mode. */
	public static final int NORMAL = 0;
	/** Fast testing game mode. */
	public static final int CORNER_TEST = 1;
	/** Decent test. */
	public static final int OUTER_TEST = 2;
	/** 1x capture test. */
	public static final int TEST_CAPTURE = 3;
	/** 2x capture test. */
	public static final int TEST_CAPTURE2 = 4;
	/** Empty board. */
	public static final int UNWINNABLE = 5;
	/** Cool test. */
	public static final int INNER_TEST = 6;

	/** Empty state of the square. */
	public static final int EMPTY = 0;
	/** Player 1. */
	public static final int BLACK = 1;
	/** Player 2. */
	public static final int WHITE = 2;

	/**
	 * This method initializes the logic of the Othello game.
	 * 
	 * @param mode - int mode of the current game.
	 */
	public void initialize(int mode) {
		switch (mode) {
		case CORNER_TEST:
			board = new int[][] { 
				{ 2, 0, 0, 0, 0, 0, 0, 1 }, 
				{ 0, 1, 0, 0, 0, 0, 2, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0, 1, 0 }, 
				{ 2, 0, 0, 0, 0, 0, 0, 2 } };

			break;
		case OUTER_TEST:
			board = new int[][] { 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 2, 2, 2, 2, 2, 2, 0 }, 
				{ 0, 2, 1, 1, 1, 1, 2, 0 },
				{ 0, 2, 1, 0, 0, 1, 2, 0 }, 
				{ 0, 2, 1, 0, 0, 1, 2, 0 }, 
				{ 0, 2, 1, 1, 1, 1, 2, 0 },
				{ 0, 2, 2, 2, 2, 2, 2, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };
			break;
		case INNER_TEST:
			board = new int[][] { 
				{ 2, 2, 2, 2, 2, 2, 2, 2 }, 
				{ 2, 0, 0, 0, 0, 0, 0, 2 }, 
				{ 2, 0, 2, 2, 2, 2, 0, 2 },
				{ 2, 0, 2, 1, 1, 2, 0, 2 }, 
				{ 2, 0, 2, 1, 1, 2, 0, 2 }, 
				{ 2, 0, 2, 2, 2, 2, 0, 2 },
				{ 2, 0, 0, 0, 0, 0, 0, 2 }, 
				{ 2, 2, 2, 2, 2, 2, 2, 2 } };
			break;
		case UNWINNABLE:
			board = new int[][] { 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };
			break;
		case TEST_CAPTURE:
			board = new int[][] { 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 1, 1, 1, 1, 1, 1, 0 }, 
				{ 0, 1, 1, 1, 1, 1, 1, 0 },
				{ 0, 1, 2, 2, 2, 1, 1, 0 }, 
				{ 0, 1, 2, 0, 2, 1, 1, 0 }, 
				{ 0, 1, 2, 2, 2, 1, 1, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };
			break;

		case TEST_CAPTURE2:
			board = new int[][] { 
				{ 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 1, 1, 1, 1, 1, 1, 1 }, 
				{ 1, 2, 2, 2, 1, 2, 1, 1 },
				{ 1, 2, 2, 2, 2, 2, 1, 1 }, 
				{ 1, 2, 2, 0, 2, 2, 1, 1 }, 
				{ 1, 2, 2, 2, 2, 1, 1, 1 },
				{ 1, 2, 1, 2, 2, 2, 1, 1 }, 
				{ 1, 1, 1, 1, 1, 1, 1, 1 } };
			break;
		default:
			board = new int[][] { 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 2, 1, 0, 0, 0 }, 
				{ 0, 0, 0, 1, 2, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };

		}
	}

	/**
	 * This method returns the current square with at the specified x - row, y -
	 * column values.
	 * 
	 * @param x - int row of that square.
	 * @param y - int column of that square.
	 * @return - int value at the row and column.
	 */
	public int getBoard(int x, int y) {
		switch (board[x][y]) {
		case EMPTY:
			return 0;

		case BLACK:
			return 1;

		case WHITE:
			return 2;

		default:
			return board[x][y];
		}
	}

	/**
	 * This method returns the total number of pieces the specified player has on
	 * the board at the time of calling.
	 * 
	 * @param player - int player number (1 - black, 2 - white).
	 * @return - int number of chips that specified player has.
	 */
	public int getChips(int player) {
		int count = 0;
		// returns the total number of pieces the specified player has on the board at
		// the time of calling
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (board[row][col] == player)
					count++;
			}
		}
		return count;
	}

	/**
	 * This method checks if the current square is a valid move for that specified
	 * player or not.
	 * 
	 * @param x      - int row value of that square.
	 * @param y      - int column value of that square.
	 * @param player - int specified player (1 - black, 2 - white).
	 * @return - true if that square is a valid move, otherwise, false.
	 */
	public boolean isValid(int x, int y, int player) {
		int opponent;
		if (player == WHITE)
			opponent = BLACK;
		else
			opponent = WHITE;

		// check if that square is occupied or not
		if (board[x][y] != EMPTY)
			return false;
		else {

			// checking the neighbours around that square
			int rowStart = Math.max(x - 1, 0);
			int rowFinish = Math.min(x + 1, board.length - 1);
			int colStart = Math.max(y - 1, 0);
			int colFinish = Math.min(y + 1, board.length - 1);

			for (int row = rowStart; row <= rowFinish; row++) {
				for (int col = colStart; col <= colFinish; col++) {

					// current square to be check, then skip
					if (row == x && col == y)
						continue;

					// finding the opponent pieces around the square
					// 8 possible cases
					if (board[row][col] == opponent) {
						int curRow = row;
						int curCol = col;
						boolean previousCell = false;
						// check the row above the current square
						if (col < y) {
							// row < x, row == x, row > x
							if (row < x) {
								// checking North-West position
								while (curRow > 0 && curCol > 0) {
									curRow -= 1;
									curCol -= 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							} else if (row == x) {
								previousCell = false;
								// checking North position
								while (curCol > 0) {
									curCol -= 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							} else if (row > x) {
								previousCell = false;
								// checking North-East position
								while (curRow < board.length - 1 && curCol > 0) {
									curRow += 1;
									curCol -= 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							}

							// check the row same as the current square
						} else if (col == y) {
							curRow = row;
							// row < x, row > x
							if (row < x) {
								previousCell = false;
								// checking West
								while (curRow > 0) {
									curRow -= 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							} else if (row > x) {
								previousCell = false;
								// checking East
								while (curRow < board.length - 1) {
									curRow += 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							}

							// checking the row below the current square
						} else if (col > y) {
							curRow = row;
							curCol = col;
							// row < x, row == x, row > x
							if (row < x) {
								previousCell = false;
								// checking South-West position
								while (curRow > 0 && curCol < board[curRow].length - 1) {
									curRow -= 1;
									curCol += 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							} else if (row == x) {
								previousCell = false;
								// checking South position
								while (curCol < board[curRow].length - 1) {
									curCol += 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							} else if (row > x) {
								previousCell = false;
								// checking South-East position
								while (curRow < board.length - 1 && curCol < board[curRow].length - 1) {
									curRow += 1;
									curCol += 1;

									if (board[curRow][curCol] == player && !previousCell)
										return true;
									else if (board[curRow][curCol] == player && previousCell)
										return false;

									if (board[curRow][curCol] == EMPTY)
										previousCell = true;
								}
							}
						}
					}

				}
			}
		}
		return false;
	}

	/**
	 * This method handles the player movement by checking the validity of the
	 * chosen square. If the move is legal, all of the appropriate chips will be
	 * flipped to the new colour.
	 * 
	 * @param x      - int row of the chosen square.
	 * @param y      - int column of the chosen square.
	 * @param player - int player (1 - black, 2 - white).
	 * @return - int the total number of chips captured by that specified player. 0
	 *         is returned for illegal move.
	 */
	public int move(int x, int y, int player) {
		int captured = 0;

		// check if that move is valid or not
		if (!isValid(x, y, player))
			return 0;
		else {
			int opponent;
			if (player == WHITE)
				opponent = BLACK;
			else
				opponent = WHITE;

			// storing the possible coordinates in the ArrayList
			ArrayList<Integer> validRow;
			ArrayList<Integer> validCol;

			// find the neighbours around that square
			int rowStart = Math.max(x - 1, 0);
			int rowFinish = Math.min(x + 1, board.length - 1);
			int colStart = Math.max(y - 1, 0);
			int colFinish = Math.min(y + 1, board.length - 1);

			for (int row = rowStart; row <= rowFinish; row++) {
				for (int col = colStart; col <= colFinish; col++) {

					// the square that is chosen by the player
					if (row == x && col == y)
						board[x][y] = player;

					// finding the opponent to "flip"
					if (board[row][col] == opponent) {
						int curRow = row;
						int curCol = col;

						// same logic as the isValid method of checking all possible directions
						// check the row above the square
						if (col < y) {
							// row < x, row == x, row > x
							if (row < x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking North-West position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curRow >= 0 && curCol >= 0) {

									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}

									curRow -= 1;
									curCol -= 1;
								}
							} else if (row == x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking North position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curCol >= 0) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}

									curCol -= 1;
								}
							} else if (row > x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking North-East position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curRow < board.length && curCol >= 0) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}

									curRow += 1;
									curCol -= 1;
								}
							}

							// checking the row same as the current square
						} else if (col == y) {
							curRow = row;
							// row < x, row > x
							if (row < x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking West position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curRow > 0) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}
									curRow -= 1;
								}
							} else if (row > x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking East position
								while (curRow < board.length) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}
									curRow += 1;
								}
							}

							// checking the row below the current square
						} else if (col > y) {
							curRow = row;
							curCol = col;
							// row < x, row == x, row > x
							if (row < x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking South-West position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curRow >= 0 && curCol < board[curRow].length) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}
									curRow -= 1;
									curCol += 1;
								}
							} else if (row == x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking South position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curCol < board[curRow].length) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}
									curCol += 1;
								}
							} else if (row > x) {
								validRow = new ArrayList<Integer>();
								validCol = new ArrayList<Integer>();
								// checking South-East position
								// add the possible opponent into the ArrayList
								// if there is a gap between them, move on to the next direction
								// if another player piece is found, captured the opponents
								while (curRow < board.length && curCol < board[curRow].length) {
									if (board[curRow][curCol] == opponent) {
										validRow.add(curRow);
										validCol.add(curCol);
									} else if (board[curRow][curCol] == EMPTY)
										break;

									if (board[curRow][curCol] == player) {
										for (int i = 0; i < validRow.size(); i++) {
											board[validRow.get(i)][validCol.get(i)] = player;
											captured++;
										}
										break;
									}
									curRow += 1;
									curCol += 1;
								}
							}
						}
					}

				}
			}
		}

		return captured;
	}

	/**
	 * This method checks if the specified player can move at all, anywhere on the
	 * board.
	 * 
	 * @param player - int specified player.
	 * @return - true if there is a move that the player can make, otherwise, false.
	 */
	public boolean canMove(int player) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				if (isValid(row, col, player))
					return true;
			}
		}
		return false;
	}
}