/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.NutzerEJB;
import Entity.Nutzer;
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
import com.google.gson.JsonObject;

/**
 *
 * @author Simon
 */
@Path("/nutzer")
@Stateless
@LocalBean
public class NutzerWS {
    
    @EJB
    private NutzerEJB nutzerEJB;
    
    
    
    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return ("test succesful1");
    }
    
    /*
    Es folgen alle Methoden, die sich auf die Klasse Nutzer beziehen
    */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(){
        Gson parser = new Gson();
        List<Nutzer> alleNutzer = nutzerEJB.getAll();
        return parser.toJson(alleNutzer);
    }
    
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") int id) {
        Gson parser = new Gson();
        return parser.toJson(nutzerEJB.getById(id));
    }
    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean login(String jsonStr) {
       
        Gson parser = new Gson();
        try {
            //getting the name of the body
            JsonObject loginUser = parser.fromJson(jsonStr, JsonObject.class);
            String username = parser.fromJson((loginUser.get("benutzername")), String.class);
            String passwort = parser.fromJson((loginUser.get("passwort")), String.class);
            System.out.println(username);
            System.out.println(passwort);
            
            //getting the Nutzer with the name gotten before
            Nutzer loginNutzer = nutzerEJB.getByUsername(username);
            
            if(loginNutzer.getPasswort().equals(passwort))
            {
                return true;
            }
            else 
            {
                return false;
            }
            
            
            
        }
            catch(JsonSyntaxException e) {
                System.out.println("catch");
                return false;
            }

        
    }
    
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean create(String jsonStr) {
        System.out.println(jsonStr);
        Gson parser = new Gson();
        try {
            Nutzer neuerNutzer = parser.fromJson(jsonStr, Nutzer.class);
            nutzerEJB.add(neuerNutzer);
            return true;
        }
            catch(JsonSyntaxException e) {
            return false;
        }
    }
    
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
        public boolean delete(@PathParam("id") int id) {
        return nutzerEJB.delete(id);
    }
        
    @PUT
    @Path("/put")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean update(String jsonStr) {
        Gson parser = new Gson();
        Nutzer aktNutzer = parser.fromJson(jsonStr, Nutzer.class);
        return nutzerEJB.update(aktNutzer);
    }
    
    
     
}