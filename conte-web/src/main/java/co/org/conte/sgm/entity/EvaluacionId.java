package co.org.conte.sgm.entity;
// Generated 26/02/2013 01:07:45 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EvaluacionId generated by hbm2java
 */
@Embeddable
public class EvaluacionId  implements java.io.Serializable {


     private long solicitudId;
     private int activityId;
     private int itemId;

    public EvaluacionId() {
    }

    public EvaluacionId(long solicitudId, int activityId, int itemId) {
       this.solicitudId = solicitudId;
       this.activityId = activityId;
       this.itemId = itemId;
    }
   

    @Column(name="solicitud_id", nullable=false)
    public long getSolicitudId() {
        return this.solicitudId;
    }
    
    public void setSolicitudId(long solicitudId) {
        this.solicitudId = solicitudId;
    }

    @Column(name="activity_id", nullable=false)
    public int getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    @Column(name="item_id", nullable=false)
    public int getItemId() {
        return this.itemId;
    }
    
    public void setItemId(int itemId) {
        this.itemId = itemId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof EvaluacionId) ) return false;
		 EvaluacionId castOther = ( EvaluacionId ) other; 
         
		 return (this.getSolicitudId()==castOther.getSolicitudId())
 && (this.getActivityId()==castOther.getActivityId())
 && (this.getItemId()==castOther.getItemId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getSolicitudId();
         result = 37 * result + this.getActivityId();
         result = 37 * result + this.getItemId();
         return result;
   }   


}


