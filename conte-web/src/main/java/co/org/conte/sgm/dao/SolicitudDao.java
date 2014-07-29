/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.util.HibernateUtil;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

/**
 *
 * @author jam
 */
public class SolicitudDao extends BaseDao<Solicitud> {
         
    public SolicitudDao() {
        super(Solicitud.class);
    }
    
    public List<BigInteger> findByDocument(String document) {
        List<BigInteger> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            System.out.println("SE INGRESA AL METODO DE BUSCA DE SOLICITUDES ");
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT a.`radicado` FROM solicitud a INNER JOIN tecnico t ON a.`c_tecnico` = t.`codigo` "
                    + "WHERE t.documento = '"+document+"' AND (a.`c_estado` NOT IN (7, 17) OR ISNULL(a.`c_estado`)) ORDER BY 1 DESC");
            System.out.println("\n\nEL QUERY ES ->"+query.getQueryString());
            //query.setParameter("documento", document );                
            retValue = (List<BigInteger>)query.list();   
            
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
    
    public BigInteger findByTecnicoTipoSolicitud(String tecnico, String tipoSolicitud) {
        List retValue = null;
        BigInteger solicitud = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            
            String querySQL = "SELECT count(*) from solicitud where c_tecnico ="+ tecnico +" AND c_tipo ="+tipoSolicitud;
            
            retValue = session.createSQLQuery( querySQL ).list();//setResultTransformer(Transformers.aliasToBean( Solicitud.class ));	
    
            //retValue = (List<Solicitud>)query.list();
            if(retValue.size()>0){
                solicitud = (BigInteger)retValue.get(0);
            }            
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return solicitud; 
    }

    public List<Solicitud> findByInactividad(Date date) {
        List<Solicitud> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Solicitud.class.getName()+" a where a.ultimaModificacion <= :fecha and a.estado.codigo = 1");
            query.setDate("fecha", date );                
            retValue = (List<Solicitud>)query.list();
                   
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
    
    public List<Solicitud> findByEstado(int i) {
        List<Solicitud> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Solicitud.class.getName()+" a where a.estado.codigo = :estado"); //msr

            query.setInteger("estado", i );                
            query.setMaxResults(10);
            retValue = (List<Solicitud>)query.list();
                   
            transaction.commit();
        } catch( HibernateException e  ){//msr			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
    
    public Long getRadicadoByDocumento(String documento) {
        List retValue = null;
        Long radicado = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            
            String querySQL = "SELECT radicado from solicitud_radicado where documento = '"+ documento +"'";
            
            retValue = session.createSQLQuery( querySQL ).list();//setResultTransformer(Transformers.aliasToBean( Solicitud.class ));	
    
            //retValue = (List<Solicitud>)query.list();
            if(retValue.size()>0){
                radicado = (Long)retValue.get(0);
            }            
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return radicado; 
    }
    
    public List<Solicitud> findBySolicitud(String i) {
        List<Solicitud> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Solicitud.class.getName()+" a where a.radicado = :estado");
            query.setString("estado", i );                
            retValue = (List<Solicitud>)query.list();
                   
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
    
    public List<BigInteger> findByRadicado(String i) {
        List<BigInteger> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT s.radicado FROM solicitud s INNER JOIN solicitud_radicado t ON t.solicitud_id = s.radicado WHERE t.radicado = " + i);               
            retValue = (List<BigInteger>)query.list();
            System.out.println(retValue.size());
                   
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
    
    public List<BigInteger> findByDocumento(String i) {
        List<BigInteger> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createSQLQuery("SELECT max(s.radicado) FROM solicitud s INNER JOIN tecnico t ON t.codigo = s.c_tecnico WHERE t.documento = " + i);               
            retValue = (List<BigInteger>)query.list();
            System.out.println(retValue.size());
                   
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Solicitud.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }

    public static void main(String args[]){
        SolicitudDao dao = new SolicitudDao();
        dao.findByDocument("1032401047");
    }
    
}
