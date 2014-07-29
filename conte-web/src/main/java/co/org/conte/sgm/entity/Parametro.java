package co.org.conte.sgm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Andrés Mise Olivera
 */
@Entity
@Table(name="parametro")
public class Parametro extends BaseEntity{
    
    @Id     
    @Column(name="codigo", unique=true, nullable=false)
    private Integer codigo;
    
    @Column(name="nombre", length=50)
    private String parametro;
    
    @Column(name="valor", length=50)
    private String valor;
    
    @Column(name="descripcion", length=255)
    private String descripcion;

    public Parametro() {
    }

    public Parametro(Integer codigo, String parametro, String valor, String descripcion) {
        this.codigo = codigo;
        this.parametro = parametro;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}