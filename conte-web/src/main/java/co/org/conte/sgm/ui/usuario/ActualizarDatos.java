package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J4M0
 */
public class ActualizarDatos extends GenericForm{

    private ComboBox ciudad;
    private ComboBox departamento;
    private TextField nombres;
    private TextField primerApellido;
    private TextField segundoApellido;    
    private TextField email;
    private TextField celular;
    private TextField telefono;
    private TextField direccion;
    private TecnicoDao daoTecnico;
    private List<Departamento> departamentos;
    private NativeSelect notificacion;
    private String si = "Si";
    private String no = "No";
    
    public ActualizarDatos(Usuario usuario) {
        super(usuario);
        nombres = new TextField("Nombres", usuario.getNombres());        
        primerApellido = new TextField("Primer apellido", usuario.getPrimerApellido());
        segundoApellido = new TextField("Segundo apellido", usuario.getSegundoApellido());
        email = new TextField("Correo", usuario.getEmail());
        celular = new TextField("Celular", usuario.getCelular());
        telefono = new TextField("Telefono", usuario.getTelefono());
        direccion = new TextField("Dirrección", usuario.getDireccion());
        ciudad = new ComboBox("Ciudad");
        departamento = new ComboBox("Departamento");
        departamentos = serviceUsuario.getAllDepartamentos();        
        notificacion = new NativeSelect("Notificacion:");
        notificacion.addItem(si);
        notificacion.addItem(no);    
        notificacion.setValue(usuario.isNotificacion() ? si : no);
        daoTecnico = new TecnicoDao();
        addListeners();
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
            
        }
        departamento.setValue(usuario.getCiudad().getDepartamento());
        this.createForm();
        this.addValidators();
        this.setRequired(true);
    }
    
    @Override
    protected void addListeners() {
        departamento.setImmediate(true);
        this.departamento.addListener(new Property.ValueChangeListener() {
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
                            if(usuario!=null && usuario.getCiudad()!=null){
                                if(usuario.getCiudad().getCodigo()==c.getCodigo()){
                                    ciudad.setValue(c);
                                }
                            }
                        }
                    }                    
                }
             }
        });
    }

    @Override
    protected void addValidators() {
        email.addValidator(new EmailValidator(getMessage("validator.email")));
    }

    @Override
    protected void createForm() {
        super.form.addField("nombres",nombres);
        super.form.addField("primerApellido",primerApellido);
        super.form.addField("segundoApellido",segundoApellido);    
        super.form.addField("email", email);
        super.form.addField("celular",celular);
        super.form.addField("telefono",telefono);
        super.form.addField("direccion",direccion);
        super.form.addField("departamento",departamento);
        super.form.addField("ciudad",ciudad);        
        super.form.addField("notificacion",notificacion);
    }

    @Override
    protected void onClickReset() {
        notificacion.setValue(null);
        
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
            
        }
        
        ciudad.removeAllItems();
        ciudad.setValue(null);
        
        departamento.setValue(null);
        nombres.setValue("");
        primerApellido.setValue("");
        segundoApellido.setValue("");
        email.setValue("");
        celular.setValue("");
        telefono.setValue("");
        direccion.setValue("");
    }

    @Override
    protected void process() {
        try{
            validate();
            usuario.setNombres(getValue(nombres));
            usuario.setPrimerApellido(getValue(primerApellido));
            usuario.setSegundoApellido(getValue(segundoApellido));
            usuario.setEmail(getValue(email));
            usuario.setCiudad((Ciudad)ciudad.getValue());
            usuario.setCelular(getValue(celular));
            usuario.setTelefono(getValue(telefono));
            usuario.setDireccion(getValue(direccion));
            usuario = serviceUsuario.merge(usuario);
            if(usuario!=null){
                Tecnico tecnico = daoTecnico.findByDocumento(usuario.getDocumento());
                if(tecnico!=null){
                    tecnico.setCelular(usuario.getCelular());
                    tecnico.setDireccion(usuario.getDireccion());
                    tecnico.setNombres(usuario.getNombres());
                    tecnico.setPrimerApellido(usuario.getPrimerApellido());
                    tecnico.setSegundoApellido(usuario.getSegundoApellido());
                    tecnico.setTelefono(usuario.getTelefono());
                    tecnico.setCiudad(usuario.getCiudad());
                    tecnico.setEmail(usuario.getEmail());
                    
                    daoTecnico.update(tecnico);
                }
                launchSubwindow("Se ha actualizado correctamente");
            } else {
                launchSubwindow("Ha ocurrido un error");
            }
            
        } catch (DaoException ex) {
            Logger.getLogger(ActualizarDatos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidValueException ive){
            launchSubwindow(getMessage("error.form"));
        } 
        
    }

    @Override
    protected void setRequired(boolean required) {
        nombres.setRequired(required);
        notificacion.setRequired(required);
        primerApellido.setRequired(required);
        email.setRequired(required);        
        celular.setRequired(required);
        telefono.setRequired(required);
        direccion.setRequired(required);
        departamento.setRequired(required);
        ciudad.setRequired(required);
    }

    @Override
    protected void setMaxLength() {
        direccion.setMaxLength(40);
        nombres.setMaxLength(40);
        primerApellido.setMaxLength(25);
        segundoApellido.setMaxLength(25);
        telefono.setMaxLength(20);
    }

    @Override
    protected void validate() throws InvalidValueException {
        nombres.validate();
        notificacion.validate();
        primerApellido.validate();
        email.validate();
        celular.validate();
        telefono.validate();
        direccion.validate();
        departamento.validate();
        ciudad.validate();
    }
}
