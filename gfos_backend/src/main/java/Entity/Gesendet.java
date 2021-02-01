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
@Table(name = "GESENDET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gesendet.findAll", query = "SELECT g FROM Gesendet g"),
    @NamedQuery(name = "Gesendet.findByChatid", query = "SELECT g FROM Gesendet g WHERE g.gesendetPK.chatid = :chatid"),
    @NamedQuery(name = "Gesendet.findByNachrichtid", query = "SELECT g FROM Gesendet g WHERE g.gesendetPK.nachrichtid = :nachrichtid")})
public class Gesendet implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GesendetPK gesendetPK;

    public Gesendet() {
    }

    public Gesendet(GesendetPK gesendetPK) {
        this.gesendetPK = gesendetPK;
    }

    public Gesendet(int chatid, int nachrichtid) {
        this.gesendetPK = new GesendetPK(chatid, nachrichtid);
    }

    public GesendetPK getGesendetPK() {
        return gesendetPK;
    }

    public void setGesendetPK(GesendetPK gesendetPK) {
        this.gesendetPK = gesendetPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gesendetPK != null ? gesendetPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gesendet)) {
            return false;
        }
        Gesendet other = (Gesendet) object;
        if ((this.gesendetPK == null && other.gesendetPK != null) || (this.gesendetPK != null && !this.gesendetPK.equals(other.gesendetPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Gesendet[ gesendetPK=" + gesendetPK + " ]";
    }
    
}
