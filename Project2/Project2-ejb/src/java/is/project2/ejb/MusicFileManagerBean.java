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
 * @author Flávio, mgreis
 */
@Stateless
public class MusicFileManagerBean implements MusicFileManagerBeanRemote {

    @EJB
    DatabaseBean database;

    @Override
    public Long upload(MusicUploadData music) throws MusicAppException {

        return database.createMusicFile(database.getAccount(music.getAccountId()),
                music.getTitle(), music.getArtist(), music.getAlbum(),
                music.getReleaseYear(), music.getFilename(), music.getData());
    }

    @Override
    public MusicData load(Long id) throws MusicAppException {
        return database.getMusicData(id);
    }

    @Override
    public MusicData[] load(Long[] ids) throws MusicAppException {
        MusicData[] musicList = new MusicData[ids.length];
        for (int i = 0; i < ids.length; i++) {
            musicList[i] = load(ids[i]);
        }

        return musicList;
    }

    @Override
    public MusicData[] loadAllMine(Long accountId) throws MusicAppException {
        if (database.getUser(accountId) != null) {
            return database.getMusicsFromUser(accountId);
        }
        throw new MusicAppException("Invalid session");
    }

    @Override
    public MusicData[] loadAllOther(Long accountId) throws MusicAppException {
        if (database.getUser(accountId) != null) {
            return database.getMusicsFromUser(accountId);
        }
        throw new MusicAppException("Invalid session");
    }

    @Override
    public MusicData[] findOther(Long accountId, SearchCriteria criteria) throws MusicAppException {
        if (database.getUser(accountId) != null) {

            if (criteria.getCriteria().equalsIgnoreCase("artist")) {
                return database.getMusicsByArtist(criteria.getArgument());
            }
            if (criteria.getCriteria().equalsIgnoreCase("title")) {
                return database.getMusicsByTitle(criteria.getArgument());
            }
            if (criteria.getCriteria().equalsIgnoreCase("artist&title")) {
                return database.getMusicsByArtistAndTitle(criteria.getArgument(), criteria.getArgument2());
            }

        }
        throw new MusicAppException("Invalid session");
    }

    @Override
    public void save(Long accountId, MusicData music) throws MusicAppException {
        if (database.getUser(accountId) != null) {
            database.updateMusicFile(music.getId(), music.getAlbum(), music.getArtist(),
                    music.getUri().getPath(), music.getReleaseYear(), music.getTitle());
        }
        throw new MusicAppException("Invalid session");
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void ping() {
    }
}
