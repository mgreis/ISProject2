/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Operations on playlists.
 *
 * @author Flávio J. Saraiva
 */
public class PlaylistsState extends AbstractState {

    public PlaylistsState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert(app.userId != null);
        try {
            final String cmd = app.read("playlists> ");
            switch (cmd) {
                case "list-asc": {
                    // @todo
                    break;
                }
                case "list-desc": {
                    // @todo
                    break;
                }
                case "create": {
                    // @todo
                    break;
                }
                case "open": {
                    // @todo
                    break;
                }
                case "back": {
                    return new MainState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Playlists commands:");
                    app.writer.println(" list-asc - list all playlists in ascending order");
                    app.writer.println(" list-desc - list all playlists in descending order");
                    app.writer.println(" create - create a new playlist");
                    app.writer.println(" open - open a playlist");
                    app.writer.println(" back");
                    app.writer.println(" logout");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        }
        return this; // same state
    }

}
