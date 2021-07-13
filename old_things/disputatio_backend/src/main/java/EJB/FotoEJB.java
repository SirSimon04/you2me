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
 * <h1>Die Klasse zum Verwalten der Fotos in der Datenbank.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der
 * Datenbank bezogen auf die Fotos. Die Daten werden bei Anfrage des Webservers
 * übergeben. Die Fotos selbst werden in der Datenbank als Base64-Strings gespeichert.</p>
 *
 * @author simon
 */
@Stateless
public class FotoEJB{

    @PersistenceContext
    private EntityManager em;

    /**
     * Diese Methode gobt alle Fotos in der Datenbank zurück.
     *
     * @return Eine Liste mit allen Fotos.
     */
    public List<Foto> getAll(){
        return em.createNamedQuery(Foto.class.getSimpleName() + ".findAll").getResultList();
    }

    /**
     * Diese Methode fügt ein neues Foto in die Datenbank ein.
     *
     * @param neuesFoto Das neue Foto
     */
    public void add(Foto neuesFoto){
        em.persist(neuesFoto);
    }

    /**
     * Diese Methode sucht ein Foto anhand des Base64-Strings, in dem die
     * Bilddaten gespeichert sind.
     *
     * @param base64 Der Base64-String
     * @return Das Foto
     */
    public Foto getByBase64(String base64){
        Query query = em.createNamedQuery(Foto.class.getSimpleName() + ".findByBase64");
        query.setParameter("base64", base64);

        List<Foto> fotoList = query.getResultList();

        Foto f = fotoList.get(0);

        return f;
    }

}
