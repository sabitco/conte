package com.quasarbi.ExecutionListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizarFechaRecepcion implements ExecutionListener {
	
	private Conexion conexion;
	private Expression solicitud, fechaRecepcion;

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		conexion = new Conexion();
		conexion.actualizarFechaRecepcionTecnico((String)solicitud.getValue(arg0), new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaRecepcion.getValue(arg0)));	
	}
}