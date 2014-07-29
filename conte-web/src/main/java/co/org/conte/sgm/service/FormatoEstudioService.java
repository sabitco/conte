/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.FormatoEstudioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.FormatoEstudio;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class FormatoEstudioService {
    
    private FormatoEstudioDao dao;

    public FormatoEstudioService() {
        
        dao = new FormatoEstudioDao();
    }
    
    public FormatoEstudio findById(Serializable id){
        try {
            dao.findById(id);
        } catch (DaoException ex) {
            Logger.getLogger(FormatoEstudioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}