package com.quasarbi.Constraints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;

import com.quasarbi.ConexionBD.Conexion;

public class ListadoCiudades extends ListOfValuesConstraint implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<String> allowedLabels = new ArrayList<String>();
    private Map<String, String> formatos;
    private final Conexion conn = new Conexion();

    @Override
    public void initialize() {
        super.setCaseSensitive(false);
        this.formatos = this.conn.obtenerLCiudades();
        Iterator<?> it = this.formatos.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes")
            Map.Entry e = (Map.Entry) it.next();
            System.out.println(e.getKey() + " " + e.getValue());
            allowedLabels.add((String) e.getKey());
        }
        System.out.println("Los valores obtenidos son: " + allowedLabels.size());
        super.setAllowedValues(allowedLabels);
    }

    @Override
    public String getDisplayLabel(String constraintAllowableValue) {
        return formatos.get(constraintAllowableValue);
    }

}
