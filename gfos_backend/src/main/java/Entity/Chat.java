/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author simon
 */
@Entity
@Table(name = "CHAT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Chat.findAll", query = "SELECT c FROM Chat c"),
    @NamedQuery(name = "Chat.findByChatid", query = "SELECT c FROM Chat c WHERE c.chatid = :chatid"),
    @NamedQuery(name = "Chat.findByErstelldatum", query = "SELECT c FROM Chat c WHERE c.erstelldatum = :erstelldatum"),
    @NamedQuery(name = "Chat.findByName", query = "SELECT c FROM Chat c WHERE c.name = :name"),
    @NamedQuery(name = "Chat.findByBeschreibung", query = "SELECT c FROM Chat c WHERE c.beschreibung = :beschreibung"),
    @NamedQuery(name = "Chat.findByProfilbild", query = "SELECT c FROM Chat c WHERE c.profilbild = :profilbild")})
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHATID")
    private Integer chatid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ERSTELLDATUM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date erstelldatum;
    @Size(max = 255)
    @Column(name = "NAME")
    private String name;
    @Size(max = 255)
    @Column(name = "BESCHREIBUNG")
    private String beschreibung;
    @Size(max = 10000)
    @Column(name = "PROFILBILD")
    private String profilbild;

    public Chat() {
    }

    public Chat(Integer chatid) {
        this.chatid = chatid;
    }

    public Chat(Integer chatid, Date erstelldatum) {
        this.chatid = chatid;
        this.erstelldatum = erstelldatum;
    }

    public Integer getChatid() {
        return chatid;
    }

    public void setChatid(Integer chatid) {
        this.chatid = chatid;
    }

    public Date getErstelldatum() {
        return erstelldatum;
    }

    public void setErstelldatum(Date erstelldatum) {
        this.erstelldatum = erstelldatum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
        this.profilbild = profilbild;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chatid != null ? chatid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Chat)) {
            return false;
        }
        Chat other = (Chat) object;
        if ((this.chatid == null && other.chatid != null) || (this.chatid != null && !this.chatid.equals(other.chatid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Chat[ chatid=" + chatid + " ]";
    }
    
}
