package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.entity.Usuario;

/**
 *
 * @author jam
 */
public class ResponderPqrs extends GenericPqrs {
    
    public ResponderPqrs(Usuario usuario) {
        super(usuario);
        action.setCaption("Responder");
    }
}