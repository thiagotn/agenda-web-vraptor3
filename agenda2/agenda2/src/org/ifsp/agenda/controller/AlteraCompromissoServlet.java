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
import org.ifsp.agenda.exception.CompromissoDuplicadoException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class AlteraCompromissoServlet extends HttpServlet {

    /*
     * Este servlet altera o dos dados de um compromisso. Ele pega o usuário que está logado na
     * sessão, cria uma lista para armazena os erros ocorridos, cria váriáveis para armazenar os 
     * valores da página editaCompromissos, instancia compromisso e se todos os atribritos requeridos
     * estiverem preenchidos ele tentará gravar a alteração no banco de dados, caso o compromisso já
     * exista um erro será armazendo na lista de erros. Ele define a localização para armazenar na 
     * lista erros os erros de acordo como as propriedades dos arquivos de internacionalizaçao. E por
     * fim direciona a requição a lista de compromisso. Se houver algum erro, ele será exibido ao 
     * usuário.
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
        
        //Cria a lista para armazenar os erros
        List<MensagemErro> errors = new ArrayList<>();
        
        /**Pega o usuário armazenado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb"); 
        
        /**Cria variáveis para armazendar os valores da requisição**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        String data = request.getParameter("data");
        String hora = request.getParameter("hora");
        String local = request.getParameter("local");
        String descricao = request.getParameter("descricao");
        String contato = request.getParameter("contato");
        
        /**Define as configurações de internacionalização para utilizá-los nos catchies.**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);
        
        /**Se os campos obrigatórios não forem todos preenchidos ele armazena o erro**/
        if (contato.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        } else {
            /**Caso contrário instancia compromisso e seta seus aributos com as váriáveis da requisição**/
            Compromisso compromisso = new Compromisso();
            CompromissoDAO dao;
            compromisso.setId(id);
            compromisso.setData(data);
            compromisso.setHora(hora);
            compromisso.setLocal(local);
            compromisso.setDescricao(descricao);
            compromisso.setContato(contato);
            compromisso.setUsuario(usuario.getLogin());

            try {
                /**Tentar atualizar o compromisso**/
                dao = new CompromissoDAO();
                dao.atualizarCompromisso(compromisso);
            } catch (SQLException ex) {
                /**Se algum erro de SQL armazena o erro na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
            } catch (CompromissoDuplicadoException ex) {
                /**Se o compromisso existir o erro é armazenado na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.compromisso.duplicado")));
            } catch (AgendaDAOException ex) {
                /**Se o compromisso não existir o erro é armazenado na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.compromisso.inexistente")));
            }
        }
        if (errors.size() > 0) {
            /**Se a lista de erro não estiver vazia os erros são atributos a requisição**/
            request.setAttribute("errors", errors);
        }
        /**direciona para a lista de compromissos.**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/listaCompromissos.view");
        rd.forward(request, response);

    }
}
