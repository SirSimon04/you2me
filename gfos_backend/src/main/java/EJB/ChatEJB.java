/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import javax.ejb.Stateless;
import Entity.Chat;
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
    
    public void fuegeHinzu(Chat chat, Nutzer nutzer){
        Chat chatInDB = em.find(Chat.class, chat.getChatid());
        chatInDB.getNutzerList().add(nutzer);
    }
    
    public void delete(Chat c){
        em.remove(c);
    }
            
}