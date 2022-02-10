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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin
 */
@Entity
@Table(name = "prenos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prenos.findAll", query = "SELECT p FROM Prenos p"),
    @NamedQuery(name = "Prenos.findByIdTra", query = "SELECT p FROM Prenos p WHERE p.idTra = :idTra"),
    @NamedQuery(name = "Prenos.findByNaRac", query = "SELECT p FROM Prenos p WHERE p.naRac = :naRac")})
public class Prenos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTra")
    private Integer idTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NaRac")
    private int naRac;

    public Prenos() {
    }

    public Prenos(Integer idTra) {
        this.idTra = idTra;
    }

    public Prenos(Integer idTra, int naRac) {
        this.idTra = idTra;
        this.naRac = naRac;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public int getNaRac() {
        return naRac;
    }

    public void setNaRac(int naRac) {
        this.naRac = naRac;
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
        if (!(object instanceof Prenos)) {
            return false;
        }
        Prenos other = (Prenos) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Prenos{" + "idTra=" + idTra + ", naRac=" + naRac + '}';
    }
    
}
