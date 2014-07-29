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
public class RepGestionInadmitidos extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepGestionInadmitidos(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'Radicado', "
                + "DATE_FORMAT(s.`fecha_radicacion`,'%d-%m-%Y') AS 'Fecha Radicacion', "
                + "UPPER(t.nombres) AS 'Nombres', "
                + "CONCAT_WS(' ', UPPER(t.primer_apellido), UPPER(t.segundo_apellido)) AS 'Apellidos', "
                + "t.documento AS 'Documento', UPPER(t.direccion) AS 'Direccion', "
                + "c.nombre AS 'Ciudad', t.telefono AS 'Telefono', "
                + "t.`email` AS 'E-Mail', "
                + "IFNULL(a.sigla,'') AS 'Asociacion', i.codigo AS 'Inspector', "
                + "inad.causa, DATE_FORMAT(inad.`fecha_notificacion`,'%d-%m-%Y') AS 'Fecha Notificacion', "
                + "DATE_FORMAT(inad.`fecha_responde`,'%d-%m-%Y') AS 'Fecha Responde', "
                + "DATEDIFF(inad.`fecha_responde`, inad.`fecha_notificacion`) AS 'Dias Tramitados' "
                + "FROM solicitud_radicado sr "
                + "INNER JOIN `tecnico` t ON t.documento = sr.documento "
                + "INNER JOIN `solicitud` s ON s.c_tecnico = t.codigo "
                + "INNER JOIN ciudad c ON c.`codigo` = t.`c_ciudad` "
                + "LEFT JOIN `asociacion` a ON a.codigo = s.`c_asociacion` "
                + "LEFT JOIN `inspector` i ON i.`codigo` = s.`c_inspector` "
                + "INNER JOIN inadmitido inad ON inad.solicitud_id = s.radicado "
                + "WHERE !ISNULL(s.`fecha_creacion`)";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("Radicado", "Radicado", 40);
        crearCampo("Fecha Radicacion", "Fecha Radicado", 80);
        crearCampo("Nombres", "Nombres", 80);
        crearCampo("Apellidos", "Apellidos", 80);
        crearCampo("Documento", "Cedula", 80);
        crearCampo("Direccion", "Direccion", 80);
        crearCampo("Ciudad", "Ciudad", 80);
        crearCampo("Telefono", "Telefono", 80);
        crearCampo("E-Mail", "Email", 80);
        crearCampo("Inspector", "Inspector", 80);
        crearCampo("Asociacion", "Asociacion", 80);
        crearCampo("causa", "Causal inadmision", 80);
        crearCampo("Fecha Notificacion", "Fecha Notificacion", 80);
        crearCampo("Fecha Responde", "Fecha Responde", 80);
        crearCampo("Dias Tramitados", "Dias Tramitados", 80);
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
        reporte.setTitulo("Gestion Inadmitidos");
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
