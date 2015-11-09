/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.ejb.data.AccountData;
import javax.ejb.Stateless;

/**
 *
 * @author Flávio
 */
@Stateless
public class AccountManagerBean implements AccountManagerBeanRemote {

    @Override
    public Long register(String email, char[] password) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long login(String email, char[] password) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AccountData load(Long id) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
