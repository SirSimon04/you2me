/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.NutzerEJB;
import Entity.Chat;
import Entity.Nutzer;
import Utilities.Hasher;
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
import com.google.gson.JsonObject;
import java.util.HashSet;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;


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
    @EJB
    private Hasher hasher;
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
    
    /*
    Es folgen alle Methoden, die sich auf die Klasse Nutzer beziehen
    */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{token}")
    public String getAll(@PathParam("token") String token){
        if(!verify(token))
        {
            return "Kein gültiges Token.";
        }
        else 
        {
           List<Nutzer> liste = nutzerEJB.getAllCopy();
            Gson parser = new Gson();
            return parser.toJson(liste);
        }
         
    }
    
    @GET
    @Path("/id/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)) {
            return "Kein gültiges Token.";
        }
        else{
            Nutzer n =  nutzerEJB.getCopyById(id);
            Gson parser = new Gson();
            return parser.toJson(n);
        }
        
    }
    
    @GET
    @Path("/benutzername/{benutzername}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByUsername(@PathParam("benutzername") String benutzername, @PathParam("token") String token){
        if(!verify(token)){
            return "Kein gültiges Token.";
        }
        else{
            try{
            Nutzer n = nutzerEJB.getCopyByUsername(benutzername);
            
            Gson parser = new Gson();
            return parser.toJson(n);
            }
            catch(EJBTransactionRolledbackException e) {
                return "Benutzername nicht vorhanden";
            }
        }
        
    }
    
    @GET
    @Path("/chatteilnehmer/id/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByChatId(@PathParam("id") int id, @PathParam("token") String token) {
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else{
            List<Nutzer> liste = nutzerEJB.getCopyByChatId(id);
        for(Nutzer n : liste) {
            for(Chat c : n.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
            for(Nutzer nutzer : n.getOwnFriendList()) {
                nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
            }
            for(Nutzer nutzer : n.getOtherFriendList()) {
                nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
            }
        }
        Gson parser = new Gson();
        return parser.toJson(liste);
        }
    }
    
    
    
    @GET
    @Path("/getUsernameById/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsernameById(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else{
            Gson parser = new Gson();
            Nutzer n = new Nutzer();
            n = nutzerEJB.getCopyById(id);

            Nutzer nutzer = new Nutzer();
            nutzer.setBenutzername(n.getBenutzername());  //nötig, damit nur der Benutzername bekannt ist

            return parser.toJson(nutzer);
        }
        
        
    }
    
    @GET
    @Path("/testtoken/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public String testToken(@PathParam("token") String token) {
        if(!verify(token)) {
            return "Kein gültiges Token.";
        }
        else {
            String name = tokenizer.getUser(token);
            return "Herzlich willkommen " + name +". Dein Token ist noch gültig.";
        }
    }
    
            
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(String jsonStr) {
       
        Gson parser = new Gson();
        try {
            //getting the name of the body
            JsonObject loginUser = parser.fromJson(jsonStr, JsonObject.class);
            String jsonUsername = parser.fromJson((loginUser.get("benutzername")), String.class);
            String jsonPasswort = parser.fromJson((loginUser.get("passwort")), String.class);
            
            Nutzer dbNutzer = nutzerEJB.getCopyByUsername(jsonUsername);
            
            for(Chat c : dbNutzer.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
            
            if(hasher.convertStringToHash(jsonPasswort).equals(dbNutzer.getPasswordhash()))
            {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", dbNutzer.getId());
                jsonObject.addProperty("benutzername", dbNutzer.getBenutzername());
                jsonObject.addProperty("email", dbNutzer.getEmail());
                jsonObject.addProperty("token", tokenizer.createNewToken(dbNutzer.getBenutzername()));
                return parser.toJson(jsonObject);
                //return "  {\"token\": \"" + tokenizer.createNewToken(dbNutzer.getBenutzername()) + "\" }  ";
            }
            else 
            {
                return "PW falsch";
            }
            
            
            
        }
            catch(JsonSyntaxException e) {
                return "Json falsch";
            }

        
    }
    
    @POST
    @Path("/fuegeFreundHinzu/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String takePart(@PathParam("token") String token, String jsonStr){
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else {
            Gson parser = new Gson();
        
            try{
                JsonObject jsonO = parser.fromJson(jsonStr, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyById(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsername(andererName);

                if(!self.getOwnFriendList().contains(other)) {
                    nutzerEJB.fuegeFreundHinzu(self, other);
                    return "true";
                }
                else {
                    return "Bereits befreundet";
                }
            }
            catch(JsonSyntaxException e) {
                return "JsonSyntaxException" + e;
            }
        }
        
        
        
    }
    
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String create(String jsonStr) {
        
            System.out.println(jsonStr);
            Gson parser = new Gson();

            try {
                Nutzer neuerNutzer = parser.fromJson(jsonStr, Nutzer.class);
                neuerNutzer.setPasswordhash(hasher.convertStringToHash(neuerNutzer.getPasswordhash()));

                nutzerEJB.add(neuerNutzer);
                return "{\"token\": \"" + tokenizer.createNewToken(neuerNutzer.getBenutzername()) + "\"}";

                //Nutzer neuerNutzer = parser.fromJson(jsonStr, Nutzer.class);
                /*

                System.out.println("1. try");
                JsonObject loginUser = parser.fromJson(jsonStr, JsonObject.class);
                String jsonUsername = parser.fromJson((loginUser.get("benutzername")), String.class);

                try {
                    System.out.println("2. try");
                    Nutzer usernameNutzer = nutzerEJB.getCopyByUsername(jsonUsername);

                    if(usernameNutzer.getBenutzername().equals(jsonUsername)){
                        return "Benutzername bereits vergeben";
                    }
                    }
                    catch(NoResultException e) {
                        System.out.println("4. exception");
                        //return "4";
                    }
                    catch(EJBTransactionRolledbackException e){
                        System.out.println("5. exception");
                        //return "5";
                    }
                    catch(TransactionRolledbackLocalException e){
                        System.out.println("6. exception");
                        //return "6";
                    }
                    catch(EJBException e){
                        System.out.println("6.5 exception");
                        //return "7";
                    }

                try {
                    Nutzer neuerNutzer = parser.fromJson(jsonStr, Nutzer.class);
                    nutzerEJB.add(neuerNutzer);
                    return "true";

                }
                    catch(NoResultException e) {
                        System.out.println("1. exception");
                        return "1";
                    }
                    catch(EJBTransactionRolledbackException e){
                        System.out.println("2. exception");
                        return "2";
                    }
                    catch(TransactionRolledbackLocalException e){
                        System.out.println("3. exception");
                        return "3";
                    }
                    catch(EJBException e){
                        System.out.println("3.5 exception");
                        return "3.5";
                    }


                */




                //return "wtf";

            }
                catch(JsonSyntaxException e) {
                return "false";
            }
            //return "";
        
        
        
    }
    
    @DELETE
    @Path("/delete/{id}/{token}")
    @Produces(MediaType.TEXT_PLAIN)
        public String delete(@PathParam("id") int id, @PathParam("token") String token) {
            if(!verify(token)){
            return "Kein gültiges Token";
        }
            else {
                if(nutzerEJB.delete(id)){
                    return "true";
                }
                else{
                    return "false";
               
                }
            }
    }
        
    @PUT
    @Path("/update/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String update(@PathParam("token") String token, String jsonStr) {
        if(!verify(token)){
            return "Kein gültiges Token";
        }
        else {
            Gson parser = new Gson();
            Nutzer aktNutzer = parser.fromJson(jsonStr, Nutzer.class);
            if(nutzerEJB.update(aktNutzer)){
                return "true";
            }
            else{
                return "false";
            }
        }
        
    }
    
    //Folgende Methoden waren nur ein Token/Hash test
    @GET
    @Path("/createTest")
    @Produces(MediaType.APPLICATION_JSON)
    public String createTest(){
        String NAME = "Mustermann";
        String PASSWORD = "geheim";
        Nutzer nutzer = new Nutzer();
        nutzer.setBenutzername(NAME);
        nutzer.setPasswordhash(hasher.convertStringToHash(PASSWORD));
        nutzer.setEmail("abc@gmail.com");
        nutzerEJB.add(nutzer);
        // Überprüfen, ob es funktioniert hat:
        Gson parser = new Gson();
        Nutzer ergebnisAusDB = nutzerEJB.getCopyByUsername("Mustermann");
        return parser.toJson(ergebnisAusDB);
        
    }
    
    
    
    @GET
    @Path("/loginTest/{pw}")
    @Produces(MediaType.TEXT_PLAIN)
    public String loginTest(@PathParam("pw") String pw) {
        String NAME ="Mustermann";
        String PASSWORD = pw;
        Nutzer acc = nutzerEJB.getCopyByUsername(NAME);
        System.out.println(acc.getPasswordhash());
        System.out.println(hasher.convertStringToHash(PASSWORD));
        if(hasher.convertStringToHash(PASSWORD).equals(acc.getPasswordhash())) {
            return "{\"token\": \"" + tokenizer.createNewToken(NAME) + "\"}";
        }
        else {
            return "Benutzername oder Passwort falsch";
        }
    }
    
    @GET
    @Path("/topsecret/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public String testToken2(@PathParam("token") String token) {
        if(tokenizer.verifyToken(token).equals("")) {
            return "Kein gültiges Token.";
        }
        else {
            String name = tokenizer.getUser(token);
            return "Herzlich willkommen " + name +". Dein Token ist noch gültig.";
        }
    }
     
}