package org.ifsp.agenda.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class LogoutServlet extends HttpServlet {

     /**
     *Este servlet exclui o usuário da sessão e direciona o site para página de login.
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        /**Redifina a sessão**/
        HttpSession session = httpRequest.getSession();
        /**Remove o usuário da sessão**/
        session.removeAttribute("usuarioWeb");
        /**Define a configuração de internacionalização**/
        String lingua = (String) request.getSession().getAttribute("lingua");
        Locale locale = null;
        if (lingua.equals("en_US")) {
            locale = new Locale("en", "US");
        } else {
            locale = new Locale("pt", "BR");
        }
        ResourceBundle rb = ResourceBundle.getBundle("org.ifsp.agenda.lingua.messages", locale);        

        RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
        rd.forward(request, response);
    }
}
