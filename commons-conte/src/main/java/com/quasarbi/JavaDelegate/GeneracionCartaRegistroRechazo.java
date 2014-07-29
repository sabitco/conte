package com.quasarbi.JavaDelegate;

import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionCartaRegistroRechazo  implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco;
	private Expression numeroRadicado;
	private Expression numeroResolucionRechazo;
	private Expression nombreCompleto;
	private Expression direccion;
	private Expression telefono;
	private Expression ciudad;
	private Expression fechaEnvio;
	private Expression fechaResolucionRechazo;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.setNombreFormato("cartaRegistroRechazo");
		this.generador.setConsecutivo((String)this.numeroResolucionRechazo.getValue(arg0));
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("numeroRadicado", (String)this.numeroRadicado.getValue(arg0));
		this.generador.parametros.put("numeroResolucionRechazo", (String)numeroResolucionRechazo.getValue(arg0));
		this.generador.parametros.put("nombreCompleto", ((String)nombreCompleto.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("direccion", ((String)direccion.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("telefono", (String)telefono.getValue(arg0));
		this.generador.parametros.put("ciudad", (String)ciudad.getValue(arg0));
		this.generador.parametros.put("departamento", 
				((String)ciudad.getValue(arg0)).equals((String) arg0.getVariable("qswfr_departamento")) 
				? " " : (String) arg0.getVariable("qswfr_departamento"));		
		this.generador.parametros.put("fechaEnvio", (Date)fechaEnvio.getValue(arg0));
		this.generador.parametros.put("fechaResolucionRechazo", (Date)fechaResolucionRechazo.getValue(arg0));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("CartaRegistroRechazo.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}

}
