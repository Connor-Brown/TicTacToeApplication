package main;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	private int numWins;
	private int numPlayouts;
	private List<Node> children;
	private Board board;
	private Node parent;
	
	public int getNumWins() {
		return numWins;
	}
	public void setNumWins(int wins) {
		this.numWins = wins;
	}
	public int getNumPlayouts() {
		return numPlayouts;
	}
	public void setNumPlayouts(int numPlayouts) {
		this.numPlayouts = numPlayouts;
	}
	public List<Node> getChildren() {
		if(children == null) return new ArrayList<>();
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		return "Node: [wins:"+numWins+", playouts:"+numPlayouts+"]";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass())
			return false;
		if(!((Node)o).getBoard().equals(board))
			return false;
		if(((Node)o).getNumPlayouts() != numPlayouts || ((Node)o).getNumWins() != numWins)
			return false;
		return true;
	}
	
}
