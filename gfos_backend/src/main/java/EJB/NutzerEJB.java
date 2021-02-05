/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

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
