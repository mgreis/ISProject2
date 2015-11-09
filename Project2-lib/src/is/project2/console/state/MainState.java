/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.MusicAppException;
import is.project2.jpa.entities.Account;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Flávio
 */
public class MainState extends AbstractState {

    public MainState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert(app.user != null);
        try {
            final String cmd = app.read("> ");
            switch (cmd) {
                case "playlists": {
                    return new PlaylistsState(app);
                }
                case "music": {
                    return new MusicFilesState(app);
                }
                case "logout": {
                    return new InitialState(app);
                }
                default: {
                    app.writer.println("Main commands:");
                    app.writer.println(" playlist - manage playlists");
                    app.writer.println(" files - manage files");
                    app.writer.println(" logout - logout application");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        }
        return this; // same state
    }
    
}
