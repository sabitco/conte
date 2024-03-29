package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * EncuestaSatisfaccion generated by hbm2java
 */
@Entity
@Table(name="encuesta_satisfaccion")
public class EncuestaSatisfaccion  implements java.io.Serializable {


     private Integer codigo;
     private Pqrs pqrs;
     private Integer valoracion;
     private String observaciones;

    public EncuestaSatisfaccion() {
    }

    public EncuestaSatisfaccion(Pqrs pqrs, Integer valoracion, String observaciones) {
       this.pqrs = pqrs;
       this.valoracion = valoracion;
       this.observaciones = observaciones;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="codigo", unique=true, nullable=false)
    public Integer getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_queja")
    public Pqrs getPqrs() {
        return this.pqrs;
    }
    
    public void setPqrs(Pqrs pqrs) {
        this.pqrs = pqrs;
    }
    
    @Column(name="valoracion")
    public Integer getValoracion() {
        return this.valoracion;
    }
    
    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }
    
    @Column(name="observaciones", length=100)
    public String getObservaciones() {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }




}


