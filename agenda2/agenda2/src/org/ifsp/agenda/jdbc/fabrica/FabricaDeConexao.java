package org.ifsp.agenda.jdbc.fabrica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ton
 */
public class FabricaDeConexao {

    /**
     * Esta classe fornece conexões aos bancos de dados MySQL, PostgreSQL e Oracle
     * Esta classe depende um conector adicionado na bilioteoca do projeto.
     * @return
     */
    public Connection getConnection() {
        System.out.println("Conectando ao banco");
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost/agenda","root","root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public static Connection getConexaoMySQL() throws Exception, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/agenda","root","root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public static Connection getConexaoPostgreSQL() throws Exception, SQLException {
        try {

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost/agenda","root","root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return
     * @throws Exception
     * @throws SQLException
     */
    public static Connection getConexaoOracleXE() throws Exception, SQLException {
        try {

            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/XE", "root", "root");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
