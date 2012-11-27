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
import org.ifsp.agenda.modelo.ContatoPessoal;
import org.ifsp.agenda.modelo.ContatoProfissional;
import org.ifsp.agenda.modelo.MensagemErro;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class EditaContatoServlet extends HttpServlet {

      /*
     *
     * Este servlet busca os dados no banco de dados e para que sejam exibidos na página de
     * requisição para que o usuário os altere. Ele cria uma lista de erros para armazenar os
     * erros ocorridos e os armazam como atributo para a requisição. Define as configurações de
     * internacionalização para armazenar as mensagens de erros dos catchies. Pega os dados
     * do usuário logado através da sessão para ser usado posteriormente. Ele pesquisa no banco
     * de e tenta retornar o contato encontrado a página de requisição caso não encontre uma mensagem
     * de erro é exibida para o usuário.
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

        //Cria a lista para armazenar os erros**/
        List<MensagemErro> errors = new ArrayList<>();
        
        /**Cria a variável classe ser utilizada para definir o tipo de contato**/
        String classe = null;
        /**Cria um contato nulo**/
        Contato contato = null;
        /**pega o id do contato definido na página de requisição**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        /**Define as configurações de internacionalização para inserir dados na lista de erros**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**Instancia ContatoDAO para utilizar o métodos para realizar operações no banco de dados**/
            ContatoDAO dao = new ContatoDAO();
            try {
                /**Redifine contato através do retorno do método pesquisar**/
                contato = dao.pesquisar(id);
            } catch (ContatoInexistenteException | SQLException ex) {
                /**Se o contato não existir uma mensagem é armazenada na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.inexistente")));
            }
            /**Caso contrário define o atributos comuns para página de requisição**/
            request.setAttribute("id", contato.getId());
            request.setAttribute("nome", contato.getNome());
            request.setAttribute("endereco", contato.getEndereco());
            request.setAttribute("telefone", contato.getTelefone());
            request.setAttribute("email", contato.getEmail());
            /**Se o contato encontrado for tipo pessoal**/
            if (contato instanceof ContatoPessoal) {
                /**Defina a variável classe para ser utilizada pela página de requisição**/
                /**para exibir os dados do tipo de contato correto**/
                classe = "pessoal";
                /**Define os atributos restantes de contatoPessoal na requisição**/
                request.setAttribute("data", ((ContatoPessoal) contato).getDataNascimento());
                request.setAttribute("celular", ((ContatoPessoal) contato).getCelular());
            } else {
                /**Se o contato for profissional define o atributo empresa para requisição**/
                classe = "profissional";
                request.setAttribute("empresa", ((ContatoProfissional) contato).getNomeEmpresa());
            }
        } catch (AgendaDAOException ex) {
            /**Se o contato já existir insere mensagem de erro na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.inexistente")));
        }
        if (errors.size() > 0) {
            /**se a lista de erros não estiver vazia, ela é enviada com atributo para a requisição**/
            request.setAttribute("errors", errors);
        }
        /**Envia a variavél classe para página de requisição**/
        request.setAttribute("classe", classe);
        /**Direciona para página editaContatos**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/editaContatos.jsp");
        rd.forward(request, response);
    }
}
