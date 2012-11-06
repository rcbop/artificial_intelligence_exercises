import br.fbv.ia.busca.BuscaLargura;
import br.fbv.ia.busca.Estado;
import br.fbv.ia.excecao.EstadoIndefinidoException;
import br.fbv.ia.util.UtilLeitura;


/**
 * O programa executa o algoritmo de busca em largura sobre estados definidos 
 * nos arquivos "inicial.txt" e "objetivo.txt", na pasta "estados\".
 * 
 * Formulação do problema:
 *  Estados: duas matrizes com tamanho igual de linhas e colunas
 *  Função sucessor: mudar a posição do espaço vazio '_' (linha e/ou coluna)
 *  Teste objetivo: o estado atual deve ser igual ao estado objetivo definido
 *  Custo: 1 para cada movimento do espaço vazio 
 */
public class Main {

	private static final boolean EXIBIR_CAMINHO_COMPLETO = false; 

	
	public static void main(String[] args) {
		String[][] inicial = UtilLeitura.lerEstadoInicial();
		String[][] objetivo = UtilLeitura.lerEstadoObjetivo();
		BuscaLargura busca = new BuscaLargura();
		
		try {
			busca.buscar(
					new Estado(inicial), 
					new Estado(objetivo), 
					EXIBIR_CAMINHO_COMPLETO);
		} 
		catch (EstadoIndefinidoException e) {
			System.err.println(e.getMessage());
		}
	}
}

