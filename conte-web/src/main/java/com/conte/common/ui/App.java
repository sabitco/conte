//package com.conte.common.ui;
//
//
//import com.conte.common.ui.util.BaseApplication;
//import com.conte.servicios.usuario.GestionUsuarios;
//import com.vaadin.ui.Window;
//import java.util.Locale;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
///**
// * Punto principal de entrada a la aplicacion
// */
//@Component(value = "app")
//@Scope(value = "session")
//public class App extends BaseApplication {
//
//  private static final long serialVersionUID = -4879742890483528287L;
//
//  private final Logger logger = LoggerFactory.getLogger(getClass());
//
//  @Autowired
//  private GestionUsuarios usuariosService;
//
//  private Window mainWindow;
//  private Window welcomeWindow;
//  private Window validationWindow;
//
//  @Override
//  public void init() {
//    setTheme("infovalmer");
//    setLogoutURL("/logout");
//    Locale defLocal = new Locale("es", "CO");
//    Locale.setDefault(defLocal);
//
//    mainWindow = new Window();
//    validationWindow = new Window("Infovalmer");
//    validationWindow.setResizable(false);
//    validationWindow.setImmediate(false);
//    validationWindow.setPositionX(920);
//    validationWindow.setPositionY(160);
//    validationWindow.setHeight("500px");
//    validationWindow.setWidth("350px");
//
// /*   Main main = new Main(validationWindow);
//    mainWindow.setContent(main);
//    setMainWindow(mainWindow);
///*
//    showWelcomeMessage();
//    validarPasswordCambioRequerido();
//    mainWindow.addWindow(validationWindow);
//  }
//
//  private void showWelcomeMessage() {
//    Usuario usuario = userDetailsService.getUsuarioAutenticado();
//
//    welcomeWindow = new Window("Bienvenido a Infovalmer");
//    welcomeWindow.setWidth("480px");
//    welcomeWindow.setHeight("170px");
//    welcomeWindow.setModal(true);
//    welcomeWindow.setResizable(false);
//
//    VerticalLayout layout = (VerticalLayout) welcomeWindow.getContent();
//    layout.setMargin(true);
//    layout.setSpacing(true);
//
//    Label usuarioMsg = new Label("Sr(a): " + usuario.getNombreCompleto());
//    Label welcomeMsg;
//
//    if (usuario.getUltimoLogin() == null) {
//      welcomeMsg = new Label("Este es su primer inicio de sesión");
//    } else {
//      String fecha = DateUtil.getInstance().formatAsHumanFull(usuario.getUltimoLogin());
//      welcomeMsg = new Label("Su anterior inicio de sesion fue el: " + fecha);
//    }
//
//    layout.addComponent(usuarioMsg);
//    layout.addComponent(welcomeMsg);
//
//    Button close = new Button("Cerrar", new Button.ClickListener() {
//
//      @Override
//      public void buttonClick(ClickEvent event) {
//        welcomeWindow.getParent().removeWindow(welcomeWindow);
//      }
//    });
//
//    layout.addComponent(close);
//    layout.setComponentAlignment(close, Alignment.BOTTOM_RIGHT);
//
//    getMainWindow().addWindow(welcomeWindow);
//
//    close.addShortcutListener(new ShortcutListener("Escape", KeyCode.ESCAPE, null) {
//
//      @Override
//      public void handleAction(Object sender, Object target) {
//        welcomeWindow.getParent().removeWindow(welcomeWindow);
//      }
//    });
//
//    welcomeWindow.focus();
//  }
//
//  private void validarPasswordCambioRequerido() {
//
//    boolean debeCambiarClave = true;
//
//    Usuario usuario = userDetailsService.getUsuarioAutenticado();
//
//    Calendar ultCambClave = usuario.getUltimoCambioClave();
//
//    String msg;
//
//    if (ultCambClave == null) {
//      msg = "Ingreso de Clave requerido";
//    } else {
//      msg = "Cambio de Clave obligatorio";
//      int diasExpiracion = AppConfigParams.getInstance().getGeneric().getPasswordTiempoExpiracion();
//      ultCambClave.add(Calendar.DAY_OF_MONTH, diasExpiracion);
//      Calendar fechaActual = Calendar.getInstance();
//      debeCambiarClave = ultCambClave.before(fechaActual);
//    }
//
//    if (debeCambiarClave) {
//      UsuarioPasswordWindow passwordWindow = new UsuarioPasswordWindow();
//      passwordWindow.setCaption(msg);
//      passwordWindow.setClosable(false);
//      BeanItem<Usuario> usuarioBean = new BeanItem<Usuario>(usuario);
//      UsuarioPasswordForm usuarioForm = new UsuarioPasswordForm(usuarioBean);
//      passwordWindow.addComponent(usuarioForm);
//      passwordWindow.addListener(new Window.CloseListener() {
//        @Override
//        public void windowClose(CloseEvent e) {
//          welcomeWindow.focus();
//        }
//      });
//
//      getMainWindow().addWindow(passwordWindow);
//      passwordWindow.bringToFront();
//    }
//  }
//
//  @Override
//  public void close() {
//    logger.debug("Application.close() method invoked.");
//    super.close();
//  }*/
//  }
//}
