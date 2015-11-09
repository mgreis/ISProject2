/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.jpa.entities.Account;
import javax.ejb.Remote;

/**
 * Interface to the remote stateless app.
 *
 * @todo sending the password raw is not secure!!!
 *
 * @author Flávio J. Saraiva
 */
@Remote
public interface MusicAppBeanRemote {

}
