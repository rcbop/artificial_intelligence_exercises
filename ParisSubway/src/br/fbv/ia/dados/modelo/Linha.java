package br.fbv.ia.dados.modelo;

public enum Linha {
	Azul(1),
	Amarelo(2),
	Vermelho(3),
	Verde(4);
	
	final int id;
	
	private Linha(int id) {
		this.id = id;
	}
	
	public int id() {
		return id;
	}
	
	public static Linha valueOf(int id) {
		for (Linha l : values()) {
			if (l.id == id)
				return l;
		}
		return null;
	}
}
