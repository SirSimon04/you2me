/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

@ApplicationPath("daten")
public class App extends Application {

    ScheduledExecutorService exec;

    @EJB
    private BlacklistEJB blacklistEJB;
    @EJB
    private NutzerEJB nutzerEJB;

    public App() {
        System.out.println(System.currentTimeMillis());
        System.out.println("App constructed");
        ScheduledExecutorService execService
                = Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(() -> {
            blacklistEJB.clearBlacklist();
            System.out.println("bl cleared");
        }, 30, 3600, TimeUnit.SECONDS);

//        execService.scheduleAtFixedRate(() -> {
//            nutzerEJB.setAllOffline();
//            System.out.println("all off");
//        }, 300, 300, TimeUnit.SECONDS);
    }
}
