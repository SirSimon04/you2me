/**
 * <h1>Der Startpunkt der Applikation</h1>
 */
package Application;

import EJB.BlacklistEJB;
import EJB.NutzerEJB;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * <h1>Diese Methode startet den Server und enthält die Threads, die automatisch immer nach bestimmten Zeiten ausgeführt werden
 * ausgeführt werden.</h1>
 *
 * @author simon
 */
@ApplicationPath("daten")
public class App extends Application{

    ScheduledExecutorService exec;

    @EJB
    private BlacklistEJB blacklistEJB;
    @EJB
    private NutzerEJB nutzerEJB;

    /**
     * Diese Methode startet und konstruiert die App.
     */
    public App(){
        ScheduledExecutorService execService
                = Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(() -> {
            blacklistEJB.clearBlacklist();
        }, 30, 3600, TimeUnit.SECONDS);

        execService.scheduleAtFixedRate(() -> {
            nutzerEJB.setAllOffline();
        }, 30, 300, TimeUnit.SECONDS);
    }
}
