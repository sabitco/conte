package co.org.conte.sgm.container;

import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author J4M0
 */
public class UsuarioContainer extends IndexedContainer {

    public UsuarioContainer() {
        addContainerProperty("Codigo", Long.class, 0);
        addContainerProperty("Tipo Documento",  String.class, null);
        addContainerProperty("Documento",  String.class, null);
        addContainerProperty("Perfil", Perfil.class,  null);        
        addContainerProperty("Estado", String.class,  null);                
        addContainerProperty("Nombres",   String.class, null);
        addContainerProperty("Primer Apellido",   String.class, null);
        addContainerProperty("Segundo Apellido",   String.class, null);
        addContainerProperty("Email",   String.class, null);
        addContainerProperty("Celular",   String.class, null);  
      
    }
    
    /**
     * Agrega los datos del usuario al contenedor
     * @param usuario Usuario que se va agregar
     */
    public void addUsuario(Usuario usuario){

        Item item = addItem(usuario);
        
        if (item != null) {
            item.getItemProperty("Codigo").setValue(usuario.getCodigo());
            item.getItemProperty("Tipo Documento").setValue(usuario.getTipoDocumento());
            item.getItemProperty("Documento").setValue(usuario.getDocumento());
            item.getItemProperty("Perfil").setValue(usuario.getPerfil());
            item.getItemProperty("Estado").setValue(usuario.getEstado());
            item.getItemProperty("Nombres").setValue(usuario.getNombres());
            item.getItemProperty("Primer Apellido").setValue(usuario.getPrimerApellido());
            item.getItemProperty("Segundo Apellido").setValue(usuario.getSegundoApellido());
            item.getItemProperty("Email").setValue(usuario.getEmail());
            item.getItemProperty("Celular").setValue(usuario.getCelular());            
        }              
    }
}
