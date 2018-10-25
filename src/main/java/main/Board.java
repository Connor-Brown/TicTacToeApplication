package main;

import java.util.ArrayList;

public class Board {

	private char[][] board;
	private char[][] finished;
	private int turn = 0;
	private char winner = ' ';
	private Move lastMove = null;

	public Board() {
		board = new char[9][9];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = ' ';
			}
		}
		finished = new char[3][3];
		for (int i = 0; i < finished.length; i++) {
			for (int j = 0; j < finished[0].length; j++) {
				finished[i][j] = ' ';
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Board ");
		if(lastMove != null) {
			sb.append(lastMove + " ");
 		} else {
 			sb.append("null ");
 		}
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				sb.append(board[i][j] + ":");
			}
		}
		return sb.toString();
	}

	public Board(char[][] b) {
		board = b.clone();
	}

	public int getTurn() {
		return turn;
	}

	public char[][] getBoard() {
		return board;
	}

	public char[][] getFinished() {
		return finished;
	}

	public char getWinner() {
		return winner;
	}

	public ArrayList<Move> findPossibleMoves() {
		if (isGameOver()) {
			return null;
		}
		ArrayList<Move> returner = new ArrayList<>();
		int loc = getBoardToGoInBasedOnLastMove();
		if (loc == -1)
			returner = findAllOpenPositions();
		else
			returner = getPossibleMovesBySquare(loc);
		return returner;
	}

	private ArrayList<Move> removeDoneSquaresFromPossibleMoves(ArrayList<Move> returner) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (finished[i][j] != ' ') {
					for (Move m : getPossibleMovesBySquare(i * 3 + j)) {
						returner.remove(m);
					}
				}
			}
		}
		return returner;
	}

	private ArrayList<Move> getPossibleMovesBySquare(int loc) {
		ArrayList<Move> list = new ArrayList<>();
		int row = loc / 3;
		int col = loc % 3;
		for (int i = row * 3; i < (row * 3) + 3; i++) {
			for (int j = col * 3; j < (col * 3) + 3; j++) {
				if (finished[row][col] == ' ' && board[i][j] == ' ')
					list.add(new Move(i, j));
			}
		}
		if (list.isEmpty())
			return findAllOpenPositions();
		return list;
	}

	private ArrayList<Move> findAllOpenPositions() {
		ArrayList<Move> returner = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int row = i / 3;
				int col = j / 3;
				if (finished[row][col] == ' ' && board[i][j] == ' ')
					returner.add(new Move(i, j));
			}
		}
		return returner;
	}

	public Move getLastMove() {
		return lastMove;
	}

	private int getBoardToGoInBasedOnLastMove() {
		if (lastMove == null)
			return -1;
		int row = lastMove.getRow() % 3;
		int col = lastMove.getCol() % 3;
		if (finished[row][col] != ' ')
			return -1;
		if (row == 0 && col == 0) {
			return 0;
		} else if (row == 0 && col == 1) {
			return 1;
		} else if (row == 0 && col == 2) {
			return 2;
		} else if (row == 1 && col == 0) {
			return 3;
		} else if (row == 1 && col == 1) {
			return 4;
		} else if (row == 1 && col == 2) {
			return 5;
		} else if (row == 2 && col == 0) {
			return 6;
		} else if (row == 2 && col == 1) {
			return 7;
		} else {
			return 8;
		}
	}

	public char getSymbolAtPos(int i, int j) {
		return board[i][j];
	}

	@Override
	public boolean equals(Object b) {
		if (b.getClass() != this.getClass())
			return false;
		if (!((Board) b).lastMove.equals(lastMove)) {
			return false;
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (((Board) b).getSymbolAtPos(i, j) != board[i][j])
					return false;
			}
		}
		return true;
	}

	public void doMove(int row, int col) {
		Piece p = new Piece(turn);
		board[row][col] = p.getSymbol();
		lastMove = new Move(row, col);
		char c = checkIfWonSquare(row, col);
		finished[row / 3][col / 3] = c;
		if (checkIfDrawSquare(row, col))
			finished[row / 3][col / 3] = '=';
	}

	private boolean checkIfDrawSquare(int r, int c) {
		int row = r / 3;
		int col = c / 3;
		for (int i = row * 3; i < (row * 3) + 3; i++) {
			for (int j = col * 3; j < (col * 3) + 3; j++) {
				if (board[i][j] == ' ')
					return false;
			}
		}
		return true;
	}

	private char checkIfWonSquare(int r, int c) {
		int row = r / 3;
		int col = c / 3;
		// check diagonals
		if (board[row * 3][col * 3] != ' ' && board[row * 3][col * 3] == board[row * 3 + 1][col * 3 + 1]
				&& board[row * 3][col * 3] == board[row * 3 + 2][col * 3 + 2])
			return board[row * 3][col * 3];
		if (board[row * 3][col * 3 + 2] != ' ' && board[row * 3][col * 3 + 2] == board[row * 3 + 1][col * 3 + 1]
				&& board[row * 3][col * 3 + 2] == board[row * 3 + 2][col * 3])
			return board[row * 3][col * 3 + 2];

		// check horizontals
		if (board[row * 3][col * 3] != ' ' && board[row * 3][col * 3] == board[row * 3][col * 3 + 1]
				&& board[row * 3][col * 3] == board[row * 3][col * 3 + 2])
			return board[row * 3][col * 3];
		if (board[row * 3 + 1][col * 3] != ' ' && board[row * 3 + 1][col * 3] == board[row * 3 + 1][col * 3 + 1]
				&& board[row * 3 + 1][col * 3] == board[row * 3 + 1][col * 3 + 2])
			return board[row * 3 + 1][col * 3];
		if (board[row * 3 + 2][col * 3] != ' ' && board[row * 3 + 2][col * 3] == board[row * 3 + 2][col * 3 + 1]
				&& board[row * 3 + 2][col * 3] == board[row * 3 + 2][col * 3 + 2])
			return board[row * 3 + 2][col * 3];

		// check verticals
		if (board[row * 3][col * 3] != ' ' && board[row * 3][col * 3] == board[row * 3 + 1][col * 3]
				&& board[row * 3][col * 3] == board[row * 3 + 2][col * 3])
			return board[row * 3][col * 3];
		if (board[row * 3][col * 3 + 1] != ' ' && board[row * 3][col * 3 + 1] == board[row * 3 + 1][col * 3 + 1]
				&& board[row * 3][col * 3 + 1] == board[row * 3 + 2][col * 3 + 1])
			return board[row * 3][col * 3 + 1];
		if (board[row * 3][col * 3 + 2] != ' ' && board[row * 3][col * 3 + 2] == board[row * 3 + 1][col * 3 + 2]
				&& board[row * 3][col * 3 + 2] == board[row * 3 + 2][col * 3 + 2])
			return board[row * 3][col * 3 + 2];
		return ' ';
	}

	public void nextTurn() {
		turn = 1 - turn;
	}

	public Board clone() {
		Board b = new Board();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				b.setSymbolAtPos(board[i][j], i, j);
			}
		}
		char[][] clonedFinish = new char[3][3];
		for (int i = 0; i < finished.length; i++) {
			for (int j = 0; j < finished[0].length; j++) {
				clonedFinish[i][j] = finished[i][j];
			}
		}
		b.setFinish(clonedFinish);
		b.turn = this.turn;
		b.lastMove = lastMove;
		return b;
	}

	private void setFinish(char[][] clonedFinish) {
		this.finished = clonedFinish;
	}

	public void setSymbolAtPos(char c, int row, int col) {
		board[row][col] = c;
	}

	public String printBoard() {
		StringBuilder sb = new StringBuilder(
				"\n\n\n\n\n\n\n" + printSquareToGoIn() + 
				"      1   2   3     4   5   6     7   8   9  \n");
		sb.append(printLine());
		sb.append(printLine());
		for (int i = 0; i < 9; i++) {
			sb.append((i + 1) + " ");
			sb.append(printRow(i));
			sb.append(printLine());
			if (i % 3 == 2)
				sb.append(printLine());
		}
		return sb.toString();
	}

	private String printSquareToGoIn() {
		StringBuilder sb = new StringBuilder();
		if (lastMove == null || getBoardToGoInBasedOnLastMove() == -1) {
			sb.append("Board to play in: Anywhere");
		} else {
			int loc = getBoardToGoInBasedOnLastMove();
			switch (loc) {
			case 0:
				sb.append("Board to play in: Top Left");
				break;
			case 1:
				sb.append("Board to play in: Top Middle");
				break;
			case 2:
				sb.append("Board to play in: Top Right");
				break;
			case 3:
				sb.append("Board to play in: Middle Left");
				break;
			case 4:
				sb.append("Board to play in: Middle Middle");
				break;
			case 5:
				sb.append("Board to play in: Middle Right");
				break;
			case 6:
				sb.append("Board to play in: Bottom Left");
				break;
			case 7:
				sb.append("Board to play in: Bottom Middle");
				break;
			case 8:
				sb.append("Board to play in: Bottom Right");
				break;
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public String printRow(int row) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			sb.append("|");
			if (i % 3 == 0)
				sb.append("||");
			sb.append(" ");
			sb.append(printBoardPosition(row, i));
			sb.append(" ");
		}
		sb.append("|||\n");
		return sb.toString();
	}

	private String printBoardPosition(int row, int col) {
		StringBuilder sb = new StringBuilder();
		if (finished[row / 3][col / 3] != ' ') {
			sb.append(finished[row / 3][col / 3]);
		} else {
			if (board[row][col] == ' ')
				sb.append(" ");
			else {
				if (board[row][col] == '0')
					sb.append('0');
				else
					sb.append('X');
			}
		}
		return sb.toString();
	}

	public String printLine() {
		return "  ---------------------------------------------\n";
	}

	public boolean isGameOver() {
		if (checkIfAPlayerWon())
			return true;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				int r = i / 3;
				int c = j % 3;
				if (finished[r][c] == ' ' && board[i][j] == ' ')
					return false;
			}
		}
		return true;
	}

	public boolean checkIfAPlayerWon() {
		if (checkDiagonals() || checkVerticals() || checkHorizontals())
			return true;
		return false;
	}

	public boolean checkDiagonals() {
		if ((finished[0][0] != ' ' && finished[0][0] != '=' && finished[0][0] == finished[1][1]
				&& finished[0][0] == finished[2][2])
				|| (finished[0][2] != ' ' && finished[0][2] != '=' && finished[0][2] == finished[1][1]
						&& finished[1][1] == finished[2][0])) {
			winner = finished[1][1];
			return true;
		}
		return false;
	}

	public boolean checkVerticals() {
		if (finished[0][0] != ' ' && finished[0][0] != '=' && finished[0][0] == finished[1][0]
				&& finished[0][0] == finished[2][0]) {
			winner = finished[0][0];
			return true;
		} else if (finished[0][1] != ' ' && finished[0][1] != '=' && finished[0][1] == finished[1][1]
				&& finished[0][1] == finished[2][1]) {
			winner = finished[0][1];
			return true;
		} else if (finished[0][2] != ' ' && finished[0][2] != '=' && finished[0][2] == finished[1][2]
				&& finished[0][2] == finished[2][2]) {
			winner = finished[0][2];
			return true;
		}
		return false;
	}

	public boolean checkHorizontals() {
		if (finished[0][0] != ' ' && finished[0][0] != '=' && finished[0][0] == finished[0][1]
				&& finished[0][0] == finished[0][2]) {
			winner = finished[0][0];
			return true;
		} else if (finished[1][0] != ' ' && finished[1][0] != '=' && finished[1][0] == finished[1][1]
				&& finished[1][0] == finished[1][2]) {
			winner = board[1][0];
			return true;
		} else if (finished[2][0] != ' ' && finished[2][0] != '=' && finished[2][0] == finished[2][1]
				&& finished[2][0] == finished[2][2]) {
			winner = finished[2][0];
			return true;
		}
		return false;
	}

}
