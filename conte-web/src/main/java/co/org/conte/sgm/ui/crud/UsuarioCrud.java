package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.container.UsuarioContainer;
import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.TipoDocumentoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.UsuarioService;
import co.org.conte.sgm.util.Password;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author J4M0
 */
public class UsuarioCrud extends GenericCrud<Usuario>{
    
    private String activo;
    private String inactivo;
    private String bloqueado;
    private TipoDocumentoDao daoTipoDocumento;
    private TextField documento;
    private List<TipoDocumento> documentos;
    private TextField nombres;
    private TextField primerApellido;
    private TextField segundoApellido;    
    private PasswordField contrasenia;
    private TextField email;
    private NativeSelect estado;
    private TextField celular;
    private TextField telefono;
    private TextField direccion;
    private ComboBox ciudad;
    private ComboBox departamento;
    private List<Departamento> departamentos;
    private NativeSelect notificacion;
    private ComboBox tipoDocumento;
    private NativeSelect perfil;
    private List<Perfil> perfiles;
    private UsuarioService service;
    private TextField filter;
    private String si = "Si";
    private String no = "No";
    private UsuarioContainer uc;
    
    public UsuarioCrud(Usuario usuario){        
        super(Usuario.class );        
        super.delete.setCaption("Inactivar");
        activo = "Activo";
        inactivo = "Inactivo";
        bloqueado = "Bloqueado";
        daoTipoDocumento = new TipoDocumentoDao();
        service = new UsuarioService();
        
        init();
    }
    
    @Override
    public void addValidators(){
        email.addValidator(new EmailValidator(getMessage("validator.email")));
    }
        
    @Override
    public void createForm(){
        form.addField("tipoDocumento", tipoDocumento);
        form.addField("documento", documento);
        form.addField("nombres", nombres);
        form.addField("primerApellido", primerApellido);
        form.addField("segundoApellido", segundoApellido);
        form.addField("Perfil", perfil);
        form.addField("email", email);
        email.addValidator(new EmailValidator("El correo no es Valido"));
        form.addField("celular", celular);
        form.addField("telefono", telefono);
        form.addField("departamento", departamento);
        form.addField("ciudad", ciudad);
        form.addField("direccion", direccion);
        form.addField("Notificacion", notificacion);
        form.addField("estado", estado);
        form.addField("Contraseña", contrasenia);
    }

    @Override
    public void createTable() {
       uc = new UsuarioContainer();
       for(Usuario u : serviceUsuario.getUsuarios()){
           uc.addUsuario(u);
       }
       table.setContainerDataSource(uc);        
    }

    @Override
    public void onClickClear() {
        estado.setValue(null);
        notificacion.setValue(null);
        
        tipoDocumento.removeAllItems();
        tipoDocumento.setValue(null);
        for(TipoDocumento td : documentos){
            tipoDocumento.addItem(td);
            tipoDocumento.setItemCaption(td, td.getNombre());
        }
        
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
            
        }
        
        perfil.removeAllItems();
        perfil.setValue(null);
        
        for(Perfil p : perfiles){
            perfil.addItem(p);
            perfil.setItemCaption(p, p.getNombre());
            
        }
        
        ciudad.removeAllItems();
        ciudad.setValue(null);
        
        contrasenia.setValue("");
        departamento.setValue(null);
        documento.setValue("");
        nombres.setValue("");
        primerApellido.setValue("");
        segundoApellido.setValue("");
        email.setValue("");
        celular.setValue("");
        telefono.setValue("");
        direccion.setValue("");
        tipoDocumento.setValue("");
    }
    
    @Override
    public void onClickCreate() {
        
    }
    
    @Override
    public void onClickEdit() {
        tipoDocumento.removeAllItems();
        for(TipoDocumento td : documentos){
            tipoDocumento.addItem(td);
            tipoDocumento.setItemCaption(td, td.getNombre());
            if(td.getNombre().equalsIgnoreCase(entity.getTipoDocumento())){
                tipoDocumento.setValue(td);
            }
        }
        
        departamento.removeAllItems();
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
            if(entity.getCiudad()!=null){
                if(entity.getCiudad().getDepartamento().getCodigo() == d.getCodigo()){
                    departamento.setValue(d);
                }
            }
            
        }
        
        perfil.removeAllItems();
        
        for(Perfil p : perfiles){
            perfil.addItem(p);
            perfil.setItemCaption(p, p.getNombre());
            if(entity.getPerfil()!=null){
                if(entity.getPerfil().getCodigo() == p.getCodigo()){
                    perfil.setValue(p);
                }
            }
        }
            
        documento.setValue(entity.getDocumento());
        nombres.setValue(entity.getNombres());
        primerApellido.setValue(entity.getPrimerApellido());
        segundoApellido.setValue(entity.getSegundoApellido());
        email.setValue(entity.getEmail());
        celular.setValue((entity.getCelular() != null) ? entity.getCelular() : "");
        telefono.setValue((entity.getTelefono()!=null) ? entity.getTelefono() : "");
        direccion.setValue((entity.getDireccion()!=null) ? entity.getDireccion() : "");
        if(entity.isNotificacion()!=null){
            notificacion.setValue(entity.isNotificacion() ? si : no);
        }
        
        if(entity.getEstado().equalsIgnoreCase(activo)){
            estado.setValue(activo);
        } else if (entity.getEstado().equalsIgnoreCase(inactivo)){
            estado.setValue(inactivo);
        } else if (entity.getEstado().equalsIgnoreCase(bloqueado)){
            estado.setValue(bloqueado);
        }
    }
    
    @Override
    public void process() {        
        if(validate()){
            Perfil p = (Perfil) perfil.getValue();
            if(p.getCodigo()==2){
                Tecnico t = new Tecnico();
                t.setCelular(getValue(celular));
                t.setCiudad((Ciudad) ciudad.getValue());
                t.setDireccion(getValue(direccion));
                t.setDocumento(getValue(documento));
                t.setEmail(getValue(email).toLowerCase());
                t.setNombres(getValue(nombres));
                t.setNotificacion((notificacion.getValue().toString().equalsIgnoreCase(si)));
                t.setPrimerApellido(getValue(primerApellido));
                t.setSegundoApellido(getValue(segundoApellido));
                t.setTelefono(getValue(telefono));
                
                TecnicoDao daoTecnico = new TecnicoDao();
                try {
                    daoTecnico.insert(t);
                } catch (DaoException ex) {
                    Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            }
            entity.setCelular(getValue(celular));
            entity.setCiudad((Ciudad) ciudad.getValue());
            entity.setDireccion(getValue(direccion));
            entity.setDocumento(getValue(documento));
            entity.setEmail(getValue(email).toLowerCase());
            entity.setEstado(getValue(estado));
            entity.setNombres(getValue(nombres));
            entity.setNotificacion((notificacion.getValue().toString().equalsIgnoreCase(si)));
            entity.setPerfil(p);
            entity.setPrimerApellido(getValue(primerApellido));
            entity.setSegundoApellido(getValue(segundoApellido));
            entity.setTelefono(getValue(telefono));
            entity.setTipoDocumento(getValue(tipoDocumento));
            
            if(!getValue(contrasenia).isEmpty()){
                entity.setContrasena(Password.encryptPassword(getValue(contrasenia)));
            }
        } 
    }
    
    @Override
    public void setRequired(boolean required) {
        documento.setRequired(required);
        nombres.setRequired(required);
        notificacion.setRequired(required);
        primerApellido.setRequired(required);
        email.setRequired(required);
        estado.setRequired(required);
        perfil.setRequired(required);
        celular.setRequired(required);
        telefono.setRequired(required);
        direccion.setRequired(required);
        tipoDocumento.setRequired(required);
        departamento.setRequired(required);
        ciudad.setRequired(required);
    }
    
    private void addListenersForm(){
        tipoDocumento.setImmediate(true);
        this.tipoDocumento.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                documento.removeAllValidators();
                if(tipoDocumento.getValue() != null && !tipoDocumento.getValue().toString().isEmpty()){
                    if(!tipoDocumento.getValue().toString().equalsIgnoreCase("PAP")){
                        documento.addValidator(new IntegerValidator("Solo puede ingresar números"));            
                    }
                    if(entity==null){
                        entity = new Usuario();
                    }
                }
             }
        });

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
                            if(entity!=null && entity.getCiudad()!=null){
                                if(entity.getCiudad().getCodigo()==c.getCodigo()){
                                    ciudad.setValue(c);
                                }
                            }
                        }
                    }                    
                }
             }
        });
        
        filter.addListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(final TextChangeEvent event) {
                uc.removeAllContainerFilters();
                uc.addContainerFilter(new Filter() {

                    @Override
                    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }
                        
                         return filterByProperty("Documento", item,
                                event.getText())
                                || filterByProperty("Nombres", item,
                                        event.getText())
                                || filterByProperty("Primer Apellido", item,
                                        event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(Object propertyId) {
                         if (propertyId.equals("Documento")
                                || propertyId.equals("Nombres")
                                || propertyId.equals("Primer Apellido")){
                             return true;
                         }                           
                        return false;
                    }
                });
            }
        });
        
    }
    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null)
            return false;
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.startsWith(text.toLowerCase().trim()))
            return true;
        return false;
    }
    
    private void init(){        
        filter = new TextField();        
        documento = new TextField("Documento:", "");
        nombres = new TextField("Nombres:", "");
        primerApellido = new TextField("Primer Apellido:", "");
        segundoApellido = new TextField("Segundo Apellido:", "");
        celular = new TextField("Celular:", celular);
        email = new TextField("Email:", "");
        telefono = new TextField("Telefono:", "");
        direccion = new TextField("Dirección:", "");
        contrasenia = new PasswordField("Contraseña:", ""); 
        tipoDocumento = new ComboBox("Tipo Documento:");
        departamentos = service.getAllDepartamentos();
        departamento = new ComboBox("Departamento:");
        ciudad = new ComboBox("Ciudad:");
        perfil = new NativeSelect("Perfil:");
        
        estado = new NativeSelect("Estado:");
        
        estado.addItem(activo);
        estado.addItem(inactivo);
        estado.addItem(bloqueado);
        
        notificacion = new NativeSelect("Notificacion:");
        notificacion.addItem(si);
        notificacion.addItem(no);
        
        try {
            perfiles = service.getAllPerfiles();
            documentos = daoTipoDocumento.findAll();            
            
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
        createForm();
        addListenersForm();
        setRequired(true);
        setMaxLength();
        filter.setWidth("400px");
        filter.setInputPrompt("Ingrese documento, nombres o primer apellido del usuario a buscar");
        removeAllComponents();
        addComponent(filter); 
        addComponent(table);
        addComponent(buttons);
        
        
    }

    @Override
    public boolean validate() {
        return (documento.isValid() && 
                estado.isValid() &&
                tipoDocumento.isValid() &&
                ciudad.isValid() && 
                notificacion.isValid() &&
                perfil.isValid() && 
                ((entity.getCodigo()<1) ? !getValue(contrasenia).isEmpty() : true)); 
    }

    @Override
    public void onClickDelete() {
        if(!entity.getEstado().equalsIgnoreCase("Inactivo")){
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar al usuario?",
            "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                @Override
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        try {
                            entity.setEstado("Inactivo");
                            dao.update(entity);
                            onClickReset();
                        } catch (DaoException ex) {
                            Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                }
            });       
        } else {
            launchSubwindow("Ya se encuentra Inactivo");
        }
    }
    
    private void setMaxLength(){   
        direccion.setMaxLength(40);
        nombres.setMaxLength(40);
        primerApellido.setMaxLength(25);
        segundoApellido.setMaxLength(25);
        telefono.setMaxLength(20);
    }
       
}