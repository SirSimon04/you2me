/**
 * <h>Die Datenbankzugriffsschicht.</h1>
 */
package EJB;

import Entity.Blacklist;
import Entity.Chat;
import Entity.Nachricht;
import Entity.Nutzer;
import Service.Tokenizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * <h1>Die Klasse zum Verwalten der Nutzer in der Datenbank.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der
 * Datenbank bezogen auf die Nutzer. Die Daten werden bei Anfrage des Webservers
 * übergeben.</p>
 *
 * @author simon
 */
@Stateless
@LocalBean
public class NutzerEJB{

    @PersistenceContext
    private EntityManager em;
    @EJB
    private Tokenizer tokenizer;
    @EJB
    private ChatEJB chatEJB;

    /**
     * Diese Methode gibt die Anmeldedaten für den SMTP-Server zurück, über den die E-Mails versandt werden.
     *
     * @return Die Anmeldedaten.
     */
    public String[] getAuth(){
        String[] returnStrings = new String[2];
        Nutzer n = this.getById(1);
        returnStrings[0] = n.getEmail();
        returnStrings[1] = n.getPasswordhash();
        return returnStrings;
    }

    /**
     * Diese Methode gibt alle Nutzer zurück.
     *
     * @return Die List emit allen Nutzern.
     */
    public List<Nutzer> getAll(){
        return em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
    }

    /**
     * Diese Methode gibt alle Nutzer zurück. Dabei wird die Verbindung jedes
     * Nutzers zur Datenbank getrennt, das heißt die Nutzer können ohne
     * Auswirkung auf die Datenbank verändert werden. Jede Liste der Nutzer, wie
     * zum Beispiel die Freundes- oder Chatlisten werden geleert, um Fehler beim
     * frontendseitigen Anzeigen vorzubeugen.
     *
     * @return Eine Liste mit allen Nutzern
     */
    public List<Nutzer> getAllCopy(){
        List<Nutzer> nutzerList;
        nutzerList = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
        for(Nutzer n : nutzerList){
            em.detach(n);
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
        }
        return nutzerList;

    }

    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id.
     *
     * @param id Die Id des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getById(int id){
        return em.find(Nutzer.class, id);
    }

    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id. Dabei wird die
     * Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann
     * ohne Auswirkung auf die Datenbank verändert werden. Jede Liste des
     * Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert,
     * um Fehler beim frontendseitigen Anzeigen vorzubeugen.
     *
     * @param id
     * @return Der Nutzer
     */
    public Nutzer getCopyById(int id){
        Nutzer n = em.find(Nutzer.class, id);
        em.detach(n);
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
        return n;

    }

    /**
     * Diese Methode gibt alle Freunde eines Nutzers zurück.
     *
     * @param id Die Id des Nutzers
     * @return Die Freundesliste
     */
    public List<Nutzer> getFriends(int id){
        Nutzer self = em.find(Nutzer.class, id);
        List<Nutzer> friends = new ArrayList<>();
        for(Nutzer n : self.getOwnFriendList()){
            if(self.getOtherFriendList().contains(n)){
                friends.add(n);
            }
        }
        return friends;
    }

    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id.Dabei wird die
     * Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann
     * ohne Auswirkung auf die Datenbank verändert werden. Die Listen des
     * Nutzers werden nicht verändert.
     *
     * @param id Die Id des Nutzers
     * @return Der Nutzer
     */
    public Nutzer getCopyByIdListsNotNull(int id){
        Nutzer n = em.find(Nutzer.class, id);
        em.detach(n);
        n.setSetting(null);
        for(Chat chat : n.getPinnedChats()){
            em.detach(chat);
        }
        for(Chat chat : n.getAdminInGroups()){
            em.detach(chat);
        }
        for(Chat chat : n.getArchivedChats()){
            em.detach(chat);
        }
        for(Nutzer nutzer : n.getOwnFriendList()){
            em.detach(nutzer);
//               nutzer.setSetting(null);
        }
        for(Nutzer nu : n.getOtherFriendList()){
            em.detach(nu);
//               nu.setSetting(null);
        }
        for(Nutzer nutzer : n.getBlockiertVon()){
            em.detach(nutzer);
//               nutzer.setSetting(null);
        }
        for(Nutzer nutzer : n.getHatBlockiert()){
            em.detach(nutzer);
//               nutzer.setSetting(null);
        }
        for(Nachricht na : n.getMarkedMessages()){
            em.detach(na);
        }
        return n;
//        return em.find(Nutzer.class, id);

    }

    /**
     * Diese Methode gibt alle Nutzer aus einem bestimmten Chat zurück. Dabei
     * wird die Verbindung der Nutzer zur Datenbank getrennt, das heißt die
     * Nutzer können ohne Auswirkung auf die Datenbank verändert werden.
     *
     * @param chatId Die Id des Chats.
     * @return Die Nutzerliste eines Chats.
     */
    public List<Nutzer> getCopyByChatId(int chatId){
        Chat c = em.find(Chat.class, chatId);
        List<Nutzer> nutzerList = c.getNutzerList();
        for(Nutzer n : nutzerList){
            em.detach(n);
            n.setSetting(null);
            for(Chat chat : n.getPinnedChats()){
                em.detach(chat);
                c.setNutzerList(null);
                c.setAdminList(null);
            }
            for(Chat chat : n.getAdminInGroups()){
                em.detach(chat);
                c.setNutzerList(null);
                c.setAdminList(null);
            }

            for(Nutzer nutzer : n.getOwnFriendList()){
                em.detach(nutzer);
                nutzer.setPinnedChats(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
                nutzer.setSetting(null);
                nutzer.setMarkedMessages(null);
                nutzer.setArchivedChats(null);
            }
            for(Nutzer nu : n.getOtherFriendList()){
                em.detach(nu);
                nu.setPinnedChats(null);
                nu.setOwnFriendList(null);
                nu.setOtherFriendList(null);
                nu.setPasswordhash(null);
                nu.setSetting(null);
                nu.setMarkedMessages(null);
                nu.setArchivedChats(null);
            }
            for(Nutzer nutzer : n.getBlockiertVon()){
                em.detach(nutzer);
                nutzer.setPinnedChats(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
                nutzer.setSetting(null);
                nutzer.setMarkedMessages(null);
                nutzer.setArchivedChats(null);
            }
            for(Nutzer nutzer : n.getHatBlockiert()){
                em.detach(nutzer);
                nutzer.setPinnedChats(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
                nutzer.setSetting(null);
                nutzer.setMarkedMessages(null);
                nutzer.setArchivedChats(null);
            }
            n.setArchivedChats(null);
            n.setMarkedMessages(null);
        }
        return nutzerList;
    }

    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getByUsername(String username){
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);
        try{
            Nutzer n = (Nutzer) query.getSingleResult();
            return n;
        }catch(Exception e){
            return null;
        }

    }

    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens. Dabei wird
     * die Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer
     * kann ohne Auswirkung auf die Datenbank verändert werden. Jede Liste des
     * Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert,
     * um Fehler beim frontendseitigen Anzeigen vorzubeugen.
     *
     * @param username
     * @return Der Nutzer
     */
    public Nutzer getCopyByUsername(String username){

        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);

        Nutzer n = (Nutzer) query.getSingleResult();
        em.detach(n);
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
        return n;
    }

    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens. Dabei wird
     * die Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer
     * kann ohne Auswirkung auf die Datenbank verändert werden. Die Listen des
     * Nutzers werden nicht verändert.
     *
     * @param username Der Nutzername
     * @return Der Nutzer
     */
    public Nutzer getCopyByUsernameListsNotNull(String username){

        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);

        Nutzer n = (Nutzer) query.getSingleResult();

        for(Chat chat : n.getPinnedChats()){
            em.detach(chat);
        }
        for(Chat chat : n.getAdminInGroups()){
            em.detach(chat);
        }

        for(Nutzer nutzer : n.getOwnFriendList()){
            em.detach(nutzer);
        }
        for(Nutzer nu : n.getOtherFriendList()){
            em.detach(nu);
        }

        return n;
    }

    /**
     * Diese Methode sucht einen Nutzer anhand seiner E-Mail.
     *
     * @param mail Die E-Mail des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getByMail(String mail){
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByEmail");
        query.setParameter("email", mail);
        try{
            Nutzer n = (Nutzer) query.getSingleResult();
            return n;
        }catch(Exception e){
            return null;
        }

    }

    /**
     * Diese Methode sucht einen Nutzer anhand seiner E-Mail. Dabei wird die
     * Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann
     * ohne Auswirkung auf die Datenbank verändert werden. Jede Liste des
     * Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert,
     * um Fehler beim frontendseitigen Anzeigen vorzubeugen.
     *
     * @param email Die E-Mail
     * @return Der Nutzer
     */
    public Nutzer getCopyByEmail(String email){

        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByEmail");
        query.setParameter("email", email);

        Nutzer n = (Nutzer) query.getSingleResult();
        em.detach(n);
        n.setSetting(null);
        n.setMarkedMessages(null);
        n.setArchivedChats(null);
        for(Chat c : n.getPinnedChats()){
            em.detach(c);
            c.setNutzerList(null);
            c.setAdminList(null);
            n.setSetting(null);
        }
        for(Chat c : n.getAdminInGroups()){
            em.detach(c);
            c.setNutzerList(null);
            c.setAdminList(null);
            n.setSetting(null);
        }
        for(Nutzer nutzer : n.getOwnFriendList()){
            em.detach(nutzer);
            nutzer.setPinnedChats(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPasswordhash(null);
            nutzer.setSetting(null);
            nutzer.setMarkedMessages(null);
            nutzer.setArchivedChats(null);
        }
        for(Nutzer nutzer : n.getOtherFriendList()){
            em.detach(nutzer);
            em.detach(nutzer);
            nutzer.setPinnedChats(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPasswordhash(null);
            nutzer.setSetting(null);
            nutzer.setMarkedMessages(null);
            nutzer.setArchivedChats(null);
        }

        return n;
    }

    /**
     * Diese Methode sucht alle Nutzer, die den angegeben Namen in ihrem Nutzernamen
     * enthalten, aus der Datenbank heraus. Dafür werden Prozentzeichen am Anfang und am Ende
     * des eingebenen Namens hinzugefügt, um den SQL-Befehl korrekt ausführen zu können.
     *
     * @param name Der gesuchte Name
     * @return Die gesuchte Nutzerliste
     */
    public List<Nutzer> searchCopy(String name){
        name = "%" + name + "%";
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".search");
        query.setParameter("benutzername", name);

        List<Nutzer> l = (List<Nutzer>) query.getResultList();

        for(Nutzer n : l){
            em.detach(n);
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
        }

        return l;
    }

    /**
     * Diese Methode registriert einen neuen Nutzer.
     *
     * @param neuerNutzer
     */
    public void add(Nutzer neuerNutzer){
        em.persist(neuerNutzer);
    }

    /**
     * Diese Methode fügt einen Nutzer zu einem Chat hinzu. Dabei ist es egal,
     * ob es sich um eine Gruppe oder einen Einzelchat handelt.
     *
     * @param chat Der Chat, zu dem der Nutzer hinzugefügt werden soll
     * @param nutzer Der Nutzer, der hinzugefügt werden soll.
     */
    public void fuegeChatHinzu(Chat chat, Nutzer nutzer){
        try{
            Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
            nutzerInDB.getPinnedChats().add(chat);
        }catch(Exception e){
        }

    }

    /**
     * Diese Methode entfernt einen Nutzer aus einem Chat. Dabei ist es egal, ob
     * es sich um eine Gruppe oder einen Einzelchat handelt.
     *
     * @param chat Der Chat, aus dem der Nutzer entfernt werden soll-
     * @param nutzer Der Nutzer, der entfernt werden soll.
     */
    public void entferneChat(Chat chat, Nutzer nutzer){
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        nutzerInDB.getPinnedChats().remove(chat);
    }

    /**
     * Diese Methode fügt einen Nutzer zu der eigenen Freundesliste hinzu, sowie
     * den eigenen Nutzer in die andere Freundesliste des anderen Nutzers.
     *
     * @param self Der eigene Nutzer
     * @param other Der andere Nutzer
     */
    public void fuegeFreundHinzu(Nutzer self, Nutzer other){
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getOwnFriendList().add(other);
        otherInDB.getOtherFriendList().add(self);
    }

    /**
     * Diese Methode entfernt einen Nutzer aus der eigenen Freundesliste, sowie
     * den eigenen Nutzer aus der anderen Freundesliste des anderen Nutzers.
     *
     * @param self
     * @param other
     */
    public void loescheFreund(Nutzer self, Nutzer other){
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getOwnFriendList().remove(other);
        otherInDB.getOtherFriendList().remove(self);
    }

    /**
     * Diese Methode blockiert einen anderen Nutzer, so dass keine Nachrichten in dem Einzelchat mehr gesendet
     * werden können.
     *
     * @param self Der eigene Nutzer.
     * @param other Der andere Nutzer.
     */
    public void block(Nutzer self, Nutzer other){
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getHatBlockiert().add(other);
        otherInDB.getBlockiertVon().add(self);
    }

    /**
     * Diese Methode hebt die Blockierung eines anderen Nutzer auf, so dass in dem Einzelchat wieder Nachrichten gesendet werden können.
     *
     * @param self Der eigene Nutzer.
     * @param other Der andere Nutzer.
     */
    public void unblock(Nutzer self, Nutzer other){
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getHatBlockiert().remove(other);
        otherInDB.getBlockiertVon().remove(self);
    }

    /**
     * Diese Methode löscht einen Nutzer anhand seiner Id aus der Datenbank.
     *
     * @param id Die id des zu löschenden Nutzers
     * @return Einen Boolean mit dem Status der Methode
     */
    public boolean delete(int id){
        try{
            Nutzer self = this.getById(id);
            int length = self.getOwnFriendList().size();
            for(int x = 0; x < length; x++){
                Nutzer n = self.getOwnFriendList().get(x);
                this.loescheFreund(self, n);
            }
            List<Chat> l = chatEJB.getAll();
            for(Chat c : l){
                if(c.getNutzerList().size() == 2 && c.getNutzerList().contains(self)){
                    chatEJB.delete(c);
                }
            }
            em.remove(this.getById(id));
            return true;
        }catch(IllegalArgumentException e){
            // Fehler: Objekt ist nicht persistiert
            return false;
        }
    }

    /**
     * Diese Methode aktualisiert die Informationen eines Nutzers.
     *
     * @param aktualisierterNutzer Der Nutzer mit den neuen Daten
     * @return Ein Boolean mit dem Status der Methode
     */
    public boolean update(Nutzer aktualisierterNutzer){
        int gesuchteId = aktualisierterNutzer.getId();
        try{
            Nutzer aktuellInDatenbank = this.getById(gesuchteId);
            aktuellInDatenbank.setVorname(aktualisierterNutzer.getVorname());
            aktuellInDatenbank.setNachname(aktualisierterNutzer.getNachname());
            aktuellInDatenbank.setBenutzername(aktualisierterNutzer.getBenutzername());
            aktuellInDatenbank.setEmail(aktualisierterNutzer.getEmail());
            aktuellInDatenbank.setHandynummer(aktualisierterNutzer.getHandynummer());
            aktuellInDatenbank.setInfo(aktualisierterNutzer.getInfo());
            aktuellInDatenbank.setProfilbild(aktualisierterNutzer.getProfilbild());
            em.merge(aktuellInDatenbank);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Diese Methode setzt den Status aller Nutzer auf offline sowie die Zeit,
     * zu der sie zuletzt online waren auf die aktuelle Zeit. Sie wird alle fünf
     * Minuten automatisch ausgeführt.
     */
    public void setAllOffline(){
        List<Nutzer> l = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
        for(Nutzer n : l){
            if(n.getIsonline()){
                n.setIsonline(false);
                BigInteger x = new BigInteger("" + System.currentTimeMillis());
                n.setLastonline(x);
            }

        }
    }

    /**
     * Die folgende Methode setzt den Status des zum Token gehörigen Nutzers auf
     * online sowei die Zeit, zu der der Nutzer das letzte Mal online war auf
     * die aktuelle Zeit.
     *
     * @param token Das Token des Nutzers
     */
    public void setOnline(String token){
        Nutzer n = (Nutzer) em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername").setParameter("benutzername", tokenizer.getUser(token)).getSingleResult();
        n.setIsonline(true);
        BigInteger x = new BigInteger("" + System.currentTimeMillis());
        n.setLastonline(x);
    }
}
