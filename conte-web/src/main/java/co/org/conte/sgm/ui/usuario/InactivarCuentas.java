package co.org.conte.sgm.ui.usuario;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Andrés Mise Olivera
 */
public class InactivarCuentas extends GenericForm {
    
    private TextField dias;
    private TwinColSelect usuarios;
    private Button inactivar;

    public InactivarCuentas(Usuario usuario) {
        super(usuario);
        dias = new TextField("Días de Inactividad");      
        usuarios = new TwinColSelect();
        inactivar = new Button("Inactivar");
        init();
    }
    
    private void init(){
         createForm();
         addValidators();
         addListeners();
    }
            
            
    
    @Override
    protected void addListeners() {
        this.inactivar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                onClickInactivar();              
            }
        });

    }

    @Override
    protected void addValidators() {
//        dias.addValidator(new NullValidator("Debe indicar el número de días"));
        dias.addValidator(new IntegerValidator("Solo puede ingresar números"));
    }

    @Override
    protected void createForm() {
        form.addField("date", dias);
        addListeners();
        addComponent(usuarios);
        addComponent(inactivar);
        inactivar.setVisible(false);
        usuarios.setVisible(false);
        setRequired(true);
    }

    @Override
    protected void onClickReset() {
        message.setCaption("");
        dias.setValue(0);
        usuarios.removeAllItems();
        usuarios.setVisible(false);
        usuarios.setValue(null);
        inactivar.setVisible(false);
    }

    @Override
    protected void process() {
        try{
            validate();
            usuarios.setVisible(true);
            inactivar.setVisible(true);
            
            int diferenciaEnDias = Integer.parseInt(getValue(dias));
            
            Calendar cal = new GregorianCalendar();
            cal.setTimeInMillis((new Date()).getTime());
            cal.add(Calendar.DATE, -diferenciaEnDias);
            Date fechaAnterior = new Date(cal.getTimeInMillis());
            
            System.out.print("LA FECHA ES =>" + fechaAnterior + "   Y los dias son " + diferenciaEnDias);
//            usuarios = new TwinColSelect("");
//            usuarios.setValue(actividadAgregada);
//            usuarios.setData(serviceUsuario.findByInactivity(fechaAnterior));
            usuarios.setValue(null);
            usuarios.removeAllItems();
            List<Usuario> us = serviceUsuario.findByInactivity(fechaAnterior);
            if(us!=null && !us.isEmpty()){
                for (Usuario u :  us) {
                    usuarios.addItem(u);
                    usuarios.setItemCaption(u, u.getDocumento());
                }  
                
                usuarios.setLeftColumnCaption("Activos");
                usuarios.setRightColumnCaption("Inactivos");
                usuarios.setWidth("900px");
            } else {
                launchSubwindow("No existen usuarios para inactivar");
            }
//            removeComponent(usuarios);
            

        } catch (InvalidValueException ex){
            message.setCaption("Existen campos invalidos");
        }
    }

    @Override
    protected void setRequired(boolean required) {
        dias.setRequired(required);
    }

    @Override
    protected void validate() throws InvalidValueException {
        dias.validate();
    }

    @Override
    protected void setMaxLength() {
    }
    
    private void onClickInactivar(){
        Set<Usuario> inactivos = (Set<Usuario>) usuarios.getValue();
        if(inactivos != null){
            int total = serviceUsuario.inactivarUsuarios(inactivos);            
            onClickReset();
            message.setCaption("Se han inactivado los usuarios seleccionados usuarios");
        }
    }
}
