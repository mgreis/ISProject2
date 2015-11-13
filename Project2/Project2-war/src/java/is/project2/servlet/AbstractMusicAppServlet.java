/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.servlet;

import is.project2.ejb.MusicData;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract servlet of MusicApp.
 *
 * @author Flávio J. Saraiva
 */
public abstract class AbstractMusicAppServlet extends HttpServlet {

    protected static transient byte[] MUSIC_STREAM = new byte[0];
    
    protected static transient long NEXT_MUSIC_ID;

    protected static final ArrayList<MusicData> MUSIC = new ArrayList<>();

    static {
        NEXT_MUSIC_ID = 1000;
        try {
            {
                final MusicData music1 = new MusicData((long) 1, new URI("http://localhost:8080/Project2-war/music/stream/1.wav"));
                music1.setTitle("music 1");
                music1.setArtist("artist for music 1");
                music1.setAlbum("album for music 1");
                music1.setReleaseYear(1991);
                music1.setAccountId((long) 1);
                MUSIC.add(music1);
            }
            {
                final MusicData music2 = new MusicData((long) 2, new URI("http://localhost:8080/Project2-war/music/stream/2.wav"));
                music2.setTitle("music 2");
                music2.setArtist("artist for music 2");
                music2.setAlbum("album for music 2");
                music2.setReleaseYear(1992);
                music2.setAccountId((long) 123);
                MUSIC.add(music2);
            }
            {
                final MusicData music3 = new MusicData((long) 3, new URI("http://localhost:8080/Project2-war/music/stream/3.wav"));
                music3.setTitle("music 3");
                music3.setArtist("artist for music 3");
                music3.setAlbum("album for music 3");
                music3.setReleaseYear(1993);
                music3.setAccountId(null);
                MUSIC.add(music3);
            }
            {
                final MusicData music10 = new MusicData((long) 10, new URI("http://localhost:8080/Project2-war/music/stream/10.wav"));
                music10.setTitle("music 10");
                music10.setArtist("artist for music 10");
                music10.setAlbum("album for music 10");
                music10.setReleaseYear(2000);
                music10.setAccountId((long) 1);
                MUSIC.add(music10);
            }
            {
                final MusicData music99 = new MusicData((long) 99, new URI("http://localhost:8080/Project2-war/music/stream/99.wav"));
                music99.setTitle("teste");
                music99.setArtist("anonymous");
                music99.setAlbum("album for music 3");
                music99.setReleaseYear(2003);
                music99.setAccountId(null);
                MUSIC.add(music99);
            }
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Process this request.
     *
     * @param request servlet request
     * @param response servlet response
     * @param errors List of errors.
     * @return Data for the onSuccess/onError methods.
     * @throws Exception if an unexpected error occurs.
     */
    protected abstract Object process(HttpServletRequest request, HttpServletResponse response, ArrayList<String> errors) throws Exception;

    /**
     * Manipulate the success response.
     *
     * @param json Success response.
     * @param arg Value returned by {@link #process}.
     */
    protected void onSuccess(JsonObjectBuilder json, Object arg) {
    }

    /**
     * Manipulate the error response.
     *
     * @param json Error response.
     * @param arg Value returned by {@link #process}.
     */
    protected void onError(JsonObjectBuilder json, Object arg) {
    }

    /**
     * Process a json request.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final ArrayList<String> errors = new ArrayList<>();
        Object data = null;
        try {
            data = process(request, response, errors);
        } catch (Exception ex) {
            errors.add(ex.toString());
            ex.printStackTrace(System.out);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(System.out);
                cause = cause.getCause();
            }
        }

        // response
        final JsonObjectBuilder json;
        if (errors.isEmpty()) {
            json = jsonSuccessResponse();
            onSuccess(json, data);
        } else {
            json = jsonErrorResponse(errors.toArray(new String[0]));
            onError(json, data);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(json.build().toString());
    }

    //------------------------------------------------
    private static JsonBuilderFactory factory;

    private JsonBuilderFactory jsonFactory() {
        if (factory == null) {
            factory = Json.createBuilderFactory(null);
        }
        return factory;
    }

    protected JsonObjectBuilder jsonObject() {
        return jsonFactory().createObjectBuilder();
    }

    protected JsonArrayBuilder jsonArray(Object... values) {
        final JsonArrayBuilder array = jsonFactory().createArrayBuilder();
        for (Object value : values) {
            if (value instanceof String) {
                array.add((String) value);
            } else if (value instanceof Long) {
                array.add((long) value);
            } else {
                throw new IllegalArgumentException(value.getClass().toString() + ": " + String.valueOf(value));
            }
        }
        return array;
    }

    protected JsonObjectBuilder jsonSuccessResponse() {
        return jsonObject().add("result", "success");
    }

    protected JsonObjectBuilder jsonErrorResponse(String... errors) {
        return jsonObject()
                .add("result", "error")
                .add("errors", jsonArray((Object[]) errors));
    }

    protected JsonArrayBuilder jsonMusic(Long accountId, MusicData[] musicDataList) {
        final JsonArrayBuilder music = jsonArray();
        for (MusicData musicData : musicDataList) {
            music.add(jsonObject()
                    .add("id", musicData.getId())
                    .add("title", musicData.getTitle())
                    .add("artist", musicData.getArtist())
                    .add("album", musicData.getArtist())
                    .add("releaseYear", musicData.getReleaseYear())
                    .add("url", String.valueOf(musicData.getUri()))
                    .add("canEdit", accountId.equals(musicData.getAccountId()))
            );
        }
        return music;
    }

    /**
     * Parses a json string.
     *
     * Parses objects to {@code HashMap<String,Object>}, arrays to to
     * {@code Object[]}, integral numbers to {@code Long}, decimal numbers to
     * {@code BigDecimal}, booleans to {@code Boolean}, strings to
     * {@code String} and null to {@code null}.
     *
     * @param reader Reader.
     * @return Parsed object.
     */
    protected Object jsonParse(Reader reader) {
        final JsonParser parser = Json.createParser(reader);
        return jsonParse(parser);
    }

    private Object jsonParse(JsonParser parser) {
        JsonParser.Event event = parser.next();
        switch (event) {
            case START_ARRAY: {
                final ArrayList<Object> array = new ArrayList<>();
                for (;;) {
                    final Object value = jsonParse(parser);
                    if (JsonParser.Event.END_ARRAY.equals(value)) {
                        break;
                    }
                    array.add(value);
                }
                return array.toArray();
            }
            case START_OBJECT: {
                final HashMap<String, Object> object = new HashMap<>();
                for (;;) {
                    if (JsonParser.Event.END_OBJECT.equals(parser.next())) {
                        break;
                    }
                    final String key = parser.getString();
                    final Object value = jsonParse(parser);
                    object.put(key, value);
                }
                return object;
            }
            case VALUE_NULL:
                return null;
            case VALUE_TRUE:
                return Boolean.TRUE;
            case VALUE_FALSE:
                return Boolean.FALSE;
            case VALUE_NUMBER:
                if (parser.isIntegralNumber()) {
                    return parser.getLong();
                } else {
                    return parser.getBigDecimal();
                }
            case VALUE_STRING:
                return parser.getString();
            default:
                return event;
        }
    }

    /**
     * Reads the full contents of a file.
     *
     * @param file File.
     * @return Data.
     */
    protected byte[] readFully(File file) throws IOException {
        byte[] data = new byte[(int) file.length()];
        try (DataInputStream dis = new DataInputStream(new FileInputStream(file))) {
            dis.readFully(data);
        }
        return data;
    }

}
