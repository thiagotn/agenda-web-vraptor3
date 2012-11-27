package org.ifsp.agenda.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class ListaContatoServlet extends HttpServlet {

     /*
     *
     * Este servlet reconfigurar a página lista de contatos para exibi-los na tag displaytab para 
     * exibir uma tabela como todos os contatos.  além disto ele pega as informações do usuário logado
     * na sessão. É nessário que seja retornada uma lista com todos os contatos. Ele também configura
     * os arquivos de internacionalização para definir a mensagems de erros que serão exibidas na página
     * lista de contatos.
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

        /**pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**instancia a Classe ContatoDAO para realizar operações no banco de dados**/
        ContatoDAO dao = null;
        
        /**Cria a lista de erros para armazenar erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**define as configurações de internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**Define a variável dao**/
            dao = new ContatoDAO();
        } catch (AgendaDAOException ex) {
            /**se houver algum erro uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }
        /**Cria a lista de contatos**/
        List<Contato> contatos = null;

        try {
            /**Define a lisata de contatos com o retorno do método getLista**/
            contatos = dao.getLista(usuario);
        } catch (AgendaDAOException ex) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.contato")));
        }
        request.setAttribute("contatos", contatos);
        
        if (errors.size() > 0) {
            /**Se a lista de erros não estiver vazia, ela é enviada para requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para página lista de contatos**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/listaContatos.jsp");
        rd.forward(request, response);
    }
}
