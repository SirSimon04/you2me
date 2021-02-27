/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.NutzerEJB;
import Entity.Chat;
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
    
    /*
    Es folgen alle Methoden, die sich auf die Klasse Nutzer beziehen
    */
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(){
         List<Nutzer> liste = nutzerEJB.getAllCopy();
        for(Nutzer n : liste) {
            for(Chat c : n.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
        }
        Gson parser = new Gson();
        return parser.toJson(liste);
    }
    
    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") int id) {
        Nutzer n =  nutzerEJB.getCopyById(id);
        for(Chat c : n.getChatList()){
            c.setNutzerList(null);
        }
        Gson parser = new Gson();
        return parser.toJson(n);
    }
    
    @GET
    @Path("/benutzername/{benutzername}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByUsername(@PathParam("benutzername") String benutzername){
        Nutzer n = nutzerEJB.getCopyByUsername(benutzername);
        for(Chat c : n.getChatList()){
            c.setNutzerList(null);
        }
        Gson parser = new Gson();
        return parser.toJson(n);
    }
    
    @GET
    @Path("/chatteilnehmer/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByChatId(@PathParam("id") int id) {
        List<Nutzer> liste = nutzerEJB.getCopyByChatId(id);
        for(Nutzer n : liste) {
            for(Chat c : n.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
        }
        Gson parser = new Gson();
        return parser.toJson(liste);
    }
    
    
    
    @GET
    @Path("/getUsernameById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsernameById(@PathParam("id") int id){
        Gson parser = new Gson();
        Nutzer n = new Nutzer();
        n = nutzerEJB.getCopyById(id);
        
        Nutzer nutzer = new Nutzer();
        nutzer.setBenutzername(n.getBenutzername());  //nötig, damit nur der Benutzername bekannt ist
        
        return parser.toJson(nutzer);
        
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
            
            //getting the Nutzer with the name gotten before
            Nutzer dbNutzer = nutzerEJB.getCopyByUsername(jsonUsername);
            
            for(Chat c : dbNutzer.getChatList()) {
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
            
            if(dbNutzer.getPasswort().equals(jsonPasswort))
            {
                return parser.toJson(dbNutzer);
            }
            else 
            {
                return "PW falsch";
            }
            
            
            
        }
            catch(JsonSyntaxException e) {
                System.out.println("catch");
                return "Json falsch";
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
            nutzerEJB.add(neuerNutzer);
            return "true";
            
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
    @Path("/delete/{id}")
    @Produces(MediaType.TEXT_PLAIN)
        public boolean delete(@PathParam("id") int id) {
        return nutzerEJB.delete(id);
    }
        
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public boolean update(String jsonStr) {
        Gson parser = new Gson();
        Nutzer aktNutzer = parser.fromJson(jsonStr, Nutzer.class);
        return nutzerEJB.update(aktNutzer);
    }
    
    
     
}