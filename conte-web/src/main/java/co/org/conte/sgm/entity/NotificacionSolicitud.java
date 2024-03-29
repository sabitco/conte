package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * NotificacionSolicitud generated by hbm2java
 */
@Entity
@Table(name="notificacion_solicitud")
public class NotificacionSolicitud  implements java.io.Serializable {


     private int codigo;
     private Ciudad ciudad;
     private Solicitud solicitud;
     private String tipo;
     private String medio;
     private String email;
     private String direccion;
     private String telefono;
     private String celular;
     private String observaciones;

    public NotificacionSolicitud() {
    }

	
    public NotificacionSolicitud(int codigo, Solicitud solicitud) {
        this.codigo = codigo;
        this.solicitud = solicitud;
    }
    public NotificacionSolicitud(int codigo, Ciudad ciudad, Solicitud solicitud, String tipo, String medio, String email, String direccion, String telefono, String celular, String observaciones) {
       this.codigo = codigo;
       this.ciudad = ciudad;
       this.solicitud = solicitud;
       this.tipo = tipo;
       this.medio = medio;
       this.email = email;
       this.direccion = direccion;
       this.telefono = telefono;
       this.celular = celular;
       this.observaciones = observaciones;
    }
   
     @Id 
    
    @Column(name="codigo", unique=true, nullable=false)
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_ciudad")
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_solicitud", nullable=false)
    public Solicitud getSolicitud() {
        return this.solicitud;
    }
    
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
    
    @Column(name="tipo", length=10)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Column(name="medio", length=10)
    public String getMedio() {
        return this.medio;
    }
    
    public void setMedio(String medio) {
        this.medio = medio;
    }
    
    @Column(name="email", length=60)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name="direccion", length=45)
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    @Column(name="telefono", length=20)
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Column(name="celular", length=20)
    public String getCelular() {
        return this.celular;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    @Column(name="observaciones", length=150)
    public String getObservaciones() {
        return this.observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }




}


