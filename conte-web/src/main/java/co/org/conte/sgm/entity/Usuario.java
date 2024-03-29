package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Usuario generated by hbm2java
 */
@Entity
@Table(name="usuario"
    , uniqueConstraints = {@UniqueConstraint(columnNames="email")} 
)
public class Usuario  extends BaseEntity{

     private long codigo;
     private Perfil perfil;
     private Ciudad ciudad;
     private String tipoDocumento;
     private String documento;
     private String estado;
     private String contrasena;
     private String nombres;
     private String primerApellido;
     private String segundoApellido;
     private String direccion;
     private String telefono;
     private String celular;
     private String email;
     private Integer intentos;
     private Boolean notificacion;
     private Date ultimoAcceso;
     private Date modificacionContrasenia;
     private Set solicituds = new HashSet(0);
     private Set pqrses = new HashSet(0);
     private Set logUsuarios = new HashSet(0);
     private Set solicitudFormatos = new HashSet(0);
     private Set<UsuarioContraseniaHistorial> contrasenias = new HashSet(0);

    public Usuario() {
    }

	
    public Usuario(long codigo, Perfil perfil, String tipoDocumento, String documento, String estado, String contrasena) {
        this.codigo = codigo;
        this.perfil = perfil;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.estado = estado;
        this.contrasena = contrasena;
    }
    public Usuario(long codigo, Perfil perfil, Ciudad ciudad, String tipoDocumento, String documento, String usuario, String contrasena, String nombres, String primerApellido, String direccion, String telefono, String celular, String email, Date ultimoAcceso, Set solicituds, Set pqrses, Set logUsuarios, Set solicitudFormatos) {
       this.codigo = codigo;
       this.perfil = perfil;
       this.ciudad = ciudad;
       this.tipoDocumento = tipoDocumento;
       this.documento = documento;
       this.estado = usuario;
       this.contrasena = contrasena;
       this.nombres = nombres;
       this.primerApellido = primerApellido;
       this.direccion = direccion;
       this.telefono = telefono;
       this.celular = celular;
       this.email = email;
       this.ultimoAcceso = ultimoAcceso;
       this.solicituds = solicituds;
       this.pqrses = pqrses;
       this.logUsuarios = logUsuarios;
       this.solicitudFormatos = solicitudFormatos;
    }
   
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name="codigo", unique=true, nullable=false)
    public long getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="perfil", nullable=false)
    public Perfil getPerfil() {
        return this.perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_ciudad")
    public Ciudad getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
    
    @Column(name="tipo_documento", nullable=false, length=3)
    public String getTipoDocumento() {
        return this.tipoDocumento;
    }

    @Column(name="notificacion",  length=1)
    public Boolean isNotificacion() {
        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }
    
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    @Column(name="documento", nullable=false, length=20)
    public String getDocumento() {
        return this.documento;
    }
    
    public void setDocumento(String documento) {
        this.documento = documento;
    }
    
    @Column(name="estado", unique=false, nullable=false)
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    @Column(name="contrasena", nullable=false, length=200)
    public String getContrasena() {
        return this.contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    @Column(name="nombres", length=60)
    public String getNombres() {
        return this.nombres;
    }
    
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    @Column(name="primer_apellido", length=60)
    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    @Column(name="segundo_apellido", length=60)
    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
        
    @Column(name="direccion", length=60)
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
    
    @Column(name="email", length=45)
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="intentos", length=45)
    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ultimo_acceso", length=19)
    public Date getUltimoAcceso() {
        return this.ultimoAcceso;
    }
    
    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modificacion_contrasenia", length=19)
    public Date getModificacionContrasenia() {
        return modificacionContrasenia;
    }

    public void setModificacionContrasenia(Date modificacionContrasenia) {
        this.modificacionContrasenia = modificacionContrasenia;
    }
    
    
    
//    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
    public Set<Solicitud> getSolicituds() {
        return this.solicituds;
    }
    
    public void setSolicituds(Set solicituds) {
        this.solicituds = solicituds;
    }

    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
    public Set<Pqrs> getPqrses() {
        return this.pqrses;
    }
    
    public void setPqrses(Set pqrses) {
        this.pqrses = pqrses;
    }
    
    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
    public Set<LogUsuario> getLogUsuarios() {
        return this.logUsuarios;
    }
    
    public void setLogUsuarios(Set logUsuarios) {
        this.logUsuarios = logUsuarios;
    }
    
    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
    public Set<SolicitudFormato> getSolicitudFormatos() {
        return this.solicitudFormatos;
    }
    
    public void setSolicitudFormatos(Set solicitudFormatos) {
        this.solicitudFormatos = solicitudFormatos;
    }
    
    //@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
    @OneToMany(fetch=FetchType.LAZY, mappedBy="usuario")
    public Set<UsuarioContraseniaHistorial> getContrasenias() {
        return this.contrasenias;
    }
    
    public void setContrasenias(Set<UsuarioContraseniaHistorial> contrasenias) {
        this.contrasenias = contrasenias;
    }

    @Override
    public int hashCode() {
        Long hash = new Long(codigo);        
        return hash.intValue()*17;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }
}