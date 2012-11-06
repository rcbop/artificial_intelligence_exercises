package br.fbv.ia.busca;

import br.fbv.ia.dados.modelo.Linha;


public class Estado extends BaseEstado<Estado> implements Comparable<Estado> {

	private String estacao;
	private Linha linhaOrigem;
	private boolean trocouLinha;
	private double heuristica;
	

	public Estado(String estacao) {
		super();
		this.estacao = estacao;
	}
	
	
	public String getEstacao() {
		return estacao;
	}

	public void setEstacao(String estacao) {
		this.estacao = estacao;
	}

	
	public Linha getLinhaOrigem() {
		return linhaOrigem;
	}

	public void setLinhaOrigem(Linha linhaOrigem) {
		this.linhaOrigem = linhaOrigem;
	}

	public boolean trocouLinha() {
		return trocouLinha;
	}

	public void setTrocouLinha(boolean trocouLinha) {
		this.trocouLinha = trocouLinha;
	}

	@Override
	public String toString() {
		return estacao.toString();
	}

	@Override
	public int compareTo(Estado o) {
		return Double.compare(getHeuristica(), o.getHeuristica());
	}

	@Override
	public boolean isValido() {
		return (estacao != null && !estacao.isEmpty());
	}
	
	public double getHeuristica() {
		return heuristica;
	}


	public void setHeuristica(double heuristica) {
		this.heuristica = heuristica;
	}

}
