package br.fbv.rcbop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Adeline {

	private Reader readerX1;
	private Reader readerX2;
	private Reader readerYd;

	double[] dataX1;
	double[] dataX2;
	double[] dataYd;

	double learning = 0.1;

	public void initData() {

		try {
			FileReader fileX1 = new FileReader("x1");
			FileReader fileX2 = new FileReader("x2");
			FileReader fileYd = new FileReader("yd");
			
			readerX1 = new BufferedReader(fileX1);
			dataX1 = new double[5];
			
			for (int i = 0; i < 5; i++) {
				dataX1[i] = readerX1.read();
			}

			readerX2 = new BufferedReader(fileX2);
			dataX2 = new double[5];

			for (int i = 0; i < 5; i++) {
				dataX2[i] = readerX2.read();
			}

			readerYd = new BufferedReader(fileYd);
			dataYd = new double[5];

			for (int i = 0; i < 5; i++) {
				dataYd[i] = readerYd.read();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startAdeline() {
		Perceptron adeline = new Perceptron();

		adeline.w1 = Math.random() * 10;
		adeline.w2 = Math.random() * 10;

		calcNext(adeline, 0);
	}

	private void calcNext(Perceptron adeline, int index) {

		int dataIndex;
		if (index <= 5) {
			dataIndex = 0;
		} else {
			dataIndex = index;
		}

		adeline.function = new Function();

		adeline.function.x1 = dataX1[dataIndex];
		adeline.function.x2 = dataX2[dataIndex];
		adeline.function.y = dataYd[dataIndex];

		adeline.yo = calcYo(adeline);
		adeline.error = Math.abs(adeline.function.y - adeline.yo);

		Perceptron newAdeline = new Perceptron();

		newAdeline.w1 = adeline.w1 + difW1(adeline, learning);
		newAdeline.w2 = adeline.w2 + difW2(adeline, learning);

		if (Math.abs(adeline.w1 - newAdeline.w1) > 0.01) {
			calcNext(newAdeline, index + 1);
		} else {
			System.out.println("<Fim> w1:" + adeline.w1 + " w2: " + adeline.w2);
		}
	}

	private double difW1(Perceptron adeline, double learn) {
		return adeline.function.x1 * learn * adeline.error;
	}

	private double difW2(Perceptron adeline, double learn) {
		return adeline.function.x2 * learn * adeline.error;
	}

	private double calcYo(Perceptron adeline) {
		return (adeline.w1 * adeline.function.x1)
				+ (adeline.w2 + adeline.function.x2);
	}

	public static void main(String[] args) {
		Adeline adeline = new Adeline();
		adeline.initData();
		adeline.startAdeline();
	}

	class Perceptron {
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
