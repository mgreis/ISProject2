/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.MusicAppException;
import is.project2.ejb.MusicFileData;
import is.project2.ejb.MusicFileManagerBeanRemote;
import is.project2.ejb.PlaylistData;
import is.project2.ejb.PlaylistManagerBeanRemote;
import is.project2.ejb.SearchCriteria;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage a particular playlist.
 *
 * @author Flávio J. Saraiva
 */
public class PlaylistState extends AbstractState {

    final private PlaylistManagerBeanRemote playlistManager;
    final private MusicFileManagerBeanRemote musicFileManager;

    public PlaylistState(MusicApp app) {
        super(app);
        playlistManager = null; // @todo
        musicFileManager = null; // @todo
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
                case "add-music-mine": {
                    addMusicFileFrom(musicFileManager.getMine(app.accountId));
                    break;
                }
                case "add-music-other": {
                    addMusicFileFrom(musicFileManager.getOther(app.accountId));
                    break;
                }
                case "add-music-other-search": {
                    final String criteria = app.read("search: ");
                    addMusicFileFrom(musicFileManager.findOther(app.accountId, new SearchCriteria(criteria)));
                    break;
                }
                case "remove-music": {
                    removeMusicFile();
                    break;
                }
                case "delete": {
                    delete();
                    return new PlaylistsState(app);
                }
                case "back": {
                    return new PlaylistsState(app);
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
                    app.writer.println(" add-music-mine - add a music file from my collection to the playlist");
                    app.writer.println(" add-music-other - add a music file not in my collection to the playlist");
                    app.writer.println(" add-music-other-search - search for a music file not in my collection and add it to the playlist");
                    app.writer.println(" remove-music - remove music from the playlist");
                    app.writer.println(" delete - delete the playlist");
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
            Logger.getLogger(PlaylistsState.class.getName()).log(Level.WARNING, null, ex);
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
        app.writer.format(" name: %s\n", playlist.getName());
        for (int index = 0; index < playlist.getMusicFiles().size(); index++) {
            final MusicFileData musicFile = playlist.getMusicFiles().get(index);
            app.writer.format(" [index=%d,id=%d] %s\n", index, musicFile.getId(), musicFile.getUri());
            app.writer.format("  title  : %s\n", musicFile.getTitle());
            app.writer.format("  artist : %s\n", musicFile.getArtist());
            app.writer.format("  album  : %s\n", musicFile.getAlbum());
            app.writer.format("  year   : %s\n", musicFile.getReleaseYear());
        }
    }

    private void changeName() throws IOException, MusicAppException {
        final String name = app.read("name: ");
        final PlaylistData playlist = load();
        playlist.setName(name);
        save(playlist);
    }

    private void addMusicFileFrom(Collection<MusicFileData> musicFiles) throws IOException, MusicAppException {
        for (final MusicFileData musicFile : musicFiles) {
            app.writer.format(" [id=%d] %s\n", musicFile.getId(), musicFile.getUri());
            app.writer.format("  title  : %s\n", musicFile.getTitle());
            app.writer.format("  artist : %s\n", musicFile.getArtist());
            app.writer.format("  album  : %s\n", musicFile.getAlbum());
            app.writer.format("  year   : %s\n", musicFile.getReleaseYear());
        }
        final long id = Long.parseLong(app.read("id: "));
        for (MusicFileData musicFile : musicFiles) {
            if (musicFile.getId().equals(id)) {
                final PlaylistData playlist = load();
                final int index = Integer.parseInt(app.read("index[0.." + String.valueOf(playlist.getMusicFiles().size()) + "]: "));
                playlist.getMusicFiles().add(index, musicFile);
                save(playlist);
            }
        }
        app.writer.println("id not listed");
    }

    private void removeMusicFile() throws MusicAppException, IOException {
        final PlaylistData playlist = load();
        final int index = Integer.parseInt(app.read("index[0.." + String.valueOf(playlist.getMusicFiles().size() - 1) + "]: "));
        playlist.getMusicFiles().remove(index);
        save(playlist);
    }

}
