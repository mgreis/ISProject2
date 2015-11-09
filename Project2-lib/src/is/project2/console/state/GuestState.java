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
import is.project2.ejb.Beans;
import is.project2.ejb.MusicAppException;
import java.util.Arrays;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * O utilizador ainda não fez login.
 *
 * @author Flávio J. Saraiva
 */
public class GuestState extends AbstractState {

    public final AccountManagerBeanRemote accountManager;

    public GuestState(MusicApp app) throws NamingException {
        super(app);
        accountManager = InitialContext.doLookup(Beans.ACCOUNT_MANAGER_BEAN);
    }

    @Override
    public AbstractState process() {
        app.accountId = null;
        try {
            final String cmd = app.read("guest> ");
            switch (cmd) {
                case "login": {
                    final String email = app.read("email: ");
                    final char[] pass = app.readPassword("password: ");
                    app.accountId = accountManager.login(email, pass);
                    break;
                }
                case "register": {
                    final String email = app.read("email: ");
                    final char[] password = app.readPassword("password: ");
                    final char[] repeatPassword = app.readPassword("repeat password: ");
                    if (Arrays.equals(password, repeatPassword)) {
                        app.accountId = accountManager.register(email, password);
                    }
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
        if (app.accountId != null) {
            return new MainState(app); // user is logged in
        } else {
            return this; // not logged in, keep state
        }
    }

}
