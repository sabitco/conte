package co.org.conte.sgm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jam
 */
@Entity
@Table(name="pqrs_descripcion")
public class PqrsDescripcion extends BaseEntity {
    
    
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    @Column(name="codigo", unique=true, nullable=false)
    private int codigo;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_pqrs")
    private Pqrs pqrs;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="c_usuario")
    private Usuario usuario;
    
    @Column(name="descripcion",  nullable=false)
    private String descripcion;
    
    @Column(name="ruta",  nullable=false)    
    private String ruta;

    public PqrsDescripcion() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Pqrs getPqrs() {
        return pqrs;
    }

    public void setPqrs(Pqrs pqrs) {
        this.pqrs = pqrs;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }   
}