/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.ChatEJB;
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
 *<h1>Der Webserver für die Datenverarbeitung, bezogen auf die Nachrichten</h1>
 * <p>Diese Klasse beinhaltet alle Methoden des Webservers bezogen auf das Objekt der Nachrichten
 * für das Bearbeiten und Ausgeben der Daten und stellt damit die Schnittstelle mit dem Frontend dar</p>
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
    private ChatEJB chatEJB;
    
    @EJB
    private Tokenizer tokenizer;
    
    private Antwort response = new Antwort();
    
    /**
     * Diese Methode verifiziert einen Token.
     * @param token Das Webtoken
     * @return Boolean, ob der Token akzeptiert wurde
     */
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
    
     /**
      * Diese Methode gibt alle Nachrichten zurück.
      * @param token Das Webtoken       
      * @return Die Liste mit allen Nachrichten
      */
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
    
    /**
     * Diese Methode liefert alle Nachrichten eines bestimmten Chats zurück.
     * @param id Die Id des Chats, aus dem die Nachrichten angezeigt werden solln
     * @param token Das Webtoken    
     * @return Die Liste mit den Nachrichten.
     */
    @GET
    @Path("/chat/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByChat(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            Gson parser = new Gson();
            List<Nachricht> nList = nachrichtEJB.getByChatId(id);
            return response.generiereAntwort(parser.toJson(nList));
        }
        
    } 
    /**
     * Diese Methode liefert die neuste Nachricht aus einem Chat.
     * @param id Die Id des Chats
     * @param token Das Webtoken
     * @return Die neueste Nachricht
     */
    @GET
    @Path("/chat/getNewest/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewest(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            Gson parser = new Gson();
            Nachricht n = nachrichtEJB.getNewest(id);
            return response.generiereAntwort(parser.toJson(n));
        }
    }
    
    /**
     * Diese Methode sendet eine Nachricht in einen Chat.
     * @param Datenn Die Daten zum Chat und der Nachricht.
     * @param token Das Webtoken
     * @return Das Responseobkjekt mit dem Status der Methode.
     */
    @POST
    @Path("/add/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(String Datenn, @PathParam("token") String token) {
        
        if(!verify(token)){
            return response.generiereFehler401("dentifrisse");
        }
        else {
            System.out.println(Datenn);
            Gson parser = new Gson();
            try {
                int jsonAnswerId = 0;
                JsonObject jsonObject = parser.fromJson(Datenn, JsonObject.class);
                Nachricht neueNachricht = parser.fromJson(Datenn, Nachricht.class);
                String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
                int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class);
                try{
                    jsonAnswerId = parser.fromJson((jsonObject.get("answerId")), Integer.class);
                    Nachricht nachrichtInDB = nachrichtEJB.getById(jsonAnswerId);
                    neueNachricht.setAntwortauf(nachrichtInDB);
                }
                catch(NullPointerException e){
                    
                }
                
                if(jsonPic != null){
                    Foto f = new Foto();
                    f.setBase64(jsonPic);
                    fotoEJB.add(f);
                    
                    Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
                    neueNachricht.setFoto(fotoInDB);
                }
                chatEJB.getById(chatId).setLetztenachricht(neueNachricht);
                nachrichtEJB.add(neueNachricht);
                return response.generiereAntwort("validé");
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("cacahuète");
            }
            catch(NullPointerException e){
                return response.generiereFehler500("Chatid oder Senderid nicht vorhanden");
            }
        }   
        
    }
    
    /**
     * Diese Methode löscht eine Nachricht aus einem Chat. Das gilt für alle Nutzer.
     * @param Daten Die Daten zur Nachricht.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/delete/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(String Daten, @PathParam("token") String token) {
        
        if(!verify(token)){
            return response.generiereFehler401("dentifrisse");
        }
        else {
            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            int lNachrichtId = parser.fromJson((jsonObject.get("nachrichtid")), Integer.class);
            Nachricht n = nachrichtEJB.getById(lNachrichtId);
            
            if(chatEJB.getById(n.getChatid()).getLetztenachricht().equals(n)){
                
                chatEJB.getById(n.getChatid()).setLetztenachricht(null);
                
                nachrichtEJB.delete(lNachrichtId);
                
                List<Nachricht> nList = nachrichtEJB.getByChatId(n.getChatid());
                System.out.println(nList);
                if(nList.size() != 0){
                    chatEJB.getById(n.getChatid()).setLetztenachricht(nList.get(nList.size() - 1));
                }
                
                return response.generiereAntwort("true");
                

                
            }
            else{
                    
                nachrichtEJB.delete(lNachrichtId);
                return response.generiereAntwort("true");
            }
            
            
        }
        
    }
    
}
   
    
    