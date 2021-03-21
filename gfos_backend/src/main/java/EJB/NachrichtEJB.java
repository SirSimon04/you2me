/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import Entity.Nachricht;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Entity.Nutzer;
import javax.ejb.EJB;

/**
 *
 * @author simon
 */
@Stateless
public class NachrichtEJB {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private NutzerEJB nutzerEJB;
    
    public void add(Nachricht neueNachricht) {
        em.persist(neueNachricht);
    }
    
    public void delete(int id){
        Nachricht n = getByID(id);
        em.remove(n);
    }
    
    
    public List<Nachricht> getAll() {
        List<Nachricht> nachrichtList  = em.createNamedQuery(Nachricht.class.getSimpleName()+".findAll").getResultList();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setChatList(null);
            try{
                n.setSender(nutzerEJB.getCopyById(n.getSenderid()).getBenutzername());
            }
            catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }
        }
        return nachrichtList;
    }
    
    public Nachricht getByID(int id){
        return em.find(Nachricht.class, id);
    }
    
    public List<Nachricht> getByChatId(int id){   
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setChatList(null);
            try{
                n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());
            }
            catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }
            
        }
        return nachrichtList;
    }
    
    public Nachricht getNewest(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        if(nachrichtList.size() == 0){
            Nachricht na = new Nachricht();
            return na;
        }
        else{
            for(Nachricht n : nachrichtList){
                em.detach(n);
                n.setNachrichtList(null);
                n.setChatList(null);
            }
            Nachricht n = nachrichtList.get(nachrichtList.size() - 1);
            em.detach(n);
            n.setNachrichtList(null);
            n.setChatList(null);
            try{
                n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());
                
            }
            catch(Exception e){
                n.setSender("gelöschter Nutezr");
            }
            
            return n;
        }
        
    }
    
    
}
