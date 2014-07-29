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
 * @author J4M0
 */
public class Presidencia  extends VerticalLayout {
    String query = "";
    private List<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();

    public Presidencia(Usuario usuario) {
        inicializarQuery();
        inicializarCamposQuery();
    }

    private void inicializarQuery() {
        query = "SELECT 'CONTE' AS 'de',SUM(IF(s.c_tipo = 1, 1, 0)) AS 'Primer vez', CAST(SUM(IF(s.c_tipo = 1, p.valor, 0)) AS DECIMAL) AS 'valor_primer_vez', "
                + "SUM(IF(s.c_tipo = 5, 1, 0)) AS 'Ampliacion', SUM(IF(s.c_tipo = 5, p.valor, 0)) AS 'valor_ampliacion', "
                + "SUM(IF(s.c_tipo = 6, 1, 0)) AS 'Licencia Especial', SUM(IF(s.c_tipo = 6, p.valor, 0)) AS 'valor_licencia_especial', "
                + "SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, 1, 0)) AS 'Duplicado Tarjeta', SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, p.valor, 0)) AS 'valor_duplicado_tarjeta', "
                + "SUM(IF(s.c_estado = 7, 1, 0)) AS 'Inadmitidos', SUM(IF(s.c_estado = 7, p.valor, 0)) AS 'valor_inadmitidos' "
                + "FROM solicitud AS s "
                + "LEFT JOIN pago p ON p.c_solicitud = s.radicado "
                + "WHERE s.`entero` = 'CONTE' AND !ISNULL(s.`fecha_creacion`) "
                + "UNION "
                + "SELECT 'Asociación',SUM(IF(s.c_tipo = 1, 1, 0)) AS 'Primer vez', SUM(IF(s.c_tipo = 1, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 5, 1, 0)) AS 'Ampliacion', SUM(IF(s.c_tipo = 5, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 6, 1, 0)) AS 'Licencia Especial', SUM(IF(s.c_tipo = 6, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, 1, 0)) AS 'Duplicado Tarjeta', SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_estado = 7, 1, 0)) AS 'Inadmitidos', SUM(IF(s.c_estado = 7, p.valor, 0)) AS 'Valor' "
                + "FROM solicitud AS s "
                + "LEFT JOIN pago p ON p.c_solicitud = s.radicado "
                + "WHERE s.`entero` = 'Asociación' AND !ISNULL(s.`fecha_creacion`) "
                + "UNION "
                + "SELECT 'Empresa',SUM(IF(s.c_tipo = 1, 1, 0)) AS 'Primer vez', SUM(IF(s.c_tipo = 1, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 5, 1, 0)) AS 'Ampliacion', SUM(IF(s.c_tipo = 5, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 6, 1, 0)) AS 'Licencia Especial', SUM(IF(s.c_tipo = 6, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, 1, 0)) AS 'Duplicado Tarjeta', SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_estado = 7, 1, 0)) AS 'Inadmitidos', SUM(IF(s.c_estado = 7, p.valor, 0)) AS 'Valor' "
                + "FROM solicitud AS s "
                + "LEFT JOIN pago p ON p.c_solicitud = s.radicado "
                + "WHERE s.`entero` = 'Empresa' AND !ISNULL(s.`fecha_creacion`) "
                + "UNION "
                + "SELECT 'Instituto de Formación Técnica',SUM(IF(s.c_tipo = 1, 1, 0)) AS 'Primer vez', SUM(IF(s.c_tipo = 1, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 5, 1, 0)) AS 'Ampliacion', SUM(IF(s.c_tipo = 5, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 6, 1, 0)) AS 'Licencia Especial', SUM(IF(s.c_tipo = 6, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, 1, 0)) AS 'Duplicado Tarjeta', SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_estado = 7, 1, 0)) AS 'Inadmitidos', SUM(IF(s.c_estado = 7, p.valor, 0)) AS 'Valor' "
                + "FROM solicitud AS s "
                + "LEFT JOIN pago p ON p.c_solicitud = s.radicado "
                + "WHERE s.`entero` = 'Instituto de Formación Técnica' AND !ISNULL(s.`fecha_creacion`) "
                + "UNION "
                + "SELECT 'TOTAL',SUM(IF(s.c_tipo = 1, 1, 0)) AS 'Primer vez', SUM(IF(s.c_tipo = 1, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 5, 1, 0)) AS 'Ampliacion', SUM(IF(s.c_tipo = 5, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 6, 1, 0)) AS 'Licencia Especial', SUM(IF(s.c_tipo = 6, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, 1, 0)) AS 'Duplicado Tarjeta', SUM(IF(s.c_tipo = 2 || s.c_tipo = 3 || s.c_tipo = 4, p.valor, 0)) AS 'Valor', "
                + "SUM(IF(s.c_estado = 7, 1, 0)) AS 'Inadmitidos', SUM(IF(s.c_estado = 7, p.valor, 0)) AS 'Valor' "
                + "FROM solicitud AS s "
                + "LEFT JOIN pago p ON p.c_solicitud = s.radicado "
                + "WHERE !ISNULL(s.`fecha_creacion`)";
        }

    private void inicializarCamposQuery() {
        configCampos.clear();
        crearCampo("de", "", 40);
        crearCampo("Primer vez", "Primer vez", 80);
        crearCampo("valor_primer_vez", "Valor", 80);
        crearCampo("Ampliacion", "Ampliacion", 80);
        crearCampo("valor_ampliacion", "Valor", 80);
        crearCampo("Licencia Especial", "Licencia Especial", 80);
        crearCampo("valor_licencia_especial", "Valor", 80);
        crearCampo("Duplicado Tarjeta", "Duplicado Tarjeta", 80);
        crearCampo("valor_duplicado_tarjeta", "Valor", 80);
        crearCampo("Inadmitidos", "Inadmitidos", 80);
        crearCampo("valor_inadmitidos", "Valor", 80);
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
        this.addComponent(obtenerReporte(this.getApplication()));
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