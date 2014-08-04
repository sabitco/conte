package co.org.conte.sgm.ui.crud;

import co.org.conte.enumerator.EstadoInspector;
import co.org.conte.sgm.dao.AsociacionDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Asociacion;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Inspector;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class AsociacionInspectorCrud extends GenericCrudMasterDetail<Asociacion, Inspector> {

    
    //Campos Asociacion
    private String activo;
    private TextField cargoFirmante;
    private TextField celularPresidente;
    private DateField fechaConvenio;
    private DateField fechaVigencia;
    private TextField firmante;
    private TextField nit;
    private TextField nombreAsociacion;
    private NativeSelect departamentoAsociacion;
    private NativeSelect ciudadAsociacion;
    private TextField direccionAsociacion;
    private NativeSelect estado;
    private String inactivo;
    private TextField telefonoAsociacion;
    private TextField celularAsociacion;
    private TextField emailAsociacion;
    private TextField emailAsociacionAlterno;
    private TextField presidente;
    private TextField sigla;
    
    //Campos Inspector
    private TextField codigoInspector;
    private NativeSelect departamentoInspector;
    private NativeSelect ciudadInspector;
    private TextField documentoInspector;
    private TextField nombresInspector;
    private TextField apellidosInspector;
    private TextField direccionInspector;
    private TextField telefonoInspector;
    private TextField celularInspector;
    private TextField emailInspector;
    private final NativeSelect estadoInspector;
    
    private List<Departamento> departamentos;
     
    public AsociacionInspectorCrud(Usuario usuario) {        
        super(Asociacion.class, Inspector.class);
        super.delete.setCaption("Inactivar");
        dao = new AsociacionDao();
        cargoFirmante = new TextField("Cargo Firmante:");
        fechaConvenio = new DateField("Fecha Convenio:");
        fechaVigencia = new DateField("Fecha Vigencia:");
        firmante = new TextField("Firmante:");
        nit = new TextField("NIT:");
        nombreAsociacion = new TextField("Nombre:");
        ciudadAsociacion = new NativeSelect("Ciudad:");
        departamentoAsociacion = new NativeSelect("Departamento:");
        direccionAsociacion = new TextField("Dirección:");
        telefonoAsociacion = new TextField("Télefono:");
        celularAsociacion = new TextField("Celular:");
        celularPresidente = new TextField("Celular Presidente:");
        emailAsociacion = new TextField("Correo:");
        emailAsociacionAlterno = new TextField("Correo Alterno:");
        presidente = new TextField("Presidente:");
        sigla = new TextField("Sigla:");
                
        codigoInspector = new TextField("Código");
        departamentoInspector = new NativeSelect("Departamento");
        ciudadInspector = new NativeSelect("Ciudad");
        documentoInspector = new TextField("Documento");
        nombresInspector = new TextField("Nombres");
        apellidosInspector = new TextField("Apellidos");
        direccionInspector = new TextField("Dirección");
        telefonoInspector = new TextField("Télefono");
        celularInspector = new TextField("Celular");
        emailInspector = new TextField("Correo");
        this.estadoInspector = new NativeSelect("Estado Inspector");
        departamentos = serviceUsuario.getAllDepartamentos();
        init();
        setDescriptions();
    }
    
    @Override
    public void addValidators(){
        emailAsociacion.addValidator(new EmailValidator(getMessage("validator.email")));
        emailAsociacionAlterno.addValidator(new EmailValidator(getMessage("validator.email")));
        emailInspector.addValidator(new EmailValidator(getMessage("validator.email")));
        codigoInspector.addValidator(new IntegerValidator(getMessage("validator.integer")));
    }
    
    
//    public void createDetail() {
//        entityDetail = new Inspector();
//        createTableDetail();
//        createDetailForm();
//        addDetail();  
//    }

    @Override
    public void createDetailForm(){
        detailForm.addField("codigo", codigoInspector);
        detailForm.addField("documento", documentoInspector);
        detailForm.addField("nombres", nombresInspector);
        detailForm.addField("apellidos", apellidosInspector);
        detailForm.addField("departamento", departamentoInspector);
        detailForm.addField("ciudad", ciudadInspector);
        detailForm.addField("direccion", direccionInspector);
        detailForm.addField("telefono", telefonoInspector);
        detailForm.addField("celular", celularInspector);
        detailForm.addField("email", emailInspector);
        super.detailForm.addField("estadoInspector", this.estadoInspector);
    }
    
    @Override
    public void createForm() {
        form.addField("sigla", sigla);
        form.addField("nombreAsociacion", nombreAsociacion);        
        form.addField("direccionAsociacion", direccionAsociacion);        
        form.addField("telefonoAsociacion", telefonoAsociacion);
        form.addField("departamentoAsociacion", departamentoAsociacion);
        form.addField("ciudadAsociacion", ciudadAsociacion);
        form.addField("celularAsociacion", celularAsociacion);
        form.addField("emailAsociacion", emailAsociacion);
        form.addField("emailAsociacionAlterno", emailAsociacionAlterno);
        form.addField("presidente", presidente);
        form.addField("celularPresidente", celularPresidente);
        form.addField("nit", nit);
        form.addField("fechaConvenio", fechaConvenio);
        form.addField("fechaVigencia", fechaVigencia);
        form.addField("firmante", firmante);
        form.addField("cargoFirmante", cargoFirmante);
        form.addField("estado", estado);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);     
        table.addContainerProperty("Sigla", String.class,  null);     
//        table.addContainerProperty("Nombre", String.class,  null);        
        table.addContainerProperty("Dirección", String.class,  null);   
        table.addContainerProperty("Telefono", String.class,  null);   
//        table.addContainerProperty("Ciudad", String.class,  null);
        table.addContainerProperty("Celular", String.class,  null);   
        table.addContainerProperty("Correo", String.class,  null);   
        table.addContainerProperty("Estado", String.class,  null);   
        List<Asociacion> asociaciones = getList();
        if(asociaciones != null && !asociaciones.isEmpty()){
            for(Asociacion a : asociaciones){
                table.addItem(new Object[]{
                    a.getCodigo(),
                    a.getSigla(),
//                    a.getNombre(),
                    a.getDireccion(),
                    a.getTelefono(),
//                    a.getCiudad().getNombre(),
                    a.getCelular(),
                    a.getEmail(),
                    (a.getEstado() ? "Activo" : "Inactivo")
                }, a);
            }            
        }         
    }
    
    @Override 
    public void createTableDetail(){
        detailTable.addContainerProperty("Codigo", Integer.class,  null);     
        detailTable.addContainerProperty("Documento", String.class,  null); 
        detailTable.addContainerProperty("Nombre", String.class,  null);     
        detailTable.addContainerProperty("Apellidos", String.class,  null);        
//        detailTable.addContainerProperty("Ciudad", String.class,  null);        
        detailTable.addContainerProperty("Dirección", String.class,  null);  
        detailTable.addContainerProperty("Telefono", String.class,  null);  
        detailTable.addContainerProperty("Celular", String.class,  null);  
        detailTable.addContainerProperty("Correo", String.class,  null);  
        detailTable.addContainerProperty("Estado", String.class,  null);
        
        Set<Inspector> inspectores = entity.getInspectors();        
        if(inspectores != null && !inspectores.isEmpty()){
            for(Inspector i : inspectores){
                detailTable.addItem(new Object[]{
                    i.getCodigo(),
                    i.getDocumento(),
                    i.getNombres(),
                    i.getApellidos(),      
//                    i.getCiudad(),
                    i.getDireccion(),
                    i.getTelefono(),
                    i.getCelular(),
                    i.getEmail(),
                    i.getEstadoInspector().getDescripcion()
                }, i);
            }            
        }
    }

    @Override
    public void onClickClear() {
        ciudadAsociacion.setValue(null);
        ciudadAsociacion.removeAllItems();
        departamentoAsociacion.setValue(null);
        departamentoAsociacion.removeAllItems();
        for(Departamento departamento : departamentos){
            departamentoAsociacion.addItem(departamento);
            departamentoAsociacion.setItemCaption(departamento, departamento.getNombre());
        }
        cargoFirmante.setValue("");
        estado.setValue(null);
        fechaConvenio.setValue(null);
        fechaVigencia.setValue(null);
        firmante.setValue("");
        nit.setValue("");
        nombreAsociacion.setValue("");        
        direccionAsociacion.setValue("");
        telefonoAsociacion.setValue("");
        celularAsociacion.setValue("");
        celularPresidente.setValue("");
        emailAsociacion.setValue("");
        emailAsociacionAlterno.setValue("");
        presidente.setValue("");
        sigla.setValue("");
    }
    
    @Override
    public void onClickClearDetail(){
        codigoInspector.setValue("");
        departamentoInspector.setValue(null);
        ciudadInspector.setValue(null);
        documentoInspector.setValue("");
        nombresInspector.setValue("");
        apellidosInspector.setValue("");
        direccionInspector.setValue("");
        telefonoInspector.setValue("");
        celularInspector.setValue("");
        emailInspector.setValue("");
    }

    @Override
    public void onClickCreate() {
        entity = new Asociacion();
    }

    @Override
    public void onClickDelete() {
        if(entity.getEstado()){
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar la"
                    + " Asociación "+entity.getNombre()+ "?",
                "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            try {
                                entity.setEstado(Boolean.FALSE);
                                dao.update(entity);
                                onClickReset();
                            } catch (DaoException ex) {
                                Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                    }
            });
        } else {
            launchSubwindow("Ya se encuentra inactiva");
        }
    }

    @Override
    public void onClickEdit() {
        try {
            entity = dao.findById(entity.getCodigo());
        } catch (DaoException ex) {
            Logger.getLogger(AsociacionInspectorCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
        departamentoAsociacion.setValue(null);
        departamentoAsociacion.removeAllItems();
        for(Departamento departamento : departamentos){
            departamentoAsociacion.addItem(departamento);
            departamentoAsociacion.setItemCaption(departamento, departamento.getNombre());
            if(departamento.getCodigo() == entity.getCiudad().getDepartamento().getCodigo()){
                departamentoAsociacion.setValue(departamento);
            }
        }
        
        cargoFirmante.setValue(entity.getCargoFirmante() != null ? entity.getCargoFirmante() : "");
        celularPresidente.setValue(entity.getCelularPresidente() != null ? entity.getCelularPresidente() : "");
        fechaConvenio.setValue(entity.getFechaConvenio());
        fechaVigencia.setValue(entity.getFechaVigencia());
        firmante.setValue(entity.getFirmanteConvenio() != null ? entity.getCargoFirmante() : "");
        nit.setValue(entity.getNit());
        nombreAsociacion.setValue(entity.getNombre());
//        ciudadAsociacion.setValue(entity.getCiudad());
        direccionAsociacion.setValue(entity.getDireccion());
        telefonoAsociacion.setValue(entity.getTelefono());
        celularAsociacion.setValue(entity.getCelular());
        emailAsociacion.setValue(entity.getEmail());
        emailAsociacionAlterno.setValue(entity.getEmailAlterno()!=null ? entity.getEmailAlterno() : "");
        estado.setValue(entity.getEstado() ? activo : inactivo);
        sigla.setValue(entity.getSigla());
        presidente.setValue(entity.getPresidente() != null ? entity.getPresidente() : "");
    }

    @Override
    public void process() {
        
        entity.setCargoFirmante(getValue(cargoFirmante));
        entity.setCelular(getValue(celularAsociacion));
        entity.setCelularPresidente(getValue(celularPresidente));
        entity.setCiudad((Ciudad) ciudadAsociacion.getValue());
        entity.setDireccion(getValue(direccionAsociacion));
        entity.setFechaConvenio((Date) fechaConvenio.getValue());
        entity.setFechaVigencia((Date) fechaVigencia.getValue());
        entity.setFirmanteConvenio(getValue(firmante));
        entity.setNombre(getValue(nombreAsociacion));
        entity.setTelefono(getValue(telefonoAsociacion));
        entity.setEmail(getValue(emailAsociacion).toLowerCase());       
        entity.setEmailAlterno(getValue(emailAsociacionAlterno).toLowerCase());
        entity.setEstado(getValue(estado).equalsIgnoreCase("Activo"));
        entity.setNit(getValue(nit));
        entity.setSigla(getValue(sigla));
        entity.setPresidente(getValue(presidente));
    }
    
    @Override
    public void processDetail(){
        entityDetail.setApellidos(getValue(apellidosInspector));
        entityDetail.setAsociacion(entity);
        entityDetail.setCelular(getValue(celularInspector));
        entityDetail.setCiudad((Ciudad) ciudadInspector.getValue());
        entityDetail.setCodigo(Integer.parseInt(getValue(codigoInspector)));
        entityDetail.setDireccion(getValue(direccionInspector));
        entityDetail.setDocumento(getValue(documentoInspector));
        entityDetail.setEmail(getValue(emailInspector).toLowerCase());
        entityDetail.setNombres(getValue(nombresInspector));
        entityDetail.setTelefono(getValue(telefonoInspector));
        entityDetail.setEstadoInspector((EstadoInspector) estadoInspector.getValue());
    }

    @Override
    public void setRequired(boolean required) {
        
        ciudadAsociacion.setRequired(required);
        departamentoAsociacion.setRequired(required);
        fechaConvenio.setRequired(required);
        fechaVigencia.setRequired(required);
        nit.setRequired(required);
        nombreAsociacion.setRequired(required);
        direccionAsociacion.setRequired(required);
        telefonoAsociacion.setRequired(required);
        emailAsociacion.setRequired(required);
        estado.setRequired(required);
        sigla.setRequired(required);
        
        ciudadInspector.setRequired(required);
        codigoInspector.setRequired(required);
        departamentoInspector.setRequired(required);
        documentoInspector.setRequired(required);
        nombresInspector.setRequired(required);
        apellidosInspector.setRequired(required);
        direccionInspector.setRequired(required);
        celularInspector.setRequired(required);
        emailInspector.setRequired(required);
        estadoInspector.setRequired(required);
    }

    @Override
    public boolean validate() {
        return (
                cargoFirmante.isValid() &&
                celularPresidente.isValid() &&
                ciudadAsociacion.isValid() &&
                departamentoAsociacion.isValid() &&
                fechaConvenio.isValid() && 
                fechaVigencia.isValid() &&
                firmante.isValid() &&
                nit.isValid() &&
        nombreAsociacion.isValid() &&
//        ciudadAsociacion.isValid() &&
        direccionAsociacion.isValid() &&
        telefonoAsociacion.isValid() &&
        celularAsociacion.isValid() &&
        emailAsociacion.isValid() &&
                emailAsociacionAlterno.isValid() &&
        estado.isValid() &&
                presidente.isValid() &&
                sigla.isValid()
                
                );
    }
    
    @Override
    public boolean validateDetail(){
        return (
                estadoInspector.isValid()
                && ciudadInspector.isValid() &&
                codigoInspector.isValid() &&
                documentoInspector.isValid() &&
                nombresInspector.isValid() &&
                apellidosInspector.isValid() &&
                direccionInspector.isValid() &&
                telefonoInspector.isValid() &&
                celularInspector.isValid() &&
                emailInspector.isValid() &&
                departamentoInspector.isValid()
                );
    }
    
    private void init(){
        activo = "Activo";
        inactivo = "Inactivo";
        estado = new NativeSelect("Estado");
        estado.addItem(activo);
        estado.addItem(inactivo); 
        fechaConvenio.setDateFormat("dd/MM/yyyy"); 
        fechaVigencia.setDateFormat("dd/MM/yyyy"); 
        for(Departamento departamento : departamentos){
            departamentoAsociacion.addItem(departamento);
            departamentoAsociacion.setItemCaption(departamento, departamento.getNombre());
        }
        for(Departamento departamento : departamentos){
            departamentoInspector.addItem(departamento);
            departamentoInspector.setItemCaption(departamento, departamento.getNombre());
        }
        for (EstadoInspector ei : EstadoInspector.values()) {
            this.estadoInspector.addItem(ei);
            this.estadoInspector.setItemCaption(ei, ei.getDescripcion());
        }
        createForm();
        addValidators();
        setRequired(true);         
        addMyListeners();
        setMaxLengthMaster();
        setMaxLengthDetail();
        super.deleteDetail.setEnabled(false);
        super.detailForm.getFooter().removeComponent(super.deleteDetail);
    }

    @Override
    public void setDetailFields() {
        if(entityDetail!=null){
            Departamento selected = entityDetail.getCiudad().getDepartamento();
            departamentoInspector.removeAllItems();
            departamentoInspector.setValue(null);
            for(Departamento d : departamentos){
                departamentoInspector.addItem(d);
                departamentoInspector.setItemCaption(d, d.getNombre());
                if(selected.getCodigo() == d.getCodigo()){
                    departamentoInspector.setValue(d);
                }
            }
            codigoInspector.setValue(entityDetail.getCodigo());
            documentoInspector.setValue(entityDetail.getDocumento());
            nombresInspector.setValue(entityDetail.getNombres());
            apellidosInspector.setValue(entityDetail.getApellidos());
            departamentoInspector.setValue(entityDetail.getCiudad().getDepartamento());
            direccionInspector.setValue(entityDetail.getDireccion());
            telefonoInspector.setValue(entityDetail.getTelefono());
            celularInspector.setValue(entityDetail.getCelular());
            emailInspector.setValue(entityDetail.getEmail());
            for (EstadoInspector ei : EstadoInspector.values()) {
                this.estadoInspector.addItem(ei);
                this.estadoInspector.setItemCaption(ei, ei.getDescripcion());
                System.out.println("Estado inspector = " + entityDetail.getEstadoInspector().getDescripcion() + "\n\n\n\n");
                if (ei == entityDetail.getEstadoInspector()) {
                    estadoInspector.setValue(ei);
                }
            }
        }
    }
    
    private void addMyListeners(){
        this.departamentoAsociacion.setImmediate(true);
        this.departamentoAsociacion.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamentoAsociacion.getValue();
                ciudadAsociacion.removeAllItems();
                ciudadAsociacion.setValue(null);
                if(selected != null){                    
                    selected = serviceUsuario.findDepartamentoById(selected.getCodigo());
                    if(!selected.getCiudads().isEmpty()){
                        for(Ciudad c : selected.getCiudads()){
                            ciudadAsociacion.addItem(c);
                            ciudadAsociacion.setItemCaption(c, c.getNombre());
                            if(entity!=null && entity.getCiudad()!=null){
                                if(entity.getCiudad().getCodigo()==c.getCodigo()){
                                    ciudadAsociacion.setValue(c);
                                }
                            }
                        }
                    }                    
                }
                
             }
        });
        
        this.departamentoInspector.setImmediate(true);
        this.departamentoInspector.addListener(new  Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Departamento selected = (Departamento) departamentoInspector.getValue();
                if(selected != null){                    
                    ciudadInspector.removeAllItems();
                    ciudadInspector.setValue(null);
                    selected = serviceUsuario.findDepartamentoById(selected.getCodigo());
                    if(!selected.getCiudads().isEmpty()){
                        for(Ciudad c : selected.getCiudads()){
                            ciudadInspector.addItem(c);
                            ciudadInspector.setItemCaption(c, c.getNombre());
                            if(entityDetail!=null && entityDetail.getCiudad()!=null){
                                if(entityDetail.getCiudad().getCodigo()==c.getCodigo()){
                                    ciudadInspector.setValue(c);
                                }
                            }
                        }
                    }                    
                }
                
             }
        });
        
        codigoInspector.addListener(new Property.ValueChangeListener() {

            public void valueChange(ValueChangeEvent event) {
                if(codigoInspector.isValid()){
                    try {
                        if(Integer.parseInt(getValue(codigoInspector))!=entityDetail.getCodigo()){
                            if(daoDetail.findById(Integer.parseInt(getValue(codigoInspector)))!=null){
                                launchSubwindow("Este codigo ya se encuentra registrado");
                                createDetail.setEnabled(false);
                                editDetail.setEnabled(false);
                                deleteDetail.setEnabled(false);
                            } else {
                                createDetail.setEnabled(true);
                                editDetail.setEnabled(true);
                                deleteDetail.setEnabled(true);
                            }                            
                        } else {
                            createDetail.setEnabled(true);
                            editDetail.setEnabled(true);
                            deleteDetail.setEnabled(true);
                        }                        
                    } catch (DaoException ex) {
                        logger.error("Error buscando Inspector");                        
                    }
                }
            }
        });
        
    }
    
    private void setMaxLengthMaster(){
        cargoFirmante.setMaxLength(20);
        celularPresidente.setMaxLength(20);
        firmante.setMaxLength(80);
        nit.setMaxLength(20);
        sigla.setMaxLength(25);
        nombreAsociacion.setMaxLength(200);
        direccionAsociacion.setMaxLength(60);
        telefonoAsociacion.setMaxLength(20);
        celularAsociacion.setMaxLength(20);
        emailAsociacion.setMaxLength(60);
        emailAsociacionAlterno.setMaxLength(60);
        presidente.setMaxLength(80);
    }
    
    private void setMaxLengthDetail(){
        codigoInspector.setMaxLength(10);
        documentoInspector.setMaxLength(10);
        nombresInspector.setMaxLength(20);
        apellidosInspector.setMaxLength(20);
        direccionInspector.setMaxLength(60);
        telefonoInspector.setMaxLength(20);
        celularInspector.setMaxLength(20);
    }
    
    private void setDescriptions(){
        nit.setDescription(getMessage("tooltip.document_nit"));
    }
}