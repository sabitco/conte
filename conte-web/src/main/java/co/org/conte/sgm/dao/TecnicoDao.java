package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author J4M0
 */
public class TecnicoDao extends BaseDao<Tecnico> {
         
    public TecnicoDao() {
        super(Tecnico.class);
    }
    
    @SuppressWarnings("empty-statement")
    public Tecnico findByDocumento(String documento) {
        List<Tecnico> retValue;
        Tecnico tecnico = null;
        Session session;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Tecnico.class.getName()+" a where a.documento = :documento");
            query.setParameter("documento", documento );           
            retValue = (List<Tecnico>)query.list();
            if(retValue.size()>0){
                tecnico = retValue.get(0);
            }            
            transaction.commit();
        } catch( HibernateException e  ){			
            tecnico = null;
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Tecnico.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return tecnico; 
    }
    
    @SuppressWarnings("empty-statement")
    public Tecnico findByIdSolicitud( Serializable idSolicitud ) throws DaoException{
		Tecnico entity = null;
		Session session;
		Transaction transaction = null;
		try{
                    session = HibernateUtil.getSessionFactory().getCurrentSession();
                    transaction = session.beginTransaction();		                    
                    SQLQuery query = session.createSQLQuery("SELECT t.* FROM tecnico t INNER JOIN solicitud s ON s.c_tecnico = t.codigo where s.radicado = :idSolicitud");
                    query.addEntity(Tecnico.class);
                    query.setParameter("idSolicitud",idSolicitud );
                    List<Tecnico> retValue = query.list();
                    if(retValue.size()>0){
                        entity = retValue.get(0);
                    }                   
                        
			transaction.commit();
		}
		catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
			this.log.error("Error inesperado encontrando "+Tecnico.class.getSimpleName()+"", e);
			throw new DaoException("Error inesperado encontrando "+entity.getClass()+"", e);
		}
		finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
		}				
		return entity; 
	}
}