package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.MailService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.Password;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.CaptchaField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author Andrés Mise Olivera
 */
@Configurable(preConstruction = true)
public class RecuperarContrasenia extends GenericForm{
        
    private CaptchaField captchaField ;
    private TextField captchaInput;
    private TextField documento;
    private List<TipoDocumento> documentos;    
    private NativeSelect tipoDocumento;
    
    @Resource
    private MailService mailService;
    
    public RecuperarContrasenia(Usuario usuario) {
        super(usuario);
        init();
    }

    @Override
    public void attach() {
        captchaField = new CaptchaField(getApplication(), true, false, null);
        createForm();
        this.setRequired(true);
        super.addComponent(super.form);      
    }
    
    @Override
    protected void addListeners(){
         tipoDocumento.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                documento.removeAllValidators();
                if(!getValue(tipoDocumento).equalsIgnoreCase("PAP")){
                    addValidators();
                }
                
             }
            });     
    }
   
    @Override
    protected void addValidators(){
        this.documento.addValidator(new IntegerValidator("Solo puede ingresar números"));
    }
  
    @Override
    protected void createForm(){  
        
        form.addField("tipoDocumento", this.tipoDocumento);
        form.addField("documento", this.documento);
        form.addField("captcha", captchaField);
        form.addField("captchaInput", captchaInput);
    }
    
    @Override
    protected void onClickReset() {
        this.documento.removeAllValidators();
        this.documento.setValue("");
        tipoDocumento.setValue(null);
        captchaInput.setValue("");
        tipoDocumento.removeAllItems();
        tipoDocumento.setImmediate(true);
        for (TipoDocumento d:  documentos ) {
            tipoDocumento.addItem(d);
            tipoDocumento.setItemCaption(d, d.getNombre());
        }
        form.removeAllProperties();
        captchaField = new CaptchaField(getApplication(), true, false, null);        
        createForm();
    }
    
    @Override
    protected void process(){
        try{
            validate();
            if(captchaField.validateCaptcha(getValue(captchaInput))){                
                //Realizar busqueda de los datos del usuario
                TipoDocumento td = (TipoDocumento)tipoDocumento.getValue();
                if(usuario==null){
                    usuario = new Usuario();
                }
                usuario.setTipoDocumento(td.getNombre());
                usuario.setDocumento(getValue(documento));
                usuario = serviceUsuario.findByDocumento(usuario);
                if(usuario != null){                    
                    //Recupera los datos del usuario para enviar el correo
                    String password;
                    do{
                        password = Password.generatePassword();
                    } while(!(Password.validate(password, new ArrayList<String>()).isValid()));
                    
                    usuario.setContrasena(Password.encryptPassword(password));
                    usuario.setModificacionContrasenia(new Date());
                    serviceUsuario.update(usuario);
                    
                    mailService.send(usuario.getEmail(), "Nueva contraseña", "Su nueva contraseña es "+password);
                    super.removeAllComponents();
                    super.message.setCaption("Se ha enviado un correo para recuperar la contresaña");
                    super.addComponent(message);
                } else {
                    captchaField = new CaptchaField(getApplication(), true, false, null);
                    form.removeAllProperties();
                    captchaInput.setValue("");
                    createForm();
                    launchSubwindow("NO se ha encontrado el usuario en "
                            + "el sistema");         
                }
            } else {
                launchSubwindow(getMessage("error.captcha"));
            }
        } catch (InvalidValueException iex){
            launchSubwindow(getMessage("error.form"));
        }
        
    }
    
    @Override
    protected void setRequired(boolean required){
        this.captchaInput.setRequired(required);
        this.tipoDocumento.setRequired(required);
        this.documento.setRequired(required);
    }
    
    @Override
    protected void validate() throws InvalidValueException{
        this.tipoDocumento.validate();
        this.documento.validate();     
        captchaInput.validate();
    }   

    @Override
    protected void setMaxLength() {
        documento.setMaxLength(20);
    }
    
    private void init() {      
        captchaInput = new TextField("Ingrese Captcha");
        documento = new TextField("Documento");      
        documentos = serviceUsuario.getAllTipoDocuementos();
        tipoDocumento = new NativeSelect ("Tipo de Documento");
        tipoDocumento.setImmediate(true);

        for (TipoDocumento d:  documentos ) {
            tipoDocumento.addItem(d);
            tipoDocumento.setItemCaption(d, d.getNombre());
        }
        addListeners();
    }
}