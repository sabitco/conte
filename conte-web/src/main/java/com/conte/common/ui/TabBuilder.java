///*
// * Informacion de Valoracion INFOVALMER, Bolsa de Valores de Colombia BVC
// * Copyright (C) 2012 Quasar Software Ltda.
// */
//package com.conte.common.ui;
//
//import com.vaadin.ui.TabSheet;
//import com.vaadin.ui.Window;
//import java.util.Collection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//
///**
// * Construye los tabs a partir de los roles de usuario.
// *
// * @author Carlos Uribe [curibe@quasarbi.com]
// */
//
//@Configurable(preConstruction = true)
//public class TabBuilder extends TabSheet /* implements TabSheet.SelectedTabChangeListener*/ {
///*
//  @Autowired
//  private MyUserDetailsService myUserDetailsService;
//
//  private Window window;
//
//  public TabBuilder(Window window) {
//    this.window = window;
//    buildTabs();
//  }
//
//  private void buildTabs() {
//    Collection<Perfil> perfiles = myUserDetailsService.findPerfilesOpcionesModuloUsuarioAutenticado();
//
//    for (Perfil perfil : perfiles) {
//      GenericTab genericTab = new GenericTab(perfil);
//      addTab(genericTab);
//
//      if (getComponentCount() == 1) {
//        myUserDetailsService.logAccess(genericTab.getCaption());
//        window.setContent(genericTab.getValidacionParadas());
//      }
//    }
//    addListener(this);
//  }
//
//  @Override
//  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
//    TabSheet tabsheet = event.getTabSheet();
//    TabSheet.Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
//
//    if (tab != null) {
//      myUserDetailsService.logAccess(tab.getCaption());
//
//      GenericTab genericTab = (GenericTab)tab.getComponent();
//      window.setContent(genericTab.getValidacionParadas());
//      window.setVisible(genericTab.getValidacionParadas().isVisible());
//    }
//  }*/
//}
