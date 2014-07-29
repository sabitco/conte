package com.quasarbi.ExecutionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class InsertarRecurso implements ExecutionListener {
	
	private Conexion conexion;
	private Expression fecha, solicitud, tipo;

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		conexion = new Conexion();
		conexion.insertarRecurso((String) solicitud.getValue(arg0), new SimpleDateFormat("yyyy-MM-dd").format((Date)fecha.getValue(arg0)), (String)tipo.getValue(arg0));		
	}

}
