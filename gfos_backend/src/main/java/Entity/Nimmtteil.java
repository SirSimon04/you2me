/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author simon
 */
@Entity
@Table(name = "NIMMTTEIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nimmtteil.findAll", query = "SELECT n FROM Nimmtteil n"),
    @NamedQuery(name = "Nimmtteil.findByNutzerid", query = "SELECT n FROM Nimmtteil n WHERE n.nimmtteilPK.nutzerid = :nutzerid"),
    @NamedQuery(name = "Nimmtteil.findByChatid", query = "SELECT n FROM Nimmtteil n WHERE n.nimmtteilPK.chatid = :chatid")})
public class Nimmtteil implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected NimmtteilPK nimmtteilPK;

    public Nimmtteil() {
    }

    public Nimmtteil(NimmtteilPK nimmtteilPK) {
        this.nimmtteilPK = nimmtteilPK;
    }

    public Nimmtteil(int nutzerid, int chatid) {
        this.nimmtteilPK = new NimmtteilPK(nutzerid, chatid);
    }

    public NimmtteilPK getNimmtteilPK() {
        return nimmtteilPK;
    }

    public void setNimmtteilPK(NimmtteilPK nimmtteilPK) {
        this.nimmtteilPK = nimmtteilPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nimmtteilPK != null ? nimmtteilPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Nimmtteil)) {
            return false;
        }
        Nimmtteil other = (Nimmtteil) object;
        if ((this.nimmtteilPK == null && other.nimmtteilPK != null) || (this.nimmtteilPK != null && !this.nimmtteilPK.equals(other.nimmtteilPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Nimmtteil[ nimmtteilPK=" + nimmtteilPK + " ]";
    }
    
}
