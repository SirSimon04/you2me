/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import javax.ejb.Stateless;
import Entity.Chat;
import Entity.Nimmtteil;
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

    public void addChat(Chat neuerChat) {
        em.persist(neuerChat);
    }
    
    public void takePart(Nimmtteil nimmtteil) {
        em.persist(nimmtteil);
    }
            
}
