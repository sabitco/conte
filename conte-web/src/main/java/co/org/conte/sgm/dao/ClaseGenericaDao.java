/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.ClaseGenerica;
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
public class ClaseGenericaDao extends BaseDao<ClaseGenerica> {

    public ClaseGenericaDao() {
        super(ClaseGenerica.class);
    }
    
    @Override
    public List<ClaseGenerica> findAll() throws DaoException{
		List<ClaseGenerica> all = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from " + ClaseGenerica.class.getName() + " order by nombre");
			all = query.list();						
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado obteniendo el listado "+ClaseGenerica.class.getSimpleName()+"", e);
			throw new DaoException("Error inesperado obteniendo el listado todos los resultados de "+ClaseGenerica.class.getSimpleName()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}		
		}				
		return all;	
	}	
    
}
