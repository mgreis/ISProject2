/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.jpa.entities.Person;
import javax.ejb.Stateless;

/**
 * Implementation of the stateless music app.
 *
 * @author Flávio J. Saraiva
 */
@Stateless
public class MusicAppBean implements MusicAppBeanRemote {

    @Override
    public Long register(String email, char[] password) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long login(String email, char[] password) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Person load(Long id) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Person person) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
