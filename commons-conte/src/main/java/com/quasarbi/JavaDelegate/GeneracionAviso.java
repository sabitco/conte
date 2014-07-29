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

public class GeneracionAviso implements JavaDelegate {
	
	private GeneracionBase generador;
	private ConexionBase conexion = new ConexionBase();
	private Conexion conn = new Conexion();
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco, numeroRadicacion, solicitud, avisoGenerar;
	private String resolucion, solicitudId;
	private Date fechaResolucion;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		solicitudId  =  (String) this.solicitud.getValue(arg0);
		resolucion = String.valueOf(conn.getResolucion(solicitudId));
		fechaResolucion = conn.getFechaResolucion(solicitudId);
		if(((String) avisoGenerar.getValue(arg0)).equals("Aviso Ampliacion")){
			generarAvisoAmpliacion(arg0);
			
		} 
		if(((String) avisoGenerar.getValue(arg0)).equals("Aviso Recurso Aprobado")){
			generarAvisoRecursoAprobado(arg0);
			
		}
		if(((String) avisoGenerar.getValue(arg0)).equals("Aviso Primera Vez")){
			generarAvisoPrimeraVez(arg0);
			
		}
		if(((String) avisoGenerar.getValue(arg0)).equals("Aviso Recurso Negado")){
			generarAvisoRecursoNegado(arg0);			
		}
		if(((String) avisoGenerar.getValue(arg0)).equals("Aviso Recurso Ampliacion")){
			generarAvisoRecursoAmpliacion(arg0);			
		}
	}
	
	private void generarAvisoAmpliacion(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroRadicado", (String) numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("nombreTecnico", ((String)arg0.getVariable("qswfr_nombres")).toUpperCase());
		this.generador.parametros.put("cedulaTecnico", (String)arg0.getVariable("qswfr_numerodocumento"));
		this.generador.parametros.put("fechaDada", new Date());
		this.generador.parametros.put("numeroActa", (String) arg0.getVariable("qswfr_numeroActa"));
		this.generador.parametros.put("fecha2", new Date());
		this.generador.parametros.put("fechaDesfijacion", new Date());
		this.generador.parametros.put("fechaVencimiento", new Date());
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("AvisoAmpliacion");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("avisoAmpliacion.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);				
	}
	
	private void generarAvisoPrimeraVez(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroRadicado", (String) numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("nombreTecnico", ((String)arg0.getVariable("qswfr_nombres")).toUpperCase());
		this.generador.parametros.put("cedulaTecnico", (String)arg0.getVariable("qswfr_numerodocumento"));
		this.generador.parametros.put("fechaDada", new Date());
		this.generador.parametros.put("numeroActa", (String) arg0.getVariable("qswfr_numeroActa"));
		this.generador.parametros.put("fecha2", new Date());
		this.generador.parametros.put("fechaDesfijacion", new Date());
		this.generador.parametros.put("fechaVencimiento", new Date());
		this.generador.parametros.put("clases", conn.generarTextoClases(solicitudId));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("avisoPrimeraVez");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("avisoPrimeraVez.jasper"));
		this.generador.generaFormato();
		
		subirArhicvo(arg0);
	}
	
	private void generarAvisoRecursoAmpliacion(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroRadicado", (String) numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("nombreTecnico", ((String)arg0.getVariable("qswfr_nombres")).toUpperCase());
		this.generador.parametros.put("cedulaTecnico", (String)arg0.getVariable("qswfr_numerodocumento"));
		this.generador.parametros.put("fechaDada", new Date());
		this.generador.parametros.put("numeroActa", (String) arg0.getVariable("qswfr_numeroActa"));
		this.generador.parametros.put("fecha2", new Date());
		this.generador.parametros.put("fechaDesfijacion", new Date());
		this.generador.parametros.put("fechaVencimiento", new Date());
		this.generador.parametros.put("clases", conn.generarTextoClases(solicitudId));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("avisoRecursoAmpliacion");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("avisoRecursoAmpliacion.jasper"));
		this.generador.generaFormato();
		
		subirArhicvo(arg0);
	}
	
	private void generarAvisoRecursoAprobado(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroRadicado", (String) numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("nombreTecnico", ((String)arg0.getVariable("qswfr_nombres")).toUpperCase());
		this.generador.parametros.put("cedulaTecnico", (String)arg0.getVariable("qswfr_numerodocumento"));
		this.generador.parametros.put("fechaDada", new Date());
		this.generador.parametros.put("numeroActa", (String) arg0.getVariable("qswfr_numeroActa"));
		this.generador.parametros.put("fecha2", new Date());
		this.generador.parametros.put("fechaDesfijacion", new Date());
		this.generador.parametros.put("fechaVencimiento", new Date());
		this.generador.parametros.put("clases", conn.generarTextoClases(solicitudId));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("avisoRecursoAprobado");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("avisoRecursoAprobado.jasper"));
		this.generador.generaFormato();
		
		subirArhicvo(arg0);
	}
	
	
	private void generarAvisoRecursoNegado(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroRadicado", (String) numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("nombreTecnico", ((String)arg0.getVariable("qswfr_nombres")).toUpperCase());
		this.generador.parametros.put("cedulaTecnico", (String)arg0.getVariable("qswfr_numerodocumento"));
		this.generador.parametros.put("fechaDada", new Date());
		this.generador.parametros.put("numeroActa", (String) arg0.getVariable("qswfr_numeroActa"));
		this.generador.parametros.put("fecha2", new Date());
		this.generador.parametros.put("fechaDesfijacion", new Date());
		this.generador.parametros.put("fechaVencimiento", new Date());
		this.generador.parametros.put("clases", conn.generarTextoClases(solicitudId));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("avisoRecursoNegado");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("avisoRecursoNegado.jasper"));
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
