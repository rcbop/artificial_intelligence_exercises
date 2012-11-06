import br.fbv.ia.busca.BuscaAStar;
import br.fbv.ia.busca.Estado;
import br.fbv.ia.excecao.EstadoIndefinidoException;

/**
 * O programa executa o algoritmo de busca A* sobre estados definidos 
 * nas constantes "ESTACAO_INICIAL" e "ESTACAO_OBJETIVO" abaixo.
 * 
 * Formulação do problema:
 *  Estados: duas estações dentre as linhas cobertas indicadas no mapa ('E1' a 'E14')
 *  Função sucessor: verificar as estações possíveis para prosseguir e calcular a heurística em cada uma delas
 *  Heurística: custo do estado pai + distância do pai para o estado filho (trecho) + distância do filho até o objetivo + peso 
 *  Teste objetivo: o estado atual deve ser igual ao estado objetivo definido
 *  Custo: cada trecho entre as estações possui um peso determinado na base de dados
 */
public class Main {

	private static final boolean EXIBIR_CAMINHO_COMPLETO = false;
	
	private static final String ESTACAO_INICIAL = "E5";
	private static final String ESTACAO_OBJETIVO = "E9";
	
	
	public static void main(String[] args) {
		BuscaAStar busca = new BuscaAStar();
		
		try {
			busca.buscar(
					new Estado(ESTACAO_INICIAL), 
					new Estado(ESTACAO_OBJETIVO), 
					EXIBIR_CAMINHO_COMPLETO);
		} 
		catch (EstadoIndefinidoException e) {
			System.err.println(e.getMessage());
		}
	}

}
