package br.fbv.rcbop.genetic;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Rogério Peixoto (rcbpeixoto@gmail.com)
 */
public class EightQueenPuzzle {

	private SortedSet<Configuration>		population				= null;
	public static final int				BOARD_SIZE				= 8;
	public static final int				GENOTYPE_SIZE			= BOARD_SIZE * 3;
	public static final int				INITIAL_POPULATION_SIZE	= 10;

	public EightQueenPuzzle() {
		population = new TreeSet<Configuration>();
		// Initial State (Problem formulation)
		generateRandomStart();
	}

	public void generateRandomStart() {
		for (int i = 0; i < INITIAL_POPULATION_SIZE / 2; i++) {
			population.add(new Configuration(generateRandomChildren()));
		}
		generateOposingChildren();
	}

	public char[] generateRandomChildren() {
		char[] genotype = new char[GENOTYPE_SIZE];
		for (int i = 0; i < GENOTYPE_SIZE; i++) {
			genotype[i] = getRandomBit();
		}
		return genotype;
	}

	public void generateOposingChildren() {
		char[] genotype;
		char[] oposingGen;
		for (Configuration child : population) {
			genotype = child.getGenotype();
			oposingGen = new char[GENOTYPE_SIZE];
			for (int i = 0; i < GENOTYPE_SIZE; i++) {
				char bit = genotype[i];
				oposingGen[i] = getOpositeBit(bit);
			}
			population.add(new Configuration(oposingGen));
		}
	}

	/**
	 * 90% Crossover + 10% Elitism
	 * */
	public Configuration tournament() {
		Configuration solution = null;
		Configuration fittest1 = null;
		Configuration fittest2 = null;
		while(solution == null){
			fittest1 = population.first();
			population.remove(fittest1);
			fittest2 = population.first();
			population.remove(fittest2);
			population = crossoverPopulation();
		}
		return null;
	}

	private SortedSet<Configuration> crossoverPopulation() {
		SortedSet<Configuration> newPopulation;
		for (Configuration individual : population) {
		}
		
		
		return null;
	}
	
	private SortedSet<Configuration> crossover(Configuration individual1, Configuration individual2){
		Configuration child1;
		Configuration child2;
		
		
		
		return null;
	}
	
	private char[] split(char[] gen1, char[] gen2){
		int crossingPoint = getRandomInt();
		char[] result;
		result = Arrays.copyOf(gen1, crossingPoint);
		return result;
	}
	
	public char getRandomBit() {
		return (Math.random() < 0.5 ? '1' : '0');
	}

	public char getOpositeBit(char bit) {
		return ((bit == 0) ? '1' : '0');
	}
	
	public int getRandomInt(){
		return (int) Math.random();
	}

}
