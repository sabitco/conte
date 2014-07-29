package com.quasarbi.ExecutionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.ExecutionListener;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizarFechaRadicacion implements ExecutionListener{
	
	private Expression fechaRadicacion, solicitudId;
	
	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		Conexion conexion = new Conexion();
		conexion.actualizaFechaRadicado((String) solicitudId.getValue(arg0), 
				new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaRadicacion.getValue(arg0)));		
		
	}
}
