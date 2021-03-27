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
@Table(name = "NUTZER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nutzer.findAll", query = "SELECT n FROM Nutzer n"),
    @NamedQuery(name = "Nutzer.findById", query = "SELECT n FROM Nutzer n WHERE n.id = :id"),
    @NamedQuery(name = "Nutzer.findByBenutzername", query = "SELECT n FROM Nutzer n WHERE n.benutzername = :benutzername"),
    @NamedQuery(name = "Nutzer.findByVorname", query = "SELECT n FROM Nutzer n WHERE n.vorname = :vorname"),
    @NamedQuery(name = "Nutzer.findByNachname", query = "SELECT n FROM Nutzer n WHERE n.nachname = :nachname"),
    @NamedQuery(name = "Nutzer.findByEmail", query = "SELECT n FROM Nutzer n WHERE n.email = :email"),
    @NamedQuery(name = "Nutzer.findByPasswordhash", query = "SELECT n FROM Nutzer n WHERE n.passwordhash = :passwordhash"),
    @NamedQuery(name = "Nutzer.findByHandynummer", query = "SELECT n FROM Nutzer n WHERE n.handynummer = :handynummer"),
    @NamedQuery(name = "Nutzer.findByInfo", query = "SELECT n FROM Nutzer n WHERE n.info = :info"),
    @NamedQuery(name = "Nutzer.findByIsadmin", query = "SELECT n FROM Nutzer n WHERE n.isadmin = :isadmin")})
public class Nutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "BENUTZERNAME")
    private String benutzername;
    @Size(max = 50)
    @Column(name = "VORNAME")
    private String vorname;
    @Size(max = 50)
    @Column(name = "NACHNAME")
    private String nachname;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "PASSWORDHASH")
    private String passwordhash;
    @Size(max = 50)
    @Column(name = "HANDYNUMMER")
    private String handynummer;
    @Size(max = 256)
    @Column(name = "INFO")
    private String info;
    @Column(name = "ISADMIN")
    private Boolean isadmin;
    @JoinTable(name = "HATBLOCKIERT", joinColumns = {
        @JoinColumn(name = "NUTZER1ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "NUTZER2ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Nutzer> hatBlockiert;
    @ManyToMany(mappedBy = "hatBlockiert")
    private List<Nutzer> blockiertVon;
    @JoinTable(name = "BEFREUNDETMIT", joinColumns = {
        @JoinColumn(name = "NUTZER1ID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "NUTZER2ID", referencedColumnName = "ID")})
    @ManyToMany
    private List<Nutzer> ownFriendList;
    @ManyToMany(mappedBy = "ownFriendList")
    private List<Nutzer> otherFriendList;
    @ManyToMany(mappedBy = "nutzerList")
    private List<Chat> chatList;
    @JoinTable(name = "ISADMIN", joinColumns = {
        @JoinColumn(name = "NUTZERID", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CHATID", referencedColumnName = "CHATID")})
    @ManyToMany
    private List<Chat> adminInGroups;
    @JoinColumn(name = "PROFILBILD", referencedColumnName = "ID")
    @ManyToOne
    private Foto profilbild;

    public Nutzer() {
    }

    public Nutzer(Integer id) {
        this.id = id;
    }

    public Nutzer(Integer id, String benutzername, String email, String passwordhash) {
        this.id = id;
        this.benutzername = benutzername;
        this.email = email;
        this.passwordhash = passwordhash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public String getHandynummer() {
        return handynummer;
    }

    public void setHandynummer(String handynummer) {
        this.handynummer = handynummer;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    @XmlTransient
    public List<Nutzer> getHatBlockiert() {
        return hatBlockiert;
    }

    public void setHatBlockiert(List<Nutzer> hatBlockiert) {
        this.hatBlockiert = hatBlockiert;
    }

    @XmlTransient
    public List<Nutzer> getBlockiertVon() {
        return blockiertVon;
    }

    public void setBlockiertVon(List<Nutzer> blockiertVon) {
        this.blockiertVon = blockiertVon;
    }

    @XmlTransient
    public List<Nutzer> getOwnFriendList() {
        return ownFriendList;
    }

    public void setOwnFriendList(List<Nutzer> ownFriendList) {
        this.ownFriendList = ownFriendList;
    }

    @XmlTransient
    public List<Nutzer> getOtherFriendList() {
        return otherFriendList;
    }

    public void setOtherFriendList(List<Nutzer> otherFriendList) {
        this.otherFriendList = otherFriendList;
    }

    @XmlTransient
    public List<Chat> getChatList() {
        return chatList;
    }

    public void setChatList(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @XmlTransient
    public List<Chat> getAdminInGroups() {
        return adminInGroups;
    }

    public void setAdminInGroups(List<Chat> adminInGroups) {
        this.adminInGroups = adminInGroups;
    }

    public Foto getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(Foto profilbild) {
        this.profilbild = profilbild;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nutzer)) {
            return false;
        }
        Nutzer other = (Nutzer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Nutzer[ id=" + id + " ]";
    }
    
}
