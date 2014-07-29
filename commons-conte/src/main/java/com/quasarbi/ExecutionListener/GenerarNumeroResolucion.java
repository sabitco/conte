package com.quasarbi.ExecutionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class GenerarNumeroResolucion implements ExecutionListener {

	private Expression solicitud, fechaResolucion, documento, modelo;
	private Conexion conexion;
	
	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		String formato = (String) modelo.getValue(arg0);
		String solicitudId = (String) solicitud.getValue(arg0);
		if(!formato.equalsIgnoreCase("Recurso Negado") 
				&& !formato.equalsIgnoreCase("Resolucion Ampliacion Recurso Negado")){
			conexion = new Conexion();
			if(formato.contains("Recurso")){
				Long resolucion = (long) conexion.getSequence("resolucion");
				conexion.insertarResolucion((String) solicitud.getValue(arg0), 
						new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaResolucion.getValue(arg0)), 
						resolucion, (String)documento.getValue(arg0));
			} else {
				Long resolucion = conexion.getResolucion(solicitudId);
				if(resolucion == null || resolucion == 0){
					resolucion = (long) conexion.getSequence("resolucion");
					conexion.insertarResolucion((String) solicitud.getValue(arg0), 
							new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaResolucion.getValue(arg0)), 
							resolucion, (String)documento.getValue(arg0));					
				}				
			}
		}
	}
}
