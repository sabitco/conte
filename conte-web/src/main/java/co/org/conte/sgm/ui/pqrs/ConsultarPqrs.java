package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.entity.Usuario;

/**
 *
 * @author jam
 */
public class ConsultarPqrs extends GenericPqrs{

    
    
    public ConsultarPqrs(Usuario usuario) {
        super(usuario);
        action.setCaption("Consultar");       
    }
    
    
}
    