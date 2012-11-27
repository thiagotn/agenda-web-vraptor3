package org.ifsp.agenda.jdbc.fabrica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.ifsp.agenda.exception.AgendaDAOException;

/**
 *
 * @author Ton
 */
public class ConnectionAgendaFactory {

    /**
     * Classe utilizada para conectar e desconectar ao banco de dados.
     * Para o uso das classes ContatoDAO, UsuariioDAO e CompromissoDAO.
     * 
     * @return
     * @throws AgendaDAOException
     */
    public static Connection getConnection() throws AgendaDAOException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/agendadb", "sa", "");
        } catch (ClassNotFoundException | SQLException e) {
            throw new AgendaDAOException(e.getMessage());
        }
    }

    /**
     *
     * @param conn
     * @param stmt
     * @param rs
     * @throws AgendaDAOException
     */
    public static void closeConnection(Connection conn,
            Statement stmt, ResultSet rs) throws AgendaDAOException {
        close(conn, stmt, rs);
    }

    /**
     *
     * @param conn
     * @param stmt
     * @throws AgendaDAOException
     */
    public static void closeConnection(Connection conn, Statement stmt)
            throws AgendaDAOException {
        close(conn, stmt, null);
    }

    /**
     *
     * @param conn
     * @throws AgendaDAOException
     */
    public static void closeConnection(Connection conn)
            throws AgendaDAOException {
        close(conn, null, null);
    }

    private static void close(Connection conn,
            Statement stmt, ResultSet rs)
            throws AgendaDAOException {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            throw new AgendaDAOException(e.getMessage());
        }
    }
}
