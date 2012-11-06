package br.fbv.rcbop.genetic;

import java.util.ArrayList;
import java.util.List;

public class Util {

	public static char getRandomBit() {
		return (Math.random() < 0.5 ? '1' : '0');
	}

	public static char getOpositeBit(char bit) {
		return ((bit == 0) ? '1' : '0');
	}

	public static int getRandomCrossingPoint() {
		return (int) ((Math.random() * 10) * 2.7); 
	}

	public void splitGen(char[] parent1, char[] parent2) {
		int crossPoint = getRandomCrossingPoint();
		char[] temp1;
		char[] temp2;
		
		for (int i = 0; i < crossPoint; i++) {
			
		}
	}
	
	public void sortPopulation(List<Configuration> population){
		Configuration current;
		Configuration fittest = population.get(0);
		List<Configuration> sortedPopulation = new ArrayList<Configuration>();
		for (int i = 0; i < population.size(); i++) {
			current = population.get(0);
			if (current.getHeuristic() < fittest.getHeuristic()) {
				fittest = current;
			}
		}
	}

}
