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
 * Resume a session.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/account/resume.json"},
        description = "Resume a session")
public class AccountResumeServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("No session");
        }
        return null;
    }

}
