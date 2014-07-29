package com.quasarbi.TaskListener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ActualizaConsejero implements TaskListener {

    @Override
    public void notify(DelegateTask arg0) {
        try {
            System.out.println("Se asigna null a bpm_assignee ");
            arg0.getExecution().setVariable("bpm_assignee", null);
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error al asigar NULL a bpm_assignee");
            e.printStackTrace();
            System.out.println(e.getMessage() + " FIN del error al asignar NULL a bpm_assignee");
        }
    }
}
