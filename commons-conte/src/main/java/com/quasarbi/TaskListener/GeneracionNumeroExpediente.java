package com.quasarbi.TaskListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.quasarbi.ConexionBD.Conexion;

public class GeneracionNumeroExpediente implements TaskListener {

	private Conexion conn;
	private static Map<String, String> numexpedienteasignado = new HashMap<String, String>();

	@Override
	public void notify(DelegateTask arg0) {
		DelegateExecution execution = arg0.getExecution();
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String radicado = "";
		Date fechaRadicado = null;
		if (!numexpedienteasignado.containsKey(arg0.getProcessInstanceId())) {
			this.conn = new Conexion();
			String tmp = this.conn.obtieneRadicadoP();
			numexpedienteasignado.put(arg0.getProcessInstanceId(), tmp);
			String[] tmpA = numexpedienteasignado.get(arg0.getProcessInstanceId()).split(",");
			radicado = tmpA[0];
			try {
				fechaRadicado = formato.parse(tmpA[1]);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			execution.setVariable("expediente", radicado);
			execution.setVariable("fechaExpediente", fechaRadicado);
			numexpedienteasignado.remove(arg0.getProcessInstanceId());
			arg0.setVariable("qswfa_numexpediente", radicado);
		} else {
			//
		}
	}

}