package com.quasarbi.TaskListener;

import java.util.Date;
import java.util.Vector;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;

public class ActualizaPQRS implements TaskListener {

	Conexion con = new Conexion();
	
	@Override
	public void notify(DelegateTask arg0) {
		// TODO Auto-generated method stub
		if(arg0.getExecution().getVariable("etapa")!=null){
			if(arg0.getExecution().getVariable("etapa").toString().contains("I")) {
				//Insertar todos los valores
				Vector<Object> registro = new Vector<Object>();
				String peticionno = (String)arg0.getExecution().getVariable("peticionno");
				registro.add(peticionno);
				Date frecep = (Date)arg0.getVariable("qswfa_fecharecepcion");
				System.out.println(frecep + "0");
				registro.add(frecep);
				String nombres = (String)arg0.getVariable("qswfa_nombres");
				System.out.println(nombres + "0");
				registro.add(nombres);
				String apellidos = (String)arg0.getVariable("qswfa_apellidos");
				System.out.println(apellidos + "0");
				registro.add(apellidos);
				String numerodocumento = (String)arg0.getVariable("qswfa_numerodocumento");
				System.out.println(numerodocumento + "0");
				registro.add(numerodocumento);
				String direccion = (String)arg0.getVariable("qswfa_direccion");
				System.out.println(direccion + "0");
				registro.add(direccion);
				String ciudad = (String)arg0.getVariable("qswfa_ciudad");
				System.out.println(ciudad + "0");
				registro.add(ciudad);
				String telefono = (String)arg0.getVariable("qswfa_telefono");
				System.out.println(telefono + "0");
				registro.add(telefono);
				String email = (String)arg0.getVariable("qswfa_email");
				System.out.println(email + "0");
				registro.add(email);
				String solicitud = (String)arg0.getVariable("qswfa_redaccionsolicitud");
				System.out.println(solicitud + "0");
				registro.add(solicitud);
				con.actualizaPQRS(registro, "I");
			} else if(arg0.getExecution().getVariable("etapa").toString().contains("A")) {
				//Actualizar el grupo al cual fue asignado el caso
				Vector<Object> registro = new Vector<Object>();
				String peticionno = (String)arg0.getExecution().getVariable("peticionno");
				registro.add(peticionno);
				String grupo = arg0.getAssignee();
				registro.add(grupo);
				con.actualizaPQRS(registro, "A");
			} else if(arg0.getExecution().getVariable("etapa").toString().contains("R")) {
				//Actualizar la fecha de respuesta del caso
				Vector<Object> registro = new Vector<Object>();
				String peticionno = (String)arg0.getExecution().getVariable("peticionno");
				registro.add(peticionno);
				Date fresp = (Date)arg0.getVariable("qswfa_fecharespuestap");
				registro.add(fresp);
				con.actualizaPQRS(registro, "R");
			} else if(arg0.getExecution().getVariable("etapa").toString().contains("NA")) {
				//Actualizar la fecha de respuesta del caso
				Vector<Object> registro = new Vector<Object>();
				String peticionno = (String)arg0.getExecution().getVariable("peticionno");
				registro.add(peticionno);
				Date fresp = (Date)arg0.getVariable("qswfa_fecharp02");
				registro.add(fresp);
				con.actualizaPQRS(registro, "NA");
			} else if(arg0.getExecution().getVariable("etapa").toString().contains("C")) {
				//Actualizar la fecha de cierre del caso
				Vector<Object> registro = new Vector<Object>();
				String peticionno = (String)arg0.getExecution().getVariable("peticionno");
				registro.add(peticionno);
				
				Date frevresp = (Date)arg0.getVariable("qswfa_fecharp");
				if (frevresp==null) {
					frevresp = (Date)arg0.getVariable("qswfa_fecharp02");
				}
				
				System.out.println("valor: " + frevresp);
				registro.add(frevresp);
				con.actualizaPQRS(registro, "C");
			}
		} else {
			// Nothing to do
		}
	}

}
