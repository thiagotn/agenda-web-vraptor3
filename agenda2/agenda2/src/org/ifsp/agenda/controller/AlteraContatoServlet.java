package org.ifsp.agenda.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.ifsp.agenda.modelo.ContatoPessoal;
import org.ifsp.agenda.modelo.ContatoProfissional;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class AlteraContatoServlet extends HttpServlet {
   
     /*
     *
     * Este servlet altera o dos dados de um usuário. Ele pega o usuário que está logado na
     * sessão, cria uma lista para armazena os erros ocorridos, cria váriáveis para armazenar os 
     * valores da página editaContatos. Verifia se todos os atribritos requeridos, se estiverem 
     * preenchidos ele tentará gravar a alteração no banco de dados, caso o contato já exista um erro
     * será armazendo na lista de erros. Ele faz conversão da string tipo data em Date para armazená-la
     * e também define a localização para armazenar na lista erros os erros de acordo com as propriedades 
     * dos arquivos de internacionalizaçao. E por fim direciona a requição a lista de contatos. 
     * Se houver algum erro, ele será exibido ao usuário.
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
       /**Pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        
        /**Atributos os valores da requisição em variávies**/
        Integer id = Integer.parseInt(request.getParameter("id"));
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String telefone = request.getParameter("telefone");
        String celular = request.getParameter("celular");
        String tipo = request.getParameter("classe");
        
        /**Difine as configurações de internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        /**Verifica se o campos obrigatários estão preenchidos, se não estiverem armazena o erro na lista**/
        if (nome.isEmpty() || email.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        } else {
            /**Se todos os dados estiverem preenchidos, instancia contato e ContatoDAO**/
            Contato contato;
            ContatoDAO dao = null;
            try {
                dao = new ContatoDAO();
            } catch (AgendaDAOException ex) {
                /**Se ocorreu algum erro ao instanciar contatoDAO, um erro é armazenado na lista de erros**/
                  errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            }
            if (tipo.equals("pessoal")) {
                /**Se o variável tipo for pessoal, instancia contatoPessoal**/
                contato = new ContatoPessoal();
                /**Pega o valor dataEmTexto para convertê-lo no tipo Date**/
                String dataEmTexto = request.getParameter("data");
                Date dataNascimento;
                try {
                    /**tenta converter a string do DataEmTexto**/
                    dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataEmTexto);
                    /**seta a data convertida o contato**/
                    ((ContatoPessoal) contato).setDataNascimento(dataNascimento);
                } catch (ParseException e) {
                    /**Se houver algum erro, ele é armazenado na lista de erros**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.converter.data")));
                }
                /**Seta o atribrito celular ao contato através de casting**/
                ((ContatoPessoal) contato).setCelular(celular);
            } else {
                /**Se for contato profissional, instancia contatoProfissional**/
                contato = new ContatoProfissional();
                /**Seta o atributo empresa com a variável da requisição**/
                String empresa = request.getParameter("empresa");
                /**Seta o atributo empressa ao contato instanciado**/
                ((ContatoProfissional) contato).setNomeEmpresa(empresa);
            }
            /**Seta os atributos restantes ao contato instanciado**/
            contato.setId(id);
            contato.setNome(nome);
            contato.setEmail(email);
            contato.setTelefone(telefone);
            contato.setEndereco(endereco);

            try {
                if (dao.verificaContato(contato.getNome(), contato.getEmail(), usuario.getLogin()) == true) {
                    try {
                        /**Verifica se nome, email e login já exisitem**/
                        dao = new ContatoDAO();
                        /**se sim altera o contato no banco de dados**/
                        dao.altera(contato);
                    } catch (ContatoInexistenteException ex) {
                        /**Se houver algum erro, ele será armazenado na lista de erros**/
                        errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.inexistente")));
                    }
                } else {
                    /**Se o contato já existir, um erro é armazenado na lista de erros**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.duplicado")));
                }
            } catch (SQLException | AgendaDAOException ex) {
                /**Se houver algum erro de sql ou de conexão com a agenda um erro é armazenado na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            }
        }
        if (errors.size() > 0) {
            /**se a lista de erros não for vazia, a lista é armazenada na requisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para lista de contatos. Seu houver algum erro ele será informado**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/listaContatos.view");
        rd.forward(request, response);
    }
}