/**
 * <h1>Der Webserver, der die Anfragen der Nutzer verarbeitet.</h1>
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
import Service.Antwort;
import Service.Tokenizer;
import Service.DateSorterChat;
import Service.NameSorter;
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
import java.io.IOException;
import java.util.ArrayList;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Objects;

/**
 * <h1>Der Webserver für die Datenverarbeitung, bezogen auf die Chats.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden des Webservers bezogen auf das Objekt
 * des Chats für das Bearbeiten und Ausgeben der Daten und stellt damit die
 * Schnittstelle mit dem Frontend dar</p>
 *
 */
@Path("/chat")
@Stateless
@LocalBean
public class ChatWS{

    @EJB
    private NutzerEJB nutzerEJB;

    @EJB
    private ChatEJB chatEJB;

    @EJB
    FotoEJB fotoEJB;

    @EJB
    private NachrichtEJB nachrichtEJB;

    @EJB
    private Tokenizer tokenizer;

    @EJB
    private BlacklistEJB blacklistEJB;

    @EJB
    private SettingsEJB settingEJB;

    private Antwort response = new Antwort();

    private Filter filter = new Filter();

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
            }else{
                List<Blacklist> bl = blacklistEJB.getAllBlacklisted();
                for(Blacklist b : bl){
                    if(b.getToken().equals(token)){
                        return false;
                    }
                }

                nutzerEJB.setOnline(token);
                nutzerEJB.setOnline(token);
                return true;
            }
        }else{
            return true;
        }

    }

    /**
     * Diese Methode gibt alle Chats zurück.
     *
     * @param token Das Webtoken
     * @return Liste mit allen Chats
     */
    @GET
    @Path("/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            List<Chat> liste = chatEJB.getAllCopy();

            Gson parser = new Gson();
            return response.generateAnswer(parser.toJson(liste));
        }

    }

    /**
     * Diese Methode gibt einen Chat anhand seiner Id zurück.
     *
     * @param token Das Webtoken
     * @param id Die id
     * @return Der Chat
     */
    @GET
    @Path("/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Chat c = chatEJB.getCopy(id);

            Gson parser = new Gson();
            return response.generateAnswer(parser.toJson(c));
        }

    }

    /**
     * Diese Methode gibt die Chatid des Privatchats von
     * zwei gegebenen Nutzern an.
     *
     * @param token Das Webtoken
     * @param id Die eigene Id
     * @param andereId Die Id des anderen Nutzers
     * @return Die Id des Chats
     */
    @GET
    @Path("/{eigeneId}/{andereId}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrivateChatId(@PathParam("token") String token, @PathParam("eigeneId") int id, @PathParam("andereId") int andereId){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();
            Nutzer self = nutzerEJB.getById(id);
            Nutzer other = nutzerEJB.getById(andereId);
            List<Chat> l = chatEJB.getAll();
            for(Chat c : l){
                if(c.getNutzerList().size() == 2 && c.getNutzerList().contains(self) && c.getNutzerList().contains(other)){
                    JsonObject returnObject = new JsonObject();
                    returnObject.add("id", parser.toJsonTree(c.getChatid()));
                    return response.generateAnswer(parser.toJson(returnObject));
                }
            }
            return response.generateError406("Fehler");
        }
    }

    /**
     * Diese Methode gibt die Informationen eines Chats zurück. Das beinhaltet
     * die Anzahl der Nachrichten, die Anzahl der Fotos, das Profilbild und den
     * Status oder die Gruppenbeschreibung. Wenn es sich um eine Gruppe handelt,
     * wird die Nutzerliste nach folgendem Schema sortiert: Zuerst steht der
     * eigene Nutzer, dann die Administratoren der Gruppe und zuletzt die
     * restlichen Nutzer. Handelt es sich um einen Einzelchat,
     * werden die Informationen des anderen Nutzers übergeben.
     *
     * @param token Das Webtoken
     * @param id Die eigene Id
     * @param chatId Die Chatid
     * @return Die angefragten Chatinformationen
     */
    @GET
    @Path("/info/{chatid}/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatInfo(@PathParam("token") String token, @PathParam("id") int id, @PathParam("chatid") int chatId){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{

            Gson parser = new Gson();

            Chat c = chatEJB.getCopy(chatId);
            List<Nutzer> nutzerListe = c.getNutzerList();

            List<Nutzer> adminListe = c.getAdminList();

            Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);
            Filter filter = new Filter();

            if(c.getIsgroup()){

                self.setBlockiertVon(null);
                self.setHatBlockiert(null);
                self.setArchivedChats(null);
                self.setMarkedMessages(null);

                nutzerListe.remove(self);

                List<Nutzer> isAdmin = new ArrayList<>();
                List<Nutzer> isNotAdmin = new ArrayList<>();

                for(Nutzer nutzer : nutzerListe){
                    nutzer.setAdminInGroups(null);
                    nutzer.setPasswordhash(null);
                    nutzer.setOtherFriendList(null);
                    nutzer.setOwnFriendList(null);
                    nutzer.setHatBlockiert(null);
                    nutzer.setBlockiertVon(null);
                    nutzer.setSetting(null);
                    nutzer.setMarkedMessages(null);
                    nutzer.setArchivedChats(null);
                    if(adminListe.contains(nutzer)){
                        isAdmin.add(nutzer);
                    }else{
                        isNotAdmin.add(nutzer);
                    }
                }

                self.setAdminInGroups(null);
                self.setPasswordhash(null);
                self.setOtherFriendList(null);
                self.setOwnFriendList(null);
                self.setPinnedChats(null);
                self.setSetting(null);
                self.setMarkedMessages(null);
                self.setArchivedChats(null);
                if(adminListe.contains(self)){
                    self.setIsadmin(true);
                }

                for(Nutzer n : isAdmin){
                    n.setIsadmin(true);
                }
//
                List<Nutzer> returnList = new ArrayList<>();
                returnList.add(self);
                returnList.addAll(isAdmin);
                returnList.addAll(isNotAdmin);
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("nutzerliste", parser.toJsonTree(returnList));

                List<Nachricht> nList = nachrichtEJB.getDetachedByChat(chatId);
                int anzahlNachrichten = nList.size();
                int anzahlFotos = 0;
                for(Nachricht n : nList){
                    if(n.getFoto() != null){
                        anzahlFotos += 1;
                    }
                }
                jsonObject.add("anzahlNachrichten", parser.toJsonTree(anzahlNachrichten));
                jsonObject.add("anzahlFotos", parser.toJsonTree(anzahlFotos));
                try{
                    jsonObject.add("beschreibung", parser.toJsonTree(chatEJB.getById(chatId).getBeschreibung()));
                }catch(NullPointerException e){

                }
                try{
                    jsonObject.add("profilbild", parser.toJsonTree(chatEJB.getById(chatId).getProfilbild().getBase64()));
                }catch(NullPointerException e){
                }
                jsonObject.add("isGroup", parser.toJsonTree(chatEJB.getById(chatId).getIsgroup()));
                return response.generateAnswer(parser.toJson(jsonObject));

            }else{
                JsonObject jsonObject = new JsonObject();

                nutzerListe.remove(self);
                Nutzer other = nutzerListe.get(0);
                try{
                    if(self.getHatBlockiert().contains(other)){
                        jsonObject.add("blockiert", parser.toJsonTree(true));
                    }else{
                        jsonObject.add("blockiert", parser.toJsonTree(false));
                    }
                }catch(NullPointerException e){

                }
//
                try{
                    if(self.getBlockiertVon().contains(other)){
                        jsonObject.add("blockiertWorden", parser.toJsonTree(true));
                        return response.generateAnswer(parser.toJson(jsonObject));
                    }else{
                        jsonObject.add("blockiertWorden", parser.toJsonTree(false));
                    }
                }catch(NullPointerException e){

                }

                other.setPasswordhash(null);
                other.setOwnFriendList(null);
                other.setOtherFriendList(null);
                other.setHatBlockiert(null);
                other.setBlockiertVon(null);
                other.setSetting(null);
                other.setMarkedMessages(null);
                other.setArchivedChats(null);
                other.setPinnedChats(null);
                other.setAdminInGroups(null);
                other.setChannel(null);
//
                List<Nachricht> nList = nachrichtEJB.getDetachedByChat(chatId);
                int anzahlNachrichten = 0;
                int anzahlFotos = 0;
                for(Nachricht n : nList){
                    anzahlNachrichten += 1;
                    if(n.getFoto() != null){
                        anzahlFotos += 1;
                    }
                }

                jsonObject.add("anzahlNachrichten", parser.toJsonTree(anzahlNachrichten));
                jsonObject.add("anzahlFotos", parser.toJsonTree(anzahlFotos));
                try{
                    jsonObject.add("beschreibung", parser.toJsonTree(other.getInfo()));
                }catch(NullPointerException e){

                }
                try{
                    jsonObject.add("profilbild", parser.toJsonTree(other.getProfilbild()));
                }catch(NullPointerException e){

                }
                List<Chat> allChats = chatEJB.getAllCopy();
                List<Chat> listTogether = new ArrayList<>();
                for(Chat chat : allChats){
                    if(chat.getNutzerList().contains(self) && chat.getNutzerList().contains(other)){
                        listTogether.add(chat);
                    }
                }

                for(Chat chat : listTogether){
                    chat.setAdminList(null);
                    chat.setNutzerList(null);
                    chat.setLetztenachricht(null);
                }
//
                Chat chat = chatEJB.getById(chatId);
                listTogether.remove(chat);
                Nutzer o = nutzerEJB.getById(other.getId());
                jsonObject.add("gemeinsameChatListe", parser.toJsonTree(listTogether));
                try{
                    if(self.getHatBlockiert().contains(o)){
                        jsonObject.add("isblocked", parser.toJsonTree(true));
                    }
                }catch(NullPointerException e){

                }
                try{
                    if(o.getHatBlockiert().contains(self)){
                        jsonObject.add("gotblocked", parser.toJsonTree(true));
                    }
                }catch(NullPointerException e){

                }
                return response.generateAnswer(parser.toJson(jsonObject));
            }
        }
//        return response.generateAnswer("true");
    }

    /**
     * Diese Methode gibt einen CHat anhand seiner Id zurück.
     * Falls es sich um einen Einzelchat handelt, werden als Chatinformationen
     * die Informationen des anderen Nutzers übergeben.
     *
     * @param chatid Die Id des Chats.
     * @param nutzerid Die eigene Id.
     * @param token Das Webtoken.
     * @return Der angefragte Chat.
     */
    @GET
    @Path("/id/{chatid}/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByChatId(@PathParam("chatid") int chatid, @PathParam("nutzerid") int nutzerid, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            try{
                Chat c = chatEJB.getCopy(chatid);
                int length = 0;

                for(Nutzer n : c.getNutzerList()){
                    n.setPinnedChats(null);
                    length += 1;
                }
                if(length == 2){
                    List<Nutzer> nutzerList = c.getNutzerList();
                    Nutzer n = nutzerEJB.getCopyById(nutzerid);
                    nutzerList.remove(n);

                    Nutzer andererNutzer = nutzerList.get(0);

                    c.setName(andererNutzer.getBenutzername());
                    c.setProfilbild(andererNutzer.getProfilbild());
                    c.setNutzerList(null);
                }

                Gson parser = new Gson();
                return response.generateAnswer(parser.toJson(c));
            }catch(EJBTransactionRolledbackException e){
                return response.generateError401("Id nicht vorhanden");
            }

        }

    }

    /**
     * Diese Methode liefert die Liste mit den letzten Chats zurück. Falls es
     * sich bei einem Chat um einen Einzelchat handelt, werden als
     * Chatinformationen die Informationen des anderen Nutzers angegeben
     * (Profilbild, Name und Info). Die ganze Liste wird mit einem erstellten
     * Comparator nach dem Versanddatum der letzten Nachricht sortiert, dabei
     * sind die Neuesten am Anfang. Chats, die noch keine Nachricht beinhalten,
     * sind am Ende.
     *
     * @param nutzerid Die eigene Id
     * @param token Das Webtoken
     * @return Die eigene Chatliste
     */
    @GET
    @Path("/nutzerid/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOwnChatlistByUserId(@PathParam("nutzerid") int nutzerid, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            try{
                List<Chat> ownChatListDB = new ArrayList<Chat>();

                Nutzer self = nutzerEJB.getCopyByIdListsNotNull(nutzerid);

                Setting selfSetting = settingEJB.getById(nutzerid);

                List<Chat> liste = chatEJB.getAllCopyListsNotNull();
                for(Chat c : liste){
                    if(c.getNutzerList().contains(self)){
                        ownChatListDB.add(c);
                    }
                }
                for(Chat c : ownChatListDB){
                    try{

                        Nachricht n = nachrichtEJB.getNewest(c.getChatid());
                        c.getLetztenachricht().setSender(n.getSender());
                        c.setLetztenachricht(n);
                        if(selfSetting.getWordfilter()){
                            if(filter.isProfane(n.getInhalt())){
                                n.setInhalt(filter.filter(n.getInhalt()));
                            }
                        }
                        if(n.getIsFile()){
                            n.setInhalt(n.getInhalt().substring(n.getInhalt().lastIndexOf("|") + 1, n.getInhalt().length()));
                        }
                    }catch(IOException e){
                    }catch(NullPointerException e){
                    }
//
                    for(Nutzer n : c.getNutzerList()){
                        n.setPasswordhash(null);
                        n.setPinnedChats(null);
                        n.setOwnFriendList(null);
                        n.setOtherFriendList(null);
                        n.setPasswordhash(null);
                        n.setSetting(null);
                        n.setMarkedMessages(null);
                        n.setArchivedChats(null);
                    }
                    //wenn Einzelchat, Infos des Anderen übernehmen
                    if(!c.getIsgroup()){
                        List<Nutzer> nutzerList = c.getNutzerList();
                        Nutzer n = nutzerEJB.getCopyById(nutzerid);
                        nutzerList.remove(n);

                        Nutzer andererNutzer = nutzerList.get(0);
                        c.setName(andererNutzer.getBenutzername());
                        c.setProfilbild(andererNutzer.getProfilbild());
                        c.setBeschreibung(andererNutzer.getInfo());
                        c.setNutzerList(null);

                        if(self.getHatBlockiert().contains(andererNutzer)){
                            c.setIsblocked(Boolean.TRUE);
                        }
                        if(andererNutzer.getHatBlockiert().contains(self)){
                            c.setGotblocked(Boolean.TRUE);
                        }
                    }
                    //Listen auf null setzen
                    c.setAdminList(null);
                    c.setNutzerList(null);
                    //anzahl neuer Nachrichten zählen
                    try{
                        c.setNnew(0);
                        List<Nachricht> nList = nachrichtEJB.getDetachedByChat(c.getChatid());
                        for(Nachricht n : nList){
                            if(!n.getNutzerList().contains(self)){
                                if(n.getDatumuhrzeit() < System.currentTimeMillis()){
                                    c.setNnew(c.getNnew() + 1);
                                }
                            }
                        }
                    }catch(EJBTransactionRolledbackException e){

                    }
                }

                //archivierte Chats in seperate Liste schreiben
                Nutzer selfInDB = nutzerEJB.getById(nutzerid);
                List<Chat> archivedChats = new ArrayList<>();
                List<Chat> archivedDB = selfInDB.getArchivedChats();
                for(Chat c : ownChatListDB){
                    for(Chat chat : archivedDB){
                        if(Objects.equals(c.getChatid(), chat.getChatid())){
                            archivedChats.add(c);
                        }
                    }
                }

                //angepinnte Chats aus Liste entfernen
                List<Chat> pinnedChats = new ArrayList<>();
                for(Chat c : ownChatListDB){
                    if(self.getPinnedChats().contains(c)){
                        c.setIspinned(Boolean.TRUE);
                        pinnedChats.add(c);
                    }
                }
                ownChatListDB.removeAll(pinnedChats);

                //alle Chats, die keine Nachricht enthalten, vor dem
                //Sortieren entfernen und danach wieder einfügen
                List<Chat> noLastMessage = new ArrayList<>();
                for(Chat c : ownChatListDB){
                    if(c.getLetztenachricht() == null){
                        noLastMessage.add(c);
                    }
                }
                ownChatListDB.removeAll(noLastMessage);
                ownChatListDB.sort(new DateSorterChat());
                ownChatListDB.addAll(noLastMessage);
                //gepinnte Chats nach Name sortieren
                pinnedChats.sort(new NameSorter());
                List<Chat> returnListFinal = new ArrayList<>();
                returnListFinal.addAll(pinnedChats);

                returnListFinal.addAll(ownChatListDB);

                returnListFinal.removeAll(archivedChats);
                JsonObject returnObject = new JsonObject();

                Gson parser = new Gson();

                returnObject.add("normal", parser.toJsonTree(returnListFinal));
                returnObject.add("archived", parser.toJsonTree(archivedChats));

                return response.generateAnswer(parser.toJson(returnObject));
            }catch(EJBTransactionRolledbackException e){
                return response.generateError401("Id nicht vorhanden");
            }
        }

    }

    /**
     * Die folgende Methode erstellt eine neue Gruppe mit den gegeben Daten.
     * Dabei wird der eigene Nutzer als einziger Admin der Gruppe gesetzt.
     *
     * @param Daten Die Chatinformationen, die eigene Id und eine Liste aus
     * Nutzern, die hinzugefügt werden soll.
     * @param token Das Webtoken.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/createAsGroup/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAsGroup(String Daten,
            @PathParam("token") String token
    ){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();
            try{
                JsonObject json = parser.fromJson(Daten, JsonObject.class
                );
                Chat neuerChat = parser.fromJson(Daten, Chat.class
                );
                neuerChat.setIsgroup(true);
                chatEJB.createChat(neuerChat);
                int eigeneId = parser.fromJson((json.get("eigeneId")), Integer.class
                );

                try{
                    Thread.sleep(200);
                }catch(Exception e){
                }

                JsonArray arr = json.getAsJsonArray("benutzernamen");
                for(int i = 0; i < arr.size(); i++){
                    Nutzer addedUser = nutzerEJB.getByUsername(arr.get(i).getAsString());
                    nutzerEJB.fuegeChatHinzu(neuerChat, addedUser);
                    chatEJB.addUser(neuerChat, addedUser);

                }
                Nutzer self = nutzerEJB.getById(eigeneId);
                nutzerEJB.fuegeChatHinzu(neuerChat, self);
                chatEJB.addUser(neuerChat, self);
                chatEJB.addAdmin(neuerChat, self);
                return response.generateAnswer("true");
            }catch(JsonSyntaxException e){
                return response.generateError406("Json falsch");
            }
        }

    }

    /**
     * Diese Methode erstellt einen neuen Einzelchat zwsichen dem eigenen Nutzer
     * und einem gegebenen anderen Nutzer.
     *
     * @param Daten Die Informationen zur Identifizierung der beiden Nutzer.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/createAsChat/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAsChat(String Daten,
            @PathParam("token") String token
    ){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();
            try{

                JsonObject jsonTP = parser.fromJson(Daten, JsonObject.class
                );
                String username = parser.fromJson((jsonTP.get("benutzername")), String.class
                );
                BigInteger date = parser.fromJson((jsonTP.get("erstelldatum")), BigInteger.class
                );
                int eigeneId = parser.fromJson((jsonTP.get("eigeneId")), Integer.class
                );

                Nutzer self = nutzerEJB.getById(eigeneId);
                Nutzer other = nutzerEJB.getByUsername(username);

                boolean notAvailable = true;
                //Anfrage, ob schon vorhanden
                for(Chat c : chatEJB.getAll()){
                    List<Nutzer> nutzerList = new ArrayList();
                    for(Nutzer n : c.getNutzerList()){
                        nutzerList.add(n);
                    }
                    if(nutzerList.size() == 2 && nutzerList.contains(self) && nutzerList.contains(other)){
                        notAvailable = false;
                    }
                }

                if(notAvailable){

                    Chat neuerChat = new Chat();
                    neuerChat.setIsgroup(false);
                    neuerChat.setErstelldatum(date);
                    neuerChat.setNutzerList(null);
                    neuerChat.setAdminList(null);
                    Chat neuerChatDB = chatEJB.createChat(neuerChat);

                    try{
                        if(neuerChatDB != null){
                            chatEJB.addUser(neuerChatDB, self);
                            chatEJB.addUser(neuerChatDB, other);

                            nutzerEJB.fuegeChatHinzu(neuerChatDB, self);
                            nutzerEJB.fuegeChatHinzu(neuerChatDB, other);

                            chatEJB.addAdmin(neuerChatDB, other);
                            chatEJB.addAdmin(neuerChatDB, self);

                            return response.generateAnswer("true");
                        }else{
                            return response.generateError500("Nicht verfügbar");
                        }
                    }catch(EJBTransactionRolledbackException e){
                        return response.generateError406("ID oder Benutzername nicht vorhanden2");
                    }
                }else{
                    return response.generateError406("schon vorhanden");
                }

//                self.getPinnedChats().remove(neuerChat);
//                other.getPinnedChats().remove(neuerChat);
            }catch(JsonSyntaxException e){
                return response.generateError401("Json falsch");
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("ID oder Benutzername nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methode fügt einen Nutzer zu einer Gruppe hinzu. Das kann nur ausgeführt werden, falls man
     * selbst ein Admin der Gruppe ist.
     *
     * @param Daten Die Informationen zum eigenen und anderen Nutzer, sowie zum
     * Chat.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/takepart/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(String Daten,
            @PathParam("token") String token
    ){

        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();

            try{
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );

                int chatid = parser.fromJson((jsonObject.get("chatid")), Integer.class
                );
                Chat c = chatEJB.getById(chatid);
                int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
                );
                Nutzer self = nutzerEJB.getById(jsonId);
                String username = parser.fromJson((jsonObject.get("benutzername")), String.class
                );
                Nutzer addedUser = nutzerEJB.getCopyByUsername(username);

                if(c.getAdminList().contains(self)){

                    if(c.getIsgroup()){
                        if(!c.getNutzerList().contains(addedUser)){
                            nutzerEJB.fuegeChatHinzu(c, addedUser);

                            chatEJB.addUser(c, addedUser);

                            return response.generateAnswer("true");
                        }else{
                            return response.generateError406("Nutzer schon hinzugefügt");
                        }
                    }else{
                        return response.generateError406("Ist ein Chat, keine Gruppe");
                    }

                }else{
                    return response.generateError406("Du bist kein Admin");
                }

            }catch(JsonSyntaxException e){
                return response.generateError406("JsonSyntaxException" + e);
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("ID oder Benutzername nicht vorhanden");
            }catch(NullPointerException e){
                return response.generateError406("ID oder Benutzername nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methode entfernt einen Nutzer aus einer Gruppe. Das kann nur ausgeführt werden, falls man
     * selbst ein Admin der Gruppe ist.
     *
     * @param Daten Die Informationen zum eigenen und anderen Nutzer, sowie zum
     * Chat.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/entferneNutzer/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(String Daten,
            @PathParam("token") String token
    ){

        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();

            try{
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );
                int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
                );
                int jsonChatId = parser.fromJson((jsonObject.get("chatId")), Integer.class
                );
                String jsonUsername = parser.fromJson((jsonObject.get("andererBenutzername")), String.class
                );

                Nutzer self = nutzerEJB.getById(jsonId);
                Nutzer other = nutzerEJB.getByUsername(jsonUsername);
                Chat c = chatEJB.getById(jsonChatId);

                if(self.equals(other)){
                    nutzerEJB.entferneChat(c, self);
                    chatEJB.removeUser(c, self);
                    return response.generateAnswer("true");
                }else{

                    if(c.getAdminList().contains(self)){

                        if(c.getIsgroup()){

                            if(c.getNutzerList().contains(other)){

                                nutzerEJB.entferneChat(c, other);

                                chatEJB.removeUser(c, other);

                                return response.generateAnswer("true");
                            }else{
                                return response.generateError406("Nutzer nicht in der Gruppe");
                            }
                        }else{
                            return response.generateError406("Ist ein Chat, keine Gruppe");
                        }

                    }else{
                        return response.generateError406("Du bist kein Admin");
                    }
                }
//                 return response.generateAnswer("true");

            }catch(JsonSyntaxException e){
                return response.generateError406("JsonSyntaxException" + e);
            }catch(EJBTransactionRolledbackException e){
                return response.generateError406("ID oder Benutzername nicht vorhanden");
            }
        }

    }

    /**
     * Mit dieser Methode kann ein Nutzer eine Gruppe verlassen. Das ist aber nur möglich, wenn man selbst nicht der einzige Admin
     * ist.
     *
     * @param token
     * @param Daten
     * @return Das responseobjekt mit dem Statsu der Methode.
     */
    @POST
    @Path("/leave/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response leave(@PathParam("token") String token, String Daten
    ){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{

            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
            );
            int jsonChatId = parser.fromJson((jsonObject.get("chatId")), Integer.class
            );

            Nutzer self = nutzerEJB.getById(jsonId);
            Chat c = chatEJB.getById(jsonChatId);
            if(c.getAdminList().size() == 1 && c.getAdminList().contains(self)){
                if(c.getNutzerList().size() == 1){
                    nutzerEJB.entferneChat(c, self);
                    chatEJB.removeUser(c, self);
                    return response.generateAnswer("true");
                }else{
                    return response.generateError406("Einziger Admin");
                }

            }else{
                nutzerEJB.entferneChat(c, self);
                chatEJB.removeUser(c, self);
                return response.generateAnswer("true");
            }
        }

    }

    /**
     * Diese Methode macht einen anderen Nutzer zum Admin der Gruppe. Dabei wird
     * überprüft, ob man selbst ein Admin ist.
     *
     * @param token Das Webtoken
     * @param Daten Die Informationen zum eigenen und anderen Nutzer, sowie zum
     * Chat.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/zuAdmin/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response promoteToAdmin(@PathParam("token") String token, String Daten
    ){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{

            Gson parser = new Gson();

            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int jsonId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
            );
            int jsonChatId = parser.fromJson((jsonObject.get("chatId")), Integer.class
            );
            String jsonUsername = parser.fromJson((jsonObject.get("andererBenutzername")), String.class
            );

            Nutzer self = nutzerEJB.getById(jsonId);
            Nutzer other = nutzerEJB.getByUsername(jsonUsername);
            Chat c = chatEJB.getById(jsonChatId);

            if(c.getAdminList().contains(self)){
                if(c.getAdminList().contains(other)){
                    if(c.getAdminList().size() == 1){
                        return response.generateError406("Einziger Admin");
                    }
                    if(!c.getAdminList().contains(other)){
                        return response.generateError406("Bereits kein Admin");
                    }else{
                        chatEJB.deleteAdmin(c, other);
                        return response.generateAnswer("true");
                    }
                }else{
                    chatEJB.addAdmin(c, other);
                    return response.generateAnswer("true");
                }

            }else{
                return response.generateError406("Kein Admin");
            }
        }
    }

    /**
     * Mit dieser Methode kann ein Nutzer einen Chat anpinnen, was bedeutet dass dieser Chat über den anderen
     * in der eigenen Chatliste angezeigt wird. Die angepinnten Chats eines Nutzers werden dem Benutzernamen nach absteigend sortiert
     * Hat ein Nutzer den angebenen Chat bereits angepinnt, wird die Fixierung des Chats aufgehoben.
     *
     * @param token Das Webtoken
     * @param Daten Die Daten zum Chat und dem eigenen Nutzer.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/pin/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response pinChat(@PathParam("token") String token, String Daten
    ){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class
            );
            int eigeneId = parser.fromJson((jsonObject.get("eigeneid")), Integer.class
            );

            chatEJB.pin(chatId, eigeneId);

            return response.generateAnswer("true");
        }

    }

    /**
     * Diese Methode archiviert einen Chat für einen Nutzer, das heißt der Chat wird nicht in der
     * normalen Chatliste angezeigt, sondern ist in einer seperat anzeigbaren Liste vorhanden.
     * Ist der angegebene Chat bereits archiviert, wird die Archivierung aufgehoben und der Chat
     * wird wieder normal angezeigt.
     *
     * @param token Das Webtoken.
     * @param Daten Die Daten zum Chat und eigenem Nutzer.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/archive/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response archiveChat(@PathParam("token") String token, String Daten
    ){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class
            );
            int eigeneId = parser.fromJson((jsonObject.get("eigeneid")), Integer.class
            );

            chatEJB.archive(chatId, eigeneId);

            return response.generateAnswer("true");
        }

    }

    /**
     * Diese Methode löscht einen Chat.
     *
     * @param token Das Webtoken
     * @param id Die Id des Chats
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @GET
    @Path("delete/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteChat(@PathParam("token") String token, @PathParam("id") int id){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();

            try{

                Chat c = chatEJB.getById(id);
                c.setNutzerList(new ArrayList<>());
                List<Nutzer> adminList = c.getAdminList();

                for(Nutzer n : adminList){
                    n.getArchivedChats().remove(c);
                }

                chatEJB.delete(c);

                return response.generateAnswer("true");
            }catch(Exception e){
                return response.generateError500("Fehler");
            }
        }
    }

    /**
     * Diese Methode setzt das Profilbild einer Gruppe. Dabei wird überprüft, ob
     * man selbst ein Admin ist.
     *
     * @param token Das Webtoken
     * @param Daten Die Informationen zum Chat, eigenen Nutzer und das
     * Profilbild.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/change/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setProfilpic(@PathParam("token") String token, String Daten){
        if(!verify(token)){
            return response.generateError401("Ungültiges Token");
        }else{
            Gson parser = new Gson();

            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class);
            int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class);
            int eigeneId = parser.fromJson((jsonObject.get("eigeneid")), Integer.class);
            String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
            String name = parser.fromJson((jsonObject.get("name")), String.class);
            String beschreibung = parser.fromJson((jsonObject.get("beschreibung")), String.class);

            Nutzer self = nutzerEJB.getById(eigeneId);
            Chat chat = chatEJB.getById(chatId);

            if(chat.getAdminList().contains(self)){
                if(jsonPic != null){
                    Foto f = new Foto();
                    f.setBase64(jsonPic);
                    fotoEJB.add(f);

                    Foto fotoInDB = fotoEJB.getByBase64(jsonPic);

                    chat.setProfilbild(fotoInDB);
                }
                if(name != null){
                    chat.setName(name);
                }
                if(beschreibung != null){
                    chat.setBeschreibung(beschreibung);
                }

                return response.generateAnswer("true");

            }else{
                return response.generateError401("Du bist kein Admin");
            }
        }

    }

}
