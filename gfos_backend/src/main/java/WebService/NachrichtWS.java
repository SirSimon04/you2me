/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.NachrichtEJB;
import Entity.Nachricht;
import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.gson.JsonSyntaxException;
/**
 *
 * @author simon
 */
@Path("/nachricht")
@Stateless
@LocalBean
public class NachrichtWS {
    
    @EJB
    private NachrichtEJB nachrichtEJB;
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean create(String jsonStr) {
        System.out.println(jsonStr);
        Gson parser = new Gson();
        try {
            Nachricht neueNachricht = parser.fromJson(jsonStr, Nachricht.class);
            nachrichtEJB.add(neueNachricht);
            return true;
        }
            catch(JsonSyntaxException e) {
            return false;
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
    Gson parser = new Gson();
    List<Nachricht> alleNachrichten = nachrichtEJB.getAll();
    return parser.toJson(alleNachrichten);
    }
}
