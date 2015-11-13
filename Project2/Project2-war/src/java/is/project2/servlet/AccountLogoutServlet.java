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
 * Perform logout.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/account/logout.json"},
        description = "Perform logout")
public class AccountLogoutServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        request.getSession().removeAttribute("accountId");
        return null;
    }

}
