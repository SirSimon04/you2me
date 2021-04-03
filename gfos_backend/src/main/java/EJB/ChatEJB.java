package EJB;

import javax.ejb.Stateless;
import javax.ejb.Stateless;
import Entity.Chat;
import Entity.Nachricht;
import Entity.Nutzer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <h1>Die Klasse zum Verwalten der Chats in der Datenbank</h1>
 * <p>Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der Datenbank
 * bezogen auf die Chats. Die Daten werden bei Anfrage des Webservers übergeben.</p>
 * @author simon
 */
@Stateless
public class ChatEJB {
    
    @PersistenceContext
    private EntityManager em;
    /**
     * Diese Methode gibt alle Chats zurück.
     * @return Eine Liste mit allen Chats.
     */
    public List<Chat> getAll() {
        return em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
    }
    /**
     * Diese Methode gibt alle Chats zurück. Dabei wird die Verbindung jedes Chats
     * zur Datenbank getrennt, das heißt die Chats können ohne Auswirkung auf die Datenbank
     * verändert werden. 
     * @return Der Chat
     */
    public List<Chat> getAllCopy(){
        List<Chat> chatList;
        chatList = em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
        for(Chat c : chatList) {
            em.detach(c);
            for(Nutzer n : c.getNutzerList()){
                em.detach(n);
                n.setPinnedChats(null);
                n.setAdminInGroups(null);
                n.setOwnFriendList(null);
                n.setOtherFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
                n.setMarkedMessages(null);
            }
            for(Nutzer n : c.getAdminList()){
                em.detach(n);
                n.setPinnedChats(null);
                n.setAdminInGroups(null);
                n.setOwnFriendList(null);
                n.setOtherFriendList(null);
                n.setHatBlockiert(null);
                n.setBlockiertVon(null);
                n.setSetting(null);
                n.setMarkedMessages(null);
            }
            try{
                Nachricht letzteN = c.getLetztenachricht();
                letzteN.setNachrichtList(null);
                letzteN.setNutzerList(null);
            }
            catch(NullPointerException e){
                
            }
            
        }
        return chatList;
    }
    
    public List<Chat> getAllCopyListsNotNull(){
        List<Chat> chatList;
        chatList = em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
        for(Chat c : chatList) {
            em.detach(c);
            for(Nutzer n : c.getNutzerList()){
                em.detach(n);
                n.setSetting(null);
                n.setMarkedMessages(null);
            }
            for(Nutzer n : c.getAdminList()){
                em.detach(n);
                n.setBlockiertVon(null);
                n.setMarkedMessages(null);
                n.setSetting(null);
            }
            try{
                Nachricht letzteN = c.getLetztenachricht();
                letzteN.setNachrichtList(null);
                letzteN.setNutzerList(null);
            }
            catch(NullPointerException e){
                
            }
            
        }
        return chatList;
    }
    
    /**
     * Diese Methode gibt einen Chat anhand seiner Id zurück.
     * @param id Die Id des gesuchten Chats
     * @return Der gesuchte Chat
     */
    public Chat getById(int id) {
        return em.find(Chat.class, id);
    }
    /**
     * Diese Methode sucht einen Chat anhand seiner Id. Dabei wird die Verbindung des Nutzers
     * zur Datenbank getrennt, das heißt der Chat kann ohne Auswirkung auf die Datenbank
     * verändert werden. 
     * @return Der Chat
     */
    public Chat getCopy(int id) {
        Chat c = em.find(Chat.class, id);
        em.detach(c);
        for(Nutzer n : c.getNutzerList()){
            em.detach(n);
            n.setPinnedChats(null);
            n.setAdminInGroups(null);
            n.setOwnFriendList(null);
            n.setOtherFriendList(null);
            n.setHatBlockiert(null);
            n.setBlockiertVon(null);
            n.setSetting(null);
            n.setMarkedMessages(null);
//            n.setSetting(null);
        }
        for(Nutzer n : c.getAdminList()){
            em.detach(n);
            n.setPinnedChats(null);
            n.setAdminInGroups(null);
            n.setOwnFriendList(null);
            n.setOtherFriendList(null);
            n.setHatBlockiert(null);
            n.setBlockiertVon(null);
            n.setSetting(null);
            n.setMarkedMessages(null);
//            n.setSetting(null);
        }
        try{
            Nachricht letzteN = c.getLetztenachricht();
//            em.detach(letzteN);
            letzteN.setNachrichtList(null);
            letzteN.setNutzerList(null);
        }
        catch(Exception e){
            System.out.println("nullpointer");
        }
        
        return c;
    }
    
    public Chat getCopyListsNotNull(int id) {
        Chat c = em.find(Chat.class, id);
        em.detach(c);
        for(Nutzer n : c.getNutzerList()){
            em.detach(n);
            n.setSetting(null);
        }
        for(Nutzer n : c.getAdminList()){
            em.detach(n);
            n.setSetting(null);
        }
        try{
            Nachricht letzteN = c.getLetztenachricht();
            letzteN.setNachrichtList(null);
            letzteN.setNutzerList(null);
        }
        catch(NullPointerException e){
            
        }
        
        return c;
    }
    
    public List<Chat> getOwnChats(Nutzer self){
        List<Chat> l = em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
        List<Chat> r = new ArrayList<>();
        for(Chat c  : l){
            if(c.getNutzerList().contains(self)){
                r.add(c);
            }
        }
        return r;
    }
    
    /**
     * Diese Methode fügt einen Nutzer zu einem Chat hinzu.
     * @param chat Der Chat, zu dem ein Nutzer hinzugefügt werden soll.
     * @param nutzer Der Nutzer, der zu einem Chat hinzugefügt werden soll.
     */
    public void fuegeNutzerHinzu(Chat chat, Nutzer nutzer){
        System.out.println("cEJB fnh");
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        chatInDB.getNutzerList().add(nutzer);
    }
    /**
     * Diese Methode entfernt einen Nutzer aus einem Chat.
     * @param chat Der Chat, aus dem der Nutzer entfernt werden soll.
     * @param nutzer Der Nutzer, der aus einem Chat entfernt werden soll.
     */
    public void entferneNutzer(Chat chat, Nutzer nutzer){
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        chatInDB.getNutzerList().remove(nutzer);
    }
    /**
     * Diese Methode fügt einem Chat einen Administrator hinzul
     * @param chat Der Chat
     * @param nutzer Der Nutzer
     */
    public void addAdmin(Chat chat, Nutzer nutzer){
        System.out.println("addAdmin");
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        chatInDB.getAdminList().add(nutzer);
        nutzerInDB.getAdminInGroups().add(chat);
    }
    /**
     * Diese Methode entfernt den Nutzer als Administrator der Gruppe.
     * @param chat Der Chat
     * @param nutzer Der Nutzer
     */
    public void deleteAdmin(Chat chat, Nutzer nutzer){
        System.out.println("addAdmin");
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        chatInDB.getAdminList().remove(nutzer);
        nutzerInDB.getAdminInGroups().remove(chat);
    }
    
    /**
     * Diese Methode fügt einen neuen Chat in die Datenbank ein.
     * @param neuerChat 
     */
    public void createChat(Chat neuerChat) {
        em.persist(neuerChat);
    }
    /**
     * Diese Methode löscht einen Chat aus der Datenbank.
     * @param c 
     */
    public void delete(Chat c){
        em.remove(c);
    }
            
}