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

public class GeneracionDocumentosEnvioDuplicado implements JavaDelegate{
	
	private GeneracionBase generador;
	private ConexionBase conexion = new ConexionBase();
	private Conexion conn = new Conexion();
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression directorioAlfresco, numeroRadicacion;
	private String matricula, documento, radicado,resolucion;	
	private Date fechaResolucion;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		documento = (String)arg0.getVariable("qswfgd_numerodocumento");		
		matricula = String.valueOf(conn.getMatricula(documento));		
		radicado = conn.getRadicado(documento).split(",")[0];
		
		generarModeloConstanciaMatriculaTarjeta(arg0);
		
		documento = (String)arg0.getVariable("qswfgd_numerodocumento");
		
		fechaResolucion = conn.getFechaResolucionByDocumento(documento);
		matricula = String.valueOf(conn.getMatricula(documento));
		resolucion = String.valueOf(conn.getResolucionByDocumento(documento));
					
		if(((String)arg0.getVariable("qswfgd_generadoEn")).equalsIgnoreCase("CONTE")){
			generarAnexo27Sec1DupConte(arg0);
		} else {
			generarAnexo27Sec1DupMinminas(arg0);
		}
		
				
		if(((String)arg0.getVariable("qswfgd_notificacionGenerar")).equalsIgnoreCase("Personal")){
			//si es personal
			generarModeloTarjetaEnvioPersonal(arg0);			
		} else {
			//si es asociacion o empresa			
			generarModeloEnvioDuplicadoAsociacion(arg0);
		}
	}
	
	private void generarModeloEnvioDuplicadoAsociacion(DelegateExecution arg0){ 
		
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		String ciudad = (String)arg0.getVariable("qswfgd_ciudad"); 
		
		String numeroGuia = (String)arg0.getVariable("qswfgd_numeroGuia2");		
		conn.agregarEnvioDuplicado((radicado),(String) arg0.getVariable("qswfr_nombres"),  
				(String) arg0.getVariable("qswfr_apellidos"), (String) arg0.getVariable("qswfgd_celular"),(String) arg0.getVariable("qswfgd_direccionResidencia"),
				ciudad ,(String) arg0.getVariable("qswfr_tiposolicitud"), numeroGuia );
		
		if(((String)arg0.getVariable("qswfgd_notificacionGenerar")).equalsIgnoreCase("Asociacion")){
			Asociacion asociacion = conn.getAsociacionBySigla((String)arg0.getVariable("qswfgd_asociacion"));
			this.generador.parametros.put("asociacion_presidente", asociacion.getPresidente() + "\nPresidente");
			this.generador.parametros.put("asociacion_sigla", asociacion.getSigla());
			this.generador.parametros.put("direccion", asociacion.getDireccion());
			this.generador.parametros.put("ciudad_nombre", asociacion.getCiudad());
			this.generador.parametros.put("departamento_nombre", asociacion.getDepartamento());
			this.generador.parametros.put("telefono", asociacion.getTelefono());
			this.generador.parametros.put("numeroGuia", numeroGuia);
			this.generador.setNombreFormato("modeloEnvioDuplicadoAsociacion");
		} else {
			try{
				this.generador.parametros.put("asociacion_presidente", (String)arg0.getVariable("qswfgd_empresa"));
				this.generador.parametros.put("asociacion_sigla", (String)arg0.getVariable(""));
				this.generador.parametros.put("direccion", (String)arg0.getVariable("qswfgd_direccionEmpresa"));
				this.generador.parametros.put("ciudad_nombre", ciudad);
				this.generador.parametros.put("departamento_nombre", (String)arg0.getVariable("qswfgd_departamentoEmpresa"));
				this.generador.parametros.put("telefono", (String)arg0.getVariable("qswfgd_telefonoEmpresa"));
				this.generador.parametros.put("numeroGuia", numeroGuia);						
			} catch (Exception ex){
				this.generador.parametros.put("numeroGuia", numeroGuia);				
			}
			this.generador.setNombreFormato("modeloEnvioDuplicadoEmpresa");
		}		
		this.generador.setExtension("docx");
		
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("modeloEnvioDuplicadoAsociacion.jasper"));
		this.generador.generaFormatoConConexion();
	
		subirArhicvo(arg0);
		
	}
	
	private void generarModeloConstanciaMatriculaTarjeta(DelegateExecution arg0){ 
		
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador.parametros.put("nombreCiudad",  (String)arg0.getVariable("qswfgd_ciudad"));
		this.generador.parametros.put("departamento",  (String)arg0.getVariable("qswfgd_departamento"));
		this.generador.parametros.put("fecha", new Date());
		this.generador.parametros.put("numeroRadicacion", radicado);
		this.generador.parametros.put("numeroCedulaSolicitante", documento);
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("fechaMatricula", conn.getFechaMatricula(documento));
		this.generador.parametros.put("nombreSolicitante", (String)arg0.getVariable("qswfgd_nombres"));
		this.generador.parametros.put("apellidoSolicitante", (String)arg0.getVariable("qswfgd_apellidos"));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("modeloConstanciaMatriculaTarjeta");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("modeloConstanciaMatriculaTarjeta.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);
		
	}
	
	
	private void generarModeloTarjetaEnvioPersonal(DelegateExecution arg0){ 
		
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador.parametros.put("fechaEnvio", (Date)arg0.getVariable("qswfgd_fechaMatricula"));
		this.generador.parametros.put("nombreTecnico", (String)arg0.getVariable("qswfgd_nombres"));
		this.generador.parametros.put("apellidoTecnico", (String)arg0.getVariable("qswfgd_apellidos"));
		this.generador.parametros.put("direccion", (String)arg0.getVariable("qswfgd_direccionResidencia"));
		this.generador.parametros.put("ciudad",  (String)arg0.getVariable("qswfgd_ciudad"));
		this.generador.parametros.put("departamento",  (String)arg0.getVariable("qswfgd_departamento"));
		this.generador.parametros.put("numeroRadicado", radicado);
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("telefono",(String)arg0.getVariable("qswfgd_telefono"));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("modeloEnvioTarjetaPersonal");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("modeloEnvioTarjetaPersonal.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);
		
	}
	
private void generarAnexo27Sec1DupConte(DelegateExecution arg0){ 
		
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));
		
		this.generador.parametros.put("nombreSolicitante", (String)arg0.getVariable("qswfgd_nombres"));
		this.generador.parametros.put("apellidoSolicitante", (String)arg0.getVariable("qswfgd_apellidos"));
		this.generador.parametros.put("numeroCedula", documento);
		this.generador.parametros.put("ciudadCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("dptoCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("fechaMatricula", conn.getFechaMatricula(documento));
		this.generador.parametros.put("numeroRadicacion", radicado);
		this.generador.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador.parametros.put("fechaExpedicion", (Date)arg0.getVariable("qswfgd_fechaMatricula"));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("anexo27Sec1DupConte");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("anexo27Sec1DupConte.jasper"));
		this.generador.generaFormato();
	
		subirArhicvo(arg0);
		
	}
	
	private void generarAnexo27Sec1DupMinminas(DelegateExecution arg0){
		this.generador = new GeneracionBase();
		this.generador.parametros = new HashMap<String, Object>();
		this.generador.setConsecutivo((String)this.numeroRadicacion.getValue(arg0));

		this.generador.parametros.put("nombreSolicitante", (String)arg0.getVariable("qswfgd_nombres"));
		this.generador.parametros.put("apellidoSolicitante", (String)arg0.getVariable("qswfgd_apellidos"));
		this.generador.parametros.put("numeroCedula", documento);
		this.generador.parametros.put("ciudadCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("dptoCedula", (String)arg0.getVariable(""));
		this.generador.parametros.put("numeroMatricula", matricula);
		this.generador.parametros.put("numeroResolucion", resolucion);
		this.generador.parametros.put("fechaResolucion", fechaResolucion);
		this.generador.parametros.put("numeroRadicacion", radicado);
		this.generador.parametros.put("clases", conn.generarTextoClasesByDocumento(documento));
		this.generador.parametros.put("fechaExpedicion", (Date)arg0.getVariable("qswfgd_fechaMatricula"));
		
		this.generador.setExtension("docx");
		this.generador.setNombreFormato("anexo27Sec1DupMinminas");
		this.generador.setPlantillaFormato(this.getClass().getResourceAsStream("anexo27Sec1DupMinminas.jasper"));
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
