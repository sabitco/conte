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
public class RepOrganizarPaquetes extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepOrganizarPaquetes(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT UPPER(t.nombres) AS 'nombres', CONCAT(UPPER(t.primer_apellido), ' ', UPPER(t.segundo_apellido)) AS 'apellidos', "
                +"t.documento AS 'documento', t.`email` as 'email', UPPER(t.`direccion`) as 'direccion', t.`celular` as 'telefono', "
                +"cu.`nombre` as 'ciudad', dp.`nombre` as 'departamento', "
                +"ts.nombre AS 'tramite', s.acta AS 'acta', sr.matricula AS 'matricula', rs.`codigo` AS 'resolucion', DATE_FORMAT(rs.fecha,'%d-%m-%Y') AS 'fecha_resolucion', "
                +"IFNULL (aso.sigla, '\\N') AS 'asociacion' ,"
                +"s.`entero` AS 'entero', "
                +"DATE_FORMAT(sr.fecha,'%d-%m-%Y') AS 'fecha_radicacion', "
                +"sr.radicado as 'radicado' "
                +"FROM solicitud s "
                +"INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                +"INNER JOIN tipo_solicitud ts ON s.c_tipo = ts.codigo "
                +"LEFT JOIN solicitud_formato sf ON s.radicado = sf.c_solicitud "
                +"INNER JOIN `solicitud_radicado` sr ON t.`documento` = sr.`documento` "
                +"LEFT JOIN resolucion rs ON s.radicado = rs.c_solicitud "
                +"INNER JOIN ciudad cu ON t.`c_ciudad` = cu.`codigo` "
                +"INNER JOIN `departamento` dp ON cu.`c_depto`= dp.`codigo` "
                +"LEFT JOIN asociacion aso ON  s.`c_asociacion` = aso.`codigo` "
                +"WHERE !ISNULL(s.acta);";
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("nombres", "Nombres", 80);
        crearCampo("apellidos", "Apellidos", 80);
        crearCampo("documento", "Documento", 80);        
        crearCampo("email", "Email", 80);
        crearCampo("direccion", "Direccion", 80);
        crearCampo("telefono", "Telefono", 80);
        crearCampo("ciudad", "Ciudad", 80);
        crearCampo("departamento", "Departamento", 80);
        crearCampo("tramite", "Tramite", 80);
        crearCampo("acta", "Acta", 80);
        crearCampo("matricula", "Matricula", 80);
        crearCampo("resolucion", "Resolucion", 80);
        crearCampo("fecha_resolucion", "Fecha resolucion", 80);
        crearCampo("asociacion", "Asociacion", 80);
        crearCampo("entero", "Entero", 80);
        crearCampo("fecha_radicacion", "Fecha Radicacion", 80);
        crearCampo("radicado", "Radicado", 80);
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
        reporte.setTitulo("Organizar Paquetes");
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
