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
public class RepOpGeneralComiteTecnico extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepOpGeneralComiteTecnico(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'radicado', DATE_FORMAT(s.fecha_radicacion,'%d-%m-%Y') AS 'fecha', s.acta as acta,"
                + "UPPER(t.nombres) AS 'nombres', CONCAT(UPPER(t.primer_apellido), ' ', UPPER(t.segundo_apellido)) AS 'apellidos', "
                + "t.documento AS 'documento', s.acta AS 'acta', "
                + "s.consejero AS 'consejero', "
                + "("
                + "SELECT GROUP_CONCAT(DISTINCT (LEFT(cg.`nombre`, 4))  SEPARATOR ' ') FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`documento` = t.documento"
                + ") AS 'clases', "
                + "ts.nombre AS 'tramite', "
                + "DATE_FORMAT(rs.fecha,'%d-%m-%Y') AS 'fecha_resolucion', "
                + "sr.matricula AS 'matricula', "
                + "rs.codigo AS 'resolucion' "
                + "FROM solicitud s "
                + "INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                + "INNER JOIN tipo_solicitud ts ON s.c_tipo = ts.codigo "
                + "LEFT JOIN solicitud_formato sf ON s.radicado = sf.c_solicitud "
                + "INNER JOIN `solicitud_radicado` sr ON t.`documento` = sr.`documento` "
                + "LEFT JOIN resolucion rs on s.radicado = rs.c_solicitud "
                + "where !isnull(s.acta)";
        /**    
        query = "SELECT s.radicado AS 'radicado', s.fecha_radicacion AS 'fecha', "
                    + "t.nombres AS 'nombres', CONCAT(t.primer_apellido, ' ', t.segundo_apellido) AS 'apellidos', "
                    + "t.documento AS 'documento', s.acta AS 'acta', "
                    + "u.nombres AS 'consejero', cg.nombre AS 'clases', "
                    + "ts.nombre AS 'tramite' "
                    + "FROM solicitud s INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                    + "INNER JOIN tipo_solicitud ts ON s.c_tipo = ts.codigo "
                    + "LEFT JOIN solicitud_formato sf ON s.radicado = sf.c_solicitud "
                    + "LEFT JOIN usuario u ON sf.c_consejero = u.codigo "
                    + "LEFT JOIN clase_agregada ca ON sf.c_solicitud = ca.c_solicitud "
                    + "LEFT JOIN clase_generica cg ON ca.c_clase_generica = cg.codigo ";
                    **/
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("radicado", "Radicado", 40);
        crearCampo("fecha", "Fecha", 80);
        crearCampo("acta", "Acta", 80);        
        crearCampo("nombres", "Nombres", 80);
        crearCampo("apellidos", "Apellidos", 80);
        crearCampo("documento", "Documento", 80);
        crearCampo("consejero", "Consejero", 80);
        crearCampo("clases", "Clases", 80);
        crearCampo("tramite", "Tramite", 80);
        crearCampo("resolucion", "Numero resolucion", 80);
        crearCampo("fecha_resolucion", "Fecha Resolucion", 80);
        crearCampo("matricula", "Matricula", 80);        
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
        reporte.setTitulo("General Comite Tecnico");
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
