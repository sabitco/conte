package co.org.conte.sgm.ui.pqrs;

import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;

/**
 *
 * @author jam
 */
public class AsignarPqrs extends GenericPqrs{

    
    private Button submit;
    
    public AsignarPqrs(Usuario usuario) {
        super(usuario);
        action.setCaption("Asignar Responsable");
        
        init();
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        submit = new Button("Guardar");
        submit.setImmediate(true);
        this.submit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                onClickSubmit();
                
            }
        });
    }
        
    private void init(){        
        form.getFooter().addComponent(clear);
        form.getFooter().addComponent(submit);        
    }
    
    private void onClickSubmit(){
        if(responsable.isValid()){
            pqrs.setUsuario((Usuario) responsable.getValue());
        } else {
            launchSubwindow(getMessage("error.form"));
        }
    }
    
    
    
}