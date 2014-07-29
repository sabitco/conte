/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.FormatoActividad;
import co.org.conte.sgm.entity.FormatoEstudio;
import co.org.conte.sgm.util.HibernateUtil;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author jam
 */
public class FormatoEstudioDao extends BaseDao<FormatoEstudio> {
    
    public FormatoEstudioDao() {        
        super(FormatoEstudio.class);
    }
    
    @Override
    @SuppressWarnings("empty-statement")
    public FormatoEstudio insert(FormatoEstudio entity) throws DaoException{			
		Session session = null;
		Transaction transaction = null;
		try{
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.persist( entity );
                        System.out.print("Se inserto con el codigo "+entity.getCodigo() + " contiene " + entity.getFormatoActividades().size() +" formatos");
                        Set<FormatoActividad> fas = new HashSet<FormatoActividad>();
                        for(FormatoActividad fa : entity.getFormatoActividades()){
                            System.out.println("Ola k ase;: " + String.valueOf(fa));
                            fa.getId().setFormatId(entity.getCodigo());
                            fa.setFormatoEstudio(entity);
                            fas.add(fa);
                        }
                        entity.setFormatoActividades(fas);
                        session.merge(entity);
                        
			transaction.commit();
		} catch (ConstraintViolationException cve){
			if(transaction != null ){
				transaction.rollback();
			}
			this.log.error("Error ocurrrido por llave duplicada, insertando en "+ entity.getClass()+"", cve);
			throw new DaoException("Error ocurrido por datos duplicados en "+entity.getClass()+"", cve);
		} catch( HibernateException e  ){			
			if(transaction != null ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
                        this.log.error("Error inesperado insertando "+entity.getClass()+"", e);
			throw new DaoException("Error inesperado insertando "+entity.getClass().getName()+" "+ e.getCause(), e);
		} finally{
			if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
				try{transaction.rollback();}catch(HibernateException e1){;}
			}
		}				
		return entity; 			
	}

    @Override
    public FormatoEstudio update(FormatoEstudio entity) throws DaoException {
        System.out.println("Vamos a realizar la actualizacion del formato de calificacion.");
        Session session = null;
        Transaction transaction = null;
        try{
            System.out.print("Iniciamos");
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            
           // System.out.print("EL TAMAÑO ES ===> "+entity.getFormatoActividades().size());
            transaction = session.beginTransaction();
            System.out.print("Continuamos");
            String hql = "delete from " + FormatoActividad.class.getName()+" a where a.formatoEstudio.codigo = " + entity.getCodigo();
            Query query = session.createQuery(hql);
            query.executeUpdate();
            
            session.merge( entity );
            transaction.commit();
        } catch( HibernateException e  ){
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado actualizando "+entity.getClass()+"", e);
            throw new DaoException("Error inesperado actualizando "+entity.getClass()+"", e);
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }				
        }				
        return entity; 	
    }
}