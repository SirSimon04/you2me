/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author simon
 */
@Embeddable
public class GesendetPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CHATID")
    private int chatid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NACHRICHTID")
    private int nachrichtid;

    public GesendetPK() {
    }

    public GesendetPK(int chatid, int nachrichtid) {
        this.chatid = chatid;
        this.nachrichtid = nachrichtid;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    public int getNachrichtid() {
        return nachrichtid;
    }

    public void setNachrichtid(int nachrichtid) {
        this.nachrichtid = nachrichtid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) chatid;
        hash += (int) nachrichtid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GesendetPK)) {
            return false;
        }
        GesendetPK other = (GesendetPK) object;
        if (this.chatid != other.chatid) {
            return false;
        }
        if (this.nachrichtid != other.nachrichtid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.GesendetPK[ chatid=" + chatid + ", nachrichtid=" + nachrichtid + " ]";
    }
    
}
