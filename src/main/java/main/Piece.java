package main;

public class Piece {
	public int symbol = 1;

	public Piece(int symbol) {
		this.symbol = symbol;
	}

	public char getSymbol() {
		if (symbol == 0)
			return '0';
		else
			return 'X';
	}

	public boolean equals(Piece p) {
		if (p.getSymbol() == symbol)
			return true;
		return false;
	}
}
