package br.fbv.ia;

import java.util.LinkedList;
import java.util.SortedSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;


/**
 * @author Rogério Peixoto (rcbpeixoto@gmail.com)
 */
public class EightQueenPuzzle {

	private SortedSet<Configuration>		searchTree					= null;
	private SortedSet<String>				visitedNodes				= null;
	private int							pIndex						= 0;
	private int							pBlockIndex					= 0;
	private static final int[]			initialQueenConfiguration	= { 0, 1, 2, 3, 4, 5, 6, 7 };
	public static final int				BOARD_SIZE					= 8;

	private Timer statusTimer;
	
	
	public EightQueenPuzzle() {
		this.searchTree = new TreeSet<Configuration>();
		this.visitedNodes = new TreeSet<String>();
		// Initial State (Problem formulation)
		Configuration initialState = new Configuration(null, initialQueenConfiguration);
		this.searchTree.add(initialState);
	}

	public Configuration greedySearchForSolution() {
		Configuration firstLeave;
		Configuration solution = null;
		
		while (solution == null && !searchTree.isEmpty()) {
			firstLeave = searchTree.first();
			searchTree.remove(firstLeave);

			// Objective function
			if (firstLeave.getHeuristic() != 0) {
				generateChildren(firstLeave);
			} else {
				solution = firstLeave;
			}
		}
		return solution;
	}

	public LinkedList<Configuration> greedySearchForSolutionList() {
		Configuration firstLeave;
		Configuration solution = null;
		LinkedList<Configuration> solutionList = new LinkedList<Configuration>();

		System.out.println("COMEÇANDO ANÁLISE: ");
		startTimerStatus();
		
		while (!searchTree.isEmpty() ) {
			firstLeave = searchTree.first();
			searchTree.remove(firstLeave);

			boolean check = true;
			String currentHash = firstLeave.toString();
			for (String visitedHash : visitedNodes) {
				if (currentHash.equals(visitedHash)) {
					check = false;
				}
			}

			if (check) {
				visitedNodes.add(firstLeave.toString());

				if (firstLeave.getHeuristic() != 0) {
					generateChildren(firstLeave);
				} else {
					solution = firstLeave;
					solutionList.add(solution);
				}

//				printProgress();
			}
		}
		stopTimerStatus();
		return solutionList;
	}

	private void printProgress() {
		if (visitedNodes.size() % 1000 == 0) {
			System.out.println("\n----------------------------------------");
			System.out.println("Estados Analisados: " + visitedNodes.size());
			System.out.println("----------------------------------------");
		} else {
			if (pIndex == 5) {
				if (pBlockIndex == 19) {
					System.out.println(" ");
					pBlockIndex = 0;
					pIndex = 0;
				}
				else {
					System.out.print(" ");
					pIndex = 0;
					pBlockIndex++;
				}
			}
			System.out.print(".");
			pIndex++;
		}
		
	}

	public static String pad(int i) {
		String s = "";
		if (s.length() == 1) {
			s += " ";
		} else if (s.length() == 2) {
			s += "  ";
		}
		return s;
	}

	/**
	 * Successor function
	 * 
	 * @param currentBranch
	 */
	private void generateChildren(Configuration currentBranch) {
		Configuration child;
		for (int i = 0; i < BOARD_SIZE - 1; i++) {
			for (int j = i + 1; j < BOARD_SIZE; j++) {
				child = new Configuration(currentBranch, i, j);
				addChildToTree(child);
			}
		}
	}

	private void addChildToTree(Configuration child) {
		searchTree.add(child);
	}
	
	
	private void startTimerStatus() {
		statusTimer = new Timer();
		statusTimer.scheduleAtFixedRate(new PrintStatus(), 0, 2000);
	}
	
	private void stopTimerStatus() {
		if (statusTimer != null) {
			statusTimer.cancel();
		}
	}
	
	class PrintStatus extends TimerTask {
		public void run() {
			System.out.println(" Estados analisados: "+ visitedNodes.size());
		}
	}
}