/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.ContraseniasDao;
import co.org.conte.sgm.dao.DepartamentoDao;
import co.org.conte.sgm.dao.PerfilDao;
import co.org.conte.sgm.dao.TipoDocumentoDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.TipoDocumento;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.entity.UsuarioContraseniaHistorial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Configurable;

/**
 *
 * @author jam
 */
@Configurable(preConstruction = true)
public class UsuarioService {
    
    private DepartamentoDao daoDepartamento;
    private PerfilDao daoPerfil;
    private UsuarioDao daoUsuario;
    private ContraseniasDao daoContrasenias;
    private TipoDocumentoDao daoTipoDocumento;
    @Resource
    private MailService mailService;

    public UsuarioService() {
        daoDepartamento = new DepartamentoDao();
        daoContrasenias = new ContraseniasDao();
        daoPerfil = new PerfilDao();
        daoUsuario = new UsuarioDao();        
        daoTipoDocumento = new TipoDocumentoDao();
    }
    
    public Usuario findById(Serializable id){
        try {
            return daoUsuario.findById(id);
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Departamento findDepartamentoById(Serializable id){
        try {
            return daoDepartamento.findById(id);
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Departamento> getAllDepartamentos(){
        List<Departamento> departamentos = null;
        try {
            departamentos = daoDepartamento.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return departamentos;        
    }
    
    public List<Perfil> getAllPerfiles(){
        List<Perfil> perfiles = null;
        try {
            perfiles = daoPerfil.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return perfiles;        
    }
    
    public List<TipoDocumento> getAllTipoDocuementos(){
        List<TipoDocumento> documentos = null;
        try {
            documentos = daoTipoDocumento.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documentos;
    }
    
    public int inactivarUsuarios(Set<Usuario> usuarios){
        int inactivos = 0;
        if(usuarios != null){
//            inactivos = new ArrayList<Usuario>();
            for(Usuario u : usuarios){
                u.setEstado("Inactivo");
                try {
                    u.getPerfil();
                    daoUsuario.update(u);
                    mailService.send(u.getEmail(), "Cuenta Inactivada", "Su cuenta se ha inactivado");
                    inactivos++;
                } catch (DaoException ex) {
                    Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return inactivos;
    }
    
    
    public List<Usuario> findByInactivity(Date date){
        return daoUsuario.findByInactivity(date);        
    }
    
    public Usuario findByDocumento(Usuario usuario){
        return daoUsuario.findByDocumento(usuario.getTipoDocumento(), usuario.getDocumento());
    }
    
    public Usuario login(Usuario usuario){
        return daoUsuario.login(usuario);        
    }
    
    public Usuario update(Usuario usuario){
        try {
            return daoUsuario.update(usuario);
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean validateEmail(Usuario usuario){
        Usuario u = new Usuario();
        u.setEmail(usuario.getEmail());
        try {
            return daoUsuario.findBy(usuario).isEmpty();
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<UsuarioContraseniaHistorial> getContrasenias(Usuario usuario){        
        return daoContrasenias.findByUsuario(usuario);
    }
    
    public List<Usuario> getUsuarios(){
        try {
            return daoUsuario.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }       
    }
    
    /**
     * Actualiza usuario
     * @param usuario Que se va actualizar
     * @return <code>Usuario</code> que se actualizo,<br/> <code>null</code> en caso de falla
     */
    public Usuario merge(Usuario usuario){
        try {
            return daoUsuario.update(usuario);
        } catch (DaoException ex) {            
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}