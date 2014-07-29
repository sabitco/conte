package co.org.conte.sgm.dao;

import co.org.conte.sgm.entity.ReferenciaPersonal;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jam
 */
public class ReferenciaPersonalDao extends BaseDao<ReferenciaPersonal> {
         
    public ReferenciaPersonalDao() {
        super(ReferenciaPersonal.class);
    }  
    
    public List<ReferenciaPersonal> findBySolicitud(String i) {
        List<ReferenciaPersonal> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery("select * from referencia_personal where c_solicitud = " + i);
            //query.setString("estado", i );                
            retValue = (List<ReferenciaPersonal>)query.list();
                   
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+ReferenciaPersonal.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
}
