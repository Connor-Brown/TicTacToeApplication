package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class MonteCarloAI {

	char type;
	char opponentType;
	Node root;
	Random r = new Random();

	public MonteCarloAI(char symbol, Node r) {
		type = symbol;
		if (symbol == 'X')
			opponentType = '0';
		else
			opponentType = 'X';
		root = r;
	}

	public void doTurn(Board b) {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 2000) {
			Node leaf = seletionPhase(b.clone());
			Node C = null;
			if (!leaf.getBoard().isGameOver()) {
				C = expansionPhase(leaf);
				backpropagation(playoutPhase(C), findChildNode(C));
			}
		}
		Node selected = null;
		double max = -100000;
		for(Node n : root.getChildren()) {
			if(n.getNumWins() > max) {
				selected = n;
				max = n.getNumWins();
			}
		}
		b.doMove(selected.getBoard().getLastMove().getRow(), selected.getBoard().getLastMove().getCol());
	}

	private Node findChildNode(Node leaf) {
		List<Node> list = new ArrayList<>();
		list.add(root);
		while (!list.isEmpty()) {
			Node n = list.remove(0);
			if (n.equals(leaf)) {
				return n;
			} else {
				for (Node m : n.getChildren())
					list.add(m);
			}
		}
		return null;
	}

	private void backpropagation(boolean result, Node node) {
		node.setNumPlayouts(node.getNumPlayouts() + 1);
		if (result)
			node.setNumWins(node.getNumWins() + 1);
		if (!node.equals(root))
			backpropagation(result, node.getParent());
	}

	private boolean playoutPhase(Node c) {
		if (c.getBoard().isGameOver())
			return c.getBoard().getWinner() == type;
		Node n = new Node();
//		if(c.getBoard().findPossibleMoves().size() == 0) {
//			System.out.println(c.getBoard().findPossibleMoves().size());
//		}
		Move m = c.getBoard().findPossibleMoves().get(r.nextInt(c.getBoard().findPossibleMoves().size()));
		Board b = c.getBoard().clone();
		b.doMove(m.getRow(), m.getCol());
		b.nextTurn();
		n.setBoard(b);
		return playoutPhase(n);
	}

	private Node expansionPhase(Node leaf) {
		List<Node> list = new ArrayList<>();
		List<Move> possibleMoves = leaf.getBoard().findPossibleMoves();
		for (Move m : possibleMoves) {
			Node n = new Node();
			Board copy = leaf.getBoard().clone();
			copy.doMove(m.getRow(), m.getCol());
			copy.nextTurn();
			n.setBoard(copy);
//			copy.printBoard();
			n.setNumWins(0);
			n.setNumPlayouts(0);
			n.setParent(leaf);
			list.add(n);
		}
		leaf.setChildren(list);
		return leaf.getChildren().get(r.nextInt(leaf.getChildren().size()));
	}

	public Node seletionPhase(Board b) {
		Node node = findRootNode(b);
		if(node != null)
			root = node;
		else {
			root = new Node();
			root.setBoard(b.clone());
			root.setParent(null);
		}
		root.setBoard(b.clone());
		Node current = root;
		while (current.getChildren() != null && current.getChildren().size() != 0) {
			double highestValue = Integer.MIN_VALUE;
			Node next = null;
			for (Node n : current.getChildren()) {
				double tempValue = UCB(root, n);
				if (tempValue > highestValue) {
					highestValue = tempValue;
					next = n;
				}
			}
			current = next;
		}
		return current;
	}

	private Node findRootNode(Board b) {
		List<Node> list = new ArrayList<>();
		list.add(root);
		while (!list.isEmpty()) {
			Node n = list.remove(0);
			if (n.getBoard().equals(b)) {
				return n;
			} else {
				for (Node m : n.getChildren())
					list.add(m);
			}
		}
		return null;
	}

	public double UCB(Node parent, Node node) {
		return (node.getNumWins() / (node.getNumPlayouts()+1))
				+ (Math.sqrt(2) * (Math.sqrt(Math.log(parent.getNumPlayouts()) / (node.getNumPlayouts()+1))));
	}

}
