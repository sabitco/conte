package co.org.conte.sgm.ui.solicitud;

import co.org.conte.enumerator.EstadoInspector;
import co.org.conte.sgm.dao.AsociacionDao;
import co.org.conte.sgm.dao.DepartamentoDao;
import co.org.conte.sgm.dao.InspectorDao;
import co.org.conte.sgm.dao.ParametroDao;
import co.org.conte.sgm.dao.ReferenciaPersonalDao;
import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.TipoDocumentoDao;
import co.org.conte.sgm.dao.TipoSolicitudDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Asociacion;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Estado;
import co.org.conte.sgm.entity.Inspector;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.SolicitudService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.AlfrescoUtil;
import co.org.conte.sgm.util.HibernateUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Andrés Mise Olivera
 */
 public abstract class SolicitudForm extends GenericForm {
     
     protected TextField documento;
     protected NativeSelect tipoDocumento;
     
     protected ComboBox asociacion;
     protected HorizontalLayout buttons;
     protected TextField celular;
     protected TextField correo;
     
     protected ComboBox ciudad;
     protected ComboBox ciudadEmpresa;
     protected AsociacionDao daoAsociacion;
     protected DepartamentoDao daoDepartamento;
    protected InspectorDao daoInspector;
    protected ReferenciaPersonalDao daoReferenciaPersonal;
    protected SolicitudDao daoSolicitud;
    protected TecnicoDao daoTecnico;
    protected TipoDocumentoDao daoTipoDocumento;
    protected TipoSolicitudDao daoTipoSolicitud;
    protected UsuarioDao daoUsuario;
    
    protected ComboBox departamento;
    protected ComboBox departamentoEmpresa;
    protected NativeSelect dependiente;
    protected TextField direccionEmpresa;
    protected TextField direccionFamiliar;
    protected TextField direccionFamiliar2;
    protected TextField direccionResidencia;
    
    protected TextField documentoTecnico;
    protected boolean esEmpresa;
    protected TextField empresa;
    
    protected NativeSelect entero;
    protected TextField enteroPor;
    
    protected DateField fechaNacimiento;
    protected FormLayout formLayout = new FormLayout();
    
    protected ComboBox inspector;
    protected Link link;
    protected TextField nombres;
    protected TextField nombresFamiliar;    
    protected TextField nombresFamiliar2;
    protected NativeSelect notificacion;
    protected TextField primerApellido;
    protected TextField segundoApellido;
    
    protected SolicitudService serviceSolicitud;
    protected Solicitud solicitud;
    
    protected Tecnico tecnico;
    
    protected TextField telefono;
    protected TextField telefonoEmpresa;
    protected TextField telefonoFamiliar;
    protected TextField telefonoFamiliar2;
    
    
    protected ComboBox tipoDocumentoTecnico;
    protected NativeSelect tipoSolicitud;
    
    protected Button validate;
    
    protected String si = "Si";
    protected String no = "NO";
    
    public SolicitudForm(Usuario usuario){
        super(usuario);
         try {
             link = new Link();
             serviceSolicitud = new SolicitudService();
             daoUsuario = new UsuarioDao();
             daoAsociacion = new AsociacionDao();
             this.usuario = daoUsuario.findById(usuario.getCodigo());
             init();
         } catch (DaoException ex) {
             Logger.getLogger(SolicitudForm.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    @Override
    protected void addListeners() {
                
        this.departamento.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamento.getValue();
                selected = serviceUsuario.findDepartamentoById(selected.getCodigo());
                ciudad.removeAllItems();
                ciudad.setValue(null);
                if(!selected.getCiudads().isEmpty()){
                    for(Ciudad c : selected.getCiudads()){
                        ciudad.addItem(c);
                        ciudad.setItemCaption(c, c.getNombre());
                        
                    }
                }
             }
        });
        
        this.departamentoEmpresa.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamentoEmpresa.getValue();
                selected = serviceUsuario.findDepartamentoById(selected.getCodigo());
                ciudadEmpresa.removeAllItems();
                ciudadEmpresa.setValue(null);
                
                if(!selected.getCiudads().isEmpty()){
                    for(Ciudad c : selected.getCiudads()){
                        ciudadEmpresa.addItem(c);
                        ciudadEmpresa.setItemCaption(c, c.getNombre());
                        
                    }
                }
             }
        });
        
        entero.setImmediate(true);
        enteroPor.setVisible(false);
        inspector.setVisible(false);
        asociacion.setVisible(false);
        this.entero.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(getValue(entero).equalsIgnoreCase("Asociación")){
                    enteroPor.setVisible(false);
                    asociacion.setVisible(true);
                    inspector.setVisible(true);
                } else {
                    asociacion.setVisible(false);
                    inspector.setVisible(false);
                    
                    if(getValue(entero).equalsIgnoreCase("Conte")){
                        enteroPor.setVisible(false);
                    } else {
                        enteroPor.setVisible(true);    
                    }
                }
             }
        });
        
        asociacion.setImmediate(true);
        asociacion.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                inspector.removeAllItems();
                Asociacion a = (Asociacion) asociacion.getValue();                
                try {
                    if(a!=null){
                        a = daoAsociacion.findById(a.getCodigo());
                    }
                } catch (DaoException ex) {
                    Logger.getLogger(SolicitudForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(a!=null){
                    for(Inspector i : a.getInspectors()){
                        if (EstadoInspector.ACTIVO == i.getEstadoInspector()) {
                            inspector.addItem(i);
                            inspector.setItemCaption(i, i.getNombres() + " " + i.getApellidos());
                        }
                    }
                }
             }
        });
        
        dependiente.setImmediate(true);
        dependiente.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(getValue(dependiente).equalsIgnoreCase(si)){
                    ciudadEmpresa.setVisible(true);
                    departamentoEmpresa.setVisible(true);
                    direccionEmpresa.setVisible(true);
                    empresa.setVisible(true);
                    telefonoEmpresa.setVisible(true);
                    
                } else {
                    ciudadEmpresa.setVisible(false);
                    departamentoEmpresa.setVisible(false);
                    direccionEmpresa.setVisible(false);
                    empresa.setVisible(false);
                    telefonoEmpresa.setVisible(false);
                }
             }
        });
    }
    
    @Override
    protected void addValidators() {
        this.correo.addValidator(new EmailValidator("El campo no es correctos"));
    }

    @Override
    protected void setRequired(boolean required) {
        celular.setRequired(required);
        ciudad.setRequired(required);
        correo.setRequired(required);
        departamento.setRequired(required);
        dependiente.setRequired(required);        
        direccionResidencia.setRequired(required);
        documento.setRequired(required);
        entero.setRequired(required);
        fechaNacimiento.setRequired(required);
        nombres.setRequired(required);
        nombresFamiliar.setRequired(required);
        notificacion.setRequired(required);
        primerApellido.setRequired(required);
        telefono.setRequired(required);
        telefonoFamiliar.setRequired(required);
        tipoSolicitud.setRequired(required);
        if(esEmpresa){
            tipoDocumentoTecnico.setRequired(required);
            documentoTecnico.setRequired(required);
        }
    }

    @Override
    protected void validate() throws InvalidValueException {
        celular.validate();
        ciudad.validate();
        correo.validate();
        departamento.validate();
        departamentoEmpresa.validate();
        direccionResidencia.validate();
        documento.validate();
        empresa.validate();
        entero.validate();
        inspector.validate();
        nombres.validate();
        nombresFamiliar.validate();
        notificacion.validate();
        primerApellido.validate();
        telefono.validate();
        telefonoFamiliar.validate();
        if(esEmpresa){
            tipoDocumentoTecnico.validate();
            documentoTecnico.validate();
        }
    }
    
    protected Component createInformationSection() {
            
        Panel panel = new Panel("Datos Personales");

        Form f = new Form();
        f.setImmediate(true);
        f.addField("nombres", this.nombres);
        f.addField("primerApellido", this.primerApellido);
        f.addField("segundoApellido",this.segundoApellido);
        f.addField("Fecha de Nacimiento", this.fechaNacimiento);
        f.addField("direccionResidencia",direccionResidencia);
        f.addField("dependiente", dependiente);
        f.addField("notificacion", notificacion);
        f.addField("correo",correo);
        f.addField("departamento",departamento);
        f.addField("ciudad",ciudad);
        f.addField("celular",celular);
        f.addField("",telefono);
        f.addField("",empresa);
        f.addField("",direccionEmpresa);
        f.addField("departamentoEmpresa", departamentoEmpresa);
        f.addField("ciudadEmpresa", ciudadEmpresa);
        f.addField("telefonoEmpresa", telefonoEmpresa);
        panel.addComponent(f);
        return panel;
    }
    
    protected Component createFamilyInformationSection() {
        Panel panel = new Panel("Referencia Personal");

        Form f = new Form();

        f.addField("nombresFamiliar",nombresFamiliar);
        f.addField("direccionFamiliar",direccionFamiliar);
        f.addField("telefonoFamiliar",telefonoFamiliar);
        f.addField("nombresFamiliar2", nombresFamiliar2);
        f.addField("direccionFamiliar2", direccionFamiliar2);
        f.addField("telefonoFamiliar2", telefonoFamiliar2);

        panel.addComponent(f);
        
        return panel;
    }

    protected Component createAdditionalInformationSection(){

        Panel panel = new Panel("Información Complementaria");
        Form f = new Form();
        f.addField("entero",this.entero);
        f.addField("enteroPor",this.enteroPor);
        f.addField("asociacion", this.asociacion);
        f.addField("inspector", this.inspector);
        panel.addComponent(f);
        return panel;
    }

    protected Component createButtons(){
        Panel panel = new Panel ();  
        reset.setCaption("Volver");
        submit.setCaption("Guardar");
        buttons = new HorizontalLayout();
        buttons.addComponent(this.reset);
        buttons.addComponent(this.submit);
        buttons.addComponent(this.link);
        
        panel.addComponent(buttons);
        return panel;
    }
    
    private void init(){
        createFields();
        direccionEmpresa.setWidth("300px");
        direccionResidencia = new TextField("Direccion Residencia",
                (usuario.getDireccion()==null || usuario.getTipoDocumento().equalsIgnoreCase("NIT")) 
                ? "" : usuario.getDireccion()) ;
        direccionResidencia.setWidth("300px");
        documento = new TextField("Documento", usuario.getDocumento());
        tipoDocumento = new NativeSelect("Tipo Documento");
        
        asociacion = new ComboBox("Asociación");
        correo = new TextField("Correo", "");
        correo.setMaxLength(200);
        correo.setWidth("300px");
        ciudad = new ComboBox("Ciudad");
        dependiente = new NativeSelect("Dependiente");
        enteroPor = new TextField("Cual");        
        fechaNacimiento = new DateField("Fecha de Nacimiento");
        fechaNacimiento.setResolution(DateField.RESOLUTION_DAY);        
        fechaNacimiento.setDateFormat("dd/MM/yyyy"); 
        
        inspector = new ComboBox("Inspector");
        
        //CAMPOS DE REFERENCIAS        
        nombresFamiliar = new TextField("Nombres Referencia 1", "");  
        telefonoFamiliar = new TextField("Telefono Referencia 1", "");
        direccionFamiliar = new TextField("Direccion Referencia 1", "");
        direccionFamiliar.setWidth("300px");
        nombresFamiliar2 = new TextField("Nombres Referencia 2:");
        telefonoFamiliar2 = new TextField("Telefono Referencia 2", "");
        direccionFamiliar2 = new TextField("Direccion Referencia 2", "");
        direccionFamiliar2.setWidth("300px");
        List<Asociacion> asociaciones = serviceSolicitud.getAsocioaciones();
        if(asociaciones != null){
            if(!asociaciones.isEmpty()){
                for(Asociacion a : asociaciones){
                    asociacion.addItem(a);
                    asociacion.setItemCaption(a, a.getSigla());
                }
            }
        }
        dependiente.addItem(si);
        dependiente.addItem(no);
        
        tipoSolicitud = new NativeSelect ("Tipo de Solicitud");
        
        documento.setReadOnly(true);
        
        solicitud = new Solicitud();
        
        setMaxLength();
        setDescriptions();
    }
    
    protected void initPublic(){
        init();
    }
       
    protected Link generateReport(Tecnico tecnico, Long codSolicitud){
        final Connection con = HibernateUtil.getConnection();
        final HashMap map = new HashMap();
        map.put("id", tecnico.getCodigo());
        map.put("codSolicitud", codSolicitud);
        try {
            StreamResource.StreamSource source = new StreamResource.StreamSource() {     
                @Override
                public InputStream getStream() {
                    byte[] b = null;
                    try {
                        if(usuario.getTipoDocumento().equalsIgnoreCase("NIT")){
                            
                            System.out.print("*************"+getServletContext().getRealPath("resources/ConteSolicitudEmpresa.jasper"));
                            b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/ConteSolicitudEmpresa.jasper"), map, con);                            
                            
                        } else {
                            b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/ConteSolicitud.jasper"), map, con);                            
                        }
                        
                    } catch (Exception ex) {
                        Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return new ByteArrayInputStream(b);
                }
            };
            Double r = (Math.random()*1000);
            StreamResource resource = new StreamResource(source, 
                    tecnico.getDocumento() + "_" +  
                    new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".pdf", getApplication());


            resource.getStream().setParameter("Content-Type", "application/x-unknown");
            
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
    protected void setMaxLength(){
        celular.setMaxLength(60);        
        correo.setMaxLength(60);
        direccionEmpresa.setMaxLength(60);
        direccionFamiliar.setMaxLength(45);
        direccionFamiliar.setMaxLength(45);
        direccionResidencia.setMaxLength(60);
        telefonoFamiliar2.setMaxLength(20);
    }
    
    private void setDescriptions(){
        fechaNacimiento.setDescription(getMessage("tooltip.date"));        
    }
    
    protected String startWorkflow(long codigoSolicitud){
        String u = null;
        ParametroDao parametroDao = new ParametroDao();
        u = parametroDao.getParameter("ip");
       
        System.out.println("valor de u " + u);
        String ticket = AlfrescoUtil.getTicket();
        System.out.println("EL ESTADO DE LA SOLICITUD ES " + solicitud.getEstado().getCodigo());
        
        if(ticket==null || solicitud.getEstado().getCodigo()!=1){
            System.out.println("YA SE ENVIO Y NO SE VOLVIO A ENVIAR :P");
            return null;
        } else {              
            try {
                String aso = "";
                String ins = "";
                String flujo = "";
                String apellidos = tecnico.getPrimerApellido() + " " + tecnico.getSegundoApellido();
                switch(((TipoSolicitud)tipoSolicitud.getValue()).getCodigo()){
                    case 1:
                        flujo = "radicacion?definition=activiti$radicacion";
                        break;
                        
                    case 2:
                        flujo = "duplicados?definition=activiti$gestionDuplicados";
                        break;
                        
                    case 3:
                        flujo = "duplicados?definition=activiti$gestionDuplicados";
                        break;
                        
                    case 4:
                        flujo = "duplicados?definition=activiti$gestionDuplicados";
                        break;
                    
                    case 5:
                        flujo = "radicacion?definition=activiti$radicacion";
                        break;
                        
                    case 6:
                        flujo = "radicacion?definition=activiti$radicacion";
                        break;
                }
                if(((Asociacion)asociacion.getValue()) != null){
                    aso = ((Asociacion) asociacion.getValue()).getSigla() ;
                    ins = String.valueOf(solicitud.getInspector().getCodigo());
                }                 
                     
                URL url = new URL(u+"alfresco/service/workflow-instance-" + flujo        
                                +"&description=" + URLEncoder.encode(getValue(tipoSolicitud).toUpperCase() + " "+tecnico.getDocumento().toUpperCase() + " " + getValue(nombres).toUpperCase() + " " + getValue(primerApellido).toUpperCase() , "UTF-8")// "funciono%2044"
                                +"&assign=admin&"
                                +"&tiposolicitud="  + URLEncoder.encode(solicitud.getTipoSolicitud().getNombre(),"UTF-8")
                                +"&tipodocumento=" + URLEncoder.encode(getValue(tipoDocumento),"UTF-8")
                                +"&numerodocumento=" + URLEncoder.encode(tecnico.getDocumento(),"UTF-8")
                                +"&nombres=" + URLEncoder.encode(tecnico.getNombres().toUpperCase(),"UTF-8")
                                +"&apellidos=" + URLEncoder.encode(apellidos.trim().toUpperCase(),"UTF-8")
                                +"&fechaNacimiento=" + URLEncoder.encode(new SimpleDateFormat("dd-MM-yyyy").format(tecnico.getFechaNacimiento()),"UTF-8")
                                +"&direccionResidencia=" + URLEncoder.encode(tecnico.getDireccion(),"UTF-8") 				
                                +"&dependiente=" + (solicitud.getDependiente() ? "SI" : "NO")
                                +"&email=" + URLEncoder.encode(tecnico.getEmail(),"UTF-8") 				
                                +"&departamento=" + URLEncoder.encode(tecnico.getCiudad().getDepartamento().getNombre(),"UTF-8")				
                                +"&ciudad=" + URLEncoder.encode(tecnico.getCiudad().getNombre(),"UTF-8") 				
                                +"&celular=" + URLEncoder.encode(tecnico.getCelular(),"UTF-8")				
                                +"&telefono=" + URLEncoder.encode(tecnico.getTelefono(),"UTF-8")
                                +"&notificacion=" + URLEncoder.encode(getValue(notificacion),"UTF-8")
                                +"&empresa=" + URLEncoder.encode((solicitud.getEmpresa()!=null) ? solicitud.getEmpresa() : "" ,"UTF-8")				
                                +"&direccionEmpresa=" + URLEncoder.encode(solicitud.getDireccionEmpresa() != null ? solicitud.getDireccionEmpresa() : "","UTF-8")				
                                +"&departamentoEmpresa=" + URLEncoder.encode(solicitud.getCiudad() != null ?
                                                            solicitud.getCiudad().getDepartamento().getNombre() : "","UTF-8")			
                                +"&ciudadEmpresa=" + URLEncoder.encode(solicitud.getCiudad() != null ?
                                                            solicitud.getCiudad().getNombre() : "","UTF-8")
                                +"&telefonoEmpresa=" + URLEncoder.encode(solicitud.getTelefonoEmpresa() != null ? solicitud.getTelefonoEmpresa() : "" ,"UTF-8")
                                +"&nombresReferencia1=" + URLEncoder.encode(getValue(nombresFamiliar),"UTF-8")				
                                +"&direccionReferencia1=" + URLEncoder.encode(getValue(direccionFamiliar),"UTF-8")
                                +"&telefonoReferencia1=" + URLEncoder.encode(getValue(telefonoFamiliar),"UTF-8")				
                                +"&nombresReferencia2=" + URLEncoder.encode(getValue(nombresFamiliar),"UTF-8")				
                                +"&direccionReferencia2=" + URLEncoder.encode(getValue(direccionFamiliar2),"UTF-8")				
                                +"&telefonoReferencia2=" + URLEncoder.encode(getValue(telefonoFamiliar2),"UTF-8")				
                                +"&tramitePor=" + URLEncoder.encode(getValue(entero),"UTF-8")				
                                +"&realizadoEn=" + URLEncoder.encode(getValue(enteroPor),"UTF-8")	
                                +"&esEmpresa=" + esEmpresa
                                +"&nit=" + usuario.getDocumento()
                                +"&numeroformulario=" + codigoSolicitud                                 
                                +"&asociacion=" + URLEncoder.encode(aso,"UTF-8")
                                +"&identificacionInspector=" + ins
                                +"&alf_ticket="+ ticket
                       );
                
                System.out.print("\n\n\n LA URL ES ===>" + url +"\n\n\n ");
                BufferedReader in = null;
                
                in = new BufferedReader(new InputStreamReader(
                        url.openStream()));
                            
                String inputLine;
                String inputText="";
                
                while ((inputLine = in.readLine()) != null) {
                    inputText = inputText + inputLine;
                }
                System.out.println("LA URL ES: " + url.toString());
                String t = inputText.replace("activiti$", "");
                in.close();
                
                solicitud.setEstado(new Estado(18));
                try {
                    daoSolicitud.update(solicitud);
                } catch (DaoException ex) {
                    Logger.getLogger(SolicitudForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return t;
                
            } catch (MalformedURLException me) {
                me.printStackTrace();
                System.out.println("URL erronea");
                return null;

            }       catch (IOException ex) {
                Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } 
    }
    
    private void createFields(){
        celular = new TextField("Celular");
        direccionEmpresa = new TextField("Dirección Empresa");        
        nombres = new TextField("Nombres");
    }
    
}