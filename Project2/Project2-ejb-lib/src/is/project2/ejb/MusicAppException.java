/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

/**
 * Generic exception from the remote music application.
 *
 * @author Flávio J. Saraiva
 */
public class MusicAppException extends Exception {

    public MusicAppException() {
        super();
    }

    public MusicAppException(String message) {
        super(message);
    }

    public MusicAppException(Throwable cause) {
        super(cause);
    }

    public MusicAppException(String message, Throwable cause) {
        super(message, cause);
    }
}
