package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.activiti.Proceso;
import co.org.conte.sgm.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;

/**
 *
 * @author J4M0
 */
public class ProcesoDao extends BaseDao<Proceso> {

    public ProcesoDao() {
        super(Proceso.class);
    }
    
    @Override
    public List<Proceso> findAll() throws DaoException{
        
        List<Proceso> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();            
            String querySQL = "	SELECT art.PROC_INST_ID_ as codigo, ahd.text_ AS documento, arv.text_ AS carpeta, art.DESCRIPTION_ AS titulo, art.name_ AS tarea "
                    + "FROM alfresco.`ACT_RU_TASK` art "
                    + "INNER JOIN alfresco.`ACT_RU_VARIABLE` arv  "
                    + "ON art.PROC_INST_ID_ = arv.PROC_INST_ID_ "
                    + "INNER JOIN alfresco.`ACT_HI_DETAIL` ahd "
                    + "ON art.PROC_INST_ID_ = ahd.PROC_INST_ID_ "
                    + "WHERE arv.name_ = 'nombrecarpeta' "
                    + "AND ahd.name_ = 'qswfr_numerodocumento' GROUP BY 2 ";
	
            Query query = session.createSQLQuery( querySQL ).setResultTransformer(Transformers.aliasToBean(Proceso.class));				
            retValue = query.list();
            transaction.commit();
            
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando el listado proceso");
            throw new DaoException("Error inesperado buscando el listado todos los resultados de ", e);
		
        }
	return retValue;
    }
        
}