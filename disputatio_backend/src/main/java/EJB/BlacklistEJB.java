/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Blacklist;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <h1>Die Klasse zum Verwalten der Blacklist-Tokens in der Datenbank.</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der
 * Datenbank bezogen auf Tokens, die der Blacklist hinzugefügt werden sollen.
 * Das sind Token von Nutzern, deren Token nicht einfach abgelaufen ist, sondern
 * die sich bewusst aus dem System ausgeloggt haben. Die Daten werden bei
 * Anfrage des Webservers übergeben.</p>
 *
 * @author simon
 */
@Stateless
public class BlacklistEJB{

    @PersistenceContext
    private EntityManager em;

    /**
     * Diese Methode setzt ein Token auf die Blacklist. Das ist für den Fall,
     * dass ein Nutzer sich ausloggt.
     *
     * @param bl Das Token und die aktuelle Zeit.
     */
    public void logOut(Blacklist bl){
        em.persist(bl);
    }

    /**
     * Diese Methode gibt alle Tokens, die sich auf der Blacklist befinden, mit
     * ihrem zugehörigem Datum zurück.
     *
     * @return Alle Tokens auf der Blacklist
     */
    public List<Blacklist> getAllBlacklisted(){
        return em.createNamedQuery(Blacklist.class.getSimpleName() + ".findAll").getResultList();
    }

    /**
     * Dese Methode löscht ein bestimmtes Token von der Blacklist.
     *
     * @param token Das Token
     */
    public void deleteToken(String token){
        em.remove(em.createNamedQuery(Blacklist.class.getSimpleName() + ".findByToken").setParameter("token", token).getSingleResult());
    }

    /**
     * Diese Methode löscht alle Token von der Blacklist. Sie wird 30 Sekuden
     * nach dem Start des Programms und dann einmal pro Stunde aufgerufen, um das
     * Überprüfen, ob sich ein bestimmtest Token in der Datenbank befindet, zu verschnellern.
     */
    public void clearBlacklist(){
        List<Blacklist> bl = em.createNamedQuery(Blacklist.class.getSimpleName() + ".findAll").getResultList();
        for(Blacklist b : bl){
            em.remove(b);
        }
    }
}
