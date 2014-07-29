package co.org.conte.sgm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="respuesta")
public class Respuesta  implements java.io.Serializable {

     private int codigo;
     private Pregunta pregunta;
     private Opcion opcion;
     private String respuesta;

    public Respuesta() {
    }

	
    public Respuesta(int codigo, Pregunta pregunta) {
        this.codigo = codigo;
        this.pregunta = pregunta;
    }
    public Respuesta(int codigo, Pregunta pregunta, Opcion opcion, String respuesta) {
       this.codigo = codigo;
       this.pregunta = pregunta;
       this.opcion = opcion;
       this.respuesta = respuesta;
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
    @JoinColumn(name="c_pregunta", nullable=false)
    public Pregunta getPregunta() {
        return this.pregunta;
    }
    
    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="c_opcion")
    public Opcion getOpcion() {
        return this.opcion;
    }
    
    public void setOpcion(Opcion opcion) {
        this.opcion = opcion;
    }
    
    @Column(name="respuesta")
    public String getRespuesta() {
        return this.respuesta;
    }
    
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}