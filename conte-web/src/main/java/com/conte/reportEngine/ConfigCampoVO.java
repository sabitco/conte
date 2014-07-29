package com.conte.reportEngine;

import com.conte.reportEngine.config.Constantes;
import com.conte.reportEngine.config.Constantes.Alineacion;
import com.conte.reportEngine.formatter.CampoFormatter;
import com.conte.reportEngine.formatter.DefaultCampoFormatter;
import java.io.Serializable;

/**
 * Almacena configuracion de columnas (campos) para el motor de reportes
 */
public class ConfigCampoVO implements Serializable {

  private static final long serialVersionUID = 1462708909305508892L;
  private String campo;
  private String titulo;
  private Integer tamano;
  private Constantes.Alineacion alineacion;
  private String formato;
  private CampoFormatter campoFormatter;
  private int precision=0;

  public ConfigCampoVO() {
    this(new DefaultCampoFormatter());
  }

  public ConfigCampoVO(CampoFormatter campoFormatter) {
    this.campoFormatter = campoFormatter;
    this.alineacion = Constantes.Alineacion.CENTRO;
  }

  public String getCampo() {
    return campo;
  }

  public void setCampo(String campo) {
    this.campo = campo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Integer getTamano() {
    return tamano;
  }

  public void setTamano(Integer tamano) {
    this.tamano = tamano;
  }

  public Constantes.Alineacion getAlineacion() {
		if (alineacion == null) {
      return Alineacion.NINGUNA;
    }
    return alineacion;
	}
	public void setAlineacion(Constantes.Alineacion alineacion) {
		this.alineacion = alineacion;
	}

  public String getFormato() {
    return formato;
  }

  public void setFormato(String formato) {
    this.formato = formato;
  }

  public CampoFormatter getCampoFormatter() {
    return campoFormatter;
  }

  public void setCampoFormatter(CampoFormatter campoFormatter) {
    this.campoFormatter = campoFormatter;
  }
  
  public void setPrecision(int precision) {
      this.precision=precision;
  }

  public int getPrecision() {
      return precision;
  }
          
}
