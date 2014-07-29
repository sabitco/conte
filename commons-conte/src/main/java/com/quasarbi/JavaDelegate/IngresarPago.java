package com.quasarbi.JavaDelegate;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import org.activiti.engine.delegate.Expression;

import com.quasarbi.ConexionBD.Conexion;

public class IngresarPago implements JavaDelegate{
	
	Expression valor, fechaConsignacion, entidadPago;

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		Conexion con = new Conexion();
		con.ingresarPago((String)arg0.getVariable("qswfr_numeroformulario"),(String)arg0.getVariable("qswfr_numerodocumento"),
				(Date) fechaConsignacion.getValue(arg0),(String)valor.getValue(arg0));
		
		
	}

}
