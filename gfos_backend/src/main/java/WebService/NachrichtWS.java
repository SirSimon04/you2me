/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.FotoEJB;
import EJB.NachrichtEJB;
import Entity.Foto;
import Entity.Nachricht;
import Entity.Nutzer;
import Utilities.Antwort;
import Utilities.Tokenizer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import javax.ws.rs.core.Response;

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
    private FotoEJB fotoEJB;
    
    @EJB
    private Tokenizer tokenizer;
    
    private Antwort response = new Antwort();
    
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
    public Response getAll(@PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
            //return "false";
        }
        else {
            Gson parser = new Gson();
            List<Nachricht> alleNachrichten = nachrichtEJB.getAll();
            return response.generiereAntwort(parser.toJson(alleNachrichten));
            //return "true";  
        }
        
    }
    
    @GET
    @Path("/chat/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByChat(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            Gson parser = new Gson();
            return response.generiereAntwort(parser.toJson(nachrichtEJB.getByChatId(id)));
        }
        
    }
    
    
    @POST
    @Path("/add/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(String jsonStr, @PathParam("token") String token) {
        
        if(!verify(token)){
            return response.generiereFehler401("dentifrisse");
        }
        else {
            System.out.println(jsonStr);
            Gson parser = new Gson();
            try {
                int jsonAnswerId = 0;
                JsonObject jsonObject = parser.fromJson(jsonStr, JsonObject.class);
                Nachricht neueNachricht = parser.fromJson(jsonStr, Nachricht.class);
                String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
                jsonAnswerId = parser.fromJson((jsonObject.get("answerId")), Integer.class);
                if(jsonPic != null){
                    Foto f = new Foto();
                    f.setBase64(jsonPic);
                    fotoEJB.add(f);
                    
                    Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
                    neueNachricht.setFoto(fotoInDB);
                }
                if(jsonAnswerId != 0){
                    Nachricht nachrichtInDB = nachrichtEJB.getByID(jsonAnswerId);
                    neueNachricht.setAntwortauf(nachrichtInDB);
                }
                nachrichtEJB.add(neueNachricht);
                return response.generiereAntwort("validé");
            }
                catch(JsonSyntaxException e) {
                    return response.generiereFehler406("cacahuète");
            }
        }
        
    }
    
    
   
    
    
    
 
}
