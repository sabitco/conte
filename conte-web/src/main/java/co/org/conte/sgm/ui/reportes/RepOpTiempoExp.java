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
public class RepOpTiempoExp extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepOpTiempoExp(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'Radicado', "
                + "DATE_FORMAT(s.fecha_radicacion,'%d-%m-%Y') AS 'Fecha Radicado', "
                + "UPPER(t.nombres) AS 'Nombres', "
                + "CONCAT_WS(' ', UPPER(t.`primer_apellido`), UPPER(t.`segundo_apellido`)) AS 'Apellidos',"
                + "t.`documento` AS 'Documento', IFNULL(a.sigla,'') AS 'Asociacion',"
                + "c.nombre AS 'Ciudad', ts.nombre AS 'Tipo Tramite', "
                + "t.`matricula` AS 'Matricula', "
                + "DATE_FORMAT(t.`fecha_expedicion`,'%d-%m-%Y') AS 'Fecha Matricula',"
                + "DATE_FORMAT(n.envio,'%d-%m-%Y') AS 'Fecha Envio', "
                + "DATE_FORMAT(n.recepcion,'%d-%m-%Y') AS 'Fecha Recepcion',"
                + "IFNULL(av.tipo, '') AS 'Aviso', "
                + "DATEDIFF(n.envio,s.fecha_radicacion) AS 'Dias Tramitados'"
                + "FROM `solicitud_radicado` sr "
                + "INNER JOIN tecnico t ON t.`documento` = sr.`documento` "
                + "INNER JOIN `solicitud` s ON s.`c_tecnico` = t.`codigo` "                
                + "LEFT JOIN `asociacion` a ON a.codigo = s.`c_asociacion` "
                + "INNER JOIN ciudad c ON c.`codigo` = t.`c_ciudad` "
                + "INNER JOIN `tipo_solicitud` ts ON ts.`codigo` = s.`c_tipo` "
                + "LEFT JOIN notificacion n ON n.solicitud_id = s.radicado "
                + "LEFT JOIN aviso av ON av.`solicitud_id` = s.`radicado`";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("Radicado", "Radicado", 40);
        crearCampo("Fecha Radicado", "Fecha Radicado", 80);
        crearCampo("Nombres", "Nombres", 80);
        crearCampo("Apellidos", "Apellidos", 80);
        crearCampo("Documento", "Documento", 80);
        crearCampo("Asociacion", "Asociacion", 80);
        crearCampo("Ciudad", "Ciudad", 80);
        crearCampo("Tipo Tramite", "Tramite", 80);
        crearCampo("Matricula", "Matricula", 80);
        crearCampo("Fecha Matricula", "Fecha Matricula", 80);
        crearCampo("Fecha Envio", "Fecha Envio", 80);
        crearCampo("Fecha Recepcion", "Fecha Recepcion", 80);
        crearCampo("Aviso", "Aviso", 80);
        crearCampo("Dias Tramitados", "Dias tramitados", 80);
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
        this.addComponent(obtenerReporte(this.getApplication()));
    }

    private QueryContainer obtenerReporte(Application app) {
        ReportService reporte = new ReportService();
        reporte.setTitulo("Tiempo");
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
