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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Martin
 */
@Entity
@Table(name = "uplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uplata.findAll", query = "SELECT u FROM Uplata u"),
    @NamedQuery(name = "Uplata.findByIdTra", query = "SELECT u FROM Uplata u WHERE u.idTra = :idTra")})
public class Uplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTra")
    private Integer idTra;
    @JoinColumn(name = "IdFil", referencedColumnName = "IdFil")
    @ManyToOne(optional = false)
    private Filijala idFil;
    @JoinColumn(name = "IdTra", referencedColumnName = "IdTra", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Transakcija transakcija;

    public Uplata() {
    }

    public Uplata(Integer idTra) {
        this.idTra = idTra;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public Filijala getIdFil() {
        return idFil;
    }

    public void setIdFil(Filijala idFil) {
        this.idFil = idFil;
    }

    public Transakcija getTransakcija() {
        return transakcija;
    }

    public void setTransakcija(Transakcija transakcija) {
        this.transakcija = transakcija;
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
        if (!(object instanceof Uplata)) {
            return false;
        }
        Uplata other = (Uplata) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Uplata{" + "idTra=" + idTra + ", idFil=" + idFil.getIdFil() + '}';
    }
    
}
