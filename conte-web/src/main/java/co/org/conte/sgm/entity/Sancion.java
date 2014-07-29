/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 *
 * @author jam
 */
@Entity
@Table(name="sancion"
)
public class Sancion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="codigo", unique=true, nullable=false)
    private int codigo;
    
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_inicio")
    private Date fechaInicio;
   
    @Temporal(TemporalType.DATE)
    @Column(name="fecha_fin", length=10)
    private Date fechaFin;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_tecnico", nullable=false)
    private Tecnico tecnico;
    
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Tecnico getTecnico() {
        return tecnico; 
   }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
            

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
        
}
