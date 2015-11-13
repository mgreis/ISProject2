/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Get list of playlists.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/playlist/list.json"},
        description = "Get list of playlists")
public class PlaylistListServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final HashMap<Long, String> playlists = new HashMap<>();
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("Not logged in");
        } else {
            // @todo
            playlists.put((long) 1, "Playlist1");
            playlists.put((long) 2, "Playlist2");
        }
        return playlists;
    }

    /**
     * Attach playlists to the success response.
     */
    @Override
    protected void onSuccess(JsonObjectBuilder json, Object data) {
        final HashMap<Long, String> playlists = (HashMap<Long, String>) data;
        final JsonArrayBuilder array = jsonArray();
        for (Map.Entry<Long, String> playlist : playlists.entrySet()) {
            array.add(jsonObject()
                    .add("id", playlist.getKey())
                    .add("name", playlist.getValue()));
        }
        json.add("playlists", array);
    }

}
