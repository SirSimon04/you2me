/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EJB;

import Entity.Nachricht;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import Entity.Nutzer;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;

/**
 * <h1>Die Klasse zum Verwalten der Nachrichten in der Datenbank.</h1>
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
            n.setLikedBy(null);
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

    /**
     * Diese Nachricht gibt eine Nachricht anhand ihrer Id zurück.
     * Dabei wird die Datenbankverbindung der Nachricht sowie die der Nutzer in der Nutzerliste der Nachricht getrennt.
     *
     * @param id Die Id der Nachricht.
     * @return Die Nachricht.
     */
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
            nutzer.setMarkedMessages(null);
            nutzer.setArchivedChats(null);
        }
        return n;
    }

    /**
     * Diese Methode gibt eine Nachricht anhand ihrer Id zurück. Dabei wird die
     * Datenbankverbindung der Nachricht getrennt und die beiden Listen der Nachricht werden geleert.
     *
     * @param id Die Id der Nachricht.
     * @return Das Nachricht.
     */
    public Nachricht getCopyByIdListsNull(int id){
        Nachricht n = em.find(Nachricht.class, id);
        em.detach(n);
        n.setNutzerList(null);
        n.setNachrichtList(null);
        n.setLikedBy(null);
        return n;
    }

    /**
     * Diese Methode gibt alle Nachrichten eines Chats zurück. Dabei wird der
     * Benutzername des Senders aus der Id des Senders ermittelt. Das geschieht
     * aus dem Grund, dass der Benutzername des Senders nicht bei jeder
     * Nachricht gespeichert werden soll und ein Nutzer seinen Benutzernamen
     * ändern kann. Existiert der Nutzer nicht mehr in der Datenbank, wird das
     * im Namen des Senders der Nachricht deutlich, dieser wird dann auf "gelöschter Nutzer"
     * gesetzt.
     *
     * @param id
     * @return Die Liste mit allen Nachrichten des Chats.
     */
    public List<Nachricht> getCopyByChat(int id){
        try{
            List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
            for(Nachricht n : nachrichtList){
                try{
                    em.detach(n);
                    n.setNachrichtList(null);
                    n.setNutzerList(null);
                    //            n.setLikedBy(null);
                    for(Nutzer nutzer : n.getLikedBy()){
                        em.detach(nutzer);
                        nutzer.setPasswordhash(null);
                        nutzer.setAdminInGroups(null);
                        nutzer.setOwnFriendList(null);
                        nutzer.setOtherFriendList(null);
                        nutzer.setPinnedChats(null);
                        nutzer.setHatBlockiert(null);
                        nutzer.setBlockiertVon(null);
                        nutzer.setSetting(null);
                        nutzer.setArchivedChats(null);
                        nutzer.setMarkedMessages(null);
                    }
                }catch(Exception e){
                }

                try{
                    n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());
                }catch(Exception e){
                    n.setSender("gelöschter Nutzer");
                }

            }
            return nachrichtList;
        }catch(Exception e){
            return new ArrayList<Nachricht>();
        }

    }

    /**
     * Diese Methode gibt alle Nachrichten eines bestimmten Chats zurück.
     *
     * @param id Die Id des Chats.
     * @return Eine Liste mit allen Nachrichten.
     */
    public List<Nachricht> getByChat(int id){
        try{
            List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
            return nachrichtList;
        }catch(Exception e){
            return new ArrayList<Nachricht>();
        }

    }

    /**
     * Diese Methode gibt alle Nachrichten eines Chats zurück.
     * Dabei wird die Datenbankverbindung aller Nachrichten getrennt.
     *
     * @param id Die Id des Chats.
     * @return Eine Liste mit allen Nachrichten.
     */
    public List<Nachricht> getDetachedByChat(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        for(Nachricht n : nachrichtList){
            em.detach(n);
            for(Nutzer nu : n.getNutzerList()){
                em.detach(nu);
            }
        }
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
     * @return Die neueste Nachricht.
     */
    public Nachricht getNewest(int id){
        List<Nachricht> nachrichtList = em.createNamedQuery("Nachricht.findByChatid").setParameter("chatid", id).getResultList();
        switch(nachrichtList.size()){
            case 0:
                Nachricht na = new Nachricht();
                return na;
            case 1: {
                Nachricht n = nachrichtList.get(0);
                em.detach(n);
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

            }
            default: {
                for(Nachricht n : nachrichtList){
                    em.detach(n);
                    n.setNachrichtList(null);
                    n.setNutzerList(null);
                    n.setLikedBy(null);
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
                n.setLikedBy(null);
                try{
                    n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());

                }catch(Exception e){
                    n.setSender("gelöschter Nutzer");
                }

                return n;
            }
        }

    }

    /**
     * Diese Methode gibt alle Nachrichten zurück, die vom eigenen Nutzer gesendet wurden.
     *
     * @param id Die eigene Id
     * @return Die Liste mit allen selbst gesendeten Nachrichten.
     */
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

    /**
     * Diese Methode gibt alle Nachrichten zurück, die vom eigenen Nutzer markiert wurden.
     *
     * @param id Die eigene Id.
     * @return Die Liste mit allen markierten Nachrichten.
     */
    public List<Nachricht> getMarkedMessages(int id){
        Nutzer self = nutzerEJB.getCopyByIdListsNotNull(id);
        List<Nachricht> nList = self.getMarkedMessages();
        for(Nachricht n : nList){
            em.detach(n);
            n.setNachrichtList(null);
            n.setNutzerList(null);
            n.setLikedBy(null);
            n.setAntwortauf(null);
            if(chatEJB.getById(n.getChatid()) != null){
                if(chatEJB.getById(n.getChatid()).getIsgroup()){
                    try{
                        if(self.getId() == n.getSenderid()){
                            n.setSender("Du in " + chatEJB.getById(n.getChatid()).getName());
                        }else{
                            n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername() + " in " + chatEJB.getById(n.getChatid()).getName());
                        }

                    }catch(Exception e){
                        n.setSender("Gelöschter Nutzer in " + chatEJB.getById(n.getChatid()).getName());
                    }

                }else{
                    try{
                        if(self.getId() == n.getSenderid()){
                            n.setSender("Du");
                        }else{
                            n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername());
                        }

                    }catch(Exception e){
                        n.setSender("Gelöschter Nutzer");
                    }
                }
            }else{
                try{
                    if(self.getId() == n.getSenderid()){
                        n.setSender("Du in gelöschtem Chat");
                    }else{
                        n.setSender(nutzerEJB.getById(n.getSenderid()).getBenutzername() + " in gelöschtem Chat");
                    }

                }catch(Exception e){
                    n.setSender("Gelöschter Nutzer in gelöschtem Chat");
                }
            }

        }
        return nList;
//        return new ArrayList<Nachricht>();
    }

    /**
     * Diese Methode markiert eine Nachricht für den eigenen Nutzer.
     *
     * @param na Die zu markierende Nachricht.
     * @param nu Der eigene Nutzer.
     */
    public void markMessage(Nachricht na, Nutzer nu){
        if(nu.getMarkedMessages().contains(na)){
            nu.getMarkedMessages().remove(na);
        }else{
            nu.getMarkedMessages().add(na);
        }

    }

}
