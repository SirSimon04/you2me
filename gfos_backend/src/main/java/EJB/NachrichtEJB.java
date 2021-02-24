/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import Entity.Nachricht;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author simon
 */
@Stateless
public class NachrichtEJB {

    @PersistenceContext
    private EntityManager em;
    
    public void add(Nachricht neueNachricht) {
        em.persist(neueNachricht);
    }
    
    
    public List<Nachricht> getAll() {
    return em.createNamedQuery(Nachricht.class.getSimpleName()+".findAll").getResultList();
    }
    
    public List<Nachricht> getByChat(int id){   
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        em.detach(nachrichtList);
        return nachrichtList;
    }
    
    //TODO: Implementierung der Methode, die alle Nachrichten einer chatId zurückgibt
}
