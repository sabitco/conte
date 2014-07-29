package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.dao.AsociacionDao;
import co.org.conte.sgm.dao.DepartamentoDao;
import co.org.conte.sgm.dao.InspectorDao;
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
import co.org.conte.sgm.entity.ReferenciaPersonal;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author Andrés Mise Olivera
 */
@Configurable(preConstruction = true)
public class CrearSolicitud extends SolicitudForm {
    
    public CrearSolicitud(Usuario usuario) {
        super(usuario);  
        setCaption("Crear Solicitud");
        reset.setCaption("Limpiar");
        daoAsociacion = new AsociacionDao();
        daoInspector = new InspectorDao();
        daoDepartamento = new DepartamentoDao();
        daoReferenciaPersonal = new ReferenciaPersonalDao();
        daoSolicitud = new SolicitudDao();
        daoTecnico = new TecnicoDao();
        daoTipoDocumento = new TipoDocumentoDao();
        daoTipoSolicitud = new TipoSolicitudDao();
        daoUsuario = new UsuarioDao();
        solicitud = new Solicitud();
        esEmpresa = usuario.getTipoDocumento().equalsIgnoreCase("NIT");
        tecnico = new Tecnico();
        tecnico.setDocumento(usuario.getDocumento());
        
        try {
            if(usuario!=null){
                List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);
                if(tecnicos.size()>0){
                    tecnico = tecnicos.get(0) ;  
                } else {
                    tecnico = null;
            }
        }
        } catch (DaoException ex) {
            Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(esEmpresa || this.usuario.getSolicituds().isEmpty()){
            init();
        } else {
            boolean ponerFormulario = true;
            for(Solicitud s : this.usuario.getSolicituds()){
                if(s.getEstado().getCodigo() != 6){
                    if(s.getEstado().getCodigo() != 17){
                        removeAllComponents();
                        message.setCaption("Ya se encuentra una solicitud en tramite " + s.getEstado().getCodigo());
                        addComponent(message);
                        ponerFormulario = false;
                        break;
                    }
                }
            }
            if(ponerFormulario){
                init();
            }
        }
    }
        
    private void init() {
        initPublic();
        tipoSolicitud.setReadOnly(false);
        tipoSolicitud.setValue(null);
        tipoSolicitud.removeAllItems();
        
        tipoDocumento.addItem(usuario.getTipoDocumento());
        tipoDocumento.setValue(usuario.getTipoDocumento());
        tipoDocumento.setReadOnly(true);      
        documentoTecnico = new TextField("Documento Tecnico", "");
        documentoTecnico.setMaxLength(20);
        documentoTecnico.setImmediate(true);
        validate = new Button("Validar");        
        nombres.setValue((usuario.getTipoDocumento().equalsIgnoreCase("NIT")) ? "" : usuario.getNombres());
        primerApellido = new TextField("Primer Apellido", (usuario.getTipoDocumento().equalsIgnoreCase("NIT")) ? "" : usuario.getPrimerApellido());
        segundoApellido = new TextField("Segundo Apellido", 
                (usuario.getSegundoApellido()==null || usuario.getTipoDocumento().equalsIgnoreCase("NIT"))
                ? "" : usuario.getSegundoApellido());
        
        notificacion = new NativeSelect("Desea Notificaciones por correo");
        
        //correo.setReadOnly(true);
        departamento = new ComboBox("Departamento");
        departamentoEmpresa = new ComboBox("Departamento Empresa");
        ciudadEmpresa = new ComboBox("Ciudad Empresa");
        telefono = new TextField("Telefono", 
                (usuario.getTelefono()==null || usuario.getTipoDocumento().equalsIgnoreCase("NIT")) ? "" : usuario.getTelefono());      
        empresa = new TextField("Empresa");
        telefonoEmpresa = new TextField("Telefono Empresa");
                
        entero = new NativeSelect("Tramite realizado a través de");        
        
        List<Departamento> departamentos = null;
        List<TipoDocumento> documentos = null;
        List<Inspector> inspectores = null;
        List<TipoSolicitud> solicitudes = null;
        try {
            departamentos = daoDepartamento.findAll();
            inspectores = daoInspector.findAll();
            solicitudes = daoTipoSolicitud.findAll();
            if(esEmpresa){
                documentos = daoTipoDocumento.findAll();
            }
        } catch (DaoException ex) {
            Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(usuario.getTipoDocumento().equalsIgnoreCase("PAP")){
            TipoSolicitud ts = new TipoSolicitud(6);
            ts.setNombre("Licencia Especial");
            tipoSolicitud.addItem(ts);
            tipoSolicitud.setItemCaption(ts, ts.getNombre());
            
        } else {
            if(tecnico != null){
                for (TipoSolicitud d:  solicitudes ) {
                    String matricula = tecnico.getMatricula() == null ? "" : tecnico.getMatricula().trim();
                    if(tecnico.getMatricula()== null || matricula.isEmpty()){
                        if(d.getCodigo() == 3 || d.getCodigo() == 5 || d.getCodigo() == 2 || d.getCodigo() == 4 ){
                            continue;
                        } else {
                            tipoSolicitud.addItem(d);
                            tipoSolicitud.setItemCaption(d, d.getNombre());
                            
                        }
                    } else {
                        if(d.getCodigo() == 6){
                            continue;
                        } else {
                            tipoSolicitud.addItem(d);
                            tipoSolicitud.setItemCaption(d, d.getNombre());                            
                        }                        
                    }
                    
                }                
            } else {
                for (TipoSolicitud d:  solicitudes ) {
                    tipoSolicitud.addItem(d);
                    tipoSolicitud.setItemCaption(d, d.getNombre());
                }            
            }           
        } 
        
        for(Departamento dep : departamentos){
            departamento.addItem(dep);
            departamento.setItemCaption(dep, dep.getNombre());            
            departamentoEmpresa.addItem(dep);
            departamentoEmpresa.setItemCaption(dep, dep.getNombre());            
        }
        
        for(Inspector i : inspectores){
            inspector.addItem(i);
            inspector.setItemCaption(i, i.getNombres() +" "+i.getApellidos());            
        }
        
        if(esEmpresa){
            
            for (TipoSolicitud d:  solicitudes ) {
                tipoSolicitud.addItem(d);
                tipoSolicitud.setItemCaption(d, d.getNombre());    
            }
            
            
            tipoDocumentoTecnico = new ComboBox("Tipo Documento Técnico");
            for(TipoDocumento td :documentos){
                if(!td.getNombre().equalsIgnoreCase("NIT")){
                    tipoDocumentoTecnico.addItem(td);
                    tipoDocumentoTecnico.setItemCaption(td, td.getNombre());
                }
                
            }
            this.tipoDocumentoTecnico.setImmediate(true);
            this.tipoDocumentoTecnico.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                documentoTecnico.removeAllValidators();
                documentoTecnico.setImmediate(true);
                documentoTecnico.setMaxLength(20);
                if(!getValue(tipoDocumentoTecnico).equalsIgnoreCase("PAP")){
                    documentoTecnico.addValidator(new DoubleValidator("Solo puede ingresar números"));
                }
             }
            });
        } else {
            correo.setValue(usuario.getEmail());
            correo.setReadOnly(true);
            fechaNacimiento.setValue(tecnico.getFechaNacimiento());
            
        }
        entero.addItem("Empresa");
        entero.addItem("Asociación"); 
        entero.addItem("Instituto de Formación Técnica");
        entero.addItem("CONTE"); 
        notificacion.addItem(si);
        notificacion.addItem(no);
        
        
        createForm();        
        super.removeAllComponents();
        super.addComponent(formLayout);
                
        this.validate.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                onClickValidate();              
            }
        });
        
        empresa.setMaxLength(80);
        enteroPor.setMaxLength(60);
        nombres.setMaxLength(40);
        nombresFamiliar.setMaxLength(60);        
        primerApellido.setMaxLength(20);
        segundoApellido.setMaxLength(20);
        telefono.setMaxLength(20);
        telefonoEmpresa.setMaxLength(20);
        telefonoFamiliar.setMaxLength(20);
        
    }

    @Override
    protected void createForm() {
        addListeners();
        setRequired(true);
        Form f = new Form();
        Panel panel = new Panel("Tipo Solicitud");
        f.addField("tipo",tipoSolicitud);
        f.addField("tipoDocumento", this.tipoDocumento);
        f.addField("documentoUsuario", documento);
        
        
        if(esEmpresa){
            f.addField("Tipo Documento Tecnico", tipoDocumentoTecnico);
            f.addField("Documento", documentoTecnico);
        }
        f.getFooter().addComponent(validate);
        f.setHeight("300px");
        panel.addComponent(f);       
        panel.setHeight("300px");
        formLayout.addComponent(panel);
    }

    @Override
    protected void onClickReset() {
        formLayout.removeAllComponents();
        init();
        reset.setCaption("Limpiar");
    }

    @Override
    protected void process() {
        try {
            addValidators();
            buttons.removeComponent(link);
            this.validate(); 
            Ciudad c = (Ciudad) ciudad.getValue();
            TipoSolicitud ts = (TipoSolicitud)tipoSolicitud.getValue();
            String dt = getValue(documentoTecnico);
            
            if(esEmpresa){
                
                tecnico = daoTecnico.findByDocumento(dt);
                if(tecnico == null){
                    tecnico = new Tecnico();
                    tecnico.setDocumento(getValue(documentoTecnico));
                    
                }
            }
            
            try{                                
                tecnico.setPrimerApellido(getValue(primerApellido).toUpperCase());
                tecnico.setSegundoApellido(getValue(segundoApellido).toUpperCase());                                
                tecnico.setNombres(getValue(nombres).toUpperCase());
                tecnico.setEmail(getValue(correo));
                tecnico.setCelular(getValue(celular).toUpperCase());
                tecnico.setDireccion(getValue(direccionResidencia));                
                tecnico.setTelefono(getValue(telefono));
                tecnico.setCiudad(c);                
                tecnico.setNotificacion(
                    (getValue(notificacion).equalsIgnoreCase("si") ? true : false));
                tecnico.setFechaNacimiento((Date) fechaNacimiento.getValue());            
            } catch (Exception e){
                System.out.print("ERROR EN CAMPOS");
                e.printStackTrace();
            }
            
            if(!esEmpresa){
                usuario.setNombres(getValue(nombres).toUpperCase());
                usuario.setPrimerApellido(getValue(primerApellido).toUpperCase());
                usuario.setSegundoApellido(getValue(segundoApellido).toUpperCase());
                usuario.setDireccion(getValue(direccionResidencia));
                usuario.setEmail(getValue(correo));
                usuario.setCelular(getValue(celular));
                usuario.setCiudad(c);                        
                usuario.setTelefono(getValue(telefono));
            }
            solicitud.setFechaCreacion(new Date());            
            solicitud.setTipoSolicitud(ts);
            solicitud.setEstado(new Estado(1));
            solicitud.setDireccionEmpresa(direccionEmpresa.getValue().toString());
            solicitud.setEmpresa(getValue(empresa));
            solicitud.setTelefonoEmpresa(telefonoEmpresa.getValue().toString());
            solicitud.setInspector((Inspector) inspector.getValue());            
            solicitud.setCiudad(c);
            solicitud.setEntero(getValue(entero));
            solicitud.setEnteroPor(enteroPor.getValue().toString());
            solicitud.setUsuario(usuario);
            solicitud.setUltimaModificacion(new Date());
            
            ReferenciaPersonal rf = new ReferenciaPersonal();
            
            rf.setDireccion(direccionFamiliar.getValue().toString());
            rf.setNombres(getValue(nombresFamiliar).toUpperCase());
            rf.setTelefono(getValue(telefonoFamiliar));
      
            usuario.setNotificacion(
                    (getValue(notificacion).equalsIgnoreCase("si") ? true : false));
            
            solicitud.setDependiente(
                    (getValue(dependiente).equalsIgnoreCase("si") ? true : false));
            if(getValue(entero).equalsIgnoreCase("Asociación")){
                solicitud.setAsociacion((Asociacion) asociacion.getValue());
                solicitud.setInspector((Inspector) inspector.getValue());  
                solicitud.setEnteroPor("");
            } else {
                solicitud.setAsociacion(null);
                solicitud.setInspector(null);    
                if(getValue(entero).equalsIgnoreCase("conte")){
                    solicitud.setEnteroPor("");
                } else {
                    solicitud.setEnteroPor(getValue(enteroPor));
                }                
                
            }
            
            try {
                if(esEmpresa){
                    //List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);
                    
                    if(daoTecnico.findByDocumento(dt)!=null){//tecnicos.size()>0){
                        //tecnico.setCodigo(tecnicos.get(0).getCodigo()) ;                    
                        tecnico = daoTecnico.update(tecnico);                        
                    } else {                        
                        tecnico = daoTecnico.insert(tecnico);
                    }
                } else {
                    usuario = daoUsuario.update(usuario);
                    tecnico = daoTecnico.update(tecnico);
                }
                solicitud.setTecnico(tecnico);
                solicitud = daoSolicitud.insert(solicitud);
                
                if(!getValue(nombresFamiliar2).isEmpty() && !getValue(telefonoFamiliar2).isEmpty()){
                        ReferenciaPersonal referenciaPersonal2 = new ReferenciaPersonal();
                        referenciaPersonal2.setNombres(getValue(nombresFamiliar2).toUpperCase());
                        referenciaPersonal2.setTelefono(getValue(telefonoFamiliar2));
                        referenciaPersonal2.setDireccion(getValue(direccionFamiliar2));
                        referenciaPersonal2.setSolicitud(solicitud);
                        daoReferenciaPersonal.insert(referenciaPersonal2);
                }
                
                buttons.removeComponent(link);
                rf.setSolicitud(solicitud);
                daoReferenciaPersonal.insert(rf);
                formLayout.removeAllComponents();
                
//                ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea enviar el formulario?, luego de enviar no podra volver a modificarlo",
//                        "Si", "No", new ConfirmDialog.Listener() {
//                            
//                            @Override
//                            public void onClose(ConfirmDialog dialog) {
//                                if (dialog.isConfirmed()) {
//                                    startWorkflow(solicitud.getRadicado());
//                                } 
//                            }
//                        });
                String r = startWorkflow(solicitud.getRadicado());
                if(r==null){
                    if(solicitud.getEstado().getCodigo() == 1){
                        launchSubwindow("Ha ocurrido un error al intentar enviar"
                                + " la solicitud");
                    }
                    
                    if(solicitud.getEstado().getCodigo() != 1){
                        launchSubwindow("La solicitud fue enviada con anterioridad");
                    }                    
                } else {
                    launchSubwindow("La solicitud se ha enviado correctamente");
                }
                addComponent(generateReport(tecnico, solicitud.getRadicado()));
                if(usuario.getTipoDocumento().equalsIgnoreCase("NIT")){
                    addComponent(reset);                    
                }
                
                
                //launchSubwindow("Se ha guardado correctamente");
                
                buttons.addComponent(link);
            } catch (DaoException ex) {
                super.message.setCaption("Ha ocurrido un error");
                Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
            }                       
                                   
        } catch (InvalidValueException ex) {
            launchSubwindow("Faltan campos por diligenciar");
        }
    }
    
    private void onClickValidate(){        
        if(getValue(this.documento)!=null && this.documento.isValid() 
                && tipoSolicitud.isValid()) {
            TipoSolicitud ts = (TipoSolicitud) tipoSolicitud.getValue();
            if(esEmpresa){
                if(documentoTecnico.isValid()){
                    
                    try {
                        tecnico = new Tecnico();
                        tecnico.setDocumento(getValue(documentoTecnico));
                        List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);                        
                        if(tecnicos.size()>0){
                            tecnico.setCodigo(tecnicos.get(0).getCodigo()) ;     
                            tecnico = daoTecnico.findById(tecnico.getCodigo());
                            switch(ts.getCodigo()){
                                case 2:
                                    String matricula = tecnico.getMatricula() == null ? "" : tecnico.getMatricula().trim();
                                    //En caso de no tener matricula no puede hacer una ampliacion
                                    if(matricula== null || matricula.isEmpty() ){
                                        message.setCaption("El usuario no puede solicitar"
                                                + " un Duplicado Matricula porque no tiene una matricula asociada");
                                        addComponent(message);
                                        return;
                                    }                                
                                    break;                                
                                case 3:
                                    //En caso de no tener matricula no puede hacer una ampliacion
                                    if(tecnico.getMatricula()== null || tecnico.getMatricula().isEmpty() ){
                                        message.setCaption("El usuario no puede solicitar"
                                                + " un Duplicado Matricula porque no tiene una matricula asociada");
                                        addComponent(message);
                                        return;
                                    }                                
                                    break;
                                case 5:
                                    if(tecnico.getMatricula()== null || tecnico.getMatricula().isEmpty() ){
                                        message.setCaption("El usuario no puede hacer "
                                                + "una ampliacion porque no tiene una matricula asociada");
                                        addComponent(message);
                                        return;
                                    }                                
                                    break;
                                case 6:
                                    if(tecnico.getMatricula()!=null ){
                                        if(!tecnico.getMatricula().isEmpty()){
                                            message.setCaption("El tecnico ya tiene una matricula asociada");
                                            addComponent(message);
                                            return;
                                        }
                                        
                                    }                                
                                    break;

                            }
                        } else {
                            if(ts.getCodigo()>1 && ts.getCodigo()<6 ){
                                message.setCaption("El tecnico no puede hacer "
                                        + "este tramite porque no tiene una matricula asociada");
                                addComponent(message);
                                return;
                            }                                
                            tecnico.setCodigo(new Long(0)) ;          
                        }
                        
                        switch(ts.getCodigo()){
                            case 6:
                                if(esEmpresa && !tipoDocumentoTecnico.getValue().toString().equalsIgnoreCase("PAP")){
                                    message.setCaption("Este tipo solicitud no es validad para este tipo de documento");
                                    addComponent(message);
                                    return;
                                }                               
                                break;
                            default:
                                if(esEmpresa && tipoDocumentoTecnico.getValue().toString().equalsIgnoreCase("PAP")){
                                    message.setCaption("Este tipo de documento no puede realizar este tipo solicitud ");
                                    addComponent(message);
                                    return;
                                }                               
                                break;
                                
                        }
                    } catch (DaoException ex) {
                        Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    message.setCaption("El valor de tipo documento tecnico no es valido");
                    return;
                }
            } else {
                if(usuario.getTipoDocumento().equalsIgnoreCase("CC") && ts.getCodigo() == 6 && (tecnico.getMatricula() == null && ts.getCodigo() == 2) ){
                    message.setCaption("Este tipo de documento no puede realizar este tipo solicitud ");
                    addComponent(message);
                    return;
                }
                    
            }
            
            solicitud.setTipoSolicitud(ts);
            solicitud.setTecnico(tecnico);
            solicitud.setUsuario(usuario);            
            if(daoSolicitud.findByTecnicoTipoSolicitud(tecnico.getCodigo().toString(), ""+ts.getCodigo()).intValue()==0){
                    this.tipoSolicitud.setReadOnly(true);
                    this.tipoDocumento.setReadOnly(true);
                    if(esEmpresa){
                        this.tipoDocumentoTecnico.setReadOnly(true);
                    }
                    this.documento.setReadOnly(true);
                    formLayout.addComponent(this.createInformationSection());
                    formLayout.addComponent(createFamilyInformationSection());
                    formLayout.addComponent(createAdditionalInformationSection());
                    formLayout.addComponent(createButtons());
                    this.validate.setVisible(false);
                    removeComponent(message);
            } else {
                message.setCaption("Esta solicitud ya existe");
                addComponent(message);
            }
        } else {
            message.setCaption("El campo Tipo Documento o Documento no es valido"); 
            addComponent(message);
        }
    }    
}