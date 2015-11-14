/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import java.util.Hashtable;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Euxiliary class that retrieves the bean proxies.
 *
 * @author Flávio J. Saraiva
 */
public class Beans {

    /**
     * The app name is the application name of the deployed EJBs. This is
     * typically the ear name without the .ear suffix. However, the application
     * name could be overridden in the application.xml of the EJB deployment on
     * the server.
     */
    public static String APP_NAME = "Project2";

    /**
     * This is the module name of the deployed EJBs on the server. This is
     * typically the jar name of the EJB deployment, without the .jar suffix,
     * but can be overridden via the ejb-jar.xml
     *
     */
    public static String MODULE_NAME = "Project2-ejb";

    /**
     * AS7 allows each deployment to have an (optional) distinct name.
     */
    public static String DISTINCT_NAME = "";

    /**
     * Looks up a bean proxy.
     *
     * @param <T> View class.
     * @param beanName bean that implements the interface.
     * @param interfaceFullName Full class name of the interface.
     * @return Bean proxy.
     * @throws NamingException If a naming exception occurred.
     * @see
     * https://docs.jboss.org/author/display/WFLY8/EJB+invocations+from+a+remote+client+using+JNDI
     * @see
     * http://stackoverflow.com/questions/24317960/wildfly-8-ejb-client-issues
     */
    public static <T> T lookup(String beanName, String interfaceFullName) throws NamingException {
        assert (beanName != null);
        assert (interfaceFullName != null);
        final Properties jndiProperties = new Properties();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        final Context jndi = new InitialContext(jndiProperties);
        final String appName = APP_NAME;
        final String moduleName = MODULE_NAME;
        final String distinctName = DISTINCT_NAME;
        final String jndiName = "ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + interfaceFullName;
        //ejb:{app-Name}/{module-Name}/{distinct-Name}/{bean-Name}!{fullPath-remote-Interface}
        T service = (T) jndi.lookup(jndiName);
        return service;
    }

    /**
     * Retrieves the account manager proxy.
     *
     * @return Account manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static AccountManagerBeanRemote lookupAccountManager() throws NamingException {
        return lookup("AccountManagerBean", AccountManagerBeanRemote.class.getName());
    }

    /**
     * Retrieves the playlist manager proxy.
     *
     * @return Playlist manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static PlaylistManagerBeanRemote lookupPlaylistManager() throws NamingException {
        return lookup("PlaylistManagerBean", PlaylistManagerBeanRemote.class.getName());
    }

    /**
     * Retrieves the music manager proxy.
     *
     * @return Music manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static MusicFileManagerBeanRemote lookupMusicManager() throws NamingException {
        return lookup("MusicFileManagerBean", MusicFileManagerBeanRemote.class.getName());
    }

}
