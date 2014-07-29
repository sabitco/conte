package com.quasarbi.TaskListener;

import java.text.SimpleDateFormat;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.Expression;
import java.util.Date;

import com.quasarbi.ConexionBD.Conexion;

public class InsertarFechaEnvioTecnico implements TaskListener {

	private Conexion conexion;
	private Expression solicitud, fechaEnvio;
	
	@Override
	public void notify(DelegateTask arg0) {
		conexion = new Conexion();
		if(solicitud.getValue(arg0)!=null || fechaEnvio.getValue(arg0)!=null){
			conexion.insertarFechaEnvioTecnico((String)solicitud.getValue(arg0), new SimpleDateFormat("yyyy-MM-dd").format((Date)fechaEnvio.getValue(arg0)));
		}
	}

}
