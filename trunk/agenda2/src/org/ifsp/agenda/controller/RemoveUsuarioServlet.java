package org.ifsp.agenda.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.dao.UsuarioDAO;
import org.ifsp.agenda.modelo.MensagemErro;

/**
 *
 * @author Ton
 */

@SuppressWarnings("serial")
public class RemoveUsuarioServlet extends HttpServlet {
    
     /**
     * Este servlet exclui um usuário do banco de dados através do parâmetro id para página 
     * lista de usuário se o usuário for o administrador, os contatos e compromissos do usuário não são
     * excluídos. Ele cria uma lista para armazenar os erros e define a internacionalização para armazenar
     * a chaves dos arquivos de internacionalização nos catchies.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /**Cria a lista para armazenar o erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Define as configurações de internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**pega o id do usuário da página lista de usuário e atribui a variável id**/
            Integer id = Integer.parseInt(request.getParameter("id"));
            /**Instancia a variável dao para executar métodos da classe**/
            UsuarioDAO dao;
            dao = new UsuarioDAO();
            /**executa o método remove**/
            dao.remove(id);
        } catch (SQLException ex) {
            /**Se ocorrer erro de slq uma mensagem será armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        } catch (UsuarioInexistenteException ex) {
            /**se o usuário não existir uma mensagem é armazenada lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.excluir.usuario")));
        } catch (AgendaDAOException ex) {
            /**Se ocurrer algum erro de conexão uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
        }

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            /**Se a lista de erros na estiver vazia, a lista será armazenada na requisição**/
        }
        /**direciona para para lista de usuários**/
        RequestDispatcher rd = request.getRequestDispatcher("/usuario/listaUsuarios.view");
        rd.forward(request, response);
    }
}