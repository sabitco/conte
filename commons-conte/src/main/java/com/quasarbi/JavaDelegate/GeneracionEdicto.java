package com.quasarbi.JavaDelegate;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionEdicto implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco;
	private Expression documento;
	private Expression fechaResolucion;
	private Expression nombres;	
	private Expression radicado;
	SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.setNombreFormato("edicto");
		this.generador.setConsecutivo("1");
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("documento", (String)documento.getValue(arg0));
		
		Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis((new Date()).getTime());
        cal.add(Calendar.DATE, 15);
        Date fechaDesfija = new Date(cal.getTimeInMillis());
		
		this.generador.parametros.put("fechaDesfija", formato.format(fechaDesfija));
		this.generador.parametros.put("fechaResolucion", (Date)this.fechaResolucion.getValue(arg0));
		this.generador.parametros.put("nombres", ((String)this.nombres.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("numeroResolucion", "1");
		this.generador.parametros.put("radicado", (String)this.radicado.getValue(arg0));
		this.generador.parametros.put("textoResolucion", "");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("edicto.jasper"));
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
}