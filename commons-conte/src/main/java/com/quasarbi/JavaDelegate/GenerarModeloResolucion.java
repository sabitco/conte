package com.quasarbi.JavaDelegate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;

public class GenerarModeloResolucion implements JavaDelegate {
	
	private GeneracionBase generador, generador2;
	private ConexionBase conexion;
	private Conexion conn;
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression modelo;
	private Expression directorioAlfresco, numeroRadicacion, solicitud, fechaResolucion,  nombreSolicitante, apellidoSolicitante,numeroFolios, numeroActa,
	numeroCedula, nombreConsejero, fechaPeticion, clasesSolicitadas, cartaGenerar, siglaAsociacion;
	/**
	 * Hace referencia a la vigencia de la matricula de una licencia especial
	 */
	private Expression fechaExpedicion;
	private String solicitudId, documento;
	private Long resolucion;
	
	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		conn = new Conexion();
		
		String carta = (String) cartaGenerar.getValue(arg0);
		
		solicitudId =  (String) this.solicitud.getValue(arg0);
		documento = (String)this.numeroCedula.getValue(arg0);
		resolucion = conn.getResolucion(solicitudId);
		if(resolucion<1 || resolucion == null){
			resolucion = (long) conn.getSequence("resolucion");
			conn.insertarResolucion(solicitudId,
					new SimpleDateFormat("yyyy-MM-dd").format((Date) fechaResolucion.getValue(arg0)), resolucion, documento);
		}
		
		conn.actualizarActaFolio((String) arg0.getVariable("qswfr_numeroActa"), (String) arg0.getVariable("qswfr_folios"), solicitudId);
		
		String formato = (String) modelo.getValue(arg0);
			
		this.generador = new GeneracionBase();
		
		this.conexion = new ConexionBase();
		this.generador.parametros = new HashMap<String, Object>();
		
		this.generador.parametros.put("numeroResolucion", resolucion.toString());
		this.generador.parametros.put("fechaExpedicion",(Date)fechaResolucion.getValue(arg0));//verificar cual es la fecha de expedicion
		this.generador.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador.parametros.put("numeroFolios", (String)this.numeroFolios.getValue(arg0));
		this.generador.parametros.put("numeroActa", (String)this.numeroActa.getValue(arg0));
		this.generador.parametros.put("numeroCedula", documento);
		this.generador.parametros.put("nombreConsejero", (String)this.nombreConsejero.getValue(arg0));
		this.generador.parametros.put("fechaNotificacion", new Date());
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
				
		if(formato.equalsIgnoreCase("Resolucion Primera Vez")){			
			generarDocumentosPrimeraVez(arg0);			
		}
		
		if(formato.equalsIgnoreCase("Resolucion Licencia Especial")){
			generarDocumentosLicenciaEspecial(arg0);
		}
		
		if(formato.equalsIgnoreCase("Revocatoria")){			
			this.generador.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));		
			
			this.generador.parametros.put("fechaResolucion", new Date());
			this.generador.parametros.put("clasesIniciales", conn.getClasesIniciales(documento));
			this.generador.parametros.put("clasesNuevas", conn.getClasesNuevas(solicitudId));
			
			this.generador.setNombreFormato("revocatoria");
			this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("revocacion.jasper"));		
			generarMatricula(arg0);
			generarCartaRevocatoria(arg0);
		}
		
		if(formato.equalsIgnoreCase("Resolucion Ampliacion Aprobada")){		
			generarCartaAprobacionAmpliacion(arg0);
			generarResolucionAmpliacionAprobada(arg0);
			generarMatricula(arg0);
		}				
		if(formato.equalsIgnoreCase("Recurso Aprobado")){
			this.generador.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));
			this.generador.parametros.put("clasesSolicitadas", (String)clasesSolicitadas.getValue(arg0));
			this.generador.parametros.put("clasesIniciales", conn.getClasesIniciales(documento));
			this.generador.parametros.put("fechaResolucion", (Date)fechaResolucion.getValue(arg0));
			this.generador.parametros.put("clasesNuevas", conn.getClasesNuevas(solicitudId));
			
			this.generador.setNombreFormato("recursoAprobado");
			this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("recursoAprobado.jasper"));		
			generarMatricula(arg0);			
						
			if(!carta.equals("Asociacion")){
				generarRecursoAprobado(arg0);
			}
			
			if(carta.equals("110 - Nacional")){
							
			}
			
		}
		
		if(formato.equalsIgnoreCase("Recurso Negado")){
			this.generador.parametros.put("clasesSolicitadas", (String)clasesSolicitadas.getValue(arg0));			
			this.generador.parametros.put("fechaPeticion", ((Date) arg0.getVariable("qswfr_fecharecurso")) == null 
					? new Date() : (Date) arg0.getVariable("qswfr_fecharecurso"));
			this.generador.parametros.put("fechaResolucion", (Date)fechaResolucion.getValue(arg0));
			this.generador.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
			this.generador.parametros.put("numeroFolioInicial", (String) numeroFolios.getValue(arg0));
			
			this.generador.setNombreFormato("recursoNegado");
			this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("recursoNegado.jasper"));
			

			
			if(!carta.equals("Asociacion")){
				generarNotificacionRecurso110(arg0);
			}
			if(carta.equals("110 - Nacional")){
				generarCartaRecursoNegado(arg0);				
			}
			
		}
		if(formato.equalsIgnoreCase("Resolucion Ampliacion Recurso Negado")){
			Date fechaPeticion = (Date) arg0.getVariable("qswfr_fecharecurso");
			fechaPeticion = (fechaPeticion) == null ? new Date() : fechaPeticion;
			this.generador.parametros.put("fechaPeticion", fechaPeticion);
			this.generador.setNombreFormato("resolucionAmpliacionRecursoNegado");			
			this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("resolucionAmpliacionRecursoNegado.jasper"));			
		}
		if(formato.equalsIgnoreCase("Resolucion Ampliacion Recurso Aprobado")){
			generarResolucionAmpliacionRecursoAprobado(arg0);			
		}	
		
		if(carta.equalsIgnoreCase("Asociacion") && 
				(formato.equalsIgnoreCase("Recurso Negado") || formato.equalsIgnoreCase("Recurso Aprobado"))
				){
			generarNotificacionRecursoAsociacion(arg0);			
		}
		
		actualizarRecurso(formato);
		 
		this.conn.actualizarConsejero(solicitudId, (String) nombreConsejero.getValue(arg0));
		
		this.generador.setExtension("docx");
		this.generador.generaFormato();
		this.conexion.setRutaArchivo(this.generador.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
	
	
	private void generarDocumentosLicenciaEspecial(DelegateExecution arg0) {
		this.generador.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));
		this.generador.setNombreFormato("resolucionLicencia");			
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("resolucionLicencia.jasper"));
		generarMatriculaLicenciaEspecial(arg0);		
	}

	private void generarRecursoAprobado(DelegateExecution arg0) {
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		if(matricula<1 || matricula == null){
			matricula = (long) conn.getSequence("matricula");
			conn.actualizarMatricula((String)this.numeroCedula.getValue(arg0), matricula);
		}
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("direccion",  (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador2.parametros.put("nombreCompleto", (String)this.nombreSolicitante.getValue(arg0) + " " 
				+ (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("fechaEnvio", new Date());
		this.generador2.parametros.put("telefono", (String) arg0.getVariable("qswfr_telefono"));
		this.generador2.parametros.put("ciudad",(String) arg0.getVariable("qswfr_ciudad"));
		this.generador2.parametros.put("departamento",(String) arg0.getVariable("qswfr_departamento"));
		this.generador2.parametros.put("numeroRadicado", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", conn.getFechaResolucionByDocumento(documento));
		this.generador2.parametros.put("numeroCedula", documento);		
		this.generador2.parametros.put("numeroMatricula", String.valueOf(matricula));
		this.generador2.parametros.put("fechaMatricula", conn.getFechaMatricula(documento));		
		
		this.generador2.setNombreFormato("cartaRecursoAprobado");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("cartaRecursoAprobado.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
		
	}


	private void generarCartaRevocatoria(DelegateExecution arg0) {
		
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		if(matricula<1 || matricula == null){
			matricula = (long) conn.getSequence("matricula");
			conn.actualizarMatricula((String)this.numeroCedula.getValue(arg0), matricula);
		}
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("fechaEnvio", new Date());
		this.generador2.parametros.put("nombreCompleto", (String)this.nombreSolicitante.getValue(arg0) + " " 
				+ (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroRadicado", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", conn.getFechaResolucionByDocumento(documento));
		this.generador2.parametros.put("numeroCedula", documento);
		this.generador2.parametros.put("numeroMatricula", String.valueOf(matricula));
		this.generador2.parametros.put("fechaMatricula", conn.getFechaMatricula(documento));		
		this.generador2.parametros.put("direccion",  (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador2.parametros.put("telefono", (String) arg0.getVariable("qswfr_telefono"));
		this.generador2.parametros.put("ciudad",(String) arg0.getVariable("qswfr_ciudad"));
		this.generador2.parametros.put("departamento",(String) arg0.getVariable("qswfr_departamento"));
		
		
		this.generador2.setNombreFormato("cartaRevocatoria");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("cartaRevocatoria.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
		
	}


	private void generarCartaRecursoNegado(DelegateExecution arg0) {
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		if(matricula<1 || matricula == null){
			matricula = (long) conn.getSequence("matricula");
			conn.actualizarMatricula((String)this.numeroCedula.getValue(arg0), matricula);
		}
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("direccion",  (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador2.parametros.put("nombreCompleto", (String)this.nombreSolicitante.getValue(arg0) + " " 
				+ (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("fechaEnvio", new Date());
		this.generador2.parametros.put("telefono", (String) arg0.getVariable("qswfr_telefono"));
		this.generador2.parametros.put("ciudad",(String) arg0.getVariable("qswfr_ciudad"));
		this.generador2.parametros.put("departamento",(String) arg0.getVariable("qswfr_departamento"));
		this.generador2.parametros.put("numeroRadicado", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", conn.getFechaResolucionByDocumento(documento));
		this.generador2.parametros.put("numeroCedula", documento);		
		this.generador2.parametros.put("numeroMatricula", String.valueOf(matricula));
		this.generador2.parametros.put("fechaMatricula", conn.getFechaMatricula(documento));		
		
		this.generador2.setNombreFormato("cartaRecursoNegado");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("cartaRecursoNegado.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
		
	}


	private void generarCartaAprobacionAmpliacion(DelegateExecution arg0) {
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("fechaEnvio", new Date());
		this.generador2.parametros.put("nombreCompleto", (String)this.nombreSolicitante.getValue(arg0) + " " 
				+ (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("direccion",  (String) arg0.getVariable("qswfr_direccionResidencia"));
		this.generador2.parametros.put("telefono", (String) arg0.getVariable("qswfr_telefono"));
		this.generador2.parametros.put("ciudad",(String) arg0.getVariable("qswfr_ciudad"));
		this.generador2.parametros.put("departamento",(String) arg0.getVariable("qswfr_departamento"));
		this.generador2.parametros.put("numeroRadicado", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", conn.getFechaResolucionByDocumento(documento));
		this.generador2.parametros.put("numeroCedula", documento);
		
		this.generador2.setNombreFormato("cartaAprobacionAmpliacion");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("cartaAprobacionAmpliacion.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}


	private void generarDocumentosPrimeraVez(DelegateExecution arg0){
		this.generador.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));
		this.generador.setNombreFormato("resolucionPrimeraVez");			
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream(
                            "/jasper/resolucionPrimeraVez.jasper"));
				
		generarMatricula(arg0);
	}
	
	
	private void generarResolucionAmpliacionAprobada(DelegateExecution arg0){
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaExpedicion", new Date());//verificar cual es la fecha de expedicion
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroFolios", (String)this.numeroFolios.getValue(arg0));//(String) arg0.getVariable("qswfr_folios"));//
		this.generador2.parametros.put("numeroActa", (String)this.numeroActa.getValue(arg0));
		this.generador2.parametros.put("numeroCedula", (String)this.numeroCedula.getValue(arg0));
		this.generador2.parametros.put("nombreConsejero", (String)this.nombreConsejero.getValue(arg0));
		
		this.generador2.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));
		this.generador2.parametros.put("fechaSolicitud", new Date());
		this.generador2.setNombreFormato("resolucionAmpliacionAprobada");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("resolucionAmpliacionAprobada.jasper"));	
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();
	}
	
	private void generarResolucionAmpliacionRecursoAprobado(DelegateExecution arg0){
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaExpedicion", new Date());//verificar cual es la fecha de expedicion
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroFolios", (String)this.numeroFolios.getValue(arg0));//(String) arg0.getVariable("qswfr_folios"));//
		this.generador2.parametros.put("numeroActa", (String)this.numeroActa.getValue(arg0));
		this.generador2.parametros.put("numeroCedula", (String)this.numeroCedula.getValue(arg0));
		this.generador2.parametros.put("nombreConsejero", (String)this.nombreConsejero.getValue(arg0));
		
		this.generador2.parametros.put("clasesSolicitadas", (String)clasesSolicitadas.getValue(arg0));	
		this.generador2.parametros.put("fechaResolucion", new Date());
		this.generador2.parametros.put("fechaNotificacion", new Date());
		this.generador2.parametros.put("clasesIniciales", conn.getClasesIniciales(documento));
		this.generador2.parametros.put("clasesNuevas", conn.getClasesNuevas(solicitudId));
		this.generador2.parametros.put("clasesDobles", conn.generarTextoClasesByDocumento(documento));
		this.generador2.setNombreFormato("resolucionAmpliacionRecursoAprobado");	
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("resolucionAmpliacionRecursoAprobado.jasper"));
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}
	
	private void generarMatricula(DelegateExecution arg0){
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		if(matricula<1 || matricula == null){
			matricula = (long) conn.getSequence("matricula");
			conn.actualizarMatricula((String)this.numeroCedula.getValue(arg0), matricula);
		}
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numCedulaSolicitante", (String)this.numeroCedula.getValue(arg0));
		this.generador2.parametros.put("numResolucionSolicitante", resolucion.toString());
		this.generador2.parametros.put("numRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numMatricula", matricula.toString());		
		this.generador2.parametros.put("fechaResolucion", (Date)fechaResolucion.getValue(arg0));
		this.generador2.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador2.parametros.put("prefijo", (String)arg0.getVariable("prefijo"));
		this.generador2.setNombreFormato("anexo27Sec1");
		this.generador2.setConsecutivo("");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("/jasper/anexo27Sec1.jasper"));
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}
	
	private void generarMatriculaLicenciaEspecial(DelegateExecution arg0){
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		if(matricula<1 || matricula == null){
			matricula = (long) conn.getSequence("matricula");
			conn.actualizarMatricula((String)this.numeroCedula.getValue(arg0), matricula);
		}
		
		this.generador2 = new GeneracionBase();
		this.generador2.parametros = new HashMap<String, Object>();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroCedula", (String)this.numeroCedula.getValue(arg0));
		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("numeroMatricula", matricula.toString());		
		this.generador2.parametros.put("fechaResolucion", (Date)fechaResolucion.getValue(arg0));
		this.generador2.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador2.parametros.put("prefijo", (String)arg0.getVariable("prefijo"));
		this.generador2.setNombreFormato("Anexo27_sec1_Licespecial");
		this.generador2.setConsecutivo("");
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("Anexo27_sec1_Licespecial.jasper"));
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();		
	}
	
	
	private void generarNotificacionRecurso110(DelegateExecution arg0){
		
		Long matricula = conn.getMatricula((String)this.numeroCedula.getValue(arg0));
		
		this.generador2 = new GeneracionBase();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros = new HashMap<String, Object>();

		this.generador2.parametros.put("numeroResolucion", resolucion.toString());
		this.generador2.parametros.put("fechaResolucion", (Date)fechaResolucion.getValue(arg0));
		this.generador2.parametros.put("numeroRadicacion", (String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros.put("nombreSolicitante", (String)this.nombreSolicitante.getValue(arg0));
		this.generador2.parametros.put("apellidoSolicitante", (String)this.apellidoSolicitante.getValue(arg0));
		this.generador2.parametros.put("numeroCedula", (String)this.numeroCedula.getValue(arg0));
		this.generador2.parametros.put("numeroMatricula", matricula.toString());		
		
		this.generador2.setNombreFormato("notificacionRecurso110");	
		this.generador2.setPlantillaFormato(this.getClass().getResourceAsStream("notificacionRecurso110.jasper"));
		
		this.generador2.setExtension("docx");
		this.generador2.generaFormato();
		this.conexion.setRutaArchivo(this.generador2.getRutaArchivo());
		this.conexion.setNombreArchivo(this.generador2.getNombreArchivo());
		this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(this.generador2.getExtension()));
		this.conexion.setDirectorioAlfresco((String)this.directorioAlfresco.getValue(arg0));
		this.conexion.subeArchivo();			
	}
	
	private void generarNotificacionRecursoAsociacion(DelegateExecution arg0){
		
		Long matricula = conn.getMatricula(documento);
		
		this.generador2 = new GeneracionBase();
		this.generador2.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		this.generador2.parametros = new HashMap<String, Object>();

		this.generador2.parametros.put("numeroMatricula", matricula.toString());
		this.generador2.parametros.put("numeroCedula", documento);
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
	
	
	public static void main(String args[]){
		
		String clases = "TE-1 T�CNICO EN INSTALACIONES EL�CTRICAS INTERIORES " +
				"Para adelantar actividades relacionadas con el estudio aplicado al  montaje de tomacorrientes, enchufes, salidas para alumbrado, lamparas, luminarias, interruptores. reparacion de tableros de distribuci�n de circuitos. as� como al  reparacion de equipo de medida, protecci�n, control,  se�alizaci�n y servicios auxiliares" +
				"\n\n" +
				"TE-2 T�CNICO EN BOBINADOS EL�CTRICOS Y ACCESORIOS" +
				"\n\n" +
				"Para adelantar actividades relacionadas con el estudio aplicado al  mando de motores el�ctricos. montaje y conexi�n,  as� como al  mantenimiento y reparaci�n de equipos de instalaciones el�ctricas;" +
				"\n\n"+
				"TE-3 TECNICO EN MANTENIMIENTO ELECTRICO"+
				"\n\n"+
				"Para adelantar actividades relacionadas con el estudio aplicado al  operaci�n y mto. de accesorios electr�nicos industriales de instrumentaci�n. operaci�n y mantenimiento de instalaciones el�ctricas de accionamiento y control de m�quinas. operaci�n y mantenimiento de instalaciones el�ctricas,  operaci�n y mto. de accesorios electr�nicos industriales de equipos y aparatos hidr�ulicos. operaci�n y mantenimiento de instalaciones el�ctricas,  as� como al  operaci�n y mto. de accesorios electr�nicos industriales de equipos y aparatos neum�ticos;"+
				"\n\n"+
				"TE-4 TECNICO EN ELECTRICIDAD INDUSTRIAL" +
				"\n\n"+
				"Para adelantar actividades relacionadas con el estudio aplicado al  montaje,  as� como al  fabricaci�n y construcci�n de celdas de baja tensi�n" 	+
				"\n\n"+
				"AUX - Auxiliar de Ingenieros Electricistas"+
				"\n\n"+
				"Para adelantar actividades y labores relacionadas con el estudio y las aplicaciones de la electricidad en cuyo ejercicio requiere la direcci�n, coordinaci�n y responsabilidad de Ingenieros Electricistas.";		
		//Long matricula = conn.getMatricula(documento);
		 GeneracionBase qgenerador2 = new GeneracionBase();
		qgenerador2.setConsecutivo("lalala3");
		qgenerador2.parametros = new HashMap<String, Object>();

		qgenerador2.parametros.put("lalal", clases);
		qgenerador2.parametros.put("numeroCedula", "lalala");
		qgenerador2.parametros.put("nombreSolicitante", "lalala");
		qgenerador2.parametros.put("apellidoSolicitante", "lalalal");
		qgenerador2.parametros.put("numeroRadicacion", "lalalala");
		qgenerador2.parametros.put("numeroResolucion","lalalal");
		qgenerador2.parametros.put("fechaExpedicion", new Date());
		qgenerador2.parametros.put("nombreAsociacion", "lalalala");
				
		qgenerador2.setNombreFormato("resolucionPrimeraVez");	
		qgenerador2.setPlantillaFormato(GenerarModeloResolucion.class.getResourceAsStream("resolucionPrimeraVez.jasper"));
		
		qgenerador2.setExtension("docx");
		qgenerador2.generaFormato();
		
	}
	
	private void actualizarRecurso(String formatoGenerar){
		if(formatoGenerar.contains("Negad")){
			conn.actualizarRecurso(solicitudId, "Negado");
		} else if (formatoGenerar.equals("Revocatoria")){
			conn.actualizarRecurso(solicitudId, "Revocatoria");
		} else if (formatoGenerar.contains("Aprobad")){
			conn.actualizarRecurso(solicitudId, "Aprobado");
		}	
	}
		
}
