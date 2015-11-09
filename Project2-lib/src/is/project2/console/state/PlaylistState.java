/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.Beans;
import is.project2.ejb.MusicAppException;
import is.project2.ejb.MusicData;
import is.project2.ejb.MusicManagerBeanRemote;
import is.project2.ejb.PlaylistData;
import is.project2.ejb.PlaylistManagerBeanRemote;
import is.project2.ejb.SearchCriteria;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Manage a particular playlist.
 *
 * @author Flávio J. Saraiva
 */
public class PlaylistState extends AbstractState {

    final private PlaylistManagerBeanRemote playlistManager;
    final private MusicManagerBeanRemote musicManager;

    public PlaylistState(MusicApp app) throws NamingException {
        super(app);
        playlistManager = InitialContext.doLookup(Beans.PLAYLIST_MANAGER_BEAN);
        musicManager = InitialContext.doLookup(Beans.MUSIC_MANAGER_BEAN);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        assert (app.playlistId != null);
        try {
            final String cmd = app.read("playlists[" + String.valueOf(app.playlistId) + "]> ");
            switch (cmd) {
                case "view": {
                    viewPlaylist();
                    break;
                }
                case "change-name": {
                    changeName();
                    break;
                }
                case "delete": {
                    delete();
                    return new PlaylistListState(app);
                }
                case "list-music": {
                    listMusic(musicManager.loadAllMine(app.accountId));
                    break;
                }
                case "list-other-music": {
                    listMusic(musicManager.loadAllOther(app.accountId));
                    break;
                }
                case "find-other-music": {
                    final String criteria = app.read("search: ");
                    listMusic(musicManager.findOther(app.accountId, new SearchCriteria(criteria)));
                    break;
                }
                case "add-music": {
                    addMusic();
                    break;
                }
                case "remove-music": {
                    removeMusic();
                    break;
                }
                case "back": {
                    return new PlaylistListState(app);
                }
                case "account": {
                    return new AccountState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Playlist commands:");
                    app.writer.println(" view - view playlist details");
                    app.writer.println(" change-name - change the name of the playlist");
                    app.writer.println(" delete - delete the playlist");
                    app.writer.println(" list-music - lists all my music");
                    app.writer.println(" list-other-music - list all music that is not mine");
                    app.writer.println(" find-other-music - search for music that isn't mine");
                    app.writer.println(" add-music - add music to the playlist");
                    app.writer.println(" remove-music - remove music from the playlist");
                    app.writer.println(" back");
                    app.writer.println(" account");
                    app.writer.println(" logout");
                }
            }
        } catch (IOException ex) {
            app.writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null; // exit
        } catch (Exception ex) {
            app.writer.println(ex);
            Logger.getLogger(PlaylistListState.class.getName()).log(Level.WARNING, null, ex);
        }
        return this; // keep state
    }

    private PlaylistData load() throws MusicAppException {
        return playlistManager.load(app.accountId, app.playlistId);
    }

    private void save(PlaylistData playlist) throws MusicAppException {
        assert (playlist != null);
        playlistManager.save(app.accountId, playlist);
    }

    private void delete() throws MusicAppException {
        playlistManager.delete(app.accountId, app.playlistId);
    }

    private void viewPlaylist() throws MusicAppException {
        final PlaylistData playlist = load();
        final MusicData[] musicList = musicManager.load(playlist.getMusic().toArray(new Long[0]));
        app.writer.format(" name: %s\n", playlist.getName());
        for (int index = 0; index < musicList.length; index++) {
            final MusicData music = musicList[index];
            app.writer.format(" [index=%d,id=%d] %s\n", index, music.getId(), music.getUri());
            app.writer.format("  title  : %s\n", music.getTitle());
            app.writer.format("  artist : %s\n", music.getArtist());
            app.writer.format("  album  : %s\n", music.getAlbum());
            app.writer.format("  year   : %s\n", music.getReleaseYear());
        }
    }

    private void changeName() throws IOException, MusicAppException {
        final String name = app.read("name: ");
        final PlaylistData playlist = load();
        playlist.setName(name);
        save(playlist);
    }

    private void listMusic(MusicData[] musicList) {
        assert (musicList != null);
        for (final MusicData music : musicList) {
            app.writer.format(" [id=%d] %s\n", music.getId(), music.getUri());
            app.writer.format("  title  : %s\n", music.getTitle());
            app.writer.format("  artist : %s\n", music.getArtist());
            app.writer.format("  album  : %s\n", music.getAlbum());
            app.writer.format("  year   : %s\n", music.getReleaseYear());
        }
    }

    private void addMusic() throws IOException, MusicAppException {
        final long musicId = Long.parseLong(app.read("id: "));
        final PlaylistData playlist = load();
        final int index = Integer.parseInt(app.read("index(0.." + String.valueOf(playlist.getMusic().size()) + "): "));
        playlist.getMusic().add(index, musicId);
        save(playlist);
    }

    private void removeMusic() throws MusicAppException, IOException {
        final PlaylistData playlist = load();
        if (playlist.getMusic().isEmpty()) {
            app.writer.println("playlist is empty");
            return;
        }
        final int index = Integer.parseInt(app.read("index(0.." + String.valueOf(playlist.getMusic().size() - 1) + "): "));
        playlist.getMusic().remove(index);
        save(playlist);
    }

}
