/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.console;

import is.project2.ejb.AccountManagerBeanRemote;
import is.project2.ejb.MusicAppException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Mário
 */
public class exp {
    public static void main(String[] args) throws NamingException, MusicAppException {
       
            AccountManagerBeanRemote exp1 =
                    (AccountManagerBeanRemote) InitialContext.doLookup("Project2-ejb/AccountManagerBean!is.project2.ejb.AccountManagerBeanRemote");
            String pass = "pass";
            
           System.out.println(exp1.register("marco", pass.toCharArray() ));
           Long userId = exp1.login("marco", pass.toCharArray());
           
        
        
    }
}
