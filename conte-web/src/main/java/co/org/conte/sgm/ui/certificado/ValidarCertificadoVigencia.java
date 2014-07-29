package co.org.conte.sgm.ui.certificado;

import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.TipoDocumentoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.ui.solicitud.CrearSolicitud;
import co.org.conte.sgm.util.HibernateUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Andrés Mise Olivera
 */
public class ValidarCertificadoVigencia extends GenericForm{
    
    private TipoDocumentoDao daoTipoDocumento = new TipoDocumentoDao();
    private TecnicoDao daoTecnico = new TecnicoDao();
    
    private TextField documento = new TextField("Documento");
    private NativeSelect tipoDocumento;
    
    private HorizontalLayout buttons = new HorizontalLayout();
    private Button generar = new Button("Generar");
    private Link link;

    public ValidarCertificadoVigencia(Usuario usuario) {
        super(usuario);
        init();
    }
    
    private void init() {  
        documento.setMaxLength(20);
        createForm();
        buttons.addComponent(generar);
    }
    
    @Override
    protected void addListeners() {
        this.tipoDocumento.addListener(new Property.ValueChangeListener() {
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                documento.removeAllValidators();                
                documento.setImmediate(true);
                documento.setMaxLength(20);
                if(!getValue(tipoDocumento).equals("PAP")){
                    addValidators();
                }
             }
        });
        
        this.generar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(link!=null){
                    buttons.removeComponent(link);
                }
                link = generateReport(documento.getValue().toString());
                addComponent(link);
            }
        });
    }

    @Override
    protected void addValidators() {
        documento.setImmediate(true);
        documento.addValidator(new DoubleValidator("Solo puede ingresar números"));
        
    }

    @Override
    protected void createForm() {
        List<TipoDocumento> documentos = null;
        try {
            documentos = daoTipoDocumento.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(ValidarCertificadoVigencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        tipoDocumento = new NativeSelect ("Tipo de Documento");
        tipoDocumento.isImmediate();
        if(documentos!=null){
            for (TipoDocumento d:  documentos ) {
                tipoDocumento.addItem(d);
                tipoDocumento.setItemCaption(d, d.getNombre());
            }            
        }
        
        
        this.addListeners();
        this.setRequired(true);
        
        form.addField("tipoDocumento", this.tipoDocumento);
        form.addField("documento", this.documento);
    }

    @Override
    protected void onClickReset() {
        documento.removeAllValidators();
        documento.setValue("");
        if(this.components.contains(buttons)){
            removeComponent(buttons);
        }
    }

    @Override
    protected void process() {
        try{
            validate();
            Tecnico tecnico = new Tecnico();
            tecnico.setDocumento(getValue(this.documento));
            List<Tecnico> tecnicos =  daoTecnico.findBy(tecnico);
            if(tecnicos.size()>0){
                tecnico = tecnicos.get(0);
                
                
                if(tecnico.getMatricula() != null){
                    if(!tecnico.getMatricula().isEmpty()){
                        addComponent(generar);
                    } else {
                        message.setCaption("No tiene una matricula asociada");
                        addComponent(message);
                    }       
                } else {
                    message.setCaption("No tiene una matricula asociada");
                    addComponent(message);
                }    
                
                
            } else {
                message.setCaption("No se encuentra el número de Documento");
            }
        } catch (DaoException ex) {
            Logger.getLogger(ValidarCertificadoVigencia.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InvalidValueException ex){
            message.setCaption("Los valores de algunos campos no son validos");
        }
    }
    
    public void GenerarCertificadoVigencia(Tecnico tecnico){
        daoTecnico = new TecnicoDao();
        final String documento = getValue(this.documento);
        if(tecnico.getMatricula() != null){
            if(!tecnico.getMatricula().isEmpty()){
                addComponent(buttons);
            } else {
                message.setCaption("No tiene una matricula asociada");
            }       
        } else {
            message.setCaption("No tiene una matricula asociada");
        }            
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
                    documento + " "+ 
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
    
    protected Link generateReport(){
        String documento = getValue(this.documento);
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
                    getValue(this.documento) + " "+ 
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

    @Override
    protected void setRequired(boolean required) {
        documento.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        documento.validate();
    }   

    @Override
    protected void setMaxLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}