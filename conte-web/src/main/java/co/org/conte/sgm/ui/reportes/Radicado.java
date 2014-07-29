package co.org.conte.sgm.ui.reportes;

import co.org.conte.sgm.entity.Usuario;
import com.conte.reportEngine.ConfigCampoVO;
import com.conte.reportEngine.QueryContainer;
import com.conte.reportEngine.ReportService;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;

/**
 *
 * @author J4M0
 */
public class Radicado extends GenericReport {
    
    private String query;
    
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public Radicado(Usuario usuario) {
        super(usuario);        
        
        inicializarCamposQuery();
    }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("radicado", "Radicado", 40);
        crearCampo("documento", "Documento", 80);
        crearCampo("nombres", "Nombres", 80);
        crearCampo("apellidos", "Apellidos", 80);
        //crearCampo("URL", "URL Repositorio", 80);
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
        super.attach();
        query = "SELECT sr.radicado as radicado , t.documento as documento, t.nombres, "
                + "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido) AS apellidos "
                //+ "CONCAT_WS('"+getURL()+"',' ',sr.radicado, ' ', t.documento, ' ', t.nombres, ' ', "
                //+ "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido)) AS URL "
                + "FROM `solicitud_radicado` sr "
                + "INNER JOIN `solicitud` s ON sr.solicitud_id = s.radicado "
                + "INNER JOIN tecnico t ON s.c_tecnico = t.codigo "
                + "ORDER BY 1 DESC";
        addComponent(obtenerReporte(getApplication()));
    }

    private QueryContainer obtenerReporte(Application app) {
        ReportService reporte = new ReportService();
        reporte.setTitulo("Radicado");
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
