package co.org.conte.sgm.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Solicitud generated by hbm2java
 */
@Entity
@Table(name="solicitud")
public class Solicitud  implements java.io.Serializable {

     private Long radicado;
     private Ciudad ciudad;
     private Asociacion asociacion;
     private TipoSolicitud tipoSolicitud;
     private Tecnico tecnico;
     private Usuario usuario;
     private Estado estado;
     private Inspector inspector;
     private Date fechaCreacion;
     private Date ultimaModificacion;
     private Date fechaRadicacion;
     private String tipoEmpleo;
     private Integer folios;
     private String acta;
     private String referenciaPago1;
     private String referenciaPago2;
     private String consejero;
     private String empresa;
     private Boolean dependiente;
     private Integer calificacion;
     private String direccionEmpresa;
     private String entero;
     private String enteroPor;
     private String telefonoEmpresa;
     private Set solicitudFormatos = new HashSet(0);
     private Set inadmisions = new HashSet(0);
     private Set observacions = new HashSet(0);
     private Set envios = new HashSet(0);
     private Set notificacionSolicituds = new HashSet(0);
     private Set referenciaPersonals = new HashSet(0);
     private Set resolucions = new HashSet(0);
     private Set logSolicituds = new HashSet(0);
     private Set<Pago> pagos = new HashSet<Pago>(0);

    public Solicitud() {
    }

	
    public Solicitud(TipoSolicitud tipoSolicitud, Tecnico tecnico, Usuario usuario, Estado estado) {
        this.tipoSolicitud = tipoSolicitud;
        this.tecnico = tecnico;
        this.usuario = usuario;
        this.estado = estado;
    }
    public Solicitud(Ciudad ciudad, Asociacion asociacion, TipoSolicitud tipoSolicitud, Tecnico tecnico, Usuario usuario, Estado estado, Inspector inspector, Date fechaCreacion, Date ultimaModificacion, Date fechaRadicacion, String tipoEmpleo, Integer folios, String acta, String referenciaPago1, String referenciaPago2, Long CConsejero, String empresa, Boolean dependiente, Integer calificacion, String direccionEmpresa, Set solicitudFormatos, Set inadmisions, Set observacions, Set envios, Set notificacionSolicituds, Set referenciaPersonals, Set resolucions, Set logSolicituds) {
       this.ciudad = ciudad;
       this.asociacion = asociacion;
       this.tipoSolicitud = tipoSolicitud;
       this.tecnico = tecnico;
       this.usuario = usuario;
       this.estado = estado;
       this.inspector = inspector;
       this.fechaCreacion = fechaCreacion;
       this.ultimaModificacion = ultimaModificacion;
       this.fechaRadicacion = fechaRadicacion;
       this.tipoEmpleo = tipoEmpleo;
       this.folios = folios;
       this.acta = acta;
       this.referenciaPago1 = referenciaPago1;
       this.referenciaPago2 = referenciaPago2;
       
       this.empresa = empresa;
       this.dependiente = dependiente;
       this.calificacion = calificacion;
       this.direccionEmpresa = direccionEmpresa;
       this.solicitudFormatos = solicitudFormatos;
       this.inadmisions = inadmisions;
       this.observacions = observacions;
       this.envios = envios;
       this.notificacionSolicituds = notificacionSolicituds;
       this.referenciaPersonals = referenciaPersonals;
       this.resolucions = resolucions;
       this.logSolicituds = logSolicituds;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="radicado", unique=true, nullable=false)
    public Long getRadicado() {
        return this.radicado;
    }
    
    public void setRadicado(Long radicado) {
        this.radicado = radicado;
    }
@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_ciudad_empresa")
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_asociacion")
    public Asociacion getAsociacion() {
        return this.asociacion;
    }
    
    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }
@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_tipo", nullable=false)
    public TipoSolicitud getTipoSolicitud() {
        return this.tipoSolicitud;
    }
    
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }
@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_tecnico", nullable=false)
    public Tecnico getTecnico() {
        return this.tecnico;
    }
    
    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
@ManyToOne(fetch=FetchType.LAZY)

    @JoinColumn(name="c_usuario", nullable=false)
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_estado", nullable=false)
    public Estado getEstado() {
        return this.estado;
    }
    
    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_inspector")
    public Inspector getInspector() {
        return this.inspector;
    }
    
    public void setInspector(Inspector inspector) {
        this.inspector = inspector;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_creacion", length=19)
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ultima_modificacion", length=19)
    public Date getUltimaModificacion() {
        return this.ultimaModificacion;
    }
    
    public void setUltimaModificacion(Date ultimaModificacion) {
        this.ultimaModificacion = ultimaModificacion;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_radicacion", length=19)
    public Date getFechaRadicacion() {
        return this.fechaRadicacion;
    }
    
    public void setFechaRadicacion(Date fechaRadicacion) {
        this.fechaRadicacion = fechaRadicacion;
    }
    
    @Column(name="tipo_empleo", length=20)
    public String getTipoEmpleo() {
        return this.tipoEmpleo;
    }
    
    public void setTipoEmpleo(String tipoEmpleo) {
        this.tipoEmpleo = tipoEmpleo;
    }
    
    @Column(name="folios")
    public Integer getFolios() {
        return this.folios;
    }
    
    public void setFolios(Integer folios) {
        this.folios = folios;
    }
    
    @Column(name="acta", length=45)
    public String getActa() {
        return this.acta;
    }
    
    public void setActa(String acta) {
        this.acta = acta;
    }
    
    @Column(name="referencia_pago1", length=20)
    public String getReferenciaPago1() {
        return this.referenciaPago1;
    }
    
    public void setReferenciaPago1(String referenciaPago1) {
        this.referenciaPago1 = referenciaPago1;
    }
    
    @Column(name="referencia_pago2", length=20)
    public String getReferenciaPago2() {
        return this.referenciaPago2;
    }
    
    public void setReferenciaPago2(String referenciaPago2) {
        this.referenciaPago2 = referenciaPago2;
    }
    
    @Column(name="consejero")
    public String getConsejero() {
        return this.consejero;
    }
    
    public void setConsejero(String consejero) {
        this.consejero = consejero;
    }
    
    @Column(name="empresa", length=80)
    public String getEmpresa() {
        return this.empresa;
    }
    
    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    
    @Column(name="telefono_empresa", length=50)
    public String getTelefonoEmpresa() {
        return telefonoEmpresa;
    }

    public void setTelefonoEmpresa(String telefonoEmpresa) {
        this.telefonoEmpresa = telefonoEmpresa;
    }
    
    @Column(name="dependiente")
    public Boolean getDependiente() {
        return this.dependiente;
    }
    
    public void setDependiente(Boolean dependiente) {
        this.dependiente = dependiente;
    }

    @Column(name="entero", length=50)
    public String getEntero() {
        return entero;
    }

    public void setEntero(String entero) {
        this.entero = entero;
    }

    @Column(name="entero_por", length=80)
    public String getEnteroPor() {
        return enteroPor;
    }

    public void setEnteroPor(String enteroPor) {
        this.enteroPor = enteroPor;
    }
    
    @Column(name="calificacion")
    public Integer getCalificacion() {
        return this.calificacion;
    }
    
    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }
    
    @Column(name="direccion_empresa", length=200)
    public String getDireccionEmpresa() {
        return this.direccionEmpresa;
    }
    
    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<SolicitudFormato> getSolicitudFormatos() {
        return this.solicitudFormatos;
    }
    
    public void setSolicitudFormatos(Set solicitudFormatos) {
        this.solicitudFormatos = solicitudFormatos;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<Inadmision> getInadmisions() {
        return this.inadmisions;
    }
    
    public void setInadmisions(Set inadmisions) {
        this.inadmisions = inadmisions;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<Observacion> getObservacions() {
        return this.observacions;
    }
    
    public void setObservacions(Set observacions) {
        this.observacions = observacions;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<Envio> getEnvios() {
        return this.envios;
    }
    
    public void setEnvios(Set envios) {
        this.envios = envios;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<NotificacionSolicitud> getNotificacionSolicituds() {
        return this.notificacionSolicituds;
    }
    
    public void setNotificacionSolicituds(Set notificacionSolicituds) {
        this.notificacionSolicituds = notificacionSolicituds;
    }
    
    @OneToMany(fetch=FetchType.EAGER, mappedBy="solicitud")
    public Set<ReferenciaPersonal> getReferenciaPersonals() {
        return this.referenciaPersonals;
    }
    
    public void setReferenciaPersonals(Set referenciaPersonals) {
        this.referenciaPersonals = referenciaPersonals;
    }
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<Resolucion> getResolucions() {
        return this.resolucions;
    }
    
    public void setResolucions(Set resolucions) {
        this.resolucions = resolucions;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<LogSolicitud> getLogSolicituds() {
        return this.logSolicituds;
    }
    
    public void setLogSolicituds(Set logSolicituds) {
        this.logSolicituds = logSolicituds;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="solicitud")
    public Set<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(Set<Pago> pagos) {
        this.pagos = pagos;
    }



    

}
