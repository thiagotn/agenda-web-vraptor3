package org.ifsp.agenda.controller;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ton
 */
@SuppressWarnings("serial")
public class FormContatoServlet extends HttpServlet {
    /*
     *
     * Este servlet prepara o formulário para inserção do dados do contato, ele apenas chama o formulário
     * para inserção de dados, uma tag select será exibida na página para que ele escolha o tipo de contato.
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
        RequestDispatcher rd = request.getRequestDispatcher("/contato/formContatos.jsp");
        rd.forward(request, response);
    }
}
