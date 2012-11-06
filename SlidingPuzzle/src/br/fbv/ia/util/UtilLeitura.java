package br.fbv.ia.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.fbv.ia.util.Constantes;


/**
 * Classe auxiliar para realizar a leitura dos estados 
 * informados pelo usuário
 */
public class UtilLeitura {

	/**
	 * Obter o estado inicial definido em arquivo
	 * @return Matriz de <code>String</code> contendo o estado inicial
	 */
	public static String[][] lerEstadoInicial() {
		String[][] inicial = null;
		
		try {
			inicial = lerEstado(Constantes.ARQUIVO_ESTADO_INICIAL);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inicial;
	}
	
	/**
	 * Obter o estado objetivo definido em arquivo
	 * @return Matriz de <code>String</code> contendo o estado objetivo
	 */
	public static String[][] lerEstadoObjetivo() {
		String[][] objetivo = null;
		
		try {
			objetivo = lerEstado(Constantes.ARQUIVO_ESTADO_OBJETIVO);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return objetivo;
	}

	/**
	 * L� um estado a partir de um arquivo
	 * @param path Caminho do arquivo
	 * @return Matriz de <code>String</code> contendo o estado
	 * @throws FileNotFoundException O arquivo não foi encontrado no caminho informado
	 * @throws IOException Ocorreu um erro ao realizar a leitura do arquivo
	 */
	private static String[][] lerEstado(String path) throws FileNotFoundException, IOException {
		String[][] retorno = null;

		FileInputStream stream = new FileInputStream(path);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader bReader = new BufferedReader(reader);

		List<String[]> linhas = new ArrayList<String[]>();
		int num_colunas = 0;
		String linha;

		do {
			linha = bReader.readLine();

			if (linha != null) {
				String[] conteudo = linha.split(Constantes.SEPARADOR); 

				if (conteudo.length > num_colunas) {
					num_colunas = conteudo.length;
				}
				linhas.add(conteudo);
			}
		}
		while (linha != null);

		if (!linhas.isEmpty()) {
			retorno = new String[linhas.size()][];

			for (int i = 0; i < linhas.size(); i++) {
				retorno[i] = Arrays.copyOf(linhas.get(i), num_colunas);
			}
		}
		return retorno;
	}
}
