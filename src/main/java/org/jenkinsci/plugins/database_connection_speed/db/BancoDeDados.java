package org.jenkinsci.plugins.database_connection_speed.db;

/**
 * @author Fernando Boaglio
 */
public enum BancoDeDados {

	Oracle("oracle","oracle.jdbc.OracleDriver","SELECT SYSDATE FROM DUAL"),
	MySQL("mysql","com.mysql.jdbc.Driver","SELECT SYSDATE()"),
	SQLServer("sqlserver","net.sourceforge.jtds.jdbc.Driver","SELECT GETDATE()");

	private String nome;

	private String driver;

	private String buscarDataSQL;

	BancoDeDados(String nome,String driver,String buscarDataSQL) {
		this.nome = nome;
		this.driver = driver;
		this.buscarDataSQL = buscarDataSQL;
	}

	public String getNome() {
		return nome;
	}

	public String getDriver() {
		return driver;
	}

	public String getBuscarDataSQL() {
		return buscarDataSQL;
	}

}
