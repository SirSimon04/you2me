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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author simon
 */
@Entity
@Table(name = "SETTING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Setting.findAll", query = "SELECT s FROM Setting s"),
    @NamedQuery(name = "Setting.findByNutzerid", query = "SELECT s FROM Setting s WHERE s.nutzerid = :nutzerid"),
    @NamedQuery(name = "Setting.findByDarkmode", query = "SELECT s FROM Setting s WHERE s.darkmode = :darkmode"),
    @NamedQuery(name = "Setting.findByLesebest\u00e4tigung", query = "SELECT s FROM Setting s WHERE s.lesebest\u00e4tigung = :lesebest\u00e4tigung"),
    @NamedQuery(name = "Setting.findByMailifimportant", query = "SELECT s FROM Setting s WHERE s.mailifimportant = :mailifimportant")})
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUTZERID")
    private Integer nutzerid;
    @Column(name = "DARKMODE")
    private Boolean darkmode;
    @Column(name = "LESEBEST\u00c4TIGUNG")
    private Boolean lesebestätigung;
    @Column(name = "MAILIFIMPORTANT")
    private Boolean mailifimportant;
    @JoinColumn(name = "NUTZERID", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Nutzer nutzer;

    public Setting() {
    }

    public Setting(Integer nutzerid) {
        this.nutzerid = nutzerid;
    }

    public Integer getNutzerid() {
        return nutzerid;
    }

    public void setNutzerid(Integer nutzerid) {
        this.nutzerid = nutzerid;
    }

    public Boolean getDarkmode() {
        return darkmode;
    }

    public void setDarkmode(Boolean darkmode) {
        this.darkmode = darkmode;
    }

    public Boolean getLesebestätigung() {
        return lesebestätigung;
    }

    public void setLesebestätigung(Boolean lesebestätigung) {
        this.lesebestätigung = lesebestätigung;
    }

    public Boolean getMailifimportant() {
        return mailifimportant;
    }

    public void setMailifimportant(Boolean mailifimportant) {
        this.mailifimportant = mailifimportant;
    }

    public Nutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Nutzer nutzer) {
        this.nutzer = nutzer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nutzerid != null ? nutzerid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Setting)) {
            return false;
        }
        Setting other = (Setting) object;
        if ((this.nutzerid == null && other.nutzerid != null) || (this.nutzerid != null && !this.nutzerid.equals(other.nutzerid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Setting[ nutzerid=" + nutzerid + " ]";
    }
    
}
