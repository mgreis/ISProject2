/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpaproject2.entities.MusicFile;
import jpaproject2.entities.Playlist;
import jpaproject2.entities.PlaylistFile;
import jpaproject2.entities.Person;

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
    public void createMusicFile(Person owner, String title, String artist, String album, Date releaseYear, String filePath) {
        insertObject(new MusicFile(owner, title, artist, album, releaseYear, filePath));
    }

    /**
     * creates a playlist row in the database
     *
     * @param name
     * @param owner
     */
    public void createPlaylist(String name, Person owner) {
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
    public void createUser(String email, String password) {
        insertObject(new Person(email, password));
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
        entityManager.remove(entityManager.find(Person.class, id));
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
    public Person findUser(Long id) {
        return entityManager.find(Person.class, id);
    }

    /**
     * get musics from othe users
     *
     * @param id
     * @return
     */
    public ArrayList<MusicFile> getMusicsFromUser(Long id) {
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
    public void updateMusicFile(Long id, String album, String artist, String filePath, Date releaseYear, String title) {
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
        Person user = findUser(id);
        user.setEmail(email);
        user.setPassword(password);
        entityManager.getTransaction().commit();

    }

}
