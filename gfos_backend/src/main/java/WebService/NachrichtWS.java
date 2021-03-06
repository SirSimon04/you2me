/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.NachrichtEJB;
import Entity.Nachricht;
import Utilities.Tokenizer;
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
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    @EJB
    private Tokenizer tokenizer;
    public boolean verify(String token){
        if(tokenizer.isOn()){
            if(tokenizer.verifyToken(token).equals(""))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else {
            return true;
        }
        
    }
    
     
    @GET
    @Path("/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(@PathParam("token") String token) {
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else {
            Gson parser = new Gson();
            List<Nachricht> alleNachrichten = nachrichtEJB.getAll();
            return parser.toJson(alleNachrichten);
        }
        
    }
    
    @GET
    @Path("/chat/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByChat(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else {
            Gson parser = new Gson();
            return parser.toJson(nachrichtEJB.getByChat(id));
        }
        
    }
    
    
    @POST
    @Path("/add/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String create(String jsonStr, @PathParam("token") String token) {
        
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else {
            System.out.println(jsonStr);
            Gson parser = new Gson();
            try {
                System.out.println("entered try");
                Nachricht neueNachricht = parser.fromJson(jsonStr, Nachricht.class);
                nachrichtEJB.add(neueNachricht);
                return "true";
            }
                catch(JsonSyntaxException e) {
                    return "false";
            }
        }
        
    }
   
    
    
    
 
}
