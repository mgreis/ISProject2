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
 *
 * @author Flávio J. Saraiva
 */
public class MainState extends AbstractState {

    public MainState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        try {
            final String cmd = app.read("> ");
            switch (cmd) {
                case "account": {
                    return new AccountState(app);
                }
                case "playlists": {
                    return new PlaylistsState(app);
                }
                case "files": {
                    return new MusicFilesState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Main commands:");
                    app.writer.println(" account - manage account");
                    app.writer.println(" playlists - manage playlists");
                    app.writer.println(" files - manage music files");
                    app.writer.println(" logout - logout application");
                }
            }
        } catch (IOException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        }
        return this; // keep state
    }

}
