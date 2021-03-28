/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    @NamedQuery(name = "Nachricht.findByDatumuhrzeit", query = "SELECT n FROM Nachricht n WHERE n.datumuhrzeit = :datumuhrzeit"),
    @NamedQuery(name = "Nachricht.findByInhalt", query = "SELECT n FROM Nachricht n WHERE n.inhalt = :inhalt"),
    @NamedQuery(name = "Nachricht.findBySender", query = "SELECT n FROM Nachricht n WHERE n.sender = :sender"),
    @NamedQuery(name = "Nachricht.findByIsimportant", query = "SELECT n FROM Nachricht n WHERE n.isimportant = :isimportant")})
public class Nachricht implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
    @Column(name = "DATUMUHRZEIT")
    private String datumuhrzeit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "INHALT")
    private String inhalt;
    @Size(max = 50)
    @Column(name = "SENDER")
    private String sender;
    @Column(name = "ISIMPORTANT")
    private Boolean isimportant;
    @JoinTable(name = "HATGELESEN", joinColumns = {
        @JoinColumn(name = "NACHRICHTID", referencedColumnName = "NACHRICHTID")}, inverseJoinColumns = {
        @JoinColumn(name = "NUTZERID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Nutzer> nutzerList;
    @JoinColumn(name = "FOTO", referencedColumnName = "ID")
    @ManyToOne
    private Foto foto;
    @OneToMany(mappedBy = "antwortauf")
    private List<Nachricht> nachrichtList;
    @JoinColumn(name = "ANTWORTAUF", referencedColumnName = "NACHRICHTID")
    @ManyToOne
    private Nachricht antwortauf;

    public Nachricht() {
    }

    public Nachricht(Integer nachrichtid) {
        this.nachrichtid = nachrichtid;
    }

    public Nachricht(Integer nachrichtid, int senderid, int chatid, String datumuhrzeit, String inhalt) {
        this.nachrichtid = nachrichtid;
        this.senderid = senderid;
        this.chatid = chatid;
        this.datumuhrzeit = datumuhrzeit;
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

    public String getDatumuhrzeit() {
        return datumuhrzeit;
    }

    public void setDatumuhrzeit(String datumuhrzeit) {
        this.datumuhrzeit = datumuhrzeit;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getIsimportant() {
        return isimportant;
    }

    public void setIsimportant(Boolean isimportant) {
        this.isimportant = isimportant;
    }

    @XmlTransient
    public List<Nutzer> getNutzerList() {
        return nutzerList;
    }

    public void setNutzerList(List<Nutzer> nutzerList) {
        this.nutzerList = nutzerList;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }

    @XmlTransient
    public List<Nachricht> getNachrichtList() {
        return nachrichtList;
    }

    public void setNachrichtList(List<Nachricht> nachrichtList) {
        this.nachrichtList = nachrichtList;
    }

    public Nachricht getAntwortauf() {
        return antwortauf;
    }

    public void setAntwortauf(Nachricht antwortauf) {
        this.antwortauf = antwortauf;
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
