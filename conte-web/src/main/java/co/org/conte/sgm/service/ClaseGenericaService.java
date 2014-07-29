/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.ClaseGenericaDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.ClaseGenerica;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class ClaseGenericaService {
    
    ClaseGenericaDao dao;

    public ClaseGenericaService() {
        dao = new ClaseGenericaDao();
    }
    
    public List<ClaseGenerica> findAll(){
        try {
            return dao.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(ClaseGenericaService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
}
