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
public class FormUsuarioServlet extends HttpServlet {
    /*
     *
     * Este servlet prepara o formulário para inserção do dados do usuário, ele apenas chama o formulário
     * para inserção de dados.
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
        RequestDispatcher rd = request.getRequestDispatcher("/usuario/formUsuarios.jsp");
        rd.forward(request, response);
    }
}
