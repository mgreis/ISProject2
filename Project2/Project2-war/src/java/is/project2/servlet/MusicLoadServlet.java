/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.MusicData;
import java.util.ArrayList;
import java.util.Map;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Load music.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/music/load.json"},
        description = "Load music.")
public class MusicLoadServlet extends AbstractMusicAppServlet {

    private class Data {

        public Long accountId;
        public ArrayList<MusicData> music = new ArrayList<>();
    };

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("Not logged in");
            return null;
        } else {
            final Map<String, Object> parameters = (Map<String, Object>) jsonParse(request.getReader());
            final Object[] ids = (Object[]) parameters.getOrDefault("ids", new Object[0]);
            // @todo ejb
            final Data data = new Data();
            data.accountId = accountId;
            for (Object id : ids) {
                for (MusicData music : MUSIC) {
                    if (id.equals(music.getId())) {
                        data.music.add(music);
                    }
                }
            }
            return data;
        }
    }

    /**
     * Attach music to the success response.
     */
    @Override
    protected void onSuccess(JsonObjectBuilder json, Object arg) {
        final Data data = (Data) arg;
        json.add("music", jsonMusic(data.accountId, data.music.toArray(new MusicData[0])));
    }

}
