/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Setting;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <h1>Die Klasse zum Verwalten der Einstellungen in der Datenbank</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der
 * Datenbank bezogen auf die Einstellungen. Die Daten werden bei Anfrage des Webservers
 * übergeben.</p>
 *
 * @author simon
 */
@Stateless
public class SettingsEJB{

    @PersistenceContext
    private EntityManager em;

    /**
     * Diese Methode gibt alle Einstellungen zurück.
     *
     * @return Die Liste mit allen Einstellungen.
     */
    public List<Setting> getAll(){
        return em.createNamedQuery(Setting.class.getSimpleName() + ".findAll").getResultList();
    }

    /**
     * Diese Methode gibt alle Einstellungen zurück.
     * Dabei wird die Verbindung jeder
     * Einstellung zur Datenbank getrennt, das heißt die Einstellungen können ohne
     * Auswirkung auf die Datenbank verändert werden. Außerdem wird der zugehörige Nutzer auf null gesetzt.
     *
     * @return Die Liste mit allen Einstellungen.
     */
    public List<Setting> getAllCopy(){
        System.out.println("moin");
        List<Setting> l = em.createNamedQuery(Setting.class.getSimpleName() + ".findAll").getResultList();
        for(Setting s : l){
            em.detach(s);
//            s.getNutzer().setOtherFriendList(null);
//            s.getNutzer().setOwnFriendList(null);
//            s.getNutzer().setAdminInGroups(null);
//            s.getNutzer().setBlockiertVon(null);
//            s.getNutzer().setHatBlockiert(null);
//            s.getNutzer().setChatList(null);
            s.setNutzer(null);
            s.setNutzerid(null);
            System.out.println(s);
        }
        return l;
    }

    /**
     * Diese Methode gibt eine Einstellung anhand seiner Id wieder, die gleich der Id des zugehörigen Nutzer ist.
     *
     * @param id Die Id des Nutzers.
     * @return Die Einstellungen.
     */
    public Setting getById(int id){
        return em.find(Setting.class, id);
    }

    /**
     * Diese Methode gibt eine Einstellung anhand seiner Id wieder, die gleich der Id des zugehörigen Nutzer ist.
     * Dabei wird die Verbindung der
     * Einstellung zur Datenbank getrennt, das heißt die Einstellung kann ohne
     * Auswirkung auf die Datenbank verändert werden. Außerdem wird der zugehörige Nutzer auf null gesetzt.
     *
     * @param id Die Id des Nutzers.
     * @return Die Einstellungen.
     */
    public Setting getCopyById(int id){
        Setting s = em.find(Setting.class, id);
        em.detach(s);
        s.setNutzer(null);
        s.setNutzerid(null);
        return s;
    }

    /**
     * Diese Methode fügt Einstellungen neu in die Datenbank ein.
     * Sie wird beim Registrierungsvorgang für jeden Nutzer automatisch aufgerufen.
     *
     * @param s Die Einstellungen.
     */
    public void add(Setting s){
        em.persist(s);
    }

}
