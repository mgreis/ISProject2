/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import is.project2.console.MusicApp;
import is.project2.ejb.MusicAppException;

/**
 * O utilizador ainda não fez login.
 *
 * @author Flávio J. Saraiva
 */
public class InitialState extends AbstractState {

    public InitialState(MusicApp app) {
        super(app);
    }

    @Override
    public AbstractState process() {
        app.user = null;
        try {
            final String cmd = app.read("guest> ");
            switch (cmd) {
                case "login": {
                    final String email = app.read("email: ");
                    final char[] pass = app.readPassword();
                    app.user = app.remote.login(email, pass);
                    break;
                }
                case "register": {
                    final String email = app.read("email: ");
                    final char[] pass = app.readPassword();
                    app.user = app.remote.register(email, pass);
                    break;
                }
                case "exit": {
                    return null; // exit the application
                }
                default: {
                    app.writer.println("Guest commands:");
                    app.writer.println(" login - enter account");
                    app.writer.println(" register - register a new account");
                    app.writer.println(" exit - exit application");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        } catch (MusicAppException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        if (app.user != null) {
            return new MainState(app); // user is logged in
        } else {
            return this; // not logged in, same state
        }
    }

}
