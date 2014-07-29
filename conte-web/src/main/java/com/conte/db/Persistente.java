/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.conte.db;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLASS: Persistente. <br> OBJETIVO: Clase abstracta que contiene los metodos necesarios para la implementacion de las clases parsistentes y que se
 * comunican al exterior con XML
 *
 * Author: Jorge Mario Calvo //- Date: Ago 1999 //- Copyright: Maybe used for non-commercial use. //-
 */
public abstract class Persistente {

  protected JDBCAdapter tabla;
//  private Connection connectiopn;
  Persistente persistente;
  String atributos[];
  int precision[];
  // Variables adicionadas por Ivan
  ResultSetMetaData metaData;
  // cambio ET
  public String alias[];
  public String tipos[];
  public int presentacion[];
  public int longitud[];
  //
  public Vector tiposRadioMaestro = new Vector();
  String nombreTabla;
  int elementosLLave;
  private PreparedStatement preparedStatement;
  public ResultSet rs;
  private Statement registro; // Jorge ArbelÃ¡ez
  public String mensajeRegistro = "-";
  public Vector aliasV = new Vector();
  public Vector urlV = new Vector();
  private String fecParametro;
  String usuarioP = "";
  String paginaP = "";
  String ipP = "";
  private String queryInsertar;
  private String queryActualizar;

  /**
   * ***************************** CIRCULAR 052 **********************************
   */
  public void cargaSesionJMI(HttpServletRequest request) {
    if (request != null) {
      HttpSession Session_Usuario = request.getSession();
      log.debug("usuario-request->" + Session_Usuario.getAttribute("usuario"));
      log.debug("ip-request->" + request.getRemoteAddr());
      log.debug("pagina-request->" + request.getParameter("Pagina"));
      log.debug("Opcion-request->" + request.getParameter("Opcion"));

      if (Session_Usuario.getAttribute("usuario") != null) {
        usuarioP = Session_Usuario.getAttribute("usuario").toString();
      }
      if (request.getParameter("Opcion") != null) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
          connection = this.tabla.getConnection();
          preparedStatement = connection.prepareStatement("select i_codigopagina from sp_opcionmodulo where i_opcionmodulo=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
          preparedStatement.setInt(1, Integer.parseInt(request.getParameter("Opcion")));
          resultSet = preparedStatement.executeQuery();
          if (resultSet.next()) {
            paginaP = resultSet.getString("i_codigopagina");
          }
        } catch (SQLException e) {
          this.log.error("Error inesperado obteniendo pagina", e);
        } finally {
          if (resultSet != null) {
            try {
              resultSet.close();
            } catch (SQLException e) {
            }
          }
          if (preparedStatement != null) {
            try {
              preparedStatement.close();
            } catch (SQLException e) {
            }
          }
        }
      }
      ipP = request.getRemoteAddr().toString();
    } else {
      this.log.trace("El usuario no tiene sesion");
    }
  }

  public int insertalog(String queryxy) throws SQLException {

    if (tabla.update(queryxy)) {
      return 1;
    } else {
      return 0;
    }

  }

  public void InsertaLogAuditoria(String s) throws SQLException {
  }

  protected Log log;

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public Persistente() {
    this.log = LogFactory.getLog(this.getClass());
    this.log.trace("Obteniendo tabla...");
    this.tabla = new JDBCAdapter();
    inicializar();
    crearSentencias();
  }

  // MÃ©todo que elabora las preparedStatements
  private boolean crearSentencias() {
    int i = 0;
    if (nombreTabla == null) {
      this.log.trace("No se encontro el nombre de la tabla");
      return false;
    }
    if (atributos.length == 0 || nombreTabla.trim().length() == 0) {
      this.log.warn("No se encontro el nombre de la tabla y/o no se encontraron atributos");
      return false;
    }
    StringBuffer query = new StringBuffer("insert into " + nombreTabla + " (");
    StringBuffer query1 = new StringBuffer("update " + nombreTabla + " set ");
    // Colocar los atributos primero
    for (i = 0; i < atributos.length; i++) {
      query.append(atributos[i]);
      if (atributos[i].equalsIgnoreCase("c_usotasa")) {
        System.out.println("USO TASA+USO TASA+USO TASA" + i);
      }
      if (i == (atributos.length - 1)) {
        query.append(") values (");
      } else {
        query.append(',');
      }
    }
    for (i = 0; i < atributos.length; i++) {
      query.append('?');
      if (i == (atributos.length - 1)) {
        query.append(')');
      } else {
        query.append(',');
      }
    }
    for (i = elementosLLave; i < atributos.length; i++) {
      query1.append(atributos[i]);
      if (i == (atributos.length - 1)) {
        query1.append(" = ? ");
      } else {
        query1.append(" = ?,");
      }
    }
    if (elementosLLave > 0) {
      // Ponerle el where a la sentencia de actualizacion, reemplaza el
      // mÃ©todo doWhere() que se empleaba hasta ahora
      query1.append(" where ");
      for (i = 0; i < elementosLLave; i++) {
        if (i == elementosLLave - 1) {
          query1.append(atributos[i] + " = ? ");
        } else {
          query1.append(atributos[i] + " = ? and ");
        }
      }
    }
    this.queryInsertar = query.toString();
    this.queryActualizar = query1.toString();
    return true;
  }

  /*
   * Metodos para el paso de las variables que permiten la generacion dinamica de SQL
   */
  public void setPersistente(Persistente p) {
    persistente = p;
  }

  public void setAtributos(String a[]) {
    atributos = a;
  }

  public void setPrecision(int a[]) {
    precision = a;
  }

  public boolean actualizarFase(Double tasaOfrecida, Date fechaInicial, Date fechaFinal, Integer estadoFase, Integer tipoAdjudicacion, String orden, Integer emision, String nombre, Integer idFase) {
    // TODO: Cambiar el query x algo seguro
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement("update dm_fase set  c_tasaofrecida=" + tasaOfrecida + " , d_fechainicial='" + fechaInicial + "' , d_fechafinal='" + fechaFinal
              + "' , i_estadofase=" + estadoFase + " , i_tipoadjudicacion=" + tipoAdjudicacion + " , c_orden='" + orden + "' , c_nombre='" + nombre + "' , i_idfase=" + idFase
              + " where i_emision=" + emision);
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public boolean actualizarEmision(Integer Emision, String Nemo, String Nombre, BigDecimal Montototal, String Unidad, Integer valorUnidad, Double Tasacolocacion, BigDecimal Montocolocacion,
          Date Fechainicial, Date Fechafinal, Time Horainicial, Time Horafinal, Date Fechacumplimiento) {
    // TODO: Cambiar el query x algo seguro
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement("update sep_emision set  c_nemo='" + Nemo + "' , c_nombre='" + Nombre + "' , e_montototal=" + Montototal + " , c_unidad='" + Unidad
              + "' , c_valorunidad=" + valorUnidad + " , c_tasacolocacion=" + Tasacolocacion + " , c_montocolocacion='" + Montocolocacion + "', d_fechainicial='" + Fechainicial
              + "' ,d_fechafinal='" + Fechafinal + "' , c_horainicial='" + Horainicial + "', c_horafinal='" + Horafinal + "' ,c_fechacumplimiento='" + Fechacumplimiento + "' where i_emision="
              + Emision);
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
        }
      }
    }

  }

  public boolean eliminarFase(Integer Emision) {
    // TODO: Cambiar el query x algo seguro
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement("delete from  dm_fase where i_emision=" + Emision + " and i_idfase=1 ");
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public boolean eliminarEmision(Integer Emision) {
    // TODO: Cambiar el query x algo seguro
    PreparedStatement preparedStatement = null;
    Connection connection = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement("delete from  sep_emision where i_emision=" + Emision);
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
        }
      }
    }
  }

  public void setTiposRadioMaestro(Vector v) {
    this.tiposRadioMaestro = v;
  }

  public void setNombreTabla(String nt) {
    nombreTabla = nt;
  }

  public void setElementosLLave(int ell) {
    elementosLLave = ell;
  }

  // cambios vladimir
  public void setLongitud(int longitud[]) {
    this.longitud = longitud;
  }

  public int[] getLongitud() {
    return this.longitud;
  }

  public Vector getAtributosLLave() {
    String llav[] = this.getAtributos();
    Vector vf = new Vector();
    vf.removeAllElements();
    for (int hj = 0; hj < this.getElementosLLave(); hj++) {
      vf.add(llav[hj]);
    }
    return vf;
  }

  public boolean esNumericoD(String decimal) {
    boolean result = true;
    try {
      double valor;
      valor = Double.parseDouble(decimal);
      result = true;
    } catch (NumberFormatException nfe) {
      result = false;
    }
    return result;
  }

  public boolean esFormaFecha(String fecha) {
    boolean result = true;
    try {
      String agno = fecha.substring(0, 4);
      String mes = fecha.substring(5, 7);
      String dia = fecha.substring(8, 10);
      char sepA = fecha.charAt(4);
      char sepM = fecha.charAt(7);
      if (sepA != '-') {
        return false;
      }
      if (sepM != '-') {
        return false;
      }
      if (esNumerico(agno)) {
        if (esNumerico(mes) && ((new Integer(mes)).intValue() <= 12)) {
          if (esNumerico(dia) && ((new Integer(dia)).intValue() <= 31)) {
            return true;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (Exception ev) {
      result = false;
    }
    return result;
  }

  // cambio del mÃ©todo validaContenido, se adiciono int siNulo[]
  public boolean validaContenido(Vector v, String tipos[], int siNulo[]) {

    int sq = 0;

    if (v.size() == tipos.length) {
      for (int x = 0; x < v.size(); x++) {

        if (tipos[x].equals("Date")) {
          if (!esFormaFecha((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("int")) {
          if (!esNumerico((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("Integer")) {
          if (!esNumerico((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("Double")) {
          if (!esNumericoD((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("float")) {
          if (!esNumericoD((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("Time")) {
          if (!esFormaHora((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }
        if (tipos[x].equals("BigInteger")) // System.out.print("ENTRA BIG INTEGER----->"+esNumericoBig((String)v.elementAt(x)));
        {
          if (!esNumericoBig((String) v.elementAt(x))) {
            if (siNulo[x] == 1) {
              return false;
            }
          }
        }

      }
      return true;
    } else {
      return true;
    }
  }

  public Vector validaContenidoR(Vector v, String tipos[]) {
    Vector valida = new Vector();
    if (v.size() == tipos.length) {
      for (int x = 0; x < v.size(); x++) {
        if (tipos[x].equals("Date")) {
          if (!esFormaFecha((String) v.elementAt(x))) {
            valida.addElement(new Integer(1));
          } else {
            valida.addElement(new Integer(0));
          }
        }
        if (tipos[x].equals("int")) {
          if (!esNumerico((String) v.elementAt(x))) {
            valida.addElement(new Integer(1));
          } else {
            valida.addElement(new Integer(0));
          }
        }
        if (tipos[x].equals("Integer")) {
          if (!esNumerico((String) v.elementAt(x))) {
            valida.addElement(new Integer(1));
          } else {
            valida.addElement(new Integer(0));
          }
        }
        if (tipos[x].equals("Double")) {
          if (!esNumericoD((String) v.elementAt(x))) {
            valida.addElement(new Integer(1));
          } else {
            valida.addElement(new Integer(0));
          }
        }
        if (tipos[x].equals("float")) {
          if (!esNumericoD((String) v.elementAt(x))) {
            valida.addElement(new Integer(1));
          } else {
            valida.addElement(new Integer(0));
          }
        }
        if (tipos[x].equals("String")) {
          valida.addElement(new Integer(0));
        }
        if (tipos[x].equals("char")) {
          valida.addElement(new Integer(0));
        }
        if (tipos[x].equals("Char")) {
          valida.addElement(new Integer(0));
        }
        if (tipos[x].equals("string")) {
          valida.addElement(new Integer(0));
        }
        if (tipos[x].equals("Time")) {
          valida.addElement(new Integer(0));
        }

      }
    }

    return valida;
  }

  // cambios Jose Manuel - Erlinton
  public boolean esNumerico(String numero) {
    boolean result = true;
    try {
      int valor;
      valor = Integer.parseInt(numero);
      result = true;
    } catch (NumberFormatException nfe) {
      result = false;
    }
    return result;
  }

  public boolean esNumericoBig(String numero) {
    boolean result = true;
    try {
      // valor;
      BigInteger valor = new BigInteger(numero);
      int g = valor.intValue();
      result = true;
    } catch (NumberFormatException nfe) {
      nfe.printStackTrace();
      result = false;
    }
    return result;
  }

  public boolean validaFecha(String fecha, String fechaSistema) {

    boolean respuesta = true;

    int agno = Integer.parseInt(fecha.substring(0, 4));
    int agno1 = Integer.parseInt(fechaSistema.substring(0, 4));
    if (agno < agno1) {
      System.out.println("El aÃ±o no es vÃ¡lido.");
      respuesta = false;
      return (respuesta);
    }

    int mes = Integer.parseInt(fecha.substring(4, 6));
    int mes1 = Integer.parseInt(fechaSistema.substring(4, 6));

    if (mes > 12 || mes < 1 || (mes < mes1)) {
      System.out.println("El mes no es vÃ¡lido.");
      return false;
    }
    int dia = Integer.parseInt(fecha.substring(6, 8));
    if (dia > 31 || dia < 1) {
      System.out.println("El dÃ­a no es vÃ¡lido.");
      respuesta = false;
    }
    return respuesta;
  }

  public int getSize() throws SQLException {
    int i = 0;

    if (rs != null) {
      try {
        int posAct = this.rs.getRow();
        this.rs.last();
        i = rs.getRow();
        this.rs.absolute(posAct);
      } catch (SQLException se) {
        System.err.print(se);
      }
    }
    // System.gc();
    return i;
  }

  public int getSRow() {
    int i = 0;
    try {
      if (this.rs.next()) {
        i = 1;
      }
    } catch (SQLException ep) {
      i = 0;
    }
    return i;
  }

  public String voltearFechaV(String str)// Para dias de 10 en adelante
  {
    String mes = str.substring(3, 5);
    if (new Integer(mes).intValue() >= 10) {
      mes = "" + ((new Integer(mes).intValue()));
    } else {
      mes = "0" + ((new Integer(mes).intValue()));
    }
    return str.substring(6, 10) + "/" + mes + "/" + str.substring(0, 2);
  }

  // cambios Ivan
  public boolean verificaNull() {
    boolean terminar = false;
    int i = 1;
    int j = 1;

    try {
      metaData = rs.getMetaData();
      if (getSize() > 0) {
        while (j <= getSize()) {
          i = 1;
          while (i <= metaData.getColumnCount()) {
            // columna es no requerida
            if (metaData.isNullable(i) == 1) {
              if (rs.getString(metaData.getColumnName(i)) == null) {
                if (metaData.getColumnClassName(i) == "java.lang.String") {
                  rs.updateString(metaData.getColumnName(i), "");
                  rs.updateRow();
                }

                if (metaData.getColumnClassName(i) == "java.sql.Date") {
                  rs.updateDate(metaData.getColumnName(i), Date.valueOf("2000-01-01"));
                  rs.updateRow();
                }

                if (metaData.getColumnClassName(i) == "java.math.BigDecimal") {
                  rs.updateDouble(metaData.getColumnName(i), 0.0);
                  rs.updateRow();
                }

                if (metaData.getColumnClassName(i) == "java.lang.Byte") {
                  rs.updateInt(metaData.getColumnName(i), 0);
                  rs.updateRow();
                }
              }
            }
            i++;
          }
          j++;
        }
      } else {
        terminar = false;
      }
    } catch (Exception ex) {
      terminar = true;
    }
    return terminar;
  }

  // cambio VOR
  public String getNombreTabla() {
    return nombreTabla;
  }

  public void setAlias(String alias[]) {
    this.alias = alias;
  }

  public void setTipos(String tipos[]) {
    this.tipos = tipos;
  }

  public void setPresentacion(int presentacion[]) {
    this.presentacion = presentacion;
  }

  public int[] getPresentacion() {
    return this.presentacion;
  }

  // Cambio ET
  public int getElementosLLave() {
    return elementosLLave;
  }

  public String[] getAtributos() {
    return this.atributos;
  }

  public int[] getPrecision() {
    return this.precision;
  }

  public String[] getTipos() {
    return this.tipos;
  }

  public String[] getAlias() {
    return this.alias;
  }

  public int getElementos() {
    return this.atributos.length;
  }

  /**
   * getRegistro se utiliza para visualizar el registro actual
   *
   * @param rs ResultSet que contiene el cursor devuelto por el query
   * @return Persistente registro actual
   */
  public Vector getRegistro() throws SQLException {
    Vector vector = new Vector();
    String[] columnNames = {};
    ResultSetMetaData metaData;
    try {
      metaData = rs.getMetaData();
    } catch (SQLException se) {
      throw new SQLException(se.toString());
    }
    try {
      int numberOfColumns = metaData.getColumnCount();
      columnNames = new String[numberOfColumns];
      for (int column = 0; column < numberOfColumns; column++) {
        columnNames[column] = metaData.getColumnLabel(column + 1);
      }

      Vector newRow = new Vector();
      for (int i = 1; i <= tabla.getColumnCount(); i++) {
        int type;
        try {
          type = metaData.getColumnType(i);
        } catch (SQLException e) {
          type = Types.VARCHAR;
        }
        Object objeto = rs.getObject(i);
        switch (type) {
          case Types.DATE:
            newRow.addElement(voltearFecha(objeto.toString()));
            break;
          default:
            newRow.addElement(objeto);
        }
      }
      vector.addElement(newRow);
    } catch (SQLException ex) {
      System.err.println(ex);
    }
    return vector;
  }

  public boolean insertar() {
    Connection connection = null;
    connection = this.tabla.getConnection();
    boolean resp = this.insertar(connection);
    //DbUtil.closeConnection(connection);
    return resp;
  }

  public boolean insertar(Connection connection) {
    String conformato = "";
    if (fecParametro == null) {
      fecParametro = capturaFechaParametro();
    }
    Vector contenido = this.getContenido();
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(this.queryInsertar);
      for (int i = 0; i < contenido.size(); i++) {
        if (contenido.elementAt(i) != null) {
          if (contenido.elementAt(i).getClass() == String.class) {
            preparedStatement.setString(i + 1, contenido.elementAt(i).toString());
            if (i == 44) {
              System.out.println(contenido.elementAt(i).toString() + " ----- " + contenido.elementAt(i).toString().length());
            }
          } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
              if (contenido.elementAt(i) == null) {
                  preparedStatement.setString(i + 1, null);
              } else {
                  if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
                      preparedStatement.setString(i + 1, null);
                  } else {
                      preparedStatement.setString(i + 1, voltearFecha(contenido.elementAt(i).toString()));
                  }
              }
          } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
              if (contenido.elementAt(i) == null) {
                  preparedStatement.setString(i + 1, null);
              } else {
                  if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
                      preparedStatement.setString(i + 1, null);
                  } else {
                      preparedStatement.setString(i + 1, voltearFecha(contenido.elementAt(i).toString()));
                  }
              }
          } else if (contenido.elementAt(i).getClass() == java.math.BigDecimal.class) {

            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            BigDecimal bd;
            if (this.precision != null) {
              if (this.precision.length >= i) {
                bd = ((new BigDecimal((((BigDecimal) contenido.elementAt(i)).toString()))).setScale(4, BigDecimal.ROUND_HALF_UP));
              } else {
                bd = (BigDecimal) contenido.elementAt(i);
              }

            } else {
              bd = (BigDecimal) contenido.elementAt(i);
            }
            conformato = formato.format(bd.doubleValue());
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.lang.Float.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            if (this.precision != null) {
              if (this.precision.length >= i) {
                BigDecimal bd = ((new BigDecimal((((Float) contenido.elementAt(i)).toString()))).setScale(4, BigDecimal.ROUND_HALF_UP));
                conformato = formato.format(bd.doubleValue());
              } else {
                conformato = formato.format(((Float) contenido.elementAt(i)).doubleValue());
              }
            } else {
              conformato = formato.format(((Float) contenido.elementAt(i)).doubleValue());
            }
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.lang.Double.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            if (this.precision != null) {
              if (this.precision.length >= i) {
                BigDecimal bd = ((new BigDecimal((((Double) contenido.elementAt(i)).toString()))).setScale(this.precision[i], BigDecimal.ROUND_HALF_UP));
                conformato = formato.format(bd.doubleValue());
              } else {
                conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
              }
            } else {
              conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
            }

            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            conformato = formato.format(contenido.elementAt(i));
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            conformato = formato.format(contenido.elementAt(i));
            preparedStatement.setString(i + 1, conformato);
          } else {
            preparedStatement.setString(i + 1, contenido.elementAt(i).toString());
          }
        } else {
          preparedStatement.setString(i + 1, null);
        }
      }
      /**
       * ***** CIRCULAR 052 ******
       */
      try {
        InsertaLogAuditoria(this.queryInsertar + " " + contenido.toString());
      } catch (Exception ex) {
        this.log.error("Error insertando en el log de auditoria " + this.queryInsertar + " " + contenido.toString());
      }

      preparedStatement.execute();
      return true;
    } catch (Exception e) {
      this.log.error("Error ejecutando la insercion " + e.getMessage(), e);
      return false;
    } finally {
      DbUtil.closeStatement(preparedStatement);
    }
  }

  // CONSULTA RESULTSET
  public boolean estadoRS() {
    if (rs == null) {
      return false;
    } else {
      return true;
    }
  }

  public void consulta() throws SQLException {
    String query = "SELECT * FROM " + nombreTabla;
    String atrb[] = this.getAtributos();
    String llave = atrb[0];
    if (!nombreTabla.equals("sp_opcionmodulo")) {
      query = query + " order by " + llave + "";
    }

    registro = this.tabla.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    this.rs = registro.executeQuery(query);

  }

  public void consultaG2(String query, Object... args) throws SQLException {
    try {
      InsertaLogAuditoria(query + " - " + (args != null ? Arrays.toString(args) : ""));
    } catch (SQLException e) {
      this.log.warn("Error insertando el el log de Auditoria el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
    try {
      this.preparedStatement = this.tabla.getConnection().prepareStatement(query);
      if (args != null) {
        int i = 0;
        for (Object arg : args) {
          preparedStatement.setObject(++i, arg);
        }
      } else {
        this.log.trace("Â¿Un query sin argumentos? \"" + query + "\".");
      }
      this.rs = this.preparedStatement.executeQuery();
    } catch (SQLException e) {
      this.log.error("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
      throw new SQLException("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
  }

  public void consultaG(String query, Object... args) throws SQLException {
    try {
      InsertaLogAuditoria(query + " - " + (args != null ? Arrays.toString(args) : ""));
    } catch (SQLException e) {
      this.log.warn("Error insertando el el log de Auditoria el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
    try {
      this.preparedStatement = this.tabla.getConnection().prepareStatement(query);
      if (args != null) {
        int i = 0;
        for (Object arg : args) {
          preparedStatement.setObject(++i, arg);
        }
      } else {
        this.log.trace("Â¿Un query sin argumentos? \"" + query + "\".");
      }
      logger.debug("PreparedStatement's query: {}",this.preparedStatement);
      this.rs = this.preparedStatement.executeQuery();
    } catch (SQLException e) {
      this.log.error("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
      throw new SQLException("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
  }

  /**
   * @deprecated Si desea ejecutar una consulta con parametros, entonces usar el metodo consultaG(String query, Object... args) que usa
   * preparedStatement; si desea ejecutar una consulta de solo lectura, entonces emplear el metodo consultaLectura(String query).
   * @param query a ejecutar
   * @throws SQLException si ocurre un error con la conexion o el query
   */
  public void consultaG(String s) throws SQLException {
    this.log.trace("Esta usando un metodo deprecado e inseguro, usar consultaG(String query, Object... args ) query: " + s);
    registro = this.tabla.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(s);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    this.rs = registro.executeQuery(s);

  }

  /**
   * Ejecuta una consulta de solo lectura
   *
   * @param query a ejecutar
   * @throws SQLException si ocurre un error con la conexion o el query
   */
  public void consultaLectura(String s) throws SQLException {

    registro = this.tabla.getConnection().createStatement();

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(s);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    this.rs = registro.executeQuery(s);
  }

  // consulta gene
  // MÃ©todos de desplazamiento ResultSet
  public boolean next() throws SQLException {
    return this.rs.next();
  }

  public boolean previous() throws SQLException {
    return this.rs.previous();
  }

  public boolean last() throws SQLException {
    return this.rs.last();
  }

  public boolean first() throws SQLException {

    return this.rs.first();

  }

  public void cerrarConexiones() {
    DbUtil.closeRecurso(this);
  }

  /*
   * public void setContenido(){ }
   */
  // MÃ©todo general consulta por llaves primarias
  public void consultaP() throws SQLException {
    StringBuffer queryx = new StringBuffer("select * from " + nombreTabla);
    queryx.append(doWhere());
    String query = new String(queryx.toString());
    String atrb[] = this.getAtributos();
    String llave = atrb[0];
    if (!nombreTabla.equals("sp_opcionmodulo")) {
      query = query + " order by " + llave + "";
    }

    registro = this.tabla.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    this.rs = registro.executeQuery(query);

  }

  public int actualiza(String query, Object... args) throws SQLException {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      try {
        InsertaLogAuditoria(query + args);
      } catch (SQLException ex) {
        log.error("Error insertando log auditoria actualiza ps", ex);
      }
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement(query);
      if (args != null) {
        int i = 0;
        for (Object arg : args) {
          preparedStatement.setObject(++i, arg);
        }
      } else {
        this.log.warn("Â¿esta haciendo un update sin argumentos? \"" + query + "\".");
      }
      preparedStatement.executeUpdate();
      return 1;
    } catch (SQLException e) {
      this.log.error("Error inesperado ejecutando actualizacion \"" + query + "\" " + Arrays.toString(args), e);
      return 0;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.warn("Error cerrando el preparedStatement", e);
        }
      }
    }
  }

  /**
   * @deprecated @param queryxy
   * @return
   * @throws SQLException
   */
  public int actualiza(String queryxy) throws SQLException {
    this.log.trace("Esta usando un metodo deprecado e inseguro, usar actualiza(String query, Object... args ) query: " + queryxy);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(queryxy);
    } catch (SQLException ex) {
      log.error("Error insertando log auditoria actualiza", ex);
    }
    if (tabla.update(queryxy)) {
      return 1;
    } else {
      return 0;
    }

  }

  public void consultaP(String cadenaWhere, Object... args) throws SQLException {
    String query = "select * from " + nombreTabla + " " + cadenaWhere;

    if (!nombreTabla.equals("sp_opcionmodulo")) {
      query += " order by " + (this.getAtributos()[0]) + "";
    }

    try {
      InsertaLogAuditoria(query + " - " + (args != null ? Arrays.toString(args) : ""));
    } catch (SQLException e) {
      this.log.warn("Error insertando el el log de Auditoria el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
    try {
      this.preparedStatement = this.tabla.getConnection().prepareStatement(query, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      if (args != null) {
        int i = 0;
        for (Object arg : args) {
          preparedStatement.setObject(++i, arg);
        }
      } else {
        this.log.trace("Â¿Un query sin argumentos? \"" + query + "\".");
      }
      this.rs = preparedStatement.executeQuery();
    } catch (SQLException e) {
      this.log.error("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
      throw new SQLException("Error inesperado ejecutando el query \"" + (query + " - " + (args != null ? Arrays.toString(args) : "")) + "\"", e);
    }
  }

  /**
   * @deprecated @param cadenaWhere
   * @throws SQLException
   */
  public void consultaP(String cadenaWhere) throws SQLException {
    this.log.trace("Esta usando un metodo deprecado e inseguro, usar consultaP(String cadenaWhere, Object... args ) cadenaWhere: " + cadenaWhere);

    StringBuffer query = new StringBuffer("select * from " + nombreTabla + " ");
    query.append(cadenaWhere);

    registro = this.tabla.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

    String consulta = query.toString();

    String atrb[] = this.getAtributos();
    String llave = atrb[0];
    if (!nombreTabla.equals("sp_opcionmodulo")) {
      consulta = consulta + " order by " + llave + "";
    }
    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(consulta);
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    this.rs = registro.executeQuery(consulta);

  }

  /**
   * MÃ©todo consultar: genera de forma dinamica el SQL para la consulta.
   *
   * @return Vector resultado de la consulta
   */
  public Vector consultar() {
    //updateConnection(0);
    StringBuilder query = new StringBuilder("SELECT * FROM " + nombreTabla);
    query.append(doWhere());

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    Vector v = new Vector();
    if (!tabla.query(query.toString())) {
      return null;
    } else {
      for (int i = 0; i < tabla.getRowCount(); i++) {
        Persistente p = persistente.nuevo(tabla.getRow(i));
        p.cerrarConexiones();
        v.addElement(p);
      }
      return v;
    }
  }

  /*
   * @param prametro Nombre de la tabla
   *
   * @return Vector con los resultados de la consulta
   */
  public Vector consultaTabla(String parametro) {

    StringBuffer query = new StringBuffer("select * from " + nombreTabla);
    query.append(parametro);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    /**
     * ***********************
     */
    Vector v = new Vector();
    if (!tabla.query(query.toString())) {
      return null;
    } else {
      for (int i = 0; i < tabla.getRowCount(); i++) {
        Persistente p = persistente.nuevo(tabla.getRow(i));
        v.addElement(p);
      }
      return v;
    }
  }

  public Vector consultaTabla() {
    StringBuffer query = new StringBuffer("select * from " + nombreTabla);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    Vector v = new Vector();
    if (!tabla.query(query.toString())) {
      return null;
    } else {
      for (int i = 0; i < tabla.getRowCount(); i++) {

        Persistente p = persistente.nuevo(tabla.getRow(i));
        v.addElement(p);
      }
      return v;
    }
  }

  public boolean actualizar() {
    String conformato = "";
    if (fecParametro == null) {
      fecParametro = capturaFechaParametro();
    }
    PreparedStatement preparedStatement = null;
    int i;
    Vector contenido = persistente.getContenido();
    try {
      preparedStatement = tabla.getConnection().prepareStatement(this.queryActualizar);
      for (i = elementosLLave; i < contenido.size(); i++) {
        if (contenido.elementAt(i) != null) {
          if (contenido.elementAt(i).getClass() == String.class) {
            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i - elementosLLave + 1, null);
            } else {
              if (contenido.elementAt(i).toString().isEmpty() || contenido.elementAt(i).toString() == null) {
                preparedStatement.setString(i - elementosLLave + 1, "2012-01-01");
              } else {
                preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
              }
            }
          } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i - elementosLLave + 1, null);
            } else {
              if (contenido.elementAt(i).toString().isEmpty() || contenido.elementAt(i).toString() == null) {
                preparedStatement.setString(i - elementosLLave + 1, "2012-01-01");
              } else {
                preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
              }
            }
          } else if (contenido.elementAt(i).getClass() == Float.class) {

            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            if (this.precision != null) {
              if (this.precision.length >= i) {
                BigDecimal bd = ((new BigDecimal((((Double) contenido.elementAt(i)).toString()))).setScale(this.precision[i], BigDecimal.ROUND_HALF_UP));
                conformato = formato.format(bd.doubleValue());
              } else {
                conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
              }
            } else {
              conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
            }
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == Double.class) {

            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            if (this.precision != null) {
              if (this.precision.length >= i) {
                BigDecimal bd = ((new BigDecimal((((Double) contenido.elementAt(i)).toString()))).setScale(this.precision[i], BigDecimal.ROUND_HALF_UP));
                conformato = formato.format(bd.doubleValue());
              } else {
                conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
              }
            } else {
              conformato = formato.format(((Double) contenido.elementAt(i)).doubleValue());
            }
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            conformato = formato.format(contenido.elementAt(i));
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {

            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          } else {
            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          }
        } else {
          preparedStatement.setString(i - elementosLLave + 1, null);
        }
      }
      int aux = contenido.size() - elementosLLave + 1;
      for (i = 0; i < elementosLLave; i++) {
        if (contenido.elementAt(i).getClass() == String.class) {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
          if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
            preparedStatement.setString(i + aux, null);
          } else {
            if (contenido.elementAt(i).toString().isEmpty() || contenido.elementAt(i).toString() == null) {
              preparedStatement.setString(i + aux, "2012-01-01");
            } else {
              preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
            }
          }
        } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
          if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
            preparedStatement.setString(i + aux, null);
          } else {
            if (contenido.elementAt(i).toString().isEmpty() || contenido.elementAt(i).toString() == null) {
              preparedStatement.setString(i + aux, "2012-01-01");
            } else {
              preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
            }
          }
        } else if (contenido.elementAt(i).getClass() == Float.class) {
          DecimalFormat formato = new DecimalFormat();
          formato.applyPattern("###0.#########");
          conformato = formato.format((Double) contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);
        } else if (contenido.elementAt(i).getClass() == Double.class) {
          DecimalFormat formato = new DecimalFormat();
          formato.applyPattern("###0.#########");
          conformato = formato.format(contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);
        } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
          java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          conformato = formato.format(contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);
        } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        } else {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        }
      }

      // Ejecutar la operacion

      /**
       * ***** CIRCULAR 052 ******
       */
      try {
        InsertaLogAuditoria(this.queryActualizar + " " + contenido.toString());
      } catch (SQLException ex) {
        this.log.error("Error actualizando el log de auditoria \"" + this.queryActualizar + "\" " + contenido.toString());
      }

      logger.debug("Persistente.actualizar preparedStatement: {}", preparedStatement);
      logger.debug("Persistente.actualizar queryActualizar: {}", queryActualizar);
      logger.debug("Persistente.actualizar contenido: {}", contenido);

      preparedStatement.execute();
      return true;

    } catch (SQLException e) {
      this.log.error("Error actualizando", e);
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.warn("Error cerrando el preparedStatement", e);
        }
      }
    }
  }

  public boolean eliminar() {
    StringBuffer query = new StringBuffer("delete from " + nombreTabla);
    query.append(doWhere());
    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    /**
     * ***********************
     */
    return tabla.update(query.toString());
  }

  /*
   * Metodos Abstractos que deben ser implementados en la clase que quiere ser persistente
   */
  abstract public Vector getContenido();

  abstract public Persistente nuevo(Vector v);

  abstract public void inicializar();

  abstract public void setContenido(Vector v);

  abstract public void setContenido() throws SQLException;
  /*
   * Metodos Privados
   */
  /**
   * construir la parte Where de la sentencia SQL.
   *
   * @return String resultado del Where.
   */
  private String doWhere() {
    StringBuffer whereString = new StringBuffer();
    Vector contenido = persistente.getContenido();
    boolean where = false;
    for (int i = 0; i < elementosLLave; i++) {
      if (contenido.elementAt(i) != null) {
        if (!where) {
          whereString.append(" where ");
          where = true;
        } else {
          whereString.append(" and ");
        }

        if (contenido.elementAt(i).getClass() == String.class) {
          whereString.append(atributos[i] + "='" + contenido.elementAt(i) + "'");
        } // else if (contenido.elementAt(i).getClass() ==
        // java.sql.Date.class)
        // whereString.append(atributos[i]+"='"+voltearFecha((java.sql.Date)contenido.elementAt(i))+"'");
        else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
          whereString.append(atributos[i] + "='" + (java.sql.Date) contenido.elementAt(i) + "'");
        } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
          whereString.append(atributos[i] + "='" + (java.util.Date) contenido.elementAt(i) + "'");
        } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
          whereString.append(atributos[i] + "='" + (java.util.Date) contenido.elementAt(i) + "'");
        } else {
          whereString.append(atributos[i] + "=" + contenido.elementAt(i));
        }
      }
    }
    return whereString.toString();
  }

  public String voltearFecha(String str) {
    if (fecParametro == null) {
      fecParametro = capturaFechaParametro();
    }
    int ocurrencia;
    ocurrencia = str.indexOf("-");
    String aux;
    int tam;
    int cont = 1;
    aux = str.substring(0, ocurrencia);
    tam = aux.length();

    // Soulucionar el proble 3-10-10
    if (tam < 4) {
      while (cont < 4) {
        str = "0" + str;
        cont++;
      }
    }

    if (str.equalsIgnoreCase(fecParametro) == true) {
      return "";
    }

    String mes = str.substring(5, 7);
    if (new Integer(mes).intValue() >= 10) {
      mes = "" + ((new Integer(mes).intValue()));
    } else {
      mes = "0" + ((new Integer(mes).intValue()));
    }

    return str.substring(0, 4) + "/" + mes + "/" + str.substring(8, 10);

  }

  public String voltearFechaVM(String str)// Para dias menores a 10
  {
    String mes = str.substring(2, 4);
    if (new Integer(mes).intValue() >= 10) {
      mes = "" + ((new Integer(mes).intValue()));
    } else {
      mes = "0" + ((new Integer(mes).intValue()));
    }
    return str.substring(5, 9) + "/" + mes + "/" + str.substring(0, 1);

  }

  public String cambiaFormato(String str) {
    return str.replace('/', '-');
  }

  public boolean fechaValida(String fecha) {
    java.util.Date fe1 = new java.util.Date();
    SimpleDateFormat forma = new SimpleDateFormat("yyyy-MM-dd");
    String fe = forma.format(fe1);
    fe1 = Date.valueOf(fe);
    Date fe2 = Date.valueOf(fecha);
    long days = ((fe1.getTime() - fe2.getTime()) / 86400000L);
    if (days < 0) {
      return false;
    } else {
      return true;
    }

  }

  public boolean fechaValidaP(String fecha) {
    String fechaParametro = "";
    long days = -1;
    int vs = 0;
    try {
      this.consultaG("select c_valor from sp_parametro where i_parametro=10");
      if (this.rs.first() == true) {
        fechaParametro = this.rs.getString("c_valor");
      }
    } catch (Exception ex) {
      this.log.error("Error inesperado obteniendo valor del parametro", ex);
    } finally {
      this.cerrarConexiones();
    }
    if (fecha.length() == 10) {
      String f1 = fecha.substring(4, 5);
      String f2 = fecha.substring(7, 8);
      if ((f1.equals("-")) || (f2.equals("-"))) {
        vs = 1;
      }
    }
    if (vs == 1) {
      Date fe1 = Date.valueOf(fechaParametro);
      Date fe2 = Date.valueOf(fecha);
      days = ((fe1.getTime() - fe2.getTime()) / 86400000L);
    }
    if (days < 0) {
      return false;
    } else {
      return true;
    }
  }

  public boolean eliminar1() {
    StringBuffer query = new StringBuffer("delete from " + nombreTabla);
    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return tabla.update(query.toString());
  }

  public void eliminar(String cadenaWhere) throws SQLException {

    StringBuffer query = new StringBuffer("delete from " + nombreTabla + " ");
    query.append(cadenaWhere);

    /**
     * ***** CIRCULAR 052 ******
     */
    try {
      InsertaLogAuditoria(query.toString());
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    /**
     * ***********************
     */
    String eliminar = query.toString();

    tabla.update(eliminar);

    // this.rs = registro.executeQuery(eliminar);
  }

  public boolean eliminar(String cadenaWhere, Object... args) throws SQLException {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    final String  query=" delete from "+nombreTabla+" where 1=1  " +cadenaWhere;
    try {
      try {
        InsertaLogAuditoria(query + args);
      } catch (SQLException ex) {
        log.error("Error insertando log auditoria actualiza ps", ex);
      }
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement(query);
      if (args != null) {
        int i = 0;
        for (Object arg : args) {
          preparedStatement.setObject(++i, arg);
        }
      } else {
        this.log.warn("Â¿esta haciendo un update sin argumentos? \"" + query + "\".");
      }
      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      this.log.error("Error inesperado ejecutando actualizacion \"" + query + "\" " + Arrays.toString(args), e);
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.warn("Error cerrando el preparedStatement", e);
        }
      }
    }
  }


  // Metodos implementados por Vladimir Oviedo, necesarios para la
  // administracion de mantenimientos
  public boolean validaRegistro(Vector registro) throws SQLException {
    return true;
  }

  public void setMensajeRegistro(String mensaje) {
    this.mensajeRegistro = mensaje;
  }

  public String getMensajeRegistro() {
    return this.mensajeRegistro;
  }

  public String capturaFechaParametro() {
    String fechaParametro = "";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement("select c_valor from sp_parametro where i_parametro = ?");
      preparedStatement.setInt(1, 20);
      resultSet = preparedStatement.executeQuery();
      if (resultSet.first()) {
        fechaParametro = resultSet.getString("c_valor");
      }
    } catch (SQLException e) {
      this.log.error("Error en la extraccion de la fecha en sp_parametro.", e);
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (SQLException e) {
          this.log.error("Error cerrando el resultset", e);
        }
      }
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.error("Error cerrando el preparedStatement", e);
        }
      }
    }
    return fechaParametro;
  }

  public boolean esFormaTimeStamp(String time) {
    boolean result = true;
    try {
      String agno = time.substring(0, 4);
      String mes = time.substring(5, 7);
      String dia = time.substring(8, 10);
      String hora = time.substring(11, 13);
      String min = time.substring(14, 16);
      String seg = time.substring(17, 19);
      char sepA = time.charAt(4);
      char sepB = time.charAt(7);
      char sepC = time.charAt(10);
      char sepD = time.charAt(13);
      char sepE = time.charAt(16);
      if (sepA != '-') {
        return false;
      }
      if (sepB != '-') {
        return false;
      }
      if (sepC != ':') {
        return false;
      }
      if (sepD != ':') {
        return false;
      }
      if (sepE != ':') {
        return false;
      }
      if (esNumerico(agno)) {
        if (esNumerico(mes) && ((new Integer(mes)).intValue() <= 12)) {
          if (esNumerico(dia) && ((new Integer(dia)).intValue() <= 31)) {
            if (esNumerico(hora) && (new Integer(hora).intValue() <= 24)) {
              if (esNumerico(min) && ((new Integer(min)).intValue() <= 60)) {
                if (esNumerico(seg) && ((new Integer(seg)).intValue() <= 60)) {
                  return true;
                } else {
                  return false;
                }
              } else {
                return false;
              }
            } else {
              return false;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (Exception ev) {
      result = false;
    }
    return result;
  }

  /**
   * Modificacion javier
   *
   */
  public boolean esFormaHora(String time) {
    boolean result = true;
    try {
      String hora = time.substring(0, 2);
      String min = time.substring(3, 5);
      String seg = time.substring(6, 8);
      char sepA = time.charAt(2);
      char sepM = time.charAt(5);
      if (sepA != ':') {
        return false;
      }
      if (sepM != ':') {
        return false;
      }
      if (esNumerico(hora) && (new Integer(hora).intValue() <= 24)) {
        if (esNumerico(min) && ((new Integer(min)).intValue() <= 60)) {
          if (esNumerico(seg) && ((new Integer(seg)).intValue() <= 60)) {
            return true;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    } catch (Exception ev) {
      result = false;
    }
    return result;
  }

  public String addSeparator(String fecha) {
    if (fecha.length() == 8) {
      String conSeparador = "";
      conSeparador = fecha.substring(0, 4);
      conSeparador += "-";
      conSeparador += fecha.substring(4, 6);
      conSeparador += "-";
      conSeparador += fecha.substring(6, 8);
      return conSeparador;
    } else {
      return fecha;
    }
  }

  public String addSeparator(String fecha, String separator) {
    if (fecha.length() > 5) {
      String conSeparador = "";
      conSeparador = fecha.substring(0, 2);
      conSeparador += separator;
      conSeparador += fecha.substring(2, 4);
      conSeparador += separator;
      conSeparador += fecha.substring(4, 6);

      return conSeparador;
    } else {
      return fecha;
    }
  }

  public String quitSeparator(String fecha) {
    if (fecha.length() == 10) {
      String fecha1 = "";
      for (int i = 0; i < fecha.length(); i++) {
        if (fecha.charAt(i) == '-') {
          fecha = fecha.substring(0, i) + fecha.substring(i + 1, fecha.length());
        }
      }
      fecha1 = fecha;
      return fecha1;
    } else {
      return fecha;
    }
  }

  // MI PRUEBA
  public boolean insertarT() throws SQLException {
    if (fecParametro == null) {
      fecParametro = capturaFechaParametro();
    }
    Vector contenido = persistente.getContenido();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement(this.queryInsertar);
      for (int i = 0; i < contenido.size(); i++) {
        if (contenido.elementAt(i) != null) {
          if (contenido.elementAt(i).getClass() == String.class) {
            preparedStatement.setString(i + 1, contenido.elementAt(i).toString());
          } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i + 1, "");
            } else {
              preparedStatement.setString(i + 1, voltearFecha(contenido.elementAt(i).toString()));
            }
          } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i + 1, "");
            } else {
              preparedStatement.setString(i + 1, voltearFecha(contenido.elementAt(i).toString()));
            }
          } else if (contenido.elementAt(i).getClass() == java.math.BigDecimal.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            BigDecimal bd = (BigDecimal) contenido.elementAt(i);
            String conformato = formato.format(bd.doubleValue());
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.lang.Float.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            String conformato = formato.format(contenido.elementAt(i));
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.lang.Double.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            String conformato = formato.format(contenido.elementAt(i));
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String conformato = formato.format(contenido.elementAt(i));
            preparedStatement.setString(i + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
            preparedStatement.setString(i + 1, contenido.elementAt(i).toString());
          } else {
            preparedStatement.setString(i + 1, contenido.elementAt(i).toString());
          }
        } else {
          preparedStatement.setString(i + 1, null);
        }
      }
      /**
       * ***** CIRCULAR 052 ******
       */
      try {
        InsertaLogAuditoria(this.queryInsertar + " " + contenido.toString());
      } catch (SQLException ex) {
        this.log.error("Error insertando en el log de auditoria \"" + this.queryInsertar + "\" " + contenido.toString());
      }
      preparedStatement.execute();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      this.log.error("Error en la insercion", e);
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.warn("Error cerrando el preparedStatement", e);
        }
      }
    }
  }

  public boolean actualizarT() throws SQLException {
    String conformato = "";

    if (fecParametro == null) {
      fecParametro = capturaFechaParametro();
    }
    int i;
    Vector contenido = persistente.getContenido();
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = this.tabla.getConnection();
      preparedStatement = connection.prepareStatement(this.queryActualizar);

      for (i = elementosLLave; i < contenido.size(); i++) {
        if (contenido.elementAt(i) != null) {
          if (contenido.elementAt(i).getClass() == String.class) {
            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i - elementosLLave + 1, "");
            } else {
              preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
            }
          } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
            if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
              preparedStatement.setString(i - elementosLLave + 1, "");
            } else {
              preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
            }
          } else if (contenido.elementAt(i).getClass() == Float.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            conformato = formato.format((Double) contenido.elementAt(i));
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == Double.class) {
            DecimalFormat formato = new DecimalFormat();
            formato.applyPattern("###0.#########");
            conformato = formato.format(contenido.elementAt(i));
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
            java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            conformato = formato.format(contenido.elementAt(i));
            conformato = conformato.replace(",", ".");
            preparedStatement.setString(i - elementosLLave + 1, conformato);
          } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          } else {
            preparedStatement.setString(i - elementosLLave + 1, contenido.elementAt(i).toString());
          }
        } else {
          preparedStatement.setString(i - elementosLLave + 1, "");
        }
      }
      int aux = contenido.size() - elementosLLave + 1;
      for (i = 0; i < elementosLLave; i++) {

        if (contenido.elementAt(i).getClass() == String.class) {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        } else if (contenido.elementAt(i).getClass() == java.sql.Date.class) {
          if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
            preparedStatement.setString(i + aux, "");
          } else {
            preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
          }
        } else if (contenido.elementAt(i).getClass() == java.util.Date.class) {
          if (contenido.elementAt(i).toString().equals(new String(fecParametro))) {
            preparedStatement.setString(i + aux, "");
          } else {
            preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
          }
        } else if (contenido.elementAt(i).getClass() == Float.class) {
          DecimalFormat formato = new DecimalFormat();
          formato.applyPattern("###0.#########");
          conformato = formato.format((Double) contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);
        } else if (contenido.elementAt(i).getClass() == Double.class) {
          DecimalFormat formato = new DecimalFormat();
          formato.applyPattern("###0.#########");
          conformato = formato.format(contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);
        } else if (contenido.elementAt(i).getClass() == java.sql.Timestamp.class) {
          java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          conformato = formato.format(contenido.elementAt(i));
          conformato = conformato.replace(",", ".");
          preparedStatement.setString(i + aux, conformato);

        } else if (contenido.elementAt(i).getClass() == java.sql.Time.class) {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        } else {
          preparedStatement.setString(i + aux, contenido.elementAt(i).toString());
        }
      }

      /**
       * ***** CIRCULAR 052 ******
       */
      try {
        InsertaLogAuditoria(this.queryActualizar + " " + contenido.toString());
      } catch (SQLException ex) {
        this.log.error("Error insertando en el log de auditoria \"" + this.queryActualizar + "\" " + contenido.toString());
      }

      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      this.log.error("Error en la actualizacion", e);
      return false;
    } finally {
      if (preparedStatement != null) {
        try {
          preparedStatement.close();
        } catch (SQLException e) {
          this.log.warn("Error cerrando el preparedStatement", e);
        }
      }
    }

  }

//  public abstract void setContenido(Vector v);
//
//  public abstract void setContenido() throws SQLException;

  @Override
  protected void finalize() throws Throwable {
    cerrarConexiones();
    super.finalize();
  }

  public Connection getConnection() throws SQLException {
    return tabla.getConnection();
  }
}