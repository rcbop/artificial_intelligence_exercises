package br.fbv.ia.busca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import br.fbv.ia.excecao.EstadoIndefinidoException;
import br.fbv.ia.util.Constantes;


/**
 * Representação do algoritmo de Busca em Largura
 */
public class BuscaLargura extends BaseBusca<Estado> {

	
	public BuscaLargura() {
		super();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void buscar(Estado inicio, Estado objetivo, boolean imprimeCaminho) throws EstadoIndefinidoException {
		boolean sucesso = false;
		
		try {
			super.inicio = inicio;
			super.objetivo = objetivo;
			super.imprimeCaminho = imprimeCaminho;
			super.tempoInicial = System.currentTimeMillis();
			super.qtdEstadosAnalisados = 0;
			
			validarEstados();
			System.out.println("Realizando busca...");

			iniciarTimerStatus();
			sucesso = buscarComPoda();
			
			if (!sucesso) {
				System.err.println("A busca não obteve êxito.");
			}
		} 
		finally {
			pararTimerStatus();
		}
	}
	
	
	/**
	 * {@inheritDoc} 
	 */
	protected boolean buscarComPoda() {
		Estado encontrado = null;
		Estado noAtual = null;
		String nomeNo;
		Queue<Estado> folhas = new LinkedList<Estado>();
		Set<String> visitados = new HashSet<String>();
		folhas.add(inicio);	
		
		while (!folhas.isEmpty() && encontrado == null) {
			noAtual = folhas.poll();
			nomeNo = noAtual.toString();
			
			if (!visitados.contains(nomeNo)) {
				if (imprimeCaminho)
					imprimirEstado(noAtual);
				
				if (isObjetivo(noAtual)) {
					encontrado = noAtual;
					mostrarResultado(noAtual);
					break;
				}
				else {
					noAtual.setFilhos(gerarFilhos(noAtual));
					folhas.addAll(noAtual.getFilhos());
				}
				visitados.add(nomeNo);
				
				qtdEstadosAnalisados++;
			}
		}
		return (encontrado != null);
	}

	
	/**
	 * {@inheritDoc}
	 */
	protected boolean isObjetivo(Estado e) {
		return e.equals(objetivo);
	}
	

	/**
	 * {@inheritDoc}
	 */
	protected List<Estado> gerarFilhos(Estado e) {
		String[][] matriz = e.getMatriz();
		List<Estado> filhos = new ArrayList<Estado>();
		Posicao vazia = encontrarPosicaoVazia(matriz);

		for (Direcao d : Direcao.values()) {
			if (podeMover(vazia, d, matriz)) {
				String[][] mFilho = mover(vazia, d, matriz);
				Estado filho = new Estado(mFilho);
				filho.setPai(e);
				filho.setCusto(e.getCusto() + 1);
				filhos.add(filho);
			}
		}
		return filhos;
	}

	/**
	 * Verifica se o movimento na direção informada pode ser realizado
	 * sem exceder os limites da matriz.
	 * @param p Posição a mover
	 * @param d Direção para mover
	 * @param matriz Matriz original
	 * @return <code>true</code> Se a posição pode ser movida. <br/>
	 * 		   <code>false</code> Se o movimento não puder ser realizado.
	 */
	private boolean podeMover(Posicao p, Direcao d, String[][] matriz) {
		Posicao novaPosicao = moverPosicao(p, d);
		int limiteLinha = matriz.length - 1;
		int limiteColuna = matriz[0].length - 1;

		if (novaPosicao.getLinha() < 0 || novaPosicao.getLinha() > limiteLinha) {
			return false;
		}
		else if (novaPosicao.getColuna() < 0 || novaPosicao.getColuna() > limiteColuna) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Obtém uma nova posição movida na direção informada.
	 * @return Nova posição após o movimento
	 */
	private Posicao moverPosicao(Posicao p, Direcao d) {
		int novaLinha = p.getLinha() + d.dLinha();
		int novaColuna = p.getColuna() + d.dColuna();
		return new Posicao(novaLinha, novaColuna);
	}

	/**
	 * Realiza o movimento da posição informada na matriz, retornando uma nova
	 * matriz modificada.
	 * @param p Posição a mover
	 * @param d Direção para mover <param>p</param>
	 * @param matriz Matriz original
	 * @return Nova matriz com <param>p</param> movido na direção <param>d</param>
	 */
	private String[][] mover(Posicao p, Direcao d, String[][] matriz) {
		String[][] novaMatriz = new String[matriz.length][];
		Posicao novaPosicao = moverPosicao(p, d);

		//Guarda os itens a mover:
		String item1 = matriz[p.getLinha()][p.getColuna()];
		String item2 = matriz[novaPosicao.getLinha()][novaPosicao.getColuna()];

		for (int i = 0; i < matriz.length; i++) {
			novaMatriz[i] = matriz[i].clone();
		}

		//Inverte a posição dos itens:
		novaMatriz[p.getLinha()][p.getColuna()] = item2;
		novaMatriz[novaPosicao.getLinha()][novaPosicao.getColuna()] = item1;

		return novaMatriz;
	}

	/**
	 * Encontra a posição do item vazio
	 */
	private Posicao encontrarPosicaoVazia(String[][] matriz) {
		Posicao pos = null;

		for (short i = 0; i < matriz.length; i++) {
			for (short j = 0; j < matriz[i].length; j++) {
				if (matriz[i][j].equals(Constantes.VAZIO)) {
					pos = new Posicao(i, j);
					break;
				}
			}
		}
		return pos;
	}
	
	
	
	protected void imprimirEstado(Estado e) {
		System.out.println(montarStringMatriz(e));
	}
	
	private String montarStringMatriz(Estado e) {
		String str = "";
		
		for (int i = 0; i < e.getMatriz().length; i++) {
			str += Arrays.deepToString(e.getMatriz()[i]);
			str += "\n";
		}
		return str;
	}
	
}
