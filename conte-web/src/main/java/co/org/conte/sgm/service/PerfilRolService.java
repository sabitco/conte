package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.PerfilRolDao;
import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.PerfilRol;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author jam
 */
public class PerfilRolService implements Serializable {

    PerfilRolDao daoPerfilRol;
    
    public PerfilRolService() {
        daoPerfilRol = new PerfilRolDao();
    }
    
    public List<PerfilRol> getPerfilRolByPerfil(Perfil perfil){
        return daoPerfilRol.findByPerfil(perfil);
           
    }
}
