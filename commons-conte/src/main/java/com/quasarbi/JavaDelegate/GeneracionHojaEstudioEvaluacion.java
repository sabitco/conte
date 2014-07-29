package com.quasarbi.JavaDelegate;

import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GeneracionHojaEstudioEvaluacion implements JavaDelegate {

	private GeneracionBase generador;
	private ConexionBase conexion;
	private Conexion conn;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	
	private Expression nombres;
	private Expression apellidos;
	private Expression expediente;
	private Expression documento;
	private Expression solicitud_id;
	
	
	private Expression folderPath;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.conn = new Conexion();
		this.conn.getDataSource();
		this.generador.setNombreFormato("HojaEstudioSolicitud");
		
		this.generador.setConsecutivo((String)this.expediente.getValue(arg0));
		
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.parametros.put("nombres", ((String)this.nombres.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("apellidos", ((String) this.apellidos.getValue(arg0)).toUpperCase());
		this.generador.parametros.put("expediente", (String)this.expediente.getValue(arg0));
		this.generador.parametros.put("documento", (String)this.documento.getValue(arg0));
		this.generador.parametros.put("solicitud_id", Integer.parseInt(this.solicitud_id.getValue(arg0).toString()));
		this.generador.parametros.put("observaciones", (String) arg0.getVariable("qswfr_observacionesCalificacion") == null 
				? "" : (String) arg0.getVariable("qswfr_observacionesCalificacion"));
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("evaluacion.jasper"));
		this.generador.setExtension("pdf");
		this.generador.generaFormatoConConexion();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.folderPath.getValue(arg0));
		this.conexion.subeArchivo();
	}

}
