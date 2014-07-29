package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Usuario;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.text.SimpleDateFormat;

public class Main extends CustomComponent {
    
    private Usuario usuario;
    private Window window;
    
    public Main(Window window, Usuario usuario) {
        this.usuario = usuario;
        this.window = window;
        
        CustomLayout content = new CustomLayout("main");
        Embedded animation = new Embedded("", new ThemeResource("img/baner_conte.png"));        
        VerticalLayout tabContainer = new VerticalLayout();
        
        tabContainer.setStyleName("maincontent");
        tabContainer.addComponent(new TabBuilder(window, this, usuario));
        
        tabContainer.addComponent(new Label("(C) Quasar Software Ltda."));
        
        content.setStyleName("principal");
        content.addComponent(animation, "animation");
        content.addComponent(tabContainer, "maincontent");

        if(this.usuario != null){
            this.window.showNotification("Su último acceso fue el "+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(this.usuario.getUltimoAcceso()));
        }
        
        setImmediate(true);
        setCompositionRoot(content);
    }

    @Override
    public void attach() {
        super.attach();
        
        HorizontalLayout buttonPanel = new HorizontalLayout();
        
       
        if(usuario != null){
            Link salir = new Link("", null);
            salir.setIcon(new ThemeResource("../conte/img/logout_icon.png"));
            salir.setDescription("Cerrar sesi&oacute;n");
            buttonPanel.setSpacing(true);
            buttonPanel.addComponent(salir);
        }
        ((CustomLayout) getCompositionRoot()).addComponent(buttonPanel, "buttons");
  }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
}