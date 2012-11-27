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
import org.ifsp.agenda.exception.ContatoInexistenteException;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;

/**
 *
 * @author Ton
 */

@SuppressWarnings("serial")
public class RemoveContatoServlet extends HttpServlet {

     /**
     * Este servlet exclui um contato do banco de dados através do parâmetro id para página 
     * lista de contatos, cria uma lista para armazenar os erros e define a internacionalização
     * para armazenar a chaves dos arquivos de internacionalização nos catchies
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**Cria a lista para armazenar erros**/
        List<MensagemErro> errors = new ArrayList<>();
        
        /**pega o parâmentro id da página de requisição e atribuir na variável id**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        /**cria variáveis dao e contato**/
        ContatoDAO dao;
        Contato contato;
        /**Define as configurações de interncionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**define dao como uma nova instancia para realizar operações no banco de dados**/
            dao = new ContatoDAO();
            /**Atribui o retorno do método pesquisar a variável contato**/
            contato = dao.pesquisar(id);
            /**Executa o método excluir para remover o contato**/
            dao.remove(contato);
        } catch (ContatoInexistenteException ex) {
            /**Se o contato no existir uma mensagem é armazenda na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.contato")));
        } catch (SQLException ex) {
            /**Se houver algum de sql uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro de conexão uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.excluir.contato")));
        }
        if (errors.size() > 0) {
            /**se a lista de erros não estiver vazia, ela será atribuida na requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para para lista de contatos**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/listaContatos.view");
        rd.forward(request, response);
    }
}
