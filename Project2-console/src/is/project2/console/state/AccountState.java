/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.AccountData;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * Manage account.
 *
 * @author Flávio J. Saraiva
 */
public class AccountState extends AbstractState {

    public AccountState(MusicApp app) throws NamingException {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        try {
            final String cmd = app.read("> ");
            switch (cmd) {
                case "view": {
                    final AccountData data = app.accountManager.load(app.accountId);
                    app.writer.format(" email: %s\n", data.getEmail());
                    break;// @todo
                }
                case "change-email": {
                    final String email = app.read("email: ");
                    final AccountData data = app.accountManager.load(app.accountId);
                    data.setEmail(email);
                    app.accountManager.save(data);
                    break;
                }
                case "change-password": {
                    final char[] password = app.readPassword("password: ");
                    final char[] repeatPassword = app.readPassword("repeat password: ");
                    if (Arrays.equals(password, repeatPassword)) {
                        final AccountData data = app.accountManager.load(app.accountId);
                        data.setPassword(password);
                        app.accountManager.save(data);
                    }
                    break;
                }
                case "back": {
                    return new MainState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Main commands:");
                    app.writer.println(" view - view account information");
                    app.writer.println(" change-email - change account email");
                    app.writer.println(" change-password - change account password");
                    app.writer.println(" back");
                    app.writer.println(" logout");
                }
            }
        } catch (IOException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        } catch (Exception ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return this; // keep state
    }

}
