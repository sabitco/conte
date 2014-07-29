package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizarActa implements TaskListener {
	
	private Expression acta, fechaActa;
	
	@Override
	public void notify(DelegateTask arg0) {
		Conexion con = new Conexion();		
		con.actualizarActaFolio((String)acta.getValue(arg0), (String) arg0.getVariable("qswfr_folios"), 
				(String) arg0.getVariable("qswfr_numeroformulario"));
		//con.actualizarConsejero((String)solicitud.getValue(arg0), (String)consejero.getValue(arg0));		
	}
}
