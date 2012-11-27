package org.ifsp.agenda.jdbc.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.ContatoDuplicadoException;
import org.ifsp.agenda.exception.ContatoInexistenteException;
import org.ifsp.agenda.jdbc.fabrica.ConnectionAgendaFactory;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.ContatoPessoal;
import org.ifsp.agenda.modelo.ContatoProfissional;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * Esta classe será utilizado para auxialiar na gravação dos dados na tabela de contatos
 * do banco de dados. Nele encontram-se todos os método necessários para conectar ao banco de dados para inserir,
 * pequisar, alterar e excluir registros.
 * 
 * @author Ton
 */
public class ContatoDAO {

    private Connection conn;

    /**
     *
     * @throws AgendaDAOException
     */
    public ContatoDAO() throws AgendaDAOException {
        try {
            this.conn = ConnectionAgendaFactory.getConnection();
        } catch (Exception e) {
            throw new AgendaDAOException("Erro: "
                    + ":\n" + e.getMessage());
        }
    }

    /**
     * Este método sera usuado para acidicona contatos nas tabelas de contatos pessoais e profissionais.
     * Ele verifica o tipo de contato que foi instanciado através do parâmetro contato do método, e grava
     * na tabela relacionada ao contato.
     * 
     * @param contato 
     * @throws ContatoDuplicadoException
     * @throws AgendaDAOException
     * @throws SQLException
     */
    public void adiciona(Contato contato) throws ContatoDuplicadoException, AgendaDAOException, SQLException {
        PreparedStatement stmt = null;
        if (contato instanceof ContatoPessoal) {
            String sql = "insert into contato_pessoal (nome,email,endereco,telefone,celular,data_nascimento,usuario) values (?,?,?,?,?,?,?)";
            try {
                stmt = this.conn.prepareStatement(sql);
                stmt.setString(1, contato.getNome());
                stmt.setString(2, contato.getEmail());
                stmt.setString(3, contato.getEndereco());
                stmt.setString(4, contato.getTelefone());
                stmt.setString(5, ((ContatoPessoal) contato).getCelular());
                stmt.setDate(6, new Date(((ContatoPessoal) contato).getDataNascimento().getTime()));
                stmt.setString(7, contato.getUsuario().getLogin());
                stmt.execute();
            } catch (SQLException sqle) {
                throw new AgendaDAOException("Erro ao inserir dados " + sqle);
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        } else {
            String sql = "insert into contato_profissional (nome,email,endereco,telefone,empresa,usuario) values (?,?,?,?,?,?)";
            try {
                stmt = this.conn.prepareStatement(sql);
                stmt.setString(1, contato.getNome());
                stmt.setString(2, contato.getEmail());
                stmt.setString(3, contato.getEndereco());
                stmt.setString(4, contato.getTelefone());
                stmt.setString(5, ((ContatoProfissional) contato).getNomeEmpresa());
                stmt.setString(6, contato.getUsuario().getLogin());
                stmt.execute();
            } catch (SQLException sqle) {
                throw new AgendaDAOException("Erro ao inserir dados " + sqle);
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        }
    }

    /**
     * retorna um arraylist com contatos pessoais e profissionais que pertencem a um usuário, ele varre as duas
     * tabelas em busca de todos os contatos.
     * 
     * @throws AgendaDAOException
     * @param usuario 
     * @return  
     */
    public List<Contato> getLista(Usuario usuario) throws AgendaDAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();
        try {
            stmt = this.conn.prepareStatement("select * from contato_pessoal where usuario=?");
            stmt.setString(1, usuario.getLogin());
            rs = stmt.executeQuery();
            while (rs.next()) {
                ContatoPessoal contatoPessoal = new ContatoPessoal();
                contatoPessoal.setId(rs.getInt("id"));
                contatoPessoal.setNome(rs.getString("nome"));
                contatoPessoal.setEmail(rs.getString("email"));
                contatoPessoal.setEndereco(rs.getString("endereco"));
                contatoPessoal.setTelefone(rs.getString("telefone"));
                contatoPessoal.setCelular(rs.getString("celular"));
                java.util.Date data = new java.util.Date();
                data.setTime(rs.getDate("data_nascimento").getTime());
                ((ContatoPessoal) contatoPessoal).setDataNascimento(data);
                contatos.add(contatoPessoal);
            }
            stmt = this.conn.prepareStatement("select * from contato_profissional where usuario=?");
            stmt.setString(1, usuario.getLogin());
            rs = stmt.executeQuery();
            while (rs.next()) {
                ContatoProfissional contatoProfissional = new ContatoProfissional();
                contatoProfissional.setId(rs.getInt("id"));
                contatoProfissional.setNome(rs.getString("nome"));
                contatoProfissional.setEmail(rs.getString("email"));
                contatoProfissional.setEndereco(rs.getString("endereco"));
                contatoProfissional.setTelefone(rs.getString("telefone"));
                contatoProfissional.setNomeEmpresa(rs.getString("empresa"));
                contatos.add(contatoProfissional);
            }
            return contatos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, stmt, rs);
        }
    }

    /**
     * Este método criar uma união entre as tabelas contatos pessoais e profissionais perttecentes a um usuário
     * e retorna um arraylist com todos dados de acordo com o filtro escolhido. Útil para buscar qualquer tipo dados
     * em qualquer campo em que o filtro atender os requisitivos da consulta. Se o filtro na atender o escopo será 
     * retornado uma lista com todos os registros.
     * 
     * @param usuario 
     * @param campo 
     * @param filtro 
     * @return 
     * @throws AgendaDAOException
     */
    public List<Contato> getLista(Usuario usuario, String campo, String filtro) throws AgendaDAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Contato> contatos = new ArrayList<>();
        String sqlFiltro = null;

        if (filtro.equals("Aniversário")) {
            sqlFiltro = "SELECT * from (SELECT id,nome,email,endereco,telefone,celular,usuario,"
                    + "TO_CHAR (data_nascimento,'DD/MM/YYYY') as data_empresa, 'F'  as tipo "
                    + "from contato_pessoal "
                    + "UNION SELECT id,nome,email,endereco,telefone,'',usuario,empresa, 'J' as tipo "
                    + "from contato_profissional) as tabela where usuario=? AND " + campo + "='" + filtro + "'";
        } else {
            sqlFiltro = "SELECT * from (SELECT id,nome,email,endereco,telefone,celular,usuario,"
                    + "TO_CHAR (data_nascimento,'DD/MM/YYYY') as data_empresa, 'F'  as tipo "
                    + "from contato_pessoal "
                    + "UNION SELECT id,nome,email,endereco,telefone,'',usuario,empresa, 'J' as tipo "
                    + "from contato_profissional) as tabela where usuario=? AND UCASE(" + campo + ") Like UCASE('" + filtro + "%')";
        }
        try {
            stmt = this.conn.prepareStatement(sqlFiltro);
            stmt.setString(1, usuario.getLogin());
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("Tipo").equals("F")) {
                    ContatoPessoal contatoPessoal = new ContatoPessoal();
                    contatoPessoal.setId(rs.getInt("id"));
                    contatoPessoal.setNome(rs.getString("nome"));
                    contatoPessoal.setEmail(rs.getString("email"));
                    contatoPessoal.setEndereco(rs.getString("endereco"));
                    contatoPessoal.setTelefone(rs.getString("telefone"));
                    contatoPessoal.setCelular(rs.getString("celular"));
                    java.util.Date data = new java.util.Date();
                    data.setTime(converteData(rs.getString("data_empresa")).getTime());
                    ((ContatoPessoal) contatoPessoal).setDataNascimento(data);
                    contatos.add(contatoPessoal);
                } else {
                    ContatoProfissional contatoProfissional = new ContatoProfissional();
                    contatoProfissional.setId(rs.getInt("id"));
                    contatoProfissional.setNome(rs.getString("nome"));
                    contatoProfissional.setEmail(rs.getString("email"));
                    contatoProfissional.setEndereco(rs.getString("endereco"));
                    contatoProfissional.setTelefone(rs.getString("telefone"));
                    contatoProfissional.setNomeEmpresa(rs.getString("data_empresa"));
                    contatos.add(contatoProfissional);
                }
            }
            if (contatos.isEmpty()) {
                return contatos = getLista(usuario);
            } else {
                return contatos;
            }
        } catch (SQLException | AgendaDAOException e) {
            throw new AgendaDAOException(e);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, stmt, rs);
        }
    }

    /**
     * Este altera um contato pessoal ou profissional dependendo do tipo de contato instanciado.
     * 
     * @param contato 
     * @throws ContatoInexistenteException
     * @throws AgendaDAOException
     */
    public void altera(Contato contato) throws ContatoInexistenteException, AgendaDAOException {
        PreparedStatement stmt = null;
        if (contato instanceof ContatoPessoal) {
            String sql = "update contato_pessoal set nome=?,email=?,endereco=?,telefone=?,celular=?,data_nascimento=? where id=?";
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, contato.getNome());
                stmt.setString(2, contato.getEmail());
                stmt.setString(3, contato.getEndereco());
                stmt.setString(4, contato.getTelefone());
                stmt.setString(5, ((ContatoPessoal) contato).getCelular());
                stmt.setDate(6, new java.sql.Date(((ContatoPessoal) contato).getDataNascimento().getTime()));
                stmt.setLong(7, contato.getId());
                stmt.execute();
            } catch (SQLException e) {
                throw new ContatoInexistenteException();
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        } else {
            String sql = "update contato_profissional set nome=?,email=?,endereco=?,telefone=?,empresa=? where id=?";
            try {
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, contato.getNome());
                stmt.setString(2, contato.getEmail());
                stmt.setString(3, contato.getEndereco());
                stmt.setString(4, contato.getTelefone());
                stmt.setString(5, ((ContatoProfissional) contato).getNomeEmpresa());
                stmt.setLong(6, contato.getId());
                stmt.execute();
            } catch (SQLException e) {
                throw new ContatoInexistenteException();
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        }
    }

    /**
     * Este método remove um contato pessoal ou profissional de acordo com tipo de contato passado
     * por parâmetro.
     * 
     * @param contato 
     * @throws ContatoInexistenteException
     * @throws AgendaDAOException
     */
    public void remove(Contato contato) throws ContatoInexistenteException, AgendaDAOException {
        PreparedStatement stmt = null;
        if (contato instanceof ContatoPessoal) {
            try {
                stmt = conn.prepareStatement("delete from contato_pessoal where id=?");
                stmt.setInt(1, contato.getId());
                stmt.execute();
            } catch (SQLException e) {
                throw new ContatoInexistenteException();
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        } else {
            try {
                stmt = conn.prepareStatement("delete from contato_profissional where id=?");
                stmt.setInt(1, contato.getId());
                stmt.execute();
                conn.commit();

            } catch (SQLException e) {
                throw new ContatoInexistenteException();
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, stmt);
            }
        }
    }

    /**
     * Este método pesquisa nas tabelas de contatos pessoais ou profissionais e retorno o contato que foi
     * localizado no banco de dados.
     * 
     * @param id 
     * @return 
     * @throws ContatoInexistenteException
     * @throws SQLException
     * @throws AgendaDAOException
     */
    public Contato pesquisar(int id) throws ContatoInexistenteException, SQLException, AgendaDAOException {
        Contato contato;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement("select * from contato_pessoal where id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                contato = new ContatoPessoal();
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setEndereco(rs.getString("endereco"));
                contato.setTelefone(rs.getString("telefone"));
                contato.setTelefone(rs.getString("telefone"));
                ((ContatoPessoal) contato).setCelular(rs.getString("celular"));
                java.util.Date data = new java.util.Date();
                data.setTime(rs.getDate("data_nascimento").getTime());
                ((ContatoPessoal) contato).setDataNascimento(data);
                return contato;
            } else {
                stmt = this.conn.prepareStatement("select * from contato_profissional where id=?");
                stmt.setInt(1, id);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    contato = new ContatoProfissional();
                    contato.setId(rs.getInt("id"));
                    contato.setNome(rs.getString("nome"));
                    contato.setEmail(rs.getString("email"));
                    contato.setEndereco(rs.getString("endereco"));
                    contato.setTelefone(rs.getString("telefone"));
                    ((ContatoProfissional) contato).setNomeEmpresa(rs.getString("empresa"));
                    return contato;
                }
            }
            return null;
        } catch (Exception e) {
            throw new ContatoInexistenteException();
        }
    }

    /**
     * Este método returna true ou false se existe um usuário com mesmo nome, email pertencente a um usuário.
     * Será utilizada para verificar se o contato já foi cadastrado no banco de dados.
     * 
     * @param nome 
     * @param email 
     * @param usuario 
     * @return 
     * @throws SQLException
     * @throws AgendaDAOException
     */
    public boolean verificaContato(String nome, String email, String usuario)
            throws SQLException, AgendaDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * from (SELECT id,nome,email,endereco,usuario from contato_pessoal "
                + "UNION SELECT id,nome,email,endereco,usuario from contato_profissional) as tabela "
                + "where usuario =? and nome=? and email=?";
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, nome);
            ps.setString(3, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlerro) {
            throw new SQLException("Erro ao localizar o contato!" + sqlerro);
        }
    }

    /**
     * Este método pesquisa se o contato pertence ao usuário e retorna o contato 
     * encontrado.
     * 
     * @param nome 
     * @param usuario 
     * @return 
     * @throws SQLException
     * @throws AgendaDAOException
     * @throws ContatoInexistenteException
     */
    public Contato pesquisarContato(String nome, Usuario usuario)
            throws SQLException, AgendaDAOException, ContatoInexistenteException {
        String sql = "SELECT * from (SELECT id,nome,email,endereco,usuario,telefone from contato_pessoal "
                + "UNION SELECT id,nome,email,endereco,usuario,telefone from contato_profissional) as tabela "
                + "where usuario=? and nome=?";
        try {
            Contato contato = new Contato();
            PreparedStatement ps = this.conn.prepareStatement(sql);
            ps.setString(1, usuario.getLogin());
            ps.setString(2, nome);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                contato.setId(rs.getInt("id"));
                contato.setNome(rs.getString("nome"));
                contato.setEmail(rs.getString("email"));
                contato.setEndereco(rs.getString("endereco"));
                contato.setTelefone(rs.getString("telefone"));
                ps.close();
                rs.close();
                return contato;
            } else {
                return null;
            }
        } catch (SQLException sqlerro) {
            throw new SQLException("Erro ao localizar o contato!" + sqlerro);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn);
        }
    }

    /**
     * Este método será usado para fazer conversões de String em Date
     *
     * @param data
     * @return Retorna uma data
     */
    public static Date converteData(String data) {
        DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Date dataCon = null;
        try {
            dataCon = new Date(fmt.parse(data).getTime());
        } catch (ParseException e) {
            Logger.getLogger(CompromissoDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        return dataCon;
    }
}
