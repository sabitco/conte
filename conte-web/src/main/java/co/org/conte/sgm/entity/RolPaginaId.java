package co.org.conte.sgm.entity;
// Generated 12/10/2012 02:34:21 PM by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RolPaginaId generated by hbm2java
 */
@Embeddable
public class RolPaginaId  implements java.io.Serializable {


     private int CRol;
     private int CPagina;

    public RolPaginaId() {
    }

    public RolPaginaId(int CRol, int CPagina) {
       this.CRol = CRol;
       this.CPagina = CPagina;
    }
   

    @Column(name="c_rol", nullable=false)
    public int getCRol() {
        return this.CRol;
    }
    
    public void setCRol(int CRol) {
        this.CRol = CRol;
    }

    @Column(name="c_pagina", nullable=false)
    public int getCPagina() {
        return this.CPagina;
    }
    
    public void setCPagina(int CPagina) {
        this.CPagina = CPagina;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof RolPaginaId) ) return false;
		 RolPaginaId castOther = ( RolPaginaId ) other; 
         
		 return (this.getCRol()==castOther.getCRol())
 && (this.getCPagina()==castOther.getCPagina());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getCRol();
         result = 37 * result + this.getCPagina();
         return result;
   }   


}


