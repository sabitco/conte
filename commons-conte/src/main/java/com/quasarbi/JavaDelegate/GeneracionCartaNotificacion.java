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
import com.quasarbi.entity.Asociacion;

public class GeneracionCartaNotificacion implements JavaDelegate {

	private GeneracionBase generador, generador2;
	private ConexionBase conexion;
	private Conexion conn;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco, numeroRadicacion, solicitud, numeroGuia, cartaGenerar, nombreSolicitante, apellidoSolicitante, documento,
	siglaAsociacion;
	private Long resolucion;
	private String numeroCedula, prefijo;
	private Date fechaResolucion, fechaMatricula;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		
		
		
		conn = new Conexion();
		
		String solicitudId =  (String) this.solicitud.getValue(arg0);
		numeroCedula = (String)arg0.getVariable("qswfr_numerodocumento");
		fechaResolucion = conn.getFechaResolucion(solicitudId);
		fechaMatricula = conn.getFechaMatricula(numeroCedula);
		prefijo = (String) arg0.getVariable("prefijo");
		
		resolucion = conn.getResolucion(solicitudId);
		if(resolucion<1 || resolucion == null){
			resolucion = (long) conn.getSequence("resolucion");
			conn.actualizarResolucion(solicitudId, resolucion, numeroCedula);
		}
		
		this.generador = new GeneracionBase();
		this.conexion = new ConexionBase();
		this.generador.parametros = new HashMap<String, Object>();
		
		String carta = (String) cartaGenerar.getValue(arg0);
		
		System.out.println("\n\n\n LA CARTA A GENERAR ES "  +  carta + "\n\n\n");
		
		String formatoGenerar = "";
		try{
			formatoGenerar =  (String)arg0.getVariable("qswfr_formatoGenerar2");
		} catch (NullPointerException e) {
			formatoGenerar = "";
		}
	
		formatoGenerar = (formatoGenerar==null) ? "no generar" : formatoGenerar;
		formatoGenerar = (formatoGenerar.isEmpty()) ? "no generar" : formatoGenerar;
				
		if(carta.equals("Asociacion")){
			this.generador.setConsecutivo((String) numeroGuia.getValue(arg0));
			Asociacion asociacion = conn.getAsociacionBySigla((String) this.siglaAsociacion.getValue(arg0));
			conn.actualizarNumeroGuiaTramite((String) numeroRadicacion.getValue(arg0), (String) numeroGuia.getValue(arg0), (String) arg0.getVariable("qswfr_tipoTramite2"));
			this.generador.parametros.put("presidente", asociacion.getPresidente());
			this.generador.parametros.put("sigla", asociacion.getSigla());
			this.generador.parametros.put("direccion", asociacion.getDireccion());
			this.generador.parametros.put("ciudad", asociacion.getCiudad());
			this.generador.parametros.put("departamento", asociacion.getDepartamento());
			this.generador.parametros.put("telefono", asociacion.getTelefono());
			this.generador.parametros.put("numeroGuia",(String) numeroGuia.getValue(arg0));
			this.generador.setNombreFormato("cartaEnvioAsociacion");
			this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("cartaEnvioAsociacion.jasper"));
			generarNotificacionPersonalAsociacion(arg0);
			this.generador.setExtension("docx");
			this.generador.generaFormatoConConexion();
		
			System.out.println("SE VA A VERIFICAR QUE TIPO DE FORMATO GENERO =>" + formatoGenerar);
			if(formatoGenerar.equalsIgnoreCase("Resolucion Primera Vez")
					|| formatoGenerar.equalsIgnoreCase("Resolucion Ampliacion Aprobada")
					|| formatoGenerar.equalsIgnoreCase("Revocatoria")){
				generarNotificacionPersonalAsociacion(arg0);
				System.out.println("EN EL IF QUE GENERA NOTIFICACION =>" + formatoGenerar);
			}
			
			if(formatoGenerar.equalsIgnoreCase("Recurso Aprobado")
					|| formatoGenerar.equalsIgnoreCase("Recurso Negado")
					){
				generarNotificacionRecursoAsociacion(arg0);
				System.out.println("EN EL IF QUE GENERA NOTIFICACION =>" + formatoGenerar);
			}
			this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
			this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
			this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
			this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
			this.conexion.subeArchivo();
		}
				
		if(carta.equals("110 - Nacional")){			
			generarNotificacion110Personal(arg0);
			generarModeloCartaAprobacionSolicitud(arg0);
		} else {
			generarNotificacionPersonalAsociacion(arg0);
		}
		
		
	}
	
	private void generarNotificacionPersonalAsociacion(DelegateExecution arg0){
		System.out.print("\n\n SE GENERA LA CARTA NotificacionPersonalAsociacion\n\n");
		
		generador2 = new GeneracionBase();
		this.generador2.setConsecutivo("");
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.parametros.put("numeroMatricula", String.valueOf(conn.getMatricula((String)arg0.getVariable("qswfr_numerodocumento"))));
		this.generador2.parametros.put("numeroCedula", (String) documento.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String) apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String) nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroRadicacion", (String) numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("fechaResolucion", fechaResolucion);
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("nombreAsociacion", (String)arg0.getVariable("qswfr_asociacion"));
		this.generador2.parametros.put("prefijo",prefijo);
		this.generador2.setExtension("docx");
		this.generador2.setNombreFormato("notificacionPersonalAsociacion");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("notificacionPersonalAsociacion.jasper"));
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}
	
	private void generarNotificacion110Personal(DelegateExecution arg0){
		
		generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroCedula", (String) documento.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String) apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String) nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroRadicacion", (String) numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("fechaResolucion", fechaResolucion);
		this.generador2.parametros.put("numeroResolucion",resolucion.toString());
		this.generador2.parametros.put("prefijo",prefijo);
		this.generador2.setExtension("docx");
				
		this.generador2.setNombreFormato("notificacion110Personal");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("notificacion110Personal.jasper"));
		
		this.generador2.generaFormato();
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}
	
	private void generarNotificacionRecursoAsociacion(DelegateExecution arg0){
		
		Long matricula = conn.getMatricula((String)this.documento.getValue(arg0));
		
		this.generador2 = new GeneracionBase();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros = new HashMap<String, Object>();

		this.generador2.parametros.put("numeroMatricula", matricula.toString());
		this.generador2.parametros.put("numeroCedula", (String)this.documento.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", fechaResolucion);
		this.generador2.parametros.put("nombreAsociacion", (String)siglaAsociacion.getValue(arg0));
				
		this.generador2.setNombreFormato("notificacionRecursoAsociacion");	
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("notificacionRecursoAsociacion.jasper"));
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();			
	}
	
private void generarModeloCartaAprobacionSolicitud(DelegateExecution arg0){
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("numeroCDO", "");
		this.generador2.parametros.put("fecha", fechaMatricula);
		this.generador2.parametros.put("nombreTenico", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoTenico", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("direccionTenico", (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador2.parametros.put("telefonoTenico", (String) arg0.getVariable("qswfr_telefono"));
		this.generador2.parametros.put("ciudadTenico", (String) arg0.getVariable("qswfr_ciudad"));
		this.generador2.parametros.put("departamentoTenico", (String) arg0.getVariable("qswfr_departamento"));
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", fechaResolucion);
		this.generador2.parametros.put("prefijo", prefijo);
		
		this.generador2.setNombreFormato("modeloCartaAprobacionSolicitud");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("modeloCartaAprobacionSolicitud.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
}