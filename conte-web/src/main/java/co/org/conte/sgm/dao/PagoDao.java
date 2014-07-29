/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Pago;
import co.org.conte.sgm.util.HibernateUtil;
import java.text.SimpleDateFormat;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author jam
 */
public class PagoDao extends BaseDao<Pago> {

    public PagoDao() {
        super(Pago.class);
    }

    public int save(Pago entity) throws DaoException {
        Session session = null;
        Transaction transaction = null;        
        int retValue = 0;
        try{            
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            String querySqlDoc = "SELECT MAX(radicado) FROM solicitud s "
                    + "INNER JOIN tecnico t ON t.codigo = s.c_tecnico "
                    + "WHERE t.documento = '"+entity.getDocumento()+"' and !ISNULL(s.fecha_creacion)";
            Query queryDoc = session.createSQLQuery(querySqlDoc);
            java.math.BigInteger solicitud = (java.math.BigInteger)queryDoc.list().get(0);
            if(solicitud == null ){
                retValue = 0;
            } else {
                String querySQL = "INSERT INTO pago VALUES"
                    + "(NULL,"
                    + " (SELECT MAX(radicado) FROM solicitud s "
                    + "INNER JOIN tecnico t ON t.codigo = s.c_tecnico "
                    + "WHERE t.documento = '"+entity.getDocumento()+"'),"
                    + "'"+entity.getDocumento()+"','"+new SimpleDateFormat("yyyy-MM-dd").format(entity.getFechaConsignacion())+"',CURDATE(),"+entity.getValor()+");";  
                Query query = session.createSQLQuery(querySQL);//.executeUpdate();//setResultTransformer(Transformers.aliasToBean(Proceso.class));				
                retValue = query.executeUpdate();   
            }
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
        return retValue; 	        
    }   
    
        public int save2(Pago entity) throws DaoException {
        Session session = null;
        Transaction transaction = null;        
        int retValue = 0;
        try{            
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            String querySQL = "INSERT INTO pago_ VALUES"
                    + "(NULL," + entity.getValor() + ",'" + entity.getDocumento() 
                    + "','" + new SimpleDateFormat("yyyy-MM-dd").format(entity.getFechaConsignacion())
                    + "', curdate())";
//                    + "(SELECT MAX(radicado) FROM solicitud s "
//                    + "INNER JOIN tecnico t ON t.codigo = s.c_tecnico "
//                    + "WHERE t.documento = '"+entity.getDocumento()+"'),"
//                    + "'"+entity.getDocumento()+"','"+"',CURDATE(),"+entity.getValor()+");";  
            Query query = session.createSQLQuery(querySQL);//.executeUpdate();//setResultTransformer(Transformers.aliasToBean(Proceso.class));				
            
            retValue = query.executeUpdate();
            
            System.out.println("\n\n\n\nRETVAL ES =>" + retValue);
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
        return retValue; 	        
    }   
        
        public int save(String solicitud, String fecha, String valor, String documento)
                throws DaoException {
        Session session = null;
        Transaction transaction = null;        
        int retValue = 0;
        try{            
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
           String querySQL = "INSERT INTO pago VALUES"
                    + "(NULL,"
                    + solicitud + ","
                    + "'"+documento+"','"+fecha+"',CURDATE(),"+valor+");";
            Query query = session.createSQLQuery(querySQL);//.executeUpdate();//setResultTransformer(Transformers.aliasToBean(Proceso.class));				
            
            retValue = query.executeUpdate();
            
            System.out.println("\n\n\n\nRETVAL ES =>" + retValue);
            transaction.commit();
        } catch (ConstraintViolationException cve){
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error ocurrrido por llave duplicada, insertando en ", cve);
            throw new DaoException("Error ocurrido por datos duplicados en ", cve);
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado insertando ", e);
            throw new DaoException("Error inesperado insertando "+ e.getCause(), e);
        } finally{
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }
        return retValue; 	        
    }   
}