/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

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
    @NamedQuery(name = "Transakcija.findBySvrha", query = "SELECT t FROM Transakcija t WHERE t.svrha = :svrha"),
    @NamedQuery(name = "Transakcija.findByIdRac", query = "SELECT t FROM Transakcija t WHERE t.idRac = :idRac")})
public class Transakcija implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTra")
    private Integer idTra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DatumVreme")
    private String datumVreme;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdRac")
    private int idRac;

    public Transakcija() {
    }

    public Transakcija(Integer idTra) {
        this.idTra = idTra;
    }

    public Transakcija(Integer idTra, String datumVreme, float iznos, int redBr, String svrha, int idRac) {
        this.idTra = idTra;
        this.datumVreme = datumVreme;
        this.iznos = iznos;
        this.redBr = redBr;
        this.svrha = svrha;
        this.idRac = idRac;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public String getDatumVreme() {
        return datumVreme;
    }

    public void setDatumVreme(String datumVreme) {
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

    public int getIdRac() {
        return idRac;
    }

    public void setIdRac(int idRac) {
        this.idRac = idRac;
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
