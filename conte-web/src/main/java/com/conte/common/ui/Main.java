///*
// * Informacion de Valoracion INFOVALMER, Bolsa de Valores de Colombia BVC
// * Copyright (C) 2012 Quasar Software Ltda.
// */
//package com.conte.common.ui;
//
//
//import com.vaadin.data.util.BeanItem;
//import com.vaadin.terminal.ExternalResource;
//import com.vaadin.terminal.ThemeResource;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.CustomComponent;
//import com.vaadin.ui.CustomLayout;
//import com.vaadin.ui.Embedded;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Link;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.themes.BaseTheme;
//import java.util.Calendar;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;
//
//@Configurable
//public class Main extends CustomComponent {
///*
//  @Autowired
//  private MyUserDetailsService userDetailsService;
//
//  private Window window;
//
//  public Main(Window window) {
//    this.window = window;
//    CustomLayout content = new CustomLayout("main");
//
//    Embedded logo = new Embedded("", new ThemeResource("img/logo_bvc.gif"));
//    Embedded animation = new Embedded("", new ThemeResource("img/logotipo_infovalmer.png"));
//
//    VerticalLayout tabContainer = new VerticalLayout();
//
//    tabContainer.setStyleName("maincontent");
//    tabContainer.addComponent(new TabBuilder(window));
//    tabContainer.addComponent(new Label("(C) Quasar Software Ltda."));
//
//    content.setStyleName("principal");
//    content.addComponent(logo, "logo");
//    content.addComponent(animation, "animation");
//    content.addComponent(tabContainer, "maincontent");
//
//    setCompositionRoot(content);
//  }
//
//  @Override
//  public void attach() {
//    super.attach();
//
//    HorizontalLayout buttonPanel = new HorizontalLayout();
//
//    final Usuario usuario = userDetailsService.getUsuarioAutenticado();
//    final UsuarioPasswordWindow usuarioPasswordWindow = new UsuarioPasswordWindow();
//
//    Button cuentaUsuario = new Button(usuario.getUsername(), new Button.ClickListener() {
//
//      @Override
//      public void buttonClick(Button.ClickEvent event) {
//
//        usuarioPasswordWindow.removeAllComponents();
//
//        Usuario usuarioAux = userDetailsService.loadUserByUsername(usuario.getUsername());
//
//        int diasMinParaCambio = AppConfigParams.getInstance().getGeneric().getPasswordTiempoMinimoCambio();
//        Calendar ultCambClave = usuarioAux.getUltimoCambioClave();
//        ultCambClave.add(Calendar.DAY_OF_MONTH, diasMinParaCambio);
//        Calendar fechaActual = Calendar.getInstance();
//
//        if (ultCambClave.after(fechaActual)) {
//          long millis = ultCambClave.getTimeInMillis() - fechaActual.getTimeInMillis();
//          double diff = (double) millis / (1000 * 60 * 60 * 24);
//          int dias = (int) Math.ceil(diff);
//          usuarioPasswordWindow.addComponent(new Label("Debe esperar " + dias + " dias para"
//                  + " volver a cambiar a su clave."));
//        } else {
//          BeanItem<Usuario> usuarioBean = new BeanItem<Usuario>(usuarioAux);
//          UsuarioPasswordForm usuarioForm = new UsuarioPasswordForm(usuarioBean);
//          usuarioForm.addDiscardButton();
//          usuarioPasswordWindow.addComponent(usuarioForm);
//        }
//
//        getApplication().getMainWindow().addWindow(usuarioPasswordWindow);
//      }
//    });
//
//    cuentaUsuario.setStyleName(BaseTheme.BUTTON_LINK + " cuenta-usuario-link");
//    cuentaUsuario.setIcon(new ThemeResource("../runo/icons/16/user.png"));
//
//    Button helper = new Button();
//    helper.setStyleName(BaseTheme.BUTTON_LINK);
//    helper.setDescription("Validaci&oacute;n - Paradas");
//    helper.setIcon(new ThemeResource("../infovalmer/img/v&p_icon.png"));
//    helper.addListener(new Button.ClickListener() {
//
//      @Override
//      public void buttonClick(ClickEvent event) {
//        if (!getWindow().getChildWindows().contains(window)) {
//          getWindow().addWindow(window);
//          window.setVisible(true);
//        }
//      }
//    });
//
//    Link salir = new Link("", new ExternalResource("logout"));
//    salir.setIcon(new ThemeResource("../infovalmer/img/logout_icon.png"));
//    salir.setDescription("Cerrar sesi&oacute;n");
//
//    buttonPanel.setSpacing(true);
//    buttonPanel.addComponent(helper);
//    buttonPanel.addComponent(cuentaUsuario);
//    buttonPanel.addComponent(salir);
//
//    ((CustomLayout) getCompositionRoot()).addComponent(buttonPanel, "buttons");
//  }*/
//}
