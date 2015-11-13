/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.MusicData;
import static is.project2.servlet.AbstractMusicAppServlet.MUSIC;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Detach a music from the account.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/music/detach.json"},
        description = "Detach a music from the account")
public class MusicDetachServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("Not logged in");
            return null;
        } else {
            final Map<String, Object> parameters = (Map<String, Object>) jsonParse(request.getReader());
            final Long musicId = (Long) parameters.get("id");
            // @todo ejb
            for (MusicData music : MUSIC) {
                if (music.getId().equals(musicId)) {
                    if (!accountId.equals(music.getAccountId())) {
                        errors.add("Music is not attached to this account");
                        return null;
                    }
                    music.setAccountId(null);
                    return null;
                }
            }
            errors.add("Music not found");
            return null;
        }
    }

}
