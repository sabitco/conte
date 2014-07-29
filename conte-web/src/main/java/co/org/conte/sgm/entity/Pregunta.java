package co.org.conte.sgm.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="pregunta")
public class Pregunta  implements java.io.Serializable {


     private int codigo;
     private String descripcion;
     private Boolean abierta;
     private Boolean estado;
     private Set opcions = new HashSet(0);
     private Set respuestas = new HashSet(0);

    public Pregunta() {
    }

	
    public Pregunta(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    public Pregunta(int codigo, String descripcion, Boolean abierta, Boolean estado, Set opcions) {
       this.codigo = codigo;
       this.descripcion = descripcion;
       this.abierta = abierta;
       this.estado = estado;
       this.opcions = opcions;
    }
   
    @Id    
    @Column(name="codigo", unique=true, nullable=false)
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    @Column(name="descripcion", nullable=false)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Column(name="abierta")
    public Boolean getAbierta() {
        return this.abierta;
    }
    
    public void setAbierta(Boolean abierta) {
        this.abierta = abierta;
    }
    
    @Column(name="estado")
    public Boolean getEstado() {
        return this.estado;
    }
    
    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    @ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="pregunta_opcion", joinColumns = { 
        @JoinColumn(name="c_pregunta", nullable=false, updatable=false) }, inverseJoinColumns = { 
        @JoinColumn(name="c_opcion", nullable=false, updatable=false) })
    public Set<Opcion> getOpcions() {
        return this.opcions;
    }
    
    public void setOpcions(Set<Opcion> opcions) {
        this.opcions = opcions;
    }
    
    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="opcion")
    public Set<Respuesta> getRespuestas() {
        return this.respuestas;
    }
    
    public void setRespuestas(Set respuestas) {
        this.respuestas = respuestas;
    }
}