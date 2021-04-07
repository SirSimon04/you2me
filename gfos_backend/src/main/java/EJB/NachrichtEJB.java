/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import javax.ejb.Stateless;
import Entity.Nachricht;
import Entity.Nutzer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Entity.Nutzer;
import java.util.ArrayList;
import javax.ejb.EJB;

/**
 * <h1>Die Klasse zum Verwalten der Nachrichten in der Datenbank</h1>
 * <p>
 * Diese Klasse beinhaltet alle Methoden zur Verknüpfung des Webservices mit der
 * Datenbank bezogen auf die Nachrichten. Die Daten werden bei Anfrage des
 * Webservers übergeben.</p>
 *
 * @author simon
 */
@Stateless
public class NachrichtEJB{

    @PersistenceContext
    private EntityManager em;

    @EJB
    private NutzerEJB nutzerEJB;
    @EJB
    private ChatEJB chatEJB;

    /**
     * Diese Methode fügt eine Nachricht in die Datenbank ein.
     *
     * @param neueNachricht Die Nachricht
     */
    public void add(Nachricht neueNachricht){
        em.persist(neueNachricht);
    }

    /**
     * Diese Methode löscht eine Nachricht aus der Datenbank.
     *
     * @param id Die id der zu löschenden Nachricht.
     */
    public void delete(int id){
        Nachricht n = getById(id);
        em.remove(n);
    }

    /**
     * Diese Methode gibt alle Nachrichten zurück.
     *
     * @return Eine Liste mit allen Nachrichten.
     */
    public List<Nachricht> getAll(){
        List<Nachricht> nachrichtList = em.createNamedQuery(Nachricht.class.getSimpleName() + ".findAll").getResultList();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setNutzerList(null);
            try{
                n.setSender(nutzerEJB.getCopyById(n.getSenderid()).getBenutzername());
            }catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }
        }
        return nachrichtList;
    }

    /**
     * Diese Methode gibt eine Nachricht anhand ihrer Id zurück.
     *
     * @param id Die Id der Nachricht.
     * @return Die Nachricht.
     */
    public Nachricht getById(int id){
        return em.find(Nachricht.class, id);
    }

    public Nachricht getCopyById(int id){
        Nachricht n = em.find(Nachricht.class, id);
        em.detach(n);
        for(Nutzer nutzer : n.getNutzerList()){
            em.detach(nutzer);
            nutzer.setPasswordhash(null);
            nutzer.setAdminInGroups(null);
            nutzer.setOwnFriendList(null);
            nutzer.setOtherFriendList(null);
            nutzer.setPinnedChats(null);
            nutzer.setHatBlockiert(null);
            nutzer.setBlockiertVon(null);
            nutzer.setSetting(null);
        }
        return n;
    }

    /**
     * Diese Methode gibt alle Nachrichten eines Chats zurück. Dabei wird der
     * Benutzername des Senders aus der Id des Senders ermittelt. Das geschieht
     * aus dem Grund, dass der Benutzername des Senders nicht bei jeder
     * Nachricht gespeichert werden soll und ein Nutzer seinen Benutzernamen
     * ändern kann. Existiert der Nutzer nicht mehr in der Datenbank, wird das
     * im Sender der Nachricht deutlich.
     *
     * @param id
     * @return
     */
    public List<Nachricht> getCopyByChat(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setNutzerList(null);
            for(Nutzer nu : n.getLikedBy()){
                em.detach(nu);
            }
            try{
                n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());
            }catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }

        }
        return nachrichtList;
    }

    public List<Nachricht> getByChat(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        return nachrichtList;
    }

    /**
     * Diese Methode gibt die neueste Nachricht aus einem Chat zurück. Dabei
     * wird der Benutzername des Senders aus der Id des Senders ermittelt. Das
     * geschieht aus dem Grund, dass der Benutzername des Senders nicht bei
     * jeder Nachricht gespeichert werden soll und ein Nutzer seinen
     * Benutzernamen ändern kann. Existiert der Nutzer nicht mehr in der
     * Datenbank, wird das im Sender der Nachricht deutlich. Außerdem wird
     * überprüft, ob das Sendedatum der Nachricht vor dem aktuellen Datum liegt,
     * wenn das nicht der Fall ist werden die Nachrichten des Chats solange
     * zurückgelaufen, bis eine ungeplante Nachricht gefunden wurde.
     *
     * @param id
     * @return
     */
    public Nachricht getNewest(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        if(nachrichtList.size() == 0){
            Nachricht na = new Nachricht();
            return na;
        }else if(nachrichtList.size() == 1){
            Nachricht n = nachrichtList.get(0);
            n.setNachrichtList(null);
            n.setNutzerList(null);

            try{
                n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());

            }catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }

            if(nachrichtList.get(0).getDatumuhrzeit() < System.currentTimeMillis()){
                return n;
            }
            return null;

        }else{
            for(Nachricht n : nachrichtList){
                em.detach(n);
                n.setNachrichtList(null);
                n.setNutzerList(null);
            }
//            Nachricht n = nachrichtList.get(nachrichtList.size() - 1);
            Nachricht n = null;
            for(int i = nachrichtList.size() - 1; i > 0; i--){
                if(nachrichtList.get(i).getDatumuhrzeit() < System.currentTimeMillis()){
                    n = nachrichtList.get(i);
                    break;
                }
            }
            em.detach(n);
            n.setNachrichtList(null);
            n.setNutzerList(null);
            try{
                n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());

            }catch(Exception e){
                n.setSender("gelöschter Nutzer");
            }

            return n;
        }

    }

    public List<Nachricht> getOwnMessages(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery(Nachricht.class.getSimpleName() + ".findAll").getResultList();
        List<Nachricht> r = new ArrayList<>();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            if(n.getSenderid() == id){
                r.add(n);
            }
        }
        return r;
    }

    public List<Nachricht> getMarkedMessages(int id){
        Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);
        List<Nachricht> nList = self.getMarkedMessages();
        for(Nachricht n : nList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setNutzerList(null);
            if(chatEJB.getById(n.getChatid()).getIsgroup()){
                n.setSender(nutzerEJB.getById(id).getBenutzername() + " |||| " + chatEJB.getById(n.getChatid()).getName());
            }
        }
        return nList;
    }

    public void markiere(Nachricht na, Nutzer nu){
        if(nu.getMarkedMessages().contains(na)){
            nu.getMarkedMessages().remove(na);
        }else{
            nu.getMarkedMessages().add(na);
        }

    }

}
