/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import javax.ejb.Remote;

/**
 * Bean interface to manage accounts.
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface AccountManagerBeanRemote {

    /**
     * Registers a new account.
     *
     * @param email Account email.
     * @param password Account password.
     * @return Account id.
     * @throws MusicAppException If an error occurs.
     */
    public Long register(String email, char[] password) throws MusicAppException;

    /**
     * Attempts to login the account.
     *
     * @param email Account email.
     * @param password Account password.
     * @return Account id.
     * @throws MusicAppException If an error occurs.
     */
    public Long login(String email, char[] password) throws MusicAppException;

    /**
     * Loads account data.
     *
     * @param id Account id.
     * @return Account data.
     * @throws MusicAppException If an error occurs.
     */
    public AccountData load(Long id) throws MusicAppException;

    /**
     * Saves the modified account data.
     *
     * @param account Account data.
     * @throws MusicAppException If an error occurs.
     */
    public void save(AccountData account) throws MusicAppException;

    /**
     * Deletes an account.
     *
     * @param id Account id.
     * @throws MusicAppException
     */
    public void delete(Long id) throws MusicAppException;

    /**
     * Does nothing. Used to make sure you can communicate with the remote bean.
     */
    public void ping();

}
