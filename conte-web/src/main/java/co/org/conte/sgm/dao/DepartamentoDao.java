/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import java.util.Collection;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jam
 */
public class DepartamentoDao extends BaseDao<Departamento> {

    public DepartamentoDao() {
        super(Departamento.class);
    }
    
    @Override
    public Departamento findById(Serializable idEntity) throws DaoException {
        Departamento entity = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();			
			entity = (Departamento)session.get( Departamento.class, idEntity);	
                        entity.getCiudads().size();                        
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado encontrando "+Departamento.class.getSimpleName()+"", e);
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
