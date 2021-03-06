/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

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
import javax.persistence.Query;

/**
 *
 * @author Simon
 */
@Stateless
@LocalBean
public class NutzerEJB {

    @PersistenceContext
    private EntityManager em;
    
    public List<Nutzer> getAll(){
        return em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
    }
    
    public List<Nutzer> getAllCopy(){
        List<Nutzer> nutzerList;
        nutzerList = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findAll").getResultList();
        for(Nutzer n : nutzerList){
           em.detach(n); 
           
           for(Chat c : n.getChatList()){
            em.detach(c);
            }
            for(Nutzer nutzer : n.getOwnFriendList()) {
                em.detach(nutzer);
           }
           for(Nutzer nu : n.getOtherFriendList()) {
                 em.detach(nu);
           }
           for(Chat c : n.getChatList()){
            c.setNutzerList(null);
            }
            for(Nutzer nutzer : n.getOwnFriendList()) {
                nutzer.setChatList(null);
                nutzer.setOwnFriendList(null);
                nutzer.setOtherFriendList(null);
                nutzer.setPasswordhash(null);
           }
           for(Nutzer nu : n.getOtherFriendList()) {
                 nu.setChatList(null);
                 nu.setOwnFriendList(null);
                 nu.setOtherFriendList(null);
           
        }
        }
        return nutzerList;
    
    }
    
    // READ
    
    public Nutzer getById(int id) {
        return em.find(Nutzer.class, id);
    }
    
    public Nutzer getCopyById(int id){
        Nutzer n = em.find(Nutzer.class, id);
        em.detach(n);
        for(Chat c : n.getChatList()){
            em.detach(c);
            c.setNutzerList(null);
            
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
        return n;
        
    }
    
    public List<Nutzer> getCopyByChatId(int chatId){
        Chat c = em.find(Chat.class, chatId);
        List<Nutzer> nutzerList = c.getNutzerList();
        for(Nutzer n : nutzerList){
           em.detach(n); 
           for(Chat chat : n.getChatList()){
            em.detach(chat);
            c.setNutzerList(null);
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
        }
        return nutzerList;
    }
  
    public Nutzer getCopyByUsername(String username){
        
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);
        
        Nutzer n = (Nutzer) query.getSingleResult();
        em.detach(n);
        for(Chat c : n.getChatList()){
            em.detach(c);
            c.setNutzerList(null);
            
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
            nutzer.setChatList(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPasswordhash(null);
        }
        return n;
    }
    
    public Nutzer getCopyByEmail(String email){
        
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByEmail");
        query.setParameter("email", email);
        
        Nutzer n = (Nutzer) query.getSingleResult();
        em.detach(n);
        for(Chat c : n.getChatList()){
            em.detach(c);
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
    
    // CREATE
    public void add(Nutzer neuerNutzer) {
        em.persist(neuerNutzer);
    }
    
    public void fuegeChatHinzu(Chat chat, Nutzer nutzer) {
        Nutzer nutzerInDB = em.find(Nutzer.class, nutzer.getId());
        nutzerInDB.getChatList().add(chat);
    }
    
    public void fuegeFreundHinzu(Nutzer self, Nutzer other) {
        Nutzer selfInDB = em.find(Nutzer.class, self.getId());
        Nutzer otherInDB = em.find(Nutzer.class, other.getId());
        selfInDB.getOwnFriendList().add(other);
        otherInDB.getOtherFriendList().add(self);
    }
    
        // DELETE
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
