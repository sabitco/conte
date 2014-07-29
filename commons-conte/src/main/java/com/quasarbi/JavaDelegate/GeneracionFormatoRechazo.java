package com.quasarbi.JavaDelegate;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.delegate.*;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionFormatoRechazo implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private Conexion conn;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression folderPath;
	private Expression numeroRadicado;
	private Expression fechaRadicacion;
	private Expression nombreSolicitante;
	private Expression documentoSolicitante;
	private Expression documentosAnexos;
	private String resolucionRechazo;
	private Date fechaResolucion;
	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.conn = new Conexion();
		this.conn.getDataSource();
		String tmp = this.conn.obtieneResolucionRechazo();
		String[] tmpA = tmp.split(",");
		this.resolucionRechazo = tmpA[0];
		this.fechaResolucion = formato.parse(tmpA[1]);
		this.generador.setNombreFormato("formatoRechazo");
		this.generador.setConsecutivo(this.resolucionRechazo);
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("radicado", (String)this.numeroRadicado.getValue(arg0));
		this.generador.parametros.put("resolucionRechazo", this.resolucionRechazo);
		this.generador.parametros.put("fechaRadicacion", (Date)this.fechaRadicacion.getValue(arg0));
		this.generador.parametros.put("nombreSolicitante", ((String)this.nombreSolicitante.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("documentoSolicitante", (String)this.documentoSolicitante.getValue(arg0));
		this.generador.parametros.put("documentosAnexos", new ArrayList<String>(Arrays.asList(((String)this.documentosAnexos.getValue(arg0)).split(","))));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("formatoRechazo.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.folderPath.getValue(arg0));
		this.conexion.subeArchivo();
		arg0.setVariable("resolucionRechazo", this.resolucionRechazo);
		arg0.setVariable("fechaResolucion", this.fechaResolucion);
	}
	
}
