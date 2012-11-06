package br.fbv.ia.busca;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import br.fbv.ia.excecao.EstadoIndefinidoException;


@SuppressWarnings("rawtypes")
public abstract class BaseBusca<T extends BaseEstado> {

	protected T inicio;
	protected T objetivo;
	protected boolean imprimeCaminho;
	
	protected long tempoInicial;
	protected int qtdEstadosAnalisados;
	protected Timer statusTimer;
	
	
	
	public BaseBusca() {
		this.inicio = null;
		this.objetivo = null;
		this.imprimeCaminho = false;
	}
	
	
	/**
	 * Estado inicial
	 */
	public T getInicio() {
		return inicio;
	}

	/**
	 * Estado objetivo
	 */
	public T getObjetivo() {
		return objetivo;
	}

	
	/**
	 * Função sucessor.
	 * @param e Estado origem para os sucessores
	 * @return Estados sucessores
	 */
	protected abstract List<T> gerarFilhos(T e);
	
	/**
	 * Teste de objetivo.
	 * @param e Estado para testar se é objetivo
	 * @return
	 */
	protected abstract boolean isObjetivo(T e);
	
	
	
	
	
	/**
	 * Realiza busca com poda partindo do estado inicial até encontrar o objetivo, imprimindo o 
	 * melhor resultado ao final
	 * @param inicio Estado inicial
	 * @param objetivo Estado objetivo
	 * @param imprimeCaminho Indica se o caminho completo percorrido contendo os estados analisados deve ser impresso no console
	 * @throws EstadoIndefinidoException Um dos estados necessários não foi informado
	 */
	public abstract void buscar(T inicio, T objetivo, boolean imprimeCaminho) throws EstadoIndefinidoException;
	
	/**
	 * Realizar busca com poda, considerando os estados informados.
	 * @param inicio Estado inicial
	 * @param objetivo Estado final
	 * @return Booleano indicando se o objetivo foi localizado
	 */
	protected abstract boolean buscarComPoda();

	
	
	/**
	 * Verifica se os estados informados são válidos para realização da busca.
	 * @throws EstadoIndefinidoException Um dos estados necessários não foi informado
	 */
	protected void validarEstados() throws EstadoIndefinidoException {
		if (this.inicio == null || !this.inicio.isValido()) {
			throw new EstadoIndefinidoException("O estado inicial não foi definido.");
		}
		else if (this.objetivo == null || !this.objetivo.isValido()) {
			throw new EstadoIndefinidoException("O estado objetivo não foi definido.");
		}
	}

	protected abstract void imprimirEstado(T e);
	
	

	
	/* MÉTODOS AUXILIARES */
	
	/**
	 * Imprimir o resultado da busca, com as estatísticas e o caminho percorrido
	 */
	@SuppressWarnings("unchecked")
	protected void mostrarResultado(T estadoFinal) {
		String mensagem = "\n" +
				"\nEstatísticas: " +
				"\n Estados percorridos:\t{0}" +
				"\n Tempo de busca:\t{1} " +
				"\n Custo total:\t\t{2} " +
				"\n" +
				"\nCaminho para solução: " +
				"\n";
				
		T atual = estadoFinal;
		Stack<T> caminho = new Stack<T>();
		long tempo = calcularTempo(tempoInicial, System.currentTimeMillis());
		
		//Empilhar o caminho percorrido para exibir na ordem correta:
		while (atual != null) {
			caminho.push(atual); 
			atual = (T) atual.getPai();
		}
		
		System.out.println(
				MessageFormat.format(mensagem, 
						qtdEstadosAnalisados,
						formatarTempo(tempo),
						estadoFinal.getCusto()));
		
		//Desmpilhar para mostrar a ordem percorrida:
		while (!caminho.isEmpty()) {
			imprimeCaminho = true;
			imprimirEstado(caminho.pop());
		}
	}
	
	protected void iniciarTimerStatus() {
		if (!imprimeCaminho) {
			statusTimer = new Timer();
			statusTimer.scheduleAtFixedRate(new ImprimirStatus(), 0, 1000);
		}
	}
	
	protected void pararTimerStatus() {
		if (statusTimer != null) {
			statusTimer.cancel();
		}
	}
	
	
	private long calcularTempo(long msInicio, long msFim) {
		return (msFim - msInicio);
	}
	
	private String formatarTempo(long tempo) {
		SimpleDateFormat f = new SimpleDateFormat( "mm'm' ss's' SSS'ms'" );
		f.setTimeZone(TimeZone.getTimeZone("GMT0"));
		return f.format(new Date(tempo));
	}
	
	
	class ImprimirStatus extends TimerTask {
		public void run() {
			if (qtdEstadosAnalisados > 0) {
				System.out.println(" Estados percorridos: " + String.valueOf(qtdEstadosAnalisados));
			}
		}
	}
}
