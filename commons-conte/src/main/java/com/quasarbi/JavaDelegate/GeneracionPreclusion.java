package com.quasarbi.JavaDelegate;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionPreclusion implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression folderPath;
	private Expression consejeroPonente;
	private Expression nombrePropietario;
	private Expression documentoPropietario;
	private Expression nombreAsociacion;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.setNombreFormato("Preclusion");
		this.generador.setConsecutivo("0");
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("expediente", (String) arg0.getVariable("expediente"));
		this.generador.parametros.put("consejeroPonente", ((String) consejeroPonente.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("nombreDisciplinado", ((String) arg0.getVariable("nombredisciplinado")).toUpperCase());
		this.generador.parametros.put("nombrePropietario", ((String) nombrePropietario.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("documentoPropietario", (String) documentoPropietario.getValue(arg0));
		this.generador.parametros.put("nombreAsociacion", ((String) nombreAsociacion.getValue(arg0)).toUpperCase());
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("Preclusion.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.folderPath.getValue(arg0));
		this.conexion.subeArchivo();
	}

}
