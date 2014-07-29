/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Pqrs;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Andrés Mise Olivera
 */
public class PqrsDao extends BaseDao<Pqrs> {

    public PqrsDao() {
        super(Pqrs.class);
    }

    @Override
    public Pqrs findById(Serializable idEntity) throws DaoException {
        Pqrs entity = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();			
            entity = (Pqrs)session.get( Pqrs.class, idEntity);		
            if(entity.getPqrsDescripciones()!=null){
                entity.getPqrsDescripciones().size();                
            }
            transaction.commit();
        } catch( HibernateException e  ){	            
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado encontrando "+ Pqrs.class.getSimpleName()+"", e);
            throw new DaoException("Error inesperado encontrando "+entity.getClass()+"", e);
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }				
        return entity; 
    }
}