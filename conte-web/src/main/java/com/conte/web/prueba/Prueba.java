/*
 * Prueba.java
 *
 * Created on 2 de octubre de 2012, 11:46
 */
package com.conte.web.prueba;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class Prueba extends Application {
       @Override
       public void init() {
             Window mainWindow = new Window("Aplicacion Miproyecto ");
             Label label = new Label("Hola usuario Vaadin");
             mainWindow.addComponent(label);
             setMainWindow(mainWindow);
       }
}