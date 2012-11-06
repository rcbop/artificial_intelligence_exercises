package br.fbv.ia;

import java.util.Arrays;

public class Configuration implements Comparable<Configuration> {

	private int[]			queensPosition;
	private int				heuristic;
	private Configuration	parent;

	public Configuration(Configuration parent, int[] queensPosition) {
		this.parent = parent;
		this.queensPosition = new int[queensPosition.length];
		for (int i = 0; i < queensPosition.length; i++) {
			this.queensPosition[i] = queensPosition[i];
		}
		calcHeuristic();
	}

	public Configuration(Configuration parent, int x, int y) {
		this.parent = parent;
		this.queensPosition = new int[parent.queensPosition.length];
		for (int i = 0; i < parent.queensPosition.length; i++) {
			this.queensPosition[i] = parent.queensPosition[i];
		}
		swapQueens(x, y);
		calcHeuristic();
	}

	/**
	 * XOR para trocar valores sem utilizar variável auxiliar
	 * 
	 * @param x
	 * @param y
	 */
	public void swapQueens(int x, int y) {
		queensPosition[x] ^= queensPosition[y];
		queensPosition[y] ^= queensPosition[x];
		queensPosition[x] ^= queensPosition[y];
	}

	private void calcHeuristic() {
		heuristic = 0;
		for (int i = 0; i < queensPosition.length; i++) {
			heuristic += checkDiagonals(queensPosition[i], i);
		}
	}

	private int checkDiagonals(int x, int y) {
		int attQueens = 0;
		for (int i = 0; i < queensPosition.length; i++) {
			if ((x != queensPosition[i] && y != i) && (Math.abs(y - i) == Math.abs(x - queensPosition[i]))) {
				attQueens++;
			}
		}
		return attQueens;
	}

	public Configuration getParent() {
		return parent;
	}

	public void setParent(Configuration parent) {
		this.parent = parent;
	}

	public int[] getQueensPosition() {
		return queensPosition;
	}

	public void setQueensPosition(int[] queensPosition) {
		this.queensPosition = queensPosition;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public String toString() {
		return Arrays.toString(queensPosition);
	}

	@Override
	public boolean equals(Object obj) {
		return this.toString().equals(obj.toString());
	}

	@Override
	public int compareTo(Configuration o) {
		return Integer.compare(this.heuristic, o.getHeuristic());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
