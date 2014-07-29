/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.FormatoActividad;
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
public class FormatoActividadDao extends BaseDao<FormatoActividad> {

    public FormatoActividadDao() {
        super(FormatoActividad.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<FormatoActividad> findByIdFormato(FormatoActividad entity) throws DaoException{
		List<FormatoActividad> all = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from " + FormatoActividad.class.getName()+ " fa where fa.id.formatId = " + entity.getId().getFormatId());
			all = query.list();						
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado obteniendo el listado "+FormatoActividad.class.getSimpleName()+"", e);
			throw new DaoException("Error inesperado obteniendo el listado todos los resultados de "+FormatoActividad.class.getSimpleName()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}		
		}				
		return all;	
	}	
    
    
    
}
