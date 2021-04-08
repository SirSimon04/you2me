/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebService;

import EJB.BlacklistEJB;
import EJB.ChatEJB;
import EJB.FotoEJB;
import EJB.NachrichtEJB;
import EJB.NutzerEJB;
import EJB.SettingsEJB;
import Entity.Blacklist;
import Entity.Chat;
import Entity.Foto;
import Entity.Nachricht;
import Entity.Nutzer;
import Entity.Setting;
import Utilities.Hasher;
import Utilities.Tokenizer;
import Utilities.Antwort;
import Utilities.Mail;
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
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import javax.ejb.EJBException;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.TransactionRolledbackLocalException;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import static java.lang.Math.toIntExact;
import java.util.Comparator;

/**
 * <h1>Der Webserver für die Datenverarbeitung, bezogen auf die Nutzer</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden des Webservers bezogen auf das Objekt
 * des Nutzers für das Bearbeiten und Ausgeben der Daten und stellt damit die
 * Schnittstelle mit dem Frontend dar</p>
 */
@Path("/nutzer")
@Stateless
@LocalBean
public class NutzerWS{

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
    private SettingsEJB settingsEJB;
    @EJB
    private NachrichtEJB nachrichtEJB;
    @EJB
    private ChatEJB chatEJB;

    private Antwort response = new Antwort();

    private Mail mail = new Mail();

    /**
     * Diese Methode verifiziert einen Token.
     *
     * @param token Das Webtoken
     * @return Boolean, ob der Token akzeptiert wurde
     */
    public boolean verify(String token){
        if(tokenizer.isOn()){
            if(tokenizer.verifyToken(token).equals("")){
                return false;
            }else if(true){
                List<Blacklist> bl = blacklistEJB.getAllBlacklisted();
                for(Blacklist b : bl){
                    if(b.getToken().equals(token)){
                        return false;
                    }
                }
            }
            nutzerEJB.setOnline(token);
            nutzerEJB.setOnline(token);
            return true;

        }else{
            return true;
        }

    }

    /**
     * Diese Methode gibt alle Nutzer zurück.
     *
     * @param token Das Webtoken
     * @return Liste mit allen Nutzern
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{token}")
    public Response getAll(@PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            List<Nutzer> liste = nutzerEJB.getAllCopy();
            Gson parser = new Gson();
            return response.generiereAntwort(parser.toJson(liste));

        }

    }

    /**
     * Diese Methode gibt einen Nutzer anhand seiner Id zurück.
     *
     * @param id Die Id des Nutzers
     * @param token Das Webtoken
     * @return Der Nutzer
     */
    @GET
    @Path("/id/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            try{
                Nutzer n = nutzerEJB.getCopyById(id);
                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(n));
            }catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("Id nicht vorhanden");
            }

        }

    }

    /**
     * Diese Methode gibt einen Nutzer anhand seines Benutzernamens zurück.
     *
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
        }else{
            try{
                Nutzer n = nutzerEJB.getCopyByUsername(benutzername);
                Gson parser = new Gson();
                return response.generiereAntwort(parser.toJson(n));
            }catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("Benutzername nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methode gibt den Benutzernamen eines Nutzers anhand seiner Id
     * zurück.
     *
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
        }else{
            try{
                Gson parser = new Gson();
                Nutzer n = new Nutzer();
                n = nutzerEJB.getCopyById(id);

                Nutzer nutzer = new Nutzer();
                nutzer.setBenutzername(n.getBenutzername());  //nötig, damit nur der Benutzername bekannt ist

                return response.generiereAntwort(parser.toJson(nutzer));
            }catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("Id nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methoden gibt die Freundeslisten eines Nutzers zurück. Dabei gibt es jeweils eine für Freunde, austehende
     * Freundschaftanfragen und eingehende Freundschaftsanfragen.
     * Außerdem werden die Freunde, deren Status online ist, in einer
     * weiteren Liste angezeigt.
     *
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
        }else{
            Gson parser = new Gson();

            JsonObject jsonObject = new JsonObject();

            Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);
//            for(Nutzer n : self.getOtherFriendList()){
//                n.set
//            }

            List<Nutzer> ownFriendListDB = self.getOwnFriendList();
            for(Nutzer n : ownFriendListDB){
                n.setAdminInGroups(null);
                n.setPinnedChats(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
            }

            List<Nutzer> otherFriendListDB = self.getOtherFriendList();
            for(Nutzer n : otherFriendListDB){
                n.setAdminInGroups(null);
                n.setPinnedChats(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
            }

            for(Nutzer n : self.getHatBlockiert()){
                n.setAdminInGroups(null);
                n.setPinnedChats(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
            }

            List<Nutzer> friends = new ArrayList<>();
            List<Nutzer> friendRequests = new ArrayList<>();
            List<Nutzer> pendingRequests = new ArrayList<>();
            List<Nutzer> onlineFriends = new ArrayList<>();
            List<Nutzer> blockedUser = self.getHatBlockiert();
            for(Nutzer n : ownFriendListDB){
                if(otherFriendListDB.contains(n)){
                    friends.add(n);
                    if(n.getIsonline()){
                        onlineFriends.add(n);
                    }
                }else{
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
            jsonObject.add("onlineFriends", parser.toJsonTree(onlineFriends));
            jsonObject.add("blockedUser", parser.toJsonTree(blockedUser));

            return response.generiereAntwort(parser.toJson(jsonObject));
        }

    }

    /**
     * Diese Methode sendet eine Freundschaftsanfrage an einen anderen Nutzer
     * und kann auch dafür genutzt werden, Freundschaftsanfragen anzunehmen.
     *
     * @param token Das Webtoken
     * @param Daten Die Informationen für den eigenen und anderen Nutzer
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/freundesAnfrage/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendeFreundesAnfrage(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            Gson parser = new Gson();

            try{
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getCopyByUsername(andererName);

                if(!self.getOwnFriendList().contains(other)){
                    nutzerEJB.fuegeFreundHinzu(self, other);
                    return response.generiereAntwort("true");
                }else{
                    return response.generiereFehler406("Bereits befreundet");
                }
            }catch(JsonSyntaxException e){
                return response.generiereFehler406("Json wrong");

            }
        }
    }

    /**
     * Diese Methode löscht einen Freund und kann auch dafür genutzt werden,
     * Freundschaftsanfragen abzulehnen.
     *
     * @param token Das Webtoken
     * @param Daten Die Informationen für den eigenen und anderen Nutzer
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/loescheFreund/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loescheFreund(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
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
            }catch(JsonSyntaxException e){
                return response.generiereFehler406("Json wrong");
            }
        }
    }

    /**
     * Dise Methode blockiert einen anderen Nutzer, das heißt, dass die beiden
     * Nutzer sich gegenseitig keine Nachrichten mehr schicken können.
     * Wenn der andere Nutzer schon blockiert ist, dann wird die Blockierung aufgehoben
     * und die Nutzer können sich wieder Nachrichten schicken.
     *
     * @param token
     * @param Daten
     * @return
     */
    @POST
    @Path("/block/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response block(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            Gson parser = new Gson();

            try{
                System.out.println(Daten);
                JsonObject jsonO = parser.fromJson(Daten, JsonObject.class);

                int eigeneId = parser.fromJson((jsonO.get("eigeneId")), Integer.class);
                Nutzer self = nutzerEJB.getById(eigeneId);

                String andererName = parser.fromJson((jsonO.get("andererNutzerName")), String.class);
                Nutzer other = nutzerEJB.getByUsername(andererName);
                try{
                    if(!self.getHatBlockiert().contains(other)){
                        nutzerEJB.block(self, other);
                        self.getHatBlockiert().add(other);
                        other.getBlockiertVon().add(self);
                        return response.generiereAntwort("true");
                    }else{
                        nutzerEJB.unblock(self, other);
                        self.getHatBlockiert().remove(other);
                        other.getBlockiertVon().remove(self);
                        return response.generiereAntwort("true");
                    }
                }catch(EJBTransactionRolledbackException e){
                    return response.generiereAntwort("false");
                }

            }catch(JsonSyntaxException e){
                return response.generiereFehler406("Json wrong");
            }
        }
    }

    /**
     * Diese Methode gibt die Einstellungen eines Nutzers zurück, sowie die Anzahl der selbst
     * gesendeten Nachrichten und Fotos. Außerdem wird für jeden Chat und jede Gruppe, an der
     * der Nutzer teilnimmt eine Übersicht angegeben, wie viele Nachrichten und Fotos
     * in dem jeweiligen Chat beziehungsweise der Gruppe vorhanden sind
     *
     * @param token
     * @param id
     * @return
     */
    @GET
    @Path("/getSettings/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            Gson parser = new Gson();
            JsonObject j = new JsonObject();
            j.add("settings", parser.toJsonTree(settingsEJB.getCopyById(id)));

            //alle selbst gesendeten Nachrichten, Fotos, etc
            int anzahlN = 0;
            int anzahlF = 0;
            List<Nachricht> nL = nachrichtEJB.getOwnMessages(id);
            for(Nachricht n : nL){
                anzahlN++;
                if(n.getFoto() != null){
                    anzahlF++;
                }
            }
            j.add("anzahlSelbstGesendet", parser.toJsonTree(anzahlN));
            j.add("anzahlFotosSelbstGesendet", parser.toJsonTree(anzahlF));

            //für jeden eigenen Chat Liste mit Anzahl Nachrichten, Fotos, etc
            Nutzer self = nutzerEJB.getById(id);
            List<Chat> chatList = chatEJB.getOwnChats(self);
            List<JsonObject> jsonList = new ArrayList<>();
            for(Chat c : chatList){
                JsonObject jO = new JsonObject();
                int anzahlNa = 0;
                int anzahlFo = 0;
                List<Nachricht> nList = nachrichtEJB.getByChat(c.getChatid());
                for(Nachricht nachricht : nList){
                    anzahlNa++;
                    if(nachricht.getFoto() != null){
                        anzahlFo++;
                    }
                }
                jO.add("id", parser.toJsonTree(c.getChatid()));
                jO.add("anzahlNachrichten", parser.toJsonTree(anzahlNa));
                jO.add("anzahlFotos", parser.toJsonTree(anzahlFo));
                jsonList.add(jO);
            }
            j.add("chatübersicht", parser.toJsonTree(jsonList));

            return response.generiereAntwort(parser.toJson(j));
        }
    }

    /**
     * Diese Methode ändert die Eintellungen des Nutzers.
     *
     * @param token Das Webtoken
     * @param Daten Die neuen Einstellung
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/setSettings/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setSettings(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            Gson parser = new Gson();
            Setting s = parser.fromJson(Daten, Setting.class);
            Nutzer self = nutzerEJB.getById(s.getNutzerid());

            try{
                self.getSetting().setDarkmode(s.getDarkmode());
            }catch(NullPointerException e){

            }
            try{
                self.getSetting().setLesebestätigung(s.getLesebestätigung());
            }catch(NullPointerException e){

            }
            try{
                self.getSetting().setMailifimportant(s.getMailifimportant());
            }catch(NullPointerException e){

            }
            return response.generiereAntwort("true");
        }
    }

    /**
     * Diese Methode gibt den persönliche Kanal eines Nutzers zurück. Dieser ist so aufgebaut,
     * dass nur der Nutzer, dem der Kanal gehört, Nachrichten in diesem veröffentlichen kann.
     * Den Nachrichten kann ein Like hinzugefügt werden, diese Methode gibt deshalb außerdem an,
     * welche und wie viele Nutzer einer Nachricht einen Like hinzugefügt haben.
     *
     * @param token
     * @param id
     * @return
     */
    @GET
    @Path("/getChannel/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannel(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{

            Gson parser = new Gson();

            Nutzer n = nutzerEJB.getById(id);

            List<Nachricht> l = nachrichtEJB.getCopyByChat(n.getChannel().getChatid());

            List<Nachricht> ll = new ArrayList<>();

            for(int x = l.size() - 1; x >= 0; x--){
                ll.add(l.get(x));
            }

            for(Nachricht na : ll){
                na.setSender(null);
                for(Nutzer nu : na.getLikedBy()){
                    nu.setPasswordhash(null);
                    nu.setAdminInGroups(null);
                    nu.setOwnFriendList(null);
                    nu.setOtherFriendList(null);
                    nu.setPinnedChats(null);
                    nu.setHatBlockiert(null);
                    nu.setBlockiertVon(null);
                    nu.setSetting(null);
                    nu.setMarkedMessages(null);
                    nu.setArchivedChats(null);

                }
            }

            return response.generiereAntwort(parser.toJson(ll));

//            return response.generiereFehler406("false");
        }
    }

    /**
     * Diese Methode realisiert den Login in das System und erstellt einen
     * Token, der einen gewissen Gültigkeitszeitraum hat.
     *
     * @param Daten Die Anmeldedaten des Nutzers, bestehend aus Nutzername und
     * Passwort
     * @return Das Responseobjekt mit dem Webtoken und den eigenen
     * Nutzerinformationen
     */
    @POST
    @Path("/login")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String Daten){
//        Response r = response.generiereAntwort(Daten);
        Gson parser = new Gson();
        try{
            //getting the name of the body
            JsonObject loginUser = parser.fromJson(Daten, JsonObject.class);
            String jsonUsername = parser.fromJson((loginUser.get("benutzername")), String.class);
            String jsonPasswort = parser.fromJson((loginUser.get("passwort")), String.class);

            Nutzer dbNutzer = nutzerEJB.getCopyByUsernameListsNotNull(jsonUsername);

            for(Chat c : dbNutzer.getPinnedChats()){
                c.setNutzerList(null); // Dies ist entscheidend, damit er nicht bis ins unendliche versucht den Parsingtree aufzubauen.
            }
            if(dbNutzer.getVerificationpin() == null){
                if(hasher.checkPassword(jsonPasswort).equals(dbNutzer.getPasswordhash())){
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
                    dbNutzer.setIsonline(Boolean.TRUE);
                    //return parser.toJson(jsonObject);
                    return response.generiereAntwort(parser.toJson(jsonObject));
                    //return "  {\"token\": \"" + tokenizer.createNewToken(dbNutzer.getBenutzername()) + "\" }  ";
//                return response.generiereAntwort("test");
                }else{
                    return response.generiereFehler406("PW falsch");
                }
            }else{
                return response.generiereFehler406("Du musst dich erst verifizieren");
            }

        }catch(JsonSyntaxException e){
            return response.generiereFehler406("Json wrong");
        }catch(EJBTransactionRolledbackException e){
            return response.generiereFehler406(e.toString());
        }
    }

    /**
     * Diese Methode realisiert den Logout aus dem System und fügt das Token des Nutzers in
     * die Blacklist ein, weswegen der Token nicht mehr zum Login genutzt werden kann.
     *
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/logout/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@PathParam("token") String token){
        if(!verify(token)){
            return response.generiereAntwort("Bereits ausgeloggt");
        }else{
            Gson parser = new Gson();
            try{
                Blacklist bl = new Blacklist();
                bl.setToken(token);

                Date date = new Date();
                bl.setTimestamp(date);

                blacklistEJB.logOut(bl);

                Nutzer n = nutzerEJB.getByUsername(tokenizer.getUser(token));
                n.setIsonline(Boolean.FALSE);
                BigInteger x = new BigInteger("" + System.currentTimeMillis());
                n.setLastonline(x);

                return response.generiereAntwort("true");

            }catch(JsonSyntaxException e){
                return response.generiereFehler406("Json wrong");
            }catch(EJBTransactionRolledbackException e){
                return response.generiereFehler406("kp");
            }

        }

    }

    /**
     * Diese Methode fügt einen Nutzer in das System ein. Bevor dieser Nutzer
     * sich einloggen kann, erhält er einen vierstelligen Pin per E-Mail,
     * mit dem der Nutzer sein Konto erst freischalten muss.
     *
     * @param Daten Die Daten zum neuen Nutzer
     * @return Das Responseobjekt mit den Nutzerinformationen und dem Webtoken
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String Daten) throws IOException, MessagingException, AddressException, InterruptedException{

        System.out.println(Daten);
        Gson parser = new Gson();

        try{

            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            String neuerNutzername = parser.fromJson((jsonObject.get("benutzername")), String.class);
            String neueEmail = parser.fromJson((jsonObject.get("email")), String.class);
            String neuerVorname = parser.fromJson((jsonObject.get("vorname")), String.class);
            String neuerNachname = parser.fromJson((jsonObject.get("nachname")), String.class);
            String neuesPasswort = parser.fromJson((jsonObject.get("passwort")), String.class);
            String neueHandynummer = parser.fromJson((jsonObject.get("handynummer")), String.class);
            String neueInfo = parser.fromJson((jsonObject.get("info")), String.class);

            Nutzer neuerNutzer = new Nutzer();

            Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername);

            if(UserNameIsFree == null){
//                System.out.println("Username free");
                neuerNutzer.setBenutzername(neuerNutzername);
            }else{
//                System.out.println("Username not free");
                return response.generiereFehler500("Benutzername bereits vergeben");
            }

            Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail);

            if(EmailIsFree == null){
                neuerNutzer.setEmail(neueEmail);
            }else{
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

            Nutzer n = nutzerEJB.getById(1);
            String mailFrom = n.getEmail();
            String pw = n.getPasswordhash();
            int min = 1000;
            int max = 9999;
            int random_int = (int) (Math.random() * (max - min + 1) + min);
            neuerNutzer.setVerificationpin(random_int);
            mail.sendVerificationPin(mailFrom, pw, neuerNutzername, neueEmail, random_int);

            neuerNutzer.setMitgliedseit(toIntExact((System.currentTimeMillis() / 1000)));

            nutzerEJB.add(neuerNutzer);
            Nutzer nutzerInDbB = nutzerEJB.getByUsername(neuerNutzername);
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("id", nutzerInDbB.getId());
            returnObject.addProperty("benutzername", nutzerInDbB.getBenutzername());
            returnObject.addProperty("email", nutzerInDbB.getEmail());
            //Einstellungen erstellen
            Setting s = new Setting();
            s.setDarkmode(Boolean.TRUE);
            s.setLesebestätigung(Boolean.TRUE);
            s.setMailifimportant(Boolean.TRUE);
            s.setNutzer(nutzerInDbB);
            s.setNutzerid(nutzerInDbB.getId());
            settingsEJB.add(s);
            nutzerInDbB.setSetting(s);

            //eigenen Channel erstellen
            Chat channel = new Chat();
            channel.setIsgroup(false);
            chatEJB.createChat(channel);
            nutzerInDbB.setChannel(channel);
            return response.generiereAntwort(parser.toJson(returnObject));

        }catch(JsonSyntaxException e){
            return response.generiereFehler406("Json falsch");
        }
        //return "";

    }

    /**
     * Diese Methode schaltet einen neu registrierten Benutzer frei. Dafür muss
     * dieser den zuvor per E-Mail erhaltenen Pin eingeben. Nach dem Aufrufen der Methode
     * kann sich der Nutzer einloggen.
     *
     * @param Daten Die Informationen zum Nutzer und der per E-Mail erhaltene
     * Pin
     * @return
     */
    @POST
    @Path("/verify")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyPin(String Daten){
        Gson parser = new Gson();
        JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
        String name = parser.fromJson((jsonObject.get("name")), String.class);
        int pin = parser.fromJson((jsonObject.get("pin")), Integer.class);

        Nutzer n = nutzerEJB.getByUsername(name);
        try{
            if(n.getVerificationpin() == pin){
                n.setVerificationpin(null);
                return response.generiereAntwort("true");
            }else{
                return response.generiereFehler406("Falscher Pin");
            }
        }catch(Exception e){
            return response.generiereFehler500("Fehler");
        }

    }

    /**
     * Diese Methode ändert das Profilbild des Nutzers.
     *
     * @param token Das Webtoken
     * @param Daten Die eigene Id und das Foto
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/setzeProfilbild/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setzeProfilbild(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
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
            }else{
                return response.generiereFehler406("ID nicht vorhanden");
            }

        }

    }

    /**
     * Diese Methode aktualisiert die Nutzerinformationen. Um den Nutzer zu verifizieren,
     * muss dieser sein altes Passwort eingeben.
     *
     * @param token Das Webtoken
     * @param Daten Die zu aktualisierenden Nutzerdaten sowie das eigene Passwort.
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @PUT
    @Path("/update/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("token") String token, String Daten) throws IOException, MessagingException, AddressException, InterruptedException{
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
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
                String msg = "<h2>Hallo " + nutzerInDB.getBenutzername() + ",</h2>"
                        + "<p>deine Accountinformationen wurden überarbeitet, hier siehst du die Änderungen:</p><p>";
                if(nutzerInDB != null){

                    if(neuerNutzername != null){
                        Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername);

                        if(UserNameIsFree == null){
                            nutzerInDB.setBenutzername(neuerNutzername);
                            msg += "Dein neuer Benutzername: \"" + neuerNutzername + "\"</p><p>";
                        }else{
                            return response.generiereFehler500("Benutzername bereits vergeben");
                        }
                    }

                    if(neueEmail != null){
                        Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail);

                        if(EmailIsFree == null){
                            nutzerInDB.setEmail(neueEmail);
                            msg += "Deine neue E-Mail: \"" + neueEmail + "\"</p><p>";
                        }else{
                            return response.generiereFehler500("Benutzername bereits vergeben");
                        }
                    }

                    if(neuerVorname != null){
                        nutzerInDB.setVorname(neuerVorname);
                        msg += "Dein neuer Vorname: \"" + neuerVorname + "\"</p><p>";
                    }

                    if(neuerVorname != null){
                        nutzerInDB.setNachname(neuerNachname);
                        msg += "Dein neuer Nachname: \"" + neuerNachname + "\"</p><p>";
                    }

                    if(neuesPasswort != null){
                        nutzerInDB.setPasswordhash(hasher.checkPassword(neuesPasswort));
                    }

                    if(neueHandynummer != null){
                        nutzerInDB.setHandynummer(neueHandynummer);
                        msg += "Deine neue Handynummer: \"" + neueHandynummer + "\"</p><p>";
                    }

                    if(neueInfo != null){
                        nutzerInDB.setInfo(neueInfo);
                        msg += "Deine neue Info: \"" + neueInfo + "\"</p><p>";
                    }

                    msg += "Falls du diese Änderungen vorgenommen hast, kannst du diese Mail ignorieren, wenn du diese Änderungen nicht vorgenommen hast, dann gibt es ein Sicherheitsproblem mit deinem Account und du solltst schnellstmöglich dein Passwort ändern!</p>"
                            + "<h3>Mit freundlichen Grüßen,</h3>"
                            + "<h3>dein Disputatio-Team</h3>";

                    Nutzer n = nutzerEJB.getById(1);
                    String mailFrom = n.getEmail();
                    String pw = n.getPasswordhash();
                    mail.sendAccChanges(mailFrom, pw, nutzerInDB.getEmail(), msg);

                    return response.generiereAntwort(tokenizer.createNewToken(neuerNutzername));

                }else{
                    return response.generiereFehler401("Id nicht vorhanden");
                }

            }else{
                return response.generiereFehler406("PW falsch");
            }

        }

    }

    /**
     * Diese Methode löscht den eigenen Nutzer, dabei wird überprüft, ob die
     * Person, von der die Anfrage kam, gleich dem zu löschenden Nutzer ist, um das Löschen von anderen Accounts
     * zu unterbinden.
     *
     * @param id Die eigene Id
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @DELETE
    @Path("/delete/{id}/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generiereFehler401("Ungültiges Token");
        }else{
            String name = tokenizer.getUser(token);
            if(nutzerEJB.getByUsername(name).equals(nutzerEJB.getById(id))){

                if(nutzerEJB.delete(id)){
                    Blacklist bl = new Blacklist();
                    bl.setToken(token);

                    Date date = new Date();
                    bl.setTimestamp(date);
                    blacklistEJB.logOut(bl);
                    return response.generiereAntwort("true");
                }else{
                    return response.generiereFehler500("Fehler beim Löschen");

                }

            }else{
                return response.generiereFehler406("Du hast nicht die nötige Berechtigung");
            }
        }
    }

}
