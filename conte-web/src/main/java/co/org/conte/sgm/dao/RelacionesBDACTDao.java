package co.org.conte.sgm.dao;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.RelacionesBDACT;
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
public class RelacionesBDACTDao extends BaseDao<RelacionesBDACT> {

    public RelacionesBDACTDao() {
        super(RelacionesBDACT.class);
    }
    
    @Override
    public List<RelacionesBDACT> findAll() throws DaoException{
        
        List<RelacionesBDACT> retValue = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();            
            String querySQL = "SELECT codigo as codigo, label as label, campo_db as campoDb, "
                    + "campo_act as campoAct, tipo as tipo, llave_tabla_db as llaveTabla, campo_db_solicitud as campoSolicitud, "
                    + "tipo_relacion_solicitud as tipoRelacion, tabla_db as tabla, tabla_act as tablaAct from relaciones_bd_act";
	
            Query query = session.createSQLQuery( querySQL ).setResultTransformer(Transformers.aliasToBean(RelacionesBDACT.class));				
            retValue = query.list();
            transaction.commit();
            
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando los campos editables");
            throw new DaoException("Error inesperado buscando los campos editables", e);
		
        }
	return retValue;
    }
    
    private String getCodigo(String relacion, String solicitud){
         Session session = null;
        Transaction transaction = null;
        String codigo = "";
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();            
            String querySQL = "";
            Query query;
            querySQL = "select " + relacion + " from solicitud where radicado = " + solicitud;             
                query = session.createSQLQuery( querySQL );
                Object valor = query.uniqueResult();
                codigo = valor.toString(); transaction.commit();
             } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando el campo");		
        }
        return codigo;
        
    }
    
    public int actualizarCampos(RelacionesBDACT relacion, String solicitud, String proceso){
        int resultado=-1;
        Session session = null;
        Transaction transaction = null;
        String codigo = "";
        String nuevovalor = String.valueOf(relacion.getNuevoValor()).trim();
        
        System.out.println("tipo de campo: " + relacion.getTipo());
        
        try{
            String querySQL = "";
            String[] tablas = relacion.getTablaAct().split(",");
            String[] campos = relacion.getCampoAct().split(",");
            
            if(relacion.getTipoRelacion().trim().equalsIgnoreCase("1")){
                codigo =getCodigo(relacion.getCampoSolicitud() , solicitud);
                querySQL = "update " + relacion.getTabla() + " "
                        + "set " + relacion.getCampoDb() 
                        + (nuevovalor.length()>0 ? " = '" + relacion.getNuevoValor() + "' " : " = null ")
                        + "where " + relacion.getLlaveTabla() + " = "+ codigo;
                resultado = executeQuery(querySQL);
            } else if(relacion.getTipoRelacion().trim().equalsIgnoreCase("2")) {
                codigo =getCodigo(relacion.getCampoSolicitud() , solicitud);
                querySQL = "update " + relacion.getTabla() + " "
                        + "set " + relacion.getCampoDb() 
                        + (nuevovalor.length()>0 ? " = '" + relacion.getNuevoValor() + "' " : " = null ")
                        + "where " + relacion.getLlaveTabla() + " = "+ codigo;
                resultado = executeQuery(querySQL);
            } else if(relacion.getTipoRelacion().trim().equalsIgnoreCase("4")) {
                codigo =getCodigo(relacion.getCampoSolicitud() , solicitud);
                querySQL = "update " + relacion.getTabla() + " "
                        + "set " + relacion.getCampoDb() 
                        + (nuevovalor.length()>0 ? " = '" + relacion.getNuevoValor()  + "' " : " = null ")
                        + "where " + relacion.getLlaveTabla() + " = "+ codigo;
                resultado = executeQuery(querySQL);
            }
            for(String tabla: tablas){
                for(String campo: campos){
                    querySQL = "update alfresco." + tabla + " set "
                        + (relacion.getTipo().trim().equalsIgnoreCase("String") ? "TEXT_ = '" : "LONG_ = ")
                        + (relacion.getTipo().trim().equalsIgnoreCase("String") ? (relacion.getNuevoValor() + "' ") : ( "UNIX_TIMESTAMP('" + relacion.getNuevoValor() + "') * 1000 "))
                        + " where PROC_INST_ID_ "
                        + "= " + proceso + " and name_ = '" + campo.trim() +"'";            
                    System.out.println("Query actualizacion alfresco: " + querySQL);
                    resultado = executeQuery(querySQL);
                }
            }
            //transaction.commit();
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            e.printStackTrace();	
        }
        return resultado;
    }
    
    private int executeQuery(String querySQL){
        int resultado=0;
        Session session = null;
        Transaction transaction = null;
        String codigo = "";
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();               
            Query query;
            query = session.createSQLQuery( querySQL );
            resultado = query.executeUpdate();
            transaction.commit();
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando el campo");		
        }
        return resultado;
    }
    
    public int actualizaTabla(String tabla, String campo, String valor, int tvalor, String campocondicion, String valorcondicion, int tvcondicion){
        int resultado=0;
        Session session = null;
        Transaction transaction = null;
        String querySQL = "update " + tabla 
                + " set " + campo + " = " + (tvalor==0 ? valor : "'" + valor + "'") 
                + " where " + campocondicion + " = " + (tvcondicion==0 ? valorcondicion : "'" + valorcondicion + "'");
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();               
            Query query;
            query = session.createSQLQuery( querySQL );
            resultado = query.executeUpdate();
            transaction.commit();
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando el campo");		
        }
        return resultado;
    }
    
    public Object obtenerValorCampo(RelacionesBDACT relacion, String solicitud, String proceso){
        Object resultado = null;
        Session session = null;
        Transaction transaction = null;
        try{
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            transaction = session.beginTransaction();            
            String querySQL = "";
            if(relacion.getTipoRelacion().trim().equalsIgnoreCase("1")){
                querySQL = "SELECT t." +relacion.getCampoDb() + " as campo from solicitud s inner join " + relacion.getTabla() 
                        + " t on t." + relacion.getLlaveTabla() 
                        + " = s." + relacion.getCampoSolicitud() + " where s.radicado = " + solicitud ;
            } else if(relacion.getTipoRelacion().trim().equalsIgnoreCase("2") || relacion.getTipoRelacion().trim().equalsIgnoreCase("3")){
                querySQL = "SELECT s." +relacion.getCampoDb() + " as campo from " + relacion.getTabla() 
                        + " s inner join solicitud t on t." + relacion.getCampoSolicitud() + " = s." 
                        + relacion.getLlaveTabla() + " where s.radicado = " + solicitud ;
            } else if(relacion.getTipoRelacion().trim().equalsIgnoreCase("4")) {
                querySQL = "SELECT s." +relacion.getCampoDb() + " as campo from " + relacion.getTabla() 
                        + " s inner join solicitud t on t." + relacion.getCampoSolicitud() + " = s." 
                        + relacion.getLlaveTabla() + " where t.radicado = " + solicitud ;
            } else if(relacion.getTipoRelacion().trim().equalsIgnoreCase("5")) {
                String[] tablas = relacion.getTablaAct().split(",");
                String[] campos = relacion.getCampoAct().split(",");
                querySQL = "SELECT " + (relacion.getTipo().equalsIgnoreCase("String") ? "TEXT_" : "CAST(FROM_UNIXTIME(LONG_/1000) AS DATE)") + " as campo "
                        + "from alfresco." + tablas[0] + " a "
                        + " where PROC_INST_ID_ "
                        + "= " + proceso + " and name_ = '" + campos[0].trim() + "'";
            }
            Query query = session.createSQLQuery( querySQL );
            List<Object> resultados = (List<Object>)query.list();
            for(Object r : resultados){
                resultado = r;
                System.out.println(resultado);
            }
            transaction.commit();
        } catch( HibernateException e  ){	
            if(transaction != null ){
                transaction.rollback();
            }
            this.log.error("Error inesperado buscando el campo");		
        }
        return resultado;
    }
        
}