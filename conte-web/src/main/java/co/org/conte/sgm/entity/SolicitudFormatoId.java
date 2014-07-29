package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SolicitudFormatoId generated by hbm2java
 */
@Embeddable
public class SolicitudFormatoId  implements java.io.Serializable {


     private long CSolicitud;
     private int CFormato;

    public SolicitudFormatoId() {
    }

    public SolicitudFormatoId(long CSolicitud, int CFormato) {
       this.CSolicitud = CSolicitud;
       this.CFormato = CFormato;
    }
   

    @Column(name="c_solicitud", nullable=false)
    public long getCSolicitud() {
        return this.CSolicitud;
    }
    
    public void setCSolicitud(long CSolicitud) {
        this.CSolicitud = CSolicitud;
    }

    @Column(name="c_formato", nullable=false)
    public int getCFormato() {
        return this.CFormato;
    }
    
    public void setCFormato(int CFormato) {
        this.CFormato = CFormato;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SolicitudFormatoId) ) return false;
		 SolicitudFormatoId castOther = ( SolicitudFormatoId ) other; 
         
		 return (this.getCSolicitud()==castOther.getCSolicitud())
 && (this.getCFormato()==castOther.getCFormato());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getCSolicitud();
         result = 37 * result + this.getCFormato();
         return result;
   }   


}

