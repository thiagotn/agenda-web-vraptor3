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
import org.ifsp.agenda.exception.CompromissoDAOException;
import org.ifsp.agenda.exception.CompromissoInexistenteException;
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class LocalizaCompromissoServlet extends HttpServlet {
     /*
     * @author Ton
     *
     * Este servlet é usuado para filtrar o dados na lista de compromisso de acordo com a escolhar do
     * usuário se nenhuma informação for filtro serão exibos todos os compromissos, o usuário pode localizar
     * compromissos por hora, data e local. Estas opções estão dispostas na página lista compromissos.
     * O direcionamente desde servlet irá para mesma página a nova lista de compromissos. Ele verifica o 
     * usuário logado na sessão, cria uma lista de erros para armazenar os erros ocorridos durante o processo.
     * Configura a internacionalização para buscar as chaves nos arquivos de internacionalização para armazenar
     * na lista lista de erros as mensagens referentes a lingua definida na sessão
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
        
        /**Pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**Cria a lista para armazenar os erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**cria lista para armazenar os compromissos**/
        List<Compromisso> compromissos = new ArrayList<>();

        /**Pega os valores da pagina lista de compromissos**/
        String por = request.getParameter("por");
        String oque = request.getParameter("oque");
        /**Define a internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**Instancia CompromissoDAO para realizar operações no banco de dados**/
            CompromissoDAO dao = new CompromissoDAO();
            try {
                /**define a lista de compromissos através do retorno do método buscarCompromissos**/
                compromissos = dao.buscarCompromissos(usuario, por, oque);
                /**Atribui lista de compromisso a requisicão**/
                request.setAttribute("compromisso", compromissos);
            } catch (SQLException | CompromissoInexistenteException | CompromissoDAOException ex) {
                /**se ocorrer algum erro ao localizar os compromissos ele é armazenda na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.compromisso")));
            } catch (UsuarioInexistenteException ex) {
                /**se o usuário não encontrado o erro é armazenda na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.inexistente")));
            }
        } catch (AgendaDAOException ex) {
            /**se ocorrer algum erro de sql ele é armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.converter.data")));
        }
        if (errors.size() > 0) {
            /**se a lista de erros não estiver vazia, ela é atribuida a requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para lista de compromissos**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/listaCompromissos.jsp");
        rd.forward(request, response);
    }
}
