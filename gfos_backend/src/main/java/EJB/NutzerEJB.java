/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Chat;
import Entity.Nutzer;
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
    
    // READ
    public Nutzer getById(int id) {
        return em.find(Nutzer.class, id);
    }
    
    public Nutzer getCopy(int id){
        Nutzer n = em.find(Nutzer.class, id);
        em.detach(n);
        return n;
        
    }
    //SELECT n FROM Nutzer n JOIN NimmtTeil t WHERE n.Id = t.NutzerId AND t.ChatId = 1
    public List<Nutzer> getByChatId(int chatId){
        Chat c = em.find(Chat.class, chatId);
        return c.getNutzerList();
    }
  
    public Nutzer getByUsername(String username){
        
        Query query = em.createNamedQuery(Nutzer.class.getSimpleName() + ".findByBenutzername");
        query.setParameter("benutzername", username);
        
        Nutzer user = (Nutzer) query.getSingleResult();
        
        return user;
    }
    
    // CREATE
    public void add(Nutzer neuerNutzer) {
        em.persist(neuerNutzer);
    }
    
    public void fuegeChatHinzu(Chat chat, Nutzer nutzer){
            System.out.println("NutzerEJB fuegeChatHinzu");
            List<Chat> chatList = nutzer.getChatList();
            chatList.add(chat);
            nutzer.setChatList(chatList);
            em.persist(nutzer);
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
            return true;
        }
            catch(Exception e) {
            return false;
        }
    }
}
