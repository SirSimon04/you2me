/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.BlacklistEJB;
import EJB.FotoEJB;
import EJB.NutzerEJB;
import Entity.Blacklist;
import Entity.Chat;
import Entity.Foto;
import Entity.Nutzer;
import Utilities.Hasher;
import Utilities.Tokenizer;
import Utilities.Antwort;
import Utilities.Mail;
import Utilities.Emailservice;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;



/**
 *<h1>Der Webserver für die Datenverarbeitung, bezogen auf die Nutzer</h1>
 * <p>Diese Klasse beinhaltet alle Methoden des Webservers bezogen auf das Objekt des Nutzers
 * für das Bearbeiten und Ausgeben der Daten und stellt damit die Schnittstelle mit dem Frontend dar</p>
 */
@Path("/nutzer")
@Stateless
@LocalBean
public class NutzerWS {
    
    @EJB
    private NutzerEJB nutzerEJB;
    @EJB
    private Hasher hasher;
    @EJB
    private Tokenizer tokenizer;
    @EJB
    private FotoEJB fotoEJB;
    @EJB
    private BlacklistEJB blacklistEJB;
    
    @EJB
    private Emailservice email;
    
    private Antwort response = new Antwort();
    
    private Mail mail = new Mail();
    
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
            else if(true)
            {
                List<Blacklist> bl = blacklistEJB.getAllBlacklisted();
                for(Blacklist b : bl)
                {
                    if(b.getToken().equals(token))
                    {
                        return false;   
                    }
                }
            }
            
            return true;
            
        }
        else {
            return true;
        }
        
    }
    
    /*
    Es folgen alle Methoden, die sich auf die Klasse Nutzer beziehen
    */
    
    /**
     * Diese Methode gibt alle Nutzer zurück.
     * @param token Das Webtoken
     * @return Liste mit allen Nutzern
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{token}")
    public Response getAll(@PathParam("token") String token){
        if(!verify(token))
        {
            return response.generiereFehler401("Ungültiges Token");
        }
        else 
        {
           List<Nutzer> liste = nutzerEJB.getAllCopy();
            Gson parser = new Gson();
            return response.generiereAntwort(parser.toJson(liste));
            
            
        }
         
    }
    
    /**
     * Diese Methode gibt einen Nutzer anhand seiner Id zurück.
     * @param id Die Id des Nutzers
     * @param token Das Webtoken
     * @return Der Nutzer
     */
    @GET
    @Path("/id/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)) {
            return response.generiereFehler401("Ungültiges Token");
        }
        else{
            try{
                Nutzer n =  nutzerEJB.getCopyById(id);
                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(n));
            }
            catch(EJBTransactionRolledbackException e) {
                return response.generiereFehler406("Id nicht vorhanden");
            }
            
        }
        
    }
    /**
     * Diese Methode gibt einen Nutzer anhand seines Benutzernamens zurück.
     * @param benutzername Der Benutzername
     * @param token Das Webtoken
     * @return Der Nutzer
     */
    @GET
    @Path("/benutzername/{benutzername}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByUsername(@PathParam("benutzername") String benutzername, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else{
            try{
                Nutzer n = nutzerEJB.getCopyByUsername(benutzername);
                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(n));
            }
            catch(EJBTransactionRolledbackException e) {
                return response.generiereFehler406("Benutzername nicht vorhanden");
            }
        }
        
    }
    
    /**
     * Diese Methode gibt den Benutzernamen eines Nutzers anhand seiner Id zurück.
     * @param id Die Id des Nutzers
     * @param token Das Webtoken
     * @return Der Benutzername
     */
    @GET
    @Path("/getUsernameById/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsernameById(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else{
            try{
                Gson parser = new Gson();
                Nutzer n = new Nutzer();
                n = nutzerEJB.getCopyById(id);

                Nutzer nutzer = new Nutzer();
                nutzer.setBenutzername(n.getBenutzername());  //nötig, damit nur der Benutzername bekannt ist

                return response.generiereAntwort(parser.toJson(nutzer));
            }
            catch(EJBTransactionRolledbackException e) {
                return response.generiereFehler406("Id nicht vorhanden");
            }
        }
        
        
    }
   
    
    /**
     * Diese Methoden gibt die Listen eines Nutzers bezogen auf die Freunde zurück. Dabei gibt es jeweils eine für 
     * Freunde, austehende Freundschaftanfragen und eingehende Freundschaftsanfragen.
     * @param id Die Id des Nutzers
     * @param token Das Webtoken
     * @return Die drei Freundeslisten
     */
    @GET
    @Path("/getFriendList/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriendList(@PathParam("id") int id, @PathParam("token") String token){
        
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else{
            Gson parser = new Gson();
            
            JsonObject jsonObject = new JsonObject();
        
            Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);
            
            List<Nutzer> ownFriendListDB = self.getOwnFriendList();
            for(Nutzer n : ownFriendListDB){
                n.setAdminInGroups(null);
                n.setChatList(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
            }
            
            List<Nutzer> otherFriendListDB = self.getOtherFriendList();
            for(Nutzer n : otherFriendListDB){
                n.setAdminInGroups(null);
                n.setChatList(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
            }
            List<Nutzer> friends = new ArrayList<>();
            List<Nutzer> friendRequests = new ArrayList<>();
            List<Nutzer> pendingRequests = new ArrayList<>();
            
            for(Nutzer n : ownFriendListDB){
                if(otherFriendListDB.contains(n)){
                    friends.add(n);
                }
                else{
                    pendingRequests.add(n);
                }
            }
            
            for(Nutzer n : otherFriendListDB){
                if(!ownFriendListDB.contains(n)){
                    friendRequests.add(n);
                }
            }
            
            jsonObject.add("friendList", parser.toJsonTree(friends));
            jsonObject.add("friendRequests", parser.toJsonTree(friendRequests));
            jsonObject.add("pendingRequests", parser.toJsonTree(pendingRequests));
            
            
            return response.generiereAntwort(parser.toJson(jsonObject)); 
        }
        
    }
    
    @POST
    @Path("/testPost")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendeFreundesAnfrage(String Daten){
    
        System.out.println(Daten);
        Gson parser = new Gson();
        return response.generiereAntwort(parser.toJson("Success"));
    }
    /**
     * Diese Methode sendet eine Freundschaftsanfrage an einen anderen Nutzer und kann auch dafür genutzt werden, Freundschaftsanfragen anzunehmen.
     * @param token Das Webtoken
     * @param Daten Die Informationen für den eigenen und anderen Nutzer 
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/freundesAnfrage/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendeFreundesAnfrage(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsername(andererName);

                if(!self.getOwnFriendList().contains(other)) {
                    nutzerEJB.fuegeFreundHinzu(self, other);
                    return response.generiereAntwort("true");
                }
                else {
                    return response.generiereFehler406("Bereits befreundet");
                }
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
        }
    }
    
    /**
     * Diese Methode löscht einen Freund und kann auch dafür genutzt werden, Freundschaftsanfragen abzulehnen.
     * @param token Das Webtoken
     * @param Daten Die Informationen für den eigenen und anderen Nutzer 
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/loescheFreund/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response loescheFreund(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsernameListsNotNull(andererName);

                    nutzerEJB.loescheFreund(self, other);
                    nutzerEJB.loescheFreund(other, self);
                    self.getOwnFriendList().remove(other);
                    self.getOtherFriendList().remove(other);
                    
                    other.getOwnFriendList().remove(self);
                    other.getOtherFriendList().remove(self);
                    
                    return response.generiereAntwort("true");      
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
        }
    }
    
    @POST
    @Path("/block/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response block(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsernameListsNotNull(andererName);

                    nutzerEJB.block(self, other);
                    self.getHatBlockiert().add(other);
                    other.getBlockiertVon().add(self);
                    
                    return response.generiereAntwort("true");      
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
        }
    }
    
    @POST
    @Path("/unblock/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response unblock(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsernameListsNotNull(andererName);

                    nutzerEJB.unblock(self, other);
                    self.getHatBlockiert().remove(other);
                    other.getBlockiertVon().remove(self);
                    
                    return response.generiereAntwort("true");      
            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
        }
    }
    
    

    
    /**
     * Diese Methode realisiert den Login in das System und erstellt einen Token.
     * @param Daten Die Anmeldedaten des Nutzers, bestehend aus Nutzername und Passwort
     * @return Das Responseobjekt mit dem Webtoken und den eigenen Nutzerinformationen
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.TEXT_PLAIN) 
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String Daten) {
//        Response r = response.generiereAntwort(Daten);
        Gson parser = new Gson();
        try {
            //getting the name of the body
            JsonObject loginUser = parser.fromJson(Daten, JsonObject.class);
            String jsonUsername = parser.fromJson((loginUser.get("benutzername")), String.class);
            String jsonPasswort = parser.fromJson((loginUser.get("passwort")), String.class);
            
            Nutzer dbNutzer = nutzerEJB.getCopyByUsernameListsNotNull(jsonUsername);
            
            for(Chat c : dbNutzer.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
            
            if(hasher.checkPassword(jsonPasswort).equals(dbNutzer.getPasswordhash()))
            {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", dbNutzer.getId());
                jsonObject.addProperty("benutzername", dbNutzer.getBenutzername());
                jsonObject.addProperty("email", dbNutzer.getEmail());
                String token = tokenizer.createNewToken(dbNutzer.getBenutzername());
                
                List<Blacklist> bl = blacklistEJB.getAllBlacklisted();
                for(Blacklist b : bl){
                    if(b.getToken().equals(token)){
                        blacklistEJB.deleteToken(token);
                    }
                }
                jsonObject.addProperty("token", token);
                
                //return parser.toJson(jsonObject);
                return response.generiereAntwort(parser.toJson(jsonObject));
                //return "  {\"token\": \"" + tokenizer.createNewToken(dbNutzer.getBenutzername()) + "\" }  ";
//                return response.generiereAntwort("test");
            }
            else 
            {
                return response.generiereFehler406("PW falsch");
            }
            
            
            
        }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
            catch(EJBTransactionRolledbackException e) {
                return response.generiereFehler406(e.toString());
            }
    }
    
    /**
     * Diese Methode realisiert den Logout aus dem System.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/logout/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(@PathParam("token") String token) {
           if(!verify(token)){
            return response.generiereAntwort("Bereits ausgeloggt");
        }
        else {
            Gson parser = new Gson();
            try{
                Blacklist bl = new Blacklist();
                bl.setToken(token);

                Date date = new Date();
                bl.setTimestamp(date);

                blacklistEJB.logOut(bl);
                
                return response.generiereAntwort("true");

            }
            catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json wrong");
            }
            catch(EJBTransactionRolledbackException e) {
                return response.generiereFehler406("kp");
            }

        }
            
    }
    
    
    /**
     * Diese Methode fügt einen Nutzer in das System ein.
     * @param Daten
     * @return Das Responseobjekt mit den Nutzerinformationen und dem Webtoken
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response create(String Daten) {
        
            System.out.println(Daten);
            Gson parser = new Gson();

            try {
                
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            String neuerNutzername = parser.fromJson((jsonObject.get("benutzername")), String.class);
            String neueEmail = parser.fromJson((jsonObject.get("email")), String.class);
            String neuerVorname = parser.fromJson((jsonObject.get("vorname")), String.class);
            String neuerNachname = parser.fromJson((jsonObject.get("nachname")), String.class);
            String neuesPasswort = parser.fromJson((jsonObject.get("passwort")), String.class);
            String neueHandynummer = parser.fromJson((jsonObject.get("handynummer")), String.class);
            String neueInfo = parser.fromJson((jsonObject.get("info")), String.class);
            
            Hasher hasher = new Hasher();
           
            Nutzer neuerNutzer = new Nutzer();
            
            Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername); 

            if(UserNameIsFree == null){ 
//                System.out.println("Username free");
                neuerNutzer.setBenutzername(neuerNutzername);    
            }
            else{
//                System.out.println("Username not free");
                return response.generiereFehler500("Benutzername bereits vergeben");
            }
            
            Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail); 
            
            if(EmailIsFree == null){ 
                neuerNutzer.setEmail(neueEmail);    
            }
            else{
                return response.generiereFehler500("Email bereits vergeben");
            }
            
            neuerNutzer.setPasswordhash(hasher.checkPassword(neuesPasswort));
            
            if(neuerVorname != null){
                neuerNutzer.setVorname(neuerVorname);    
            }

            if(neuerVorname != null){    
                neuerNutzer.setNachname(neuerNachname);    
            }

            if(neueHandynummer != null){
                neuerNutzer.setHandynummer(neueHandynummer);
            }
 
            if(neueInfo != null){
                neuerNutzer.setInfo(neueInfo);
            }


            nutzerEJB.add(neuerNutzer);
            Nutzer nutzerInDbB = nutzerEJB.getByUsername(neuerNutzername);
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("id", nutzerInDbB.getId());
            returnObject.addProperty("benutzername", nutzerInDbB.getBenutzername());
            returnObject.addProperty("email", nutzerInDbB.getEmail());
            returnObject.addProperty("token", tokenizer.createNewToken(nutzerInDbB.getBenutzername()));

            return response.generiereAntwort(parser.toJson(returnObject));


            }
                catch(JsonSyntaxException e) {
                return response.generiereFehler406("Json falsch");
            }
            //return "";
        
    }
    
    /**
     * Diese Methode ändert das Profilbild des Nutzers.
     * @param token Das Webtoken
     * @param Daten Die eigene Id und das Foto
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/setzeProfilbild/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response setzeProfilbild(@PathParam("token") String token, String Daten) {
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
            
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            int jsonId = parser.fromJson((jsonObject.get("id")), Integer.class);
            String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
            
            Foto f = new Foto();
            f.setBase64(jsonPic);
            fotoEJB.add(f);
            
            Nutzer self = nutzerEJB.getById(jsonId);
            
            if(self != null){
                Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
            
                self.setProfilbild(fotoInDB);

                return response.generiereAntwort("true");
            }
            else {
                return response.generiereFehler406("ID nicht vorhanden");
            }
            
        }
        
        
    }
    
    /**
     * Diese Methode löscht den eigenen Nutzer, dabei wird überprüft, ob die Person, von der die Anfrage kam, gleich dem zu löschenden Nutzer ist.
     * @param id Die eigene ID
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @DELETE
    @Path("/delete/{id}/{token}")
    @Produces(MediaType.TEXT_PLAIN)
        public Response delete(@PathParam("id") int id, @PathParam("token") String token) {
            if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
            else {
                String name = tokenizer.getUser(token);
                if(nutzerEJB.getByUsername(name).equals(nutzerEJB.getById(id))){
                    
                
                
                
                    if(nutzerEJB.delete(id)){
                        Blacklist bl = new Blacklist();
                        bl.setToken(token);

                        Date date = new Date();
                        bl.setTimestamp(date);
                        blacklistEJB.logOut(bl);
                        return response.generiereAntwort("true");
                    }
                    else{
                        return response.generiereFehler500("Fehler beim Löschen");

                    }
                    
                }
                else{
                    return response.generiereFehler406("Du hast nicht die nötige Berechtigung");
                }
            }
    }
     
        
    //funktioniert nur bei eingeschaltetem Tokenizer
    /**
     * Diese Methode aktualisiert die Nutzerinformationen.
     * @param token Das Webtoken
     * @param Daten Die zu aktualisierenden Nutzerdaten
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @PUT
    @Path("/update/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response update(@PathParam("token") String token, String Daten) {
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }
        else {
            Gson parser = new Gson();
            
            String name = tokenizer.getUser(token); //get the Name of the User from the Token
            
            
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            int eigeneId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class);
            String altesPasswort = parser.fromJson((jsonObject.get("altesPasswort")), String.class);
            String neuerNutzername = parser.fromJson((jsonObject.get("benutzername")), String.class);
            String neueEmail = parser.fromJson((jsonObject.get("email")), String.class);
            String neuerVorname = parser.fromJson((jsonObject.get("vorname")), String.class);
            String neuerNachname = parser.fromJson((jsonObject.get("nachname")), String.class);
            String neuesPasswort = parser.fromJson((jsonObject.get("passwort")), String.class);
            String neueHandynummer = parser.fromJson((jsonObject.get("handynummer")), String.class);
            String neueInfo = parser.fromJson((jsonObject.get("info")), String.class);
            
            Nutzer nutzerInDB = nutzerEJB.getById(eigeneId);
            Hasher hasher = new Hasher();
            if(nutzerInDB.getPasswordhash().equals(hasher.checkPassword(altesPasswort))){
            
                if(nutzerInDB != null){

                    if(neuerVorname != null){
                        nutzerInDB.setVorname(neuerVorname);    
                    }

                    if(neuerVorname != null){    
                        nutzerInDB.setNachname(neuerNachname);    
                    }

                    if(neuesPasswort != null){
                        nutzerInDB.setPasswordhash(hasher.checkPassword(neuesPasswort));
                    }

                    if(neueHandynummer != null){
                        nutzerInDB.setHandynummer(neueHandynummer);
                    }

                    if(neueInfo != null){
                        nutzerInDB.setInfo(neueInfo);
                    }

                    if(neuerNutzername != null){
                        Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername); 

                        if(UserNameIsFree == null){ 
                            nutzerInDB.setBenutzername(neuerNutzername);    
                        }
                        else{
                            return response.generiereFehler500("Benutzername bereits vergeben");
                        }
                    }

                    if(neueEmail != null){
                        Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail); 

                        if(EmailIsFree == null){ 
                            nutzerInDB.setEmail(neueEmail);    
                        }
                        else{
                            return response.generiereFehler500("Benutzername bereits vergeben");
                        }
                    }




                        return response.generiereAntwort(tokenizer.createNewToken(neuerNutzername));

                }
                else{
                    return response.generiereFehler401("Id nicht vorhanden");
                }
            
            }
            else{
                return response.generiereFehler406("PW falsch");
            }
            
            
        }
        
    }
   
     
}
