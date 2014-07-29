package com.conte.common.ui.util;

import com.vaadin.Application;
import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

/**
 *
 * @author Andrés Mise Olivera
 */
public abstract class Form extends Application{
    
    protected com.vaadin.ui.Form form;
    protected final TextField ingresoCaptcha;
    protected final Label mensaje;
    protected final Label message;
    protected final Button reset;
    protected final Button submit = new Button("Enviar");
    protected Window mainWindow;
    
    public Form(){
        form = new com.vaadin.ui.Form();
        message = new Label("");
        mensaje = new Label("");
        reset = new Button("Limpiar");
        
        form.getFooter().addComponent(this.message);
        form.getFooter().addComponent(this.reset);
        form.getFooter().addComponent(this.submit);
        
        ingresoCaptcha = new TextField("Ingrese Captcha");
        ingresoCaptcha.setRequired(true);
        
        this.reset.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                onClickReset();              
            }
        });
        
        this.submit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                process();              
            }
        });
    }
    
    public String getValue(Field field) {
        if (field.getValue() == null || field.getValue().toString().trim().isEmpty()) {
                mainWindow.getWindow().showNotification(field.getCaption() + " es un campo requerido", Window.Notification.TYPE_ERROR_MESSAGE);
                return null;
        }
        return field.getValue().toString().trim();
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
    protected abstract void validate() throws Validator.InvalidValueException;   
    
    /**
     * Verifica que el valor ingresado en el campo Ingrese Captcha
     * sea igual al Captcha mostrado al usuario
     * @return <code>true</code> en caso de que el valor ingresado sea igual al
     * valor mostrado
     */
    /*protected boolean validateCaptcha(){
        return captcha.validateCaptcha((String)ingresoCaptcha.getValue());
    }*/
}