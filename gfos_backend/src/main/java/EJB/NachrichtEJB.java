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
    
    //TODO: Implementierung der Methode, die alle Nachrichten einer chatId zurückgibt
}
