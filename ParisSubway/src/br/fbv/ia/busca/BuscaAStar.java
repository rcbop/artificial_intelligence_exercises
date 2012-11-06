package br.fbv.ia.busca;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import br.fbv.ia.dados.BaseDados;
import br.fbv.ia.dados.modelo.Trecho;
import br.fbv.ia.excecao.EstadoIndefinidoException;

public class BuscaAStar extends BaseBusca<Estado> {
	
	//km/h
	private final double VELOCIDADE  = 30d; 
	//'5 min' convertido para horas
	private final double TEMPO_TROCA = 5d / 60d;	
	//Distancia equivalente aos '5 min'
	private final double PESO_TROCA  = VELOCIDADE * TEMPO_TROCA; 

	private BaseDados dados;
	private List<Estado> folhas;
	
	/**
	 * Representação do algoritmo de Busca A*
	 */
	public BuscaAStar() {
		super();
		this.dados = new BaseDados();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void buscar(Estado inicio, Estado objetivo, boolean imprimeCaminho) throws EstadoIndefinidoException {
		boolean sucesso = false;
		
		try {
			this.dados.conectar();

			super.inicio = inicio;
			super.objetivo = objetivo;
			super.imprimeCaminho = imprimeCaminho;
			super.tempoInicial = System.currentTimeMillis();
			super.qtdEstadosAnalisados = 0;
			
			validarEstados();
			System.out.println(MessageFormat.format(
							"Realizando busca... \"{0}\" -> \"{1}\"",
							((Estado)inicio).getEstacao(), ((Estado)objetivo).getEstacao()));
			
			iniciarTimerStatus();
			sucesso = buscarComPoda();
			
			if (!sucesso) {
				System.err.println("A busca não obteve êxito.");
			}
		} 
		catch (EstadoIndefinidoException e) {
			throw e;
		} 
		catch (SQLException e) { 
			
		} 
		catch (Exception e) {
			System.out.println("Erro ao executar busca.");
		} 
		finally {
			pararTimerStatus();
			
			System.out.println();
			
			if (dados.isConectado())
				dados.desconectar();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	protected boolean buscarComPoda() {
		Estado encontrado = null;
		Estado noAtual = null;
		
		folhas = new Vector<Estado>();
		folhas.add(inicio);
		
		
		while (!folhas.isEmpty() && encontrado == null) {
			//Obtém o item com menor custo (primeiro)
			noAtual = folhas.remove(0);
			
			if (!foiVisitado(noAtual)) {
				if (imprimeCaminho)
					imprimirEstado(noAtual);
				
				if (isObjetivo(noAtual)) {
					encontrado = noAtual;
					mostrarResultado(noAtual);
				}
				else {
					gerarFilhos(noAtual);
				}
				qtdEstadosAnalisados++;
			}
		}
		return (encontrado != null);
	}
	
	/**
	 * Faz a poda dos estados visitados, subindo na árvore para ver se há um pai igual 
	 */
	private boolean foiVisitado(Estado e) {
		boolean visitado = false;
		Estado pai = e.getPai();
		
		while ((pai != null) && (!visitado)) {
			if (e.equals(pai)) {
				visitado = true;
			}
			pai = pai.getPai();
		}
		return visitado;
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
		List<Estado> filhos = new ArrayList<Estado>();
		Set<Trecho> trechosPossiveis = dados.getDistanciasReais(e.getEstacao());
		String estacaoDestino;
		Trecho distanciaObjetivo;
		
		for (Trecho trecho : trechosPossiveis) {
			if (!trecho.getEstacao1().equals(e.getEstacao())) {
				estacaoDestino = trecho.getEstacao1();
			}
			else {
				estacaoDestino = trecho.getEstacao2();
			}
			
			//Obtém a distância em linha reta da estacao destino até o objetivo
			distanciaObjetivo = dados.getDistanciaLinhaReta(estacaoDestino, objetivo.getEstacao());
			
			//Cria o filho
			Estado novoFilho = new Estado(estacaoDestino);
			novoFilho.setPai(e);
			novoFilho.setLinhaOrigem(trecho.getLinha());
			novoFilho.setCusto(calcularCusto(trecho, novoFilho));
			novoFilho.setHeuristica(calcularHeuristica(trecho, novoFilho, distanciaObjetivo.getDistancia()));
			
			//debugVisual(e);
			adicionarFilho(novoFilho);
		}
		return filhos;
	}
	
	private void adicionarFilho(Estado novoFilho) {
		boolean adicionado = false;
		for (int i = 0; i < folhas.size() && adicionado == false; i++) {
			if (novoFilho.getHeuristica() < folhas.get(i).getHeuristica()) {
				folhas.add(i, novoFilho);
				adicionado = true;
			}
		}
		if (!adicionado) {
			folhas.add(novoFilho);
		}
			
	}

	private void debugVisual(Estado e) {
		System.out.println("\n------------------------------------");
		System.out.println("\nCURRENT STATION: " + e.toString());
		System.out.print("\nLeaves on tree: ");
		if (folhas.isEmpty()) {
			System.out.print("empty");
		}
		for (Estado estado : folhas) {
			System.out.print(estado.toString() + " ");
		}
		System.out.println("\n");
	}
	
	
	private double calcularCusto(Trecho trecho, Estado filho) {
		double custoTotal = filho.getPai().getCusto() + trecho.getDistancia();
		
		custoTotal = verificaTrocaLinha(trecho, filho, custoTotal);
		
		return custoTotal;
	}


	/**
	 * Cálculo da heurística 
	 * (custo do estado pai + distância do pai para o estado filho (trecho) + 
	 * distância do filho até o objetivo + peso). 
	 * 
	 * @param trecho Distância do estado pai até o filho
	 * @param filho Estado filho
	 * @param distanciaObjetivo Distância do estado filho até o objetivo
	 */
	private double calcularHeuristica(Trecho trecho, Estado filho, int distanciaObjetivo) {
		double heuristicaTotal = filho.getPai().getCusto() + (trecho.getDistancia() + distanciaObjetivo);
		
//		heuristicaTotal = verificaTrocaLinha(trecho, filho, heuristicaTotal);
		
		return heuristicaTotal;
	}

	/**
	 * Verifica se precisa adicionar peso de troca de linha e retorna caso precise
	 * 
	 * @param trecho
	 * @param filho
	 * @param custoTotal
	 * @return
	 */
	private double verificaTrocaLinha(Trecho trecho, Estado filho, double custoTotal) {
		//Verifica se trocou de linha:
		if (filho.getPai().getLinhaOrigem() != null && 
			!filho.getPai().getLinhaOrigem().equals(trecho.getLinha())) {
			
			//Adiciona um peso se trocou de linha
			custoTotal = custoTotal + PESO_TROCA;
			filho.setTrocouLinha(true);
		}
		return custoTotal;
	}
	
	
	protected void imprimirEstado(Estado e) {
		String s = e.toString();
		
		if (e.trocouLinha()) {
			s += "* ";
		}
		
		if (e.trocouLinha()) {
			s += "\t (Trocou linha)";
		}
		System.out.println(s);
	}
}
