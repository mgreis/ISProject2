/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.jpa.entities.Account;
import java.util.Collection;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Flávio
 */
@Stateless
public class PlaylistManagerBean implements PlaylistManagerBeanRemote {

    @EJB
    DatabaseBean database;

    @Override
    public Collection<Map.Entry<Long, String>> list(Long accountId, SortOrder sort) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Long create(Long accountId, String name) throws MusicAppException {
        //get the account object with the accountId 
        AccountData user=database.getUser(accountId);
        Long id=null;
        if(user!=null){
            
            id= database.createPlaylist(name, user);
            if (id!=null) return id;
            else throw new MusicAppException("Error creating playlist.");
        }
        else throw new MusicAppException("Invalid session.");
    }

    @Override
    public PlaylistData load(Long accountId, Long playlistId) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(Long accountId, PlaylistData playlist) throws MusicAppException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long accountId, Long playlistId) throws MusicAppException {
       if(database.getUser(accountId)!=null)database.deletePlaylist(playlistId);
       else throw new MusicAppException("Invalid session");
    }

}
