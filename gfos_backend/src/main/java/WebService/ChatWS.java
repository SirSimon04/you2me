/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;
import EJB.ChatEJB;
import EJB.FotoEJB;
import EJB.NachrichtEJB;
import EJB.NutzerEJB;
import Entity.Chat;
import Entity.Foto;
import Entity.Nachricht;
import Entity.Nutzer;
import Utilities.Antwort;
import Utilities.Tokenizer;
import Utilities.DateSorter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ws.rs.core.Response;

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
    
    @EJB FotoEJB fotoEJB;
    
    @EJB
    private NachrichtEJB nachrichtEJB;
    
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
        }
        else {
           List<Chat> liste = chatEJB.getAllCopy();
            
            Gson parser = new Gson();
            return response.generiereAntwort(parser.toJson(liste)); 
        }
        
    }
    
    @GET
    @Path("/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("token") String token, @PathParam("id") int id) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
           Chat c = chatEJB.getCopy(id);
            
            Gson parser = new Gson();
            return response.generiereAntwort(parser.toJson(c)); 
        }
        
    }
    //Anzahl von Nachrichten etc.
    @GET
    @Path("/info/{chatid}/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatInfo(@PathParam("token") String token, @PathParam("id") int id, @PathParam("chatid") int chatId) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
           Gson parser = new Gson();
            
            Chat c = chatEJB.getCopy(chatId);
           List<Nutzer> nutzerListe = c.getNutzerList();
           
           List<Nutzer> adminListe = c.getAdminList();
           
           Nutzer self = nutzerEJB.getCopyById(id); //Diese Zeile zerstört alles
           
           System.out.println("nutzerliste" + nutzerListe);
           System.out.println("adminListe" + adminListe);
           
           nutzerListe.remove(self);
 
           List<Nutzer> isAdmin = new ArrayList<>();
           List<Nutzer> isNotAdmin = new ArrayList<>();

           
           for(Nutzer nutzer : nutzerListe){
               nutzer.setAdminInGroups(null);
               nutzer.setPasswordhash(null);
               nutzer.setOtherFriendList(null);
               nutzer.setOwnFriendList(null);
               
              if(adminListe.contains(nutzer)){
                  isAdmin.add(nutzer); 
              }
              else{
                  isNotAdmin.add(nutzer);
              }
           }
           
           System.out.println("isAdmin" + isAdmin);
           System.out.println("isNotAdmin" + isNotAdmin);
           
           
            self.setAdminInGroups(null);
            self.setPasswordhash(null);
            self.setOtherFriendList(null);
            self.setOwnFriendList(null);
            self.setChatList(null);
            if(adminListe.contains(self)){
                self.setIsadmin(true);
            }
           
            for(Nutzer n : isAdmin){
                n.setIsadmin(true);
            }
            
            List<Nutzer> returnList = new ArrayList<>();
            returnList.add(self);
            returnList.addAll(isAdmin);
            returnList.addAll(isNotAdmin);
            System.out.println("returnListe" + returnList);
           JsonObject jsonObject = new JsonObject();
           jsonObject.add("nutzerliste", parser.toJsonTree(returnList));
           
           List<Nachricht> nList = nachrichtEJB.getByChatId(chatId);
           int anzahlNachrichten = nList.size();
           int anzahlFotos = 0;
           for(Nachricht n : nList){
               if(n.getFoto() != null){
                   anzahlFotos += 1;
               }
           }
           jsonObject.add("anzahlNachrichten", parser.toJsonTree(anzahlNachrichten));
           jsonObject.add("anzahlFotos", parser.toJsonTree(anzahlFotos));
           jsonObject.add("beschreibung", parser.toJsonTree(chatEJB.getById(chatId).getBeschreibung()));;
           jsonObject.add("profilbild", parser.toJsonTree(chatEJB.getById(chatId).getProfilbild().getBase64()));;
            return response.generiereAntwort(parser.toJson(jsonObject)); 
           
           
//           return response.generiereAntwort("true");
        }
        
    }
    
    
    //wie die eigene Chatliste, aber nur für einen Chat
    @GET
    @Path("/id/{chatid}/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByChatId(@PathParam("chatid") int chatid, @PathParam("nutzerid") int nutzerid, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            try{
                Chat c =  chatEJB.getCopy(chatid);
                int length = 0;

                for(Nutzer n : c.getNutzerList()){
                    n.setChatList(null);
                    length +=1;
                }

                System.out.println(length);
                if(length == 2)
                {
                    List<Nutzer> nutzerList = c.getNutzerList();
                    Nutzer n = nutzerEJB.getCopyById(nutzerid);
                    nutzerList.remove(n);

                    Nutzer andererNutzer = nutzerList.get(0);

                    c.setName(andererNutzer.getBenutzername());
                    c.setProfilbild(andererNutzer.getProfilbild());
                    c.setNutzerList(null);
                    System.out.println(nutzerList);
                }


                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(c));
            }
            catch(EJBTransactionRolledbackException e){
                return response.generiereFehler401("Id nicht vorhanden");
            }
             
        }
        
    }
    //eigene Chatliste
    @GET
    @Path("/nutzerid/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnChatlistByUserId(@PathParam("nutzerid") int nutzerid, @PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            try{
                List<Chat> returnList = new ArrayList<Chat>();

                Nutzer nutzer = nutzerEJB.getCopyById(nutzerid);

                List<Chat> liste = chatEJB.getAllCopy();
                for(Chat c : liste) {
                    if(c.getNutzerList().contains(nutzer))
                    {
                        returnList.add(c);
                    }
                }


                for(Chat c : returnList){
                    try{
                        Nachricht n = nachrichtEJB.getNewest(c.getChatid());
                        c.getLetztenachricht().setSender(n.getSender());
                    }
                    catch(Exception e){
                        
                    }
                    
                    for(Nutzer n : c.getNutzerList()){
                        n.setPasswordhash(null);
                        n.setOtherFriendList(null);
                        n.setOwnFriendList(null);
                    }
                        
                        if(!c.getIsgroup())
                        {
                            System.out.println(c);
                           // c.setAdminList(null);
                            List<Nutzer> nutzerList = c.getNutzerList();
                            Nutzer n = nutzerEJB.getCopyById(nutzerid);
                            nutzerList.remove(n);

                            Nutzer andererNutzer = nutzerList.get(0);

                            c.setName(andererNutzer.getBenutzername());
                            c.setProfilbild(andererNutzer.getProfilbild());
                            c.setBeschreibung(andererNutzer.getInfo());
                            c.setNutzerList(null);
                            System.out.println(nutzerList);
                        }
                        c.setAdminList(null);
                        c.setNutzerList(null);
                    
                }
                
                
                
                List<Chat> toRemove = new ArrayList<>();
                for(Chat c : returnList){
                    if(c.getLetztenachricht() == null){
                        toRemove.add(c);
                    }
                }
                returnList.removeAll(toRemove);
                returnList.sort(new DateSorter());
                returnList.addAll(toRemove);
                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(returnList)); 
            }
            catch(EJBTransactionRolledbackException e){
                return response.generiereFehler401("Id nicht vorhanden");
            }
        }
        
        
    }
    
    
    @POST
    @Path("/createAsGroup/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAsGroup(String jsonStr, @PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            System.out.println(jsonStr);
            Gson parser = new Gson();
        try {
            JsonObject json = parser.fromJson(jsonStr, JsonObject.class);
            Chat neuerChat = parser.fromJson(jsonStr, Chat.class);
            neuerChat.setIsgroup(true);
            chatEJB.createChat(neuerChat);
            int eigeneId = parser.fromJson((json.get("eigeneId")), Integer.class);
            System.out.println(eigeneId);
            
            try {
            Thread.sleep(200);
            } catch (Exception e) {}
            
            JsonArray arr = json.getAsJsonArray("benutzernamen");
            for(int i = 0; i < arr.size() ; i++){
                System.out.println(arr.get(i).getAsString());
                Nutzer addedUser = nutzerEJB.getByUsername(arr.get(i).getAsString());
                nutzerEJB.fuegeChatHinzu(neuerChat, addedUser);
                chatEJB.fuegeNutzerHinzu(neuerChat, addedUser);
                
            }
            Nutzer self = nutzerEJB.getById(eigeneId);
            nutzerEJB.fuegeChatHinzu(neuerChat, self);
            chatEJB.fuegeNutzerHinzu(neuerChat, self);
            chatEJB.addAdmin(neuerChat, self);
            return response.generiereAntwort("true");
        }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json falsch");
        }
        }
        
    }
    
    @POST
    @Path("/createAsChat/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response createAsChat(String jsonStr, @PathParam("token") String token) {
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            System.out.println(jsonStr);
            Gson parser = new Gson();
            try {
                
                JsonObject jsonTP = parser.fromJson(jsonStr, JsonObject.class);
                String username = parser.fromJson((jsonTP.get("benutzername")), String.class);
                String date = parser.fromJson((jsonTP.get("erstelldatum")), String.class);
                int eigeneId = parser.fromJson((jsonTP.get("eigeneId")), Integer.class);
                
                Chat neuerChat = new Chat();
                neuerChat.setIsgroup(false);
                neuerChat.setErstelldatum(date);
                
                
                Nutzer self = nutzerEJB.getCopyById(eigeneId);
                Nutzer other = nutzerEJB.getCopyByUsername(username);
                chatEJB.createChat(neuerChat);
                
                nutzerEJB.fuegeChatHinzu(neuerChat, self);
                nutzerEJB.fuegeChatHinzu(neuerChat, other);
                
                boolean test = true;
                
                for(Chat c : chatEJB.getAll()){
                    List<Nutzer> nutzerList = new ArrayList();
                    for(Nutzer n : c.getNutzerList()){
                        nutzerList.add(n);
                    }
                    if(nutzerList.size() == 2 && nutzerList.contains(self) && nutzerList.contains(other)){
                        test = false;
                    }
                }
                
                if(test){
                    chatEJB.fuegeNutzerHinzu(neuerChat, self);
                    chatEJB.fuegeNutzerHinzu(neuerChat, other);
                    
                    return response.generiereAntwort("true");
                }
                else{
                    chatEJB.delete(neuerChat);
                    return response.generiereFehler406("schon vorhanden");
                }
            }
            catch(JsonSyntaxException e) {
                 return response.generiereFehler401("Json falsch");
                }
            catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("ID oder Benutzername nicht vorhanden");
            }
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
    @Path("/takepart/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response takePart(String jsonStr, @PathParam("token") String token){
        
        if(!verify(token)){
            return response.generiereFehler401("Kein gültiges Token");
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonTP = parser.fromJson(jsonStr, JsonObject.class);

                int chatid = parser.fromJson((jsonTP.get("chatid")), Integer.class);
                Chat c = chatEJB.getById(chatid);

                String username = parser.fromJson((jsonTP.get("benutzername")), String.class);
                Nutzer addedUser = nutzerEJB.getCopyByUsername(username);

                if(!c.getNutzerList().contains(addedUser))
                {
                System.out.println("ChatWs fuegeChatHinzu");
                nutzerEJB.fuegeChatHinzu(c, addedUser);

                System.out.println("Chatws fuegeNutzerhinzu");
                chatEJB.fuegeNutzerHinzu(c, addedUser);

                return response.generiereAntwort("true");
                }
                else{
                    return response.generiereFehler406("Nutzer schon hinzugefügt");
                }
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("JsonSyntaxException" + e);
            }
            catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("ID oder Benutzername nicht vorhanden");
            }
        }
        
    }
    
    @POST
    @Path("/zuAdmin/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response macheZuAdmin(@PathParam("token") String token, String jsonStr) {
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            
            Gson parser = new Gson();
            
            JsonObject jsonObject = parser.fromJson(jsonStr, JsonObject.class);
            int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class);
            int jsonChatId = parser.fromJson((jsonObject.get("chatId")), Integer.class);
            String jsonUsername = parser.fromJson((jsonObject.get("andererBenutzername")), String.class);
            
            Nutzer self = nutzerEJB.getById(jsonId);
            Nutzer other = nutzerEJB.getByUsername(jsonUsername);
            Chat c = chatEJB.getById(jsonChatId);
            
            if(c.getAdminList().contains(self)){
                if(c.getAdminList().contains(other)){
                    return response.generiereFehler406("Bereits Admin");
                }
                else{
                    chatEJB.addAdmin(c, other);
                    return response.generiereAntwort("true");
                }
                
            }
            else {
               return response.generiereFehler406("Kein Admin");
            }
            
            
            
        }
    
    }
    
    @POST
    @Path("/entferneAdmin/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response entferneAdmin(@PathParam("token") String token, String jsonStr) {
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            
            Gson parser = new Gson();
            
            JsonObject jsonObject = parser.fromJson(jsonStr, JsonObject.class);
            int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class);
            int jsonChatId = parser.fromJson((jsonObject.get("chatId")), Integer.class);
            String jsonUsername = parser.fromJson((jsonObject.get("andererBenutzername")), String.class);
            
            Nutzer self = nutzerEJB.getById(jsonId);
            Nutzer other = nutzerEJB.getByUsername(jsonUsername);
            Chat c = chatEJB.getById(jsonChatId);
            if(c.getAdminList().contains(self)){
                if(c.getAdminList().size() == 1){
                    return response.generiereFehler406("Einziger Admin");
                }
                if(!c.getAdminList().contains(other)){
                    return response.generiereFehler406("Bereits kein Admin");
                }
                else{
                    chatEJB.deleteAdmin(c, other);
                    return response.generiereAntwort("true");
                }
                
            }
            else {
               return response.generiereFehler406("Kein Admin");
            }
            
            
            
        }
    
    }
    
    @POST
    @Path("/setzeProfilbild/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response setzeProfilbild(@PathParam("token") String token, String jsonStr) {
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
            
            JsonObject jsonObject = parser.fromJson(jsonStr, JsonObject.class);
            int jsonId = parser.fromJson((jsonObject.get("id")), Integer.class);
            String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
            
            Foto f = new Foto();
            f.setBase64(jsonPic);
            fotoEJB.add(f);
            
            Chat chat = chatEJB.getById(jsonId);
            
            Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
            
            chat.setProfilbild(fotoInDB);
            
            return response.generiereAntwort("true");
        }
        
        
    }
    
}
