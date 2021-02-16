/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;
import EJB.ChatEJB;
import EJB.NutzerEJB;
import Entity.Chat;
import Entity.Nutzer;
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

/**
 *
 * @author simon
 */
@Path("/chat")
@Stateless
@LocalBean
public class ChatWS {
    
    @EJB
    private NutzerEJB nutzerEJB;
    
    @EJB
    private ChatEJB chatEJB;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll() {
        Gson parser = new Gson();
        List<Chat> alleNachrichten = chatEJB.getAll();
        return parser.toJson(alleNachrichten);
    }
    
    
    
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean create(String jsonStr) {
        System.out.println(jsonStr);
        Gson parser = new Gson();
        try {
            System.out.println("entered try");
            Chat neuerChat = parser.fromJson(jsonStr, Chat.class);
            chatEJB.createChat(neuerChat);
            return true;
        }
            catch(JsonSyntaxException e) {
            return false;
        }
    }
    /*
    Als erstes wird die Id des Chats angegeben, danach der Benutzername des Benutzers, der hinzugefügt wird
    {
        "chatid": 0,
        "benutzername": "NoSkiller"
    }
    */
    @POST
    @Path("/takepart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean takePart(String jsonStr){
        Gson parser = new Gson();
        
        try{
            JsonObject jsonTP = parser.fromJson(jsonStr, JsonObject.class);
            
            int chatid = parser.fromJson((jsonTP.get("chatid")), Integer.class);
            Chat c = chatEJB.getById(chatid);
            
            String username = parser.fromJson((jsonTP.get("benutzername")), String.class);
            Nutzer addedUser = nutzerEJB.getByUsername(username);
            
            chatEJB.fuegeHinzu(c, addedUser);
            
            return true;
        }
        catch(JsonSyntaxException e) {
            return false;
        }
    }
    
    /*
    @POST
    @Path("/takepart")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean takePart(String jsonStr){
        Gson parser = new Gson();
        
        try{    
        
            JsonObject jsonTP = parser.fromJson(jsonStr, JsonObject.class);
            
            int chatid = parser.fromJson((jsonTP.get("chatid")), Integer.class);
            
            String username = parser.fromJson((jsonTP.get("benutzername")), String.class);
            Nutzer loginNutzer = nutzerEJB.getByUsername(username);
            int userid = loginNutzer.getId();
            
            Nimmtteil nt;
            nt = new Nimmtteil();
            nt.setChatid(chatid);
            nt.setNutzerid(userid);
            
            chatEJB.takePart(nt);
            
            return true; 
           
        }
            catch(JsonSyntaxException e) {
            return false;
        }
            
            
    }
    */
    
    
    
}
