/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Flávio
 */
@Stateless
public class AccountManagerBean implements AccountManagerBeanRemote {
    
    @EJB
    DatabaseBean database;
    
    
    /**
     * Register Account into database
     * @param email
     * @param password
     * @return
     * @throws MusicAppException 
     */
    @Override
    public Long register(String email, char[] password) throws MusicAppException {
        return database.createUser(email, String.copyValueOf(password));
    }
    
    /**
     * Login user by comparing inserted email and password to database contents
     * @param email
     * @param password
     * @return
     * @throws MusicAppException 
     */
    @Override
    public Long login(String email, char[] password) throws MusicAppException {
        AccountData aux=database.getUser(email, String.copyValueOf(password));
        if (aux!=null)return aux.getId();
        else throw new MusicAppException("Invalid email or password");
        
    }
    /**
     * Get Account data from database using the userId
     * @param id
     * @return
     * @throws MusicAppException 
     */
    @Override
    public AccountData load(Long id) throws MusicAppException {
        AccountData aux=database.getUser(id);
        if (aux!=null)return aux;
        else throw new MusicAppException("Invalid UserId");
    }

    @Override
    public void save(AccountData account) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
