package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


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
 * Resolucion generated by hbm2java
 */
@Entity
@Table(name="resolucion")
public class Resolucion  implements java.io.Serializable {


     private long codigo;
     private Solicitud solicitud;
     private Date fecha;
     private String numero;

    public Resolucion() {
    }

	
    public Resolucion(long codigo, Solicitud solicitud) {
        this.codigo = codigo;
        this.solicitud = solicitud;
    }
    public Resolucion(long codigo, Solicitud solicitud, Date fecha, String numero) {
       this.codigo = codigo;
       this.solicitud = solicitud;
       this.fecha = fecha;
       this.numero = numero;
    }
   
     @Id 
    
    @Column(name="codigo", unique=true, nullable=false)
    public long getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_solicitud", nullable=false)
    public Solicitud getSolicitud() {
        return this.solicitud;
    }
    
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
    @Temporal(TemporalType.DATE)
    @Column(name="fecha", length=10)
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    @Column(name="numero", length=10)
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }




}


