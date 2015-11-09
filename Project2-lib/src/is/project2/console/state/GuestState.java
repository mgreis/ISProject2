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
import is.project2.ejb.AccountManagerBeanRemote;
import is.project2.ejb.MusicAppException;

/**
 * O utilizador ainda não fez login.
 *
 * @author Flávio J. Saraiva
 */
public class GuestState extends AbstractState {

    public final AccountManagerBeanRemote accountManager;

    public GuestState(MusicApp app) {
        super(app);
        accountManager = null; // @todo lookup
    }

    @Override
    public AbstractState process() {
        app.userId = null;
        try {
            final String cmd = app.read("guest> ");
            switch (cmd) {
                case "login": {
                    final String email = app.read("email: ");
                    final char[] pass = app.readPassword();
                    app.userId = accountManager.login(email, pass);
                    break;
                }
                case "register": {
                    final String email = app.read("email: ");
                    final char[] pass = app.readPassword();
                    app.userId = accountManager.register(email, pass);
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
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        } catch (MusicAppException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        if (app.userId != null) {
            return new MainState(app); // user is logged in
        } else {
            return this; // not logged in, keep state
        }
    }

}
