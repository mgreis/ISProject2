/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.PlaylistData;
import java.util.ArrayList;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Load playlists.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/playlist/load.json"},
        description = "Load playlists")
public class PlaylistLoadServlet extends AbstractMusicAppServlet {

    private class Data {

        public ArrayList<PlaylistData> playlists = new ArrayList<>();
    };

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("Not logged in");
            return null;
        } else {
            // @todo ejb
            final Data data = new Data();
            {
                final PlaylistData playlist1 = new PlaylistData((long) 1);
                playlist1.setName("Playlist1");
                playlist1.getMusic().add((long) 1);
                playlist1.getMusic().add((long) 2);
                data.playlists.add(playlist1);
            }
            {
                final PlaylistData playlist2 = new PlaylistData((long) 2);
                playlist2.setName("Playlist2");
                playlist2.getMusic().add((long) 2);
                playlist2.getMusic().add((long) 3);
                data.playlists.add(playlist2);
            }
            return data;
        }
    }

    /**
     * Attach playlists to the success response.
     */
    @Override
    protected void onSuccess(JsonObjectBuilder json, Object arg) {
        final Data data = (Data) arg;

        final JsonArrayBuilder playlists = jsonArray();
        for (PlaylistData playlist : data.playlists) {
            playlists.add(jsonObject()
                    .add("id", playlist.getId())
                    .add("name", playlist.getName())
                    .add("music", jsonArray(playlist.getMusic().toArray()))
            );
        }
        json.add("playlists", playlists);
    }

}
