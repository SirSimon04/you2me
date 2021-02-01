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
public class NimmtteilPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "NUTZERID")
    private int nutzerid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CHATID")
    private int chatid;

    public NimmtteilPK() {
    }

    public NimmtteilPK(int nutzerid, int chatid) {
        this.nutzerid = nutzerid;
        this.chatid = chatid;
    }

    public int getNutzerid() {
        return nutzerid;
    }

    public void setNutzerid(int nutzerid) {
        this.nutzerid = nutzerid;
    }

    public int getChatid() {
        return chatid;
    }

    public void setChatid(int chatid) {
        this.chatid = chatid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) nutzerid;
        hash += (int) chatid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NimmtteilPK)) {
            return false;
        }
        NimmtteilPK other = (NimmtteilPK) object;
        if (this.nutzerid != other.nutzerid) {
            return false;
        }
        if (this.chatid != other.chatid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.NimmtteilPK[ nutzerid=" + nutzerid + ", chatid=" + chatid + " ]";
    }
    
}
