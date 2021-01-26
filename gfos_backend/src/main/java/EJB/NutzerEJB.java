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

/**
 *
 * @author krink
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
    public Nutzer get(int id) {
        return em.find(Nutzer.class, id);
    }
    
    // CREATE
    public void add(Nutzer neuerNutzer) {
        em.persist(neuerNutzer);
    }
    
        // DELETE
    public boolean delete(int id) {
        try {
            em.remove(this.get(id));
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
            Nutzer aktuellInDatenbank = this.get(gesuchteId);
            aktuellInDatenbank.setVorname(aktualisierterNutzer.getVorname());
            return true;
        }
            catch(Exception e) {
            return false;
        }
    }
}
