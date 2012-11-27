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
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.CompromissoInexistenteException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.modelo.MensagemErro;

/**
 *
 * @author Ton
 */

@SuppressWarnings("serial")
public class RemoveCompromissoServlet extends HttpServlet {
     /**
     * Este servlet exclui um comprimisso do banco de dados através do parâmetro id para página 
     * lista de compromissos, cria uma lista para armazenar os erros e define a internacionalização
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
        /**pega o id comprimisso da lista de compromissos e armazenada na variável id**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        /**Cria a lista armazenar os erros**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Cria a variável dao**/
        CompromissoDAO dao;
        /**define a internacionalização da página utilizar nos catchies**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);
        
        try {
            /**instancia a variável dao**/
            dao = new CompromissoDAO();
            try {
                /**Executa o método excluir da Classe CompromissoDAO**/
                dao.remove(id);
            } catch (CompromissoInexistenteException ex) {
                /**Se houver algum erro uma mensagem será armazenada na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.compromisso")));
            }
        } catch (AgendaDAOException ex) {
            /**se houver algum erro de conexão uma mensagem é armazenada na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.excluir.compromisso")));
        }
        
        if (errors.size() > 0) {
            /**se a lista de erros não estiver vazia o seu conteúdo é armazenado na requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para lista de compromissos**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/listaCompromissos.view");
        rd.forward(request, response);
    }
}
