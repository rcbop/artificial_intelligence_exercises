package br.fbv.rcbop.knn.controller;

import java.io.IOException;

import br.fbv.rcbop.knn.ui.IConsoleListener;

public class Facade {

	private static Facade	instance;
	private Algorithms		algorithms;
	
	public static final int MINKOWSKI = Algorithms.MINKOWSKI;

	private final String	IRIS_DATA	= "database/iris.data";
	
	private static final int TEST_PERCENTAGE = 25;

	private Facade() {
		algorithms = new Algorithms(IRIS_DATA);
	}

	public static Facade getInstance() {
		if (instance == null) {
			instance = new Facade();
		}
		return instance;
	}

	public int[][] initAlgorithmAndCalculateClass(int k, int distMode, int p) throws IOException {
		return algorithms.kNearestNeighbors(k, distMode, p, TEST_PERCENTAGE);
	}
	
	public String getOutput(){
		return algorithms.getOutput();
	}
	
	public void setConsoleListener(IConsoleListener listener){
		algorithms.setConsoleListener(listener);
	}
	
	public int getSuccess(){
		return algorithms.getSuccess();
	}
	
	public int getFailure(){
		return algorithms.getFailure();
	}
	
	public double getSuccessRate(){
		return algorithms.getSuccessRate();
	}
	
	public int getTestSize() throws IOException{
//		return algorithms.getTestSize(TEST_PERCENTAGE);
		return 0;
	}

}
