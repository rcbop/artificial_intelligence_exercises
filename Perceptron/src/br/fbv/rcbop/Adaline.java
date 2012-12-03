package br.fbv.rcbop;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Adaline {

	private String X1DB = "x1";
	private String X2DB = "x2";
	private String YDDB = "yd";

	double[] dataX1;
	double[] dataX2;
	double[] dataYd;

	double learning = 0.01;
	private int dataIndex;

	public void initData() {

		try {

			BufferedReader readerX1 = new BufferedReader(new FileReader(X1DB));
			dataX1 = new double[Util.countLines(X1DB)];

			for (int i = 0; i < 5; i++) {
				dataX1[i] = Double.parseDouble(readerX1.readLine());
			}
			readerX1.close();

			BufferedReader readerX2 = new BufferedReader(new FileReader(X2DB));
			dataX2 = new double[Util.countLines(X2DB)];

			for (int i = 0; i < 5; i++) {
				dataX2[i] = Double.parseDouble(readerX2.readLine());
			}
			readerX2.close();

			BufferedReader readerYd = new BufferedReader(new FileReader(YDDB));
			dataYd = new double[Util.countLines(YDDB)];

			for (int i = 0; i < 5; i++) {
				dataYd[i] = Double.parseDouble(readerYd.readLine());
			}
			readerYd.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startAdeline() {
		AdalineData adaline = new AdalineData();

		adaline.w1 = Math.round(Math.random() * 10);
		adaline.w2 = Math.round(Math.random() * 10);

		calcNext(adaline, 0);
	}

	private void calcNext(AdalineData adaline, int index) {

		if (index % 5 == 0) {
			dataIndex = 0;
		} else {
			dataIndex++;
		}

		adaline.function = new Function();

		adaline.function.x1 = dataX1[dataIndex];
		adaline.function.x2 = dataX2[dataIndex];
		adaline.function.y = dataYd[dataIndex];

		adaline.yo = calcYo(adaline);
		adaline.error = adaline.function.y - adaline.yo;

		AdalineData newAdeline = new AdalineData();

		newAdeline.w1 = adaline.w1 + difW1(adaline, learning);
		newAdeline.w2 = adaline.w2 + difW2(adaline, learning);

		if (!(Math.abs(adaline.w1 - newAdeline.w1) < 0.0001 && Math.abs(adaline.w2 - newAdeline.w2) < 0.0001)) {
			calcNext(newAdeline, index + 1);
		} else {
			System.out.println("<Fim> w1: " + adaline.w1 + " w2: " + adaline.w2);
		}
	}

	private double difW1(AdalineData adeline, double learn) {
		return adeline.function.x1 * learn * adeline.error;
	}

	private double difW2(AdalineData adeline, double learn) {
		return adeline.function.x2 * learn * adeline.error;
	}

	private double calcYo(AdalineData adeline) {
		return (adeline.w1 * adeline.function.x1)
				+ (adeline.w2 + adeline.function.x2);
	}

	public static void main(String[] args) {
		Adaline adaline = new Adaline();
		adaline.initData();
		adaline.startAdeline();
	}

	class AdalineData {
		
		private Function function;

		private double w1;
		private double w2;

		private double yo;

		private double error;
	}

	class Function {
		private double x1;
		private double x2;

		private double y;
	}
}
