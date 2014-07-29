/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.entity;

import java.io.Serializable;

/**
 *
 * @author jam
 */

public class RelacionesBDACT implements Serializable{

    private static final long serialVersionUID = 1L;
    private Integer codigo;
    private String label;
    private String campoDb;
    private String campoAct;
    private String tipo;
    private String llaveTabla;
    private String campoSolicitud;
    private String tipoRelacion;
    private String tabla;
    private String tablaAct;
    private Object valor;
    private Object nuevoValor;
    
    public RelacionesBDACT() {
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCampoDb() {
        return campoDb;
    }

    public void setCampoDb(String campoDb) {
        this.campoDb = campoDb;
    }

    public String getCampoAct() {
        return campoAct;
    }

    public void setCampoAct(String campoAct) {
        this.campoAct = campoAct;
    }

    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLlaveTabla() {
        return llaveTabla;
    }

    public void setLlaveTabla(String llaveTabla) {
        this.llaveTabla = llaveTabla;
    }

    public String getCampoSolicitud() {
        return campoSolicitud;
    }

    public void setCampoSolicitud(String campoSolicitud) {
        this.campoSolicitud = campoSolicitud;
    }

    public String getTipoRelacion() {
        return tipoRelacion;
    }

    public void setTipoRelacion(String tipoRelacion) {
        this.tipoRelacion = tipoRelacion;
    }

    /**
     * @return the tabla
     */
    public String getTabla() {
        return tabla;
    }

    /**
     * @param tabla the tabla to set
     */
    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    /**
     * @return the valor
     */
    public Object getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }

    /**
     * @return the nuevoValor
     */
    public Object getNuevoValor() {
        return nuevoValor;
    }

    /**
     * @param nuevoValor the nuevoValor to set
     */
    public void setNuevoValor(Object nuevoValor) {
        this.nuevoValor = nuevoValor;
    }

    /**
     * @return the tablaAct
     */
    public String getTablaAct() {
        return tablaAct;
    }

    /**
     * @param tablaAct the tablaAct to set
     */
    public void setTablaAct(String tablaAct) {
        this.tablaAct = tablaAct;
    }
}
