package co.org.conte.sgm.ui;

import co.org.conte.sgm.dao.TecnicoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Sancion;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrés Mise Olivera
 */
public class ConsultarSanciones extends VerticalLayout{
    
    private TecnicoDao daoTecnico = new TecnicoDao();
    private Tecnico tecnico;
    private Table table;
    private Long codigo;
    private Label message;

    public ConsultarSanciones(Usuario usuario) { 
        message = new Label("");
        tecnico = new Tecnico();
        tecnico.setDocumento(usuario.getDocumento());
        init();
    }

    private void init() {
        removeAllComponents();
        this.createTable();
        super.addComponent(table);
    }
    
    private void createTable(){
        table = new Table("Sanciones");
    
        table.addContainerProperty("Fecha de Inicio", java.util.Date.class,  null);
        table.addContainerProperty("Fecha de Finalizacion", java.util.Date.class,  null);
        table.addContainerProperty("Estado",  String.class, "Activa");
        try {            
            List<Tecnico> tecnicos = daoTecnico.findBy(tecnico);
             if(!tecnicos.isEmpty()){
                tecnico = tecnicos.get(0);
                if(tecnico.getSanciones().size()>0){


                    int aux = 0;
                    for (Sancion sancion : tecnico.getSanciones()) {
                        aux++;
                         table.addItem(new Object[] {
                             sancion.getFechaInicio(),
                             sancion.getFechaFin(), 
                             (sancion.getFechaFin().getTime() > new Date().getTime()) 
                                 ? "Activo" : "Inactivo"
                         }, new Integer(aux));
                    }
                } else {
                    message.setCaption("NO tiene sanciones");
                    addComponent(message);
                }
             } 
        } catch (DaoException ex) {
            Logger.getLogger(ConsultarSanciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}