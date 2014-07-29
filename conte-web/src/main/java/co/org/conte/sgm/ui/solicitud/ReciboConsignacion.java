package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.SolicitudService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.HibernateUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Link;
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
 * @author jam
 */
public final class ReciboConsignacion extends GenericForm{

    private ComboBox cantidad;
    private Button generar;
    private Link link;
    private SolicitudService serviceSolicitud;
    private ComboBox tipoSolicitud;
    
    public ReciboConsignacion(Usuario usuario) {        
        super(usuario);
        usuario = serviceUsuario.findById(usuario.getCodigo());
        setCaption("ReciboConsignacion");
        this.usuario = usuario;
        if(this.usuario.getTipoDocumento().equalsIgnoreCase("NIT")){
            serviceSolicitud = new SolicitudService();
            init();
        } else {
            removeAllComponents();
            generar = new Button("Generar");
            final Long codigo = this.usuario.getCodigo();
        }        
    }    
        
    protected Link generateReport(Long codigo, Integer codSolicitud, Integer cantidad){
        final Connection con = (Connection) HibernateUtil.getConnection();
        final HashMap map = new HashMap();
        map.put("codigo", codigo);
        map.put("codSolicitud", codSolicitud);
        map.put("cantidad", cantidad);
        try {
            StreamResource.StreamSource source = new StreamResource.StreamSource() {     
                @Override
                public InputStream getStream() {
                    byte[] b = null;
                    try {
//                        Sytem.out.print(
                        b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/Consignacion.jasper"), map, con);
                        
                    } catch (Exception ex) {
                        Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return new ByteArrayInputStream(b);
                }
            };
            Double r = (Math.random()*1000);
            StreamResource resource = new StreamResource(source, 
                    "recibo "+ new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())+".pdf", getApplication());


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
    
    public ServletContext getServletContext() {
        ApplicationContext ctx = getApplication().getContext();
        if (ctx == null) {
            return null;
        }
        final ServletContext sCtx = ((WebApplicationContext)ctx).getHttpSession().getServletContext();
        return sCtx;
    }

    @Override
    protected void addListeners() {
        this.tipoSolicitud.setImmediate(true);
        this.tipoSolicitud.addListener(new  Property.ValueChangeListener() {
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                TipoSolicitud td = (TipoSolicitud) tipoSolicitud.getValue();
                message.setCaption("");
                cantidad.setValue(null);
                cantidad.removeAllItems();
                if(td !=null){
                    int total = serviceSolicitud.getCantidadSolicitudes(usuario, td);
                    if(total>0){
                        submit.setEnabled(true);
                        for(int i=0; i<total; i++){
                            cantidad.addItem(i+1);
                        }
                    } else {
                        submit.setEnabled(false);
                        message.setCaption("NO tiene solicitudes de este tipo");
                        if(link != null){
                            removeComponent(link);
                        }
                    }
                }
             }
        });
        
    }

    @Override
    protected void addValidators() {
        
    }

    @Override
    protected void createForm() {        
        form.addField("tipoSolicitud", tipoSolicitud);
        form.addField("cantidad", cantidad);
        addListeners();
        setRequired(true);
    }

    @Override
    protected void onClickReset() {
        message.setCaption("");
        cantidad.removeAllItems();
        cantidad.setValue(null);
        tipoSolicitud.setValue(null);
    }

    @Override
    protected void process() {        
        if(link != null){
            removeComponent(link);
        }
        link = generateReport(usuario.getCodigo(), ((TipoSolicitud) tipoSolicitud.getValue()).getCodigo(), (Integer) cantidad.getValue()); 
        addComponent(link);
        launchSubwindow("Ya puede descargar el recibo");
    }

    @Override
    protected void setRequired(boolean required) {
        cantidad.setRequired(required);
        tipoSolicitud.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        cantidad.validate();
        generar.validate();
        tipoSolicitud.validate();
    }
    
    private void init(){
        cantidad = new ComboBox("Cantidad");        
        tipoSolicitud = new ComboBox("Solicitud");        
        List<TipoSolicitud> solicitudes = serviceSolicitud.getTipoSolicitud();
        for(TipoSolicitud td : solicitudes){
            tipoSolicitud.addItem(td);
            tipoSolicitud.setItemCaption(td, td.getNombre());
        }
        createForm();
    }

    @Override
    protected void setMaxLength() {
    }
}
