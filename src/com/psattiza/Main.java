package com.psattiza;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {

		SudokuBoard board = new SudokuBoard();
		setG(board);
		board.readFromGrid();
        JFrame frame = new JFrame("Sudoku");
        frame.add(board);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);            
        frame.setVisible(true);
	}
	
	
	public static void setG(SudokuBoard g){

		g.set(0, 1, 5);
		g.set(0, 2, 1);
		g.set(0, 5, 6);
		g.set(0, 7, 7);
		g.set(0, 8, 9);

		g.set(1, 3, 3);

		g.set(2, 0, 8);
		g.set(2, 1, 4);
		g.set(2, 2, 2);
		g.set(2, 4, 9);
		g.set(2, 6, 5);
		g.set(2, 7, 6);

		g.set(3, 0, 4);
		g.set(3, 7, 2);

		g.set(4, 0, 9);
		g.set(4, 3, 2);
		g.set(4, 4, 7);
		g.set(4, 5, 8);
		g.set(4, 8, 6);

		g.set(5, 1, 2);
		g.set(5, 8, 7);

		g.set(6, 1, 7);
		g.set(6, 2, 4);
		g.set(6, 4, 5);
		g.set(6, 6, 6);
		g.set(6, 7, 9);
		g.set(6, 8, 1);

		g.set(7, 5, 9);

		g.set(8, 0, 1);
		g.set(8, 1, 9);
		g.set(8, 3, 7);
		g.set(8, 6, 3);
		g.set(8, 7, 5);
	}

}
