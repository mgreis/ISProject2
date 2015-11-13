/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Register a new account and perform login.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/account/register.json"},
        description = "Register a new account and perform login")
public class AccountRegisterServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final String email = request.getParameter("email");
        final String password = request.getParameter("password");

        // @todo ejb
        final Long accountId = (long) 1;
        request.getSession().setAttribute("accountId", accountId);
        return null;
    }

}
