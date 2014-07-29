package com.quasarbi.ExecutionListener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizarConsejero implements ExecutionListener{
	
	private Expression solicitud, consejero;
	
	@Override
	public void notify(DelegateExecution arg0) throws Exception {
		Conexion con = new Conexion();		
		con.actualizarConsejero((String)solicitud.getValue(arg0), (String)consejero.getValue(arg0));		
	}
}
