/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author QUASAR
 */
public class RepOpRecursos extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepOpRecursos(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'Radicado', "
                + "DATE_FORMAT(s.`fecha_radicacion`,'%d-%m-%Y') AS 'Fecha Radicado', "
                + "UPPER(t.nombres) AS 'Nombres', "
                + "CONCAT_WS(' ', UPPER(t.`primer_apellido`), UPPER(t.`segundo_apellido`)) AS 'Apellidos', "
                + "t.`documento` AS 'Documento', s.`consejero` AS 'Consejero', "
                + "r.creado AS 'Fecha Recurso', s.`acta` AS 'Acta', "
                + "IFNULL(r.estado, '') AS 'Estado',  ts.nombre AS 'Tipo Tramite', "
                + "r.tipo AS 'Tipo Recurso' "
                + "FROM `solicitud_radicado` sr "
                + "INNER JOIN tecnico t ON t.`documento` = sr.`documento` "
                + "INNER JOIN `solicitud` s ON s.`c_tecnico` = t.`codigo` "
                + "INNER JOIN `tipo_solicitud` ts ON ts.`codigo` = s.`c_tipo` "
                + "INNER JOIN recurso r ON r.solicitud_id = s.radicado"; 
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("Radicado", "Radicado", 40);
        crearCampo("Fecha Radicado", "Fecha Radicado", 80);
        crearCampo("Nombres", "Nombres", 80);
        crearCampo("Apellidos", "Apellidos", 80);
        crearCampo("Documento", "Documento", 80);
        crearCampo("Consejero", "Consejero", 80);
        crearCampo("Fecha Recurso", "Fecha Recurso", 80);
        crearCampo("Acta", "Acta", 80);
        crearCampo("Estado", "Estado", 80);
        crearCampo("Tipo Tramite", "Tipo Tramite", 80);
        crearCampo("Tipo Recurso", "Tipo Recurso", 80);
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
        reporte.setTitulo("Recursos");
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
