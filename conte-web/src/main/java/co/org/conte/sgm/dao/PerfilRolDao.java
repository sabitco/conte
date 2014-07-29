/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.entity.Perfil;
import co.org.conte.sgm.entity.PerfilRol;
import co.org.conte.sgm.entity.Usuario;
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
public class PerfilRolDao extends BaseDao<PerfilRol> {

    public PerfilRolDao() {
        super(PerfilRol.class);
    }    
    
    public List<PerfilRol> findByPerfil(Perfil perfil) {
        List<PerfilRol> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + PerfilRol.class.getName()+" a where a.id.CPerfil = :perfil");
            query.setParameter("perfil", perfil.getCodigo() );            
            retValue = (List<PerfilRol>)query.list();                       
            transaction.commit();
        } catch( HibernateException e  ){			
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado obteniendo el listado "+Usuario.class.getSimpleName()+"", e);
		
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }	
        return retValue; 
    }
}
