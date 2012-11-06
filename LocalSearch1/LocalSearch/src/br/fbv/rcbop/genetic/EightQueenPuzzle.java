package br.fbv.rcbop.genetic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rogério Peixoto (rcbpeixoto@gmail.com)
 */
public class EightQueenPuzzle {

	private List<Configuration>		population		= null;
	public static final int		BOARD_SIZE		= 8;
	public static final int		GENOTYPE_SIZE	= BOARD_SIZE * 3;
	public static final int		INITIAL_POPULATION_SIZE = 10;

	public EightQueenPuzzle() {
		population = new ArrayList<Configuration>();
		// Initial State (Problem formulation)
		generateRandomStart();
	}

	public void generateRandomStart() {
		//Mirror children
		for (int i = 0; i < INITIAL_POPULATION_SIZE / 2; i++) {
			population.add(new Configuration(generateRandomChildren()));
		}
		generateOposingChildren();
	}

	public char[] generateRandomChildren(){
		char[] genotype = new char[GENOTYPE_SIZE];
		for (int i = 0; i < GENOTYPE_SIZE; i++) {
			genotype[i] = Util.getRandomBit();
		}
		return genotype;
	}
	
	public void generateOposingChildren(){
		char[] oposingGen;
		for (Configuration child : population) {
			oposingGen = generateOposingGen(child);
			population.add(new Configuration(oposingGen));
		}
	}

	private char[] generateOposingGen(Configuration child) {
		char[] genotype;
		char[] oposingGen;
		genotype = child.getGenotype();
		oposingGen = new char[GENOTYPE_SIZE];
		for (int i = 0; i < GENOTYPE_SIZE; i++) {
			char bit = genotype[i];
			oposingGen[i] = Util.getOpositeBit(bit); 
		}
		return oposingGen;
	}

	public Configuration survivalOfFittestTournament() {
		Configuration solution = null;
		List<Configuration> best = null;		
		while(solution == null && !population.isEmpty()){
			best = new ArrayList<Configuration>();
			best.add(population.get(0));
			best.add(population.get(1));
			
			
		}
		return null;
	}
	
	

}
