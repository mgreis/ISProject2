/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.SortOrder;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operations on playlists.
 *
 * @author Flávio J. Saraiva
 */
public class PlaylistListState extends AbstractState {

    public PlaylistListState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        try {
            final String cmd = app.read("playlists> ");
            switch (cmd) {
                case "list-asc": {
                    for (Map.Entry<Long, String> playlist : app.playlistManager.list(app.accountId, SortOrder.ASCENDING)) {
                        app.writer.format(" %d - %s\n", playlist.getKey(), playlist.getValue());
                    }
                    break;
                }
                case "list-desc": {
                    for (Map.Entry<Long, String> playlist : app.playlistManager.list(app.accountId, SortOrder.DESCENDING)) {
                        app.writer.format(" %d - %s\n", playlist.getKey(), playlist.getValue());
                    }
                    break;
                }
                case "create": {
                    final String name = app.read("name: ");
                    app.playlistId = app.playlistManager.create(app.accountId, name);
                    return new PlaylistState(app);
                }
                case "open": {
                    final String id = app.read("id: ");
                    app.playlistId = Long.parseLong(id);
                    return new PlaylistState(app);
                }
                case "back": {
                    return new MainState(app);
                }
                case "account": {
                    return new AccountState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Playlists commands:");
                    app.writer.println(" list-asc - list playlists in ascending order");
                    app.writer.println(" list-desc - list playlists in descending order");
                    app.writer.println(" create - create a new playlist");
                    app.writer.println(" open - open a playlist");
                    app.writer.println(" back");
                    app.writer.println(" account");
                    app.writer.println(" logout");
                }
            }
        } catch (IOException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        } catch (Exception ex) {
            app.writer.println(ex);
            Logger.getLogger(PlaylistListState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this; // keep state
    }

}
