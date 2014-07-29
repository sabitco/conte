package com.quasarbi.Constantes;

import java.util.HashMap;
import java.util.Map;

public class TiposArchivo {
	
	public Map<String, String> mimetypes = new HashMap<String, String>();
	public Map<String, String> relacionTaskFields = new HashMap<String, String>();
	
	public TiposArchivo(){
		mimetypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimetypes.put("pdf", "application/pdf");
		mimetypes.put("txt", "text/plain");
		
		
		//crear radicado
		relacionTaskFields.put("alfrescoScripttask1", "2");
		//digitalizacion
		relacionTaskFields.put("utDigitalizarDocumentos", "3");
		relacionTaskFields.put("digitalizarDocumentos", "3");
		//revision documental
		relacionTaskFields.put("utVerificarValidezDocumentacion", "5");
		//validacion de informacion
		relacionTaskFields.put("utValidarInformacionExpediente", "4");
		//inadmision temporal
		relacionTaskFields.put("utEmitirConceptoDoc", "6");
		//inadmitido
		relacionTaskFields.put("utNotificarSolicitante", "7");
		//en evaluacion
		relacionTaskFields.put("utEvaluarExpediente", "8");
		//evaluacion finalizada
		relacionTaskFields.put("utSeleccionarFormato", "9");
		//pasar solicitud a comite
		relacionTaskFields.put("utPasarSolicitudComite", "10");
		//en impresion
		relacionTaskFields.put("utImprimirResolucionRechazo", "11");
		//publicacion de aviso
		relacionTaskFields.put("utPublicarAviso", "14");
		//desfijacion de aviso
		relacionTaskFields.put("mailtask1", "15");
		//aprobado
		relacionTaskFields.put("utAprobarSolicitud", "17");
		relacionTaskFields.put("ubicarExpediente", "17");
		relacionTaskFields.put("enviarDuplicado", "17");
		
	}
	
}
