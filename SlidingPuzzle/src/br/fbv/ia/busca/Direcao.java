package br.fbv.ia.busca;

/**
 * Deslocamento para mover um item de uma posção para outra
 */
public enum Direcao {
	ACIMA	(-1,  0),
	DIREITA	( 0,  1),
	ABAIXO	( 1,  0),
	ESQUERDA( 0, -1);
	
	private final short _dx;
	private final short _dy;
	
	
	private Direcao(int dx, int dy) {
		this._dx = (short) dx;
		this._dy = (short) dy;
	}
	
	/**
	 * Deslocamento no sentido das linhas necessário 
	 * para mover nesta direção
	 */
	public short dLinha() {
		return _dx;
	}

	/**
	 * Deslocamento no sentido das colunas necessário 
	 * para mover nesta direção
	 */
	public short dColuna() {
		return _dy;
	}	
}
