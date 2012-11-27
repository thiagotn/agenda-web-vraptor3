package org.ifsp.agenda.jdbc.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ifsp.agenda.controller.EditaUsuarioServlet;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.fabrica.ConnectionAgendaFactory;
import org.ifsp.agenda.modelo.Usuario;
import org.ifsp.agenda.util.PWSec;

/**
// *
 * @author Ton
 */
public class UsuarioDAO {

    /**
     *Esta classe é utilizada conecta ao banco de dados para inserir, excluir,
     * atualizar e pesquisar usuários na tabela usuários.
     * 
     */
    public String senhaEmail;
    private Connection conn;

    /**
     *
     * @throws AgendaDAOException
     */
    public UsuarioDAO() throws AgendaDAOException {
        try {
            this.conn = ConnectionAgendaFactory.getConnection();
        } catch (Exception e) {
            throw new AgendaDAOException("Erro: "
                    + ":\n" + e.getMessage());
        }
    }

    /**
     *
     * Este método é utilizado para localizar o usuário na tabela usuários
     * 
     * @param login 
     * @param senha 
     * @return uma instância do tipo Usuario
     * @throws SQLException
     * @throws UsuarioInexistenteException
     * @throws AgendaDAOException
     */
    public Usuario autenticar(String login, String senha) throws SQLException, UsuarioInexistenteException, AgendaDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.conn.prepareStatement("select * from usuario where login=? and senha=md5(?,'ISO-8859-1')");

            ps.setString(1, login);
            ps.setString(2, senha);
            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                return usuario;
            }
            return null;

        } catch (Exception e) {
            throw new UsuarioInexistenteException();
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps, rs);
        }
    }

    /**
     * Este método será usuário para pesquisar se o login já existe na tabela usuários
     * @param login 
     * @return o nome do login do usuário
     * @throws SQLException
     * @throws UsuarioInexistenteException
     * @throws AgendaDAOException
     */
    public String pesquisarLogin(String login) throws SQLException, UsuarioInexistenteException, AgendaDAOException {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = this.conn.prepareStatement("select * from usuario where login=?");
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                return usuario.getLogin();
            }
            return null;

        } catch (Exception e) {
            throw new UsuarioInexistenteException();
        }
    }

    /**
     * Este método utilizado para localizar o usuário na tabela usuario usando
     * como parâmetro uma instância do tipo Usuario
     * 
     * @return uma instância do tipo Usuario
     * @throws SQLException
     * @throws UsuarioInexistenteException
     * @throws AgendaDAOException
     * @param usuario 
     */
    public Usuario pesquisarUsuario(Usuario usuario) throws SQLException, UsuarioInexistenteException, AgendaDAOException {

        try {
            PreparedStatement ps = this.conn.prepareStatement("select * from usuario where login=?");
            ps.setString(1, usuario.getLogin());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                return usuario;
            }
            return null;

        } catch (Exception e) {
            throw new UsuarioInexistenteException();
        }
    }

    /**
     * Este método localiza um usuário na tabela usuários e retorna uma instância do tipo Usuario
     * @param login 
     * @return uma instancia do tipo Usuario
     * @throws SQLException
     * @throws UsuarioInexistenteException
     * @throws AgendaDAOException
     */
    public Usuario pesquisarUsuario(String login) throws SQLException, UsuarioInexistenteException, AgendaDAOException {
        try {
            PreparedStatement ps = this.conn.prepareStatement("select * from usuario where login=?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                return usuario;
            }
            return null;

        } catch (Exception e) {
            throw new UsuarioInexistenteException();
        }
    }

    /**
     * Este método retorna uma instância do tipo Usuário se o for encontrado um
     * usuário com id informado no parâmetro.
     * 
     * @param id 
     * @return uma instancia do tipo Usuario
     * @throws SQLException
     * @throws UsuarioInexistenteException
     * @throws AgendaDAOException
     */
    public Usuario pesquisarUsuarioId(int id) throws SQLException, UsuarioInexistenteException, AgendaDAOException {
        try {
            PreparedStatement ps = this.conn.prepareStatement("select * from usuario where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                return usuario;
            }
            return null;

        } catch (Exception e) {
            throw new UsuarioInexistenteException();
        }
    }

    /**
     * Este método adiciona um usuário na tabela usuários
     * 
     * @param usuario 
     * @throws AgendaDAOException
     * @throws SQLException
     * @throws UsuarioInexistenteException
     */
    public void cadastrar(Usuario usuario) throws AgendaDAOException, SQLException, UsuarioInexistenteException {
        PreparedStatement ps = null;
        String sql = "insert into usuario (login,nome,email,senha,alerta,senha_gmail) values (?,?,?,?,?,?)";
        try {
                ps = this.conn.prepareStatement(sql);
            ps.setString(1, usuario.getLogin());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, md5(usuario.getSenha()));
            ps.setInt(5, usuario.getAlerta());
            ps.setString(6,usuario.getSenhaEmail());
            ps.execute();
        } catch (SQLException sqle) {
            throw new AgendaDAOException("Erro ao inserir o usuário");
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps);
        }
    }

    /**
     * Este método é usuado para alterar os dados do usuario na tabela usuários
     * 
     * @param usuario 
     * @throws AgendaDAOException
     * @throws SQLException
     * @throws UsuarioInexistenteException
     */
    public void alterar(Usuario usuario) throws AgendaDAOException, SQLException, UsuarioInexistenteException {
        PreparedStatement ps = null;
        String sql = "update usuario set nome=?,email=?,senha=md5(?,'ISO-8859-1'),alerta=?,senha_gmail=? where login=? and id=?";
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());
            ps.setInt(4, usuario.getAlerta());
            ps.setString(5, usuario.getSenhaEmail());
            ps.setString(6, usuario.getLogin());
            ps.setInt(7, usuario.getId());
            ps.execute();
        } catch (SQLException sqle) {
            throw new AgendaDAOException("Usuário não existe!" + sqle);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps);
        }
    }

    /**
     * Este método retorno uma arraylist com todos usuários da tabela usuários
     * 
     * @return arraylist com Usuários
     * @throws AgendaDAOException
     * @throws SQLException
     * @throws UsuarioInexistenteException
     */
    public List<Usuario> getLista() throws AgendaDAOException, SQLException, UsuarioInexistenteException {
        PreparedStatement ps = null;
        try {
            List<Usuario> usuarios = new ArrayList<>();
            ps = this.conn.prepareStatement("select * from usuario");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setLogin(rs.getString("login"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setAlerta(rs.getInt("alerta"));
                usuario.setSenhaEmail(PWSec.decrypt(rs.getString("senha_gmail")));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps);
        }
    }

    /**
     * Este método remove um da tabela usuários se for encontrado se o id existir
     * 
     * @param id
     * @throws AgendaDAOException
     * @throws SQLException
     * @throws UsuarioInexistenteException
     */
    public void remove(int id) throws AgendaDAOException, SQLException, UsuarioInexistenteException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("delete from usuario where id=?");
            ps.setInt(1, id);
            ps.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AgendaDAOException("Erro ao excluir o usuário");
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps);
        }
    }

    /**
     * Este método converte uma string em MD5 para salvar ou recuperar senha na tabela usuários
     * @return String convertida em MD5
     * @param senha  Uma string 
     */
    public static String md5(String senha) {
        String sen = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(EditaUsuarioServlet.class.getName()).log(Level.SEVERE, null, e);
        }
        BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
        sen = hash.toString(16);
        return sen;
    }
}