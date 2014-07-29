package com.quasarbi.JavaDelegate;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionNotificacionPersonal  implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private Conexion conn = new Conexion();
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco;
	private Expression numeroRadicado;
	private Expression resolucionRechazo;
	private Expression nombreSolicitante;
	private Expression documentoSolicitante;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		String documento = (String)this.documentoSolicitante.getValue(arg0);
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.setNombreFormato("notificacionPersonal");
		this.generador.setConsecutivo((String)this.resolucionRechazo.getValue(arg0));
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("radicado", (String)this.numeroRadicado.getValue(arg0));
		this.generador.parametros.put("resolucionRechazo", (String)this.resolucionRechazo.getValue(arg0));
		this.generador.parametros.put("nombreSolicitante", ((String)this.nombreSolicitante.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("documentoSolicitante", documento);
		this.generador.parametros.put("documentoSolicitante", conn.getFechaResolucionByDocumento(documento));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("notificacionPersonal.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
	
}
