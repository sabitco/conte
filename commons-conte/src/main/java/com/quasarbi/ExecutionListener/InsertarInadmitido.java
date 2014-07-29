package com.quasarbi.ExecutionListener;

import java.text.SimpleDateFormat;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

import java.util.Date;

public class InsertarInadmitido implements ExecutionListener{
	
	private Conexion conexion;
	private Expression solicitud, causa, fechaNotificacion;

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		conexion = new Conexion();
		conexion.insertarInadmitido((String)solicitud.getValue(arg0), (String)causa.getValue(arg0), 
				new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaNotificacion.getValue(arg0)));
	}

}
