package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.AsociacionDao;
import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.TipoSolicitudDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Asociacion;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class SolicitudService {
    
    private AsociacionDao daoAsociacion;
    private SolicitudDao daoSolicitud;
    private TipoSolicitudDao daoTipoSolicitud;
    
    
    public SolicitudService(){
        daoAsociacion = new AsociacionDao();
        daoSolicitud = new SolicitudDao();
        daoTipoSolicitud = new TipoSolicitudDao();
    }
    
    public List<TipoSolicitud> getTipoSolicitud(){
        try {
            return daoTipoSolicitud.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(SolicitudService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int getCantidadSolicitudes(Usuario usuario, TipoSolicitud tipoSolicitud){
        int cantidad = 0;
        if(tipoSolicitud != null){
            for(Solicitud solicitud : usuario.getSolicituds()){                
                if(solicitud.getTipoSolicitud().getCodigo()==(tipoSolicitud.getCodigo())){
                    if(solicitud.getEstado().getCodigo()==1){
                        cantidad++;
                    }
                }
            }
        }
        return cantidad;
    }

    public List<Asociacion> getAsocioaciones() {
        try {
            return daoAsociacion.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(SolicitudService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Solicitud findById(Serializable id){
        try {       
            return daoSolicitud.findById(id);
        } catch (DaoException ex) {
            Logger.getLogger(SolicitudService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Solicitud> eliminarSolicitudes(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        List<Solicitud> solicitudes = daoSolicitud.findByInactividad(date);
        List<Solicitud> eliminadas = new ArrayList<Solicitud>();
        if(solicitudes!=null || solicitudes.size() > 0){
            for(Solicitud s : solicitudes){
                try {
                    daoSolicitud.delete(s);
                    eliminadas.add(s);
                } catch (DaoException ex) {
                    Logger.getLogger(SolicitudService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } 
        return eliminadas;
    }
}