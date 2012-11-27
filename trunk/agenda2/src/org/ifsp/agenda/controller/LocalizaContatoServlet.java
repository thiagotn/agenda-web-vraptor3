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
import org.ifsp.agenda.exception.ContatoInexistenteException;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class LocalizaContatoServlet extends HttpServlet {
     /**
      * @param request 
      * @param response 
      * @throws ServletException 
      * @throws IOException 
      * @author Ton
     *
     * Este servlet é usuado para filtrar o dados na lista de contatos de acordo com a escolhar do
     * usuário se nenhuma informação for filtro serão exibos todos os contatos, o usuário pode localizar
     * contatos por nome, empresa e data de nascimento. Estas opções estão dispostas na página lista de contatos.
     * O direcionamente desde servlet irá para mesma página a nova lista de contatos. Ele verifica o 
     * usuário logado na sessão, cria uma lista de erros para armazenar os erros ocorridos durante o processo.
     * Configura a internacionalização para buscar as chaves nos arquivos de internacionalização para armazenar
     * na lista lista de erros as mensagens referentes a lingua definida na sessão
     *
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**Pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**cria a lista de erros para armazem o erros gerados pelos cathies**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Cria uma lista para armazenar todos os contatos**/
        List<Contato> contatos = new ArrayList<>();
        /**Obtem os parâmetros escolhidos pelo usuário na requisição**/
        String por = request.getParameter("por");
        String oque = request.getParameter("oque").toUpperCase();
        /**Define a configuração de internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**instancia ContatoDAO para realizar operações no banco de dados**/
            ContatoDAO dao = new ContatoDAO();
            try {
                /**Define contatos através do retorno do método getLista**/
                contatos = dao.getLista(usuario, por, oque);
                /**Atribui a lista de contatos a requisicao**/
                request.setAttribute("contatos", contatos);
            } catch (ContatoInexistenteException ex) {
                /**Se não existir contatos uma mensagem será armazenda na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.inexistente")));
            }

        } catch (AgendaDAOException ex) {
            /**Se ocorrer algum erro de sql uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }
        if (errors.size() > 0) {
            /**se lista de erros não estiver vazia, ela é atribuida na requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para página lista de contatos**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/listaContatos.jsp");
        rd.forward(request, response);
    }
}
