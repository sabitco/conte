package com.quasarbi.JavaDelegate;

import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionDucumentosDuplicados implements JavaDelegate{

	private GeneracionBase generador;
	private ConexionBase conexion = new ConexionBase();
	private Conexion conn = new Conexion();
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco, numeroRadicacion;
	private String resolucion, matricula, documento, radicado;
	private Date fechaResolucion;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		
		documento = (String)arg0.getVariable("qswfgd_numerodocumento");
		
		fechaResolucion = conn.getFechaResolucionByDocumento(documento);
		matricula = String.valueOf(conn.getMatricula(documento));
		resolucion = String.valueOf(conn.getResolucionByDocumento(documento));
		radicado = conn.getRadicado(documento).split(",")[0];
		String generadoPor = (String)arg0.getVariable("qswfgd_generadoEn");
		
		if((generadoPor).equalsIgnoreCase("CONTE")){
			generarAnexo27Sec1DupConte(arg0);
		} else {
			generarAnexo27Sec1DupMinminas(arg0);
		}
	}
	
	
	
	
	
	private void generarAnexo27Sec1DupConte(DelegateExecution arg0){ 
		
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador.parametros.put("nombreSolicitante", ((String)arg0.getVariable("qswfgd_nombres")).toUpperCase());
		this.generador.parametros.put("apellidoSolicitante", ((String)arg0.getVariable("qswfgd_apellidos")).toUpperCase());
		this.generador.parametros.put("numeroCedula", documento);
		this.generador.parametros.put("ciudadCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("dptoCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("fechaMatricula", new Date());
		this.generador.parametros.put("numeroRadicacion", radicado);
		this.generador.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador.parametros.put("fechaExpedicion", new Date());
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("anexo27Sec1DupConte");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("anexo27Sec1DupConte.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);
		
	}
	
	private void generarAnexo27Sec1DupMinminas(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));


		this.generador.parametros.put("nombreSolicitante", ((String)arg0.getVariable("qswfgd_nombres")).toUpperCase());
		this.generador.parametros.put("apellidoSolicitante", ((String)arg0.getVariable("qswfgd_apellidos")).toUpperCase());
		this.generador.parametros.put("numeroCedula", documento);
		this.generador.parametros.put("ciudadCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("dptoCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("numeroRadicacion", radicado);
		this.generador.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador.parametros.put("fechaExpedicion", new Date());
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("anexo27Sec1DupMinminas");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("anexo27Sec1DupMinminas.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);				
	}
	
	private void subirArhicvo(DelegateExecution arg0){
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();	
	}
}
