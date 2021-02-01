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
@Table(name = "NUTZER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nutzer.findAll", query = "SELECT n FROM Nutzer n"),
    @NamedQuery(name = "Nutzer.findById", query = "SELECT n FROM Nutzer n WHERE n.id = :id"),
    @NamedQuery(name = "Nutzer.findByBenutzername", query = "SELECT n FROM Nutzer n WHERE n.benutzername = :benutzername"),
    @NamedQuery(name = "Nutzer.findByVorname", query = "SELECT n FROM Nutzer n WHERE n.vorname = :vorname"),
    @NamedQuery(name = "Nutzer.findByNachname", query = "SELECT n FROM Nutzer n WHERE n.nachname = :nachname"),
    @NamedQuery(name = "Nutzer.findByEmail", query = "SELECT n FROM Nutzer n WHERE n.email = :email"),
    @NamedQuery(name = "Nutzer.findByPasswort", query = "SELECT n FROM Nutzer n WHERE n.passwort = :passwort"),
    @NamedQuery(name = "Nutzer.findByHandynummer", query = "SELECT n FROM Nutzer n WHERE n.handynummer = :handynummer"),
    @NamedQuery(name = "Nutzer.findByProfilbild", query = "SELECT n FROM Nutzer n WHERE n.profilbild = :profilbild"),
    @NamedQuery(name = "Nutzer.findByInfo", query = "SELECT n FROM Nutzer n WHERE n.info = :info")})
public class Nutzer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @Size(min = 1, max = 50)
    @Column(name = "PASSWORT")
    private String passwort;
    @Size(max = 50)
    @Column(name = "HANDYNUMMER")
    private String handynummer;
    @Size(max = 10000)
    @Column(name = "PROFILBILD")
    private String profilbild;
    @Size(max = 256)
    @Column(name = "INFO")
    private String info;

    public Nutzer() {
    }

    public Nutzer(Integer id) {
        this.id = id;
    }

    public Nutzer(Integer id, String benutzername, String email, String passwort) {
        this.id = id;
        this.benutzername = benutzername;
        this.email = email;
        this.passwort = passwort;
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

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getHandynummer() {
        return handynummer;
    }

    public void setHandynummer(String handynummer) {
        this.handynummer = handynummer;
    }

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
        this.profilbild = profilbild;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
