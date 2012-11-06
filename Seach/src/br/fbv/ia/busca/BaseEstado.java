package br.fbv.ia.busca;

import java.util.ArrayList;
import java.util.List;


/**
 * Representação de um estado possível
 */
@SuppressWarnings("rawtypes")
public abstract class BaseEstado <T extends BaseEstado> {
	
	private List<T> filhos;
	private T pai;
	private double custo;
	
	
	public BaseEstado() {
		this.filhos = new ArrayList<T>();
		this.custo = 0;
		this.pai = null;
	}
	
	
	/**
	 * Existem filhos para este estado
	 */
	public boolean temFilhos() {
		return !filhos.isEmpty();
	}

	/**
	 * Filhos deste estado
	 */
	public List<T> getFilhos() {
		return filhos;
	}

	/**
	 * Altera os filhos deste estado
	 */
	public void setFilhos(List<T> filhos) {
		this.filhos = filhos;
	}

	/**
	 * Estado que deu origem a este estado
	 */
	public T getPai() {
		return pai;
	}

	/**
	 * Altera o estado que deu origem a este estado
	 */
	public void setPai(T pai) {
		this.pai = pai;
	}

	/**
	 * Custo total até a obtenção deste estado
	 */
	public double getCusto() {
		return custo;
	}

	/**
	 * Altera o custo total até a obtenção deste estado
	 */
	public void setCusto(double custo) {
		this.custo = custo;
	}

	/**
	 * Indica se o estado está válido
	 */
	public abstract boolean isValido();
	
	
	@Override
	public boolean equals(Object obj) {
		return (this.toString().equals(obj.toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public abstract String toString();
	
}
