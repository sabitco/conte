package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.entity.Usuario;

/**
 *
 * @author jam
 */
public class RevisarPqrs extends GenericPqrs {

    public RevisarPqrs(Usuario usuario) {
        super(usuario);
        action.setCaption("Revisar");
    }   
}