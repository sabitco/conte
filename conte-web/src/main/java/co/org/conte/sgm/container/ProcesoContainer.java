package co.org.conte.sgm.container;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Label;

/**
 *
 * @author J4M0
 */
public class ProcesoContainer extends IndexedContainer{

    public ProcesoContainer() {
        addContainerProperty("Radicado",  Integer.class, null);
        addContainerProperty("Documento",  String.class, null);        
        addContainerProperty("Tarea Actual",  String.class, null);               
        addContainerProperty("URL", Label.class, null);
        addContainerProperty("URL Repositorio",  Label.class, null);       
    }   
    
    public void addProceso(Integer radicado, String documento,
            Label solicitudNombres, String tareaActual, Label url){
      
        Object itemId = addItem();
        
        Item item = getItem(itemId);
        
        if (item != null) {
            item.getItemProperty("Radicado").setValue(radicado);
            item.getItemProperty("Documento").setValue(documento);
            item.getItemProperty("URL Repositorio").setValue(solicitudNombres);            
            item.getItemProperty("Tarea Actual").setValue(tareaActual);            
            item.getItemProperty("URL").setValue(url);            
        }              
    
    }
    
    public void addProceso(String codigo, Integer radicado, String documento,
            Label solicitudNombres, String tareaActual, Label url){
      
        addContainerProperty("Process Id",  String.class, null); 
        
        Object itemId = addItem();
        Item item = getItem(itemId);
        
        if (item != null) {
            item.getItemProperty("Radicado").setValue(radicado);
            item.getItemProperty("Documento").setValue(documento);
            item.getItemProperty("URL Repositorio").setValue(solicitudNombres);            
            item.getItemProperty("Tarea Actual").setValue(tareaActual);            
            item.getItemProperty("URL").setValue(url); 
            item.getItemProperty("Process Id").setValue(codigo);
        }              
    
    }
    
}
