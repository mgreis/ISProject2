/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.MusicData;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author Flávio
 */
public class MusicState extends AbstractState {

    public MusicState(MusicApp app) throws NamingException {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        assert (app.musicId != null);
        try {
            final MusicData music = app.musicManager.load(app.musicId);
            final String cmd = app.read("music[" + String.valueOf(app.musicId) + "]> ");
            switch (cmd) {
                case "view": {
                    app.writer.format(" id     : %d\n", music.getId());
                    app.writer.format(" uri    : %s\n", music.getUri());
                    app.writer.format(" title  : %s\n", music.getTitle());
                    app.writer.format(" artist : %s\n", music.getArtist());
                    app.writer.format(" album  : %s\n", music.getAlbum());
                    app.writer.format(" year   : %s\n", music.getReleaseYear());
                    app.writer.format(" mine   : %b\n", app.accountId.equals(music.getAccountId()));
                    break;
                }
                case "change-title": {
                    if (canChange(music)) {
                        final String title = app.read("title: ");
                        music.setTitle(title);
                        app.musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-artist": {
                    if (canChange(music)) {
                        final String artist = app.read("artist: ");
                        music.setArtist(artist);
                        app.musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-album": {
                    if (canChange(music)) {
                        final String album = app.read("album: ");
                        music.setAlbum(album);
                        app.musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-year": {
                    if (canChange(music)) {
                        final int year = Integer.parseInt(app.read("year: "));
                        music.setReleaseYear(year);
                        app.musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "detach": {
                    if (canChange(music)) {
                        music.setAccountId(null);
                        app.musicManager.save(app.accountId, music);
                        return new MusicListState(app);
                    }
                    break;
                }
                case "back": {
                    return new MusicListState(app);
                }
                case "account": {
                    return new AccountState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Music commands:");
                    app.writer.println(" view - view music details");
                    app.writer.println(" change-title - change the title");
                    app.writer.println(" change-artist - change the artist");
                    app.writer.println(" change-album - change the album");
                    app.writer.println(" change-year - change the release year");
                    // @todo change-data?
                    app.writer.println(" detach - detach user from music (will not be able to edit)");
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
            Logger.getLogger(MusicListState.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this; // keep state
    }

    private boolean canChange(MusicData music) {
        if (!app.accountId.equals(music.getAccountId())) {
            app.writer.println("abort, not my music");
            return false; // not my music
        }
        return true; // my music
    }
}
