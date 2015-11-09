/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.jpa.entities.Account;
import javax.ejb.Remote;

/**
 * Interface to the remote stateless app.
 *
 * @todo sending the password raw is not secure!!!
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface MusicAppBeanRemote {

    /**
     * Registers a new user.
     *
     * @param email User email.
     * @param password User password.
     * @return User id.
     * @throws MusicAppException If an error occurs.
     */
    public Long register(String email, char[] password) throws MusicAppException;

    /**
     * Attempts to login the user.
     *
     * @todo password hash?
     *
     * @param email User email.
     * @param password User password.
     * @return User id.
     * @throws MusicAppException If an error occurs.
     */
    public Long login(String email, char[] password) throws MusicAppException;

    /**
     * Loads user data.
     *
     * @param id User id.
     * @return User data.
     * @throws MusicAppException If an error occurs.
     */
    public Account load(Long id) throws MusicAppException;

    /**
     * Saves user data.
     *
     * @param person User data.
     * @throws MusicAppException If an error occurs.
     */
    public void save(Account person) throws MusicAppException;

    /**
     * Deletes a user.
     *
     * @param id User id.
     * @throws MusicAppException
     */
    public void delete(Long id) throws MusicAppException;

}
