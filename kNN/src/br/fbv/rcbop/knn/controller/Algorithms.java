package br.fbv.rcbop.knn.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Random;

import br.fbv.rcbop.knn.model.Iris;
import br.fbv.rcbop.knn.ui.IConsoleListener;
import br.fbv.rcbop.knn.util.MathUtils;
import br.fbv.rcbop.knn.util.Util;

public class Algorithms {
	private Iris[]				db;
	private Iris[]				testDb;

	private Iris[]				dbSetosa;
	private Iris[]				dbVersicolor;
	private Iris[]				dbVirginica;

	// Classes
	public final String			SETOSA		= "Iris-setosa";
	public final String			VERSICOLOUR	= "Iris-versicolor";
	public final String			VIRGINICA	= "Iris-virginica";

	// Distance calc methods
	public static final int		EUCLIDES	= 1;
	public static final int		MINKOWSKI	= 2;
	public static final int		MANHATTAN	= 3;

	public String				databasePath;
	private int[][]				confusionValues;

	private String				output;

	private IConsoleListener	listener;
	private int					success;
	private File				currentFile;
	private int					testPercentage;

	public Algorithms(String dbPath) {
		databasePath = dbPath;
		currentFile = new File(databasePath);
		output = new String();
	}

	public void readDatabase() throws IOException {
		writeConsoleBreakLine("==>  Iniciando leitura de base de dados");
		BufferedReader br = new BufferedReader(new FileReader(currentFile));

		db = new Iris[Util.countLines(databasePath)];

		dbSetosa = new Iris[50];
		dbVersicolor = new Iris[50];
		dbVirginica = new Iris[50];

		int currentSetosa = 0;
		int currentVersicolor = 0;
		int currentVirginica = 0;

		try {
			String line = br.readLine();
			String[] values;
			Iris iris;
			while (line != null) {
				values = line.split(",");
				iris = new Iris(Float.valueOf(values[0]), Float.valueOf(values[1]), Float.valueOf(values[2]),
						Float.valueOf(values[3]), values[4]);
				if (iris.getActualType().equals(SETOSA)) {
					dbSetosa[currentSetosa++] = iris;
				} else if (iris.getActualType().equals(VERSICOLOUR)) {
					dbVersicolor[currentVersicolor++] = iris;
				} else if (iris.getActualType().equals(VIRGINICA)) {
					dbVirginica[currentVirginica++] = iris;
				}
				line = br.readLine();
			}
		} finally {
			br.close();
			writeConsoleBreakLine("==>  Base de dados lida");
		}
	}

	public int[][] kNearestNeighbors(int k, int distanceCalcMethod, int p, int testPercentage) throws IOException {
		confusionValues = new int[3][3];
		success = 0;
		readDatabase();
		divideDatabase(testPercentage);
		writeConsoleBreakLine("==>  Embaralhando base de dados");
		db = Util.shuffleDatabase(db);
		writeConsoleBreakLine("==>  Base de dados embaralhada");
		
		writeConsoleBreakLine(" |---------     COMEÇANDO TESTES      ----------|");

		// Iterating over test base
		int i = 0;
		for (Iris input : testDb) {
			writeConsoleBreakLine("Analizando registro de teste: " + (1 + i++));
			// Calc index method
			switch (distanceCalcMethod) {
			case EUCLIDES:
				// iterating over learning base
				for (Iris current : db) {
					current.setDistance(MathUtils.calcEuclidianDistance(input, current));
				}
				break;
			case MINKOWSKI:
				// iterating over learning base
				for (Iris current : db) {
					current.setDistance(MathUtils.calcMinkowskiDistance(input, current, p));
				}
				break;
			case MANHATTAN:
				// iterating over learning base
				for (Iris current : db) {
					current.setDistance(MathUtils.calcManhattanDistance(input, current));
				}
				break;

			default:
				break;
			}
			// Sort per index
			writeConsole("QuickSort iniciado!");
			System.out.println();
			db = Util.basicQuickSort(0, db.length, db);
			writeConsoleBreakLine("QuickSort efetuado!");

			int count = 0, setosa = 0, versicolour = 0, virginica = 0;

			while (count++ < k) {
				if (db[count].getActualType().equals(SETOSA)) {
					setosa++;
				} else if (db[count].getActualType().equals(VERSICOLOUR)) {
					versicolour++;
				} else if (db[count].getActualType().equals(VIRGINICA)) {
					virginica++;
				}
			}
			int mode = Math.max(setosa, Math.max(versicolour, virginica));

			input.setPredictedType(feedConfusionMatrix(input, mode, setosa, versicolour, virginica));
		}
		writeConsoleBreakLine(" |---------     FIM DE ALGORÍTMO      -------------|");

		return confusionValues;
	}

	private String feedConfusionMatrix(Iris input, int classValue, int setosa, int versicolour, int virginica) {
		String rtn = "";
		if (classValue == setosa) {
			rtn = SETOSA;
			if (rtn.equals(input.getActualType())) {
				confusionValues[0][0]++;
				success++;
			} else {
				if (rtn.equals(VIRGINICA)) {
					confusionValues[0][1]++;
				} else {
					confusionValues[0][2]++;
				}
			}
		} else if (classValue == versicolour) {
			rtn = VERSICOLOUR;
			if (rtn.equals(input.getActualType())) {
				confusionValues[1][1]++;
				success++;
			} else {
				if (rtn.equals(VIRGINICA)) {
					confusionValues[1][2]++;
				} else {
					confusionValues[1][0]++;
				}
			}
		} else if (classValue == virginica) {
			rtn = VIRGINICA;
			if (rtn.equals(input.getActualType())) {
				confusionValues[2][2]++;
				success++;
			} else {
				if (rtn.equals(SETOSA)) {
					confusionValues[2][0]++;
				} else {
					confusionValues[2][1]++;
				}
			}
		}
		return rtn;
	}

	public void divideDatabase(int testPercent) {
		writeConsoleBreakLine("==>  Dividindo base de teste/aprendizado");
		testPercentage = testPercent;
		int testCoef = getTestSize(testPercent);
		testDb = new Iris[testCoef];
		for (int i = 0; i < testCoef/3; i++) {
			testDb[i] = dbSetosa[i];
		}
		for (int i = 0; i < testCoef/3; i++) {
			testDb[i] = dbVirginica[i];
		}
		for (int i = 0; i < testCoef/3 + 1; i++) {
			testDb[i] = dbVersicolor[i];
		}
		// Shift left
		Iris[] newDb = new Iris[db.length - testCoef];
		for (int i = 0; i < newDb.length; i++) {
			newDb[i] = db[i + testCoef];
		}
		db = newDb;
		writeConsoleBreakLine("==>  Base de dados dividida");
	}

	private void writeConsoleBreakLine(String s) {
		output = s + "\n";
		listener.updateLine();
	}

	private void writeConsole(String s) {
		output = s + " ";
		listener.updateLine();
	}

	public void setConsoleListener(IConsoleListener listener) {
		this.listener = listener;
	}

	public String getOutput() {
		return output;
	}

	public int getSuccess() {
		return success;
	}

	public int getFailure() {
		int testSize = 0;
		if (testDb != null) {
			testSize = testDb.length;
		}
		return testSize - success;
	}

	public double getSuccessRate() {
		return MathUtils.round(((double) success / testDb.length) * 100, 2, BigDecimal.ROUND_HALF_UP);
	}

	private int getTestSize(int testPercent) {
		return (int) (db.length * ((double) testPercent / 100));
	}

}
