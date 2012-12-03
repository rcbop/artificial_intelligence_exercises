package br.fbv.rcbop.perceptron;

import java.io.IOException;

public class Perceptron {

	private final String	TRAINING_DATABASE_PATH	= "adeline/database";
	private final String	TEST_DATABASE_PATH		= "perceptron/database";

	private double			weightA;
	private double			weightB;

	private double			beas;
	private double			weightBeas;

	private float			learning;

	private ProblemClass[]	dbTraining;
	private ProblemClass[]	dbTest;
	private PerceptronRepo	dao;

	public Perceptron(int learning) {
		dao = PerceptronRepo.getInstance();
		this.learning = learning;
	}

	public void initWeights() {
		weightA = Math.random();
		weightB = Math.random();
		weightBeas = Math.random();
	}

	public void doTraining() {
		try {
			dbTraining = dao.readPerceptronDatabase(TRAINING_DATABASE_PATH);
			
			for (int i = 0; i < dbTraining.length; i++) {
				if (calculateActivation(dbTraining[i]) == 1) {
					dbTraining[i].setPredictedClass(1);
				}else {
					dbTraining[i].setPredictedClass(2);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doTesting() {
		try {
			dbTest = dao.readPerceptronDatabase(TEST_DATABASE_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public double calculateSum(ProblemClass pc){
		return (pc.getAttrA() * weightA  + pc.getAttrB() * weightB);
	}
	
	public int calculateActivation(ProblemClass pc){
		if (calculateSum(pc) > weightBeas) {
			return 1;
		}
		else{
			return -1;
		}
	}
	
	public double calculateError(ProblemClass pc){
		return pc.getRealClass() - pc.getPredictedClass();
	}
	
	public void adjustWeights(){
		
	}
}
