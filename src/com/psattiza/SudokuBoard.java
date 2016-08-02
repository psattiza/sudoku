package com.psattiza;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SudokuBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	static int WIDTH = 9;
	static int HEIGHT = 9;
	static int GUESSES = 0;

	private int[][] grid;
	private int[][] copy;
	private BoardPart input[];

	public SudokuBoard() {
		grid = new int[WIDTH][HEIGHT];

		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setLayout(new GridLayout(4, 3));
		input = new BoardPart[9];
		for (int i = 0; i < 9; i++) {
			input[i] = new BoardPart();
			add(input[i]);
		}
		JButton button = new JButton("Solve");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readFromTextFeilds();
				copy = null;
				int re = solve(10);
				if(re != 0){
					grid = copy;
				}
				readFromGrid();
				System.out.println(SudokuBoard.this.toString() + "Steps Used: " + GUESSES + "\nSolutions Found: " + re);
				
			}
		});
		add(button);
		button = new JButton("Reset");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetGrid();
			}
		});
		add(button);
		button = new JButton("Remove");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n=0;
				while(removeOne()){
					n++;
				}
				System.out.println("Removed "+n+" cells");
				// resetGrid();
				// randomize();
				readFromGrid();
				// solve();
			}
		});
		add(button);
	}

	public static class BoardPart extends JPanel {

		private static final long serialVersionUID = 1L;
		public JTextField input[];

		public BoardPart() {
			setBorder(BorderFactory.createLineBorder(Color.GRAY));
			setLayout(new GridLayout(3, 3));
			input = new JTextField[9];
			for (int i = 0; i < 9; i++) {
				input[i] = new JTextField();
				input[i].setPreferredSize(new Dimension(30, 30));
				add(input[i]);
			}
		}
	}

	public int solve(int minSolutions) {
		if(minSolutions == -1)
			minSolutions = Integer.MAX_VALUE;
		GUESSES = 0;
		System.out.println("Searching for solution\n" + toString());
		return solve(0, 0, minSolutions);
		

	}

	public void readFromGrid() {
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if(grid == null){
					System.out.println("Null?");
				}
				int spot = grid[col][row];
				String setThis = spot + "";
				if (spot == 0) {
					setThis = "";
				}

				int partX = col / 3;
				int partY = row / 3;
				int smallX = col % 3;
				int smallY = row % 3;
				input[partX + 3 * partY].input[smallX + 3 * smallY].setText(setThis);
			}
		}
	}

	private void readFromTextFeilds() {
		int ix = 0, iy = 0, jx = 0, jy = 0;
		for (int i = 0; i < 9; i++) {
			ix = 3 * (i % 3);
			iy = 3 * (i / 3);
			BoardPart p = input[i];
			for (int j = 0; j < 9; j++) {
				jx = (j % 3);
				jy = (j / 3);
				int spot = 0;
				try {
					spot = Integer.valueOf(p.input[j].getText());
				} catch (Exception e) {

				}
				// System.out.println("Value:" + spot + " Read @ " + (ix + jx) +
				// " " + (iy + jy));
				grid[ix + jx][iy + jy] = spot;
			}
		}
	}

	private boolean removeOne() {
		int n = 20, row, col, spot;
		while (n > 0) {
			row = (int) (Math.random() * 9);
			col = (int) (Math.random() * 9);
			spot = grid[row][col];
			if (spot != 0) {
				grid[row][col] = 0;
				if (solve(2) == 1) {
					return true;
				} else {
					grid[row][col] = spot;
					n--;
				}
			}
			n--;
		}
		System.out.println("Falied??");
		return false;
	}

	private int solve(int i, int j, int minSolutions) {
		GUESSES++;
		if (GUESSES % 10000000 == 0) {
			System.out.println(toString() + "Solution taking a while, might be impossible!");
			return 1;
		}

		if (j == 9) {
			j = 0;
			i++;
			if (i == 9) {
				// readFromGrid();
				// System.out.println(toString());
				gridCopy();
				return 1;
			}
		}

		if (grid[i][j] != 0) {
			return solve(i, j + 1, minSolutions);
		}
		int guess = 1;
		int re = 0;
		while (guess < 10) {
			grid[i][j] = guess;
			if (checkCell(i, j)) {
				re += solve(i, j + 1, minSolutions);
				if (re >= minSolutions){
					grid[i][j] = 0;
					return re;
				}
			}
			guess++;
		}
		grid[i][j] = 0;
		return re;
	}

	public void randomize() {
		int row = 0, col = 0, value = 0;
		for (int n = 0; n < 30; n += 2) {
			row = (int) (Math.random() * 9);
			col = (int) (Math.random() * 9);
			value = (int) (Math.random() * 9) + 1;
			if (grid[col][row] != 0) {
				continue;
			}
			grid[col][row] = value;
			if (!checkCell(col, row)) {
				n--;
				grid[col][row] = 0;
			}
		}
	}

	private void resetGrid() {
		grid = new int[WIDTH][HEIGHT];
		readFromGrid();
	}

	public int get(int col, int row) {
		return grid[col][row];
	}

	public void set(int col, int row, int set) {
		grid[col][row] = set;
	}

	public boolean checkCell(int col, int row) {
		return checkRow(row) && checkCol(col) && checkSquare(col, row);
	}

	private boolean checkSquare(int col, int row) {
		Set<Integer> seen = new HashSet<Integer>();
		int startRow = 3 * (row / 3);
		int startCol = 3 * (col / 3);
		for (int x = startCol; x < startCol + 3; x++) {
			for (int y = startRow; y < startRow + 3; y++) {
				int spot = grid[x][y];
				if (spot == 0)
					continue;
				if (seen.contains(spot)) {
					return false;
				} else {
					seen.add(spot);
				}
			}
		}
		return true;
	}

	private boolean checkCol(int col) {
		Set<Integer> seen = new HashSet<Integer>();
		for (int i = 0; i < 9; i++) {
			int spot = grid[col][i];
			if (spot == 0)
				continue;
			if (seen.contains(spot)) {
				return false;
			} else {
				seen.add(spot);
			}
		}
		return true;
	}

	private boolean checkRow(int row) {
		Set<Integer> seen = new HashSet<Integer>();
		for (int i = 0; i < 9; i++) {
			int spot = grid[i][row];
			if (spot == 0)
				continue;
			if (seen.contains(spot)) {
				return false;
			} else {
				seen.add(spot);
			}
		}
		return true;
	}

	public String toString() {
		String re = "+-----------------------+\n";
		for (int row = 0; row < 9; row++) {
			if (row % 3 == 0 && row != 0) {
				re += "|-------+-------+-------|\n";
			}
			for (int col = 0; col < 9; col++) {
				if (col % 3 == 0) {
					re += "| ";
				}
				int spot = grid[col][row];
				if (spot == 0)
					re += "* ";
				else
					re += spot + " ";
			}
			re += "|\n";
		}
		re += "+-----------------------+\n";
		return re;
	}

	public int[][] gridCopy() {
		copy = new int[WIDTH][HEIGHT];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				copy[row][col] = grid[row][col];
			}
		}
		return copy;
	}
}
