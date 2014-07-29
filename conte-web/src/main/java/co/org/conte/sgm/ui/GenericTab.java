/*
 * Copyright (C) 2012 Quasar Software Ltda.
 */
package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Pagina;
import co.org.conte.sgm.entity.Rol;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tab generico contenedor de elementos de UI y controladores de eventos.
 *
 * @author Andrés Mise Olivera [amise@quasarbi.com]
 */
public class GenericTab extends VerticalLayout {
    private static final long serialVersionUID = -593479948706915L;
    
    private Label breadcrumb;
    private Rol rol;
    private Usuario usuario;
    
    private CustomLayout customLayout;
    private VerticalLayout bodyContent;
    private MenuSection menuSection;
    
    public GenericTab(Rol rol, Usuario usuario) {
        this.rol = rol;
        this.usuario = usuario;
        breadcrumb = new Label("");
        setCaption(rol.getNombre());
        addComponents();
        addListeners();
    }
    
    private void addComponents() {
        customLayout = new CustomLayout("tabsheet");
        menuSection = new MenuSection(rol.getRolPaginas());
        bodyContent = new VerticalLayout();
        customLayout.addComponent(breadcrumb, "breadcrumb");
        customLayout.addComponent(menuSection, "menusection");
        customLayout.addComponent(bodyContent, "contentpage");
        addComponent(customLayout);
    }
    
    private void addListeners() {
        menuSection.setImmediate(true);
        menuSection.addListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                clickMenuSection(event);
            }
        });
    }
    private void clickMenuSection(ItemClickEvent event) {
        Pagina pagina = (Pagina) event.getItemId();
        try {
            Class clase = Class.forName(pagina.getEnlace());            
            Constructor constructor = clase.getConstructor(new Class[] {Usuario.class});            
            Component object = (Component) constructor.newInstance(usuario);
            breadcrumb.setCaption(pagina.getNombre());
            setBodyContent(object);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GenericTab.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void setBodyContent(Component componentContainer) {
        bodyContent.removeAllComponents();
        bodyContent.addComponent(componentContainer);
    }
}