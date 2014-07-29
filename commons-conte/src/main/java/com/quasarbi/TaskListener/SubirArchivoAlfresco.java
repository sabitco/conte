package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.Constantes.TiposArchivo;
import com.quasarbi.Generadores.*;;

public class SubirArchivoAlfresco implements TaskListener {

	ConexionBase conexion = new ConexionBase();
	private TiposArchivo tiposArchivo = new TiposArchivo();
	private Expression rutaArchivo;

	@Override
	public void notify(DelegateTask arg0) {
		// TODO Auto-generated method stub
		if((String) rutaArchivo.getValue(arg0)!=null && ((String) rutaArchivo.getValue(arg0)!="")){
			String separador = System.getProperty("file.separator");
			separador = separador.equals("\\") ? "\\"+"\\" : separador;
			String ruta[] = ((String) rutaArchivo.getValue(arg0)).split(separador);
			this.conexion.setRutaArchivo((String) rutaArchivo.getValue(arg0));
			this.conexion.setNombreArchivo(ruta[ruta.length-1]);
			String extension[] = ruta[ruta.length-1].split("\\.");
			this.conexion.setTipoArchivo(tiposArchivo.mimetypes.get(extension[extension.length-1].toLowerCase()));
			this.conexion.setDirectorioAlfresco((String) arg0.getVariable("nombrecarpeta"));
			this.conexion.subeArchivo();
		}
	}

}
