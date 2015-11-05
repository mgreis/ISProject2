/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpaproject2.api;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import jpaproject2.entities.MusicFileEntity;
import jpaproject2.entities.PlaylistEntity;
import jpaproject2.entities.PlaylistFileEntity;
import jpaproject2.entities.PlaylistFilePK;
import jpaproject2.entities.UserEntity;

/**
 *
 * @author Mário
 */
public class DatabaseEngine {

    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;

    public DatabaseEngine() {
        entityManagerFactory = Persistence.createEntityManagerFactory("JPAPROJECT2PU");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Closes the engine
     */
    public void EngineClose() {
        entityManager.close();
        entityManagerFactory.close();
    }

    /**
     * creates a musicFile row in the database
     *
     * @param user
     * @param title
     * @param artist
     * @param album
     * @param releaseYear
     * @param filePath
     */
    public void createMusicFile(UserEntity user, String title, String artist, String album, int releaseYear, String filePath) {

        this.insertObject(new MusicFileEntity(user, title, artist, album, releaseYear, filePath));

    }

    /**
     * creates a playlist row in the database
     *
     * @param name
     * @param user
     */
    public void createPlaylist(String name, UserEntity user) {

        this.insertObject(new PlaylistEntity(name, user));

    }

    /**
     * creates a plalistFile row in the database
     *
     * @param playlist
     * @param musicFile
     */
    public void createPlaylistFile(PlaylistEntity playlist, MusicFileEntity musicFile) {
        this.insertObject(new PlaylistFileEntity(playlist, musicFile));
    }

    /**
     * Creates an account row in the database
     *
     * @param email
     * @param password
     */
    public void createUser(String email, String password) {

        this.insertObject(new UserEntity(email, password));

    }

    /**
     * deletes a MusicFile object with a certain primary key
     *
     * @param id
     */
    public void deleteMusicFile(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(MusicFileEntity.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a playlist object with a certain primary key
     *
     * @param id
     */
    public void deletePlaylist(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(PlaylistEntity.class, id));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a playlistFile object with a certain composite primary key
     *
     * @param id
     */
    public void deletePlaylistFile(PlaylistEntity playlist, MusicFileEntity musicFile, Long id) {
        entityManager.getTransaction().begin();
        PlaylistFilePK aux = new PlaylistFilePK(playlist, musicFile, id);
        entityManager.remove(entityManager.find(PlaylistFileEntity.class, aux));
        entityManager.getTransaction().commit();
    }

    /**
     * deletes a user object with a certain composite primary key
     *
     * @param id
     */
    public void deleteUser(Long id) {
        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(UserEntity.class, id));
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
    public MusicFileEntity findMusicFile(Long id) {
        return entityManager.find(MusicFileEntity.class, id);
    }

    /**
     * returns a playListEntity with a certain pk
     *
     * @param id
     * @return
     */
    public PlaylistEntity findPlaylist(Long id) {
        return entityManager.find(PlaylistEntity.class, id);
    }

    /**
     * returns a playListFile with a certain pk
     *
     * @param playlist
     * @param musicFile
     * @param id
     * @return
     */
    public PlaylistFileEntity findPlaylistFile(PlaylistEntity playlist, MusicFileEntity musicFile, Long id) {
        PlaylistFilePK aux = new PlaylistFilePK(playlist, musicFile, id);
        return entityManager.find(PlaylistFileEntity.class, aux);
    }

    /**
     * returns a userEntity with a certain pk
     *
     * @param id
     * @return
     */
    public UserEntity findUser(Long id) {
        return entityManager.find(UserEntity.class, id);
    }

    /**
     * get all musics from a playlist with a given pk
     * @param id
     * @return 
     */
    public ArrayList<MusicFileEntity> getMusicsFromPlaylist(Long id) {
        ArrayList <MusicFileEntity> musicList= new ArrayList<MusicFileEntity>();
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<PlaylistFileEntity> query = builder.createQuery(PlaylistFileEntity.class);

        // build query
        final Root<PlaylistFileEntity> musics = query.from(PlaylistFileEntity.class);
        query.where(builder.equal(musics.get("playlist_id"), id)).orderBy(builder.asc(musics.get("id")));

        //executes the query to get the musics primary key
        List<PlaylistFileEntity> auxList = entityManager.createQuery(query).getResultList();
        
        //put query result into an arraylist
        for (PlaylistFileEntity auxVar : auxList) {
            musicList.add(auxVar.getMusicFile());

        }
        //return the arrayList
        return musicList;
    }

    public void updatePlaylist(Long id, String name) {

        PlaylistEntity playlist = findPlaylist(id);
        entityManager.getTransaction().begin();
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

        MusicFileEntity musicFile = findMusicFile(id);
        entityManager.getTransaction().begin();
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

        UserEntity user = findUser(id);
        entityManager.getTransaction().begin();
        user.setEmail(email);
        user.setPassword(password);
        entityManager.getTransaction().commit();

    }

}
