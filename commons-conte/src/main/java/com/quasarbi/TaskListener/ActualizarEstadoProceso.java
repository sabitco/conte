package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Constantes.TiposArchivo;

public class ActualizarEstadoProceso implements TaskListener {

	private Conexion conn;
	private TiposArchivo tip;
	
	@Override
	public void notify(DelegateTask arg0) {
		this.conn = new Conexion();
		this.conn.getDataSource();
		tip = new TiposArchivo();
		System.out.println("El task definition key es: " + arg0.getTaskDefinitionKey());
		System.out.println("El id de la tarea es: " + arg0.getId());
		System.out.println("El nombre de la tarea es: " + arg0.getName());
		System.out.println("El numero del formulario es: " + (String)arg0.getVariable("qswfr_numeroformulario"));
		this.conn.actualizaEstadoProceso((String)arg0.getVariable("qswfr_numeroformulario"), tip.relacionTaskFields.get(arg0.getTaskDefinitionKey()));
	}

}
