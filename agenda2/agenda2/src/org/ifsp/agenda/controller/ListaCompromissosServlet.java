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
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class ListaCompromissosServlet extends HttpServlet {

     /*
     *
     * Este servlet reconfigurar a página lista de compromissos para exibi-los na tag displaytab para 
     * exibir uma tabela como todos os compromissos.  além disto ele pega as informações do usuário logado
     * na sessão. É nessário que seja retornada uma lista com todos os compromissos. Ele também configura
     * os arquivos de internacionalização para definir a mensagems de erros que serão exibidas na página
     * lista de compromissos.
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
        /**pega os dados do usuário logado definidos na sessão**/
        HttpSession session = request.getSession();        
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**instancia a variável dar para executar métodos para realizar operações no banco de dados**/
        CompromissoDAO dao = null;
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
            /**Define a variável dao**/
            dao = new CompromissoDAO();
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro uma mensagem é armazenada na lista de errros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }
        /**Cria uma lista do tipo Compromisso para armazenar os compromissos**/
        List<Compromisso> compromisso = null;
        try {
            /**Define compromisso com retorno do método getlista**/
            compromisso = dao.getLista(usuario);
        } catch (AgendaDAOException ex) {
            /**Se ocorrer algum erro ele será armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.compromisso")));
        }
        /**Atribui a lista de contatos a requisição**/
        request.setAttribute("compromisso", compromisso);
        
        if (errors.size() > 0) {
            /**Se a lista de erros não estiver vazio, é atribuido a lista de erros as requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para página lista de compromissos**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/listaCompromissos.jsp");
        rd.forward(request, response);
    }
}
