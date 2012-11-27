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
import javax.mail.AuthenticationFailedException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ifsp.agenda.exception.AgendaDAOException;
import org.ifsp.agenda.exception.ContatoDuplicadoException;
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.mail.Mensageiro;
import org.ifsp.agenda.mail.Mensagem;
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
public class AdicionaContatoServlet extends HttpServlet {

    /**
     *
     * @param request 
     * @param response 
     * @throws ServletException 
     * @throws IOException 
     *
     * Este servlet adiciona contatos ao banco de dados. Ele resgata o usuário
     * logado, pega todos os valores da página formContatos, define a
     * localização para armazenar os erros ocorridos na array de erros e os
     * armazena na requisição da página para que ele sejam exibidos ao usuário
     * na página listaContatos. Ele verifica e a senha do usuário e a
     * desencriptogra para que seja enviado um email ao contado após a sua
     * inclusão. Ele também verfica os dados dos inputs e converte a string data
     * para o tipo Date para armazená-lo ao campo do tipo data do banco de
     * dados.
     *
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /**Cria array para armazenar os erros ocorridos**/
        List<MensagemErro> errors = new ArrayList<>();

        /**Pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");

        /**Atribui os valores da requisão à variáveis**/
        String tipo = request.getParameter("tipo");
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String endereco = request.getParameter("endereco");
        String telefone = request.getParameter("telefone");

        /**Define a lingua para auxiliar o armazenamento dos do que acordo com a chaves dos arquivos de internacionalizaçao**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        /**Se os campos obrigatórios não forem atribuídos, o erro é armazenado na array erros.**/
        if (nome.isEmpty() || email.isEmpty() || endereco.isEmpty() || telefone.isEmpty()) {
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.preencher.campos")));
        } else {
            /**instancia a classe contato para verificar o tipo de contato resgatado através da variável tipo**/
            Contato contato;
            /**Se contato for pessoal define as variáveis celular e data de nascimento.**/
            if (tipo.equals("pessoal")) {
                contato = new ContatoPessoal();
                String celular = request.getParameter("celular");
                String dataEmTexto = request.getParameter("data");
                Date dataNascimento;
                try {
                    /**Converte a variável dataEmTexto para o tipo Date**/
                    dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(dataEmTexto);
                    /**Seta a data de nascimento no contato instanciado**/
                    ((ContatoPessoal) contato).setDataNascimento(dataNascimento);
                } catch (ParseException e) {
                    /**Se houver erro de conversão o resultado é armazendado na array de erros**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.converter.data")));
                }
                /**setar o celular ao contato através de casting para identificar o tipo de contato**/
                ((ContatoPessoal) contato).setCelular(celular);
                contato.setUsuario(usuario);
            } else {
                /**Se o contato  for profissiona é instanciado ContatoProfissional e o atributo empresa**/
                contato = new ContatoProfissional();
                String empresa = request.getParameter("empresa");
                /**Casting para definir a instancia genérica contato**/
                ((ContatoProfissional) contato).setNomeEmpresa(empresa);
            }
            /**Seta o restante dos atributos a classe contato**/
            contato.setNome(nome);
            contato.setEmail(email);
            contato.setEndereco(endereco);
            contato.setTelefone(telefone);
            contato.setUsuario(usuario);
            /**Instancia ContatoDAO para fazer operações no banco de dados**/
            ContatoDAO dao = null;
            try {
                dao = new ContatoDAO();
            } catch (AgendaDAOException ex) {
                /**Se houver algum erro na instancia de ContatoDAO o erro é armazenado aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
            }
            try {
                /**Pesquisa se o nome, o email e o login já existe para não deixar repetir contatos no banco de dados**/
                if (dao.verificaContato(contato.getNome(), contato.getEmail(), usuario.getLogin()) == false) {
                    try {
                        /**Adiciona o contato ao banco de dados através do método adiciona**/
                        dao.adiciona(contato);
                        try {
                            if (usuario.getSenhaEmail() != null) {
                                /**Senha a senha não for nula envia um email ao contato**/
                                enviarEmailNovoUsuario(contato, usuario);
                            }
                        } catch (AuthenticationFailedException ex) {
                            /**Se houver erros durante o envio ele armazena aqui**/
                            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.enviar.email")));
                        }
                    } catch (ContatoDuplicadoException ex) {
                        /**Se o contato já existe o resultado é armazenado aqui**/
                        errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.duplicado")));
                    }
                } else {
                    /**Se algum qualquer erro na instancia de contatoDAO o resultato é armazenado aqui**/
                    errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.inserir.contato")));
                }
            } catch (SQLException | AgendaDAOException ex) {
                /**Se houver qualquer outro erro de sql o erro é armazenado aqui**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.sql")));
            }
        }
        if (errors.size() > 0) {
            /**Se a lista de erros não estiver vazia o resultado é armazenado na resquisição**/
            request.setAttribute("errors", errors);
        }
        /**Direciona para a lista de contatos**/
        RequestDispatcher rd = request.getRequestDispatcher("/contato/listaContatos.view");
        rd.forward(request, response);
    }

    /**
     *
     * @param contato
     * @param usuario
     * @throws AuthenticationFailedException
     */
    public void enviarEmailNovoUsuario(Contato contato, Usuario usuario) throws AuthenticationFailedException {
        try {
            /**Envia e mail ao contato informando-o que ele foi adicionado a lista de contatos.**/
            Mensageiro mensageiro = new Mensageiro();
            String msg;
            msg = "Olá %s, você vou adicionado a minha lista de contatos. Em breve estarei lhe enviando informações"
                    + " quando eu agendar algum coomprimisso com você, caso não queira receber estas informações por favor me"
                    + " me informe respondendo este. \n\nUm forte abraço.\n\n%s\n%s";
            String mensagemTxt = String.format(msg, contato.getNome(), usuario.getNome(), usuario.getEmail());
            Mensagem mensagem = new Mensagem();
            mensagem.setPara(contato.getEmail());
            mensagem.setAssunto("Olá " + contato.getNome() + " - Você foi adicionado a Web Agenda");
            mensagem.setMensagem(mensagemTxt);
            mensageiro.enviarMensagem(mensagem, usuario);
        } catch (Exception ex) {
            throw new AuthenticationFailedException("Ocorreu um erro de altenticação");
        }
    }
}
