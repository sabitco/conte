///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package co.org.conte.sgm.ui;
//
//import co.org.conte.sgm.ui.usuario.RecuperarContrasenia;
//import co.org.conte.sgm.ui.certificado.ValidarCertificadoVigencia;
//import co.org.conte.sgm.entity.Usuario;
//import co.org.conte.sgm.service.UsuarioService;
//import co.org.conte.sgm.ui.crud.AsociacionInspectorCrud;
//import co.org.conte.sgm.ui.crud.FormatoCalificacionCrud;
//import co.org.conte.sgm.ui.crud.InstitucionEducativaCrud;
//import co.org.conte.sgm.ui.crud.ParametroCrud;
//import co.org.conte.sgm.ui.crud.UsuarioCrud;
//import co.org.conte.sgm.util.Password;
//import com.vaadin.Application;
//import com.vaadin.data.Validator;
//import com.vaadin.terminal.Sizeable;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.HorizontalSplitPanel;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Layout;
//import com.vaadin.ui.TabSheet;
//import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.themes.Runo;
//import java.util.Date;
//
///**
// *
// * @author jam
// */
//public class Template  extends Application {
//
//    @SuppressWarnings("deprecation")
//    private static final Date DATEFIELD_VALUE = new Date(2010 - 1900, 7 - 1,
//            19, 14, 46, 00);
//    UsuarioService serviceUsuario = new UsuarioService();
//    Usuario usuario = new Usuario();
//    private TabSheet right = new TabSheet();
//    private TabSheet styles = new TabSheet();
//    private TabSheet samples = new TabSheet();
//    EditarSolicitud editarSolicitud = new EditarSolicitud(usuario);
////    private CaptchaField captcha;
////    private TPTCaptcha captcha= new  TPTCaptcha();
//
//    @Override
//    public void init() {
//        setTheme("conte");
//
//        VerticalLayout root = new VerticalLayout();
//        root.setMargin(true);
//        root.setSizeFull();
//        Window main = new Window("CONTE", root);
//        setMainWindow(main);
//
//        Label title = this.createTitle();
//        root.addComponent(title);
//        root.setComponentAlignment(title, Alignment.TOP_CENTER);
//
//        Label slogan = this.createSlogan();
//        root.addComponent(slogan);
//        root.setComponentAlignment(slogan, Alignment.TOP_CENTER);
//
////        Label spacer = new Label("");
////        spacer.setHeight("20px");
////        root.addComponent(spacer);
//
//        final HorizontalSplitPanel split = new HorizontalSplitPanel();
//        split.setStyleName(Runo.SPLITPANEL_REDUCED);
//        split.setSplitPosition(1, Sizeable.UNITS_PIXELS);
//        split.setLocked(true);
//        root.addComponent(split);
////        root.addComponent(captcha);
//        root.setExpandRatio(split, 1);
//
////        Panel left = new Panel("Example Sidebar");
////        left.setSizeFull();
////        split.setFirstComponent(left);
////        captcha = new CaptchaField(this);
//       
//        right.setSizeFull();
//        // right.addStyleName(Runo.TABSHEET_SMALL);
//        split.setSecondComponent(right);
//        split.setLocked(true);
////
////        right.addTab(buildWelcomeScreen(), "Welcome");
////        right.addTab(buildSamples(), "Sample Layouts");
////        right.addTab(buildStyleReference(), "Style Reference");
//        right.setImmediate(true);
//      
//        right.addListener(new TabSheet.SelectedTabChangeListener() {
//            @Override
//            public void selectedTabChange(SelectedTabChangeEvent event) {
////                getMainWindow().executeJavaScript("javascript:vaadin.forceSync();");
//                if(right.getSelectedTab().getCaption()!=null){
////                    System.out.print(right.getSelectedTab().getCaption());
//                    if(right.getSelectedTab().getCaption().equalsIgnoreCase("Editar Solicitud")){ 
//                        usuario = serviceUsuario.login(usuario);
//                        System.out.print("Esta en editar solicitud con el otro if");
//                        editarSolicitud = new EditarSolicitud(usuario);                         
//                        right.replaceComponent(right.getSelectedTab(), editarSolicitud);
//                    }
//                    
//                    if(right.getSelectedTab().getCaption().equalsIgnoreCase("Crear Solicitud")){ 
//                        usuario = serviceUsuario.login(usuario);
//                        right.replaceComponent(right.getSelectedTab(), new CrearSolicitud(usuario));
//                    }
//                    
//                    if(right.getSelectedTab().getCaption().equalsIgnoreCase("ListarSolicitudes")){ 
//                        usuario = serviceUsuario.login(usuario);
//                        right.replaceComponent(right.getSelectedTab(), new ListarSolicitudes(usuario));
//                    }
//                    
//                    if(right.getSelectedTab().getCaption().equalsIgnoreCase("ConsultarEstadoTramite")){ 
//                        usuario = serviceUsuario.login(usuario);
//                        right.replaceComponent(right.getSelectedTab(), new ConsultarEstadoTramite(usuario));
//                    }
//                    if(right.getSelectedTab().getCaption().equalsIgnoreCase("ReciboConsignacion")){ 
//                        usuario = serviceUsuario.login(usuario);
//                        right.replaceComponent(right.getSelectedTab(), new ReciboConsignacion(usuario));
//                    }
//                }
//            }
//        });
//        
//        createLogin();
//    }
//   
//    private Layout buildWelcomeScreen() {
//        VerticalLayout l = new VerticalLayout();
//        l.setSizeFull();
//        l.setCaption("Welcome");
//        l.addComponent(new Login() {
//
//            @Override
//            protected void process() {
//                
//                right.removeAllComponents();
//                right.addTab(new CambiarContrasenia(null),"Cambiar Contraseña");
//                
////                right.addTab(new ConsultarSanciones(),"Consultar Sanciones");
////                right.addTab(new CrearCuenta(),"Crear Cuenta");
//               // right.addTab(new CrearSolicitud(1),"Crear Solicitud");
////                right.addTab(new ListarSolicitudes(new Long(1)),"Listar Solicitud");
//                right.addTab(new RecuperarContrasenia(),"Recuperar Contraseña");
//                right.addTab(new ValidarCertificadoVigencia(),"Validar Certificado de Vigencia");
//            }
//        });
//         return l;
//    }
//    
//    private Label createSlogan(){
//        Label slogan = new Label(
//                "Sistema Gestión de Matriculas");
//        slogan.addStyleName(Runo.LABEL_SMALL);
//        slogan.setSizeUndefined();
//        return slogan;
//    }
//    
//    private Label createTitle(){
//        Label title = new Label("Conte");
//        title.addStyleName(Runo.LABEL_H1);
//        title.setSizeUndefined();
//        return title;
//    }
//    
//    private void createLogin(){        
//        this.right.addTab(new ValidarCertificadoVigencia(), "Validar Certificado");
//        this.right.addTab(new RecuperarContrasenia(), "Recuperar Contraseña");
////        this.right.addTab(new CrearCuenta(), "Crear Cuenta");
//        this.right.addTab(new Login() {
//
//            @Override
//            protected void process() {
//                try {                    
//                    validate();
//                    usuario = new Usuario();
//                    usuario.setDocumento(getValue(login));
//                    usuario.setContrasena(Password.encryptPassword(getValue(password)));
//                    usuario = serviceUsuario.login(usuario);
//                    if(usuario!=null){
//                        right.removeAllComponents();
//                        
//                        
//                        switch (usuario.getPerfil().getCodigo()){
//                            case 1:
//                                editarSolicitud = new EditarSolicitud(usuario);
//                                createMenuTecnico();
//                                break;
//                            case 2:
//                                right.addTab(new ListarSolicitudes(usuario), "Listar formularios");
//                        right.addTab(new ConsultarEstadoTramite(usuario), "Consultar Estado Tramite");                        
//                        right.addTab(new CambiarContrasenia(usuario), "Cambiar Contraseña");
//                        right.addTab(new CrearSolicitud(usuario), "Crear Solicitud");
//                        right.addTab(new EditarSolicitud(usuario), "Editar Solicitud");
//                        right.addTab(new ReciboConsignacion(usuario), "Recibo de Consignación");
//                                break;
//                            case 3:
//                                createMenuAdmin();
//                                break;
//                                
//                        }
//                        right.addTab(new Logout(), "Cerrar Sesión");
//                    } else {
//                        message.setCaption("Nombre de usuario o contraseña incorrecto");
//                    }
//                } catch (Validator.InvalidValueException ive) {
//                    message.setCaption("Existen campos incorrectos");
//                }
//            }
//        }, "Ingresar");
//    }
//    
//    private void createMenuAdmin(){
//        right.removeAllComponents();
//        right.addTab(new UsuarioCrud(),"Administrar Usuarios");
//        right.addTab(new InstitucionEducativaCrud(),"Administrar Instituciones");
//        right.addTab(new AsociacionInspectorCrud(),"Administrar Asociaciones");
//        right.addTab(new ParametroCrud(),"Administrar Parametros");
//        right.addTab(new FormatoCalificacionCrud(), "Administrar Formatos");
//        right.addTab(new InactivarCuentas(),"Inactivar Cuentas");
//        right.addTab(new EliminarSolicitudes(), "Eliminar Solicitudes");        
//    }
//    
//    private void createMenuTecnico(){
//        right.removeAllComponents();
//        right.addTab(new ConsultarSanciones(usuario), "Consultar Sanciones Disciplinarias");
//        right.addTab(new GenerarCertificadoVigencia(usuario), "Certificado de Vigencia");
////        right.addTab(new ListarSolicitudes(usuario), "Listar formularios");
//        right.addTab(new ConsultarEstadoTramite(usuario), "Consultar Estado Tramite");                        
//        right.addTab(new CambiarContrasenia(usuario), "Cambiar Contraseña");
//        right.addTab(new CrearSolicitud(usuario), "Crear Solicitud");
//        right.addTab(editarSolicitud, "Editar Solicitud");
////        right.addTab(new ReciboConsignacion(usuario), "Recibo de Consignación");
//    }
//    
//}