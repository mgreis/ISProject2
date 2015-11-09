/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.AccountManagerBeanRemote;
import is.project2.ejb.MusicAppException;
import is.project2.ejb.data.AccountData;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage account.
 *
 * @author Flávio J. Saraiva
 */
public class AccountState extends AbstractState {

    final private AccountManagerBeanRemote accountManager;

    public AccountState(MusicApp app) {
        super(app);
        accountManager = null; // @todo
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        try {
            final String cmd = app.read("> ");
            switch (cmd) {
                case "view": {
                    final AccountData data = accountManager.load(app.accountId);
                    app.writer.format(" email: %s\n", data.getEmail());
                    break;// @todo
                }
                case "change-email": {
                    final String email = app.read("email: ");
                    final AccountData data = accountManager.load(app.accountId);
                    data.setEmail(email);
                    accountManager.save(data);
                    break;
                }
                case "change-password": {
                    final char[] password = app.readPassword("password: ");
                    final char[] repeatPassword = app.readPassword("repeat password: ");
                    if (Arrays.equals(password, repeatPassword)) {
                        final AccountData data = accountManager.load(app.accountId);
                        data.setPassword(password);
                        accountManager.save(data);
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
        } catch (MusicAppException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return this; // keep state
    }

}
