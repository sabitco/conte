package com.quasarbi.TaskListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;

public class GeneracionNumeroRadicado implements TaskListener {

	private Conexion conn;
	
	@Override
	public void notify(DelegateTask arg0){
		this.conn = new Conexion();
		DelegateExecution execution = arg0.getExecution();
		String radicado = (String) execution.getVariable("qswfr_numeroRadicado");
		String solicitud_id = (String) execution.getVariable("qswfr_numeroformulario");
		String documento = (String) execution.getVariable("qswfr_numerodocumento");
		Date fechaRadicado = (Date) execution.getVariable("qswfr_fechaRecibeDocumentos");
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String tmpA = conn.getRadicado(documento);
		int numeroRadicado = -1;
		if(tmpA.split(",")[0].length()>0){
			numeroRadicado = Integer.parseInt(tmpA.split(",")[0]);
		}
		if( numeroRadicado < 1 ){
			//Se genera el número de radicado
			String tmp = this.conn.obtieneRadicado();
			radicado = tmp.split(",")[0];			
//			try {
//				fechaRadicado = formato.parse(tmp.split(",")[1]);
//			} catch (ParseException e) {
//				System.out.println("Se presento un error obteniendo el numero consecutivo del radicado.");
//				e.printStackTrace();
//			}
			//Se establecen las variables
			
			conn.insertarRadicado(solicitud_id, radicado, tmp.split(",")[1], (String) execution.getVariable("qswfr_numerodocumento"));
			//
			execution.setVariable("qswfr_numeroRadicado", radicado);
			execution.setVariable("qswfr_fechaRadicado", fechaRadicado);	
			execution.setVariable("bpm_workflowDescription", radicado 
					+ " " + (String) execution.getVariable("qswfr_tiposolicitud")
					+ " " + (String) execution.getVariable("qswfr_numerodocumento")
					+ " " + (String) execution.getVariable("qswfr_nombres") 
					+ " " + (String)execution.getVariable("qswfr_apellidos"));
			
			execution.setVariable("nombrecarpeta", radicado					
					+ " " + (String) execution.getVariable("qswfr_numerodocumento")
					+ " " + (String) execution.getVariable("qswfr_nombres") 
					+ " " + (String)execution.getVariable("qswfr_apellidos"));
			//throw new NullPointerException("El numero de radicado es: "+ radicado);
		} else {
			radicado = String.valueOf(numeroRadicado);
			try {
				fechaRadicado = formato.parse(tmpA.split(",")[1]);
			} catch (ParseException e) {
				System.out.println("Se presento un error obteniendo el numero consecutivo del radicado.");
				e.printStackTrace();
			}
			execution.setVariable("qswfr_numeroRadicado", radicado);
			execution.setVariable("qswfr_fechaRadicado", fechaRadicado);	
			execution.setVariable("bpm_workflowDescription", radicado 
					+ " " + (String) execution.getVariable("qswfr_tiposolicitud")
					+ " " + (String) execution.getVariable("qswfr_numerodocumento")
					+ " " + (String) execution.getVariable("qswfr_nombres") 
					+ " " + (String)execution.getVariable("qswfr_apellidos"));
		}
	}	
}