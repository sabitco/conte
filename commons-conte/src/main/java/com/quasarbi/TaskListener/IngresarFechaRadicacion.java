package com.quasarbi.TaskListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;

public class IngresarFechaRadicacion implements TaskListener{
	
	private Expression solicitud, fechaRadicacion;
	private Conexion conexion;

	@Override
	public void notify(DelegateTask arg0) {
		conexion = new Conexion();
		conexion.insertarFechaRadicacion((String) solicitud.getValue(arg0), 
				new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaRadicacion.getValue(arg0)));		
	}

}
