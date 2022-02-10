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
@Table(name = "isplata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Isplata.findAll", query = "SELECT i FROM Isplata i"),
    @NamedQuery(name = "Isplata.findByIdTra", query = "SELECT i FROM Isplata i WHERE i.idTra = :idTra"),
    @NamedQuery(name = "Isplata.findByIdFil", query = "SELECT i FROM Isplata i WHERE i.idFil = :idFil")})
public class Isplata implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTra")
    private Integer idTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdFil")
    private int idFil;

    public Isplata() {
    }

    public Isplata(Integer idTra) {
        this.idTra = idTra;
    }

    public Isplata(Integer idTra, int idFil) {
        this.idTra = idTra;
        this.idFil = idFil;
    }

    public Integer getIdTra() {
        return idTra;
    }

    public void setIdTra(Integer idTra) {
        this.idTra = idTra;
    }

    public int getIdFil() {
        return idFil;
    }

    public void setIdFil(int idFil) {
        this.idFil = idFil;
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
        if (!(object instanceof Isplata)) {
            return false;
        }
        Isplata other = (Isplata) object;
        if ((this.idTra == null && other.idTra != null) || (this.idTra != null && !this.idTra.equals(other.idTra))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Isplata{" + "idTra=" + idTra + ", idFil=" + idFil + '}';
    }
    
}
