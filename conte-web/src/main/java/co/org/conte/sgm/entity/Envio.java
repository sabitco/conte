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
 * Envio generated by hbm2java
 */
@Entity
@Table(name="envio")
public class Envio  implements java.io.Serializable {


     private long codigo;
     private Ciudad ciudad;
     private Solicitud solicitud;
     private String tipo;
     private String empresaMensajeria;
     private String direccion;
     private String telefono;
     private String numeroGuia;
     private Character confirmacion;

    public Envio() {
    }

	
    public Envio(long codigo) {
        this.codigo = codigo;
    }
    public Envio(long codigo, Ciudad ciudad, Solicitud solicitud, String tipo, String empresaMensajeria, String direccion, String telefono, String numeroGuia, Character confirmacion) {
       this.codigo = codigo;
       this.ciudad = ciudad;
       this.solicitud = solicitud;
       this.tipo = tipo;
       this.empresaMensajeria = empresaMensajeria;
       this.direccion = direccion;
       this.telefono = telefono;
       this.numeroGuia = numeroGuia;
       this.confirmacion = confirmacion;
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
    @JoinColumn(name="c_ciudad")
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_solicitud")
    public Solicitud getSolicitud() {
        return this.solicitud;
    }
    
    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
    
    @Column(name="tipo", length=15)
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Column(name="empresa_mensajeria", length=45)
    public String getEmpresaMensajeria() {
        return this.empresaMensajeria;
    }
    
    public void setEmpresaMensajeria(String empresaMensajeria) {
        this.empresaMensajeria = empresaMensajeria;
    }
    
    @Column(name="direccion", length=45)
    public String getDireccion() {
        return this.direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    @Column(name="telefono", length=45)
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    @Column(name="numero_guia", length=60)
    public String getNumeroGuia() {
        return this.numeroGuia;
    }
    
    public void setNumeroGuia(String numeroGuia) {
        this.numeroGuia = numeroGuia;
    }
    
    @Column(name="confirmacion", length=1)
    public Character getConfirmacion() {
        return this.confirmacion;
    }
    
    public void setConfirmacion(Character confirmacion) {
        this.confirmacion = confirmacion;
    }




}


