/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.Beans;
import is.project2.ejb.MusicData;
import is.project2.ejb.MusicManagerBeanRemote;
import is.project2.ejb.MusicUploadData;
import is.project2.ejb.SearchCriteria;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Flávio
 */
public class MusicState extends AbstractState {

    final MusicManagerBeanRemote musicManager;

    public MusicState(MusicApp app) throws NamingException {
        super(app);
        musicManager = InitialContext.doLookup(Beans.MUSIC_MANAGER_BEAN);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        assert (app.musicId != null);
        try {
            final MusicData music = musicManager.load(app.musicId);
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
                        musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-artist": {
                    if (canChange(music)) {
                        final String artist = app.read("artist: ");
                        music.setArtist(artist);
                        musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-album": {
                    if (canChange(music)) {
                        final String album = app.read("album: ");
                        music.setAlbum(album);
                        musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "change-year": {
                    if (canChange(music)) {
                        final Date year = new SimpleDateFormat("y-M-d").parse(app.read("year(yyyy-mm-dd): "));
                        music.setReleaseYear(year);
                        musicManager.save(app.accountId, music);
                    }
                    break;
                }
                case "detach": {
                    if (canChange(music)) {
                        music.setAccountId(null);
                        musicManager.save(app.accountId, music);
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
