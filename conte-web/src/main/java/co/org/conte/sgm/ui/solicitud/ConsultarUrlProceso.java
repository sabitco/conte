package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.container.ProcesoContainer;
import co.org.conte.sgm.dao.ProcesoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.entity.activiti.Proceso;
import co.org.conte.sgm.ui.App;
import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

/**
 *
 * @author J4M0
 */
public class ConsultarUrlProceso extends GenericContent {

    private Table table;
    private ProcesoDao procesoDao;
    private TextField filter;
    ProcesoContainer pc = new ProcesoContainer();
    
    
    public ConsultarUrlProceso(Usuario usuario) {        
        super(usuario);
        procesoDao = new ProcesoDao();
        filter = new TextField();
        filter.setWidth("400px");
        filter.setInputPrompt("Ingrese radicado del usuario a buscar");
        addComponent(filter);
        
        addListeners();
        
    }   
    
    private void createTable(String u){
        try {
            
            List<Proceso> procesos = procesoDao.findAll();                        
            for(Proceso proceso : procesos){
                Label url = new Label("", Label.CONTENT_XHTML);
                Label urlRepo = new Label("", Label.CONTENT_XHTML);
                String[] strings = proceso.getTitulo().startsWith(" ") ?  
                        proceso.getTitulo().replaceFirst(" ", "").split(" ") : 
                        proceso.getTitulo().split(" ");
                Integer radicado = 0;
                try{
                    if(proceso.getDocumento().equals("73143579")){
                        System.out.print("ESTE ES" + strings[0] 
                        + "el arreglo es " + strings + "y el valor de "
                                + " titulo es " + proceso.getTitulo());
                    }                    
                    radicado = Integer.parseInt(strings[0].trim()); 
                    strings[0] = "";
                } catch(NumberFormatException nfe) {}          
                String repo = proceso.getTitulo().replace("Matricula Profesional", "");
                repo = repo.replace("Solicitud Tarjeta", "");
                repo = repo.replace("Duplicado Matricula", "");
                repo = repo.replace("Duplicado Tarjeta", "");
                repo = repo.replace("Ampliacion", "");
                repo = repo.replace("Licencia Especial", "");                
                repo = repo.replace("  ", " ");                
                urlRepo.setValue("<a href='"+u+"share/page/repository#filter=path|%2FCONTE%2F"+proceso.getCarpeta()+"' target='_blank' >"+proceso.getTitulo()+"</a>");
                url.setValue("<a href='"+u+"share/page/workflow-details?workflowId=activiti$"+proceso.getCodigo()+"' target='_blank' >"+radicado+"</a>");
                pc.addProceso(radicado, proceso.getDocumento(),urlRepo,proceso.getTarea(),url);
            }
            table = new Table("", pc);
        } catch (DaoException ex) {
            table = new Table();
            Logger.getLogger(ConsultarUrlProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void addListeners() {
    filter.addListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                pc.removeAllContainerFilters();
                pc.addContainerFilter(new Container.Filter() {

                    @Override
                    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }
                        
                        if(onlyNumbers()){
                            filter.setValue(((String)filter.getValue()).replace(event.getText(),"" ));
                            return true;
                        }
                        
                         return filterByProperty("Radicado", item,
                                event.getText())
                                || filterByProperty("Documento", item,
                                        event.getText()
                                 
                                 )|| filterByProperty("Tarea Actual", item,
                                        event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(Object propertyId) {
                         if (propertyId.equals("Radicado")
                                || propertyId.equals("Documento")
                                 || propertyId.equals("Tarea Actual")){
                             return true;
                         }                           
                        return false;
                    }
                });
            }
        });
    }
    
    private boolean onlyNumbers(){
        return false;
        
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

    private Integer getDocumento(String s) {
        try{
            return Integer.parseInt(s);            
        } catch(NumberFormatException nfe) {
            return null;
        }
    }

    public ConsultarUrlProceso(Table table, ProcesoDao procesoDao, TextField filter, Usuario usuario) {
        super(usuario);
        this.table = table;
        this.procesoDao = procesoDao;
        this.filter = filter;
    }

    @Override
    public void attach() {
        String url = getURL();
        createTable(url);        
        addComponent(table);
    }    
}