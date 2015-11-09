/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console;

import is.project2.console.state.AbstractState;
import is.project2.console.state.GuestState;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aplicação de música.
 *
 * @author Flávio J. Saraiva
 */
public class MusicApp implements Runnable {

    private final Console console;
    private final BufferedReader reader;
    public final PrintWriter writer;
    public Long accountId;
    public Long playlistId;
    public Long musicId;

    public MusicApp(String[] args) {
        console = System.console();
        if (console == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(new OutputStreamWriter(System.out), true);
        } else {
            reader = null;
            writer = console.writer();
        }
    }

    @Override
    public void run() {
        writer.println("MusicApp");
        try {
            AbstractState state = new GuestState(this);
            while (state != null) {
                state = state.process();
            }
        } catch (Exception ex) {
            writer.println(ex);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a trimmed line from the console.
     *
     * @param prefix Text to write before reading.
     * @return Trimmed line.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public String read(String prefix) throws IOException {
        assert (prefix != null);
        writer.print(prefix);
        writer.flush();
        if (console != null) {
            return console.readLine().trim();
        }
        return reader.readLine().trim();
    }

    /**
     * Returns a password from the console.
     *
     * @param prefix Text to write before reading.
     * @return Password characters
     * @throws java.io.IOException If an I/O error occurs.
     */
    public char[] readPassword(String prefix) throws IOException {
        writer.print(prefix);
        writer.flush();
        if (console != null) {
            return console.readPassword();
        }
        return reader.readLine().toCharArray();
    }

    public static void main(String[] args) {
        final MusicApp app = new MusicApp(args);
        app.run();
    }

}
