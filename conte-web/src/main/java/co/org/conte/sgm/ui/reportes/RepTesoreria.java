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
public class RepTesoreria extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepTesoreria(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'radicado', CAST(s.fecha_radicacion AS DATE) AS 'fecha', "
                + "UPPER(t.nombres) AS 'nombres', CONCAT(UPPER(t.primer_apellido), ' ', UPPER(t.segundo_apellido)) AS 'apellidos', "
                + "t.documento AS 'documento', "
                + "a.sigla AS 'asociacion', i.codigo AS 'inspector', "
                + "s.consejero AS 'consejero', "
                + "s.acta AS 'nro_acta', pg.valor AS 'vr_consignado', "
                + "ts.nombre AS 'tramite' "
                + "FROM solicitud s INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                + "LEFT JOIN inspector i ON s.c_inspector = i.codigo "
                + "LEFT JOIN asociacion a ON s.c_asociacion = a.codigo "                
                + "INNER JOIN pago pg ON s.radicado = pg.`c_solicitud`  "
                + "INNER JOIN tipo_solicitud ts ON s.c_tipo = ts.codigo "
                + "INNER JOIN `solicitud_radicado` sr ON t.`documento` = sr.`documento` "
                + "WHERE !ISNULL(s.`fecha_creacion`) "
                + "ORDER BY radicado DESC";
    }

    private void inicializarCamposQuery() {
        crearCampo("radicado", "Radicado", 40);
        crearCampo("fecha", "Fecha", 80);
        crearCampo("nombres", "Nombres", 80);
        crearCampo("apellidos", "Apellidos", 80);
        crearCampo("documento", "Documento", 80);
        crearCampo("asociacion", "Asociacion", 80);
        crearCampo("inspector", "Inspector", 80);
        crearCampo("consejero", "Consejero", 80);
        crearCampo("nro_acta", "No. Acta", 80);
        crearCampo("vr_consignado", "Vr. consignado", 80);
        crearCampo("tramite", "Tramite", 80);
    }

    public void crearCampo(String nombreCampo, String rotulo, int tam) {
        ConfigCampoVO cfgCampo;
        cfgCampo = new ConfigCampoVO();
        cfgCampo.setCampo(nombreCampo);
        cfgCampo.setTitulo(rotulo);
        cfgCampo.setTamano(tam);
        configCampos.add(cfgCampo);
    }
    
    @Override
    public void attach() {
        this.addComponent(contratosDerivados(this.getApplication()));
    }

    private QueryContainer contratosDerivados(Application app) {
        ReportService reporte = new ReportService();
        reporte.setTitulo("Tesoreria");
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
