/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.PagoDao;
import co.org.conte.sgm.dao.ParametroDao;
import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Pago;
import co.org.conte.sgm.entity.Parametro;
import co.org.conte.sgm.entity.Solicitud;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author jam
 */
public class PagoService {
    
    PagoDao dao = new PagoDao();
    SolicitudDao daoSolicitud = new SolicitudDao();
    
    public boolean addPago(String[] pago){
        boolean retVal = false;
        String document = pago[6].replaceFirst("[0]", "");
        while(document.startsWith("0")){
            document = document.replaceFirst("[0]", "");
        }

        Pago p = new Pago();
        Solicitud s = new Solicitud();
        p.setDocumento(document);            
        p.setFechaIngreso(new Date());
        p.setSolicitud(s);
        p.setValor(new Double(pago[0]));  
        try {
            p.setFechaConsignacion(new SimpleDateFormat("yyyy-MM-dd").parse(pago[4]));
            retVal = dao.save(p)==1;
            if(!retVal){
                dao.save2(p);
            }                        
        } catch (ConstraintViolationException cve) {
            Logger.getLogger(PagoService.class.getName()).log(Level.SEVERE, null, cve);
        } catch (DaoException ex) {                
            Logger.getLogger(PagoService.class.getName()).log(Level.SEVERE, null, ex);                
        } catch (ParseException ex) {
            Logger.getLogger(PagoService.class.getName()).log(Level.SEVERE, null, ex);
        }           
        
        return retVal;
    } 
    
    public String getRadicadoByDocumento(String documento){
        return daoSolicitud.getRadicadoByDocumento(documento).toString();
    }
}