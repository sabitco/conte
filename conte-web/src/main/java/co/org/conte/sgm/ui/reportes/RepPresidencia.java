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
public class RepPresidencia extends VerticalLayout {
    
    String query = "select * from usuario";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();
    
    public RepPresidencia(Usuario usuario){
        inicializarQuery();
        inicializarCamposQuery();
    }
    
    private void inicializarQuery() {
        query = "";
    }
    
    private void inicializarCamposQuery() {
        ConfigCampoVO cfgCampo;
        cfgCampo = new ConfigCampoVO();
        cfgCampo.setCampo("");
        cfgCampo.setTitulo("");
        cfgCampo.setTamano(40);
        configCampos.add(cfgCampo);
    }
    
    @Override
    public void attach() {
        this.addComponent(obtenerReporte(getApplication()));
    }

    private QueryContainer obtenerReporte(Application app) {
        ReportService reporte = new ReportService();
        reporte.setTitulo("Presidencia");
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
