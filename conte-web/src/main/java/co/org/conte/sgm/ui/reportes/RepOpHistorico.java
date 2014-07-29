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
public class RepOpHistorico extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepOpHistorico(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'Radicado', "
                + "DATE_FORMAT(s.fecha_radicacion,'%d-%m-%Y') AS 'Fecha Radicado', "
                + "UPPER(t.nombres) AS 'Nombres', "
                + "CONCAT_WS(' ', UPPER(t.primer_apellido), UPPER(t.segundo_apellido)) AS 'Apellidos',"
                + "sr.documento AS 'Documento', r.codigo AS 'Resolucion', "
                + "DATE_FORMAT(r.fecha,'%d-%m-%Y') AS 'Fecha Resolucion', t.matricula AS 'Matricula', "
                + "DATE_FORMAT(t.fecha_expedicion,'%d-%m-%Y') AS 'Fecha Matricula',"
                + "(SELECT GROUP_CONCAT(DISTINCT (LEFT(cg.`nombre`, 4)) SEPARATOR ' ') "
                + "FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`documento` = t.documento) AS 'Clases', "
                + "s.acta AS 'Acta', ts.nombre AS 'Tramite' "
                + "FROM `resolucion` r "
                + "INNER JOIN `solicitud` s ON s.radicado = r.c_solicitud "
                + "INNER JOIN tecnico t ON t.codigo = s.c_tecnico "
                + "INNER JOIN `solicitud_radicado` sr ON sr.documento = t.documento "
                + "INNER JOIN `tipo_solicitud` ts ON ts.codigo = s.c_tipo "
                + "WHERE !ISNULL(s.acta)";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("Radicado", "Radicado", 40);
        crearCampo("Fecha Radicado", "Fecha Radicado", 80);
        crearCampo("Nombres", "Nombres", 80);
        crearCampo("Apellidos", "Apellidos", 80);
        crearCampo("Documento", "Documento", 80);
        crearCampo("Resolucion", "Resolucion", 80);
        crearCampo("Fecha Resolucion", "Fecha Resolucion", 80);
        crearCampo("Matricula", "Matricula", 80);
        crearCampo("Fecha Matricula", "Fecha Matricula", 80);
        crearCampo("Clases", "Clases", 80);
        crearCampo("Acta", "Acta", 80);
        crearCampo("Tramite", "Tramite", 80);
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
        reporte.setTitulo("Historico");
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
