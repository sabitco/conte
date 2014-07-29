/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.entity.Evaluacion;
import co.org.conte.sgm.entity.Parametro;
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
public class ParametroDao extends BaseDao<Parametro> {

    public ParametroDao() {
        super(Parametro.class);
    }
    
    public String getParameter(String name){
        String valor = null;
        List retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            
            String querySQL = "SELECT valor from parametro where nombre = '"+ name +"'";
            
            retValue = session.createSQLQuery( querySQL ).list();//setResultTransformer(Transformers.aliasToBean( Solicitud.class ));	
    
            //retValue = (List<Solicitud>)query.list();
            if(retValue.size()>0){
                valor = (String)retValue.get(0);
            }            
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Evaluacion.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return valor; 
        
    }
    
    
}
