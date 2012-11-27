package org.ifsp.agenda.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.ifsp.agenda.util.PWSec;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class AlteraUsuarioServlet extends HttpServlet {
     /*
     *
     * Este servlet altera o dos dados de um usuário. Ele pega o usuário que está logado na
     * sessão, cria uma lista para armazena os erros ocorridos, cria váriáveis para armazenar os 
     * valores da página editaUsuarios. Verifica se todos os atribritos requeridos estiverem preenchidos
     * ele tentará gravar a alteração no banco de dados, caso o contato já exista um erro será armazendo 
     * na lista de erros. Ele define a localização para armazenar na lista erros os erros de acordo como as
     * propriedades dos arquivos de internacionalizaçao. E por fim direciona a requição a lista de contatos.
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
        //cria a lista para armazenar os erros ocorridos
        List<MensagemErro> errors = new ArrayList<>();
        //Pega o usuário logado no sessão
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");
        //define as variáveis da requisão
        String login = request.getParameter("login");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String gmail = request.getParameter("gmail").trim();
        Integer alerta = Integer.parseInt(request.getParameter("alerta"));
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        //Define as configurações de internacionalização para utilizá-las nos cathies
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);
        //Verifica se os campos obrigatórios foram preenchidos se houve, se não armazena o erro na lista
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || senha.equals("null")) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        } else {
            //Se os dados estvirem ok ele instancia contatoDAO e Usuario
            UsuarioDAO usuarioDAO;
            Usuario novo = null;
            if (!nome.isEmpty() || !email.isEmpty() || !senha.isEmpty()) {
                //Se o nome, o email e a senha não estiverem preenchidads
                //Criar regex para validar o e-mail digitado
                Pattern regex = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher m = regex.matcher(email);
                boolean ok = m.matches();
                //Se o e-mail não for válido,armazena o erro na lista de erros
                if (!ok) {
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("email.validar.email")));
                } else {
                    try {
                        //Casso contrário instancia UsuaarioDAO para fazer operações no banco de dados
                        usuarioDAO = new UsuarioDAO();
                        try {
                            //Verifica se o usuário existe
                            novo = usuarioDAO.pesquisarUsuario(login);
                        } catch (UsuarioInexistenteException ex) {
                            //Se houver erro duarante a pesquisa, armazena o resultado na lista de erros
                            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.usuario")));
                        }
                        //Verifica se o novo usuário existe e difine seus atributos com as variáveis de entrada
                        if (novo instanceof Usuario) {
                            novo.setNome(nome);
                            novo.setEmail(email);
                            novo.setSenha(senha);
                            novo.setAlerta(alerta);
                            String novasenhaEmail = null;
                            try {
                                //Cria uma nova senha critografada para o usuário 
                                novasenhaEmail = PWSec.encrypt(gmail);
                            } catch (Exception ex) {
                                //Se houver errro durante a criptografia, um erro é armazenado na lista de erros
                                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("email.senha.gmail")));
                            }
                            try {
                                //Se tudo ok, setar a nova senha ao usuário e tenta alterar os dados no banco de dados
                                novo.setSenhaEmail(novasenhaEmail);
                                usuarioDAO.alterar(novo);
                            } catch (UsuarioInexistenteException ex) {
                                //Se houver algum erro uma mensagem é armazenada na lista de erros
                                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.inexistente")));
                            }
                        } else {
                            //armazena um erro na lista caso ocorra na lista de erros
                            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.usuario.login")));
                        }
                    } catch (SQLException ex) {
                        //se houver algum erro durante a alteração, uma mensagem é armazenda na lista de erros
                        errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.alterar.usuario")));
                    } catch (AgendaDAOException ex) {
                        //Se houver algum erro na validação dos dados uma mensagem é armazenada na lista de erros
                        errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.validar.login")));
                    }
                }
            } else {
                //Se todos os campos não forem preenchidos uma mensagem é armazenada na lista de erros
                 errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
            }
        }        
        if (errors.size() > 0) {
            //Se a lista de erros não estiver vazia. Define o artributos errors na requisição
            request.setAttribute("errors", errors);
        }
        RequestDispatcher rd = null;
        if (usuario.getLogin().equals("admin")) {
            //Se usuário for admin direciona para lista de usuários
            rd = request.getRequestDispatcher("/usuario/listaUsuarios.view");
            //Caso contrario direciona para lista de usuários
        } else {
            rd = request.getRequestDispatcher("/contato/listaContatos.view");
        }
        rd.forward(request, response);
    }
}
