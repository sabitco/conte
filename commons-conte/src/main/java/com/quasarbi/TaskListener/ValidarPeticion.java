package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ValidarPeticion implements TaskListener{

	@Override
	public void notify(DelegateTask arg0) {
		// TODO Auto-generated method stub
		DelegateExecution execution = arg0.getExecution();
		String procesoASeguir = (String) arg0.getVariable("qswfa_ltipopeticion");
		if(!procesoASeguir.contains("Proceso")){
			execution.setVariable("peticionno", execution.getProcessInstanceId());
		}
	}

}
