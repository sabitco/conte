///*
// * Informaci�n de Valoraci�n INFOVALMER, Bolsa de Valores de Colombia BVC
// * Copyright (C) 2012 Quasar Software Ltda.
// */
//package com.conte.common.ui;
//
//import com.vaadin.event.ItemClickEvent;
//import com.vaadin.ui.*;
//import com.vaadin.ui.Button.ClickEvent;
//import java.util.LinkedList;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//
///**
// * Tab generico contenedor de elementos de UI y controladores de eventos.
// *
// * @author Carlos Uribe [curibe@quasarbi.com]
// * @author Robert Mart�nez[rmartinez@quasarbi.com]
// * @author Alejandro Riveros [lriveros@quasarbi.com]
// */
//@Configurable
//public class GenericTab extends VerticalLayout {
//
//  private static final long serialVersionUID = -593479948706915L;
//
//  private final Logger logger = LoggerFactory.getLogger(getClass());
///*
//  private Perfil perfil;
//
//  private CustomLayout customLayout;
//  private VerticalLayout bodyContent;
//
//  private BreadCrumb breadCrumb;
//  private MenuSection menuSection;
//  private ValidacionParadas validacionParadas;
//
//  @Autowired
//  protected VaadinComponentFactory vaadinComponentFactory;
//
//  @Autowired
//  private MyUserDetailsService myUserDetailsService;
//
//  public GenericTab(Perfil perfil) {
//    this.perfil = perfil;
//    setCaption(perfil.getAuthority());
//    addComponents();
//    addListeners();
//  }
//
//  private void addComponents() {
//    customLayout = new CustomLayout("tabsheet");
//    menuSection = new MenuSection(perfil);
//    validacionParadas = new ValidacionParadas(perfil);
//    bodyContent = new VerticalLayout();
//    breadCrumb = new BreadCrumb();
//
////    mainWindow.addWindow(new Window("Test Window"));
//
//    refreshListenersButtonsBreadCrumb();
//
//    customLayout.addComponent(breadCrumb, "breadcrumb");
//    customLayout.addComponent(menuSection, "menusection");
//    customLayout.addComponent(bodyContent, "contentpage");
//    addComponent(customLayout);
//    setBodyContent(new BienvenidaContent(this));
//  }
//
//  private void addListeners() {
//    validacionParadas.addListener(new Listener() {
//
//      @Override
//      public void componentEvent(Event event) {
//        if (event != null && event instanceof Button.ClickEvent) {
//          clickValidacionParadas(event);
//        }
//      }
//    });
//
//    menuSection.addListener(new ItemClickEvent.ItemClickListener() {
//
//      @Override
//      public void itemClick(ItemClickEvent event) {
//        clickMenuSection(event);
//      }
//    });
//  }
//
//  private void clickValidacionParadas(Event event) {
//    OpcionModulo opcionModulo = validacionParadas.getOpcionModulo((Button) event.getComponent());
//    logger.debug("Call from linkItem click event [{}] {}", opcionModulo.getId(), opcionModulo.getNombre());
//    GenericContent genericContent = vaadinComponentFactory.getVaadinComponent(opcionModulo, this);
//    setBodyContent(genericContent);
//
//    List<OpcionModulo> path = menuSection.expandItems(opcionModulo.getId());
//    menuSection.select(opcionModulo);
//    breadCrumb.refreshPath(path);
//    refreshListenersButtonsBreadCrumb();
//    myUserDetailsService.logAccess(opcionModulo);
//  }
//
//  private void clickMenuSection(ItemClickEvent event) {
//    if (event.getButton() == ItemClickEvent.BUTTON_LEFT) {
//      OpcionModulo opcion = (OpcionModulo) event.getItemId();
//      // C�digo compartido con refreshListenersButtonsBreadCrumb
//      sharedCode(opcion);
//
//      if (menuSection.isExpanded(opcion)) {
//        List<OpcionModulo> path = menuSection.expandItems(opcion.getId());
//        breadCrumb.refreshPath(path);
//        menuSection.collapseItem(opcion);
//        refreshListenersButtonsBreadCrumb();
//      } else {
//        menuSection.expandItem(opcion);
//        List<OpcionModulo> path = menuSection.expandItems(opcion.getId());
//        breadCrumb.refreshPath(path);
//        refreshListenersButtonsBreadCrumb();
//      }
//      menuSection.select(opcion);
//      //Se hace la auditor�a del click en el men�
//      myUserDetailsService.logAccess(opcion);
//    }
//  }
//
//  private void refreshListenersButtonsBreadCrumb() {
//    LinkedList<Component> botonesBreadCrumb = breadCrumb.getComponents();
//    for (Component component : botonesBreadCrumb) {
//      final Button nuevo = (Button) component;
//      nuevo.addListener(new Button.ClickListener() {
//        private List<OpcionModulo> path;
//
//        @Override
//        public void buttonClick(ClickEvent event) {
//          if (nuevo.getCaption().equals("Inicio")) {
//            menuSection.collapseItems();
//            GenericTab.this.setBodyContent(new BienvenidaContent(GenericTab.this));
//          } else {
//            OpcionModulo opcion = (OpcionModulo) nuevo.getData();
//            //C�digo compartido con clickMenuSection
//            sharedCode(opcion);
//            path = menuSection.expandItems(opcion.getId());
//            menuSection.select(opcion);
//            //Se hace la auditor�a del click en el men�
//            myUserDetailsService.logAccess(opcion);
//          }
//          breadCrumb.refreshPath(path);
//          refreshListenersButtonsBreadCrumb();
//        }
//      });
//    }
//  }
//
// 
//  public void sharedCode(OpcionModulo opcion) {
//    /**
//     * Se a�adieron excepciones para los casos donde nodos padre tienen contenido
//     * Opcion: Volatilidades y deltas, ID: 2069
//     * Opcion: Generaci�n de Archivos - Derivados, ID: 2075
//     * Opcion: Generaci�n de Archivos - Derivex, ID: 4000
//  
//    if (!menuSection.isParent(opcion) || opcion.getId() == 2069 || opcion.getId() == 2075 || opcion.getId() == 4000) {
//      GenericContent genericContent = vaadinComponentFactory.getVaadinComponent(opcion, this);
//      setBodyContent(genericContent);
//    } else {
//      setBodyContent(new BienvenidaContent(this));
//    }
//  }
//
//  public void setBodyContent(ComponentContainer componentContainer) {
//    bodyContent.removeAllComponents();
//    logger.debug(bodyContent == null ? "NULL" : bodyContent.toString());
//    logger.debug(componentContainer == null ? "NULL" : componentContainer.toString());
//    bodyContent.addComponent(componentContainer);
//  }
//
//  public void setVaadinComponentFactory(VaadinComponentFactory vaadinComponentFactory) {
//    logger.debug("setVaadinComponentFactory call ");
//    this.vaadinComponentFactory = vaadinComponentFactory;
//  }
//
//  public MenuSection getMenuSection() {
//    return menuSection;
//  }
//
//  public ValidacionParadas getValidacionParadas() {
//    return validacionParadas;
//  }
//
//  public void goHome() {
//    setBodyContent(new BienvenidaContent(this));
//  }
//
//  public Perfil getPerfil() {
//    return perfil;
//  }
//  */
//}
