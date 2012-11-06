package br.fbv.rcbop.knn.model;

public class Iris {
	float	lenghtSep, widthSep, lenghtPet, widthPet;
	String	actualType;
	String	predictedType;
	double	distance;

	public Iris(float lenghtSep, float widthSep, float lenghtPet, float widthPet, String type) {
		this.lenghtSep = lenghtSep;
		this.widthSep = widthSep;
		this.lenghtPet = lenghtPet;
		this.widthPet = widthPet;
		this.actualType = type;
	}

	public float getLenghtSep() {
		return lenghtSep;
	}

	public void setLenghtSep(float lenghtSep) {
		this.lenghtSep = lenghtSep;
	}

	public float getWidthSep() {
		return widthSep;
	}

	public void setWidthSep(float widthSep) {
		this.widthSep = widthSep;
	}

	public float getLenghtPet() {
		return lenghtPet;
	}

	public void setLenghtPet(float lenghtPet) {
		this.lenghtPet = lenghtPet;
	}

	public float getWidthPet() {
		return widthPet;
	}

	public void setWidthPet(float widthPet) {
		this.widthPet = widthPet;
	}

	public String getActualType() {
		return actualType;
	}

	public void setActualType(String actualType) {
		this.actualType = actualType;
	}

	public String getPredictedType() {
		return predictedType;
	}

	public void setPredictedType(String predictedType) {
		this.predictedType = predictedType;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
}
