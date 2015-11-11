/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console.state;

import is.project2.console.MusicApp;
import is.project2.ejb.MusicData;
import is.project2.ejb.MusicUploadData;
import is.project2.ejb.SearchCriteria;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * Music files of the account.
 *
 * @author Flávio J. Saraiva
 */
public class MusicListState extends AbstractState {

    public MusicListState(MusicApp app) throws NamingException {
        super(app);
    }

    @Override
    public AbstractState process() {
        assert (app.accountId != null);
        try {
            final String cmd = app.read("music> ");
            switch (cmd) {
                case "list": {
                    listMusic(app.musicManager.loadAllMine(app.accountId));
                    break;
                }
                case "list-other-music": {
                    listMusic(app.musicManager.loadAllOther(app.accountId));
                    break;
                }
                case "find-other-music": {
                    final String criteria = app.read("search: ");
                    listMusic(app.musicManager.findOther(app.accountId, new SearchCriteria(criteria)));
                    break;
                }
                case "create": {
                    final String title = app.read("title: ");
                    final String artist = app.read("artist: ");
                    final String album = app.read("album: ");
                    final Date year = new SimpleDateFormat("y-M-d").parse(app.read("year(yyyy-mm-dd): "));
                    final File file = new File(app.read("file: "));
                    final byte[] data = new byte[(int) file.length()];
                    try (FileInputStream input = new FileInputStream(file)) {
                        int offset = 0;
                        while (offset < data.length) {
                            int bytes = input.read(data, offset, data.length - offset);
                            if (bytes == -1) {
                                app.writer.println("abort, unexpected end of data");
                                return this; // keep state
                            }
                            offset += bytes;
                        }
                    }
                    final MusicUploadData uploadData = new MusicUploadData();
                    uploadData.setAccountId(app.accountId);
                    uploadData.setFilename(file.getName());
                    uploadData.setData(data);
                    uploadData.setTitle(title);
                    uploadData.setArtist(artist);
                    uploadData.setAlbum(album);
                    uploadData.setReleaseYear(year);
                    app.musicId = app.musicManager.upload(uploadData);
                    return new MusicState(app);
                }
                case "open": {
                    final String id = app.read("id: ");
                    app.musicId = Long.parseLong(id);
                    return new MusicState(app);
                }
                case "back": {
                    return new MainState(app);
                }
                case "account": {
                    return new AccountState(app);
                }
                case "logout": {
                    return new GuestState(app);
                }
                default: {
                    app.writer.println("Music list commands:");
                    app.writer.println(" list - lists all my music");
                    app.writer.println(" list-other-music - list all music that is not mine");
                    app.writer.println(" find-other-music - search for music that isn't mine");
                    app.writer.println(" create - create a new music entry");
                    app.writer.println(" open - open a music entry");
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

    private void listMusic(MusicData[] musicList) {
        assert (musicList != null);
        for (MusicData music : musicList) {
            app.writer.format(" [id=%d] %s\n", music.getId(), music.getUri());
            app.writer.format("  title  : %s\n", music.getTitle());
            app.writer.format("  artist : %s\n", music.getArtist());
            app.writer.format("  album  : %s\n", music.getAlbum());
            app.writer.format("  year   : %s\n", music.getReleaseYear());
        }
    }

}
