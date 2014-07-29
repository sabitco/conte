package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
//import org.alfresco.repo.jscript.People;

import com.quasarbi.ConexionBD.Conexion;

public class VerificarAsignacion implements TaskListener{

	private Conexion conn;
	
	@Override
	public void notify(DelegateTask arg0) {
		conn = new Conexion();
		System.out.println("Tarea que implementa este listener: " + arg0.getDescription());
		String nit = (String) arg0.getVariable("qswfr_nit");
		System.out.println("El nit del proceso de radicacion es: " + nit);
		String responsable = "";
		System.out.println("El usuario que esta asignado en esta tarea es: " + responsable);
		if(nit!=null){
			responsable = conn.consultaAsignacionProcesos(nit);
			if(arg0.getVariable("estadoValidacion")==null){
				System.out.println("Se consultara en la tabla si ya se ha asignado un usuario para este nit");
				if(responsable.length()>0){
					System.out.println("Ya se han asignado los flujos con nit: " + nit + " al usuario: " + responsable + " este flujo tambien se asignara a este usuario.");
					//People p = new People();
					//p.getPerson(responsable);
					arg0.setVariable("usuarioasignado", responsable);
					//arg0.setVariable("bpm_assignee", p);
				}
				arg0.setVariable("estadoValidacion", "antes");
			} else {
				responsable = (String) arg0.getVariable("usuarioasignado");
				System.out.println("Se asignaran los flujos con nit: " + nit + " al usuario: " + responsable);
				conn.asignaProcesosAUsuario(responsable, nit);
				arg0.setVariable("estadoValidacion", "despues");
			}
		}
	}

}
