/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

import EJB.NutzerEJB;
import Utilities.Timer;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("daten")
public class App extends Application{
    
    ScheduledExecutorService exec;
    
    @EJB
    private NutzerEJB nutzerEJB;
    /*
    public App() throws IOException{
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleWithFixedDelay(new Timer(), 0, 20, TimeUnit.SECONDS);
    }
    */
    
    public App(){
        ScheduledExecutorService execService
                        =   Executors.newScheduledThreadPool(5);
        execService.scheduleAtFixedRate(()->{
            //The repetitive task, say to update Database
            nutzerEJB.clearBlacklist();
            System.out.println("bl cleared");
        }, 30, 3600, TimeUnit.SECONDS);
        
    }
}
