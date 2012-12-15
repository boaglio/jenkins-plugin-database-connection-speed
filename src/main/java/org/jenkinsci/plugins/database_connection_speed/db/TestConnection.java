package org.jenkinsci.plugins.database_connection_speed.db;

import hudson.model.BuildListener;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Fernando Boaglio
 */
public class TestConnection {

	private static long tempoInicial;
	private static long tempoFinal;
	private static BuildListener listener;

	private static final String linha = "------------------------------------------------------------------";

	public static void inicia(String url,String usuario,String senha,String dbType,boolean detalhes,BuildListener listener) throws Exception {

		TestConnection.listener = listener;

		String driver = "";
		String sql = "";
		tempoInicial = System.currentTimeMillis();

		if (dbType == null) {
			dbType = BancoDeDados.MySQL.getNome();
		}

		listener.getLogger().println(linha);

		if (detalhes) {
			listener.getLogger().println("Testando conexao [" + dbType + "] detalhada: " + usuario + "@" + url);
		} else {
			listener.getLogger().println("Testando conexao [" + dbType + "]: " + usuario + "@" + url);
		}

		if (dbType.equals(BancoDeDados.Oracle.getNome())) {
			driver = BancoDeDados.Oracle.getDriver();
			sql = BancoDeDados.Oracle.getBuscarDataSQL();
		} else if (dbType.equals(BancoDeDados.MySQL.getNome())) {
			driver = BancoDeDados.MySQL.getDriver();
			sql = BancoDeDados.MySQL.getBuscarDataSQL();
		} else {
			driver = BancoDeDados.SQLServer.getDriver();
			sql = BancoDeDados.SQLServer.getBuscarDataSQL();
		}

		if (detalhes) {
			listener.getLogger().println("Carregando driver...");
		}
		Class.forName(driver);

		Connection conn = DriverManager.getConnection(url,usuario,senha);
		try {
			Statement stmt = conn.createStatement();
			try {
				if (detalhes) {
					listener.getLogger().println("Buscando a data de hoje...");
				}
				ResultSet rset = stmt.executeQuery(sql);
				try {
					Date data = null;
					while (rset.next()) {
						data = rset.getDate(1);
					}
					if (detalhes) {
						listener.getLogger().println("Resultado do SQL: " + data);
					}
				} finally {
					try {
						rset.close();
					} catch (Exception ignore) {}
				}
			} finally {
				try {
					stmt.close();
				} catch (Exception ignore) {}
			}
		} finally {
			try {
				conn.close();
			} catch (Exception ignore) {}
		}
		if (detalhes) {
			listener.getLogger().println("Finalizando...");
		}
		tempoFinal = System.currentTimeMillis();
	}

	public static long diff() {
		return tempoFinal - tempoInicial;
	}

	public static void finaliza() {
		listener.getLogger().println(linha);
	}
}
