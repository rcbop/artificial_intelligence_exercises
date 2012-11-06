package br.fbv.ia.dados.modelo;

public class Trecho {

	private String estacao1;
	private String estacao2;
	private int distancia;
	private Linha linha;
	
	
	public Trecho(String estacao1, String estacao2, int distancia) {
		this.inicializar(estacao1, estacao2, distancia, null);
	}

	
	public Trecho(String estacao1, String estacao2, int distancia, Linha linha) {
		this.inicializar(estacao1, estacao2, distancia, linha);
	}
	
	public void inicializar(String estacao1, String estacao2, int distancia, Linha linha) {
		this.estacao1 = estacao1;
		this.estacao2 = estacao2;
		this.distancia = distancia;
		this.linha = linha;
	}
	
	
	public String getEstacao1() {
		return estacao1;
	}


	public void setEstacao1(String estacao1) {
		this.estacao1 = estacao1;
	}


	public String getEstacao2() {
		return estacao2;
	}


	public void setEstacao2(String estacao2) {
		this.estacao2 = estacao2;
	}


	public int getDistancia() {
		return distancia;
	}


	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}


	public Linha getLinha() {
		return linha;
	}


	public void setLinha(Linha linha) {
		this.linha = linha;
	}
	
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return toString().equals(obj.toString());
	}

	@Override
	public String toString() {
		return estacao1 + "-" + estacao2;
	}
	
}
