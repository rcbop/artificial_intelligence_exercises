package br.fbv.rcbop.knn.util;

import java.math.BigDecimal;

import br.fbv.rcbop.knn.model.Iris;

public class MathUtils {
	
	public static double calcEuclidianDistance(Iris input, Iris current) {
		double sum = (input.getLenghtSep() - current.getLenghtSep()) + (input.getWidthSep() - current.getWidthSep())
				+ (input.getLenghtPet() - current.getLenghtPet()) + (input.getWidthPet() - current.getWidthPet());
		return Math.sqrt(sum);
	}

	public static double calcMinkowskiDistance(Iris input, Iris current, int p) {
		double sum = (input.getLenghtSep() - current.getLenghtSep()) + (input.getWidthSep() - current.getWidthSep())
				+ (input.getLenghtPet() - current.getLenghtPet()) + (input.getWidthPet() - current.getWidthPet());
		return Math.pow(sum, 1 / p);
	}

	public static double calcManhattanDistance(Iris input, Iris current) {
		return Math.abs(input.getLenghtSep() - current.getLenghtSep()) + Math.abs(input.getWidthSep() - current.getWidthSep())
				+ Math.abs(input.getLenghtPet() - current.getLenghtPet()) + Math.abs(input.getWidthPet() - current.getWidthPet());
	}
	
	public static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}
}
