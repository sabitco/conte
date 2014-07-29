/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 *
 * @author jam
 */
@SuppressWarnings("deprecation")
public class HibernateUtil {
    
    private static final SessionFactory sessionFactory;
    
    static{      
        sessionFactory = new Configuration().configure().buildSessionFactory();	
    }
    
    private HibernateUtil(){
        
    }

    public static SessionFactory getSessionFactory(){
        return HibernateUtil.sessionFactory;
    }	
    
    public static Connection getConnection(){
        Connection conn = null;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ConteDS");
            conn = ds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
//        org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider j;
//        j = new  org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider();
//        Connection conn = null;
////        String url = "jdbc:mysql://172.16.1.74:3306/";
//        String url = "jdbc:mysql://localhost:3306/";    
//        String dbName = "sgm_conte_test";
//        String driver = "com.mysql.jdbc.Driver";
//        String userName = "root"; 
//        String password = "root";
//        try {
//            Class.forName(driver).newInstance();
//            conn = (com.mysql.jdbc.Connection) DriverManager.getConnection(url+dbName,userName,password);
//        } catch (Exception e) {
//        }
//         return conn;
    }
    
}