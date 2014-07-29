/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conte.db;

import java.util.logging.Level;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Roger Padilla C.
 */
public class DataSourceUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public DataSource obtainDataSource() {

        InitialContext ic = null;
        DataSource dataSource = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dsFile = "java:comp/env/jdbc/ConteDS";

            ic = new InitialContext();
            dataSource = (DataSource) ic.lookup(dsFile);
        } catch (NamingException e) {
            logger.debug(e.getExplanation(), e);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataSourceUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ic.close();
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
        }

        return dataSource;
    }

    public static DataSourceUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {

        public static final DataSourceUtil INSTANCE = new DataSourceUtil();
    }
}
