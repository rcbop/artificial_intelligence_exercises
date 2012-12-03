package br.fbv.rcbop.perceptron;

public class ProblemClass {

	private double	attrA;
	private double	attrB;

	private int		predictedClass;
	private int		realClass;

	private double	error;

	public ProblemClass(Double attrA, Double attrB, Integer realClass) {
		super();
		this.attrA = attrA;
		this.attrB = attrB;
		this.realClass = realClass;
	}

	public double getAttrA() {
		return attrA;
	}

	public void setAttrA(double attrA) {
		this.attrA = attrA;
	}

	public double getAttrB() {
		return attrB;
	}

	public void setAttrB(double attrB) {
		this.attrB = attrB;
	}

	public int getPredictedClass() {
		return predictedClass;
	}

	public void setPredictedClass(int predictedClass) {
		this.predictedClass = predictedClass;
	}

	public int getRealClass() {
		return realClass;
	}

	public void setRealClass(int realClass) {
		this.realClass = realClass;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

}
