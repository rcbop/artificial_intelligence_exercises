package br.fbv.rcbop.perceptron;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import br.fbv.rcbop.util.Util;

public class PerceptronRepo {

	private static PerceptronRepo	instance;
	private int						currentClass1;
	private int						currentClass2;

	private PerceptronRepo() {
	}

	public static PerceptronRepo getInstance() {
		if (instance == null) {
			instance = new PerceptronRepo();
		}
		return instance;
	}

	public ProblemClass[] readPerceptronDatabase(String databasePath) throws IOException {
		int dbSize = Util.countLines(databasePath);
		BufferedReader br = new BufferedReader(new FileReader(new File(databasePath)));

		ProblemClass[] db = new ProblemClass[dbSize];

		currentClass1 = 0;
		currentClass2 = 0;
		int index = 0;
		
		try {
			String line = br.readLine();
			String[] values;
			ProblemClass newClass;
			while (line != null) {
				values = line.split(",");
				newClass = new ProblemClass(Double.valueOf(values[0]), Double.valueOf(values[1]),
						Integer.valueOf(values[2]));
				db[index++] = newClass;
				if (newClass.getRealClass() == 1) {
					currentClass1++;
				} else {
					currentClass2++;
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		db = (ProblemClass[]) Util.shuffleDatabase(db);
		return db;
	}
	
	public int getCurrentClass1() {
		return currentClass1;
	}

	public int getCurrentClass2() {
		return currentClass2;
	}

}
