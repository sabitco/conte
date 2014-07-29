package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.dao.ParametroDao;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.PqrsService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.AlfrescoUtil;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.CaptchaField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import javax.servlet.ServletContext;

/**
 *
 * @author Andrés Mise Olivera
 */
public class RegistrarPqrs extends GenericForm{

    private TextField apellidos;
    private CaptchaField captcha;
    private TextField captchaInput;
    private TextField celular;
    private TextField correo;
    private NativeSelect ciudad;
    private TextField documento;
    private NativeSelect departamento;
    private TextField direccion;
    private List<Departamento> departamentos;
    private TextField nombres;    
    private TextArea observaciones;
    private PqrsService service;
    private TextField telefono;
    private NativeSelect tipoDocumento;
    private NativeSelect tipo;
    private Upload upload;
    private Label label;
    private File file;
    private String rutaArchivo;
    
    private TextField razonSocial;
    private TextField cargo;
    private String inPath;
//    private UploadField  uploadField ;
    
    public RegistrarPqrs(Usuario usuario) {
        super(usuario);
        inPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
        label = new Label("");
        service = new PqrsService();
        rutaArchivo = "";
        init();
    }

    @Override
    public void attach() {
        super.attach();
        if(usuario ==  null){
            captcha = new CaptchaField(getApplication(), true, false, null);
            captchaInput = new TextField("Ingrese Captcha");
        }
        createForm();
        setRequired(true);
        setMaxLength();
        addValidators();
        addListeners();
    }

    @Override
    protected void addListeners() {
        departamento.setImmediate(true);
        this.departamento.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamento.getValue();
                ciudad.removeAllItems();
                ciudad.setValue(null);
                if(selected != null){
                    
                    selected = serviceUsuario.findDepartamentoById(selected.getCodigo());
                    if(!selected.getCiudads().isEmpty()){
                        for(Ciudad c : selected.getCiudads()){
                            ciudad.addItem(c);
                            ciudad.setItemCaption(c, c.getNombre());                            
                        }
                    }                    
                }
             }
        });
        
        this.tipoDocumento.setImmediate(true);
        this.tipoDocumento.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tipoDocumento.getValue()!=null){
                    documento.removeAllValidators();
                    tipoDocumento.setImmediate(true);
                    documento.setImmediate(true);
                    documento.setMaxLength(20);
                    if(getValue(tipoDocumento)!= null){
                        cargo.setVisible(false);
                        razonSocial.setVisible(false);
                        cargo.setRequired(false);
                        razonSocial.setRequired(false);
                        if(!tipoDocumento.getValue().toString().equalsIgnoreCase("PAP")){                            
                            documento.addValidator(new DoubleValidator("Solo puede ingresar números"));            
                        }

                        if(getValue(tipoDocumento).equals("NIT")){
                            cargo.setVisible(true);
                            razonSocial.setVisible(true);
                            cargo.setRequired(true);
                            razonSocial.setRequired(true);
                            documento.setDescription("No digite el numero de verificación");
                        } else {
                            documento.setDescription("");                  
                        }
                    }
                }
             }
        });
    }

    @Override
    protected void addValidators() {
        correo.setImmediate(true);
        correo.addValidator(new EmailValidator(getMessage("validator.email")));
    }

    @Override
    protected void createForm() {
        form.addField("tipoDocumento", tipoDocumento);
        form.addField("documento", documento);
        form.addField("razonSocial", razonSocial);
        form.addField("cargo", cargo);
        form.addField("nombres", nombres);
        form.addField("apellidos", apellidos);
        form.addField("correo", correo);
        form.addField("celular", celular);
        form.addField("telefono", telefono);
        form.addField("departamento", departamento);
        form.addField("ciudad", ciudad);
        form.addField("direccion", direccion);
        form.addField("tipo", tipo);
        form.addField("observaciones", observaciones);    
        if(usuario == null){
            form.addField("captcha", captcha);
            form.addField("captchaInput", captchaInput);
        }
        form.getLayout().addComponent(upload);
        form.getLayout().addComponent(label);
    }

    @Override
    protected void onClickReset() {
        apellidos.setValue("");
        celular.setValue("");
        correo.setValue("");
        departamento.setValue(null);
        direccion.setValue("");
        documento.setValue("");
        nombres.setValue("");
        observaciones.setValue("");
        telefono.setValue("");
        tipoDocumento.setValue(null);
        form.removeAllProperties();
        captcha = new CaptchaField(getApplication(), true, false, null);
        captchaInput.setValue("");
        tipo.setValue(null);
        createForm();
    }

    @Override
    protected void process() {
        try {
            if(captcha.validateCaptcha(getValue(captchaInput))){
                validate();
                ApplicationContext ctx = getApplication().getContext();        
                final ServletContext sCtx = ((WebApplicationContext)ctx).getHttpSession().getServletContext();
                ParametroDao pd = new ParametroDao();
            
                String s = pd.getParameter("ip");//super.getURL();//getWindow().getURL().toString().replace(sCtx.getContextPath(), "").replace("1/", "");
                String ticket = AlfrescoUtil.getTicket();
                
                if(ticket!=null){
                    URL url = new URL(s+ "alfresco/service/workflow-instance-create?definition=activiti$process14&"
                    + "description=" + URLEncoder.encode(getValue(tipo) + " "+getValue(documento) + " " + getValue(nombres) + " " + getValue(apellidos) , "UTF-8")// "funciono%2044"
                    + "&assign=admin&"
                    + "tiposolicitud=" + URLEncoder.encode(getValue(tipo), "UTF-8")
                    + "&tipodocumento=" + URLEncoder.encode(((TipoDocumento) tipoDocumento.getValue()).getDescripcion(), "UTF-8")
                    + "&numerodocumento=" + getValue(documento)
                    + "&nombres=" + URLEncoder.encode(getValue(nombres), "UTF-8")
                    + "&apellidos=" + URLEncoder.encode(getValue(apellidos), "UTF-8")
                    + "&telefono=" + getValue(telefono)
                    + "&celular=" + getValue(celular)
                    + "&departamento=" + URLEncoder.encode(String.valueOf(((Departamento) departamento.getValue()).getCodigo()), "UTF-8")
                    + "&ciudad=" + URLEncoder.encode(String.valueOf(((Ciudad) ciudad.getValue()).getCodigo()), "UTF-8")
                    + "&direccion=" + URLEncoder.encode(getValue(direccion), "UTF-8")
                    + "&email=" + getValue(correo)
                    + "&redaccionsolicitud=" + URLEncoder.encode(getValue(observaciones), "UTF-8")
                    + "&razonSocial=" + URLEncoder.encode(getValue(razonSocial), "UTF-8")        
                    + "&cargo=" + URLEncoder.encode(getValue(cargo), "UTF-8")
                    + "&rutaarchivo=" + URLEncoder.encode(rutaArchivo, "UTF-8")
                    + "&alf_ticket="+ ticket);
            
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
            
            launchSubwindow("Se ha guardado correctamente " + getValue(tipo)+", Esto es una prueba el codigo del ticket es " + t); //msr
            onClickReset();
            in.close();
            } else {
                    form.removeAllProperties();
                captcha = new CaptchaField(getApplication(), true, false, null);
                captchaInput.setValue("");
                createForm();
                launchSubwindow(getMessage("error.captcha"));                    
                    launchSubwindow("Ha ocurrido un error al intentar crear su peticion");
                }
            } else {
                form.removeAllProperties();
                captcha = new CaptchaField(getApplication(), true, false, null);
                captchaInput.setValue("");
                createForm();
                launchSubwindow(getMessage("error.captcha"));
            }
            
        } catch (MalformedURLException me) {
            me.printStackTrace();
            System.out.println("URL erronea");
            launchSubwindow("Ha ocurrido un error al intentar crear su "+ getValue(tipo));
        } catch (InvalidValueException ex){
            form.removeAllProperties();
                captcha = new CaptchaField(getApplication(), true, false, null);
                captchaInput.setValue("");
                createForm();
            launchSubwindow(getMessage("error.form"));
        } catch (IOException ioe) {
            launchSubwindow("Ha ocurrido un error al intentar crear su peticion");
            ioe.printStackTrace();
            System.out.println("Error IO");
        } 
            
            //        try {
            //            validate();
            //            Pqrs pqrs = new Pqrs();
            //            pqrs.setApellidos(getValue(apellidos));
            //            pqrs.setCelular(getValue(celular));
            //            pqrs.setCiudad((Ciudad) ciudad.getValue());
            //            pqrs.setDireccion(getValue(direccion));
            //            pqrs.setDocumento(getValue(documento));
            //            pqrs.setEmail(getValue(correo));
            //            pqrs.setNombres(getValue(nombres));
            //            pqrs.setSolicitud(getValue(observaciones));
            //            pqrs.setTelefono(getValue(telefono));
            //            pqrs.setTipoDocumento((TipoDocumento) tipoDocumento.getValue());
            //            pqrs.setUsuario(usuario);
            //            
            //            if(usuario == null){
            //                if(captcha.validateCaptcha(getValue(captchaInput))){
            //                    if(service.save(pqrs) != null){
            //                        launchSubwindow("Se ha registrado exitosamente");
//                    URL url = new URL("http://www.antisacsor.com");
//                    url.openConnection();
                                    //getWindow().open(new ExternalResource(,"_blank"));
            //                        onClickReset();
            //                    } else {
            //                        launchSubwindow("Ha ocurrido un error, por favor vuelva a intentarlo");
            //                    }
            //                } else {
            //                    launchSubwindow(getMessage("error.captcha"));
            //                }
            //            } else {
            //                if(service.save(pqrs) != null){
            //                    launchSubwindow("Se ha registrado exitosamente");
            //                    onClickReset();
            //                } else {
            //                    launchSubwindow("Ha ocurrido un error, por favor vuelva a intentarlo");
            //                }
            //            }
            //            
            //        } catch (InvalidValueException ex){
            //            launchSubwindow(getMessage("error.form"));
            //        }
//        } catch (IOException ex) {
//            Logger.getLogger(RegistrarPqrs.class.getName()).log(Level.SEVERE, null, ex);
//        } 
    }

    @Override
    protected void setRequired(boolean required) {
        apellidos.setRequired(required);
        correo.setRequired(required);
        celular.setRequired(true);
        ciudad.setRequired(required);
        departamento.setRequired(required);
        direccion.setRequired(required);
        documento.setRequired(required);
        nombres.setRequired(required);
        observaciones.setRequired(required);
        telefono.setRequired(required);
        tipoDocumento.setRequired(required);
        tipo.setRequired(required);
    }

    @Override
    protected void setMaxLength() {
        apellidos.setMaxLength(20);
        celular.setMaxLength(20);
        correo.setMaxLength(60);      
        direccion.setMaxLength(60);
        documento.setMaxLength(20);
        nombres.setMaxLength(20);
        telefono.setMaxLength(20);
    }

    @Override
    protected void validate() throws InvalidValueException {
        apellidos.validate();
        celular.validate();
        correo.validate();
        ciudad.validate();
        departamento.validate();
        direccion.validate();
        documento.validate();
        nombres.validate();
        observaciones.validate();
        telefono.validate();
        tipoDocumento.validate();
    }
    
    private void init(){
        apellidos = new TextField("Apellidos:");
        celular = new TextField("Celular:");
        correo = new TextField("Correo:");
        correo.setWidth("300px");
        ciudad = new NativeSelect("Ciudad:");
        departamento = new NativeSelect("Departamento:");
        direccion = new TextField("Dirección");
        direccion.setWidth("300px");
        documento = new TextField("Documento");
        nombres = new TextField("Nombres:");
        observaciones = new TextArea("Observaciones");        
        telefono = new  TextField("Telefono:");
        tipoDocumento = new NativeSelect("Tipo Documento:");
        createUpload();
        
        tipo = new NativeSelect("Tipo de Solicitud:");
        tipo.addItem("Peticion");
        tipo.addItem("Queja");
        tipo.addItem("Reclamo");
        tipo.addItem("Sugerencia");
        tipo.addItem("Proceso disciplinario");
        
        departamentos = serviceUsuario.getAllDepartamentos();
        
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
        }
        
        for(TipoDocumento td : serviceUsuario.getAllTipoDocuementos()){
            tipoDocumento.addItem(td);
            tipoDocumento.setItemCaption(td, td.getNombre());
        }
        
        observaciones.setWidth("300");
        razonSocial = new TextField("Razón Social:");
        cargo = new TextField("Cargo:");
        
        cargo.setVisible(false);
        razonSocial.setVisible(false);
    }   
    
    private void createUpload(){
        upload = new Upload(getMessage("file.upload.caption"), new Upload.Receiver() {     
            
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                FileOutputStream fos = null; 
                try {
                    file =new File (inPath + filename);
                    fos = new  FileOutputStream(file);
                } catch (IOException ex) {
                    logger.error(getMessage("error.file_upload_failed", filename), ex);
                    file = null;
                } 
                return fos;
            }
        });
        
        upload.addListener(new Upload.SucceededListener() {
            
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {                
                label.setValue("Se ha cargado correctamente el archivo " + readFile());
            }
        });
        
        upload.addListener(new Upload.FailedListener() {
            
            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                label.setCaption(getMessage("file.upload.error", event.getFilename()));
            }
        });
    }
    
    private String readFile(){
        rutaArchivo = file.getAbsolutePath();
        upload.setReadOnly(true);
        return file.getName();
        
    }
}