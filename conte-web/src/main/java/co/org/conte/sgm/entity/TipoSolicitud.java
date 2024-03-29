package co.org.conte.sgm.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TipoSolicitud generated by hbm2java
 */
@Entity
@Table(name="tipo_solicitud")
public class TipoSolicitud  extends BaseEntity{


     private int codigo;
     private String nombre;
     private Float porcentaje;
     private boolean estado;
     private Set<Solicitud> solicituds = new HashSet(0);

    public TipoSolicitud() {
    }

	
    public TipoSolicitud(int codigo) {
        this.codigo = codigo;
    }
    public TipoSolicitud(int codigo, String nombre, Float porcentaje, Set<Solicitud> solicitudes) {
       this.codigo = codigo;
       this.nombre = nombre;
       this.porcentaje = porcentaje;
       this.solicituds = solicitudes;
    }
   
     @Id 
    
    @Column(name="codigo", unique=true, nullable=false)
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    @Column(name="nombre", length=45)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Column(name="porcentaje")
    public Float getPorcentaje() {
        return this.porcentaje;
    }
    
    public void setPorcentaje(Float porcentaje) {
        this.porcentaje = porcentaje;
    }

    @Column(name="estado")
    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="tipoSolicitud")
    public Set<Solicitud> getSolicituds() {
        return this.solicituds;
    }
    
    public void setSolicituds(Set<Solicitud> solicituds) {
        this.solicituds = solicituds;
    }

    @Override
    public String toString() {
        return getNombre();
    }   

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.codigo;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoSolicitud other = (TipoSolicitud) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }
}