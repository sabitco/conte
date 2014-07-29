/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name="usuario_contrasenia_historial")
public class UsuarioContraseniaHistorial extends BaseEntity{
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name="codigo", unique=true, nullable=false)
    private long codigo;
    
    @Column(name="contrasenia")
    private String contrasenia;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cod_usuario", nullable=false)
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha", length=19)
    private Date fecha;
    
    public UsuarioContraseniaHistorial() {
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
