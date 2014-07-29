package com.quasarbi.ExecutionListener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class InsertarAviso implements ExecutionListener {
	
	private Conexion conexion;
	private Expression aviso, solicitud;	

	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		
		conexion = new Conexion();
		conexion.insertarAviso((String) solicitud.getValue(arg0), (String)aviso.getValue(arg0));		
	}
}
