/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.jpa.api.Database;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import is.project2.jpa.entities.MusicFile;
import is.project2.jpa.entities.Playlist;
import is.project2.jpa.entities.PlaylistFile;
import is.project2.jpa.entities.Account;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

/**
 *
 * @author mgreis
 */


@Singleton
@LocalBean
public class DatabaseBean{

   
    //private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JPAPROJECT2PU");
    //private EntityManager entityManager = entityManagerFactory.createEntityManager();
    @PersistenceContext(name="JPAPROJECT2PU")
    EntityManager entityManager; 
    


    //@Override
    //public void close() {
        //entityManager.close();
        //entityManagerFactory.close();
    //}

    /**
     * creates a musicFile row in the database
     *
     * @param owner
     * @param title
     * @param artist
     * @param album
     * @param releaseYear
     * @param filePath
     * @param fileData
     * @return
     */
    public Long createMusicFile(Account owner, String title, String artist, String album, int releaseYear, String filePath, byte[] fileData) {
        MusicFile aux = new MusicFile(owner, title, artist, album, releaseYear, filePath, fileData);
        insertObject(aux);
        return aux.getId();
    }

    /**
     * creates a playlist row in the database
     *
     * @param name
     * @param owner
     * @return
     */
    public Long createPlaylist(String name, AccountData owner) {
        Playlist aux = new Playlist(name, this.getAccount(owner.getId()));
        insertObject(aux);
        return aux.getId();
    }

    /**
     * creates a plalistFile row in the database
     *
     * @param playlist
     * @param musicFile
     */
    public Long createPlaylistFile(Playlist playlist, MusicFile musicFile) {
        // @todo index
        PlaylistFile aux = new PlaylistFile(playlist, musicFile);
        insertObject(aux);
        return aux.getId();
    }

    /**
     * Creates an account row in the database
     *
     * @param email
     * @param password
     * @return
     */
    public Long createUser(String email, String password) {
        Account account = new Account(email, password);
        insertObject(account);
        return account.getId();
    }

    public void detatchUserFromMusicFile(Long id) {
        entityManager.getTransaction().begin();
        final MusicFile musicFile = getMusicFile(id);
        musicFile.setOwner(null);
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a MusicFile object with a certain primary key
     *
     * @param id
     */
    public void deleteMusicFile(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(MusicFile.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a playlist object with a certain primary key and all its
     * PlaylistFile dependencies
     *
     * @param id
     */
    public void deletePlaylist(Long id) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PlaylistFile> query = builder.createQuery(PlaylistFile.class);
        // build query
        final Root<PlaylistFile> musics = query.from(PlaylistFile.class);
        query.where(builder.equal(musics.get("playlist_id"), id)).orderBy(builder.asc(musics.get("id")));

        //executes the query to get the musics primary key
        final List<PlaylistFile> auxList = entityManager.createQuery(query).getResultList();

        //start deleting from the PlayListfile table
        //entityManager.getTransaction().begin();
        for (PlaylistFile auxVar : auxList) {
            entityManager.remove(auxVar);

        }
        entityManager.remove(entityManager.find(Playlist.class, id));
        //entityManager.getTransaction().commit();
    }

    /**
     * deletes a playlistFile object with a certain composite primary key
     *
     * @param id
     */
    public void deletePlaylistFile(Long id) {
        //entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(PlaylistFile.class, id));
        //entityManager.getTransaction().commit();
    }

    /**
     * deletes a user object with a certain composite primary key
     *
     * @param id
     */
    public void deleteUser(Long id) {
        //entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Account.class, id));
        //entityManager.getTransaction().commit();
    }

    /**
     * private method to insert any object into a certain table
     *
     * @param object
     */
    private void insertObject(Object object) {
        //entityManager.getTransaction().begin();
        entityManager.persist(object);
        //entityManager.getTransaction().commit();
    }

    /**
     * returns a musicEntity with a certain pk
     *
     * @param id
     * @return
     */
    public MusicFile getMusicFile(Long id) {
        return entityManager.find(MusicFile.class, id);
    }

    /**
     * get he musicFile and turn it into MusicData
     *
     * @param id
     * @return
     */
    public MusicData getMusicData(Long id) {
        return this.musicFileToMusicData(entityManager.find(MusicFile.class, id));
    }

    /**
     * returns a PlaylistObject with a certain pk
     *
     * @param id
     * @return
     */
    public Playlist getPlaylist(Long id) {
        return entityManager.find(Playlist.class, id);
    }

    public Map.Entry<Long, String>[] getPlaylistsFromUser(Long accountId, SortOrder sort) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Playlist> query = builder.createQuery(Playlist.class);
        Account user = this.getUserAccount(accountId);

        // build query
        final Root<Playlist> playlist = query.from(Playlist.class);
        query.where(builder.equal(playlist.get("owner"), user));
        //executes the query to get the musics primary key
        final List<Playlist> auxList = entityManager.createQuery(query).getResultList();
        MapEntry[] output = new MapEntry[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (Playlist auxVar : auxList) {

            output[cont] = new MapEntry(auxVar.getId(), auxVar.getName());
            cont++;

        }
        //return the array
        return output;
    }

    /**
     * returns a playListData object with a certain pk
     *
     * @param id
     * @return
     */
    public PlaylistData getPlaylistData(Long id) {
        Playlist aux = entityManager.find(Playlist.class, id);
        PlaylistData data = new PlaylistData(id);
        data.setName(aux.getName());
        MusicData[] musics = this.getMusicsFromPlaylist(id);
        for (MusicData music : musics) {
            data.getMusic().add(music.getId());
        }
        return data;
    }

    /**
     * returns a playListFile with a certain pk
     *
     * @param id
     * @return
     */
    public PlaylistFile getPlaylistFile(Long id) {
        return entityManager.find(PlaylistFile.class, id);
    }

    /**
     * returns a userEntity with a certain pk
     *
     * @param id
     * @return
     */
    public Account getUserAccount(Long id) {
        return entityManager.find(Account.class, id);
    }

    /**
     * get musics from othe users
     *
     * @param id
     * @return
     */
    public MusicData[] getMusicsFromUser(Long accountId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);
        Account user = this.getUserAccount(accountId);
        // build query
        final Root<MusicFile> musics = query.from(MusicFile.class);
        query.where(builder.equal(musics.get("owner"), user));
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {

            output[cont] = this.musicFileToMusicData(auxVar);
            cont++;
        }

        //return the array
        return output;
    }

    public MusicData[] getMusicsByArtist(String artist) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        final Root<MusicFile> musics = query.from(MusicFile.class);
        query.where(builder.like(musics.get("artist"), artist));
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {

            output[cont] = this.musicFileToMusicData(auxVar);
            cont++;

        }
        //return the arrayList
        return output;
    }

    public MusicData[] getMusicsByArtistAndTitle(String artist, String title) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        final Root <MusicFile> musics = query.from(MusicFile.class);
        Predicate p1 = builder.like(musics.get("artist"), artist);
        Predicate p2 = builder.like(musics.get("title"), title);
        Predicate p3 = builder.and(p1,p2);
        
        query.where(p3);
        
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {

            output[cont] = this.musicFileToMusicData(auxVar);
            cont++;

        }
        //return the arrayList
        return output;
    }

    public MusicData[] getMusicsByTitle(String title) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        final Root<MusicFile> musics = query.from(MusicFile.class);
        query.where(builder.like(musics.get("title"), title));
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {

            output[cont] = this.musicFileToMusicData(auxVar);
            cont++;

        }
        //return the arrayList
        return output;
    }

    /**
     * get all musics from a playlist with a given pk
     *
     * @param id
     * @return
     */
    public MusicData[] getMusicsFromPlaylist(Long id) {
        
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PlaylistFile> query = builder.createQuery(PlaylistFile.class);

        // build query
        final Root<PlaylistFile> musics = query.from(PlaylistFile.class);
        query.where(builder.equal(musics.get("playlist_id"), id)).orderBy(builder.asc(musics.get("id")));

        //executes the query to get the musics primary key
        final List<PlaylistFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (PlaylistFile auxVar : auxList) {
            output[cont] = this.musicFileToMusicData(auxVar.getMusicFile());
            cont++;
        }
        //return the arrayList
        return output;
    }

    /**
     * get musics from user
     *
     * @param id
     * @return
     */
    public MusicData[] getMusicsFromOtherUsers(Long accountId) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);
        Account user = this.getUserAccount(accountId);

        // build query
        final Root <MusicFile> musics = query.from(MusicFile.class);
        query.where(builder.notEqual(musics.get("owner"), user));
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            
                output[cont] = this.musicFileToMusicData(auxVar);
                cont++;
            
        }
        //return the array
        return output;
    }

    /**
     * Get the account data by its email and password
     *
     * @param email
     * @param password
     * @return
     */
    public AccountData getUser(String email, String password) {
        AccountData user = null;
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Account> query = builder.createQuery(Account.class);
        final Root <Account> account = query.from(Account.class);
        Predicate p1 = builder.like(account.get("email"), email);
        Predicate p2 = builder.like(account.get("password"), password);
        Predicate p3 = builder.and(p1,p2);
        
        query.where(p3);
        final List<Account> auxList = entityManager.createQuery(query).getResultList();
        for (Account auxVar : auxList) {
            if (auxVar.getEmail().matches(email) && auxVar.getPassword().matches(password)) {
                user = new AccountData(auxVar.getId());
                user.setEmail(auxVar.getEmail());
                user.setPassword(auxVar.getPassword().toCharArray());

            }
        }
        return user;
    }

    /**
     * Get the account data by its userId
     *
     * @param id
     * @return
     */
    public AccountData getUser(Long id) {
        AccountData user = null;
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Account> query = builder.createQuery(Account.class);
        final Root <Account> account = query.from(Account.class);
        query.where(builder.equal(account.get("id"), id));

        final List<Account> auxList = entityManager.createQuery(query).getResultList();
        for (Account auxVar : auxList) {
            
                user = new AccountData(auxVar.getId());
                user.setEmail(auxVar.getEmail());
                user.setPassword(auxVar.getPassword().toCharArray());

            
        }
        return user;
    }

    public Account getAccount(Long id) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Account> query = builder.createQuery(Account.class);
        final Root <Account> account = query.from(Account.class);
        query.where(builder.equal(account.get("id"), id));

        final List<Account> auxList = entityManager.createQuery(query).getResultList();
        for (Account auxVar : auxList) {
            
                return auxVar;

            
        }
        return null;
    }

    /**
     * Inserts a music list into a playlist
     *
     * @param musicList
     * @param playlist
     */
    public void InsertMusicListToPlaylist(ArrayList<MusicFile> musicList, Playlist playlist) {
        for (MusicFile music : musicList) {
            this.createPlaylistFile(playlist, music);
        }
    }

    public void updatePlaylist(Long accountId, PlaylistData playlist) {

        if (this.getUser(accountId) != null) {
            entityManager.getTransaction().begin();
            Playlist list = this.getPlaylist(playlist.getId());
            list.setName(playlist.getName());
            entityManager.getTransaction().commit();
        }
    }

    /**
     * updates a musicFile
     *
     * @param id
     * @param name
     */
    public void updateMusicFile(Long id, String album, String artist, String filePath, int releaseYear, String title) {
        entityManager.getTransaction().begin();
        final MusicFile musicFile = getMusicFile(id);
        musicFile.setAlbum(album);
        musicFile.setArtist(artist);
        musicFile.setFilePath(filePath);
        musicFile.setReleaseYear(releaseYear);
        musicFile.setTitle(title);
        entityManager.getTransaction().commit();
    }

    /**
     * updates a userEntity
     *
     * @param id
     * @param email
     * @param password
     */
    public void updateUser(Long id, String email, String password) {
        entityManager.getTransaction().begin();
        Account user = getUserAccount(id);
        user.setEmail(email);
        user.setPassword(password);
        entityManager.getTransaction().commit();

    }

    private MusicData musicFileToMusicData(MusicFile source) {

        try {
            MusicData target = new MusicData(source.getId(), new URI(source.getFilePath()));
            target.setAlbum(source.getAlbum());
            target.setArtist(source.getArtist());
            target.setReleaseYear(source.getReleaseYear());
            target.setTitle(source.getTitle());
            target.setAccountId(source.getOwner().getId());
            return target;
        } catch (URISyntaxException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

}
