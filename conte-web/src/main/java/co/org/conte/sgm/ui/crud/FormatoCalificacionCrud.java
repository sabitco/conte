package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.FormatoEstudioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.ActividadGenerica;
import co.org.conte.sgm.entity.ClaseGenerica;
import co.org.conte.sgm.entity.FormatoActividad;
import co.org.conte.sgm.entity.FormatoActividadId;
import co.org.conte.sgm.entity.FormatoEstudio;
import co.org.conte.sgm.entity.InstitucionEducativa;
import co.org.conte.sgm.entity.Item;
import co.org.conte.sgm.entity.TituloAcademico;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.FormatoService;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author Andrés Mise Olivera
 */
public class FormatoCalificacionCrud  extends GenericCrud<FormatoEstudio> implements
        Property.ValueChangeListener {

    private String activo;
    private OptionGroup clases;
    private HorizontalLayout buttons;
    private NativeSelect estado;
    private FormatoService formatoService;
    private String inactivo;
    private ComboBox institucion;
    private List<InstitucionEducativa> instituciones;
    private ComboBox titulo;
    private HashMap twinColSelects = new HashMap();
    private HashMap optionGroups = new HashMap();
    
    private VerticalLayout vl = new VerticalLayout();
    
    private List<ClaseGenerica> clasesGenericas;

    public FormatoCalificacionCrud(Usuario usuario) {
        super(FormatoEstudio.class);
        super.dao = new FormatoEstudioDao();
        super.delete.setCaption("Inactivar");
        formatoService = new FormatoService();      

        clasesGenericas = formatoService.getAllClases();
        clases = new OptionGroup("Seleccione la clase a agregar", clasesGenericas);        
        clases.setMultiSelect(true);
        clases.setImmediate(true); // send the change to the server at once
        clases.addListener(this); // react when the user selects something
        init();
    }
    
    @Override
    public void valueChange(ValueChangeEvent event) {
        Set<ClaseGenerica> claseSeleccionada = (Set<ClaseGenerica>) clases.getValue();
        OptionGroup activities;
        OptionGroup items;
        Map<Integer, Set<Item>> activityItem = new HashMap<Integer, Set<Item>>(); 
        HorizontalLayout horizontalLayout;
        TwinColSelect tcs;
            for(ClaseGenerica cg : clasesGenericas){
                if(claseSeleccionada.contains(cg)){
                    Set<ActividadGenerica> actividadAgregada = new HashSet<ActividadGenerica>();
                    if(entity.getCodigo()!=null){
                        List<FormatoActividad> fas = formatoService.getFormatoActividadByFormatoEstudio(entity);                            
                        for(ActividadGenerica ag : cg.getActividadGenericas()){
                            Set<Item> itemSelected = new HashSet<Item>();
                            for(FormatoActividad fa : fas){                                
                                if(ag.getCodigo() == fa.getActividadGenerica().getCodigo()){
                                    if(actividadAgregada.contains(ag)){
                                        itemSelected.add(fa.getItem());
                                        if(activityItem.containsKey(ag.getCodigo())){
                                            activityItem.remove(ag.getCodigo());
                                            activityItem.put(ag.getCodigo(), itemSelected);
                                        } else {
                                            activityItem.put(ag.getCodigo(), itemSelected);
                                        }                                        
                                    } else {
                                        actividadAgregada.add(ag);
                                        itemSelected.add(fa.getItem());
                                        if(activityItem.containsKey(ag.getCodigo())){
                                            activityItem.remove(ag.getCodigo());
                                            activityItem.put(ag.getCodigo(), itemSelected);
                                        } else {
                                            activityItem.put(ag.getCodigo(), itemSelected);
                                        }   
                                    }
                                }                                
                            }
                        }
                    }
                    
                    if(!optionGroups.containsKey(cg.getCodigo())){
                        VerticalLayout vl = new VerticalLayout();
                        vl.setCaption(cg.getNombre());
                        for(ActividadGenerica ag : cg.getActividadGenericas()){
                            horizontalLayout = new HorizontalLayout();
                            activities = new OptionGroup();
                            activities.addItem(ag);
                            if(actividadAgregada.contains(ag)){
                                activities.setValue(ag);
                            }
                            activities.setItemCaption(ag, ag.getNombre());
                            activities.setMultiSelect(true);
                            
                            items = new OptionGroup();
                            items.setMultiSelect(true);
                            Set<Item> itemsValue = new HashSet<Item>();
                            for(Item i : ag.getItems() ){
                                items.addItem(i);
                                
                                if(activityItem.containsKey(ag.getCodigo())){
                                    
                                    for(Item j : activityItem.get(ag.getCodigo())) {
                                        System.out.println("EN EL FOR");
                                        if(i.getId()==j.getId()){
                                            itemsValue.add(i);
                                            break;
                                        }
                                        
                                    }
                                }                           
                            }
                            items.setValue(itemsValue);
                            
                            horizontalLayout.addComponent(activities);
                            horizontalLayout.addComponent(items);
                            vl.addComponent(horizontalLayout);
                        }                                            
                        optionGroups.put(cg.getCodigo(), vl);
                    }
                    
                    if(!twinColSelects.containsKey(cg.getCodigo())){
                        tcs = new TwinColSelect(cg.getNombre() , cg.getActividadGenericas());
                        tcs.setValue(actividadAgregada);
                        tcs.setLeftColumnCaption("Disponibles");
                        tcs.setRightColumnCaption("Agregadas");
                        tcs.setWidth("900px");
                        
                        twinColSelects.put(cg.getCodigo(), tcs);
                    }
                } else {
                    if(twinColSelects.containsKey(cg.getCodigo())){
                        twinColSelects.remove(cg.getCodigo());
                    }
                    if(optionGroups.containsKey(cg.getCodigo())){
                        optionGroups.remove(cg.getCodigo());
                    }
                }               
            }
            
            Iterator it = twinColSelects.entrySet().iterator();
            vl.removeAllComponents();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                vl.addComponent((TwinColSelect)e.getValue());
            }
            vl.addComponent(buttons);
            
            it = optionGroups.entrySet().iterator();
            vl.removeAllComponents();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry)it.next();
                vl.addComponent((VerticalLayout)e.getValue());
            }
            vl.addComponent(buttons);
        }

    @Override
    public void addValidators() {
    }

    @Override
    public void createForm() {
        
        form.addField("institucion", institucion);
        form.addField("titulo", titulo);
        form.addField("estado", estado);
        form.addField("clases", clases);
        
        buttons = new HorizontalLayout();
        buttons.addComponent(reset);
        buttons.addComponent(clear);
        buttons.addComponent(submit);        
        vl.addComponent(buttons); 
        
        form.getFooter().removeAllComponents();
        form.getFooter().addComponent(vl);
        
        
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Long.class,  null);        
        table.addContainerProperty("Institución Educativa",  String.class, null);
        table.addContainerProperty("Titulo", String.class,  null);        
        table.addContainerProperty("Estado", String.class,  null);      
        
        for(FormatoEstudio fe : getList()){
            table.addItem(new Object[] {
                fe.getCodigo(),
                fe.getTituloAcademico().getInstitucionEducativa().getNombre(),
                fe.getTituloAcademico().getNombre(),
                fe.getEstado() ? "Activo" : "Inactivo",
            } , fe);
        }
    }
    
    @Override
    public void onClickClear() {
        estado.setValue(null);
        clases.setValue(null);
        institucion.setValue(null);
        titulo.setValue(null);
        titulo.removeAllItems();
    }

    @Override
    public void onClickCreate() {
    }

    @Override
    public void onClickDelete() {
        if(entity.getEstado()){
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar al formato?",
            "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                @Override
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        try {
                            entity.setEstado(false);
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

    @Override
    public void onClickEdit() {
        estado.setValue(entity.getEstado() ? activo : inactivo);
//        nombre.setValue(entity.getNombre());
        twinColSelects = null;
        twinColSelects = new HashMap();
        institucion.removeAllItems();
        institucion.setValue(null);
        for(InstitucionEducativa ie : instituciones){
            institucion.addItem(ie);
            institucion.setItemCaption(ie, ie.getNombre());            
            if(entity.getTituloAcademico()!=null){
                if(entity.getTituloAcademico().getInstitucionEducativa().getCodigo().equals(ie.getCodigo())) {
                    institucion.setValue(ie);
                }
            }
        }
        
        titulo.removeAllItems();
        titulo.setValue(null);
        for(TituloAcademico ta : ((InstitucionEducativa)institucion.getValue()).getTituloAcademicos()){
            titulo.addItem(ta);
            titulo.setItemCaption(ta, ta.getNombre());
            if(entity.getTituloAcademico().getCodigo() == ta.getCodigo()){
                titulo.setValue(ta);
            }
        }
        
        clases.removeAllItems();
        clases.setValue(null);
        Set<ClaseGenerica> cgs = new HashSet<ClaseGenerica>();
        List<FormatoActividad> fas = formatoService.getFormatoActividadByFormatoEstudio(entity);
        for(ClaseGenerica cg : clasesGenericas ){
            clases.addItem(cg);
            for(FormatoActividad fa : fas){
                if(fa.getActividadGenerica().getClaseGenerica().getCodigo() == cg.getCodigo()){
                    cgs.add(cg);
                    break;
                }
            }
        }
        clases.setValue(cgs);
    }

    @Override
    public void process() {
        entity.setTituloAcademico((TituloAcademico) titulo.getValue());
        entity.setEstado(estado.getValue().toString().equals(activo));
        Set<FormatoActividad> fas = new HashSet<FormatoActividad>();
        List<FormatoActividad> faxx = new ArrayList<FormatoActividad>();
        Iterator it = optionGroups.entrySet().iterator();
        int activityId = 0;
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry)it.next(); 
            VerticalLayout vl = (VerticalLayout) e.getValue();
            Iterator it2 =  vl.getComponentIterator();
           
            for(int i = 0; i<vl.getComponentCount(); i++){                
                HorizontalLayout hl = (HorizontalLayout) vl.getComponent(i);
                for(int j=0; j<hl.getComponentCount(); j++){                    
                    Component c = hl.getComponent(j);                    
                    if(c instanceof OptionGroup){                        
                        
                        OptionGroup castComponent = (OptionGroup) c;
                        Set set = (Set) castComponent.getValue();
                        
                        for(Object o : set){
                            FormatoActividad fa = new FormatoActividad();
                            FormatoActividadId faid = new FormatoActividadId();
                            if(entity.getCodigo() != null){                                
                                faid.setFormatId(entity.getCodigo());
                            }
                            if(o instanceof ActividadGenerica){
                                ActividadGenerica ag = (ActividadGenerica) o;
                                if(activityId!=ag.getCodigo()){
                                    activityId = (ag.getCodigo());
                                }                                
                            } else {
                                Item castItem = (Item) o;
                                faid.setActivityId(activityId);
                                faid.setItemId(castItem.getId());
                                fa.setId(faid);
                                
                                if(!fas.contains(fa)){
                                    fas.add(fa);
                                }
                                if(activityId==0){
                                    launchSubwindow("HA OCURRIDO UN ERROR");
                                    break;
                                }
                            }
                        }                        
                    }                    
                }                
            }                
        }
        entity.setFormatoActividades(fas);
    }

    @Override
    public void setRequired(boolean required) {
        estado.setRequired(required);
        institucion.setRequired(required);
        titulo.setRequired(required);
        clases.setRequired(required);
    }

    @Override
    public boolean validate() {
        return (estado.isValid() && 
                institucion.isValid() && 
                titulo.isValid());
    }
    
    private void addListenersForm(){
        institucion.setImmediate(true);
        this.institucion.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                InstitucionEducativa selected = (InstitucionEducativa) institucion.getValue();
                titulo.removeAllItems();
                titulo.setValue(null);
                if(selected != null && !selected.getTituloAcademicos().isEmpty()){
                    for(TituloAcademico ta : selected.getTituloAcademicos()){
                        titulo.addItem(ta);
                        titulo.setItemCaption(ta, ta.getNombre());                        
                    }
                }
             }
        });
    }
    
    private void init(){
        activo = "Activo";    
        estado = new NativeSelect("Estado");
        instituciones = formatoService.getAllInstituciones();
        institucion = new ComboBox("Institucion Educativa", instituciones);
        inactivo = "Inactivo";
        titulo = new ComboBox("Titulo");
        estado.addItem(activo);
        estado.addItem(inactivo);
        createForm();
        addListenersForm();
        setRequired(true);
    }
}