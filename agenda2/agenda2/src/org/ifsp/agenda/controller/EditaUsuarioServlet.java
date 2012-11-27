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
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class EditaUsuarioServlet extends HttpServlet {

     /*
     *
     * Este servlet busca os dados no banco de dados e para que sejam exibidos na página de
     * requisição para que o usuário os altere. Ele cria uma lista de erros para armazenar os
     * erros ocorridos e os armazenam como atributo para a requisição. Define as configurações de
     * internacionalização para armazenar as mensagens de erros dos catchies. Pega os dados
     * do usuário logado através da sessão para ser usado posteriormente. Ele pesquisa no banco
     * de e tenta retornar o contato encontrado a página de requisição caso não encontre uma mensagem
     * de erro é exibida para o usuário. A sua finalidade é trazer os dados do usuário para serem alterados.
     *
     */
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    @SuppressWarnings("empty-statement")
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**pega o id do usuário na página de requisição**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        /**Cria um lista para armazenar os erros ocorridos**/
        List<MensagemErro> errors = new ArrayList<>();
        
        /**Instancia UsuarioDAO e Usuário**/
        UsuarioDAO dao = null;
        Usuario novoUsuario = null;
        /**Define as configurações de internacionalização através da sessão para armazenar os erros na lista**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);
        
        try {
            /**Define a variável dao para realizar operações no banco de dados**/
            dao = new UsuarioDAO();
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro de sql, ele será armazenado na lista de erro**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }
        try {
            /**Tenta criar um novo usuário através do retorno do método pesquisarUsuario**/
            novoUsuario = dao.pesquisarUsuarioId(id);
        } catch (SQLException ex) {
            /**Se o houver algum erro de sql ou conexão o  erro é armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.usuario")));
        } catch (UsuarioInexistenteException ex) {
            /**Se o usuário não existir o erro é armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.inexistente")));;
        } catch (AgendaDAOException ex) {
            /**Se houver algum outro erro, uma mensagem será armazendo na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
        }
        /**Se não houver nenhum erro os dados do usuário são setados na requisição da página**/
        request.setAttribute("id", novoUsuario.getId());
        request.setAttribute("login", novoUsuario.getLogin());
        request.setAttribute("nome", novoUsuario.getNome());
        request.setAttribute("email", novoUsuario.getEmail());
        request.setAttribute("senha", novoUsuario.getSenha());
        request.setAttribute("alerta", novoUsuario.getAlerta());
        request.setAttribute("gmail", novoUsuario.getSenhaEmail());

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            /**se a lista de erros não estiver vazio, os erros serão armazendados como atributos da página**/
        }
        /**Direciona para para página editaUsuários para que o usuário altere os dados**/
        RequestDispatcher rd = request.getRequestDispatcher("/usuario/editaUsuarios.jsp");
        rd.forward(request, response);
    }
}