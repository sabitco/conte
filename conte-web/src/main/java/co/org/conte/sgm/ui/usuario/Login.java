package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import co.org.conte.sgm.util.Password;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

/**
 *
 * @author jam
 */
public class Login extends GenericForm{
    
    private TextField captchaInput = new TextField("Ingrese Captcha");
    protected TextField login = new TextField ( "Documento");
    protected PasswordField password = new PasswordField ( "Contraseña");


    public Login(Usuario usuario) {        
        super(usuario);
    }

    @Override
    public void attach() {
        super.attach();
        createForm();
    }
  
    @Override
    protected void onClickReset() {
        this.login.setValue("");
        this.password.setValue("");
    }

    @Override
    protected void addListeners() {
    }

    @Override
    protected void addValidators() {
    }

    @Override
    protected void createForm() {
        TextField tf = new TextField("", "Es necesario ingresar la contraseña tal como se creó");
        tf.setReadOnly(true);
        tf.setWidth("400px");
        super.form.addField("login", this.login);
        super.form.addField("", tf);
        super.form.addField("password", this.password);
        setRequired(true);
    }

    @Override
    protected void setRequired(boolean required) {
        this.login.setRequired(required);
        this.password.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        this.login.validate();
        this.password.validate();
    }

    @Override
    protected void process() {
        try {                 
            validate();
            
            usuario = new Usuario();
            usuario.setDocumento(getValue(login)); 
            usuario.setContrasena(Password.encryptPassword(getValue(password)));            
            usuario = serviceUsuario.login(usuario);
            if(usuario!=null){
                if(usuario.getEstado() != null){
                    if(usuario.getEstado().equals("Bloqueado")){
                        //Mail.enviarMail("admin", "Intento", "texto");
                        launchSubwindow("Su usuario ha sido bloqueado");
                    } else if(usuario.getEstado().equals("Inactivo")){
                        launchSubwindow("Su usuario se encuentra inactivo");
                    } else {
                        getApplication().setUser(usuario);
                    }
                } else {
                    getApplication().setUser(usuario);
                }
            } else {
                form.removeAllProperties();
                captchaInput.setValue("");
                createForm();
                launchSubwindow(getMessage("error.login"));
            }
            
        } catch (Validator.InvalidValueException ive) {
            message.setCaption("Existen campos incorrectos");
        }
    }

    @Override
    protected void setMaxLength() {
        this.login.setMaxLength(20);
    }
 }