package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.PerfilRol;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.PerfilRolService;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;
import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Configurable;

/**
 * Construye los tabs a partir de los roles de usuario.
 *
 * @author Carlos Uribe [curibe@quasarbi.com]
 */
public class TabBuilder extends TabSheet implements TabSheet.SelectedTabChangeListener {

//  private MyUserDetailsService myUserDetailsService;

  private Window window;
  private Main main;
  private PerfilRolService servicePerfilRol;
  private Usuario usuario;
 
  
  public TabBuilder(Window window, Main main, Usuario usuario) {
    this.window = window;
    this.main = main;
    servicePerfilRol = new PerfilRolService();
    this.usuario = usuario;
    buildTabs();
    
  }

  private void buildTabs() {
      List<PerfilRol> perfiles;
      
      if(usuario!=null){
          perfiles = servicePerfilRol.getPerfilRolByPerfil(usuario.getPerfil());
      } else {
          
              perfiles = servicePerfilRol.getPerfilRolByPerfil(new Perfil(1));
          
      }
      
      for (PerfilRol perfilRol : perfiles) {
          GenericTab genericTab = new GenericTab(perfilRol.getRol(), usuario);
          addTab(genericTab);
      }
      addListener(this);
  }

  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    TabSheet tabsheet = event.getTabSheet();    
    TabSheet.Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());

    if (tab != null) {
//      myUserDetailsService.logAccess(tab.getCaption());
//
//      ValidacionParadas validator = ((GenericTab)tab.getComponent()).getValidacionParadas();
//      window.setContent(validator);
//
//      if (!validator.getProcesos().isEmpty()) {
//        main.setHelperEnabled(true);
//      } else {
//        main.setHelperEnabled(false);
//        window.setVisible(false);
//      }
    }
  }

//  public void showValidator() {
//    if (!getWindow().getChildWindows().contains(window)) {
//      getWindow().addWindow(window);
//    }
//  }
}
