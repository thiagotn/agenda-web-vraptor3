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
import javax.servlet.http.HttpSession;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.CompromissoInexistenteException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class EditaCompromissoServlet extends HttpServlet {

    /*
     *
     * Este servlet busca os dados no banco de dados e para que sejam exibidos na página de
     * requisição para que o usuário os altere. Ele cria uma lista de erros para armazenar os
     * erros ocorridos e os armazam como atributo para a requisição. Define as configurações de
     * internacionalização para armazenar as mensagens de erros dos catchies. Pega os dados
     * do usuário logado através da sessão para ser usado posteriormente. ele cria uma lista 
     * todos os contatos para exibi-los na página de requisição através de uma tab select e cria
     * um nova comprimisso através de uma pesquisa, se não existir armazem uma mensagem na lista
     * de erros.
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

        /**Cria a lista para armazenar os erros**/
        List<MensagemErro> errors = new ArrayList<>();

        /**Define a configuração de internacionalização para utilizá-la nos catchies**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**Pega o id do compromisso atrávés do input da página e  cria um novo compromisso**/
            Integer id = Integer.parseInt(request.getParameter("id"));
            Compromisso compromisso;
            /**pega o usuário logado na sessão**/
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
            /**Instancia o compromissoDAO e o ContatoDAO para realizar operações no banco**/
            CompromissoDAO dao = new CompromissoDAO();
            ContatoDAO cdao = new ContatoDAO();

            /**Cria um lista como todos os contatos**/
            List<Contato> contatos = cdao.getLista(usuario);
            /**Inserir a lista de contatos a requisição**/
            request.setAttribute("contatos", contatos);

            try {
                /**Verifica se o compromisso existe, se sim define os atributos na requisição**/
                compromisso = dao.pesquisar(id);
                request.setAttribute("id", compromisso.getId());
                request.setAttribute("data", compromisso.getData());
                request.setAttribute("hora", compromisso.getHora());
                request.setAttribute("local", compromisso.getLocal());
                request.setAttribute("descricao", compromisso.getDescricao());
                request.setAttribute("contato", compromisso.getContato());
            } catch (SQLException ex) {
                /**Se houver algum erro no sql uma mensagem é armazenada na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            } catch (CompromissoInexistenteException ex) {
                /**Se o compromisso não existe uma mensagem é armazenada na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.compromisso")));
            }
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro na conexão com a agenda uma mensagem é armazenda na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        }

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            /**Se a lista de erros não estiver vazia, ela é enviada à página de requisição**/
        }
        /**Direciona para página edita contatos.**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/editaCompromissos.jsp");
        rd.forward(request, response);
    }
}
