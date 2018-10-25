package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Start {
	
	public static void main(String[] args) {
//		doHumanOnlyGame();
		doHumanAndMonteCarloAIGame();
		
//		Board b = new Board();
//		Node n = new Node();
//		n.setBoard(b);
//		n.setNumPlayouts(9);
//		n.setNumWins(5);
//		Node n1 = new Node();
//		n1.setBoard(b);
//		n1.setNumWins(1);
//		n1.setNumPlayouts(2);
//		Node n2 = new Node();
//		n2.setBoard(b);
//		n2.setNumWins(1);
//		n2.setNumPlayouts(3);
//		Node n3 = new Node();
//		n3.setBoard(b);
//		n3.setNumWins(3);
//		n3.setNumPlayouts(4);
//		List<Node> l = new ArrayList<>();
//		l.add(n1);
//		l.add(n2);
//		l.add(n3);
//		n.setChildren(l);
//		Node n4 = new Node();
//		n4.setBoard(b);
//		n4.setNumPlayouts(2);
//		n4.setNumWins(2);
//		Node n5 = new Node();
//		n5.setBoard(b);
//		n5.setNumWins(1);
//		n5.setNumPlayouts(2);
//		List<Node> i = new ArrayList<>();
//		i.add(n4);
//		i.add(n5);
//		n3.setChildren(i);
//		MonteCarloAI m = new MonteCarloAI('X', n);
//		System.out.println(m.UCB(n, n1));
	}
	
	private static void doHumanAndMonteCarloAIGame() {
		Board b = new Board();
		Node root = new Node();
		root.setBoard(b);
		MonteCarloAI m = new MonteCarloAI('X', root);
		Scanner scan = new Scanner(System.in);
		System.out.println(b.printBoard());
		while(!b.isGameOver()) {
			if(b.getTurn() == 1)
				m.doTurn(b);
			else {
				Move move = getInput(b, scan);
				b.doMove(move.getRow(), move.getCol());
			}
			System.out.println(b.printBoard());
			b.nextTurn();
		}
		if(b.getWinner() == ' ')
			System.out.println("The game is a draw");
		else
			System.out.println("The winner is " + b.getWinner() + "'s!!!");
		scan.close();
	}

	private static void doHumanAndMinimaxAIGame() {
		MinimaxAI a = new MinimaxAI('X');
		Scanner scan = new Scanner(System.in);
		Board b = new Board();
		b.printBoard();
		while(!b.isGameOver()) {
			if(b.getTurn() == 1)
				a.doTurn(b);
			else {
				Move move = getInput(b, scan);
				b.doMove(move.getRow(), move.getCol());
			}
			b.printBoard();
			b.nextTurn();
		}
		if(b.getWinner() == ' ')
			System.out.println("The game is a draw");
		else
			System.out.println("The winner is " + b.getWinner() + "'s!!!");
		scan.close();
	}
	
	private static void doHumanOnlyGame() {
		Scanner scan = new Scanner(System.in);
		Board b = new Board();
		b.printBoard();
		while(!b.isGameOver()) {
			Move move = getInput(b, scan);
			b.doMove(move.getRow(), move.getCol());
			b.printBoard();
			b.nextTurn();
		}
		if(b.getWinner() == ' ')
			System.out.println("The game is a draw");
		else
			System.out.println("The winner is " + b.getWinner() + "'s!!!");
		scan.close();
	}

	private static Move getInput(Board b, Scanner scan) {
		String input = "";
		do {
			System.out.println("Enter the row and column for your move (i.e. 1 9 to go in the top right most position)");
			input = scan.nextLine();
		} while (!isLegalInput(b, input));
		Scanner s = new Scanner(input);
		int first = Integer.parseInt(s.next());
		int second = Integer.parseInt(s.next());
		s.close();
		return new Move(first - 1, second - 1);
	}

	private static boolean isLegalInput(Board b, String input) {
		if (input.length() != 3)
			return false;
		String first = input.substring(0, 1);
		String second = input.substring(2, 3);
		if ((first.equals("1") || first.equals("2") || first.equals("3") || first.equals("4") || first.equals("5") || first.equals("6") || first.equals("7") || first.equals("8") || first.equals("9"))
				&& (second.equals("1") || second.equals("2") || second.equals("3") || second.equals("4") || second.equals("5") || second.equals("6") || second.equals("7") || second.equals("8") || second.equals("9"))) {
			if (b.getSymbolAtPos(Integer.parseInt(first) - 1, Integer.parseInt(second) - 1) == ' ' && b.findPossibleMoves().contains(new Move(Integer.parseInt(first) - 1, Integer.parseInt(second) - 1)))
				return true;
		}
		return false;
	}
	
}
