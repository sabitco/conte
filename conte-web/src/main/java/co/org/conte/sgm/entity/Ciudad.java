package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Ciudad generated by hbm2java
 */
@Entity
@Table(name="ciudad")
public class Ciudad extends BaseEntity{

     private int codigo;
     private Departamento departamento;
     private String nombre;
     private Set<Asociacion> asociaciones = new HashSet<Asociacion>();
     private Set<Envio> envios = new HashSet(0);
     private Set<Tecnico> tecnicos = new HashSet(0);
     private Set<NotificacionSolicitud> notificacionSolicituds = new HashSet(0);
     private Set usuarios = new HashSet(0);
     private Set pqrses = new HashSet(0);
     private Set<Inspector> inspectors = new HashSet(0);
     private Set<Solicitud> solicitudes = new HashSet(0);

    public Ciudad() {
    }

	
    public Ciudad(int codigo, Departamento departamento) {
        this.codigo = codigo;
        this.departamento = departamento;
    }
    public Ciudad(int codigo, Departamento departamento, String nombre, Set<Envio> envios, Set<Tecnico> tecnicos, Set<NotificacionSolicitud> notificacionSolicituds, Set usuarios, Set pqrses, Set inspectors) {
       this.codigo = codigo;
       this.departamento = departamento;
       this.nombre = nombre;
       this.envios = envios;
       this.tecnicos = tecnicos;
       this.notificacionSolicituds = notificacionSolicituds;
       this.usuarios = usuarios;
       this.pqrses = pqrses;
       this.inspectors = inspectors;
    }
   
     @Id 
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="codigo", unique=true, nullable=false)
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_depto", nullable=false)
    public Departamento getDepartamento() {
        return this.departamento;
    }
    
    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    @Column(name="nombre", length=60)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Asociacion> getCiudades() {
        return this.asociaciones;
    }
    
    public void setCiudades(Set<Asociacion> asociaciones) {
        this.asociaciones = asociaciones;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Envio> getEnvios() {
        return this.envios;
    }
    
    public void setEnvios(Set<Envio> envios) {
        this.envios = envios;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Tecnico> getTecnicos() {
        return this.tecnicos;
    }
    
    public void setTecnicos(Set<Tecnico> tecnicos) {
        this.tecnicos = tecnicos;
    }

@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<NotificacionSolicitud> getNotificacionSolicituds() {
        return this.notificacionSolicituds;
    }
    
    public void setNotificacionSolicituds(Set notificacionSolicituds) {
        this.notificacionSolicituds = notificacionSolicituds;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Usuario> getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Pqrs> getPqrses() {
        return this.pqrses;
    }
    
    public void setPqrses(Set pqrses) {
        this.pqrses = pqrses;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Inspector> getInspectors() {
        return this.inspectors;
    }
    
    public void setInspectors(Set<Inspector> inspectors) {
        this.inspectors = inspectors;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="ciudad")
    public Set<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(Set<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }
    
    

    @Override
    public String toString() {
        return getNombre();
    }
    
    




}


