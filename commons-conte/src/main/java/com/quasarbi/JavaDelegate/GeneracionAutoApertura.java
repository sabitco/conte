package com.quasarbi.JavaDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionAutoApertura implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat formato2 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	private Expression consejeroPonente;
	private Expression direccionPonente;
	private Expression nombrePropietario;
	private Expression fechaPeticion;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.setNombreFormato("AutoApertura");
		this.generador.setConsecutivo("0");
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("expediente", (String) arg0.getVariable("expediente"));
		this.generador.parametros.put("consejeroPonente", ((String) consejeroPonente.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("nombreDisciplinado", ((String) arg0.getVariable("nombredisciplinado")).toUpperCase());
		this.generador.parametros.put("direccionPonente", ((String) direccionPonente.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("nombrePropietario", ((String) nombrePropietario.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("fechaPeticion", formato.format((Date) fechaPeticion.getValue(arg0)));
		this.generador.parametros.put("fechaComite", formato.format((Date)arg0.getVariable("fechaasignacionconsejero")));
		this.generador.parametros.put("fechaHoraCitacion", formato2.format((Date)arg0.getVariable("fechahoracitaciontecnico")));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("AutoApertura.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String) arg0.getVariable("nombrecarpeta"));
		this.conexion.subeArchivo();
	}
	
}
