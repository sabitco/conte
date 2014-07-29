package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.ActividadGenerica;
import co.org.conte.sgm.entity.ClaseGenerica;
import co.org.conte.sgm.entity.Item;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.ItemService;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class ClaseActividadCrud extends GenericCrudMasterDetail<ClaseGenerica, ActividadGenerica> {


    private ItemService service;
        
    //Campos de la clase
    private TextField nombreClase;
    private String activo;
    private NativeSelect estado;
    private String inactivo;
    
    //Campos de la actividad
    private TextField nombreActividad;
    private TextField descripcionActividad;
    private TwinColSelect itemstsc;
    private List<Item> items;
    
    public ClaseActividadCrud(Usuario usuario) {
        super(ClaseGenerica.class, ActividadGenerica.class);
        service = new ItemService();
        init();
    }

    @Override
    public void addValidators() {
     
    }

    @Override
    public void onClickClearDetail() {
        descripcionActividad.setValue("");
        nombreActividad.setValue("");
    }

    @Override
    public void processDetail() {
        entityDetail.setClaseGenerica(entity);
        entityDetail.setDescripcion(getValue(descripcionActividad));
        entityDetail.setNombre(getValue(nombreActividad));
        entityDetail.setItems((Set<Item>)itemstsc.getValue());
    }

    @Override
    public void setDetailFields() {
        if(entityDetail!=null){
            descripcionActividad.setValue(entityDetail.getDescripcion());
            nombreActividad.setValue(entityDetail.getNombre());
            
            List<Item> select = new ArrayList<Item>();
            
            for(Item i : items){
                for(Item it : entityDetail.getItems()){
                    if(i.getId()==it.getId()){
                        select.add(i);
                    }                    
                }
                
            }            
            itemstsc.setValue(select);
        }
    }

    @Override
    public boolean validateDetail() {
        return (
                descripcionActividad.isValid() &&
                nombreActividad.isValid()
                );
    }

    

    @Override
    public void createForm() {
        form.addField("nombreClase", nombreClase);
        form.addField("estado", estado);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);  
        table.addContainerProperty("Nombre", String.class,  null);     
        List<ClaseGenerica> clases = getList();
        if(clases != null && !clases.isEmpty()){
            for(ClaseGenerica c : clases){
                table.addItem(new Object[]{                    
                    c.getCodigo(),                   
                    c.getNombre()
                }, c);
            }            
        }   
    }

    @Override
    public void onClickClear() {
        nombreClase.setValue("");
        estado.setValue(null);
    }

    @Override
    public void onClickCreate() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onClickDelete() {
        if(entity.getEstado()){
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar la"
                    + " Clase "+entity.getNombre()+ "?",
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
        estado.setValue(entity.getEstado() ? activo : inactivo);
        nombreClase.setValue(entity.getNombre());
    }

    @Override
    public void process() {
        entity.setNombre(getValue(nombreClase));
        entity.setEstado(getValue(estado).equals(activo));
    }

    @Override
    public void setRequired(boolean required) {
        descripcionActividad.setRequired(required);
        nombreActividad.setRequired(required);
        nombreClase.setRequired(required);
        estado.setRequired(required);
    }

    @Override
    public boolean validate() {
        return nombreClase.isValid();
    }
    
    private void init(){
        descripcionActividad = new TextField("Descripcion");
        nombreClase = new TextField("Nombre");
        nombreActividad = new TextField("Nombre");
        activo = "Activo";
        inactivo = "Inactivo";
        estado = new NativeSelect("Estado:");        
        estado.addItem(activo);
        estado.addItem(inactivo);
        items = service.findAll();
        itemstsc = new TwinColSelect("Items" , items);
        createForm();
        setRequired(true);
    }

    @Override
    public void createDetailForm() {
        detailForm.addField("nombreActividad", nombreActividad);
        detailForm.addField("descripcionActividad", descripcionActividad);
        detailForm.addField("items", itemstsc);
    }   
    
    @Override
    public void createTableDetail(){
        detailTable.addContainerProperty("Codigo", Integer.class,  null);     
        detailTable.addContainerProperty("Nombre", String.class,  null);     
        detailTable.addContainerProperty("Descripción", String.class,  null);        
        
        Set<ActividadGenerica> actividades = entity.getActividadGenericas();        
        if(actividades != null && !actividades.isEmpty()){
            for(ActividadGenerica ta : actividades){
                detailTable.addItem(new Object[]{
                    ta.getCodigo(),
                    ta.getNombre(),
                    ta.getDescripcion(),                    
                }, ta);
            }            
        }        
    }
}