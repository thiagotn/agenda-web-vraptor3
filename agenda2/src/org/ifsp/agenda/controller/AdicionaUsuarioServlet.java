package org.ifsp.agenda.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.AuthenticationFailedException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.UsuarioInexistenteException;
import org.ifsp.agenda.jdbc.dao.UsuarioDAO;
import org.ifsp.agenda.mail.Mensageiro;
import org.ifsp.agenda.mail.Mensagem;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;
import org.ifsp.agenda.util.PWSec;

/**
 *
 * @author Ton
 */

@SuppressWarnings("serial")
public class AdicionaUsuarioServlet extends HttpServlet {
    /**
     *
     * @param request 
     * @param response 
     * @throws ServletException 
     * @throws IOException 
     *
     * Este servlet adiciona uma novo usuário ao banco de dados. Ele pega todos os dados da página formUsuarios,
     * armazena os valores da página a variáveis, define a localização para usá-la para armazenar o erros na array 
     * de erros de acordo com as chaves definidas nos arquivos de internacionalização, para que os resultados sejam
     * exibidos na página (listaUsuarios quando o usuário for admin ou formUsuário quando for outro usuário).
     * Ao cadastrar o usuário o sistema envia um email informando ao usuário que ele foi adionado na Web Agenda,
     * com seu login, senha e link para utilizar o sistema. O sistema faz verificação do IP do host para facilitar
     * quando há o host receber ip dinâmico. Ele também desencriptograda a senha do usuário antes de enviar email.
     */

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**Criar array para armazenar os erros ocorridos**/
        List<MensagemErro> errors = new ArrayList<>();
        /**Pega o usuário da sessão e o armazena na variável usuario**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        /**Cria variáveis com os conteúdos da página de requisição**/
        String login = request.getParameter("login");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String gmail = request.getParameter("gmail");              
        Integer alerta = Integer.parseInt(request.getParameter("alerta"));

        /**Define a localização para utilizá-la nos catchies.**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        /**Instancia UsuárioDAO para fazer operações no banco de dados pelos métodos da Classe**/                
        UsuarioDAO usuDAO = null;
        if (!login.isEmpty() || !nome.isEmpty() || !email.isEmpty() || !senha.isEmpty()) {
          /**Verificar se as várias estão vazias caso contrário crie um REGEX para verificar**/
            /**se o email digita é válido.**/            
            Pattern regex = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher m = regex.matcher(email);
            boolean ok = m.matches();
            /**Se o não for email armazena o erro na array erros**/            
            if (!ok) {
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("email.validar.email")));
            } else {
                /**Se tudo estiver ok valores são setados a instancia novo do tipo Usuario**/                
                Usuario novo = new Usuario();
                novo.setLogin(login.replace(" ", ""));
                novo.setNome(nome);
                novo.setEmail(email);
                novo.setSenha(senha);
                novo.setAlerta(alerta);
                String novasenhaEmail = null;
                try {
                    /**Encriptografa a senha do gmail para armazenar o novo usuário**/                    
                    novasenhaEmail = PWSec.encrypt(gmail);
                } catch (Exception ex) {
                    /**se houver algum erro de conversão o usuário não tem senha Gmail**/
                   errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.enviar.email.sem.senha")));
                }
                /**Seta a senha nova ou novo usuário**/
                novo.setSenhaEmail(novasenhaEmail);
                try {
                    usuDAO = new UsuarioDAO();
                    String loginEmUso = null;
                    try {
                        /**Verifica se o nome do login usuário novo já existe no banco de dados.**/                        
                        loginEmUso = usuDAO.pesquisarLogin(novo.getLogin());
                    } catch (UsuarioInexistenteException ex) {
                        /**Se houver algum erro ao localizar o usuário o resultado é armazendo aqui**/
                         errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.usuario")));
                    }
                    if (loginEmUso != null) {
                        /**Se o login do usuário já existir o resultado será armazenado aqui**/
                        errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.login")));
                    } else {
                        /**Se tudo ok armazena o novo usuário**/                        
                        usuDAO.cadastrar(novo);
                        try {                       
                            if (usuario.getSenhaEmail() != null) {
                                enviarEmailNovoUsuario(novo, usuario);
                            }
                        } catch (AuthenticationFailedException ex) {
                            /**Se houver erro ao enviar email ele armazenda aqui**/
                            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.enviar.email")));
                        }
                    }
                } catch (SQLException ex) {
                    /**Armazena erro se houver algum erro ao inserir o usuário**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.inserir.usuario")));
                } catch (AgendaDAOException ex) {
                    /**Armazena erro se houver algum erro ao inserir ao validar o login**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.validar.login")));
                }
            }
        } else {
            /**Se os campos não forem preenchidos o resultados é armazenado aqui**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        }
        if (errors.size() > 0) {
            /**Adiciona os erros a requisação caso existam**/
            request.setAttribute("errors", errors);
        }        
        /**Direciona para para lista de usuários**/
        RequestDispatcher rd = request.getRequestDispatcher("/usuario/listaUsuarios.view");
        rd.forward(request, response);

    }

    /**
     *
     * @param novo
     * @param usuario
     * @throws AuthenticationFailedException
     */    
    public void enviarEmailNovoUsuario(Usuario novo, Usuario usuario) throws AuthenticationFailedException {
        try {
            /**Envia email ao novo usuário**/            
            Mensageiro mensageiro = new Mensageiro();
            /** Pega o IP do host**/            
            InetAddress end = InetAddress.getLocalHost();
            String ip = end.toString();
            /**Retira o nome do localhost e deixa somente o ip**/            
            String meuIp = ip.substring(ip.indexOf("/") + 1, ip.length());

            String msg;
            msg = "Olá %s, você foi adicionado ao meu projeto Web Agenda. Por favor me ajude a testá-lo."
                    + " Nele você poderá incluir seus contatos e compromissos e enviar emails. Cadastre os seus contatos"
                    + " e insira seus compromissos, os seus dados serão mantidos em sigilo. Para acessá-lo use o link"
                    + " (http://%s:8084/agenda) entre com o login é %s e a senha %s e depois altere as suas configurações"
                    + " com e-mail e senha do gmail. Se você não tiver uma conta no gmail por favor acesse o site"
                    + " http://www.gmail.com e crie uma, pois o sistema envia e-mails para contatos através dele. \n\nDesde"
                    + " já agradeço a sua colaboração. \n\nCaso não queira participar por favor responda este."
                    + "\n\n%s\n%s";

            String mensagemTxt = String.format(msg, novo.getNome(), meuIp, novo.getLogin(), novo.getSenha(), usuario.getNome(), usuario.getEmail());
            Mensagem mensagem = new Mensagem();
            mensagem.setPara(novo.getEmail());
            mensagem.setAssunto("Olá " + novo.getNome() + " - Você é um novo usuário da Web Agenda!");
            mensagem.setMensagem(mensagemTxt);
            mensageiro.enviarMensagem(mensagem, usuario);
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Ocorreu um erro de altenticação" + ex);
        }

    }
}
