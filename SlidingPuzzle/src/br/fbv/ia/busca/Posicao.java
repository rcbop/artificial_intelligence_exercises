package br.fbv.ia.busca;

import java.text.MessageFormat;

/**
 * Representação da posição do item em uma matriz 
 * em termos de linhas e colunas
 */
public class Posicao {

	private int linha;
	private int coluna;
	
	
	public Posicao(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public int getLinha() {
		return linha;
	}

	public void setLinha(int linha) {
		this.linha = linha;
	}

	public int getColuna() {
		return coluna;
	}

	public void setColuna(int coluna) {
		this.coluna = coluna;
	}

	@Override
	public String toString() {
		return MessageFormat.format("[{0}][{1}]", linha, coluna);
	}

	@Override
	public boolean equals(Object obj) {
		return  toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	
}
