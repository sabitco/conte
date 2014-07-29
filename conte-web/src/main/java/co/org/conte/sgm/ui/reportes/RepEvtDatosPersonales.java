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
public class RepEvtDatosPersonales extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepEvtDatosPersonales(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT sr.radicado AS 'Radicado', "
                + "DATE_FORMAT(s.fecha_radicacion,'%d-%m-%Y') AS 'Fecha Radicado', "
                + "t.nombres AS 'Nombres', "
                + "CONCAT_WS(' ', UPPER(t.`primer_apellido`), UPPER(t.`segundo_apellido`)) AS 'Apellidos', "
                + "t.`documento` AS 'Documento', UPPER(t.`direccion`) AS 'Direccion', "
                + "c.`nombre` AS 'Ciudad', t.`telefono` AS 'Telefono', "
                + "t.`email` AS 'E-Mail', "
                + "( "
                + "SELECT GROUP_CONCAT(DISTINCT (LEFT(cg.`nombre`, 4))  SEPARATOR ' ') FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`documento` = t.documento "
                + ") AS 'clases' "
                + "FROM `solicitud_radicado` sr "
                + "INNER JOIN tecnico t ON t.`documento` = sr.`documento` "
                + "INNER JOIN `solicitud` s ON s.`c_tecnico` = t.`codigo` "
                + "INNER JOIN ciudad c ON c.`codigo` = t.`c_ciudad` "
                + "WHERE !ISNULL(s.`acta`)";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("Radicado", "Radicado", 40);
        crearCampo("Fecha Radicado", "Fecha", 80);
        crearCampo("Nombres", "Nombres", 80);
        crearCampo("Apellidos", "Apellidos", 80);
        crearCampo("Documento", "Documento", 80);
        crearCampo("Direccion", "Direccion", 80);
        crearCampo("Ciudad", "Ciudad", 80);
        crearCampo("Telefono", "Telefono", 80);
        crearCampo("E-Mail", "Email", 80);
        crearCampo("clases", "Clases", 80);
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
        reporte.setTitulo("Datos personales");
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
