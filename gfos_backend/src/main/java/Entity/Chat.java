/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author simon
 */
@Entity
@Table(name="CHAT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name="Chat.findAll", query="SELECT c FROM Chat c"),
    @NamedQuery(name="Chat.findByChatid", query="SELECT c FROM Chat c WHERE c.chatid = :chatid"),
    @NamedQuery(name="Chat.findByErstelldatum", query="SELECT c FROM Chat c WHERE c.erstelldatum = :erstelldatum"),
    @NamedQuery(name="Chat.findByName", query="SELECT c FROM Chat c WHERE c.name = :name"),
    @NamedQuery(name="Chat.findByBeschreibung", query="SELECT c FROM Chat c WHERE c.beschreibung = :beschreibung"),
    @NamedQuery(name="Chat.findByIsgroup", query="SELECT c FROM Chat c WHERE c.isgroup = :isgroup"),
    @NamedQuery(name="Chat.findByIsblocked", query="SELECT c FROM Chat c WHERE c.isblocked = :isblocked"),
    @NamedQuery(name="Chat.findByGotblocked", query="SELECT c FROM Chat c WHERE c.gotblocked = :gotblocked"),
    @NamedQuery(name="Chat.findByNnew", query="SELECT c FROM Chat c WHERE c.nnew = :nnew"),
    @NamedQuery(name="Chat.findByIspinned", query="SELECT c FROM Chat c WHERE c.ispinned = :ispinned")})
public class Chat implements Serializable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="CHATID")
    private Integer chatid;
    @Column(name="ERSTELLDATUM")
    private BigInteger erstelldatum;
    @Size(max=50)
    @Column(name="NAME")
    private String name;
    @Size(max=100)
    @Column(name="BESCHREIBUNG")
    private String beschreibung;
    @Column(name="ISGROUP")
    private Boolean isgroup;
    @Column(name="ISBLOCKED")
    private Boolean isblocked;
    @Column(name="GOTBLOCKED")
    private Boolean gotblocked;
    @Column(name="NNEW")
    private Integer nnew;
    @Column(name="ISPINNED")
    private Boolean ispinned;
    @JoinTable(name="NIMMTTEIL", joinColumns={
        @JoinColumn(name="CHATID", referencedColumnName="CHATID")}, inverseJoinColumns={
        @JoinColumn(name="NUTZERID", referencedColumnName="ID")})
    @ManyToMany
    private List<Nutzer> nutzerList;
    @ManyToMany(mappedBy="adminInGroups")
    private List<Nutzer> adminList;
    @JoinColumn(name="PROFILBILD", referencedColumnName="ID")
    @ManyToOne
    private Foto profilbild;
    @JoinColumn(name="LETZTENACHRICHT", referencedColumnName="NACHRICHTID")
    @ManyToOne
    private Nachricht letztenachricht;

    public Chat(){
    }

    public Chat(Integer chatid){
        this.chatid = chatid;
    }

    public Integer getChatid(){
        return chatid;
    }

    public void setChatid(Integer chatid){
        this.chatid = chatid;
    }

    public BigInteger getErstelldatum(){
        return erstelldatum;
    }

    public void setErstelldatum(BigInteger erstelldatum){
        this.erstelldatum = erstelldatum;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getBeschreibung(){
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung){
        this.beschreibung = beschreibung;
    }

    public Boolean getIsgroup(){
        return isgroup;
    }

    public void setIsgroup(Boolean isgroup){
        this.isgroup = isgroup;
    }

    public Boolean getIsblocked(){
        return isblocked;
    }

    public void setIsblocked(Boolean isblocked){
        this.isblocked = isblocked;
    }

    public Boolean getGotblocked(){
        return gotblocked;
    }

    public void setGotblocked(Boolean gotblocked){
        this.gotblocked = gotblocked;
    }

    public Integer getNnew(){
        return nnew;
    }

    public void setNnew(Integer nnew){
        this.nnew = nnew;
    }

    public Boolean getIspinned(){
        return ispinned;
    }

    public void setIspinned(Boolean ispinned){
        this.ispinned = ispinned;
    }

    @XmlTransient
    public List<Nutzer> getNutzerList(){
        return nutzerList;
    }

    public void setNutzerList(List<Nutzer> nutzerList){
        this.nutzerList = nutzerList;
    }

    @XmlTransient
    public List<Nutzer> getAdminList(){
        return adminList;
    }

    public void setAdminList(List<Nutzer> adminList){
        this.adminList = adminList;
    }

    public Foto getProfilbild(){
        return profilbild;
    }

    public void setProfilbild(Foto profilbild){
        this.profilbild = profilbild;
    }

    public Nachricht getLetztenachricht(){
        return letztenachricht;
    }

    public void setLetztenachricht(Nachricht letztenachricht){
        this.letztenachricht = letztenachricht;
    }

    @Override
    public int hashCode(){
        int hash = 0;
        hash += (chatid != null ? chatid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object){
        // TODO: Warning - this method won't work in the case the id fields are not set
        if(!(object instanceof Chat)){
            return false;
        }
        Chat other = (Chat) object;
        if((this.chatid == null && other.chatid != null) || (this.chatid != null && !this.chatid.equals(other.chatid))){
            return false;
        }
        return true;
    }

    @Override
    public String toString(){
        return "Entity.Chat[ chatid=" + chatid + " ]";
    }

}
