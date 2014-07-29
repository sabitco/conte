package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.encuesta.Encuesta;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import javax.servlet.ServletContext;

/**
 *
 * @author jam
 */
public class App extends Application{

    private Window mainWindow;
    private Usuario usuario;
    final Window mywindow = new Window("Encuesta");

    public App() {
        mywindow.setTheme("conte");
        // Manually set the name of the window.
        mywindow.setName("encuesta");
        mywindow.addComponent(encuesta());
    }
    
    
    
    @Override
    public void init() {
        setTheme("conte");
        mainWindow = new Window();
        mainWindow.addComponent(new Main(mainWindow, usuario));
        mainWindow.setImmediate(true);
        setMainWindow(mainWindow);
        
        
        
        
        //usuario.setNombres("encuesta");
        
        // Add some content to the window.
//        mywindow.addComponent(new Encuesta(usuario));

        // Add the window to the application.
        addWindow(mywindow);
    }

    @Override
    public void setUser(Object user) {
//         System.out.print("LA URL ES ------>"+getURL().toString());
//        //if(getWindow("encuesta").equals(mywindow)){
//        if(getURL().toString().contains("encuesta")){
//            System.out.print("EN EL IF");
//            removeWindow(mywindow);
////            setMainWindow(mainWindow);
//        } else {
            this.usuario = (Usuario)user;
            removeWindow(mainWindow);
            init();
//        }                
    }
    
    
    
    
    private CustomLayout encuesta(){
        CustomLayout content = new CustomLayout("main");
        Embedded animation = new Embedded("", new ThemeResource("img/baner_conte.png"));        
        VerticalLayout tabContainer = new VerticalLayout();
        
        
        tabContainer.setStyleName("maincontent");
        tabContainer.addComponent(new Encuesta(usuario));
        
        tabContainer.addComponent(new Label("(C) Quasar Software Ltda."));
        
        content.setStyleName("principal");
        content.addComponent(animation, "animation");
        content.addComponent(tabContainer, "maincontent");
        return content;
    }
}
