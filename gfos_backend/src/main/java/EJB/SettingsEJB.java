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
 *
 * @author simon
 */
@Stateless
public class SettingsEJB{

    @PersistenceContext
    private EntityManager em;

    public List<Setting> getAll(){
        return em.createNamedQuery(Setting.class.getSimpleName() + ".findAll").getResultList();
    }

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

    public Setting getById(int id){
        return em.find(Setting.class, id);
    }

    public Setting getCopyById(int id){
        Setting s = em.find(Setting.class, id);
        em.detach(s);
        s.setNutzer(null);
        s.setNutzerid(null);
        return s;
    }

    public void add(Setting s){
        em.persist(s);
    }

}
