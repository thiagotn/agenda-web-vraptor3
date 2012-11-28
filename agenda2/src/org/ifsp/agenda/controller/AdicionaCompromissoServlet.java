package org.ifsp.agenda.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.mail.AuthenticationFailedException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.CompromissoDAOException;
import org.ifsp.agenda.exception.ContatoInexistenteException;
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.dao.CompromissoDAO;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.jdbc.dao.UsuarioDAO;
import org.ifsp.agenda.mail.Mensageiro;
import org.ifsp.agenda.mail.Mensagem;
import org.ifsp.agenda.modelo.Compromisso;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class AdicionaCompromissoServlet extends HttpServlet {

    /**
     *
     * Esta classe do tipo Servlet controller será utilizada para inserir
     * compromissos ao banco de dados, define o localização para
     * internacionalizar os resultados do catcheis, verifica se os dados
     * obrigatórios foram inseridos, localiza o contato, localiza o usuário
     * descriptografa as senha do usuário e enviar um e-mail para contato o qual
     * foi inserido ao compromisso através do método enviarEmailNovoUsuario. E
     * grava todos os erros (se ocorrem) ocorridas na lista de erros e dispacha
     * a reposta para a página lista lista de compromissos através da view
     * listaCompromissos.
     * 
     * @param request 
     * @param response 
     * @throws ServletException 
     * @throws IOException 
     *
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**Array para armazenar a lista de erros**/
        List<MensagemErro> errors = new ArrayList<>();

        /**pega a sessão da requisição**/    
        HttpSession session = request.getSession();
        /**Pega o usuário que foi definido no servlet LoginServlet armazenado na sessão **/
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");

        /**Cria as variáveis da página formCompromissos.jsp**/
        String contato = request.getParameter("contato");
        String data = request.getParameter("data");
        String hora = request.getParameter("hora");
        String local = request.getParameter("local");
        String descricao = request.getParameter("descricao");

        /**Define o locale para armazenar as respostas dos catchies na array de erros**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        /**Verifica se os campos obrigatótios foram inseridos, se não o resultados em inserido na array de erros.**/
        if (contato.isEmpty() || local.isEmpty() || descricao.isEmpty()) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        } else {
            /**Cria um novo compromisso e seta seus atributos**/
            Compromisso compromisso = new Compromisso();
            compromisso.setData(data);
            compromisso.setHora(hora);
            compromisso.setLocal(local);
            compromisso.setDescricao(descricao);
            compromisso.setContato(contato);
            compromisso.setUsuario(usuario.getLogin());
            try {
                /**define um novo contato e um compromisso **/
                Contato novoContato;
                /** Instancia o CompromissoDAO**/
                CompromissoDAO cdao = new CompromissoDAO();
                //Inserir o compromisso
                cdao.inserirCompromisso(compromisso);
                //Define usuário para enviar email para o contat                            
                /**Instancia ContatoDAO e excuta o método pesquisarContato para verificar se o contato existe.**/        
                ContatoDAO dao = new ContatoDAO();
               /**Pesquisa se o contato existe**/
                novoContato = dao.pesquisarContato(contato, usuario);
                try {
                    if (usuario.getSenhaEmail() != null) {
                       /**Senha não houver erros um email é enviado ao contato sobre o compromisso agendado**/
                        enviarEmailNovoUsuario(novoContato, compromisso, usuario);
                    }
                } catch (AuthenticationFailedException ex) {
                    /**Caso haja erro durante o envio do emial o resultado é armazenado aqui**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.enviar.email")));
                }
            } catch (ContatoInexistenteException ex) {
                /**se não existir o contato o resultado é armazendo aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.contato")));
            } catch (SQLException ex) {
                /**se não houver erros de sql o sistema o armazena aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            } catch (CompromissoDAOException ex) {
                /**se não houver erro ao instancia o compromissoDAO sistema o armazena aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.inserir.compromisso")));
            } catch (AgendaDAOException ex) {
                /**se não houver algum outro erro o sistema o armazena aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.compromisso.duplicado")));
            }
        }
        /**Verifica se ocorreu algum erro eles são armazenados na sessão**/
        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
        }
        /**Retorna para a página lista de compromissos**/
        RequestDispatcher rd = request.getRequestDispatcher("/compromisso/listaCompromissos.view");
        rd.forward(request, response);
    }

    /**
     *
     * @param contato
     * @param compromisso
     * @param usuario
     * @throws AuthenticationFailedException
     */
    public void enviarEmailNovoUsuario(Contato contato, Compromisso compromisso, Usuario usuario) throws AuthenticationFailedException {
        try {
            /**Monta a mensagem de email instanciando a Classe Mensageiro que possui todos os métodos para enviar emails**/
            Mensageiro mensageiro = new Mensageiro();
            String msg;
            /**Monta a mensagem e adicionar a atributos da classes envolvidas**/
            msg = "Olá %s, tudo bem? Bom estou lhe enviando este para informá-lo que no dia %s às %s "
                    + "vamos nos reunir-mos para conversármos sobre o(a) %s que será realizado no %s. Conto "
                    + "com a sua presença. \n\nDesde já agradeço a sua disposição. \n\nUm forte abraço\n"
                    + "\n%s\n%s";
            /**Adiciona os atribritos a mensagem com String.format.**/
            String mensagemTxt = String.format(msg, contato.getNome(), compromisso.getData(), compromisso.getHora(),
                    compromisso.getDescricao(), compromisso.getLocal(), usuario.getNome(), usuario.getEmail());
            Mensagem mensagem = new Mensagem();
            /**Criar uma nova mensagem e define o email do contato**/
            mensagem.setPara(contato.getEmail());
            /**Define o assunto do email e setar os atributos das classes ele.**/
            mensagem.setAssunto("Olá " + contato.getNome() + " - Você recebeu um agendamendo de " + usuario.getNome());
            /**Insere a mensagem ao email.**/
            mensagem.setMensagem(mensagemTxt);
            /**envia o e-mail**/
            mensageiro.enviarMensagem(mensagem, usuario);
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Ocorreu um erro de altenticação" + ex);
        }
    }
}
