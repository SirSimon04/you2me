/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Blacklist;
import Entity.Chat;
import Entity.Nutzer;
import java.util.HashSet;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * <h1>Die Klasse zum Verwalten der Nutzer in der Datenbank</h1>
 * <p>Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der Datenbank
 * bezogen auf die Nutzer. Die Daten werden bei Anfrage des Webservers übergeben.</p>
 * @author simon
 */
@Stateless
@LocalBean
public class NutzerEJB {
    
    @PersistenceContext
    private EntityManager em;
    /**
     * Diese Methode gibt alle Nutzer zurück.
     * @return 
     */
    public List<Nutzer> getAll(){
        return em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
    }
    /**
     * Diese Methode gibt alle Nutzer zurück. Dabei wird die Verbindung jedes Nutzers
     * zur Datenbank getrennt, das heißt die Nutzer können ohne Auswirkung auf die Datenbank
     * verändert werden. Jede Liste der Nutzer, wie zum Beispiel die Freundes- oder Chatlisten werden geleert, um 
     * Fehler beim frontendseitigen Anzeigen vorzubeugen.
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
           n.setChatList(null);
           n.setHatBlockiert(null);
            n.setBlockiertVon(null);
        }
        return nutzerList;
    
    }
    
    
    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id.
     * @param id Die Id des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getById(int id) {
        return em.find(Nutzer.class, id);
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id.
     * Dabei wird die Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann ohne Auswirkung auf die Datenbank
     * verändert werden. Jede Liste des Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert, um 
     * Fehler beim frontendseitigen Anzeigen vorzubeugen.
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
        n.setChatList(null);
        n.setHatBlockiert(null);
        n.setBlockiertVon(null);
        return n;
        
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seiner Id. Dabei wird die Verbindung des Nutzers
     * zur Datenbank getrennt, das heißt der Nutzer kann ohne Auswirkung auf die Datenbank
     * verändert werden. Die Listen des Nutzers werden nicht verändert.
     * @return Der Nutzer
     */
    public Nutzer getCopyByIdListsNotNull(int id){
        Nutzer n = em.find(Nutzer.class, id);
        em.detach(n);
        for(Chat chat : n.getChatList()){
            em.detach(chat);
            }
            for(Chat chat : n.getAdminInGroups()){
            em.detach(chat);
            }
           
           for(Nutzer nutzer : n.getOwnFriendList()) {
               em.detach(nutzer);
           }
           for(Nutzer nu : n.getOtherFriendList()) {
               em.detach(nu);
           }
           for(Nutzer nutzer : n.getBlockiertVon()){
               em.detach(nutzer);
            }
           for(Nutzer nutzer : n.getHatBlockiert()){
               em.detach(nutzer);
            }
        return n;
    }
    
    /**
     * Diese Methode gibt alle Nutzer aus einem bestimmten Chat zurück. Dabei wird die Verbindung der Nutzer
     * zur Datenbank getrennt, das heißt die Nutzer können ohne Auswirkung auf die Datenbank
     * verändert werden.
     * @param chatId Die Id des Chats.
     * @return Die Nutzerliste eines Chats.
     */
    public List<Nutzer> getCopyByChatId(int chatId){
        Chat c = em.find(Chat.class, chatId);
        List<Nutzer> nutzerList = c.getNutzerList();
        for(Nutzer n : nutzerList){
           em.detach(n); 
           for(Chat chat : n.getChatList()){
            em.detach(chat);
            c.setNutzerList(null);
            c.setAdminList(null);
            }
            for(Chat chat : n.getAdminInGroups()){
            em.detach(chat);
            c.setNutzerList(null);
            c.setAdminList(null);
            }
           
           for(Nutzer nutzer : n.getOwnFriendList()) {
               em.detach(nutzer);
               nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
           }
           for(Nutzer nu : n.getOtherFriendList()) {
               em.detach(nu);
               nu.setChatList(null);
                nu.setOwnFriendList(null);
                nu.setOtherFriendList(null);
                nu.setPasswordhash(null);
           }
           for(Nutzer nutzer : n.getBlockiertVon()){
               em.detach(nutzer);
               nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
            }
           for(Nutzer nutzer : n.getHatBlockiert()){
               em.detach(nutzer);
               nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
            }
        }
        return nutzerList;
    }
    
    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens.
     * @param username Der Benutzername des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getByUsername(String username){
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);
        try{
            Nutzer n = (Nutzer) query.getSingleResult();
            return n;
        }
        catch(Exception e){
            return null;
        }
        
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens.
     * Dabei wird die Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann ohne Auswirkung auf die Datenbank
     * verändert werden. Jede Liste des Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert, um 
     * Fehler beim frontendseitigen Anzeigen vorzubeugen.
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
        n.setChatList(null);
        n.setHatBlockiert(null);
        n.setBlockiertVon(null);
        return n;
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seines Benutzernamens. Dabei wird die Verbindung des Nutzers
     * zur Datenbank getrennt, das heißt der Nutzer kann ohne Auswirkung auf die Datenbank
     * verändert werden. Die Listen des Nutzers werden nicht verändert.
     * @param username Der Nutzername
     * @return Der Nutzer
     */
    public Nutzer getCopyByUsernameListsNotNull(String username){
        
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);
        
        Nutzer n = (Nutzer) query.getSingleResult();
        
        for(Chat chat : n.getChatList()){
            em.detach(chat);
            }
            for(Chat chat : n.getAdminInGroups()){
            em.detach(chat);
            }
           
           for(Nutzer nutzer : n.getOwnFriendList()) {
               em.detach(nutzer);
           }
           for(Nutzer nu : n.getOtherFriendList()) {
               em.detach(nu);
           }
        
        return n;
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seiner E-Mail.
     * @param mail Die E-Mail des gesuchten Nutzers.
     * @return Der Nutzer
     */
    public Nutzer getByMail(String mail){
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByEmail");
        query.setParameter("email", mail);
        try{
            Nutzer n = (Nutzer) query.getSingleResult();
            return n;
        }
        catch(Exception e){
            return null;
        }
        
        
    }
    /**
     * Diese Methode sucht einen Nutzer anhand seiner E-Mail.
     * Dabei wird die Verbindung des Nutzers zur Datenbank getrennt, das heißt der Nutzer kann ohne Auswirkung auf die Datenbank
     * verändert werden. Jede Liste des Nutzers, wie zum Beispiel die Freundes- oder Chatlisten werden geleert, um 
     * Fehler beim frontendseitigen Anzeigen vorzubeugen.
     * @param email Die E-Mail
     * @return Der Nutzer
     */
    public Nutzer getCopyByEmail(String email){
        
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByEmail");
        query.setParameter("email", email);
        
        Nutzer n = (Nutzer) query.getSingleResult();
        em.detach(n);
        for(Chat c : n.getChatList()){
            em.detach(c);
            c.setNutzerList(null);
            c.setAdminList(null);
        }
        for(Chat c : n.getAdminInGroups()){
            em.detach(c);
            c.setNutzerList(null);
            c.setAdminList(null);
        }
        for(Nutzer nutzer : n.getOwnFriendList()) {
            em.detach(nutzer);
            nutzer.setChatList(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPasswordhash(null);
        }
        for(Nutzer nutzer : n.getOtherFriendList()) {
            em.detach(nutzer);
            em.detach(nutzer);
            nutzer.setChatList(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPasswordhash(null);
        }
        
        return n;
    }
    
    /**
     * Diese Methode registriert einen neuen Nutzer.
     * @param neuerNutzer 
     */
    public void add(Nutzer neuerNutzer) {
        em.persist(neuerNutzer);
    }
    /**
     * Diese Methode fügt einen Nutzer zu einem Chat hinzu. Dabei ist es egal,
     * ob es sich um eine Gruppe oder einen Einzelchat handelt.
     * @param chat Der Chat, zu dem der Nutzer hinzugefügt werden soll
     * @param nutzer Der Nutzer
     */
    public void fuegeChatHinzu(Chat chat, Nutzer nutzer) {
        System.out.println("nEJB fch");
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        nutzerInDB.getChatList().add(chat);
    }
    /**
     * Diese Methode entfernt einen Nutzer aus einem Chat. Dabei ist es egal,
     * ob es sich um eine Gruppe oder einen Einzelchat handelt.
     * @param chat Der Chat, asu dem der Nutzer entfernt werden soll-
     * @param nutzer 
     */
    public void entferneChat(Chat chat, Nutzer nutzer) {
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        nutzerInDB.getChatList().remove(chat);
    }
    /**
     * Diese Methode fügt einen Nutzer zu der eigenen Freundesliste hinzu, sowie den eigenen Nutzer in die Freundesliste des 
     * anderen Nutzers. 
     * @param self Der eigene Nutzer
     * @param other Der andere Nutzer
     */
    public void fuegeFreundHinzu(Nutzer self, Nutzer other) {
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getOwnFriendList().add(other);
        otherInDB.getOtherFriendList().add(self);
    }
    /**
     * Diese Methode entfernt einen Nutzer aus der eigenen Freundesliste, sowie den eigenen Nutzer aus der Freundesliste des anderen Nutzers.
     * @param self
     * @param other 
     */
    public void loescheFreund(Nutzer self, Nutzer other) {
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getOwnFriendList().remove(other);
        otherInDB.getOtherFriendList().remove(self);
    }
    
    public void block(Nutzer self, Nutzer other){
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getHatBlockiert().add(other);
        otherInDB.getBlockiertVon().add(self);
    }
    
    /**
     * Diese Methode löscht einen Nutzer anhand seiner Id aus der Datenbank.
     * @param id Die id des zu löschenden Nutzers
     * @return Einen Boolean mit dem Status der Methode
     */
    public boolean delete(int id) {
        try {
            em.remove(this.getById(id));
            return true;
        }
            catch(IllegalArgumentException e) {
            // Fehler: Objekt ist nicht persistiert
            return false;
        }
    }
    /**
     * Diese Methode aktualisiert die Informationen eines Nutzers.
     * @param aktualisierterNutzer Der Nutzer mit den neuen Daten
     * @return Ein Boolean mit dem Status der Methode
     */
    public boolean update(Nutzer aktualisierterNutzer) {
        int gesuchteId = aktualisierterNutzer.getId();
        try {
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
        }
            catch(Exception e) {
            return false;
        }
    }
    
    
}
