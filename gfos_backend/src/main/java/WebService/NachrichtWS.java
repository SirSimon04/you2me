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
import Service.Antwort;
import Service.FileService;
import Service.Mail;
import Service.Tokenizer;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;

/**
 * <h1>Der Webserver für die Datenverarbeitung, bezogen auf die Nachrichten.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden des Webservers bezogen auf das Objekt
 * der Nachrichten für das Bearbeiten und Ausgeben der Daten und stellt damit
 * die Schnittstelle mit dem Frontend dar</p>
 */
@Path("/nachricht")
@Stateless
@LocalBean
public class NachrichtWS{

    @EJB
    private NachrichtEJB nachrichtEJB;
    @EJB
    private FotoEJB fotoEJB;
    @EJB
    private ChatEJB chatEJB;
    @EJB
    private NutzerEJB nutzerEJB;
    @EJB
    private Tokenizer tokenizer;
    @EJB
    private BlacklistEJB blacklistEJB;
    @EJB
    private SettingsEJB settingsEJB;
    private Mail mail = new Mail();

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
     * Diese Methode gibt alle Nachrichten zurück.
     *
     * @param token Das Webtoken
     * @return Die Liste mit allen Nachrichten
     */
    @GET
    @Path("/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
            //return "false";
        }else{
            Gson parser = new Gson();
            List<Nachricht> alleNachrichten = nachrichtEJB.getAll();
            return response.generateAnswer(parser.toJson(alleNachrichten));
            //return "true";
        }

    }

    @GET
    @Path("/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Nachricht n = nachrichtEJB.getCopyByIdListsNull(id);

            FileService file = new FileService();
            String filename = n.getInhalt().substring(3, n.getInhalt().length());
            String base64 = file.read(filename);
            n.setInhalt(base64);
            Gson parser = new Gson();
            return response.generateAnswer(parser.toJson(n));
        }
    }

    /**
     * Diese Methode liefert alle Nachrichten eines bestimmten Chats zurück. Sobald ein Nutzer die neuen Nachrichten eines Chats lädt, bedeutet dass,
     * das der Nutzer diese gelesen hat. Die nachrichten werden dem Datum nach absteigend sortiert.
     *
     * @param chatid Die Id des Chats, aus dem die Nachrichten angezeigt werden
     * solln
     * @param token Das Webtoken
     * @param nutzerid Die eigene Id.
     * @return Die Liste mit den Nachrichten.
     */
    @GET
    @Path("/chat/{id}/{nutzerid}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByChat(@PathParam("id") int chatid, @PathParam("token") String token, @PathParam("nutzerid") int nutzerid) throws IOException{
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Nutzer self = nutzerEJB.getById(nutzerid);
            List<Nachricht> list = nachrichtEJB.getByChat(chatid);
            Chat c = chatEJB.getById(chatid);
            for(Nachricht n : list){
                if(n.getDatumuhrzeit() < System.currentTimeMillis()){

                    if(!n.getNutzerList().contains(self)){ //Nachricht wird von self gelesen
                        n.getNutzerList().add(self);
                    }
                    if(n.getNutzerList().size() == c.getNutzerList().size()){
                        n.setReadbyall(Boolean.TRUE);
                    }
                }

            }
            Gson parser = new Gson();
            List<Nachricht> nList = nachrichtEJB.getCopyByChat(chatid);
            List<Nachricht> newN = new ArrayList<>();
            for(Nachricht n : nList){
                if(n.getDatumuhrzeit() > System.currentTimeMillis()){
                    if(n.getSenderid() != self.getId()){
                        newN.add(n);
                    }else{
                        n.setIsplanned(Boolean.TRUE);
                    }
                }

                if(n.getIsFile()){
                    String filename = n.getInhalt().substring(n.getInhalt().lastIndexOf("|") + 1, n.getInhalt().length());
                    n.setInhalt(filename);
//                    n.setInhalt("haha");;
                }
            }
            nList.removeAll(newN);

            if(self.getSetting().getWordfilter()){
                for(Nachricht n : nList){
//                    if(!n.getIsFile()){
                    if(filter.isProfane(n.getInhalt())){
                        n.setInhalt(filter.filter(n.getInhalt()));
                    }
//                    }
                }
            }
            if(nList.size() > 1){
                Collections.sort(nList, (Nachricht z1, Nachricht z2) -> {
                    if(z1.getDatumuhrzeit() > z2.getDatumuhrzeit()){
                        return 1;
                    }
                    if(z1.getDatumuhrzeit() < z2.getDatumuhrzeit()){
                        return -1;
                    }
                    return 0;
                });
            }

            List<Nachricht> markedMessages = self.getMarkedMessages();
            for(Nachricht n : nList){
                if(markedMessages.contains(n)){
                    n.setIsMarked(true);
                }
            }

            return response.generateAnswer(parser.toJson(nList));
        }

    }

    /**
     * Diese Methode liefert die neuste Nachricht aus einem Chat. Sie wird vom
     * Client dazu genutzt, zu überprüfen, ob eine neue Nachricht im Chat
     * vorhanden ist. Ist das der Fall, werden alle Nachrichten neu geladen.
     *
     * @param id Die Id des Chats
     * @param token Das Webtoken
     * @return Die neueste Nachricht
     */
    @GET
    @Path("/chat/getNewest/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewest(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();
            Nachricht n = nachrichtEJB.getNewest(id);
            return response.generateAnswer(parser.toJson(n));
        }
    }

    /**
     * Diese Methode gibt die Informationen einer Nachricht zurück. Das
     * beinhaltet zwei Listen. Die eine zeigt alle Nutzer, die die Nachricht
     * noch nicht gelesen haben und die andere, welche die Nachricht gelesen
     * haben. Dabei ist die zweite Liste so sortiert, dass die Nutezr die die
     * Nachricht eher gelesen haben am Anfang der Liste stehen.
     *
     * @param id Die Id der Nachricht
     * @param token Das Webtoken
     * @return Die Nachrichteninformationen
     */
    @GET
    @Path("/info/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfo(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Nachricht n = nachrichtEJB.getCopyById(id);

            List<Nutzer> gelesenVon = new ArrayList<>();
            gelesenVon.addAll(n.getNutzerList());

            List<Nutzer> nichtGelesenVon = chatEJB.getCopy(n.getChatid()).getNutzerList();
            nichtGelesenVon.removeAll(gelesenVon);

            Gson parser = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("gelesenVon", parser.toJsonTree(gelesenVon));
            jsonObject.add("nichtgelesenVon", parser.toJsonTree(nichtGelesenVon));

            return response.generateAnswer(parser.toJson(jsonObject));
//                return response.generateAnswer(parser.toJson(nachrichtEJB.getCopyById(id)));

        }
    }

    /**
     * Diese Methode sendet eine Nachricht in einen Chat. Wenn es sich bei der
     * Nachricht um eine wichtige handelt, wird an alle Nutzer des Chats eine
     * E-Mail mit der Nachricht gesendet. Diese E-Mail wird aber nur verschickt,
     * wenn die Nutzer diese Einstellung nicht deaktiviert haben. Außerdem wird
     * vorher überprüft, ob einer der Nutzer den anderen blockiert hat, in
     * diesem Fall wird die Nachircht nicht gesendet.
     *
     * @param Daten Die Daten zum Chat und der Nachricht.
     * @param token Das Webtoken
     * @return Das Responseobkjekt mit dem Status der Methode.
     */
    @POST
    @Path("/add/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response send(String Daten, @PathParam("token") String token) throws IOException, MessagingException, AddressException, InterruptedException{

        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{

            Gson parser = new Gson();
            try{
                int jsonAnswerId = 0;
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );
                Nachricht neueNachricht = parser.fromJson(Daten, Nachricht.class);
                String jsonPic = parser.fromJson((jsonObject.get("base64")), String.class);
                int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class);
                int senderId = parser.fromJson((jsonObject.get("senderid")), Integer.class);
                //Antwort auf eine andere Nachricht
                try{
                    jsonAnswerId = parser.fromJson((jsonObject.get("answerId")), Integer.class
                    );
                    Nachricht nachrichtInDB = nachrichtEJB.getById(jsonAnswerId);
                    neueNachricht.setAntwortauf(nachrichtInDB);
                }catch(NullPointerException e){

                }
//                System.out.println(neueNachricht.getIsplanned());
                //Foto verschicken
                if(jsonPic != null){
                    Foto f = new Foto();
                    f.setBase64(jsonPic);
                    fotoEJB.add(f);

                    Foto fotoInDB = fotoEJB.getByBase64(jsonPic);
                    neueNachricht.setFoto(fotoInDB);
                }
                Chat c = chatEJB.getById(chatId);
                Nutzer self = nutzerEJB.getById(senderId);
                if(!c.getIsgroup()){
                    //check if someone is blockeds
                    Chat copy = chatEJB.getCopyListsNotNull(chatId);

                    copy.getNutzerList().remove(self);
                    Nutzer other = copy.getNutzerList().get(0);
                    if(self.getHatBlockiert().contains(other)){
                        return response.generateError406("Du hast diesen Nutzer blockiert");
                    }
                    if(other.getHatBlockiert().contains(self)){
                        return response.generateError406("Du bist von diesem Nutzer blockiert");
                    }
                }
                //wenn wichtig, dann mail versenden
                try{
                    if(neueNachricht.getIsimportant()){
                        System.out.println("HIIIIIIIIIEEEEEEEEEEEEERRRRRRRR");
                        Chat cc = chatEJB.getCopyListsNotNull(neueNachricht.getChatid());
                        List<Nutzer> nList = cc.getNutzerList();
                        nList.remove(self);
                        Nutzer nutzer = nutzerEJB.getById(1);
                        String mailFrom = nutzer.getEmail();
                        String pw = nutzer.getPasswordhash();
                        for(Nutzer n : nList){
//                            System.out.println(n.getSetting() == null);
                            Setting s = settingsEJB.getById(n.getId());
                            System.out.println(s.getMailifimportant());
//                            System.out.println(n.getSetting().getMailifimportant());
//                            if(n.getSetting().getMailifimportant()){
                            if(s.getMailifimportant()){
                                String msg = "";
                                if(cc.getIsgroup()){
                                    msg += "<h2>Hallo " + n.getBenutzername() + ",</h2>"
                                            + "<p>in der Gruppe " + cc.getName() + " wurde von "
                                            + nutzerEJB.getById(neueNachricht.getSenderid()).getBenutzername()
                                            + " eine wichtige Nachricht gesendet.</p>"
                                            + "<p>Sie lautet: \""
                                            + neueNachricht.getInhalt()
                                            + "\"</p>"
                                            + "<p>Falls du keine Benachhrichtigungen mehr erhalten willst, "
                                            + "wenn jemand eine wichtige Nachricht schickt, kannst du das "
                                            + "in den Einstellungen ausschalten.</p>"
                                            + "<h3>Mit freundlichen Grüßen,</h3>"
                                            + "<h3>dein Disputatio-Team</h3>";
                                    mail.sendImportantMessage(mailFrom, pw, n.getEmail(), msg);

                                }else{
                                    msg += "<h2>Hallo " + n.getBenutzername() + ",</h2>"
                                            + "<p>"
                                            + nutzerEJB.getById(neueNachricht.getSenderid()).getBenutzername()
                                            + " hat dir eine wichtige Nachricht gesendet.</p>"
                                            + "<p>Sie lautet: \""
                                            + neueNachricht.getInhalt()
                                            + "\"</p>"
                                            + "<p>Falls du keine Benachhrichtigungen mehr erhalten willst, "
                                            + "wenn jemand eine wichtige Nachricht schickt, kannst du das "
                                            + "in den Einstellungen ausschalten.</p>"
                                            + "<h3>Mit freundlichen Grüßen,</h3>"
                                            + "<h3>dein Disputatio-Team</h3>";
                                    mail.sendImportantMessage(mailFrom, pw, n.getEmail(), msg);
                                }
                            }
                        }

//                        }
                    }
                }catch(NullPointerException e){
                    response.generateAnswer("Komisch");
                }
                neueNachricht.setIsFile(false);
                chatEJB.getById(chatId).setLetztenachricht(neueNachricht);
                neueNachricht.setIsplanned(Boolean.FALSE);
                nachrichtEJB.add(neueNachricht);
                return response.generateAnswer("validé");
            }catch(JsonSyntaxException e){
                return response.generateError406("cacahuète");
            }catch(NullPointerException e){
                return response.generateError500("Chatid oder Senderid nicht vorhanden");
            }
        }

    }

    /**
     * Diese Methode bearbeitet eine Nachricht. Dabei wird geprüft, ob die Nachricht von einem selbst gesendet
     * wurde, die Nachricht kann nur bearbeitet werden, wenn das der Fall ist. Der Status der Nachricht wird
     * auf bearbeitet gesetzt, um hinterher zu kennzeichnen, dass die Nachircht bearbeitet wurde.
     *
     * @param Daten Die Daten zur Nachricht und einem selbst
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     * @throws IOException
     * @throws MessagingException
     * @throws AddressException
     * @throws InterruptedException
     */
    @POST
    @Path("/edit/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editMessage(String Daten, @PathParam("token") String token) throws IOException, MessagingException, AddressException, InterruptedException{

        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{

            Gson parser = new Gson();
            try{
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );
                int nachrichtId = parser.fromJson((jsonObject.get("nachrichtid")), Integer.class);
                int senderId = parser.fromJson((jsonObject.get("senderid")), Integer.class);
                String inhalt = parser.fromJson((jsonObject.get("inhalt")), String.class);

                Nachricht n = nachrichtEJB.getById(nachrichtId);

                if(n.getSenderid() == senderId){
                    n.setIsEdited(true);
                    n.setInhalt(inhalt);
                    return response.generateAnswer("true");
                }else{
                    return response.generateAnswer("Nicht deine Nachricht");
                }

            }catch(NullPointerException e){
                return response.generateAnswer("Fehler");
            }
        }
    }

    /**
     * Diese Nachricht sendet eine Datei in einen Chat. Sie wird in Form eines Base-64-Strings übergeben.
     * Dieser Base64-String wird in eine Datei geschrieben, aus welcher der String wieder ausgelesen
     * werden kann.
     *
     * @param Daten Die Daten zur Datei und zur Nachricht.
     * @param token Das Webtoken.
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/addFile/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendFile(String Daten, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{

            Gson parser = new Gson();
            try{
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );
                Nachricht neueNachricht = parser.fromJson(Daten, Nachricht.class
                );
                int chatId = parser.fromJson((jsonObject.get("chatid")), Integer.class
                );
                String jsonFilename = parser.fromJson((jsonObject.get("filename")), String.class
                );

                //check if someone is blocked
                Chat c = chatEJB.getById(chatId);
                Chat copy = chatEJB.getCopyListsNotNull(chatId);
                Nutzer self = nutzerEJB.getById(neueNachricht.getSenderid());
                copy.getNutzerList().remove(self);
                Nutzer other = copy.getNutzerList().get(0);
                if(!c.getIsgroup()){
                    if(self.getHatBlockiert().contains(other)){
                        return response.generateError406("Du hast diesen Nutzer blockiert");
                    }
                    if(other.getHatBlockiert().contains(self)){
                        return response.generateError406("Du bist von diesem Nutzer blockiert");
                    }
                }

                String filename = "";
                filename += neueNachricht.getSenderid() + "|" + neueNachricht.getDatumuhrzeit() + "|" + jsonFilename;

                FileService file = new FileService();
                file.create(filename);
                file.write(filename, neueNachricht.getInhalt());
//                CreateFile create = new CreateFile();
//                create.create(filename);
//                WriteToFile write = new WriteToFile();
//                write.write(filename, neueNachricht.getInhalt());

                neueNachricht.setInhalt("|||" + filename);
                neueNachricht.setIsFile(true);
                chatEJB.getById(chatId).setLetztenachricht(neueNachricht);
                nachrichtEJB.add(neueNachricht);

                return response.generateAnswer("true");
            }catch(Exception e){
                return response.generateError500("Fehler");
            }
        }
    }

    /**
     * Diese Methode löscht eine Nachricht aus einem Chat. Das gilt für alle
     * Nutzer. Handelt es sich um eine Nachricht in einem persönlichen Channel,
     * dann wird überprüft, ob der Nutzer aus den übertragenen Daten gleich dem
     * zum Token gehörigen Nutzer ist, um zu vermeiden, dass andere Nutzer
     * Nachrichten aus dem Channel eines Nutzers löschen können. Bei einem
     * normalen Chat oder einer Gruppe wird aus demselben Grund überprüft, ob
     * der Nutzer an der Gruppe oder dem Chat teilnimmt.
     *
     * @param Daten Die Daten zur Nachricht.
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode.
     */
    @POST
    @Path("/delete/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String Daten, @PathParam("token") String token){

        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{

            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int lNachrichtId = parser.fromJson((jsonObject.get("nachrichtid")), Integer.class
            );
            int eigeneId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
            );
            Nachricht n = nachrichtEJB.getById(lNachrichtId);
            Nutzer self = nutzerEJB.getById(eigeneId);

            String username = tokenizer.getUser(token);

            if(n.getSenderid() == 0){
                if(nutzerEJB.getByUsername(username).getChannel().getChatid() == n.getChatid()){
                    nachrichtEJB.delete(lNachrichtId);
                    return response.generateAnswer("true");
                }else{
                    return response.generateError406("Keine Berechtigung");
                }
            }

            if(n.getSenderid() == eigeneId){
                List<Nutzer> l = chatEJB.getById(n.getChatid()).getNutzerList();

                if(l.contains(self)){
                    if(chatEJB.getById(n.getChatid()).getLetztenachricht().equals(n)){

                        chatEJB.getById(n.getChatid()).setLetztenachricht(null);

                        nachrichtEJB.delete(lNachrichtId);

                        List<Nachricht> nList = nachrichtEJB.getCopyByChat(n.getChatid());
                        if(nList.size() != 0){
                            chatEJB.getById(n.getChatid()).setLetztenachricht(nList.get(nList.size() - 1));
                        }

                        return response.generateAnswer("true");

                    }else{

                        nachrichtEJB.delete(lNachrichtId);
                        return response.generateAnswer("true");
                    }
                }else{
                    return response.generateError406("Nicht im Chat");
                }
            }else{
                return response.generateAnswer("Nicht deine Nachricht");
            }

        }

    }

    /**
     * Diese Methode markiert eine Nachricht für einen Nutzer und fügt diese zu
     * seiner Sammlung mit markierten Nachrichten hinzu. Ist die Nachricht schon
     * markiert, wird die Markierung aufgehoben.
     *
     * @param Daten Die Daten zum eigenen Nutzer und zur Nachricht
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/mark/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMessage(String Daten, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{
            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int nId = parser.fromJson((jsonObject.get("nachrichtid")), Integer.class
            );
            int eigeneId = parser.fromJson((jsonObject.get("eigeneid")), Integer.class
            );

            Nachricht na = nachrichtEJB.getById(nId);
            Nutzer nu = nutzerEJB.getById(eigeneId);

            try{
                nachrichtEJB.markMessage(na, nu);
                return response.generateAnswer("true");
            }catch(Exception e){
                return response.generateError406("Fehler");
            }

        }
    }

    /**
     * Diese Methode gibt die markierten Nachrichten eines Nutzers wieder. Da es
     * sich dabei um Nachrichten aus allen beliebigen Chats handeln kann, wird
     * der Name des Chats auch übergeben.
     *
     * @param id Die Id des Nutzers
     * @param token Das Webtoken
     * @return Die markierten Nachrichten
     */
    @GET
    @Path("/getMarked/{id}/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMarked(@PathParam("id") int id, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("Kein gültiges Token");
        }else{
            Gson parser = new Gson();
            List<Nachricht> nList = nachrichtEJB.getMarkedMessages(id);
            return response.generateAnswer(parser.toJson(nList));
        }

    }

    /**
     * Diese Methode sendet eine Nachricht in den persönlichen Channel eines
     * Nutzers.
     *
     * @param Daten Die Dateninformationen
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     * @throws IOException
     * @throws MessagingException
     * @throws AddressException
     * @throws InterruptedException
     */
    @POST
    @Path("/sendInChannel/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendInChannel(String Daten, @PathParam("token") String token) throws IOException, MessagingException, AddressException, InterruptedException{

        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{

            Gson parser = new Gson();
            try{
                JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
                );
                Nachricht neueNachricht = parser.fromJson(Daten, Nachricht.class
                );

                int eigeneId = parser.fromJson((jsonObject.get("eigeneId")), Integer.class
                );

                neueNachricht.setChatid(nutzerEJB.getById(eigeneId).getChannel().getChatid());
                neueNachricht.setCountLikes(0);
                nachrichtEJB.add(neueNachricht);

                return response.generateAnswer("true");

            }catch(JsonSyntaxException e){
                return response.generateError406("cacahuète");
            }catch(NullPointerException e){
                return response.generateError500("Chatid oder Senderid nicht vorhanden");
            }

        }
    }

    /**
     * Diese Methode fügt einer Nachricht einen Like hinzu. Sie ist nur auf
     * Nachrichten, die sich in einem Channel befinden, aufrufbar.
     * Hat die Nachricht vom eigenen Nutzer schon einen Like erhalten, so wird dieser entfernt.
     *
     * @param Daten Die Daten zum eigenen Nutzer und zur Nachricht
     * @param token Das Webtoken
     * @return Das Responseobjekt mit dem Status der Methode
     */
    @POST
    @Path("/like/{token}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response like(String Daten, @PathParam("token") String token){
        if(!verify(token)){
            return response.generateError401("dentifrisse");
        }else{
            Gson parser = new Gson();
            JsonObject jsonObject = parser.fromJson(Daten, JsonObject.class
            );
            int nId = parser.fromJson((jsonObject.get("nachrichtid")), Integer.class
            );
            int eigeneId = parser.fromJson((jsonObject.get("eigeneid")), Integer.class
            );

            Nachricht na = nachrichtEJB.getById(nId);
            Nutzer nu = nutzerEJB.getById(eigeneId);

            try{
                if(!na.getLikedBy().contains(nu)){
                    na.getLikedBy().add(nu);
                    na.setCountLikes(na.getLikedBy().size());
                    System.out.println(na.getLikedBy().size());
                    return response.generateAnswer("true");
                }else{
                    na.getLikedBy().remove(nu);
                    na.setCountLikes(na.getLikedBy().size());
                    return response.generateError406("true");
                }
            }catch(Exception e){
                return response.generateError406("Fehler");
            }
        }
    }
}
