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
    @NamedQuery(name = "Nutzer.findByVorname", query = "SELECT n FROM Nutzer n WHERE n.vorname = :vorname"),
    @NamedQuery(name = "Nutzer.findByNachname", query = "SELECT n FROM Nutzer n WHERE n.nachname = :nachname")})
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
    @Column(name = "VORNAME")
    private String vorname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NACHNAME")
    private String nachname;

    public Nutzer() {
    }

    public Nutzer(Integer id) {
        this.id = id;
    }

    public Nutzer(Integer id, String vorname, String nachname) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
