/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conte.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JDBCAdapter {

  private DataSource dataSource;
  private Connection con;
  private String[] columnNames = {};
  private Class[] columnTpyes = {};
  private Vector rows = new Vector();
  private ResultSetMetaData resultSetMetaData = null;
  int[] Presicion = {};
  private Log log;

  public JDBCAdapter() {
    this.log = LogFactory.getLog(this.getClass());
    dataSource = DataSourceUtil.getInstance().obtainDataSource();
    initConnection();
  }

  /**
   * @param query Cadena que contiene el query que se va a ejecutar.
   * @return boolean que indica si se pudo ejecutar el update.
   *
   */
  public boolean query(String query) {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    try {
      connection = this.getConnection();
      statement = connection.createStatement();
      resultSet = statement.executeQuery(query);
      resultSetMetaData = resultSet.getMetaData();

      int numberOfColumns = resultSetMetaData.getColumnCount();
      columnNames = new String[numberOfColumns];
      Presicion = new int[numberOfColumns];
      // Get the column names and cache them.
      // Then we can close the connection.
      for (int column = 0; column < numberOfColumns; column++) {
        columnNames[column] = resultSetMetaData.getColumnLabel(column + 1);
        Presicion[column] = resultSetMetaData.getPrecision(column + 1);
      }

      // Get all rows.
      rows = new Vector();
      while (resultSet.next()) {
        Vector newRow = new Vector();
        for (int i = 1; i <= getColumnCount(); i++) {
          int type;
          try {
            type = resultSetMetaData.getColumnType(i);
          } catch (SQLException e) {
            type = Types.VARCHAR;
          }
          Object objeto = resultSet.getObject(i);
          switch (type) {
            case Types.DATE:
              newRow.addElement(voltearFecha(objeto.toString()));
              break;
            default:
              newRow.addElement(objeto);
          }
        }
        rows.addElement(newRow);
      }
      return true;
    } catch (SQLException e) {
      this.log.error("Error ejecutando el query", e);
      return false;
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
        }
      }
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  /**
   * @param query Cadena que contiene el update que se va a ejecutar.
   * @return Boolean indicando si se ejectuó exitosamente el update.
   *
   */
  public boolean update(String query) {
    Connection connection = null;
    Statement statement = null;
    try {
      connection = this.getConnection();
      statement = connection.createStatement();
      statement.executeUpdate(query);
      return true;
    } catch (SQLException e) {
      this.log.error("Error ejecutando el query", e);
      return false;
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  //
  // Implementation of the TableModel Interface
  // MetaData
  public String getColumnName(int column) {
    if (columnNames[column] != null) {
      return columnNames[column];
    } else {
      return "";
    }
  }

  public Class getColumnClass(int column) {
    int type;
    try {
      type = resultSetMetaData.getColumnType(column + 1);
    } catch (SQLException e) {
      return null;

    }

    switch (type) {
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
        return String.class;

      case Types.BIT:
        return Boolean.class;

      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
        return Integer.class;

      case Types.BIGINT:
        return Long.class;

      case Types.FLOAT:
        return Float.class;

      case Types.DOUBLE:
        return Float.class;

      case Types.DATE:
        return String.class;

      default:
        return Object.class;
    }

  }

  public boolean isCellEditable(int row, int column) {
    try {
      return resultSetMetaData.isWritable(column + 1);
    } catch (SQLException e) {
      return false;
    }
  }

  public int getColumnCount() {
    return columnNames.length;
  }

  public int getPresicion(int colum) {
    return this.Presicion[colum];
  }
  // Data methods

  public int getRowCount() {
    return rows.size();
  }

  public Vector getRow(int aRow) {
    return (Vector) rows.elementAt(aRow);
  }

  public Object getValueAt(int aRow, int aColumn) {
    Vector row = (Vector) rows.elementAt(aRow);
    return row.elementAt(aColumn);
  }

  public String dbRepresentation(int column, Object value) {
    int type;

    if (value == null) {
      return "null";
    }

    try {
      type = resultSetMetaData.getColumnType(column + 1);
    } catch (SQLException e) {
      return value.toString();
    }

    switch (type) {
      case Types.INTEGER:
      case Types.DOUBLE:
      case Types.FLOAT:
        return value.toString();
      case Types.BIT:
        return ((Boolean) value).booleanValue() ? "1" : "0";
      case Types.DATE:
        return value.toString(); // This will need some conversion.
      default:
        return "\"" + value.toString() + "\"";
    }
  }

  public void setValueAt(Object value, int row, int column) {
    try {
      String tableName = resultSetMetaData.getTableName(column + 1);
      // Some of the drivers seem buggy, tableName should not be null.
      if (tableName == null) {
        System.out.println("Table name returned null.");
      }
      String columnName = getColumnName(column);
      String query = "update " + tableName + " set " + columnName + " = "
              + dbRepresentation(column, value) + " where ";

      for (int col = 0; col < getColumnCount(); col++) {
        String colName = getColumnName(col);
        if (colName.equals("")) {
          continue;
        }
        if (col != 0) {
          query = query + " and ";
        }
        query = query + colName + " = "
                + dbRepresentation(col, getValueAt(row, col));
      }
      System.out.println(query);
    } catch (SQLException e) {
      System.err.println("Update failed");
      e.printStackTrace();
    }
    Vector dataRow = (Vector) rows.elementAt(row);
    dataRow.setElementAt(value, column);
  }

  public String voltearFecha(String str) {
    String dia = str.substring(8, 10);
    if (new Integer(dia).intValue() >= 10) {
      dia = "" + ((new Integer(dia).intValue()));
    } else {
      dia = "0" + ((new Integer(dia).intValue()));
    }
    return str.substring(0, 4) + "-" + str.substring(5, 7) + "-" + dia;
  }

  public Connection getConnection() {
    if (con != null) {
      try {
        if (con.isClosed()) {
          initConnection();
        }
      } catch (SQLException ex) {
        Logger.getLogger(JDBCAdapter.class.getName()).log(Level.SEVERE, "Error verificando e iniclaizando conexion", ex);
      }
    } else {
      initConnection();
    }
    return con;
  }

  private void initConnection() {
    try {
      con = DataSourceUtil.getInstance().obtainDataSource().getConnection();
    } catch (SQLException ex) {
      Logger.getLogger(JDBCAdapter.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void cerrarConexiones() {
    try {
      if (con != null) {
        con.close();
        con = null;
      }
    } catch (SQLException ex) {
      Logger.getLogger(JDBCAdapter.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  @Override
  public void finalize() throws Throwable {
    cerrarConexiones();
    super.finalize();
  }
}