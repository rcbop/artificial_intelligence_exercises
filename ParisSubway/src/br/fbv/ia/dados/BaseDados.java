package br.fbv.ia.dados;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import br.fbv.ia.dados.modelo.Linha;
import br.fbv.ia.dados.modelo.Trecho;


/**
 * Representação do algoritmo de Busca A*
 */
public class BaseDados {

	private static final String URL = "jdbc:hsqldb:file:base" + File.separator + "metro_paris";
	private static final String USUARIO = "";
	private static final String SENHA = "";

	private Connection con;
	private Statement stm;
	private boolean conectado;


	public BaseDados() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} 
		catch (ClassNotFoundException e) {
			System.err.println("Erro ao carregar o driver da base de dados.");
		}
	}

	public void conectar() throws SQLException {
		System.out.print("Conectando base de dados... ");
		
		try {
			con = DriverManager.getConnection(URL, USUARIO, SENHA);
			stm = con.createStatement();
			stm.execute("SET SCHEMA METRO_PARIS");
			conectado = true;
			System.out.println("OK");
		} 
		catch (SQLException e) {
			System.err.println("FALHA");
			throw e;
		}
	}

	public void desconectar() {
		System.out.print("Desconectando base de dados... ");
		
		try {
			if (stm != null &&!stm.isClosed()) {
				stm.execute("SHUTDOWN");
				stm.close();
				conectado = false;
				System.out.print("OK");
			}
		} catch (SQLException e) {
			System.err.println("FALHA");
		}
	}

	public boolean isConectado() {
		return conectado;
	}
	
	

	private static final String SELECT_TRECHO = "SELECT EST_1 , EST_2, DIST, LINHA FROM METRO_PARIS.TRECHO";

	/**
	 * Obtém o trecho real entre duas estações 
	 * @return trecho real entre as estações ou <code>null</code> se não há ligação entre elas.
	 */
	public Trecho getDistanciaReal(String estacao1, String estacao2) {
		return getDistanciaEntre(estacao1, estacao2, true);
	}


	/**
	 * Obtém o trecho real entre a estação informada e as demais que possuem ligação com ela.
	 * Utilize este método também para descobrir possíveis estações que pode-se seguir partindo
	 * da estação informada.
	 * @return Lista de trecho de estações ligadas à que foi informada 
	 */
	public Set<Trecho> getDistanciasReais(String estacao) {
		return getDistanciasEstacao(estacao, true);
	}


	/**
	 * Obtém o trecho em linha reta para as demais estações, a partir da
	 * estação informada.
	 */
	public Set<Trecho> getDistanciaLinhaReta(String estacao) {
		return getDistanciasEstacao(estacao, false);
	}

	/**
	 * Obtém o trecho em linha reta entre duas estações.
	 */
	public Trecho getDistanciaLinhaReta(String estacao1, String estacao2) {
		return getDistanciaEntre(estacao1, estacao2, false);
	}


	private Trecho getDistanciaEntre(String estacao1, String estacao2, boolean distanciaReal) {
		Trecho d = null;
		ResultSet rs;

		try {
			rs = stm.executeQuery(SELECT_TRECHO + " " +
					"WHERE DIST_REAL = "+ distanciaReal +" " +
					"	AND ((EST_1 = '"+ estacao1 +"' AND EST_2 = '"+ estacao2 +"') OR " +
					" 	 	 (EST_1 = '"+ estacao2 +"' AND EST_2 = '"+ estacao1 +"'))");

			Set<Trecho> temp = lerTrechos(rs);

			if (!temp.isEmpty())
				d = temp.iterator().next();

			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return d;
	}

	private Set<Trecho> getDistanciasEstacao(String estacao, boolean distanciaReal) {
		Set<Trecho> lista = new HashSet<Trecho>();
		ResultSet rs;

		try {
			rs = stm.executeQuery(SELECT_TRECHO + " " +
					"WHERE DIST_REAL = "+ distanciaReal +" " +
					"	AND (EST_1 = '"+ estacao +"' OR EST_2 = '"+ estacao +"')");
			lista = lerTrechos(rs);
			rs.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}


	private Set<Trecho> lerTrechos(ResultSet rs) throws SQLException {
		Set<Trecho> lista = new HashSet<Trecho>();

		while(rs.next()) {
			String est1 = rs.getString("EST_1");
			String est2 = rs.getString("EST_2");
			Integer distancia = rs.getInt("DIST");
			Integer idLinha = rs.getInt("LINHA");

			lista.add(new Trecho(est1, est2, distancia, Linha.valueOf(idLinha)));
		}

		return lista;
	}

}
