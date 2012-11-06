package br.fbv.ia.busca;

import java.util.Arrays;


public class Estado extends BaseEstado<Estado> {

	private String[][] matriz;
	


	public Estado(String[][] matriz) {
		super();
		this.matriz = matriz;
	}
	

	public String[][] getMatriz() {
		return matriz;
	}

	public void setMatriz(String[][] matriz) {
		this.matriz = matriz;
	}

	
	@Override
	public String toString() {
		return Arrays.deepToString(matriz);
	}


	@Override
	public boolean isValido() {
		return matriz != null;
	}
}
