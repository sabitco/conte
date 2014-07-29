package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.dao.AsociacionDao;
import co.org.conte.sgm.dao.DepartamentoDao;
import co.org.conte.sgm.dao.InspectorDao;
import co.org.conte.sgm.dao.ReferenciaPersonalDao;
import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.TipoSolicitudDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Asociacion;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Inspector;
import co.org.conte.sgm.entity.ReferenciaPersonal;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author J4M0
 */
public class EditarSolicitud extends SolicitudForm{
   
    private ReferenciaPersonal referenciaPersonal;    
    private ReferenciaPersonal referenciaPersonal2;   
    private Table table; 
    private Button send;
    
    public EditarSolicitud(Usuario usuario) {
        super(usuario);        
        daoAsociacion = new AsociacionDao();
        daoInspector = new InspectorDao();
        daoDepartamento = new DepartamentoDao();
        daoReferenciaPersonal = new ReferenciaPersonalDao();
        daoSolicitud = new SolicitudDao();
        daoTecnico = new TecnicoDao();
        daoTipoSolicitud = new TipoSolicitudDao();
        daoUsuario = new UsuarioDao();
        solicitud = new Solicitud();        
        
        try {
            if(usuario!=null){
                tecnico = new Tecnico();
                tecnico.setDocumento(usuario.getDocumento());
                List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);
                if(tecnicos.size()>0){
                    tecnico = daoTecnico.findById(tecnicos.get(0).getCodigo()) ;                    
                } 
            }
        } catch (DaoException ex) {
            Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        init();
      
    }
       
    private void init() {
        reset.setCaption("Cancelar");
        removeAllComponents();
        formLayout = new FormLayout();
        if(usuario.getSolicituds().isEmpty()){
            message.setCaption("No tiene solicitudes en tramite");            
            removeAllComponents();
            super.addComponent(super.message);
        } else {
            table = new Table();
            table.setSelectable(true);
            table.setImmediate(true);
            
            table.addListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(ValueChangeEvent event) {
                    setSolicitud((Solicitud)table.getValue());
                }
            });
                
                table.addContainerProperty("Consecutivo", Long.class,  null);
                table.addContainerProperty("Tipo", String.class, null);

                for(Solicitud s : usuario.getSolicituds()){      
                    if(s.getEstado().getCodigo()==1){
                        table.addItem(new Object[] {
                            s.getRadicado(),s.getTipoSolicitud().getNombre()} , s);                        
                    }
                }
                addComponent(message);
                addComponent(table);
                
        }
    }    
    
    private void setSolicitud(Solicitud solicitud){
        this.solicitud = solicitud;
        
        
        try {
            this.tecnico = daoTecnico.findByIdSolicitud(solicitud.getRadicado());
            fechaNacimiento.setValue(tecnico.getFechaNacimiento());
                send = new Button("Enviar");
                
                tipoSolicitud = new NativeSelect("Tipo Solicitud");

                celular.setValue( tecnico.getCelular());
                
                    correo = new TextField("Correo", tecnico.getEmail());
                    ciudadEmpresa = new ComboBox("Ciudad Empresa");
                    departamento = new ComboBox("Departamento");
                    departamentoEmpresa = new ComboBox("Departamento Empresa");
                    direccionResidencia.setValue(tecnico.getDireccion());
                    documento = new TextField("Documento", tecnico.getDocumento());
                    documento.setReadOnly(true);
                    empresa = new TextField("Empresa");
                    nombres.setValue(tecnico.getNombres());
                    notificacion = new NativeSelect("Desea Notificaciones por correo");
                    notificacion.addItem(si);
                    notificacion.addItem(no);
                    notificacion.setValue((tecnico.isNotificacion() != null && 
                            tecnico.isNotificacion()) ? si : no);
                    primerApellido = new TextField("Primer Apellido", tecnico.getPrimerApellido());
                    segundoApellido = new TextField("Segundo Apellido", tecnico.getSegundoApellido());
                                                            
                    entero = new NativeSelect("Tramite realizado a través de:");        
                    
                                        
                    for(Departamento dep : daoDepartamento.findAll()){
                        departamento.addItem(dep);
                        departamento.setItemCaption(dep, dep.getNombre()); 
                        if(tecnico.getCiudad() != null){
                            if(dep.getNombre().equals(tecnico.getCiudad().getDepartamento().getNombre())){
                                departamento.setValue(dep);
                            }
                        }
                        departamentoEmpresa.addItem(dep);
                        departamentoEmpresa.setItemCaption(dep, dep.getNombre());            
                    }
                    Departamento selected = serviceUsuario.findDepartamentoById(tecnico.getCiudad().getDepartamento().getCodigo());
                    for(Ciudad c : selected.getCiudads()){
                        ciudad.addItem(c);
                        ciudad.setItemCaption(c, c.getNombre());   
                        if(c.getNombre().equals(tecnico.getCiudad().getNombre())){
                            ciudad.setValue(c);
                        }
                    }
                    telefono = new TextField("Telefono", tecnico.getTelefono());
                    telefonoEmpresa = new TextField("Telefono Empresa");
                    tipoDocumento = new NativeSelect("Tipo Documento");
                    tipoDocumento.addItem(usuario.getTipoDocumento());
                    tipoDocumento.setValue(usuario.getTipoDocumento());
                    tipoDocumento.setReadOnly(true);
            
                        
            for(Departamento dep : daoDepartamento.findAll()){
                departamentoEmpresa.addItem(dep);
                departamentoEmpresa.setItemCaption(dep, dep.getNombre());   
                if(this.solicitud.getCiudad()!=null){
                    if(dep.getNombre().equals(this.solicitud.getCiudad().getDepartamento().getNombre())){
                        departamentoEmpresa.setValue(dep);
                    }
                }
            }

            if(this.solicitud.getCiudad()!=null){
                Departamento departamentoSelected = serviceUsuario.findDepartamentoById(this.solicitud.getCiudad().getDepartamento().getCodigo());
                for(Ciudad c : departamentoSelected.getCiudads()){
                    ciudadEmpresa.addItem(c);
                    ciudadEmpresa.setItemCaption(c, c.getNombre());   
                    if(c.getNombre().equals(solicitud.getCiudad().getNombre())){
                        ciudadEmpresa.setValue(c);
                    }
                }
                
            }
            
            
            
            if(!this.solicitud.getReferenciaPersonals().isEmpty()){
                for(ReferenciaPersonal rf : this.solicitud.getReferenciaPersonals()){                    
                    if(rf!=null){
                        if(referenciaPersonal == null){
                            referenciaPersonal = rf;
                            nombresFamiliar.setValue(referenciaPersonal.getNombres());
                            direccionFamiliar.setValue(referenciaPersonal.getDireccion());
                            telefonoFamiliar.setValue(referenciaPersonal.getTelefono());
                            continue;                            
                        } else if (referenciaPersonal2 == null){
                            referenciaPersonal2 = rf;
                            nombresFamiliar2.setValue(referenciaPersonal2.getNombres());
                            direccionFamiliar2.setValue(referenciaPersonal2.getDireccion());
                            telefonoFamiliar2.setValue(referenciaPersonal2.getTelefono());
                            continue;                            
                        }
                        
                    }                    
                }                
            }
            entero.addItem("Empresa");
            entero.addItem("Asociación"); 
            entero.addItem("Instituto de Formación Técnica");
            entero.addItem("CONTE");
            addListeners();
            entero.setValue(this.solicitud.getEntero());        
            if(this.solicitud.getEntero().equals("Asociación")){
                asociacion.setVisible(true);
                asociacion.removeAllItems();
                List<Asociacion> asociaciones = serviceSolicitud.getAsocioaciones();
                if(!asociaciones.isEmpty()){
                    for(Asociacion a : asociaciones){
                        asociacion.addItem(a);
                        asociacion.setItemCaption(a, a.getSigla());
                        if(solicitud.getAsociacion()!=null){
                            if(solicitud.getAsociacion().getCodigo()==a.getCodigo()){
                                asociacion.setValue(a);
                            }
                        }
                    }
                }
                inspector.removeAllItems();
                for(Inspector i : daoInspector.findAll()){
                    inspector.addItem(i);
                    inspector.setItemCaption(i, i.getNombres()); 
                    if(solicitud.getInspector()!= null){
                        if(solicitud.getInspector().getCodigo() == i.getCodigo()){
                            inspector.setValue(i);                    
                        }
                    }
                }
            } else {
                if(!this.solicitud.getEntero().equalsIgnoreCase("Conte")){
                    enteroPor.setVisible(true);
                    enteroPor.setValue(this.solicitud.getEnteroPor());
                }
            }
            
            dependiente.setValue((solicitud.getDependiente()) ? si : no);
            
            if(solicitud.getDependiente()){
                direccionEmpresa.setValue(this.solicitud.getDireccionEmpresa());
                empresa.setValue(this.solicitud.getEmpresa());
                telefonoEmpresa.setValue(this.solicitud.getTelefonoEmpresa());
            } else {
                direccionEmpresa.setVisible(false);
                empresa.setVisible(false);
                telefonoEmpresa.setVisible(false);
                departamentoEmpresa.setVisible(false);
                ciudadEmpresa.setVisible(false);
            }
                    
            
        } catch (DaoException ex) {
            Logger.getLogger(EditarSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        createForm();
    }
    
    @Override
    protected void createForm() {
        
        empresa.setMaxLength(80);
        enteroPor.setMaxLength(60);
        nombres.setMaxLength(40);
        nombresFamiliar.setMaxLength(60);        
        primerApellido.setMaxLength(20);
        segundoApellido.setMaxLength(20);
        telefono.setMaxLength(20);
        telefonoEmpresa.setMaxLength(20);
        telefonoFamiliar.setMaxLength(20);
        try {
            
            addValidators();
            setRequired(true);
            if(solicitud.getEstado().getCodigo()!=1){
                setReadOnlyFields(true);
            }
            
            Panel panel = new Panel("Tipo Solicitud");
            
            Form f = new Form();
            
            for (TipoSolicitud d:  daoTipoSolicitud.findAll() ) {
                tipoSolicitud.addItem(d);
                tipoSolicitud.setItemCaption(d, d.getNombre());
                tipoSolicitud.setReadOnly(false);
                if(d.getNombre().equals(solicitud.getTipoSolicitud().getNombre())){
                    tipoSolicitud.setValue(d);                    
                }                    
            }
            tipoSolicitud.setReadOnly(true);
            f.addField("tipoSolicitud", tipoSolicitud);
            f.addField("tipoDocumento", tipoDocumento);
            f.addField("documento", documento);
            
            panel.addComponent(f);
            formLayout.addComponent(panel);
            formLayout.addComponent(createInformationSection());
            formLayout.addComponent(createFamilyInformationSection());
            formLayout.addComponent(createAdditionalInformationSection());
            formLayout.addComponent(createButtons());
            
            removeAllComponents();
            addComponent(formLayout);
        } catch (DaoException ex) {
            Logger.getLogger(EditarSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void onClickReset() {
        formLayout.removeAllComponents();        
        init();
        
    }

    @Override
    protected void process() {
        try {
            if(link != null){
                buttons.removeComponent(link);
            }    
            link = null;
            this.validate(); 
            Ciudad c = (Ciudad) ciudad.getValue();
            TipoSolicitud ts = (TipoSolicitud)tipoSolicitud.getValue();
            tecnico.setPrimerApellido(getValue(primerApellido).toUpperCase());
            tecnico.setSegundoApellido(getValue(segundoApellido).toUpperCase());            
            tecnico.setCelular(getValue(celular));
            tecnico.setDireccion(getValue(direccionResidencia));
            tecnico.setEmail(getValue(correo));
            tecnico.setNombres(getValue(nombres).toUpperCase());
            tecnico.setTelefono(getValue(telefono));
            tecnico.setCiudad(c);
            tecnico.setNotificacion(notificacion.getValue().toString().equalsIgnoreCase(si));
            tecnico.setFechaNacimiento((Date) fechaNacimiento.getValue() );          
            usuario.setNombres(getValue(nombres).toUpperCase().toUpperCase());
            usuario.setPrimerApellido(getValue(primerApellido).toUpperCase());
            usuario.setSegundoApellido(getValue(segundoApellido).toUpperCase());
            usuario.setDireccion(getValue(direccionResidencia));
            usuario.setEmail(getValue(correo));
            usuario.setCelular(getValue(celular));
            usuario.setCiudad(c);                        
            usuario.setTelefono(getValue(telefono));
            
            c = (Ciudad) ciudadEmpresa.getValue();
            
            solicitud.setFechaCreacion(new Date());
            solicitud.setTecnico(tecnico);
            solicitud.setTipoSolicitud(ts);
           
            solicitud.setDireccionEmpresa(getValue(direccionEmpresa));
            solicitud.setEmpresa(getValue(empresa));
            solicitud.setTelefonoEmpresa(getValue(telefonoEmpresa));
                    
            solicitud.setCiudad(c);
            solicitud.setEntero(getValue(entero));
            
            if(getValue(entero).equalsIgnoreCase("Asociación")){
                solicitud.setAsociacion((Asociacion) asociacion.getValue());
                solicitud.setInspector((Inspector) inspector.getValue());  
                solicitud.setEnteroPor("");
            } else {
                solicitud.setAsociacion(null);
                solicitud.setInspector(null);    
                solicitud.setEnteroPor(getValue(enteroPor));
            }
            solicitud.setUsuario(usuario);
            solicitud.setUltimaModificacion(new Date());
            referenciaPersonal = (referenciaPersonal == null) ? new ReferenciaPersonal() : referenciaPersonal;
            referenciaPersonal.setDireccion(getValue(direccionFamiliar));
            referenciaPersonal.setNombres(getValue(nombresFamiliar).toUpperCase());
            referenciaPersonal.setTelefono(getValue(telefonoFamiliar));
            referenciaPersonal.setSolicitud(solicitud);
      
            usuario.setNotificacion(
                    (getValue(notificacion).equalsIgnoreCase("si") ? true : false));
                        
            if(getValue(dependiente).equalsIgnoreCase("si")){
                solicitud.setDependiente(true);   
                solicitud.setDireccionEmpresa(getValue(direccionEmpresa));
                solicitud.setEmpresa(getValue(empresa));
                solicitud.setTelefonoEmpresa(getValue(telefonoEmpresa));
            } else {
                solicitud.setDireccionEmpresa("");
                solicitud.setEmpresa("");
                solicitud.setTelefonoEmpresa("");                
            }
            
            
            try {           
                
                if(referenciaPersonal2 == null){
                    if(!getValue(nombresFamiliar2).isEmpty() && !getValue(telefonoFamiliar2).isEmpty()){
                        referenciaPersonal2 = new ReferenciaPersonal();
                        referenciaPersonal2.setNombres(getValue(nombresFamiliar2).toUpperCase());
                        referenciaPersonal2.setTelefono(getValue(telefonoFamiliar2));
                        referenciaPersonal2.setDireccion(getValue(direccionFamiliar2));
                        referenciaPersonal2.setSolicitud(solicitud);
                        daoReferenciaPersonal.insert(referenciaPersonal2);
                    }
                } else {
                    if(!getValue(nombresFamiliar2).isEmpty() && !getValue(telefonoFamiliar2).isEmpty()){
                        referenciaPersonal2.setNombres(getValue(nombresFamiliar2).toUpperCase());
                        referenciaPersonal2.setTelefono(getValue(telefonoFamiliar2));
                        referenciaPersonal2.setDireccion(getValue(direccionFamiliar2));
                        referenciaPersonal2.setSolicitud(solicitud);                    
                        daoReferenciaPersonal.update(referenciaPersonal2);
                    }                
                }

                if(!usuario.getTipoDocumento().equalsIgnoreCase("nit")){
                    daoUsuario.update(usuario);
                }
                daoReferenciaPersonal.update(referenciaPersonal);
                
                daoTecnico.update(tecnico);
                daoSolicitud.update(solicitud);
                //launchSubwindow("Se ha guardado correctamente");
                
//                ConfirmDialog.show(getWindow(), "Confirmar", "Se ha guardado correctamente \n "
//                        + "¿Desea enviar el formulario?, luego de enviar no podra volver a modificarlo",
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
                link = ( generateReport(tecnico, solicitud.getRadicado()));
                buttons.addComponent(link);
                

            } catch (DaoException ex) {
                super.message.setCaption("Ha ocurrido un error");
                Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
            }                       
                                   
        } catch (InvalidValueException ex) {
            addComponent(message);
            launchSubwindow("Existen valores incorrectos o con campos sin diligenciar");
        }
    }

     
    private void setReadOnlyFields(boolean readOnly){ 
        tipoSolicitud.setReadOnly(readOnly);
        tipoDocumento.setReadOnly(readOnly);
        direccionEmpresa.setReadOnly(readOnly);
        departamentoEmpresa.setReadOnly(readOnly);
        ciudadEmpresa.setReadOnly(readOnly);
        telefonoEmpresa.setReadOnly(readOnly);
        nombresFamiliar.setReadOnly(readOnly);
        direccionFamiliar.setReadOnly(readOnly);
        telefonoFamiliar.setReadOnly(readOnly);
        entero.setReadOnly(readOnly);
        enteroPor.setReadOnly(readOnly);
        inspector.setReadOnly(readOnly);
    }
}