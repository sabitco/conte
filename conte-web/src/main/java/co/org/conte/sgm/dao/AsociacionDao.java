/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Asociacion;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jam
 */
public class AsociacionDao extends BaseDao<Asociacion> {

    public AsociacionDao() {
        super(Asociacion.class);
    }

    @Override
    public Asociacion findById(Serializable idEntity) throws DaoException {
        Asociacion entity = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();			
			entity = (Asociacion)session.get( Asociacion.class, idEntity);	
                        entity.getInspectors().size();
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado encontrando "+Asociacion.class.getSimpleName()+"", e);
			throw new DaoException("Error inesperado encontrando "+entity.getClass()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			//cerrarSession(session);
		}				
		return entity; 
    }
    
    
    
    
    
}
