package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TipoDocumento generated by hbm2java
 */
@Entity
@Table(name="tipo_documento")
public class TipoDocumento  implements java.io.Serializable {


     private Integer codigo;
     private String nombre;
     private String descripcion;

    public TipoDocumento() {
    }

    public TipoDocumento(String nombre, String descripcion) {
       this.nombre = nombre;
       this.descripcion = descripcion;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="codigo", unique=true, nullable=false)
    public Integer getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    @Column(name="nombre", length=45)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Column(name="descripcion", length=45)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
    




}


