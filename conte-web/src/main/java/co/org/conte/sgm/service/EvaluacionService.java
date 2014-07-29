package co.org.conte.sgm.service;

import co.org.conte.sgm.dao.EvaluacionDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Evaluacion;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public class EvaluacionService {
    
    private EvaluacionDao dao;

    public EvaluacionService() {
        dao = new EvaluacionDao();
    }
    
    public void save(List<Evaluacion> evaluaciones){
        for(Evaluacion e : evaluaciones){
            try {
                Evaluacion findById = dao.findById(e.getId());
                if(findById==null){
                    dao.insert(e);
                }
            } catch (DaoException ex) {
                Logger.getLogger(EvaluacionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}