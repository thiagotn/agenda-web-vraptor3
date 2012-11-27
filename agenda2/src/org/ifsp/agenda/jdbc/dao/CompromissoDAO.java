package org.ifsp.agenda.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.CompromissoDAOException;
import org.ifsp.agenda.exception.CompromissoDuplicadoException;
import org.ifsp.agenda.exception.CompromissoInexistenteException;
import org.ifsp.agenda.exception.ContatoInexistenteException;
import org.ifsp.agenda.jdbc.fabrica.ConnectionAgendaFactory;
import org.ifsp.agenda.modelo.Aviso;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.Usuario;
import org.ifsp.agenda.util.PWSec;

/**
 *
 * @author Ton
 */
public class CompromissoDAO {

    /**
     *
     * Esta classe é utilizada para fazer operações no banco de dados para
     * tratar os compromissos Os métodos nesta classe permite ao usuário
     * conectar ao banco de dados para inserir, pesquisar, alterar e excluir
     * dados sobre os compromissos assim també enviar email.
     *
     * @throws AgendaD AOException
     */
    private Connection conn;

    /**
     *
     * @throws AgendaDAOException
     */
    public CompromissoDAO() throws AgendaDAOException {
        try {
            this.conn = ConnectionAgendaFactory.getConnection();
        } catch (Exception e) {
            throw new AgendaDAOException("Erro: "
                    + ":\n" + e.getMessage());
        }
    }

    /**
     *
     * Este método atualiza os dados dos compromisso no banco de dados. Ele
     * localiza primeiramente se o compromisso existe através do id do
     * compromisso e o usuário verificaCompromissoAlteracao para seja alterado
     * somente o local e descrição, caso verdeiro ele verifica se há data e hora
     * disponíveis através do método verificaAlteracaoDados com o novos valores.
     * Se não existir hora e data ele lança exceção. Caso contrário ele atulaiza
     * o compromisso. O status é alterado para 0 para que o usuário receba email
     * de aviso.
     *
     *
     * @param compromisso
     * @throws SQLException
     * @throws CompromissoDuplicadoException
     * @throws AgendaDAOException
     */
    public void atualizarCompromisso(Compromisso compromisso)
            throws SQLException, CompromissoDuplicadoException, AgendaDAOException {
        PreparedStatement ps = null;
        if (compromisso == null) {
            throw new SQLException("Erro durante alteração do compromisso");
        }
        try {
            if (verificaCompromissoAlteracao(compromisso.getId(), compromisso.getUsuario()) == true) {
                if (verificaAlteracaoDados(compromisso.getId(), compromisso.getData(), compromisso.getHora(), compromisso.getUsuario()) == true) {
                    String SQL = "UPDATE compromisso SET local=?,descricao=?,contato=?,status=0 where id=?";
                    ps = this.conn.prepareStatement(SQL);
                    ps.setString(1, compromisso.getLocal());
                    ps.setString(2, compromisso.getDescricao());
                    ps.setString(3, compromisso.getContato());
                    ps.setInt(4, compromisso.getId());
                    ps.executeUpdate();
                } else {
                    if (verificaDataHoraCompromisso(compromisso.getData(), compromisso.getHora(), compromisso.getUsuario()) == true) {
                        throw new CompromissoDuplicadoException();
                    } else {
                        String SQL = "UPDATE compromisso SET local=?,descricao=?,contato=?,data=?,hora=? where id=?";
                        ps = this.conn.prepareStatement(SQL);
                        ps.setString(1, compromisso.getLocal());
                        ps.setString(2, compromisso.getDescricao());
                        ps.setString(3, compromisso.getContato());
                        ps.setString(4, compromisso.getData());
                        ps.setString(5, compromisso.getHora());
                        ps.setInt(6, compromisso.getId());
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erro durante alteração do compromisso");
        }
    }

    /**
     * Este método insere compromisso no banco de dados, verificando se
     * compromisso já, caso no exista um erro lançado caso contrário o
     * compromisso é inserido no banco de dados.
     *
     * @param compromisso
     * @throws SQLException
     * @throws CompromissoDAOException
     * @throws AgendaDAOException
     * @throws CompromissoDuplicadoException
     */
    public void inserirCompromisso(Compromisso compromisso)
            throws SQLException, CompromissoDAOException, AgendaDAOException, CompromissoDuplicadoException {
        if (compromisso != null) {
            PreparedStatement ps = null;
            try {
                if (verificaDataHoraCompromisso(compromisso.getData(), compromisso.getHora(), compromisso.getUsuario()) == true) {
                    throw new CompromissoDuplicadoException();
                } else {
                    String SQL = "insert into compromisso (data,hora,local,descricao,contato,usuario,status) values (?,?,?,?,?,?,0);";
                    ps = this.conn.prepareStatement(SQL);
                    ps.setString(1, compromisso.getData());
                    ps.setString(2, compromisso.getHora());
                    ps.setString(3, compromisso.getLocal());
                    ps.setString(4, compromisso.getDescricao());
                    ps.setString(5, compromisso.getContato());
                    ps.setString(6, compromisso.getUsuario());
                    ps.execute();
                }
            } catch (SQLException sqlerro) {
                throw new CompromissoDAOException("Erro ao inserir dados:");
            } finally {
                ConnectionAgendaFactory.closeConnection(conn, ps);
            }
        }
    }

    /**
     * Este método é para que o desenvolver use qualquer campo e valor para
     * localizar dados na tabela de compromissos Se a filtragem não for
     * estabelecida ele retorno todos os compromissos usuário. Ele utiliza a
     * Cláusula LIKE para ajuda o usuário inserir apenas partes da string no
     * filro. Este método utiliza o método getlista para obter todos os
     * compromissos sem parâmentros de filtragem.
     *
     * @param usuario
     * @param campo
     * @param filtro
     * @return
     * @return: Uma lista de compromisso caso exista.
     * @throws AgendaDAOException
     * @throws SQLException
     * @throws CompromissoInexistenteException
     * @throws ContatoInexistenteException
     * @throws CompromissoDAOException
     */
    public List<Compromisso> buscarCompromissos(Usuario usuario, String campo, String filtro) throws AgendaDAOException, SQLException, CompromissoInexistenteException, ContatoInexistenteException, CompromissoDAOException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.conn.prepareStatement("select * from compromisso where usuario=? and UCASE(" + campo + ") like UCASE('" + filtro + "%')");
            ps.setString(1, usuario.getLogin());
            rs = ps.executeQuery();
            List<Compromisso> list = new ArrayList<>();
            List<Compromisso> listcompleta = new ArrayList<>();
            while (rs.next()) {
                Compromisso compromisso = new Compromisso();
                compromisso.setId(rs.getInt("id"));
                compromisso.setLocal(rs.getString("local"));
                compromisso.setData(rs.getString("data"));
                compromisso.setDescricao(rs.getString("descricao"));
                compromisso.setHora(rs.getString("hora"));
                compromisso.setContato(rs.getString("contato"));
                compromisso.setUsuario(rs.getString("usuario"));
                list.add(compromisso);
            }
            if (list.isEmpty()) {
                return listcompleta = getLista(usuario);
            } else {
                return list;
            }
        } catch (SQLException sqlerro) {
            throw new CompromissoDAOException("Erro ao localizar o compromisso!");
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps, rs);
        }
    }

    /**
     * Este método verifica se já existe compromisso no horário e data escolhido
     * para um usuário
     *
     * @param data
     * @param hora
     * @param usuario
     * @return : true ou false
     * @throws SQLException
     * @throws AgendaDAOException
     */
    public boolean verificaDataHoraCompromisso(String data, String hora, String usuario)
            throws SQLException, AgendaDAOException {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = this.conn.prepareStatement("select * from compromisso where (data=? and hora=? and usuario=?)");
            ps.setString(1, data);
            ps.setString(2, hora);
            ps.setString(3, usuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlerro) {
            throw new SQLException("Erro ao localizar o compromisso!" + sqlerro);
        }

    }

    /**
     *
     * Este método verifica se o compromisso existe através do id, hora, data e
     * login do usuário. Este método será caso haja a necessidade de localizar
     * um compromisso pelo id. A localização será precisa.
     *
     * @param id
     * @param data
     * @param hora
     * @param usuario
     * @return
     * @throws SQLException
     * @throws AgendaDAOException
     */
    public boolean verificaAlteracaoDados(int id, String data, String hora, String usuario)
            throws SQLException, AgendaDAOException {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = this.conn.prepareStatement("select * from compromisso where data=? and hora=? and usuario=? and id=?");
            ps.setString(1, data);
            ps.setString(2, hora);
            ps.setString(3, usuario);
            ps.setInt(4, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlerro) {
            throw new SQLException("Erro ao localizar o compromisso!" + sqlerro);
        }
    }

    /**
     *
     * Este método séra utilizado somente para verifica se existe o compromisso
     *
     * @throws SQLException
     * @throws AgendaDAOException
     * @return
     * @param id
     * @param usuario
     */
    public boolean verificaCompromissoAlteracao(int id, String usuario)
            throws SQLException, AgendaDAOException {
        PreparedStatement ps;
        ResultSet rs;
        try {
            ps = this.conn.prepareStatement("select * from compromisso where usuario=? and id=?");
            ps.setString(1, usuario);
            ps.setInt(2, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException sqlerro) {
            throw new SQLException("Erro ao localizar o compromisso!" + sqlerro);
        }
    }

    /**
     *
     * Este metódo retorna uma lista com todos os compromissos que percentecem a
     * um usuário.
     *
     * @param usuario
     * @return
     * @throws AgendaDAOException
     */
    public List<Compromisso> getLista(Usuario usuario) throws AgendaDAOException {
        try {
            List<Compromisso> compromissos = new ArrayList<>();
            try (PreparedStatement stmt = this.conn.prepareStatement("select * from compromisso where usuario=? and to_date (data,'DD/MM/YYYY')>=Today()")) {
                stmt.setString(1, usuario.getLogin());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Compromisso compromisso = new Compromisso();
                        compromisso.setId(rs.getInt("id"));
                        compromisso.setData(rs.getString("data"));
                        compromisso.setHora(rs.getString("hora"));
                        compromisso.setLocal(rs.getString("local").trim());
                        compromisso.setDescricao(rs.getString("descricao").trim());
                        compromisso.setContato(rs.getString("contato"));
                        compromissos.add(compromisso);
                    }
                }
            }
            return compromissos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionAgendaFactory.closeConnection(conn);
        }
    }

    /**
     *
     * Este método será utilizado para retorna um único compromisso, se não
     * existir retorna null
     *
     * @throws CompromissoInexistenteException
     * @throws SQLException
     * @throws AgendaDAOException
     * @return
     * @param id
     */
    public Compromisso pesquisar(int id) throws CompromissoInexistenteException, SQLException, AgendaDAOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = this.conn.prepareStatement("select * from compromisso where id=?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Compromisso compromisso = new Compromisso();
            if (rs.next()) {
                compromisso.setId(rs.getInt("id"));
                compromisso.setData(rs.getString("data"));
                compromisso.setHora(rs.getString("hora"));
                compromisso.setLocal(rs.getString("local"));
                compromisso.setDescricao(rs.getString("descricao"));
                compromisso.setContato(rs.getString("contato"));
                compromisso.setUsuario(rs.getString("usuario"));
                return compromisso;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new CompromissoInexistenteException();
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, stmt, rs);
        }
    }

    /**
     *
     * Este método exclui o compromisso da tabela, caso não consiga retorna um
     * erro.
     *
     * @param id
     * @throws CompromissoInexistenteException
     * @throws AgendaDAOException
     */
    public void remove(int id) throws CompromissoInexistenteException, AgendaDAOException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("delete from compromisso where id=?");
            stmt.setLong(1, id);
            stmt.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new CompromissoInexistenteException();
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, stmt);
        }
    }

    public void atualizaStatus(int id) throws CompromissoInexistenteException, AgendaDAOException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement("update compromisso set status=1 where id=?");
            stmt.setLong(1, id);
            stmt.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new CompromissoInexistenteException();
        }
    }

    /**
     * Este metódo monta a mensagem de email sobre um comprimisso, e atualiza o
     * seu status para 1 para que o agendador não envie mas a mesma mensagem de
     * email. e retorna uma lista com todos os compromissos de todos os usuarios
     * com as suas configurações inclusive a sua senha desencriptografada.
     *
     *
     * @throws SQLException
     * @throws CompromissoInexistenteException
     * @throws AgendaDAOException
     * @throws Exception
     * @return
     */
    public List<Aviso> MaillerCompromisso() throws SQLException, CompromissoInexistenteException, AgendaDAOException, Exception {
        List<Aviso> avisos = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = this.conn.prepareStatement("select c.id,c.local,DATEDIFF(to_date (c.data,'DD/MM/YYYY'), SYSDATE) as Falta,"
                    + "u.alerta,c.data,c.hora,c.descricao,c.contato,c.usuario,c.status,u.alerta,u.login,u.email,u.senha_gmail,"
                    + "u.nome from compromisso c inner join usuario u on c.usuario=u.login "
                    + "where DATEDIFF(to_date (c.data,'DD/MM/YYYY'), SYSDATE) <=u.alerta and status<>1");
            rs = ps.executeQuery();
            String senha = null;
            while (rs.next()) {
                Aviso aviso = new Aviso();
                senha = PWSec.decrypt(rs.getString("senha_gmail"));
                aviso.setCompromissoId(rs.getInt("id"));
                aviso.setCompromissoContato(rs.getString("contato"));
                aviso.setCompromissoData(rs.getString("data"));
                aviso.setCompromissoDescricao(rs.getString("descricao"));
                aviso.setCompromissoHora(rs.getString("hora"));
                aviso.setCompromissoLocal(rs.getString("local"));
                aviso.setUsuarioEmail(rs.getString("email"));
                aviso.setSenhaGmail(senha);
                aviso.setUsuarioNome(rs.getString("nome"));
                aviso.setUsuarioAlerta(rs.getInt("alerta"));
                aviso.setCompromissoStatus(rs.getInt("status"));
                ps = this.conn.prepareStatement("UPDATE compromisso SET status=1 where id=?");
                ps.setInt(1, aviso.getCompromissoId());
                ps.executeUpdate();
                avisos.add(aviso);
            }
            return avisos;
        } catch (SQLException e) {
            throw new CompromissoInexistenteException();
        } finally {
            ConnectionAgendaFactory.closeConnection(conn, ps, rs);
        }
    }
}