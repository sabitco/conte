/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.Validator;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.*;
import javax.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author jam
 */
@Configurable(preConstruction = true)
public abstract class GenericForm extends GenericContent{
    
    protected Form form;
    protected Label message;
    protected Button reset;
    protected Button submit;
    protected Window subwindow;
    
    public GenericForm(Usuario usuario){
        super(usuario);
        this.init();
        super.addComponent(this.form);
    }
           
    
    private void init(){
        form = new com.vaadin.ui.Form();
        form.setImmediate(true);
        message = new Label("");
        reset = new Button("Limpiar");
        submit = new Button("Enviar");
        
        form.getFooter().addComponent(this.message);
        form.getFooter().addComponent(this.reset);
        form.getFooter().addComponent(this.submit); 
              
        this.reset.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                onClickReset();              
            }
        });
         
        submit.setImmediate(true);
        this.submit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                process();              
            }
        });
        
        
    }
     
    public String getValue(Field field) {
        String value;
        try{
            value = field.getValue().toString().trim();
        } catch (Exception e) {
            logger.error("Campo nulo " + e.getMessage());            
            value = "";
        }        
        return value;
    }
    
    
    protected abstract void addListeners();
    protected abstract void addValidators(); 
    protected abstract void createForm();
        
    /**
     * Limpia todos los campos de un formulario
     */
    protected abstract void onClickReset();
    /**
     * Método llamado al dar clic en el botón enviar del formulario
     */
    protected abstract void process();
    protected abstract void setRequired(boolean required);
    
    /**
     * Establece la longitud máxima de cada campo
     */
    protected abstract void setMaxLength();
    protected abstract void validate() throws Validator.InvalidValueException;   
}