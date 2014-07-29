/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.dao;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.entity.UsuarioContraseniaHistorial;
import co.org.conte.sgm.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jam
 */
public class ContraseniasDao extends BaseDao<UsuarioContraseniaHistorial> {

    public ContraseniasDao() {
        super(UsuarioContraseniaHistorial.class);
    }    
    
    public List<UsuarioContraseniaHistorial> findByUsuario(Usuario usuario){
        List<UsuarioContraseniaHistorial> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + UsuarioContraseniaHistorial.class.getName()+" a where a.usuario.codigo = :usuario order by a.fecha desc"  );
            query.setParameter("usuario", usuario.getCodigo() );                        
            retValue = (List<UsuarioContraseniaHistorial>)query.list();          
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