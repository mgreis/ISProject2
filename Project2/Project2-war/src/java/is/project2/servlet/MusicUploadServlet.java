/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.MusicData;
import is.project2.ejb.MusicUploadData;
import static is.project2.servlet.AbstractMusicAppServlet.MUSIC;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.balusc.http.multipart.MultipartMap;

/**
 * Upload music.
 *
 * @author Flávio J. Saraiva
 */
@WebServlet(
        urlPatterns = {"/music/upload.json"},
        description = "Upload music")
@MultipartConfig(maxRequestSize = 25 * 1024 * 1024) // 25MiB
public class MusicUploadServlet extends AbstractMusicAppServlet {

    @Override
    protected Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception {
        final Long accountId = (Long) request.getSession().getAttribute("accountId");
        if (accountId == null) {
            errors.add("Not logged in");
            return null;
        } else {
            final MultipartMap map = new MultipartMap(request, System.getProperty("java.io.tmpdir"));
            final MusicUploadData upload = new MusicUploadData();
            upload.setAccountId(accountId);
            upload.setFilename(map.getParameter("filename"));
            upload.setData(readFully(map.getFile("data")));
            upload.setTitle(map.getParameter("title"));
            upload.setArtist(map.getParameter("artist"));
            upload.setAlbum(map.getParameter("album"));
            upload.setReleaseYear(Integer.parseInt(map.getParameter("releaseYear")));
            // @todo ejb
            final Long id = NEXT_MUSIC_ID++;
            final URI uri = new URI("http://localhost:8080/Project2-war/music/stream/"
                    + String.valueOf(id) + "." + upload.getFilename().substring(upload.getFilename().lastIndexOf('.') + 1));
            final MusicData music = new MusicData(id, uri);
            music.setTitle(upload.getTitle());
            music.setArtist(upload.getArtist());
            music.setAlbum(upload.getAlbum());
            music.setReleaseYear(upload.getReleaseYear());
            music.setArtist(upload.getArtist());
            music.setAccountId(accountId);
            MUSIC.add(music);
            return null;
        }
    }

}
