package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizarConsejero implements JavaDelegate{
	
	Expression solicitud, consejero;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		Conexion con = new Conexion();		
		con.actualizarConsejero((String)solicitud.getValue(arg0), (String)consejero.getValue(arg0));		
	}
}