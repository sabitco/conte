package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.TipoDocumentoDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.MailService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.MyPasswordValidator;
import co.org.conte.sgm.util.Password;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author Andrés Mise Olivera
 */
@Configurable(preConstruction = true)
public class CrearCuenta extends GenericForm {
    
    private TecnicoDao daoTecnico = new TecnicoDao();
    TipoDocumentoDao dao = new TipoDocumentoDao();
    UsuarioDao usuarioDao = new UsuarioDao();
    
    private CaptchaField captchaField ;
    private TextField captchaInput;
    private PasswordField contrasenia = new PasswordField("Contraseña");
    private PasswordField confirmarContrasenia = new PasswordField("Confirmar Contraseña");
    private TextField correo = new TextField("E-mail");
    private TextField documento;   
    
    @Resource
    private MailService mailService;
    
    @Resource
     private VelocityEngine velocityEngine;
   
    private TextField nombres = new TextField("Nombres");    
    private TextField primerApellido;
    private TextField segundoApellido;
    
    private Tecnico tecnico;
    private NativeSelect tipoDocumento;
    private Button validate = new Button("Continuar");
    
    //EN CASO DE SER EMPRESA MUESTRA LOS SIGUIENTES CAMPOS
    
    private TextField celular;
    private ComboBox ciudad;
    private List<Departamento> departamentos;
    private ComboBox departamento;
    private TextField telefono;

    public CrearCuenta(Usuario usuario) { 
        super(usuario);
        if(usuario==null){
            this.usuario = new Usuario();
        }
        init();        
    }       
 
    private void init() {        
        captchaInput = new TextField("Ingrese Captcha");
        celular = new TextField("Celular");
        ciudad = new ComboBox("Ciudad");
        departamentos = serviceUsuario.getAllDepartamentos();
        departamento = new ComboBox("Departamento");
        documento = new TextField("Documento");
        primerApellido = new TextField("Primer Apellido");
        segundoApellido = new TextField("Segundo Apellido");
        telefono = new TextField("Telefono");
        correo.setWidth("200px");
        List<TipoDocumento> documentos = serviceUsuario.getAllTipoDocuementos();        
        tipoDocumento = new NativeSelect ("Tipo de Documento");
        tipoDocumento.setImmediate(true);
        for (TipoDocumento d:  documentos ) {
            tipoDocumento.addItem(d);
            tipoDocumento.setItemCaption(d, d.getNombre());
        }
        
        
        
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
        }
        correo.setWidth("300px");
        setMaxLength();
        addListeners();
        addValidators();
        setRequired(true);
        setVisibleFields(false);
        
    }

    @Override
    protected void addListeners() {
        
        this.validate.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                onClickValidate();              
            }
        });
        
        this.tipoDocumento.setImmediate(true);
        this.tipoDocumento.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(tipoDocumento.getValue()!=null){
                    tecnico = null;
                    documento.removeAllValidators();
                    tipoDocumento.setImmediate(true);
                    documento.setImmediate(true);
                    documento.setMaxLength(20);
                    if(getValue(tipoDocumento)!= null){
                        if(!tipoDocumento.getValue().toString().equalsIgnoreCase("PAP")){                            
                            documento.addValidator(new IntegerValidator("Solo puede ingresar números"));            
                        }

                        if(getValue(tipoDocumento).equals("NIT")){
                            documento.setDescription(getMessage("tooltip.document_nit"));
                            usuario.setPerfil(new Perfil(3));
                            
                        } else {
                            documento.setDescription(getMessage("tooltip.document"));
                            usuario.setPerfil(new Perfil(2));
                            tecnico = new Tecnico();                            
                        }
                    }
                }
             }
        });
        
        this.departamento.setImmediate(true);
        this.departamento.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamento.getValue();
                if(selected != null){
                    ciudad.removeAllItems();
                    ciudad.setValue(null);
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
    }

    @Override
    protected void addValidators() {
        this.correo.addValidator(new EmailValidator("El mail no es correcto"));
    }

    @Override
    protected void createForm() {                 
        form.addField("tipoDocumento", this.tipoDocumento);        
        form.addField("documento", this.documento);        
        form.addField("captcha", captchaField);
        form.addField("captchaInput", captchaInput);
        form.addField("nombres", this.nombres);
        form.addField("primerApellido", this.primerApellido);
        form.addField("segundoApellido", this.segundoApellido);
        form.addField("email", this.correo);
        form.addField("departamento", departamento);
        form.addField("ciudad", ciudad);
        form.addField("Celular", celular);
        form.addField("telefono", telefono);
        form.addField("contrasenia", this.contrasenia);
        form.addField("confirmar", this.confirmarContrasenia);
        
        form.getFooter().addComponent(validate);
    }

    @Override
    protected void onClickReset() {
        captchaInput.setVisible(true);
        captchaField.setVisible(true);
        this.celular.setValue("");
        this.ciudad.removeAllItems();
        this.ciudad.setValue(null);
        this.departamento.setValue(null);
        this.setReadOnlyFields(false);
        this.tipoDocumento.setValue(null);
        this.documento.setValue("");       
        this.setVisibleFields(false);        
        this.validate.setVisible(true);
        this.primerApellido.setValue("");
        this.segundoApellido.setValue("");
        this.contrasenia.setValue("");
        this.confirmarContrasenia.setValue("");
        this.correo.setValue("");
        this.nombres.setValue("");     
        this.telefono.setValue("");
        this.captchaInput.setValue("");
    }

    @Override
    protected void process() {
        try{
            this.validate();
            if(this.contrasenia.getValue().toString().equals(
                    this.confirmarContrasenia.getValue().toString())){
                MyPasswordValidator.ValidationResult validationResult = Password.validate(getValue(contrasenia), new ArrayList<String>());
                if(validationResult.isValid()){
                    
                
                    usuario.setEmail(getValue(correo));

                    if(tecnico!=null){         
                        tecnico.setDocumento(getValue(documento));
                        tecnico.setPrimerApellido(getValue(primerApellido));
                        tecnico.setSegundoApellido(getValue(segundoApellido));
                        tecnico.setNombres(getValue(nombres));
                        tecnico.setEmail(getValue(correo));                    
                    }
                    try {
                        //Se verifica que no existe un usuario con el mismo correo
                        if(serviceUsuario.validateEmail(usuario)){
                            usuario.setDocumento(getValue(documento));
                            usuario.setTipoDocumento(tipoDocumento.getValue().toString());
                            usuario.setEmail(getValue(correo));                            
                            usuario.setNombres(this.nombres.getValue().toString());
                            usuario.setPrimerApellido(getValue(this.primerApellido));
                            usuario.setSegundoApellido(getValue(this.segundoApellido));                        
                            usuario.setContrasena(Password.encryptPassword(getValue(this.contrasenia)));

                           if(usuario.getTipoDocumento().equalsIgnoreCase("NIT")){
                                usuario.setCiudad((Ciudad) ciudad.getValue());
                                usuario.setTelefono(getValue(telefono));
                                usuario.setCelular(getValue(celular));
                            }

                            this.usuarioDao.insert(usuario);
                            if(tecnico!=null){                    
                                daoTecnico.insert(tecnico);
                            } 
                            usuario.setContrasena(getValue(contrasenia));
                            HashMap model = new HashMap();
                            model.put("user", usuario);
                            String text = VelocityEngineUtils.mergeTemplateIntoString(
                                    velocityEngine, "registration-confirmation.vm", model);
//                            File file = new File(CrearCuenta.class.getResource("images/logo_conte.jpg").getPath());
//                            System.out.print("EL PATH ES ----------->"+file.getPath());
                            mailService.send(usuario.getEmail(), "Creación de usuario",
                                   text);

                            ConfirmDialog.show(getWindow(), "Confirmar", "Se ha creado correctamente el usuario",
                            "Aceptar", "Crear Nuevo", new ConfirmDialog.Listener() {

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        getApplication().getMainWindow().getApplication().close();
                                    } else {
                                        onClickReset();
                                    }
                                }
                            });
                        } else {
                            message.setCaption("Existe un usuario con el correo ingresado");
                        }

                    } catch (DaoException ex) {
                        Logger.getLogger(CrearCuenta.class.getName()).log(Level.SEVERE, null, ex);
                        message.setCaption("Ha ocurrido un error al intentar insertar");
                    }                               
                } else {
                    String error = "";
                    for(String m : validationResult.getMessages()){
                        error = error + m + "\n";
                    }
                    form.getFooter().removeComponent(message);                    
                    message.setCaption(error);
                    addComponent(message);
                }
            } else {
                launchSubwindow(getMessage("error.form_confirm_password"));
            }            
        } catch (InvalidValueException ex){
            launchSubwindow(getMessage("error.form"));
        }        
    }

    @Override
    protected void setMaxLength(){
        celular.setMaxLength(20);        
        correo.setMaxLength(60);        
        nombres.setMaxLength(60);        
        primerApellido.setMaxLength(20);
        segundoApellido.setMaxLength(20);
        telefono.setMaxLength(20);
    }
    
    @Override
    protected void setRequired(boolean required) {
        this.captchaInput.setRequired(required);
        this.documento.setRequired(required);
        this.primerApellido.setRequired(required);
        this.contrasenia.setRequired(required);
        this.confirmarContrasenia.setRequired(required);
        this.correo.setRequired(required);
        this.nombres.setRequired(required);
        this.tipoDocumento.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        this.documento.validate();
        this.primerApellido.validate();
        this.contrasenia.validate();
        this.confirmarContrasenia.validate();
        this.correo.validate();
        this.nombres.validate();
    }    
    
    private void onClickValidate(){
        if(documento.isValid() && !getValue(documento).isEmpty() && 
                tipoDocumento.isValid() && captchaInput.isValid()){
            if(captchaField.validateCaptcha(getValue(captchaInput))){
                
                usuario.setTipoDocumento(getValue(this.tipoDocumento));
                usuario.setDocumento(getValue(this.documento));
                if(usuarioDao.findByDocumento(usuario.getTipoDocumento(), usuario.getDocumento())==null){
                    this.setReadOnlyFields(true);
                    this.validate.setVisible(false);
                    this.setVisibleFields(true);
                    captchaInput.setVisible(false);
                    captchaField.setVisible(false);
                    if(usuario.getPerfil().getCodigo()==3){                                   
                        nombres.setCaption("Empresa");
                        celular.setRequired(true);
                        ciudad.setRequired(true);                        
                        departamento.setRequired(true);
                        primerApellido.setRequired(false);
                        telefono.setRequired(true);                       
                        celular.setVisible(true);
                        departamento.setVisible(true);
                        ciudad.setVisible(true);
                        primerApellido.setVisible(false);
                        segundoApellido.setVisible(false);
                        
                    } else {
                        nombres.setCaption("Nombres");                        
                        celular.setRequired(false);
                        ciudad.setRequired(false);
                        departamento.setRequired(false);
                        primerApellido.setRequired(true);
                        telefono.setRequired(false);                        
                        celular.setVisible(false);
                        departamento.setVisible(false);
                        ciudad.setVisible(false);
                        primerApellido.setRequired(true);                        
                        segundoApellido.setVisible(true);
                        telefono.setVisible(false);
                    }
                } else {
                    captchaField = new CaptchaField(getApplication(), true, false, null);
                    form.removeAllProperties();
                    captchaInput.setValue("");
                    createForm();
                    launchSubwindow(getMessage("error.form_documento_existe", getValue(documento)));                    
                    
                }
            } else {
                launchSubwindow(getMessage("error.captcha"));
                captchaInput.setValue("");
            }
        } else {
            launchSubwindow(getMessage("error.form"));
        }
    }
    
    private void setReadOnlyFields(boolean readOnly){
        this.tipoDocumento.setReadOnly(readOnly);
        this.documento.setReadOnly(readOnly);
    }
    
    private void setVisibleFields(boolean visible){        
        super.reset.setVisible(visible);
        super.submit.setVisible(visible);
        this.celular.setVisible(visible);
        this.ciudad.setVisible(visible);
        this.departamento.setVisible(visible);
        this.primerApellido.setVisible(visible);
        this.segundoApellido.setVisible(visible);
        this.contrasenia.setVisible(visible);
        this.confirmarContrasenia.setVisible(visible);
        this.correo.setVisible(visible);
        this.nombres.setVisible(visible);    
        this.telefono.setVisible(visible);
    }

    @Override
    public void attach() {
        captchaField = new CaptchaField(getApplication(), true, false, null);
        createForm();
        setDescriptions();
    }
    
    private void setDescriptions(){
        correo.setDescription(getMessage("tooltip.email"));
        celular.setDescription(getMessage("tooltip.celular"));
        documento.setDescription(getMessage("tooltip.document"));
    }
   
}