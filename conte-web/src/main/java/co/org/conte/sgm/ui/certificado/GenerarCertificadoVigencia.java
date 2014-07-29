package co.org.conte.sgm.ui.certificado;

import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.solicitud.CrearSolicitud;
import co.org.conte.sgm.util.HibernateUtil;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author J4M0
 */
public class GenerarCertificadoVigencia extends VerticalLayout{
    
    private TecnicoDao daoTecnico;
    private Button generar;
    private Label messege;
    private Usuario usuario;
    private Tecnico tecnico;
    private Link link;
    
    public GenerarCertificadoVigencia(Usuario usuario){
        daoTecnico = new TecnicoDao();
        this.usuario = usuario;
        generar = new Button("Generar");
        this.tecnico = new Tecnico();
        tecnico.setDocumento(usuario.getDocumento());
        try {
            List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);
            if(tecnicos.size()>0){
                tecnico = tecnicos.get(0);
            }
        } catch (DaoException ex) {
            Logger.getLogger(GenerarCertificadoVigencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        final String documento = this.usuario.getDocumento();
        this.generar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                    
                    link = generateReport(documento);
                    if(link!=null){
                        removeComponent(link);
                        addComponent(link);
                    }
                    
                }
            });
        if(tecnico.getMatricula() != null){
            if(!tecnico.getMatricula().isEmpty()){
                addComponent(generar);
            } else {
                messege = new Label("No tiene una matricula asociada");
                addComponent(messege);
            }       
        } else {
            messege = new Label("No tiene una matricula asociada");
            addComponent(messege);
        }            
    }
    
      public ServletContext getServletContext() {
        ApplicationContext ctx = getApplication().getContext();
        if (ctx == null) {
            return null;
        }
        final ServletContext sCtx = ((WebApplicationContext)ctx).getHttpSession().getServletContext();
        return sCtx;
    }
      
      
      protected Link generateReport(String documento){
        final Connection con = (Connection) HibernateUtil.getConnection();
        final HashMap map = new HashMap();
        map.put("documento", documento);
        try {
            StreamResource.StreamSource source = new StreamResource.StreamSource() {     
                @Override
                public InputStream getStream() {
                    byte[] b = null;
                    try {
                        b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/CertificadoVigencia.jasper"), map, con);                            
                    } catch (JRException ex) {
                        Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return new ByteArrayInputStream(b);
                }
            };
            Double r = (Math.random()*1000);
            StreamResource resource = new StreamResource(source, 
                    tecnico.getDocumento() + " "+ 
                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())+".pdf", getApplication());


            resource.getStream().setParameter("Content-Type", "application/x-unknown");
            
            //resource.setMIMEType("application/octet-stream");
            resource.setCacheTime(0);
            Link l = new Link("Descargar", resource);
            l.setTargetName("_blank");
            l.setResource(resource);
            return l;

        } catch (Exception ex) {
            Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return null;
    }
}
