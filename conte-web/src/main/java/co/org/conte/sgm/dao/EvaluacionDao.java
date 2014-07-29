package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Evaluacion;
import co.org.conte.sgm.util.HibernateUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jam
 */
public class EvaluacionDao extends BaseDao<Evaluacion> {

    public EvaluacionDao() {
        super(Evaluacion.class);
    }
    
    public void save(List<Evaluacion> evaluaciones){
        for(Evaluacion e : evaluaciones){
            try {
                insert(e);
            } catch (DaoException ex) {
                Logger.getLogger(EvaluacionDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    public List<Evaluacion>  findByDocument(String document){
        List<Evaluacion> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Evaluacion.class.getName()+" a where a.documento = :documento");
            query.setParameter("documento", document );                
            retValue = (List<Evaluacion>)query.list();               
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Evaluacion.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }   
}