package main;

import java.util.List;
import java.util.Random;

public class MinimaxAI {
	char type;
	char opponentType;
	int maxDepth = 1;
	
	public MinimaxAI(char symbol) {
		type = symbol;
		if(symbol == 'X')
			opponentType = '0';
		else
			opponentType = 'X';
	}
	
	public void doTurn(Board b) {
		List<Move> moves = b.findPossibleMoves();
		int best = Integer.MIN_VALUE;
		int pos = 0;
		for(int i = 0; i < moves.size(); i++) {
			Board clone = b.clone();
			clone.doMove(moves.get(i).getRow(), moves.get(i).getCol());
			clone.nextTurn();
			int choice = doMinimax(clone, true, maxDepth);
			if(choice >= best) {
				if(choice > best) {
					best = choice;
					pos = i;
				} else {
					Random r = new Random();
					if(r.nextBoolean()) {
						pos = i;
					}
				}
			}
		}
		b.doMove(moves.get(pos).getRow(), moves.get(pos).getCol());
	}
	
	private int evaluateBoard(Board b) {
		if(b.isGameOver()) {
			if(b.getWinner() == type) {
				return 10000;
			} else if(b.getWinner() == ' ') {
				return 0;
			} else {
				return -10000;
			}
		} else {
			int score = 0;
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					if(i+j == 4)
						score += 5*evaluateSubBoard(b, i, j);
					else if((i+j)%2 == 0)
						score += 3*evaluateSubBoard(b, i, j);
					else
						score += evaluateSubBoard(b, i, j);
				}
			}
			return score;
		}
	}
	
	private int evaluateSubBoard(Board b, int row, int col) {
		if(b.getFinished()[row][col] != ' ') {
			if(b.getFinished()[row][col] == type)
				return 100;
			else if(b.getFinished()[row][col] == '=')
				return 0;
			else
				return -100;
		}
		int score = 0;
		for(int i = row; i < row+3; i++) {
			for(int j = col; j < col+3; j++) {
				if(b.getBoard()[i][j] == type) {
					if(i+j == 4)
						score += 5;
					else if((i+j)%2 == 0)
						score += 3;
					else
						score += 1;
				} else if(b.getBoard()[i][j] == opponentType) {
					if(i+j == 4)
						score -= 5;
					else if((i+j)%2 == 0)
						score -= 3;
					else
						score -= 1;
				}
			}
		}
		return score;
	}

	private int doMinimax(Board b, boolean maximizing, int depth) {
		if(b.isGameOver() || depth == 0) {
			return evaluateBoard(b);
		}
		if(maximizing) {
			int best = Integer.MIN_VALUE;
			for(int i = 0; i < b.findPossibleMoves().size(); i++) {
				Board clone = b.clone();
				clone.doMove(b.findPossibleMoves().get(i).getRow(), b.findPossibleMoves().get(i).getCol());
				clone.nextTurn();
				int value = doMinimax(clone, false, depth-1);
				if(value > best) {
					best = value;
				}
			}
			return best;
		} else {
			int best = Integer.MAX_VALUE;
			for(int i = 0; i < b.findPossibleMoves().size(); i++) {
				Board clone = b.clone();
				clone.doMove(b.findPossibleMoves().get(i).getRow(), b.findPossibleMoves().get(i).getCol());
				clone.nextTurn();
				int value = doMinimax(clone, true, depth-1);
				if(value < best) {
					best = value;
				}
			}
			return best;
		}
	}
}
