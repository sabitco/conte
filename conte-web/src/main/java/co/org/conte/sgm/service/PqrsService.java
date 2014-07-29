/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.PqrsDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Pqrs;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class PqrsService {
    
    private PqrsDao dao;

    public PqrsService() {
        dao = new PqrsDao();
    }
    
    public List<Pqrs> getAllPqrs(){
        try {
            return dao.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(PqrsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Pqrs findById(Pqrs pqrs){
        try {
            return dao.findById(pqrs.getConsecutivo());
        } catch (DaoException ex) {
            Logger.getLogger(PqrsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Pqrs save(Pqrs pqrs){
        try {
            return dao.insert(pqrs);
        } catch (DaoException ex) {
            Logger.getLogger(PqrsService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}