package br.fbv.rcbop.genetic;

	import java.io.IOException;
	import java.util.Arrays;
	import java.util.LinkedList;
	import java.util.Scanner;
	import java.util.Stack;

	public class Main {		
		public final static int OPTION1 = 1;
		public final static int OPTION2 = 2;
		
		public static void main(String[] args) {
			Scanner terminal = new Scanner(System.in);

			System.out.println("--------------------------");
			System.out.println("  PROBLEMA DAS 8 RAINHAS  ");
			System.out.println("--------------------------");
			
			int option = terminal.nextInt();
			terminal.close();

			switch (option) {
			
			case OPTION1:
				
				EightQueenPuzzle puzzle = new EightQueenPuzzle();
				// Imprimindo todas as soluções
				// (demora um pouco, aproximadamente 40mil (8!) estados serem analisados)
				solveAllSolutions(puzzle);
				break;
			case OPTION2:
				
				EightQueenPuzzle puzzle2 = new EightQueenPuzzle();
				// Imprimindo uma solução completa
				solveFirstSolution(puzzle2);
				break;
			default:
				break;
			}

		}

		private static void solveFirstSolution(EightQueenPuzzle puzzle) {
			long initTime = System.currentTimeMillis();
//			Configuration solution = puzzle.applyTournamentForFitest();
			long endTime = System.currentTimeMillis();
			long durationTime = endTime - initTime;
//			printSolutionStats(solution, durationTime);
		}

		private static void solveAllSolutions(EightQueenPuzzle puzzle) {
			long initTime = System.currentTimeMillis();
//			LinkedList<Configuration> solutionList = puzzle.greedySearchForSolutionList();
			long endTime = System.currentTimeMillis();
			long durationTime = endTime - initTime;
//			printSolutionList(solutionList, durationTime);

		}

		public static void printSolutionList(LinkedList<Configuration> solutionList, long durationTime) {
			try {
				if (isUnix()) {
					Runtime.getRuntime().exec("clear");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("\n");
			System.out.println("TEMPO DECORRIDO DE ANÁLISE: " + (double) durationTime / 1000 + "s");
			System.out.println("QUANTIDADE DE SOLUÇÕES ENCONTRADAS: " + solutionList.size());
			System.out.println("-----------------------------------------------------------");
			System.out.println("----------------------- Soluções --------------------------");
			System.out.println("-----------------------------------------------------------");
			int i = 1;
			for (Configuration configuration : solutionList) {
				System.out.println("[" + (i++) + "] " + Arrays.toString(configuration.getPhenotype()));
			}
		}
		
		public static boolean isWindows() {
			String os = System.getProperty("os.name").toLowerCase();
			return (os.indexOf("win") >= 0);
		}
	 
		public static boolean isUnix() {
			String os = System.getProperty("os.name").toLowerCase();
			return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	 
		} 

		public static void printSolutionStats(Configuration solution, long durationTime) {

			System.out.println("\n-----------------------------------------------------------");
			System.out.println("--------- RESOLVENDO PASSO A PASSO UMA SOLUÇÃO ------------");
			System.out.println("-----------------------------------------------------------\n");
			System.out.println("TEMPO DECORRIDO DE ANÁLISE: " + (double) durationTime / 1000 + "s");

			Stack<Configuration> solutionStack = new Stack<Configuration>();

			Configuration parent = solution;
			while (parent != null) {
				solutionStack.push(parent);
//				parent = parent.getParent();
			}

			int depth = solutionStack.size();

			System.out.println("RESOLVIDO EM " + depth + " MOVIMENTOS\n");
			System.out.println("MOVIMENTOS: ");

			Configuration currentState;
			int i = 0;
			while (!solutionStack.isEmpty()) {
				System.out.print("[" + ((i++) + 1) + "] ");
				currentState = solutionStack.pop();
				System.out.println(Arrays.toString(currentState.getPhenotype()));
			}
			System.out.println();
		}
	}
