/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.UsuarioService;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Locale;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.MessageSource;

/**
 *
 * @author jam
 */
@Configurable(preConstruction = true)
public abstract class GenericContent extends VerticalLayout {
    
    protected final Logger logger;
    @Resource
    protected MessageSource messages;
    protected Usuario usuario;
    protected UsuarioService serviceUsuario;
    protected Window subwindow;
    
    public GenericContent(Usuario usuario) {
        logger = LoggerFactory.getLogger(getClass());
        this.usuario = usuario;
        
        serviceUsuario = new UsuarioService();
             
   
    }

    public String getMessage(String code, Object... args) {
        return messages.getMessage(code, args, Locale.ROOT);
    }
    
    public String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }
    
    public  Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    protected void launchSubwindow(String message) {
        subwindow = new Window("Mensaje");
        subwindow.setModal(true);
        subwindow.setResizable(false);
        subwindow.setDraggable(false);
        subwindow.addComponent(new Label(message, Label.CONTENT_XHTML));
        getWindow().addWindow(subwindow);
//        getWindow().executeJavaScript("alert(\""+ message + "\")");
  } 
    
    protected ServletContext getServletContext() {
        ApplicationContext ctx = getApplication().getContext();
        if (ctx == null) {
            return null;
        }
        final ServletContext sCtx = ((WebApplicationContext)ctx).getHttpSession().getServletContext();
        return sCtx;
    }
    
    protected String getURL(){        
        final ServletContext sCtx = getServletContext();
        String x = getWindow().getURL().toString();
        String url = x.substring(0,x.indexOf(sCtx.getContextPath()))+"/";
        System.out.println("la URL es "+url);
        return url;
    }
}