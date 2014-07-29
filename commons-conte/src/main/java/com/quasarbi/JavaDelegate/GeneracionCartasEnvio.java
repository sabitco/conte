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

public class GeneracionCartasEnvio implements JavaDelegate{
	
	private GeneracionBase generador;
	private ConexionBase conexion;
	private Conexion conn;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression modelo;
	private Expression directorioAlfresco;
	private String solicitudId, numeroRadicacion, numeroCedula, numeroMatricula, nombreTecnico, apellidoTecnico;
	private Date fechaMatricula;
	private Long resolucion;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		
		String generar = (String) arg0.getVariable("qswfr_notificacionGenerar");
		
		System.out.print("\n\n\nSE VA A GENERAR " + generar + "\n\n\n");
		
		conn = new Conexion();
		numeroRadicacion = (String) arg0.getVariable("qswfr_numeroRadicado");
		numeroCedula = (String) arg0.getVariable("qswfr_numerodocumento");
		numeroMatricula = String.valueOf(conn.getMatricula(numeroCedula));
		nombreTecnico = ((String) arg0.getVariable("qswfr_nombres")).toUpperCase();
		apellidoTecnico = ((String) arg0.getVariable("qswfr_apellidos")).toUpperCase();
		fechaMatricula = (Date) arg0.getVariable("qswfr_fechaMatricula");
				
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();

		if(generar.equals("110 - Nacional")
				&& ((String) arg0.getVariable("qswfr_interponerecursov")).equalsIgnoreCase("NO")){
			generarConstanciaMatricula(arg0);
			generarCartaEnvioMatricula(arg0);
		}
		
	}
	
	private void generarConstanciaMatricula(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo(numeroRadicacion);
		
		this.generador.parametros.put("numeroRadicacion", numeroRadicacion);
		this.generador.parametros.put("cedulaTecnico", numeroCedula);
		this.generador.parametros.put("numeroMatricula", numeroMatricula);
		this.generador.parametros.put("nombreTenico", nombreTecnico);
		this.generador.parametros.put("apellidoTenico", apellidoTecnico);
		this.generador.parametros.put("fechaMatricula", fechaMatricula);

		this.generador.setNombreFormato("constanciaMatricula");	
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("modeloConstanciaMatricula.jasper"));
		
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();				
	}
	
	private void generarCartaEnvioMatricula(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo(numeroRadicacion);
		
		this.generador.parametros.put("numeroCDO", "");
		this.generador.parametros.put("fecha", new Date());
		this.generador.parametros.put("nombreTenico", nombreTecnico);
		this.generador.parametros.put("apellidoTenico", apellidoTecnico);
		this.generador.parametros.put("direccionTenico", ((String) arg0.getVariable("qswfr_direccionResidencia")).toUpperCase());
		this.generador.parametros.put("telefonoTenico", (String) arg0.getVariable("qswfr_telefono"));
		this.generador.parametros.put("ciudadTenico", (String) arg0.getVariable("qswfr_ciudad"));
		this.generador.parametros.put("departamentoTenico", (String) arg0.getVariable("qswfr_departamento"));	
		this.generador.parametros.put("numeroRadicacion", numeroRadicacion);
		this.generador.parametros.put("cedulaTecnico", numeroCedula);
		this.generador.parametros.put("numeroMatricula", numeroMatricula);
		this.generador.parametros.put("fechaMatricula", fechaMatricula);
		
		this.generador.setNombreFormato("modeloCartaEnvioMatricula");	
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("modeloCartaEnvioMatricula.jasper"));
		
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();				
	}

}