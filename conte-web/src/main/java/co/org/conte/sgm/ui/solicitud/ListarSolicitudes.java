package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrés Mise Olivera
 */
public class ListarSolicitudes extends VerticalLayout{
    
    private UsuarioDao dao;
    private Tecnico tecnico;
    private Table table;
    private Long codigo;
    private Label message;
    private Usuario usuario;
    
    public ListarSolicitudes(Usuario usuario) {
        try {
            dao = new UsuarioDao();
            this.usuario = dao.findById(usuario.getCodigo());
            tecnico = new Tecnico();
    //        tecnico.setDocumento(usuario.getDocumento());
            message = new Label("");
            createTable();
            super.addComponent(table);
        } catch (DaoException ex) {
            Logger.getLogger(ListarSolicitudes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createTable(){
        table = new Table("Solicitudes");

        table.addContainerProperty("Consecutivo", Integer.class,  null);
        table.addContainerProperty("Fecha",  java.util.Date.class, null);
//        table.addContainerProperty("Nombres",   String.class, null);
//        table.addContainerProperty("Apellidos",   String.class, null);
        table.addContainerProperty("Estado",   String.class, null);
//        try {
//            List<Tecnico> tecnicos = dao.findBy(tecnico);
//            if(!tecnicos.isEmpty()){
//                tecnico = tecnicos.get(0);
                if(usuario.getSolicituds().size()>0){
                    int aux = 0;
                    for(Solicitud solicitud : usuario.getSolicituds()){
                        if(solicitud.getEstado().getCodigo()==17 || solicitud.getEstado().getCodigo()==7){
                            aux++;
                            table.addItem(new Object[] {
                                solicitud.getRadicado(),
                                solicitud.getFechaCreacion(), 
//                                usuario.getNombres(),
//                                usuario.getPrimerApellido()+ " "+tecnico.getSegundoApellido(),
                                solicitud.getEstado().getNombre()                       
                            }, new Integer(aux));                            
                        }
                    }                   
                } else {
                    removeAllComponents();
                    message.setCaption("No tiene solicitudes");
                    addComponent(message);                    
                }
//            } else {
//                removeAllComponents();
//                message.setCaption("No tiene solicitudes");
//                addComponent(message);                                    
//            }
//        } catch (DaoException ex) {
//            Logger.getLogger(ListarSolicitudes.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}