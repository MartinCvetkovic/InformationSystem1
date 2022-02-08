/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin
 */
@Entity
@Table(name = "transakcija")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transakcija.findAll", query = "SELECT t FROM Transakcija t"),
    @NamedQuery(name = "Transakcija.findByIdTra", query = "SELECT t FROM Transakcija t WHERE t.idTra = :idTra"),
    @NamedQuery(name = "Transakcija.findByDatumVreme", query = "SELECT t FROM Transakcija t WHERE t.datumVreme = :datumVreme"),
    @NamedQuery(name = "Transakcija.findByIznos", query = "SELECT t FROM Transakcija t WHERE t.iznos = :iznos"),
    @NamedQuery(name = "Transakcija.findByRedBr", query = "SELECT t FROM Transakcija t WHERE t.redBr = :redBr"),
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IdTra")
    private Integer idTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DatumVreme")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datumVreme;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Iznos")
    private float iznos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RedBr")
    private int redBr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Svrha")
    private String svrha;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Isplata isplata;
    @JoinColumn(name = "IdRac", referencedColumnName = "IdRac")
    @ManyToOne(optional = false)
    private Racun idRac;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Prenos prenos;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "transakcija")
    private Uplata uplata;

    public Transakcija() {
    }

    public Transakcija(Integer idTra) {
        this.idTra = idTra;
    }

    public Transakcija(Integer idTra, Date datumVreme, float iznos, int redBr, String svrha) {
        this.idTra = idTra;
        this.datumVreme = datumVreme;
        this.iznos = iznos;
        this.redBr = redBr;
        this.svrha = svrha;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public Date getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(Date datumVreme) {
        this.datumVreme = datumVreme;
    }

    public float getIznos() {
        return iznos;
    }

    public void setIznos(float iznos) {
        this.iznos = iznos;
    }

    public int getRedBr() {
        return redBr;
    }

    public void setRedBr(int redBr) {
        this.redBr = redBr;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public Isplata getIsplata() {
        return isplata;
    }

    public void setIsplata(Isplata isplata) {
        this.isplata = isplata;
    }

    public Racun getIdRac() {
        return idRac;
    }

    public void setIdRac(Racun idRac) {
        this.idRac = idRac;
    }

    public Prenos getPrenos() {
        return prenos;
    }

    public void setPrenos(Prenos prenos) {
        this.prenos = prenos;
    }

    public Uplata getUplata() {
        return uplata;
    }

    public void setUplata(Uplata uplata) {
        this.uplata = uplata;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTra != null ? idTra.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transakcija)) {
            return false;
        }
        Transakcija other = (Transakcija) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transakcija{" + "idTra=" + idTra + ", datumVreme=" + datumVreme + ", iznos=" + iznos + ", redBr=" + redBr + ", svrha=" + svrha + ", idRac=" + idRac + '}';
    }
    
}
