/*
 * Información de Valoración INFOVALMER, Bolsa de Valores de Colombia BVC
 * Copyright (C) 2012 Quasar Software Ltda.
 */
package co.org.conte.sgm.ui.reportes;

import co.org.conte.sgm.entity.Usuario;
import com.conte.reportEngine.ConfigCampoVO;
import com.conte.reportEngine.QueryContainer;
import com.conte.reportEngine.ReportService;
import com.vaadin.Application;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Valero
 */
public class RepPQRS extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepPQRS(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT pq.consecutivo AS 'consecutivo', 'fecha', "
                + "UPPER(pq.nombres) AS 'nombres', 'documento', "
                + "UPPER(pq.direccion) AS 'direccion', c.nombre AS 'ciudad', "
                + "pq.telefono AS 'telefono', pq.email AS 'email', "
                + "'dp_asignada', pq.solicitud AS 'tema_solicitud', "
                + "'fecha_respuesta', 'fecha_cierre' "
                + "FROM pqrs pq INNER JOIN ciudad c ON pq.c_ciudad = c.codigo";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("consecutivo", "Consecutivo PQRS", 40);
        crearCampo("fecha", "F. Recibido", 80);
        crearCampo("nombres", "Nombres", 80);
        crearCampo("documento", "Documento rte.", 80);
        crearCampo("direccion", "Direccion", 80);
        crearCampo("ciudad", "Ciudad", 80);
        crearCampo("telefono", "Telefono", 80);
        crearCampo("email", "Email", 80);
        crearCampo("dp_asignada", "Dep. asignada", 80);
        crearCampo("tema_solicitud", "Tema solicitud", 80);
        crearCampo("fecha_respuesta", "F. respuesta", 80);
        crearCampo("fecha_cierre", "F. cierre", 80);
    }

    public void crearCampo(String nombreCampo, String rotulo, int tam){
        ConfigCampoVO cfgCampo;
        cfgCampo = new ConfigCampoVO();
        cfgCampo.setCampo(nombreCampo);
        cfgCampo.setTitulo(rotulo);
        cfgCampo.setTamano(tam);
        configCampos.add(cfgCampo);
    }
    
    @Override
    public void attach() {
        this.addComponent(obtenerReporte(this.getApplication()));
    }

    private QueryContainer obtenerReporte(Application app) {
        ReportService reporte = new ReportService();
        reporte.setTitulo("PQRS");
        reporte.setFormatoSalida("Web20");
        reporte.setArchivoSalida("salida2." + "Web20".toLowerCase());
        reporte.setQuery(query);
        reporte.setConfigCampos(configCampos);
        if (app != null) {
            return reporte.creaConsultaWeb20(app, app.getMainWindow());
        } else {
            return reporte.creaConsultaWeb20(getApplication(), getApplication().getMainWindow());
        }
    }
}
