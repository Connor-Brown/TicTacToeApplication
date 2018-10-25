package server;

import main.Board;
import main.MonteCarloAI;
import main.Node;

public class Wrapper {
	
	private Board board;
	private Node root;
	private MonteCarloAI ai;
	private String identifier;
	
	public Wrapper(Board b, Node r, MonteCarloAI m, String identifier) {
		board = b;
		root = r;
		ai = m;
		this.identifier = identifier;
	}
	
	@Override
	public boolean equals(Object o) {
		return o.equals(identifier);
	}
	
	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public MonteCarloAI getAi() {
		return ai;
	}

	public void setAi(MonteCarloAI ai) {
		this.ai = ai;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
}
