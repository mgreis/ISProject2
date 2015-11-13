/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.MusicData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Find music.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/music/find.json"},
        description = "Find music")
public class MusicFindServlet extends AbstractMusicAppServlet {

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
            final String query = (String) parameters.getOrDefault("query", "");
            // @todo ejb
            final Data data = new Data();
            data.accountId = accountId;
            for (MusicData music : MUSIC) {
                if (query.isEmpty()
                        || (music.getTitle() != null && music.getTitle().toLowerCase().contains(query.toLowerCase()))
                        || (music.getArtist() != null && music.getArtist().toLowerCase().contains(query.toLowerCase()))
                        || (music.getAlbum() != null && music.getAlbum().toLowerCase().contains(query.toLowerCase()))
                        || (music.getReleaseYear() != null && new SimpleDateFormat("yyyy-mm-dd").format(music.getReleaseYear()).contains(query))) {
                    data.music.add(music); // found match
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
