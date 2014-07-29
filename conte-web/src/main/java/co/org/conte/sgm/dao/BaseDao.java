package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.exception.ConstraintViolationException;

public class BaseDao<T extends Serializable> implements Serializable{

	protected Log log;
	private Class<T> clase;
	
	public BaseDao(Class<T> clase){
		this.log = LogFactory.getLog( this.getClass() );
		this.clase = clase;
	}
	
	public T insert(T entity) throws DaoException{			
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.persist( entity );
			transaction.commit();
		} catch (ConstraintViolationException cve){
			if(transaction != null ){
				transaction.rollback();
			}
			this.log.error("Error ocurrrido por llave duplicada, insertando en "+ entity.getClass()+"", cve);
			throw new DaoException("Error ocurrido por datos duplicados en "+entity.getClass()+"", cve);
		}
		
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
                        this.log.error("Error inesperado insertando "+entity.getClass()+"", e);
			throw new DaoException("Error inesperado insertando "+entity.getClass().getName()+" "+ e.getCause(), e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
		}				
		return entity; 			
	}
	
	public T update(T entity) throws DaoException{			
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			
                        transaction = session.beginTransaction();
		
			session.merge( entity );
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
                        this.log.error("Error inesperado actualizando "+entity.getClass()+"", e);
			throw new DaoException("Error inesperado actualizando "+entity.getClass()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}				
		}				
		return entity; 			
	}
	
	public T delete(T entity) throws DaoException{			
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.delete( entity );		
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}	
                        this.log.error("Error inesperado eliminando "+entity.getClass()+"", e);
			throw new DaoException("Error inesperado eliminando "+entity.getClass()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}			
		}				
		return entity; 			
	}
	
	@SuppressWarnings("unchecked")
	public T findById( Serializable idEntity ) throws DaoException{
		T entity = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();			
			entity = (T)session.get( this.clase, idEntity);				
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado encontrando "+this.clase.getSimpleName()+"", e);
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
	
	@SuppressWarnings("unchecked")
	public List<T> findBy( T searchEntity ) throws DaoException{
		List<T> retValue = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();	
			retValue = session.createCriteria( this.clase ).add( Example.create( searchEntity ) ).list();						
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado obteniendo el listado "+this.clase.getSimpleName()+"", e);
			throw new DaoException("Error inesperado obteniendo el listado "+this.clase.getSimpleName()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
		}				
		return retValue; 
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() throws DaoException{
		List<T> all = null;
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("from " + clase.getName());
			all = query.list();						
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado obteniendo el listado "+this.clase.getSimpleName()+"", e);
			throw new DaoException("Error inesperado obteniendo el listado todos los resultados de "+this.clase.getSimpleName()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}		
		}				
		return all;	
	}	
}   