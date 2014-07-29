package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.dao.ContraseniasDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.entity.UsuarioContraseniaHistorial;
import co.org.conte.sgm.service.MailService;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.MyPasswordValidator;
import co.org.conte.sgm.util.Password;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.CaptchaField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

/**
 *
 * @author J4M0
 */
public class CambiarContrasenia extends GenericForm{
    
    private static final int MAX_LENGHT = 20;
    
    private UsuarioDao daoUsuario = new UsuarioDao();
    private ContraseniasDao daoContrasenia = new ContraseniasDao();
    
    private CaptchaField captchaField ;
    private TextField captchaInput;
    private PasswordField contrasenia;
    private PasswordField nuevaContrasenia;
    private PasswordField confirmarContrasenia;
    @Resource
    private MailService mailService;
    
    public CambiarContrasenia(Usuario usuario) {
        super(usuario);
        init();
        if(this.usuario.getModificacionContrasenia()!=null){
            if(((((new Date()).getTime()-this.usuario.getModificacionContrasenia().getTime()))/(1000*60*60*24))<2){                
                removeAllComponents();
                message.setCaption("Debe esperar 1 desde su ultima modificacion de contraseña");                
                addComponent(message);                
            } 
        }
    }    

    @Override
    public void attach() {
        super.attach();
        captchaField = new CaptchaField(getApplication(), true, false, null);
        createForm();
        this.setRequired(true);
    }
    
    
    private void init(){
        captchaInput = new TextField("Ingrese Captcha");
        contrasenia = new PasswordField("Contraseña Actual");
        nuevaContrasenia = new PasswordField("Nueva Contraseña");
        confirmarContrasenia = new PasswordField("Confirmar Nueva Contraseña");
                
        contrasenia.setDescription("Ingrese su contraseña actual");
        nuevaContrasenia.setDescription("Ingrese su nueva contraseña");
        confirmarContrasenia.setDescription("Confirme la nueva contraseña");
    }
    
    @Override
    protected void addListeners() {
    }

    @Override
    protected void addValidators() {        
    }

    @Override
    protected void createForm() {
        super.form.addField("contrasenia", this.contrasenia);
        super.form.addField("ncontrasenia", this.nuevaContrasenia);
        super.form.addField("ncontrasenia", this.confirmarContrasenia);
        super.form.addField("captcha", this.captchaField);
        super.form.addField("captchaInput", this.captchaInput);
        this.setRequired(true);
    }

    @Override
    protected void onClickReset() {
        this.contrasenia.setValue("");
        this.nuevaContrasenia.setValue("");
        this.confirmarContrasenia.setValue("");
    }
    
    @Override
    protected void process() {
        try {
            validate();
            usuario.setContrasena(Password.encryptPassword(getValue(contrasenia)));
            Usuario u = new Usuario();
            u.setContrasena(Password.encryptPassword(getValue(contrasenia)));
            u.setDocumento(usuario.getDocumento());
            //IF QUE VALIDA EL CAPTCHA
            if(captchaField.validateCaptcha(getValue(captchaInput))){
                if(!daoUsuario.findBy(u).isEmpty()){         
                    
                    if(getValue(this.nuevaContrasenia).equals(getValue(this.confirmarContrasenia))){
                        //Valida que la nueva contrasenia no sea igual a la anterior
                        if(!getValue(this.contrasenia).equals(getValue(this.nuevaContrasenia))){
                            try {
                                List<String> contrasenias = new ArrayList<String>();
                                List<UsuarioContraseniaHistorial> uchs = serviceUsuario.getContrasenias(usuario);
                                if(uchs!=null){
                                    for(UsuarioContraseniaHistorial uch : uchs){
                                        contrasenias.add(uch.getContrasenia());
                                    }
                                } 
                                MyPasswordValidator.ValidationResult vr = Password.validate((getValue(nuevaContrasenia)), contrasenias);
                                if(vr.isValid()){
                                    UsuarioContraseniaHistorial uch = new UsuarioContraseniaHistorial();
                                    uch.setContrasenia(usuario.getContrasena());
                                    uch.setUsuario(usuario);
                                    uch.setFecha(new Date());
                                    usuario.setContrasena(Password.encryptPassword(getValue(nuevaContrasenia)));
                                    usuario.setModificacionContrasenia(new Date());
                                    daoUsuario.update(usuario);
                                    removeAllComponents();
                                    super.message.setCaption("Se ha actualizado la contraseña");
                                    addComponent(message);
                                    
                                    daoContrasenia.insert(uch);
                                    mailService.send(usuario.getEmail(), "Su contraseña ha sido cambiada", "Se ha actualizado su contraseña ");
                                } else {
                                    captchaField = new CaptchaField(getApplication(), true, false, null);
                                    form.removeAllProperties();
                                    nuevaContrasenia.setValue("");
                                    confirmarContrasenia.setValue("");
                                    captchaInput.setValue("");
                                    createForm();
                                    form.getFooter().removeComponent(message);
                                    message.setCaption(vr.getMessages().toString());
                                    addComponent(message);
                                }
                            } catch (DaoException ex) {
                                Logger.getLogger(CambiarContrasenia.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
                            captchaField = new CaptchaField(getApplication(), true, false, null);
                            form.removeAllProperties();
                            nuevaContrasenia.setValue("");
                            confirmarContrasenia.setValue("");
                            captchaInput.setValue("");
                            createForm();
                            launchSubwindow("La nueva contraseña es igual a la anterior");                            
                        }                       
                    } else {                    
                        captchaField = new CaptchaField(getApplication(), true, false, null);
                        form.removeAllProperties();
                        nuevaContrasenia.setValue("");
                        confirmarContrasenia.setValue("");
                        captchaInput.setValue("");
                        createForm();
                        launchSubwindow("Las contraseñas no son iguales");   
                        
                    }
                } else {
                    captchaField = new CaptchaField(getApplication(), true, false, null);
                    form.removeAllProperties();
                    contrasenia.setValue("");
                    captchaInput.setValue("");
                    createForm();
                    launchSubwindow("La contraseña no es correcta");        
                }
            } else {
                captchaInput.setValue("");
                launchSubwindow(getMessage("error.captcha"));
            }
        } catch (DaoException ex) {
            Logger.getLogger(CambiarContrasenia.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InvalidValueException ex) {
            super.message.setCaption("Faltan campos por llenar");
        }
    }

    @Override
    protected void setRequired(boolean required) {
        this.captchaInput.setRequired(required);
        this.contrasenia.setRequired(required);
        this.confirmarContrasenia.setRequired(required);
        this.nuevaContrasenia.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        this.contrasenia.validate();
        this.confirmarContrasenia.validate();
        this.nuevaContrasenia.validate();
    }

    @Override
    protected void setMaxLength() {
        contrasenia.setMaxLength(MAX_LENGHT);
        confirmarContrasenia.setMaxLength(MAX_LENGHT);
        nuevaContrasenia.setMaxLength(MAX_LENGHT);
    }
}