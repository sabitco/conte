/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class Logout extends VerticalLayout{

    Button cerrar = new Button("Cerrar Sesión");
    
    public Logout(Usuario usuario) {
        addComponent(cerrar);
        this.cerrar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                cerrarSesion();
                          
            }
        });
    }    
    
    private void cerrarSesion(){
        ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea cerrar la sesión?",
        "Cerrar", "Cancelar", new ConfirmDialog.Listener() {

            @Override
            public void onClose(ConfirmDialog dialog) {
                if (dialog.isConfirmed()) {
                    getApplication().setUser(null);
                } 
            }
        });
    }
}