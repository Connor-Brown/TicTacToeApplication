package main;

import java.util.Scanner;

public class Move {
	int row, col;

	public Move(int row, int col) {
		this.row = row;
		this.col = col;
	}

	public Move(String inputLine) {
		Scanner scan = new Scanner(inputLine);
		if(scan.hasNext() && scan.next().equals("Move")) {
			row = scan.nextInt();
			col = scan.nextInt();
		}
		scan.close();
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass())
			return false;
		if(((Move)o).getRow() == row && ((Move)o).getCol() == col)
			return true;
		return false;
	}
	
	@Override
	public String toString() {
		return "Move "+row+" "+col;
	}
}
