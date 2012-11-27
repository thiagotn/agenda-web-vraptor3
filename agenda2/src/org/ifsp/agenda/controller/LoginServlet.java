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
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.dao.UsuarioDAO;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

    /**
     *
     * Este servelet autentica o usuário no sistema e o armazena na sessão ele cria uma lista de erros para
     * armazenar os erros ocorridos durante o processo, ele recebe os parâmentros da requisação e consultar no
     * banco de dados, se o usuário for encontrado o site é direcionada a página de lista de contatos. Ele 
     * também configura a internacionalização da paágina para armazenar as mensagens de erros que serão exibidas
     * na mesma página caso algum erro ocorra.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        /**Pega a sessao**/
        HttpSession session = request.getSession();
        /**Cria a lista de erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Pega os valores os inputs da página login e armazena na variável**/
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        
        /**Define a internacionalização para armazenar valores das chaves no catchies**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);
        
        /**Cria a variável dao para realizar operações no banco de dados**/
        UsuarioDAO dao = null;
        try {
            /**Instancia a variável dao**/
            dao = new UsuarioDAO();
        } catch (AgendaDAOException ex) {
            /**se algum erro ocorrer uma mensagem é armazenda na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("Edição", "Erro de conexão"));
        }
        /**Cria a variável usuário**/
        Usuario usuario = null;
        try {
            try {
                /**Define a variável usuário através do método autenticar**/
                usuario = dao.autenticar(login, senha);
            } catch (SQLException ex) {
                /**Se ocorrer algum erro de slq uma mensagem é armazenda na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            } catch (UsuarioInexistenteException ex) {
                /**See o usuário na existir uma mensagem de erro será armazenda na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.usuario")));
            }
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro de conexão o uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
        }

        RequestDispatcher rd;

        if (usuario != null) {
            /**se o usuário existir o usuário é adicionado sessão**/
            session.setAttribute("usuarioWeb", usuario);
            /**direciona o site para página lista de contatos**/
            rd = request.getRequestDispatcher("/contato/listaContatos.view");
        } else {
            /**se o usuário não for encontrado uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("usuario.invalido")));
            /**direciona para página login**/
            session.invalidate();
            rd = request.getRequestDispatcher("/login.jsp");
            
        }

        if (errors.size() > 0) {
            /**se a lista de erros não estiver vazia, ela atribuida na requisição**/
            request.setAttribute("errors", errors);
        }

        rd.forward(request, response);
    }
}
