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
public class RepGestionGeneral extends VerticalLayout {

    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public RepGestionGeneral(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
//        query = "select sr.radicado as 'Radicado', "
//                + "date_format(s.`fecha_radicacion`,'%d-%m-%Y') as 'Fecha Radicacion', "
//                + "t.nombres as 'Nombres', "
//                + "concat_ws(' ', t.primer_apellido, t.segundo_apellido) as 'Apellidos', "
//                + "t.documento as 'Documento', c.nombre as 'Ciudad', s.`acta` as 'Acta', "
//                + "s.`consejero` as 'Consejero', "
//                + "( "
//                + "SELECT GROUP_CONCAT(DISTINCT (LEFT(cg.`nombre`, 4))  SEPARATOR ' ') FROM `evaluacion` e "
//                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
//                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
//                + "WHERE e.`documento` = t.documento "
//                + ") AS 'clases', "
//                + "i.codigo as 'Inspector', a.sigla as 'Asociacion', "
//                + "ts.nombre as 'Tramite' "
//                + "from `solicitud_radicado` sr "
//                + "inner join tecnico t on t.`documento` = sr.`documento` "
//                + "inner join ciudad c on c.`codigo` = t.`c_ciudad` "
//                + "inner join solicitud s on s.`c_tecnico` = t.`codigo` "
//                + "left join `asociacion` a on s.c_asociacion = a.`codigo` "
//                + "left join `inspector` i on i.`codigo` = s.`c_inspector` "
//                + "inner join `tipo_solicitud` ts on ts.codigo = s.c_tipo "
//                + "where !isnull(s.`fecha_creacion`)";
       
        query = "SELECT sr.radicado as 'radicado', DATE_FORMAT(s.fecha_radicacion,'%d-%m-%Y') AS 'fecha', "
                + "UPPER(t.nombres) as 'nombres', CONCAT(UPPER(t.primer_apellido), ' ', UPPER(t.segundo_apellido)) AS 'apellidos', "
                + "t.documento as 'documento', UPPER(t.direccion) as 'direccion', "
                + "c.nombre as 'ciudad', t.telefono as 'telefono', "
                + "t.`matricula` AS 'matricula', DATE_FORMAT(t.`fecha_expedicion`,'%d-%m-%Y') AS 'fecha_matricula', "
                + "r.`codigo` as 'resolucion',  DATE_FORMAT(r.`fecha`,'%d-%m-%Y') AS 'fecha_resolucion', "
                + "t.email as 'email', i.codigo as 'inspector', "
                + "a.sigla as 'asociacion', ts.nombre as 'tramite', "
                + "("
                + "SELECT GROUP_CONCAT(DISTINCT (LEFT(cg.`nombre`, 4))  SEPARATOR ' ') FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`documento` = t.documento"
                + ") AS 'clases', "
                + "e.nombre as 'estado' FROM solicitud s "
                + "INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                + "LEFT JOIN ciudad c ON t.c_ciudad = c.codigo "
                + "LEFT JOIN inspector i ON s.c_inspector = i.codigo "
                + "LEFT JOIN asociacion a ON s.c_asociacion = a.codigo "
                + "LEFT JOIN tipo_solicitud ts ON s.c_tipo = ts.codigo "
                + "LEFT JOIN estado e ON s.c_estado = e.codigo "
                + "INNER JOIN `solicitud_radicado` sr on t.`documento` = sr.`documento` "
                + "LEFT JOIN `resolucion` r ON r.`c_solicitud` = s.`radicado` "
                + "WHERE !ISNULL(s.fecha_creacion) ORDER BY 1 DESC";                
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("radicado", "Radicado",40);
        crearCampo("fecha", "Fecha Radicado",80);
        crearCampo("nombres", "Nombres",80);
        crearCampo("apellidos", "Apellidos",80);
        crearCampo("documento", "Documento",80);
        crearCampo("direccion", "Direccion",80);
        crearCampo("ciudad", "Ciudad",80);
        crearCampo("telefono", "Telefono",80);
        crearCampo("clases", "Clases",80);
        crearCampo("matricula", "Matricula",80);
        crearCampo("fecha_matricula", "Fecha Expedicion",80);
        crearCampo("resolucion", "Resolucion",80);
        crearCampo("fecha_resolucion", "Fecha Resolucion",80);
        crearCampo("email", "Email",80);
        crearCampo("inspector", "Inspector",80);
        crearCampo("asociacion", "Asociacion",80);
        crearCampo("tramite", "Tramite",80);
        crearCampo("estado", "Estado", 80);
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
        reporte.setTitulo("Gestion General");
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
