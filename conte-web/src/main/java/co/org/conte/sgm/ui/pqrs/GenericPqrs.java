package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Pqrs;
import co.org.conte.sgm.entity.PqrsDescripcion;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.PqrsService;
import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import java.util.List;

/**
 *
 * @author jam
 */
public class GenericPqrs extends GenericContent {
    
    protected Button action;
    protected Button clear;    
    protected NativeSelect estado;
    protected Form form;
    protected TextArea observaciones;
    protected Pqrs pqrs;
    protected NativeSelect responsable;
    private Button reset;
    private PqrsService service;
    private Table table;  
    
    private TextField apellidos;
    private TextField celular;
    private TextField correo;
    private NativeSelect ciudad;
    private NativeSelect departamento;
    private TextField direccion;
    private List<Departamento> departamentos;
    private TextField documento;
    private TextField nombres;            
    private TextField telefono;
    private NativeSelect tipoDocumento;
    
    
    public GenericPqrs(Usuario usuario) {
        
        super(usuario);
        action = new Button();
        apellidos = new TextField("Apellidos");
        celular = new TextField("Celular");
        ciudad = new NativeSelect("Ciudad");
        clear = new Button("Limpiar");
        correo = new TextField("Correo");        
        departamento = new NativeSelect("Departamento");
        direccion = new TextField("Dirección");        
        documento = new TextField("Documento");
        estado = new NativeSelect("Estado");
        form = new Form();
        nombres = new TextField("Nombres:");
        observaciones = new TextArea("Observaciones"); 
        reset = new Button("Volver");
        service = new PqrsService();
        table = new Table();
        telefono = new  TextField("Telefono:");
        tipoDocumento = new NativeSelect("Tipo Documento");
        departamentos = serviceUsuario.getAllDepartamentos();
        
        table.setSelectable(true);
        init();
        addListeners();
        createForm();
    }
    
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);      
        table.addContainerProperty("Tipo Documento", String.class, null);
        table.addContainerProperty("Documento", String.class, null);
        table.addContainerProperty("Nombres", String.class,  null);   
        table.addContainerProperty("Apellidos", String.class,  null);   
        table.addContainerProperty("Email", String.class,  null);   
        table.addContainerProperty("Celular", String.class,  null);   
       // table.addContainerProperty("Tipo", Character.class,  null);   
        List<Pqrs> ps = service.getAllPqrs();
        if(ps != null && !ps.isEmpty()){
            for(Pqrs pqr : ps){
                table.addItem(new Object[]{                    
                    pqr.getConsecutivo(),
                    pqr.getTipoDocumento().getNombre(),
                    pqr.getDocumento(),
                    pqr.getNombres(),
                    pqr.getApellidos(),
                    pqr.getEmail(),
                    pqr.getCelular()
                //    pqr.getTipo()
                }, pqr);
            }            
        }    
    }
    
    protected void createForm() {
        form.addField("tipoDocumento", tipoDocumento);
        form.addField("documento", documento);
        form.addField("nombres", nombres);
        form.addField("apellidos", apellidos);
        form.addField("correo", correo);
        form.addField("celular", celular);
        form.addField("telefono", telefono);
        form.addField("departamento", departamento);
        form.addField("ciudad", ciudad);
        form.addField("direccion", direccion);
//        form.addField("observaciones", observaciones);    
        form.getFooter().addComponent(reset);
    }
    
    protected void addListeners(){
        action.setImmediate(true);
        this.action.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(pqrs != null){
                    onClickAction();
                    removeAllComponents();
                    addComponent(form);
                } else {
                    launchSubwindow("Debe seleccionar uno");
                }
            }
        });
        
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
                            if(pqrs != null){
                                if(pqrs.getCiudad()!=null){
                                    if(pqrs.getCiudad().getCodigo()==c.getCodigo()){
                                        ciudad.setValue(c);
                                    }
                                }
                            }
                        }
                    }                    
                }
             }
        });
        
        reset.setImmediate(true);
        reset.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                removeAllComponents();
                init();
            }
        });  
        
        table.setImmediate(true);
        table.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                pqrs = ((Pqrs)table.getValue());
            }
        });  
        
    }
    
    private void onClickAction(){
        setReadOnlyFields(false);
        pqrs = service.findById(pqrs);        
        departamento.setValue(null);
        departamento.removeAllItems();
        for(Departamento d : departamentos){
            departamento.addItem(d);
            departamento.setItemCaption(d, d.getNombre());
            if(pqrs!=null){
                if(pqrs.getCiudad().getDepartamento().getCodigo() == d.getCodigo()){
                    departamento.setValue(d);
                }                
            }
            
        }
        
        
        apellidos.setValue(pqrs.getApellidos());
        celular.setValue(pqrs.getCelular());
        correo.setValue(pqrs.getEmail());
        direccion.setValue(pqrs.getDireccion());
        documento.setValue(pqrs.getDocumento());
        nombres.setValue(pqrs.getNombres());
//        observaciones.setValue(pqrs.getSolicitud());
        telefono.setValue(pqrs.getTelefono());
        tipoDocumento.addItem(pqrs.getTipoDocumento().getNombre());
        tipoDocumento.setValue(pqrs.getTipoDocumento().getNombre());
       
        if(pqrs.getPqrsDescripciones() != null && !pqrs.getPqrsDescripciones().isEmpty()){
            TextArea descripcion;
            for(PqrsDescripcion pd : pqrs.getPqrsDescripciones()){
                descripcion = new TextArea("Descripcion", pd.getDescripcion());
                descripcion.setReadOnly(true);
                form.addField("", descripcion);
            }
        }
        if(this instanceof ResponderPqrs){
            observaciones.setCaption("Respuesta");
            form.addField("respuesta", observaciones);
        }
        
        if(this instanceof AsignarPqrs){
            responsable = new NativeSelect("Responsable");
            responsable.setRequired(true);
            form.addField("responsable", responsable);
        }
        
//        form.addField("estado", estado);
        
        setReadOnlyFields(true);
    }
    
    private void onClickClear(){
//        apellidos.setValue("");
//        celular.setValue("");
//        correo.setValue("");
//        departamento.setValue(null);
//        direccion.setValue("");
//        nombres.setValue("");
//        observaciones.setValue("");
//        telefono.setValue("");
    }
 
    private void setReadOnlyFields(boolean readOnly){
        apellidos.setReadOnly(readOnly);
        celular.setReadOnly(readOnly);
        ciudad.setReadOnly(readOnly);
        correo.setReadOnly(readOnly);
        departamento.setReadOnly(readOnly);
        direccion.setReadOnly(readOnly);
        documento.setReadOnly(readOnly);
        nombres.setReadOnly(readOnly);
        telefono.setReadOnly(readOnly);
        tipoDocumento.setReadOnly(readOnly);
    }
    
    private void init(){
        createTable();
        addComponent(table);
        addComponent(action);
    }
}