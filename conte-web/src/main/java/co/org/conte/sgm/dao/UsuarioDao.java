package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.LogUsuario;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.util.HibernateUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author jam
 */
public class UsuarioDao extends BaseDao<Usuario> {
    
    public UsuarioDao() {
        super(Usuario.class);
    }

    public Usuario findByDocumento(String tipoDocumento, String documento) {
        List<Usuario> retValue = null;
        Usuario usuario = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Usuario.class.getName()+" a where a.documento = :documento and a.tipoDocumento = :tipoDocumento");
            query.setParameter("documento", documento );
            query.setParameter("tipoDocumento", tipoDocumento );            
            retValue = (List<Usuario>)query.list();
            if(retValue.size()>0){
                usuario = retValue.get(0);
            }            
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
        return usuario; 
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Usuario findById( Serializable idEntity ) throws DaoException{
        Usuario entity = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();			
            entity = (Usuario)session.get( Usuario.class, idEntity);		
            if(entity.getSolicituds()!=null){
                entity.getSolicituds().size();                
            }
            transaction.commit();
        } catch( HibernateException e  ){	            
            if(transaction != null ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
            this.log.error("Error inesperado encontrando "+ Usuario.class.getSimpleName()+"", e);
            throw new DaoException("Error inesperado encontrando "+entity.getClass()+"", e);
        } finally {
            if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ){
                try{transaction.rollback();}catch(HibernateException e1){;}
            }
        }				
        return entity; 
    }

    public Usuario login(Usuario usuario) {
        List<Usuario> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            String password = usuario.getContrasena();
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Usuario.class.getName()+" a where a.documento = :documento" );
            query.setParameter("documento", usuario.getDocumento() );                        
            retValue = (List<Usuario>)query.list();
            if(retValue.size()>0){
                for(Usuario u : retValue){                 
                    usuario = u;
                }
                if(usuario.getContrasena().equals(password)){
                    usuario.setIntentos(0);
                    usuario.setUltimoAcceso(new Date());
                    session.merge( usuario );                    
                } else {
                    if(usuario.getIntentos()!=null){
                        usuario.setIntentos(usuario.getIntentos()+1);
                        if(usuario.getIntentos()>5){
                            usuario.setEstado("Bloqueado");
                        }
                    } else {
                        usuario.setIntentos(1);
                    }
                    session.merge( usuario );
                    if(usuario.getEstado()!=null){
                        if(!usuario.getEstado().equals("Bloqueado")){
                            usuario = null;
                        }
                    } else {
                        usuario = null;
                    }
                } 
                    
            } else {
                usuario = null;
            }
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
        return usuario; 
    }
    
    public List<Usuario> findByInactivity(Date fecha) {
        List<Usuario> retValue = null;
        Usuario usuario = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();
            Query query = session.createQuery("from " + Usuario.class.getName()+" a where a.ultimoAcceso <= :fecha and (a.perfil.codigo = 1 or a.perfil.codigo = 2)"
                    + " and estado != 'Inactivo'");
            query.setDate("fecha", fecha );                
            retValue = (List<Usuario>)query.list();
                   
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
    
    @Override
    public Usuario insert(Usuario entity) throws DaoException{			
		Session session = null;
		Transaction transaction = null;
		try{       
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			transaction = session.beginTransaction();
			session.persist( entity );
                        
                        LogUsuario logUser = new LogUsuario();
                    logUser.setAccion("agrego");
                    logUser.setCodUsuarioModificador(1);
                    logUser.setDescripcion("Se agrega un nuevo usuario");
                    logUser.setFecha(new Date());
                    logUser.setIp(null);
                    logUser.setUsuario(new Usuario(1, null, null, null, null, null));
                    logUser.setUsuarioModificado(entity.getDocumento());
                    logUser.setUsuarioModificador("system");
                    session.persist( logUser );
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
}