package com.quasarbi.JavaDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionFormatoInadmitidos implements JavaDelegate{

	private GeneracionBase generador;
	private ConexionBase conexion;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco;
	private Expression observaciones;
	private Expression requisitosFaltantes;
	private Expression nombres;
	private Expression apellidos;
	private Expression documento;
	private Expression informoVia;
	private Expression dependencia;
	private Expression conceptoConsejero;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		
		List<String> observaciones = new ArrayList<String>();
		try {
			List<String> observacionestmp = new ArrayList<String>(Arrays.asList(((String)this.observaciones.getValue(arg0)).split(",")));
			for(String val : observacionestmp){
				observaciones.add(val);
			}
			observaciones.add(((String) arg0.getVariable("qswfr_conceptodocumentacion")) == null 
					? "" : (String) arg0.getVariable("qswfr_conceptodocumentacion"));
		} catch (NullPointerException ex ) { 
			ex.printStackTrace();
		}
		
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		
		this.generador.setNombreFormato("formatoInadmitidos");
		this.generador.setConsecutivo("");
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("observaciones", observaciones);
		this.generador.parametros.put("requisitosFaltantes", new ArrayList<String>(Arrays.asList(((String)requisitosFaltantes.getValue(arg0)).split(","))));
		this.generador.parametros.put("nombres", ((String)this.nombres.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("apellidos", ((String)this.apellidos.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("documento", (String)this.documento.getValue(arg0));
		this.generador.parametros.put("informoVia", new ArrayList<String>(Arrays.asList(((String)this.informoVia.getValue(arg0)).split(","))));
		this.generador.parametros.put("dependencia", (String)this.dependencia.getValue(arg0));
		this.generador.parametros.put("conceptoConsejero", (String)this.conceptoConsejero.getValue(arg0));
		this.generador.parametros.put("radicado", (String) arg0.getVariable("qswfr_numeroRadicado"));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("formatoInadmitidos.jasper"));
		this.generador.setExtension("pdf");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
		
		String generar = ((String) arg0.getVariable("generarCartaInadmitidos")) == null
				? "" : (String) arg0.getVariable("generarCartaInadmitidos");
		if(generar.equals("Si")){
			generarCartaInadmitidos(arg0);
		}
	}	
	
	private void generarCartaInadmitidos(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.setNombreFormato("cartaInadmitidos");
		this.generador.setConsecutivo("");
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("numeroCDO", "");
		this.generador.parametros.put("fechaNotificacion", new Date());
		this.generador.parametros.put("nombreAsociacion", (String) arg0.getVariable("qswfr_asociacion"));
		this.generador.parametros.put("NombreSolicitante", (String)this.nombres.getValue(arg0));
		this.generador.parametros.put("ApellidoSolicitante", (String)this.apellidos.getValue(arg0));
		this.generador.parametros.put("direccionResidencia", (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador.parametros.put("telefonoResidencia", (String) arg0.getVariable("qswfr_telefono"));
		this.generador.parametros.put("ciudadResidencia", (String) arg0.getVariable("qswfr_ciudad"));
		this.generador.parametros.put("departamentoResidencia",
				((String)arg0.getVariable("qswfr_ciudad")).equals((String) arg0.getVariable("qswfr_departamento")) 
				? " " : (String) arg0.getVariable("qswfr_departamento"));
		this.generador.parametros.put("numeroRadicado", (String) arg0.getVariable("qswfr_numeroRadicado"));
		this.generador.parametros.put("identificacionInspector", (String) arg0.getVariable("qswfr_identificacionInspector"));
		this.generador.parametros.put("redaccionNotificacion", (String) arg0.getVariable("qswfr_redaccionCartaInadmitidos"));
		
		this.generador.parametros.put("informoVia", new ArrayList<String>(Arrays.asList(((String)this.informoVia.getValue(arg0)).split(","))));
		
		this.generador.parametros.put("conceptoConsejero", (String)this.conceptoConsejero.getValue(arg0));
		this.generador.parametros.put("radicado", (String) arg0.getVariable("qswfr_numeroRadicado"));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("cartaInadmitidos.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
}