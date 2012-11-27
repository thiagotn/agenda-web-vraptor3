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
public class ListaUsuarioServlet extends HttpServlet {
     /*
     *
     * Este servlet reconfigurar a página lista de usuários para exibi-los na tag displaytab para 
     * exibir uma tabela como todos os usuários de usuário logado for o administraod.  além disto ele 
     * pega as informações do usuário logado na sessão. É nessário que seja retornada uma lista com todos
     * os contatos. Ele também configura os arquivos de internacionalização para definir a mensagems de 
     * erros que serão exibidas na página lista de usuários.
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
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /**Cria a lista de erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Cria a variável dao para realizar operações no banco de dados.**/
        UsuarioDAO dao = null;
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
            /**Define a variável dao;**/
            dao = new UsuarioDAO();
        } catch (AgendaDAOException ex) {
              /**se ocorrer algo erro uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));          
        }
        /**Cria a lista para armazenar o usuários**/
        List<Usuario> usuario = null;

        try {
            try {
                /**Define a lista com retorno do método getlista**/
                usuario = dao.getLista();
            } catch (SQLException ex) {
                /**Se ocorrer algum erro ao localizar os usuários um erro é armazenado na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.usuario")));
            } catch (UsuarioInexistenteException ex) {
                /**Se não existir nenhum usuário uma mensagem é armazenada com lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.inexistente")));
            }
        } catch (AgendaDAOException ex) {
            /**Se houver algum outro erro de sql uma mensagem é armazenada na lista de erros.**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }

        if (errors.size() > 0) {
            /**Se a lista de erros estiver vazia, ela é armazenada na requisição da página lista de usuários**/
            request.setAttribute("errors", errors);
        }
        /**definir a lista de usuário na requisição**/
        request.setAttribute("usuario", usuario);
        /**direciona para página lista usuários**/
        RequestDispatcher rd = request.getRequestDispatcher("/usuario/listaUsuarios.jsp");
        rd.forward(request, response);
    }
}
