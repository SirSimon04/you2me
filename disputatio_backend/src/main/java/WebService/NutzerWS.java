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
import Service.Filter;
import Service.Hasher;
import Service.Tokenizer;
import Service.Antwort;
import Service.Mail;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJBTransactionRolledbackException;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.ws.rs.core.Response;
import static java.lang.Math.toIntExact;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * <h1>Der Webserver für die Datenverarbeitung, bezogen auf die Nutzer.</h1>
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

    private final Filter filter = new Filter();

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
            return response.generateError401("Ungültiges Token");
        }else{
            List<Nutzer> liste = nutzerEJB.getAllCopy();
            Gson parser = new Gson();
            return response.generateAnswer(parser.toJson(liste));

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
            return response.generateError401("Ungültiges Token");
        }else{
            try{
                Nutzer n = nutzerEJB.getCopyById(id);
                Gson parser = new Gson();
                return response.generateAnswer(parser.toJson(n));
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("Id nicht vorhanden");
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
            return response.generateError401("Ungültiges Token");
        }else{
            try{
                Nutzer n = nutzerEJB.getCopyByUsername(benutzername);
                Gson parser = new Gson();
                return response.generateAnswer(parser.toJson(n));
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("Benutzername nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methode gibt die Nutzer zurück, die von einem selbst in
     * einen Chat hinzugefügt werden können. Diese Liste beinhaltet alle Freunde,
     * die noch nicht in dem Chat sind. Handelt es sich bei der Chatid, die übergeben
     * um eine ungültige Id, wurde der Chat also noch nicht erstellt, wird einfach die Freundesliste
     * des Nutzers übergeben.
     *
     * @param chatid Die Id des Chatid
     * @param nutzerid DIe Id des Nutzers
     * @param token Das Webtoken
     * @return Liste mit hinzufügbaren Freunden
     */
    @GET
    @Path("/getAddable/{chatid}/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAddable(@PathParam("chatid") int chatid, @PathParam("nutzerid") int nutzerid, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{

            Nutzer self = nutzerEJB.getCopyByIdListsNotNull(nutzerid);
            Chat c = chatEJB.getById(chatid);
            List<Nutzer> friends = new ArrayList<>();
            if(c != null){
                for(Nutzer n : self.getOwnFriendList()){
                    if(self.getOtherFriendList().contains(n)){
                        if(!c.getNutzerList().contains(n)){
                            n.setPasswordhash(null);
                            n.setAdminInGroups(null);
                            n.setOwnFriendList(null);
                            n.setOtherFriendList(null);
                            n.setPinnedChats(null);
                            n.setHatBlockiert(null);
                            n.setBlockiertVon(null);
                            n.setSetting(null);
                            n.setMarkedMessages(null);
                            n.setArchivedChats(null);
                            friends.add(n);
                        }
                    }
                }
            }else{
                for(Nutzer n : self.getOwnFriendList()){
                    if(self.getOtherFriendList().contains(n)){
                        n.setPasswordhash(null);
                        n.setAdminInGroups(null);
                        n.setOwnFriendList(null);
                        n.setOtherFriendList(null);
                        n.setPinnedChats(null);
                        n.setHatBlockiert(null);
                        n.setBlockiertVon(null);
                        n.setSetting(null);
                        n.setMarkedMessages(null);
                        n.setArchivedChats(null);
                        friends.add(n);
                    }
                }
            }

            Gson parser = new Gson();
            return response.generateAnswer(parser.toJson(friends));
        }

    }

    /**
     * Diese Methode sucht nach Nutzern anhand ihres Benutzernamens und gibt diese
     * zurück.Sie kann zum Beispiel beim Hinzufügen von Freunden genutzt werden.
     * Aißerdem wird angegeben,ob man selbst einem anderen Nutzer schon eine Freundschaftsanfrage geschickt hat.
     *
     * @param benutzername Der angefragte Benutzername
     * @param token Das Webtoken
     * @param id Die eigene Id
     * @return Die Liste mit den gesuchten Nutzern
     */
    @GET
    @Path("/search/{benutzername}/{eigeneId}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@PathParam("benutzername") String benutzername, @PathParam("token") String token, @PathParam("eigeneId") int id){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            List<Nutzer> l = nutzerEJB.searchCopy(benutzername);
            Nutzer self = nutzerEJB.getById(id);
            Gson parser = new Gson();
            if(l.isEmpty()){
                return response.generateError406("Benutzername nicht vorhanden");
            }else{
                List<JsonObject> jl = new ArrayList<JsonObject>();
                for(Nutzer n : l){
                    if(!self.equals(n)){
                        JsonObject jo = new JsonObject();
                        jo.add("nutzer", parser.toJsonTree(n));
                        if(self.getOwnFriendList().contains(n)){
                            jo.add("sentfa", parser.toJsonTree(true));
                        }
                        jl.add(jo);
                    }

                }
                JsonObject returnObject = new JsonObject();
                returnObject.add("results", parser.toJsonTree(jl));
                return response.generateAnswer(parser.toJson(returnObject));
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
            return response.generateError401("Ungültiges Token");
        }else{
            try{
                Gson parser = new Gson();
                Nutzer n = new Nutzer();
                n = nutzerEJB.getCopyById(id);

                Nutzer nutzer = new Nutzer();
                nutzer.setBenutzername(n.getBenutzername());  //nötig, damit nur der Benutzername bekannt ist

                return response.generateAnswer(parser.toJson(nutzer));
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("Id nicht vorhanden");
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
        Gson parser = new Gson();
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{

            JsonObject jsonObject = new JsonObject();

            Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);

            Date now = new Date(System.currentTimeMillis());

            List<Nutzer> ownFriendListDB = self.getOwnFriendList();
            for(Nutzer n : ownFriendListDB){
                n.setAdminInGroups(null);
                n.setPinnedChats(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
                n.setChannel(null);
                n.setArchivedChats(null);
                n.setMarkedMessages(null);
                n.setPasswordhash(null);
                List<Chat> chats = chatEJB.getOwnChats(n);
                for(Chat c : chats){
                    if(c.getNutzerList().size() == 2 && c.getNutzerList().contains(self) && c.getNutzerList().contains(n)){
                        n.setHasChat(true);
                    }
                }
                Date lastOnlineDate = new Date(n.getLastonline().longValue());
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
                System.out.println(simple.format(now).substring(0, 6));
                System.out.println(simple.format(now).substring(7, 11));
                System.out.println(simple.format(now).substring(12, 17));
                String lastOnlineString = "";
                String nowDate = simple.format(now);
                String lastOnline = simple.format(lastOnlineDate);
                if(!nowDate.substring(0, 6).equals(lastOnline.substring(0, 6))){
                    lastOnlineString += lastOnline.substring(0, 6) + " ";
                }
                if(!nowDate.substring(7, 11).equals(lastOnline.substring(7, 11))){
                    lastOnlineString += lastOnline.substring(7, 11) + " ";
                }
                lastOnlineString += lastOnline.substring(12, 17);
                n.setLastOnlineString(lastOnlineString);

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
                n.setChannel(null);
                n.setArchivedChats(null);
                n.setMarkedMessages(null);
                n.setPasswordhash(null);
            }

            for(Nutzer n : self.getHatBlockiert()){
                n.setAdminInGroups(null);
                n.setPinnedChats(null);
                n.setOtherFriendList(null);
                n.setOwnFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
                n.setChannel(null);
                n.setArchivedChats(null);
                n.setMarkedMessages(null);
                n.setPasswordhash(null);
            }

            List<Nutzer> offlineFriends = new ArrayList<>();
            List<Nutzer> friendRequests = new ArrayList<>();
            List<Nutzer> pendingRequests = new ArrayList<>();
            List<Nutzer> onlineFriends = new ArrayList<>();
            List<Nutzer> blockedUser = self.getHatBlockiert();
            for(Nutzer n : ownFriendListDB){
                if(otherFriendListDB.contains(n)){
                    if(n.getIsonline()){
                        onlineFriends.add(n);
                    }else{
                        offlineFriends.add(n);
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
            List<Nutzer> returnFriends = new ArrayList<>();
            returnFriends.addAll(onlineFriends);
            returnFriends.addAll(offlineFriends);

            List<JsonObject> returnFriendsList = new ArrayList<JsonObject>();
            for(Nutzer n : returnFriends){
                JsonObject j = new JsonObject();
                j.add("nutzer", parser.toJsonTree(n));
                try{
                    if(self.getHatBlockiert().contains(n)){
                        j.add("blockiert", parser.toJsonTree(true));
                    }else{
                        j.add("blockiert", parser.toJsonTree(false));
                    }
                }catch(NullPointerException e){

                }
                returnFriendsList.add(j);
            }

            jsonObject.add("friendList", parser.toJsonTree(returnFriendsList));
            jsonObject.add("friendRequests", parser.toJsonTree(friendRequests));
            jsonObject.add("pendingRequests", parser.toJsonTree(pendingRequests));
            jsonObject.add("blockedUser", parser.toJsonTree(blockedUser));

            return response.generateAnswer(parser.toJson(jsonObject));
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
            return response.generateError401("Ungültiges Token");
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
                    return response.generateAnswer("true");
                }else{
                    return response.generateError406("Bereits befreundet");
                }
            }catch(JsonSyntaxException e){
                return response.generateError406("Json wrong");

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
    public Response deleteFriend(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
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

                List<Chat> l = chatEJB.getAll();
                for(Chat c : l){
                    if(c.getNutzerList().size() == 2 && c.getNutzerList().contains(self) && c.getNutzerList().contains(other)){
                        chatEJB.delete(c);
                    }
                }

                return response.generateAnswer("true");
            }catch(JsonSyntaxException e){
                return response.generateError406("Json wrong");
            }
        }
    }

    /**
     * Dise Methode blockiert einen anderen Nutzer, das heißt, dass die beiden
     * Nutzer sich gegenseitig keine Nachrichten mehr schicken können.
     * Wenn der andere Nutzer schon blockiert ist, dann wird die Blockierung aufgehoben
     * und die Nutzer können sich wieder Nachrichten schicken.
     *
     * @param token Das Webtoken
     * @param Daten Die Daten zum eigenen sowie anderen Nutzer.
     * @return Das Responseobjekt mir de Status der Methode.
     */
    @POST
    @Path("/block/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response block(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();

            try{
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
                        return response.generateAnswer("true");
                    }else{
                        nutzerEJB.unblock(self, other);
                        self.getHatBlockiert().remove(other);
                        other.getBlockiertVon().remove(self);
                        return response.generateAnswer("true");
                    }
                }catch(EJBTransactionRolledbackException e){
                    return response.generateAnswer("false");
                }

            }catch(JsonSyntaxException e){
                return response.generateError406("Json wrong");
            }
        }
    }

    /**
     * Diese Methode gibt die Einstellungen eines Nutzers zurück, sowie die Anzahl der selbst
     * gesendeten Nachrichten und Fotos. Außerdem wird für jeden Chat und jede Gruppe, an der
     * der Nutzer teilnimmt eine Übersicht angegeben, wie viele Nachrichten und Fotos
     * in dem jeweiligen Chat beziehungsweise der Gruppe vorhanden sind
     *
     * @param token Das Webtoken
     * @param id Die eigene Id
     * @return Die eigenen Einstellungen
     */
    @GET
    @Path("/getSettings/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSettings(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
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
                if(c.getIsgroup()){
                    jO.add("name", parser.toJsonTree(c.getName()));
                }else{
                    List<Nutzer> nutzer = c.getNutzerList();
                    if(nutzer.get(0).equals(self)){
                        jO.add("name", parser.toJsonTree(nutzer.get(1).getBenutzername()));
                    }else{
                        jO.add("name", parser.toJsonTree(nutzer.get(0).getBenutzername()));
                    }
                }
            }

            //Sortiere Chats nach der Anzahl der Nachrichten
            Collections.sort(jsonList, (JsonObject o1, JsonObject o2) -> new BigInteger(o2.get("anzahlNachrichten").toString()).compareTo(new BigInteger(o1.get("anzahlNachrichten").toString())));

            j.add("chatübersicht", parser.toJsonTree(jsonList));

            List<Nachricht> nList = nachrichtEJB.getMarkedMessages(id);
            j.add("markedMessages", parser.toJsonTree(nList));
            Nutzer n = nutzerEJB.getCopyById(id);
            j.add("self", parser.toJsonTree(n));
            return response.generateAnswer(parser.toJson(j));
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
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();
            Setting s = parser.fromJson(Daten, Setting.class);
            Nutzer self = nutzerEJB.getById(s.getNutzerid());

            try{
                settingsEJB.getById(self.getId()).setDarkmode(s.getDarkmode());
            }catch(NullPointerException e){

            }
            try{
                settingsEJB.getById(self.getId()).setWordfilter(s.getWordfilter());
            }catch(NullPointerException e){

            }
            try{
                settingsEJB.getById(self.getId()).setMailifimportant(s.getMailifimportant());
            }catch(NullPointerException e){

            }
            return response.generateAnswer("true");
        }
    }

    /**
     * Diese Methode gibt den persönliche Kanal eines Nutzers zurück. Dieser ist so aufgebaut,
     * dass nur der Nutzer, dem der Kanal gehört, Nachrichten in diesem veröffentlichen kann.
     * Den Nachrichten kann ein Like hinzugefügt werden, diese Methode gibt deshalb außerdem an,
     * welche und wie viele Nutzer einer Nachricht einen Like hinzugefügt haben.
     *
     * @param token Das Webtoken
     * @param id Die Id des angefragten Nutzers
     * @return Alle Nachrichten aus dem Channel des Nutzers
     */
    @GET
    @Path("/getChannel/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChannel(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{

            Gson parser = new Gson();

            Nutzer nutzer = nutzerEJB.getById(id);

            List<Nachricht> l = nachrichtEJB.getCopyByChat(nutzer.getChannel().getChatid());

            List<Nachricht> ll = new ArrayList<>();
            List<JsonObject> returnList = new ArrayList<JsonObject>();
            for(int x = l.size() - 1; x >= 0; x--){
                ll.add(l.get(x));
            }
            Date now = new Date(System.currentTimeMillis());
            if(ll.size() > 1){
                Collections.sort(ll, (Nachricht z1, Nachricht z2) -> {
                    if(z1.getDatumuhrzeit() > z2.getDatumuhrzeit()){
                        return -1;
                    }
                    if(z1.getDatumuhrzeit() < z2.getDatumuhrzeit()){
                        return 1;
                    }
                    return 0;
                });
            }
            for(Nachricht n : ll){
                JsonObject j = new JsonObject();
                j.add("post", parser.toJsonTree(n));
                try{
                    String base64 = nutzerEJB.getById(n.getSenderid()).getProfilbild().getBase64();
                    j.add("profilbild", parser.toJsonTree(base64));
                }catch(NullPointerException e){
                }

                Date lastOnlineDate = new Date(n.getDatumuhrzeit());
                DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");
                String lastOnlineString = "";
                String nowDate = simple.format(now);
                String lastOnline = simple.format(lastOnlineDate);
                if(!nowDate.substring(0, 6).equals(lastOnline.substring(0, 6))){
                    lastOnlineString += lastOnline.substring(0, 6) + " ";
                }
                if(!nowDate.substring(7, 11).equals(lastOnline.substring(7, 11))){
                    lastOnlineString += lastOnline.substring(7, 11) + " ";
                }
                lastOnlineString += lastOnline.substring(12, 17);
                j.add("timestring", parser.toJsonTree(lastOnlineString));
                returnList.add(j);
            }

            JsonObject returnObject = new JsonObject();
            returnObject.add("posts", parser.toJsonTree(returnList));
            return response.generateAnswer(parser.toJson(returnObject));

//            return response.generateError406("false");
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
        Gson parser = new Gson();
        try{
            //getting the name of the body
            JsonObject loginUser = parser.fromJson(Daten, JsonObject.class);
            String jsonUsername = parser.fromJson((loginUser.get("benutzername")), String.class);
            String jsonPasswort = parser.fromJson((loginUser.get("passwort")), String.class);

            Nutzer dbNutzer = nutzerEJB.getCopyByUsernameListsNotNull(jsonUsername);
            if(dbNutzer.getVerificationpin() == null){
                if(hasher.checkPassword(jsonPasswort).equals(dbNutzer.getPasswordhash())){
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", dbNutzer.getId());
                    jsonObject.addProperty("benutzername", dbNutzer.getBenutzername());
                    jsonObject.addProperty("email", dbNutzer.getEmail());
                    try{
                        jsonObject.addProperty("profilbild", parser.toJson(dbNutzer.getProfilbild().getBase64()));
                    }catch(Exception e){
                    }
                    String token = tokenizer.createNewToken(dbNutzer.getBenutzername());
                    jsonObject.addProperty("token", token);
                    jsonObject.addProperty("darkmode", settingsEJB.getById(dbNutzer.getId()).getDarkmode());
                    dbNutzer.setIsonline(Boolean.TRUE);
                    return response.generateAnswer(parser.toJson(jsonObject));
                }else{
                    return response.generateError406(parser.toJson("passwordWrong"));
                }
            }else{
                return response.generateError406(parser.toJson("verify"));
            }

        }catch(JsonSyntaxException e){
            return response.generateError406("Json wrong");
        }catch(EJBTransactionRolledbackException e){
            return response.generateError406(parser.toJson("usernameWrong"));
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
            return response.generateAnswer("Bereits ausgeloggt");
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

                return response.generateAnswer("true");

            }catch(JsonSyntaxException e){
                return response.generateError406("Json wrong");
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("kp");
            }

        }

    }

    /**
     * Diese Methode fügt einen Nutzer in das System ein.Bevor dieser Nutzer
     * sich einloggen kann, erhält er einen vierstelligen Pin per E-Mail,
     * mit dem der Nutzer sein Konto erst freischalten muss.
     *
     * @param Daten Die Daten zum neuen Nutzer
     * @return Das Responseobjekt mit den Nutzerinformationen und dem Webtoken
     * @throws java.io.IOException
     * @throws javax.mail.MessagingException
     * @throws javax.mail.internet.AddressException
     * @throws java.lang.InterruptedException
     */
    @POST
    @Path("/add")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(String Daten) throws IOException, MessagingException, AddressException, InterruptedException{

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
            String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);

            Nutzer neuerNutzer = new Nutzer();

            Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername);

            if(UserNameIsFree == null){
                if(filter.isProfane(neuerNutzername)){
                    return response.generateError406("Benutzername nicht zulässig");
                }else{
                    neuerNutzer.setBenutzername(neuerNutzername);
                }
            }else{
                return response.generateError500("Benutzername bereits vergeben");
            }

            Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail);

            if(EmailIsFree == null){
                neuerNutzer.setEmail(neueEmail);
            }else{
                return response.generateError500("Email bereits vergeben");
            }

            neuerNutzer.setPasswordhash(hasher.checkPassword(neuesPasswort));

            if(neuerVorname != null){
                if(filter.isProfane(neuerVorname)){
                    return response.generateError406("Vorname nicht zulässig");
                }else{
                    neuerNutzer.setVorname(neuerVorname);
                }
            }

            if(neuerNachname != null){
                if(filter.isProfane(neuerNachname)){
                    return response.generateError406("Nachname nicht zulässig");
                }else{
                    neuerNutzer.setNachname(neuerNachname);
                }
            }

            if(neueHandynummer != null){
                neuerNutzer.setHandynummer(neueHandynummer);
            }

            if(neueInfo != null){
                if(filter.isProfane(neueInfo)){
                    response.generateError406("Info nicht zulässig");
                }
                neuerNutzer.setInfo(neueInfo);
            }

            if(jsonPic != null){
                Foto f = new Foto();
                f.setBase64(jsonPic);
                fotoEJB.add(f);
                Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
                neuerNutzer.setProfilbild(fotoInDB);
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
            neuerNutzer.setIsonline(Boolean.FALSE);

            nutzerEJB.add(neuerNutzer);
            Nutzer nutzerInDbB = nutzerEJB.getByUsername(neuerNutzername);
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("id", nutzerInDbB.getId());
            returnObject.addProperty("benutzername", nutzerInDbB.getBenutzername());
            returnObject.addProperty("email", nutzerInDbB.getEmail());
            //Einstellungen erstellen
            Setting s = new Setting();
            s.setDarkmode(Boolean.TRUE);
            s.setWordfilter(Boolean.TRUE);
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
            return response.generateAnswer(parser.toJson(returnObject));

        }catch(JsonSyntaxException e){
            return response.generateError406("Json falsch");
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
     * @return Das Responseobjekt mit dem Status der Methode.
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
                JsonObject returnObject = new JsonObject();
                returnObject.addProperty("id", n.getId());
                returnObject.addProperty("benutzername", n.getBenutzername());
                returnObject.addProperty("email", n.getEmail());
                String token = tokenizer.createNewToken(n.getBenutzername());

                List<Blacklist> bl = blacklistEJB.getAllBlacklisted();
                for(Blacklist b : bl){
                    if(b.getToken().equals(token)){
                        blacklistEJB.deleteToken(token);
                    }
                }
                returnObject.addProperty("token", token);
                n.setIsonline(Boolean.TRUE);
                return response.generateAnswer(parser.toJson(returnObject));

            }else{
                return response.generateError406("Falscher Pin");
            }
        }catch(Exception e){
            return response.generateError500("Fehler");
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
    public Response setProfilpic(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
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

                return response.generateAnswer("true");
            }else{
                return response.generateError406("ID nicht vorhanden");
            }

        }

    }

    /**
     * Diese Methode aktualisiert die Nutzerinformationen.Um den Nutzer zu verifizieren,
     * muss dieser sein altes Passwort eingeben.
     *
     * @param token Das Webtoken
     * @param Daten Die zu aktualisierenden Nutzerdaten sowie das eigene Passwort.
     * @return Das Responseobjekt mit dem Status der Methode
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws javax.mail.MessagingException
     * @throws javax.mail.internet.AddressException
     */
    @POST
    @Path("/update/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("token") String token, String Daten) throws IOException, MessagingException, AddressException, InterruptedException{
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
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

                        if(filter.isProfane(neuerNutzername)){
                            return response.generateError406("Benutzername nicht zulässig");
                        }else{
                            Nutzer UserNameIsFree = nutzerEJB.getByUsername(neuerNutzername);

                            if(UserNameIsFree == null || nutzerInDB.getBenutzername().equals(neuerNutzername)){
                                nutzerInDB.setBenutzername(neuerNutzername);
                                msg += "Dein neuer Benutzername: \"" + neuerNutzername + "\"</p><p>";
                            }else{
                                return response.generateError500("Benutzername bereits vergeben");
                            }
                        }

                    }

                    if(neueEmail != null){

                        Nutzer EmailIsFree = nutzerEJB.getByMail(neueEmail);

                        if(EmailIsFree == null || nutzerInDB.getEmail().equals(neueEmail)){
                            nutzerInDB.setEmail(neueEmail);
                            msg += "Deine neue E-Mail: \"" + neueEmail + "\"</p><p>";
                        }else{
                            return response.generateError500("Email bereits vergeben");
                        }

                    }

                    if(neuerVorname != null){
                        if(filter.isProfane(neuerVorname)){
                            return response.generateError406("Vorname nicht zulässig");
                        }else{
                            nutzerInDB.setVorname(neuerVorname);
                            msg += "Dein neuer Vorname: \"" + neuerVorname + "\"</p><p>";
                        }

                    }

                    if(neuerNachname != null){
                        if(filter.isProfane(neuerNachname)){
                            return response.generateError406("Nachname nicht zulässig");
                        }else{
                            nutzerInDB.setNachname(neuerNachname);
                            msg += "Dein neuer Nachname: \"" + neuerNachname + "\"</p><p>";
                        }
                    }

                    if(neuesPasswort != null){
                        nutzerInDB.setPasswordhash(hasher.checkPassword(neuesPasswort));
                    }

                    if(neueHandynummer != null){
                        nutzerInDB.setHandynummer(neueHandynummer);
                        msg += "Deine neue Handynummer: \"" + neueHandynummer + "\"</p><p>";
                    }

                    if(neueInfo != null){
                        if(filter.isProfane(neueInfo)){
                            return response.generateError406("Info nicht zulässig");
                        }else{
                            nutzerInDB.setInfo(neueInfo);
                            msg += "Deine neue Info: \"" + neueInfo + "\"</p><p>";
                        }
                    }

                    msg += "Falls du diese Änderungen vorgenommen hast, kannst du diese Mail ignorieren, wenn du diese Änderungen nicht vorgenommen hast, dann gibt es ein Sicherheitsproblem mit deinem Account und du solltst schnellstmöglich dein Passwort ändern!</p>"
                            + "<h3>Mit freundlichen Grüßen,</h3>"
                            + "<h3>dein Disputatio-Team</h3>";

                    Nutzer n = nutzerEJB.getById(1);
                    String mailFrom = n.getEmail();
                    String pw = n.getPasswordhash();
                    mail.sendAccChanges(mailFrom, pw, nutzerInDB.getEmail(), msg);

                    return response.generateAnswer(tokenizer.createNewToken(neuerNutzername));

                }else{
                    return response.generateError401("Id nicht vorhanden");
                }

            }else{
                return response.generateError406("PW falsch");
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
    @GET
    @Path("/delete/{id}/{token}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response delete(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            String name = tokenizer.getUser(token);
            if(nutzerEJB.getByUsername(name).equals(nutzerEJB.getById(id))){

                if(nutzerEJB.delete(id)){
                    Blacklist bl = new Blacklist();
                    bl.setToken(token);

                    Date date = new Date();
                    bl.setTimestamp(date);
                    blacklistEJB.logOut(bl);

                    return response.generateAnswer("true");

                }else{
                    return response.generateError500("Fehler beim Löschen");

                }
            }else{
                return response.generateError406("Du hast nicht die nötige Berechtigung");
            }
        }
    }

}
