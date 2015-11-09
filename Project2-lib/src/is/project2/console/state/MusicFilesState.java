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
 * File operations.
 *
 * @author Flávio J. Saraiva
 */
public class MusicFilesState extends AbstractState {

    public MusicFilesState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert(app.userId != null);
        try {
            final String cmd = app.read("music> ");
            switch (cmd) {
                case "back": {
                    return new MainState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Music commands:");
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
