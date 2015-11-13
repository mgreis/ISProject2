/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.jpa.api;

import is.project2.ejb.MusicData;
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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mário
 */
public class Database implements AutoCloseable {

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    public Database() {
        entityManagerFactory = Persistence.createEntityManagerFactory("JPAPROJECT2PU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Override
    public void close() {
        entityManager.close();
        entityManagerFactory.close();
    }

    /**
     * creates a musicFile row in the database
     *
     * @param owner
     * @param title
     * @param artist
     * @param album
     * @param releaseYear
     * @param filePath
     */
    public void createMusicFile(Account owner, String title, String artist, String album, int releaseYear, String filePath, byte[] fileData) {
        insertObject(new MusicFile(owner, title, artist, album, releaseYear, filePath, fileData));
    }

    /**
     * creates a playlist row in the database
     *
     * @param name
     * @param owner
     */
    public void createPlaylist(String name, Account owner) {
        insertObject(new Playlist(name, owner));
    }

    /**
     * creates a plalistFile row in the database
     *
     * @param playlist
     * @param musicFile
     */
    public void createPlaylistFile(Playlist playlist, MusicFile musicFile) {
        // @todo index
        insertObject(new PlaylistFile(playlist, musicFile));
    }

    /**
     * Creates an account row in the database
     *
     * @param email
     * @param password
     */
    public Long createUser(String email, String password) {
        Account account = new Account(email, password);
        insertObject(account);
        return account.getId();
    }

    public void detatchUserFromMusicFile(Long id) {
        entityManager.getTransaction().begin();
        final MusicFile musicFile = findMusicFile(id);
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
     * deletes a playlist object with a certain primary key
     *
     * @param id
     */
    public void deletePlaylist(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Playlist.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a playlistFile object with a certain composite primary key
     *
     * @param id
     */
    public void deletePlaylistFile(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(PlaylistFile.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a user object with a certain composite primary key
     *
     * @param id
     */
    public void deleteUser(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Account.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * private method to insert any object into a certain table
     *
     * @param object
     */
    private void insertObject(Object object) {
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.getTransaction().commit();
    }

    /**
     * returns a musicEntity with a certain pk
     *
     * @param id
     * @return
     */
    public MusicFile findMusicFile(Long id) {
        return entityManager.find(MusicFile.class, id);
    }

    /**
     * returns a playListEntity with a certain pk
     *
     * @param id
     * @return
     */
    public Playlist findPlaylist(Long id) {
        return entityManager.find(Playlist.class, id);
    }

    /**
     * returns a playListFile with a certain pk
     *
     * @param id
     * @return
     */
    public PlaylistFile findPlaylistFile(Long id) {
        return entityManager.find(PlaylistFile.class, id);
    }

    /**
     * returns a userEntity with a certain pk
     *
     * @param id
     * @return
     */
    public Account findUser(Long id) {
        return entityManager.find(Account.class, id);
    }

    /**
     * get musics from other users
     *
     * @param id
     * @return
     */
    public MusicData[] getMusicsFromUser(Long id) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        //final Root <MusicFileEntity> musics = query.from(MusicFileEntity.class);
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();
        final MusicData[] output = new MusicData[auxList.size()];
        int cont = 0;
        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            if (auxVar.getOwner() != null && Objects.equals(id, auxVar.getOwner().getId())) {
                output[cont] = this.musicFileToMusicData(auxVar);
                cont++;
            }
        }
        //return the array
        return output;
    }

    public ArrayList<MusicFile> getMusicsByArtist(String artist) {
        final ArrayList<MusicFile> musicList = new ArrayList<MusicFile>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        //final Root <MusicFileEntity> musics = query.from(MusicFileEntity.class);
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();

        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            if (auxVar.getArtist().contains(artist)) {
                musicList.add(auxVar);
            }
        }
        //return the arrayList
        return musicList;
    }

    public ArrayList<MusicFile> getMusicsByArtistAndTitle(String artist, String title) {
        ArrayList<MusicFile> musicList = new ArrayList<MusicFile>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        //final Root <MusicFileEntity> musics = query.from(MusicFileEntity.class);
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();

        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            if (auxVar.getArtist().contains(artist) && auxVar.getTitle().contains(title)) {
                musicList.add(auxVar);
            }
        }
        //return the arrayList
        return musicList;
    }

    public ArrayList<MusicFile> getMusicsByTitle(String title) {
        final ArrayList<MusicFile> musicList = new ArrayList<MusicFile>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        //final Root <MusicFileEntity> musics = query.from(MusicFileEntity.class);
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();

        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            if (auxVar.getTitle().equalsIgnoreCase(title)) {
                musicList.add(auxVar);
            }
        }
        //return the arrayList
        return musicList;
    }

    /**
     * get all musics from a playlist with a given pk
     *
     * @param id
     * @return
     */
    public ArrayList<MusicFile> getMusicsFromPlaylist(Long id) {
        final ArrayList<MusicFile> musicList = new ArrayList<MusicFile>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PlaylistFile> query = builder.createQuery(PlaylistFile.class);

        // build query
        final Root<PlaylistFile> musics = query.from(PlaylistFile.class);
        query.where(builder.equal(musics.get("playlist_id"), id)).orderBy(builder.asc(musics.get("id")));

        //executes the query to get the musics primary key
        final List<PlaylistFile> auxList = entityManager.createQuery(query).getResultList();

        //put query result into an arraylist
        for (PlaylistFile auxVar : auxList) {
            musicList.add(auxVar.getMusicFile());

        }
        //return the arrayList
        return musicList;
    }

    /**
     * get musics from user
     *
     * @param id
     * @return
     */
    public ArrayList<MusicFile> getMusicsFromOtherUsers(Long id) {
        final ArrayList<MusicFile> musicList = new ArrayList<MusicFile>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<MusicFile> query = builder.createQuery(MusicFile.class);

        // build query
        //final Root <MusicFileEntity> musics = query.from(MusicFileEntity.class);
        //executes the query to get the musics primary key
        final List<MusicFile> auxList = entityManager.createQuery(query).getResultList();

        //put query result into an arraylist
        for (MusicFile auxVar : auxList) {
            if (auxVar.getOwner() != null && id == auxVar.getOwner().getId()) {
                musicList.add(auxVar);
            }
        }
        //return the arrayList
        return musicList;
    }

    public Long getUser(String email, String password) {
        Long userId = null;
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Account> query = builder.createQuery(Account.class);

        final List<Account> auxList = entityManager.createQuery(query).getResultList();
        for (Account auxVar : auxList) {
            if (auxVar.getEmail().matches(email) && auxVar.getPassword().matches(password)) {
                userId = auxVar.getId();
            }
        }
        return userId;
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

    public void updatePlaylist(Long id, String name) {
        entityManager.getTransaction().begin();
        Playlist playlist = findPlaylist(id);
        playlist.setName(name);
        entityManager.getTransaction().commit();
    }

    /**
     * updates a musicFile
     *
     * @param id
     * @param name
     */
    public void updateMusicFile(Long id, String album, String artist, String filePath, int releaseYear, String title) {
        entityManager.getTransaction().begin();
        final MusicFile musicFile = findMusicFile(id);
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
        Account user = findUser(id);
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
