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
public class IndexServlet extends HttpServlet {

    /*
     *
     * Este servlet reconfigurar a página index que é exibido ao entra no site, se o usuário já estiver
     * logado ela exibirá a lista de contatos, caso contrário será exibido a página de boas vindas. 
     * Aqui o os dados serão difinidos para configurar a tag diplaytab que utilizar de uma arraylista
     * para configurar a tabela. Para os dados sejam exibidos ele criar uma lista para armazenar os erros
     * e outra com os contatos, além disto ele pega as informações do usuário logado na sessão. Este site
     * implementa a Classe filter para monitorar as páginas acessadas.
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

        /**Pega o usuário armazenado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**Cria a lista de erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Define para onde a página será direcionada**/
        RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
        /**Cria a lista de contatos**/
        List<Contato> contatos = new ArrayList<>();
        /**Instancia ContatoDAO**/
        ContatoDAO dao = null;
        /**Define a configurações de internacionalização para armazenar o erros na lista de erros**/
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
            /**Se ocorrer algum erro, ele será armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }
        try {
            /**Define a variável contatos através do retorno do método gelist**/
            contatos = dao.getLista(usuario);
        } catch (AgendaDAOException ex) {
            /**Se ocorrer ao erro ao localizar os contatos um erro é armazenado na lista de errors**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.contato")));
        }
        if (errors.size() > 0) {
            /**Se a lista de erros não estiver vazia, os erros serão enviados a requisição**/
            request.setAttribute("errors", errors);
        }
        /**Envia a lista de contatos para a requisição**/
        request.setAttribute("contatos", contatos);
        rd.forward(request, response);
    }
}
