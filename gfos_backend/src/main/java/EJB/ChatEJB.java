/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import javax.ejb.Stateless;
import Entity.Chat;
import Entity.Nachricht;
import Entity.Nutzer;
import java.util.List;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author simon
 */
@Stateless
public class ChatEJB {
    
    @PersistenceContext
    private EntityManager em;
    
    public Chat getById(int id) {
        return em.find(Chat.class, id);
    }
    
    public Chat getCopy(int id) {
        Chat c = em.find(Chat.class, id);
        em.detach(c);
        for(Nutzer n : c.getNutzerList()){
            em.detach(n);
            n.setChatList(null);
            n.setAdminInGroups(null);
        }
        for(Nutzer n : c.getAdminList()){
            em.detach(n);
            n.setChatList(null);
            n.setAdminInGroups(null);
        }
        try{
            Nachricht letzteN = c.getLetztenachricht();
            letzteN.setChatList(null);
            letzteN.setNachrichtList(null);
        }
        catch(NullPointerException e){
            
        }
        
        return c;
    }
    
    public List<Chat> getAll() {
        return em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
    }
    
    public List<Chat> getAllCopy(){
        List<Chat> chatList;
        chatList = em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
        for(Chat c : chatList) {
            em.detach(c);
            for(Nutzer n : c.getNutzerList()){
                em.detach(n);
                n.setChatList(null);
                n.setAdminInGroups(null);
            }
            for(Nutzer n : c.getAdminList()){
                em.detach(n);
                n.setChatList(null);
                n.setAdminInGroups(null);
            }
            try{
                Nachricht letzteN = c.getLetztenachricht();
            letzteN.setChatList(null);
            letzteN.setNachrichtList(null);
            }
            catch(NullPointerException e){
                
            }
            
        }
        return chatList;
    }

    public void createChat(Chat neuerChat) {
        em.persist(neuerChat);
    }
    /*s
    public void takePart(Nimmtteil nimmtteil) {
        em.persist(nimmtteil);
    }
    */
    
    public void fuegeNutzerHinzu(Chat chat, Nutzer nutzer){
        System.out.println("cEJB fnh");
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        chatInDB.getNutzerList().add(nutzer);
    }
    
    public void entferneNutzer(Chat chat, Nutzer nutzer){
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        chatInDB.getNutzerList().remove(nutzer);
    }
    
    public void delete(Chat c){
        em.remove(c);
    }
    
    public void addAdmin(Chat c, Nutzer n){
        System.out.println("addAdmin");
        Chat chatInDB = em.find(Chat.class, c.getChatid());
        Nutzer nutzerInDB = em.find(Nutzer.class, n.getId());
        chatInDB.getAdminList().add(n);
        nutzerInDB.getAdminInGroups().add(c);
    }
    
    public void deleteAdmin(Chat c, Nutzer n){
        System.out.println("addAdmin");
        Chat chatInDB = em.find(Chat.class, c.getChatid());
        Nutzer nutzerInDB = em.find(Nutzer.class, n.getId());
        chatInDB.getAdminList().remove(n);
        nutzerInDB.getAdminInGroups().remove(c);
    }
            
}