/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;


import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Flávio,mgreis
 */
@Stateless
public class PlaylistManagerBean implements PlaylistManagerBeanRemote {

    @EJB
    DatabaseBean database;

    @Override
    public Map.Entry<Long, String> [] list(Long accountId, SortOrder sort) throws MusicAppException {
        if(database.getAccount(accountId)!=null){
            return database.getPlaylistsFromUser(accountId, sort);
            
        }
        else throw new MusicAppException("Invalid session");
    }

    @Override
    public Long create(Long accountId, String name) throws MusicAppException {
        //get the account object with the accountId 
        AccountData user = database.getUser(accountId);
        Long id;
        
        //if user exists;
        if (user != null) {

            id = database.createPlaylist(name, user);
            
            //if playlist was successfully created
            if (id != null) {
                return id;
                
            } else {
                throw new MusicAppException("Error creating playlist.");
            }
        } else {
            throw new MusicAppException("Invalid session.");
        }
    }

    @Override
    public PlaylistData load(Long accountId, Long playlistId) throws MusicAppException {
        AccountData user = database.getUser(accountId);

        if (user != null) {

           return database.getPlaylistData(playlistId);
           
        } else {
            throw new MusicAppException("Invalid session.");
        }
    }

    @Override
    public void save(Long accountId, PlaylistData playlist) throws MusicAppException {
        database.updatePlaylist(accountId, playlist);
    }
    
    @Override
    public void delete(Long accountId, Long playlistId) throws MusicAppException {
        //if user exists in database
        if (database.getUser(accountId) != null) {
            
            //delete the playlist and all its associated playlistFile entries
            database.deletePlaylist(playlistId);
            
        } else {
            throw new MusicAppException("Invalid session");
        }
    }

    @Override
    public void ping() {
    }

}
