/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Foto;
import Entity.Nutzer;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author simon
 */
@Stateless
public class FotoEJB {

    @PersistenceContext
    private EntityManager em;

    public List<Foto> getAll(){
        return em.createNamedQuery(Foto.class.getSimpleName() + ".findAll").getResultList();
    }
    
    public void add(Foto neuesFoto){
        em.persist(neuesFoto);
    }
    public Foto getByBase64(String base64){
        Query query = em.createNamedQuery(Foto.class.getSimpleName() + ".findByBase64");
        query.setParameter("base64", base64);
        
        Foto f = (Foto) query.getSingleResult();
        return f;
    }
}
