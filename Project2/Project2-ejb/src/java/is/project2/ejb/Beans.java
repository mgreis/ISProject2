/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is.project2.ejb;

import is.project2.ejb.AccountManagerBean;
import is.project2.ejb.AccountManagerBeanRemote;
import is.project2.ejb.MusicFileManagerBean;
import is.project2.ejb.MusicManagerBeanRemote;
import is.project2.ejb.PlaylistManagerBean;
import is.project2.ejb.PlaylistManagerBeanRemote;
import java.util.Hashtable;
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
     * Retrieves a bean proxy.
     *
     * @return Bean proxy.
     * @throws NamingException If a naming exception occurred.
     * @see https://docs.jboss.org/author/display/WFLY8/EJB+invocations+from+a+remote+client+using+JNDI
     */
    public static <T> T lookup(Class beanClass, Class viewClass) throws NamingException {
        assert (beanClass != null);
        assert (viewClass != null);
        final Hashtable jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        final Context context = new InitialContext(jndiProperties);
        final String appName = APP_NAME;
        final String moduleName = MODULE_NAME;
        final String distinctName = DISTINCT_NAME;
        final String beanName = beanClass.getSimpleName();
        final String viewClassName = viewClass.getName();
        return (T) context.lookup("ejb:" + appName + "/" + moduleName + "/" + distinctName + "/" + beanName + "!" + viewClassName);
    }

    /**
     * Retrieves the account manager proxy.
     *
     * @return Account manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static AccountManagerBeanRemote lookupAccountManager() throws NamingException {
        return lookup(AccountManagerBean.class, AccountManagerBeanRemote.class);
    }

    /**
     * Retrieves the playlist manager proxy.
     *
     * @return Playlist manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static PlaylistManagerBeanRemote lookupPlaylistManager() throws NamingException {
        return lookup(PlaylistManagerBean.class, PlaylistManagerBeanRemote.class);
    }

    /**
     * Retrieves the music manager proxy.
     *
     * @return Music manager proxy.
     * @throws NamingException If a naming exception occurred.
     */
    public static MusicManagerBeanRemote lookupMusicManager() throws NamingException {
        return lookup(MusicFileManagerBean.class, MusicManagerBeanRemote.class);
    }

}
