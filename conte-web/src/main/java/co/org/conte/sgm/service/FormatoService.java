package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.ClaseGenericaDao;
import co.org.conte.sgm.dao.FormatoActividadDao;
import co.org.conte.sgm.dao.FormatoEstudioDao;
import co.org.conte.sgm.dao.InstitucionEducativaDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.ClaseGenerica;
import co.org.conte.sgm.entity.FormatoActividad;
import co.org.conte.sgm.entity.FormatoActividadId;
import co.org.conte.sgm.entity.FormatoEstudio;
import co.org.conte.sgm.entity.InstitucionEducativa;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class FormatoService {
    
    private ClaseGenericaDao daoClaseGenerica;
    private FormatoActividadDao daoFormatoActividad;
    private InstitucionEducativaDao daoInstitucionEducativa;

    public FormatoService() {
        daoClaseGenerica = new ClaseGenericaDao();
        daoFormatoActividad = new FormatoActividadDao();
        daoInstitucionEducativa = new InstitucionEducativaDao();
    }
    
    public List<ClaseGenerica> getAllClases(){
        try {
            return daoClaseGenerica.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(FormatoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<InstitucionEducativa> getAllInstituciones(){
        try {
            return daoInstitucionEducativa.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(FormatoService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<FormatoActividad> getFormatoActividadByFormatoEstudio(FormatoEstudio formatoEstudio){
        List<FormatoActividad> fas = null;
        FormatoActividad formatoActividad = new FormatoActividad();
        formatoActividad.setFormatoEstudio(formatoEstudio);
        FormatoActividadId id = new FormatoActividadId();
        id.setFormatId(formatoEstudio.getCodigo());
        formatoActividad.setId(id);
        try {
            fas = daoFormatoActividad.findByIdFormato(formatoActividad);
        } catch (DaoException ex){
            ex.printStackTrace();
        }
        
        
        return fas;
    }   
}