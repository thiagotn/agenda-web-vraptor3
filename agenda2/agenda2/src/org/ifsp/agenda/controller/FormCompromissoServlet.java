package org.ifsp.agenda.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.ifsp.agenda.jdbc.dao.ContatoDAO;
import org.ifsp.agenda.modelo.Contato;
import org.ifsp.agenda.modelo.MensagemErro;
import org.ifsp.agenda.modelo.Usuario;

/**
 *
 * @author Ton
 */
public class FormCompromissoServlet extends HttpServlet {
    /*
     *
     * Este servlet prepara o formulário para inserção do dados do compromisso no banco de dados, ele cria uma
     * arraylist para armazenar data de hoje `30 dias, para preencher uma tag select da página de requisição,
     * pega o usuário logado na sessão, define as configuração de internacionalização para definir as chaves
     * que serão armazenadas na lista de erros, pega a lista de contatos do usuário. Se não ocorrer nenhum erro
     * os dados obtidos serão enviados a página formCompromissos. O usuário somente incluirá um compromisso se 
     * pelo menos existir um contato cadastrado.
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

        /**Cria uma lista para armazenar datas para preencher a tag select da requisição**/
        ArrayList<String> list = null;
        /**Define a data atual**/
        Date hoje = new Date();
        /**Cria uma instancia da Classe Calendar**/
        Calendar fim = Calendar.getInstance();
        /**Define a var do tipo calender com a data atual**/
        fim.setTime(hoje);
        /**Adiciona 30 dias a data atual**/
        fim.add(Calendar.DAY_OF_YEAR, 30);
        /**criar uma instancia tipo date para armazenar o final**/
        Date ate = fim.getTime();
        /**converte a data no formato dia/mes/ano**/
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        /**converte as variáveis em string**/
        String dataincial = ft.format(hoje).toString();
        String datafinal = ft.format(ate).toString();
        /**Pega o usuário logado na sessão**/
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioWeb");

        /**Cria um ContatoDAO para fazer operações no banco de dados;**/
        ContatoDAO dao = null;

        /**Define a configuração de internacionalização para armazem a mensagens do catchies**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);

        try {
            /**Define a variavél dao**/
            dao = new ContatoDAO();
        } catch (AgendaDAOException ex) {
            /**Se houver algum erro, ele armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.conexao")));
        }
        /**Cria a lista de contatos **/
        List<Contato> contatos = null;
        try {
            /**define contatos através do retorno do método getLista**/
            contatos = dao.getLista(usuario);
            if (contatos.isEmpty()) {
                /**Se a lista estiver vazia um erro é armazenado na lista de erros**/
                errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.contato.lista.vazia")));
            }
        } catch (AgendaDAOException ex) {
            /**se houver algum erro ele é armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.localizar.contato")));
        }
        /**Insere a lista de contatos na requisição**/
        request.setAttribute("contatos", contatos);

        try {
            /**define a lista de dias informando a data inicial e a final**/
            list = getListaDias(dataincial, datafinal);
        } catch (Exception ex) {
            /**Se houver algum erro ao converter as datas um erro é armazenado na lista de erros**/
            errors.add(new org.ifsp.agenda.modelo.MensagemErro("mensagem", rb.getString("erro.converter.data")));
        }
        /**insere a lista de datas e a lista de contatos na requisição**/
        request.setAttribute("datas", list);
        request.setAttribute("contatos", contatos);

        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            /**Se a lista de erros não estiver vazia, eles serão enviados para requisição**/
        }

        RequestDispatcher rd;

        if (contatos.isEmpty()) {
            /**Se a lista de contatos estiver vazia direciona para página de contatos**/
            rd = request.getRequestDispatcher("/contato/formContatos.jsp");
            /**Caso contrário direciona para página formcomprimissos**/
        } else {
            rd = request.getRequestDispatcher("/compromisso/formCompromissos.jsp");
        }
        rd.forward(request, response);
    }

    /**Este método faz um parse em strings para data**/
    private static Calendar parseCal(String dd_mm_yyyy) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date dt = df.parse(dd_mm_yyyy);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal;
    }
    /**Este método cria uma lista do tipo string com datas de período a outro**/
    /**
     *
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getListaDias(String dataInicio, String dataFim) throws Exception {
        ArrayList<String> lista = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar inicio = parseCal(dataInicio);
        Calendar fim = parseCal(dataFim);

        for (Calendar c = (Calendar) inicio.clone(); c.compareTo(fim) <= 0; c.add(Calendar.DATE, +1)) {
            /**if (c.get (Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {**/
            lista.add(df.format(c.getTime()));
            /**  }  **/
        }
        return lista;
    }
}