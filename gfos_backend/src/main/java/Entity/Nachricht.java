/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author simon
 */
@Entity
@Table(name = "NACHRICHT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nachricht.findAll", query = "SELECT n FROM Nachricht n"),
    @NamedQuery(name = "Nachricht.findByNachrichtid", query = "SELECT n FROM Nachricht n WHERE n.nachrichtid = :nachrichtid"),
    @NamedQuery(name = "Nachricht.findBySenderid", query = "SELECT n FROM Nachricht n WHERE n.senderid = :senderid"),
    @NamedQuery(name = "Nachricht.findByChatid", query = "SELECT n FROM Nachricht n WHERE n.chatid = :chatid"),
    @NamedQuery(name = "Nachricht.findByUhrzeit", query = "SELECT n FROM Nachricht n WHERE n.uhrzeit = :uhrzeit"),
    @NamedQuery(name = "Nachricht.findByInhalt", query = "SELECT n FROM Nachricht n WHERE n.inhalt = :inhalt")})
public class Nachricht implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NACHRICHTID")
    private Integer nachrichtid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SENDERID")
    private int senderid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHATID")
    private int chatid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "UHRZEIT")
    private String uhrzeit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "INHALT")
    private String inhalt;

    public Nachricht() {
    }

    public Nachricht(Integer nachrichtid) {
        this.nachrichtid = nachrichtid;
    }

    public Nachricht(Integer nachrichtid, int senderid, int chatid, String uhrzeit, String inhalt) {
        this.nachrichtid = nachrichtid;
        this.senderid = senderid;
        this.chatid = chatid;
        this.uhrzeit = uhrzeit;
        this.inhalt = inhalt;
    }

    public Integer getNachrichtid() {
        return nachrichtid;
    }

    public void setNachrichtid(Integer nachrichtid) {
        this.nachrichtid = nachrichtid;
    }

    public int getSenderid() {
        return senderid;
    }

    public void setSenderid(int senderid) {
        this.senderid = senderid;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nachrichtid != null ? nachrichtid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nachricht)) {
            return false;
        }
        Nachricht other = (Nachricht) object;
        if ((this.nachrichtid == null && other.nachrichtid != null) || (this.nachrichtid != null && !this.nachrichtid.equals(other.nachrichtid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Nachricht[ nachrichtid=" + nachrichtid + " ]";
    }
    
}
