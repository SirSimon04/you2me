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
    
    public List<Chat> getAll() {
        return em.createNamedQuery(Chat.class.getSimpleName()+".findAll").getResultList();
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
        List<Nutzer> nutzerList = chat.getNutzerList();
        nutzerList.add(nutzer);
    }
            
}
