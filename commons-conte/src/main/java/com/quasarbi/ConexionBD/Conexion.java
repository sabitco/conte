package com.quasarbi.ConexionBD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;
import com.quasarbi.JavaDelegate.GeneracionAutoApertura;
import com.quasarbi.entity.Asociacion;
import com.quasarbi.entity.Evaluacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Conexion {

    private DataSource ds;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public Conexion() {
        getDataSource();
    }

    public void getDataSource() {
        try {
            InitialContext ctx = new InitialContext();
            this.ds = (DataSource) ctx.lookup("java:comp/env/jdbc/ConteDS");
        } catch (NamingException e) {
            logger.error("NO se pudo obtener DataSource {}", e.getMessage());
        }
    }

    public static Connection getConnection() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx
                    .lookup("java:comp/env/jdbc/ConteDS");
            return dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            //
            e.printStackTrace();
        }
        return null;
    }
    
    public Map<String, Object> getDataHojaEstudio(String document) throws SQLException{
        Map<String, Object> data = new HashMap<String, Object>();
        
        String query = "SELECT t.nombres AS 'nombreSolicitante', "
                + "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido) AS 'apellidoSolicitante', "
                + "sr.radicado AS 'expediente' "              
                + "FROM solicitud_radicado sr "
                + "INNER JOIN tecnico t ON t.documento = sr.documento "
                + "INNER JOIN evaluacion e on e.documento = sr.documento "
                + "WHERE sr.documento = '" + document + "' LIMIT 1";
        logger.debug("Ejecutando query consunta datos matricula {}", query);
        Connection c = ds.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            data.put("nombreSolicitante", rs.getString("nombreSolicitante"));
            data.put("apellidoSolicitante", rs.getString("apellidoSolicitante"));            
            data.put("numeroRadicacion", rs.getString("expediente"));
            
            data.put("expediente", rs.getString("expediente"));            
            data.put("nombres", rs.getString("nombreSolicitante"));
            data.put("apellidos", rs.getString("apellidoSolicitante"));
            data.put("observaciones", "");
            
            data.put("documento", document);
        }

        return data;
    }
    
    public Map<String, Object> getDataMatricula(String document) throws SQLException{
        Map<String, Object> data = new HashMap<String, Object>();
        
        String query = "SELECT t.nombres AS 'nombreSolicitante', "
                + "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido) AS 'apellidoSolicitante', "
                + "r.codigo AS 'numResolucionSolicitante', "
                + "CAST(r.fecha AS DATE) AS 'fechaResolucion', "
                + "t.matricula AS 'numMatricula', "                
                + "sr.radicado AS 'numRadicacion' "              
                + "FROM solicitud_radicado sr "
                + "INNER JOIN tecnico t ON t.documento = sr.documento "
                + "INNER JOIN resolucion r on r.numero = sr.documento "
                + "WHERE sr.documento = '" + document + "'";
        logger.debug("Ejecutando query consunta datos matricula {}", query);
        Connection c = ds.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            data.put("nombreSolicitante", rs.getString("nombreSolicitante"));
            data.put("apellidoSolicitante", rs.getString("apellidoSolicitante"));
            data.put("numResolucionSolicitante", rs.getString("numResolucionSolicitante"));
            data.put("fechaResolucion", rs.getDate("fechaResolucion"));
            data.put("numMatricula", rs.getString("numMatricula"));            
            data.put("numRadicacion", rs.getString("numRadicacion"));   
            data.put("numeroRadicacion", rs.getString("numRadicacion"));
            data.put("numCedulaSolicitante", document); 
            data.put("clases", generarTextoClasesByDocumento(document));
        }

        return data;
    }
    
    public Map<String, Object> getDataResolucionPrimerVez(String document) throws SQLException{
        Map<String, Object> data = new HashMap<String, Object>();
        
        String query = "SELECT t.nombres AS 'nombreSolicitante', "
                + "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido) AS 'apellidoSolicitante', "
                + "sr.radicado AS 'numeroRadicacion', "
                + "r.codigo AS 'numeroResolucion', s.folios AS 'numeroFolios', "
                + "s.acta AS 'numeroActa', s.consejero AS 'nombreConsejero', "
                + "CAST(r.fecha AS DATE) AS 'fechaExpedicion' "
                + "FROM solicitud_radicado sr "
                + "INNER JOIN tecnico t ON t.documento = sr.documento "
                + "INNER JOIN resolucion r on r.numero = sr.documento "
                + "INNER JOIN solicitud s on s.radicado = sr.solicitud_id "
                + "WHERE sr.documento = '" + document + "'";
        logger.debug("Ejecutando query consunta datos Resolucion Primer Vez {}", query);
        Connection c = ds.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            data.put("nombreSolicitante", rs.getString("nombreSolicitante"));
            data.put("apellidoSolicitante", rs.getString("apellidoSolicitante"));
            data.put("numeroRadicacion", rs.getString("numeroRadicacion"));
            data.put("numeroResolucion", rs.getString("numeroResolucion"));
            data.put("numeroFolios", rs.getString("numeroFolios"));
            data.put("numeroActa", rs.getString("numeroActa"));
            data.put("nombreConsejero", rs.getString("nombreConsejero"));
            data.put("fechaExpedicion", rs.getDate("fechaExpedicion"));
            data.put("numeroCedula", document); 
            data.put("clasesDobles", generarTextoClasesByDocumento(document));
        }

        return data;
    }
    
    public Map<String, Object> getDataResolucionAmpliacionAprobada(String document) throws SQLException{
        Map<String, Object> data = new HashMap<String, Object>();
        
        String query = "SELECT t.nombres AS 'nombreSolicitante', "
                + "CONCAT_WS(' ', t.primer_apellido, t.segundo_apellido) AS 'apellidoSolicitante', "
                + "sr.radicado AS 'numeroRadicacion', "
                + "r.codigo AS 'numeroResolucion', s.folios AS 'numeroFolios', "
                + "s.acta AS 'numeroActa', s.consejero AS 'nombreConsejero', "
                + "CAST(r.fecha AS DATE) AS 'fechaExpedicion' "
                + "FROM solicitud_radicado sr "
                + "INNER JOIN tecnico t ON t.documento = sr.documento "
                + "INNER JOIN resolucion r on r.numero = sr.documento "
                + "INNER JOIN solicitud s on s.radicado = sr.solicitud_id "
                + "WHERE sr.documento = '" + document + "'";
        logger.debug("Ejecutando query consunta datos Resolucion Primer Vez {}", query);
        Connection c = ds.getConnection();
        Statement s = c.createStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            data.put("nombreSolicitante", rs.getString("nombreSolicitante"));
            data.put("apellidoSolicitante", rs.getString("apellidoSolicitante"));
            data.put("numeroRadicacion", rs.getString("numeroRadicacion"));
            data.put("numeroResolucion", rs.getString("numeroResolucion"));
            data.put("numeroFolios", rs.getString("numeroFolios"));
            data.put("numeroActa", rs.getString("numeroActa"));
            data.put("nombreConsejero", rs.getString("nombreConsejero"));
            data.put("fechaExpedicion", rs.getDate("fechaExpedicion"));
            data.put("numeroCedula", document); 
            data.put("clasesDobles", generarTextoClasesByDocumento(document));
        }

        return data;
    }
    
    public String getParameter(String name){
        String parameter = null;
        try {
            String query = "SELECT valor FROM parametro WHERE nombre = '" + name + "'";
            logger.debug("Ejecutando query consunta parametro {}", query);
            Connection c = ds.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            while(rs.next()){
                parameter = rs.getString("valor");
            }
        } catch(SQLException ex) {
            logger.error("NO se ha podido consultar parametro {}", ex.getMessage());
        } 
        return parameter;
    }

    public void actualizarConsejero(String solicitudId, String consejero) {
        try {
            String query;
            query = "update solicitud set consejero = '" + consejero
                    + "' where radicado = '" + solicitudId + "'";
            System.out.println("EJECUTANDO QUERY => " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            logger.error("Se presento un error al actualizar consejero {}", e.getMessage());
        }
    }

    public Map<String, String> obtenerLDepartamentos() {
        Map<String, String> formatos = new HashMap<String, String>();
        try {
            String query = "select codigo, nombre from departamento;";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                formatos.put(rs.getString(1), rs.getString(2));
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
        return formatos;
    }

    public Map<String, String> obtenerLCiudades() {
        Map<String, String> formatos = new HashMap<String, String>();
        try {
            String query = "select codigo, nombre from ciudad;";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                formatos.put(rs.getString(1), rs.getString(2));
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            logger.error("Se presento un error obteniendo el listado de ciudades {}", 
                    e.getMessage());            
        }
        return formatos;
    }

    public Map<String, String> obtenerTDocumentos() {
        Map<String, String> formatos = new HashMap<String, String>();
        try {
            String query = "select codigo, nombre from tipo_documento;";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                formatos.put(rs.getString(1), rs.getString(2));
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            logger.error("Se presento un error obteniendo el listado de tipos de documento {}", 
                    e.getMessage());            
        }
        return formatos;
    }

    public Map<String, String> obtenerFormatos() {
        Map<String, String> formatos = new HashMap<String, String>();
        try {
            String query = "select fe.codigo, ta.nombre "
                    + "from formato_estudio fe "
                    + "inner join titulo_academico ta on fe.c_titulo = ta.codigo "
                    + "where fe.estado = 1 "
                    + "ORDER BY 2";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                formatos.put(rs.getString(1), rs.getString(2));
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            logger.error("Se presento un error obteniendo el listado de formatos {}", 
                    e.getMessage());
        }
        return formatos;
    }

    public void actualizarActaFolio(String acta, String folio,
            String solicitudId) {
        try {            
            folio = (folio == null || folio.isEmpty()) ? "0" : folio;
            String query = "update solicitud set acta = '" + acta + "', folios = "
                    + folio + " where radicado = '" + solicitudId + "'";
            System.out.println("EJECUTANDO QUERY => " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            logger.error("Se presento un error al actualizar el acta y el folio {}", 
                    e.getMessage());            
        }
    }

    public void asignaProcesosAUsuario(String usuario, String nit) {
        try {
            String query;
            if (consultaAsignacionProcesos(nit).length() == 0) {
                query = "insert into asignaciones(usuario, nit) values('"
                        + usuario + "', '" + nit + "');";
            } else {
                query = "update asignaciones set usuario = '" + usuario
                        + "' where nit = '" + nit + "';";
            }
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate(query);
            closeResources(con);            
        } catch (SQLException e) {
            logger.error("Se presento un error {}", 
                    e.getMessage());         
        }
    }

    public void actualizarNumeroGuiaTramite(String radicado, String guia,
            String tramite) {
        try {
            String query;
            query = "update solicitud_radicado set numero_guia = '" + guia
                    + "', tramite = '" + tramite + "' where radicado = '"
                    + radicado + "'";
            logger.debug("EJECUTANDO QUERY {} ", query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate(query);            
            closeResources(con);
        } catch (SQLException e) {
            logger.error("Se presento un error al actualizar numero de guia", e.getMessage());            
        }
    }

    public void agregarEnvioDuplicado(String radicado, String nombres,
            String apellidos, String celular, String direccion, String ciudad,
            String tramite, String guia) {
        try {
            String query;
            query = "insert into `envio_duplicado` values (null,'" + radicado
                    + "','" + nombres + "'," + "'" + apellidos + "','"
                    + celular + "','" + direccion + "','" + ciudad + "'," + "'"
                    + tramite + "','" + guia + "')";
            logger.debug("EJECUTANDO QUERY {}", query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ps.executeUpdate(query);            
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error en la insertando en envio_duplicado");
            e.printStackTrace();
        }
    }

    public void actualizarMatriculaEnTecnico(String documento, long matricula) {
        try {
            String query;
            query = "UPDATE tecnico SET fecha_expedicion = CURDATE(), `matricula` = '"
                    + matricula + "' where documento = '" + documento + "'";
            System.out.println("EJECUTANDO QUERY MATRICULA=> " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de actualizacion de matricula en la tabla tecnico es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
    }

    public void actualizarMatricula(String documento, long matricula) {
        try {
            String query = "";
            query = "update solicitud_radicado set matricula = '" + matricula
                    + "' where documento = '" + documento + "'";
            System.out.println("EJECUTANDO QUERY MATRICULA => " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs + " se actualiza ahora la tabla tecnico");
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
        actualizarMatriculaEnTecnico(documento, matricula);
    }

    /**
     * Usar insertarResolucion
     *
     * @param solicitudId
     * @param resolucion
     * @param documento
     */
    @Deprecated
    public void actualizarResolucion(String solicitudId, long resolucion,
            String documento) {
        try {
            String query = "";

            query = "INSERT INTO resolucion (codigo, `c_solicitud`, fecha, numero) VALUES ("
                    + resolucion
                    + ", "
                    + solicitudId
                    + ", CURDATE(), '"
                    + documento + "')";
            System.out.println("EJECUTANDO QUERY => " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
    }

    public void insertarResolucion(String solicitudId, String fecha,
            long resolucion, String documento) {
        try {
            String query = "";

            query = "INSERT INTO resolucion (codigo, `c_solicitud`, fecha, numero) VALUES ("
                    + resolucion
                    + ", "
                    + solicitudId
                    + ",'"
                    + fecha
                    + "', '"
                    + documento + "')";
            System.out.println("EJECUTANDO QUERY => " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
    }

    public String consultaAsignacionProcesos(String nit) {
        String usuario = "";
        try {
            String query = "select usuario as usuario from asignaciones where nit = '"
                    + nit + "';";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                usuario = rs.getString(1);
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
        return usuario;
    }

    public void actualizaEstadoProceso(String numeroFormulario, String estado) {
        try {
            String query = "update solicitud set c_estado = " + estado
                    + " where radicado = " + numeroFormulario;
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de actualizacion del estado del proceso es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo actualizaEstadoProceso");
            e.printStackTrace();
        }
    }

    public BigDecimal obtieneValorAPagarxSolicitud(int tipoSolicitud) {
        BigDecimal valorAPagar = null;
        try {
            String query = "select (p.valor * s.porcentaje) as valor from parametro p, tipo_solicitud s where p.nombre = 'SMMLV' and s.codigo = "
                    + tipoSolicitud;
            Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            // ps.setInt(0, tipoSolicitud);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                valorAPagar = rs.getBigDecimal("valor");
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el valor a pagar segun el tipo de solicitud.");
            e.printStackTrace();
        }
        return valorAPagar;
    }

    public int obtieneTipoSolicitud(String numeroFormulario) {
        int tipoSolicitud = -1;
        try {
            String query = "select c_tipo as c_tipo from solicitud where radicado = "
                    + numeroFormulario;
            Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tipoSolicitud = rs.getInt("c_tipo");
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el tipo de solicitud del formulario.");
            e.printStackTrace();
        }
        return tipoSolicitud;
    }

    public BigDecimal compruebaPago(String numeroFormulario,
            String numeroDocumento) {
        BigDecimal pagoRealizado = null;
        try {
            String query = "select sum(valor) as valor from pago where c_solicitud = '"
                    + numeroFormulario
                    + "' and documento = '"
                    + numeroDocumento + "'";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            System.out.println("\n\n\nEjecutando query =>" + query
                    + "con los parametros " + numeroFormulario + " y "
                    + numeroDocumento);
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                pagoRealizado = rs.getBigDecimal("valor");
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        }
        return pagoRealizado;
    }

    public BigDecimal compruebaPago_(String numeroFormulario,
            String numeroDocumento) {
        BigDecimal pagoRealizado = null;
        try {
            String query = "select sum(valor) as valor from pago_ where documento = '"
                    + numeroDocumento + "'";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
			// ps.setString(0, numeroFormulario);
            // ps.setString(1, numeroDocumento);
            System.out.println("\n\n\nEjecutando query =>" + query
                    + "con los parametros " + numeroFormulario + " y "
                    + numeroDocumento);
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                pagoRealizado = rs.getBigDecimal("valor");
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        }
        return pagoRealizado;
    }

    public boolean moverAPago(String numeroFormulario, String numeroDocumento) {
        boolean valido = true;
        try {
            String query = "select codigo, valor, documento, fecha_consignacion, fecha_ingreso from pago_ where documento = '"
                    + numeroDocumento + "'";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            System.out.println("\n\n\nEjecutando query =>" + query
                    + "con los parametros " + numeroFormulario + " y "
                    + numeroDocumento);
            ResultSet rs = ps.executeQuery(query);
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String valor = rs.getString("valor");
                String documento = rs.getString("documento");
                String fecha_consignacion = rs.getString("fecha_consignacion");
                String fecha_ingreso = rs.getString("fecha_ingreso");
                query = "insert into pago (c_solicitud, valor, documento, fecha_consignacion, fecha_ingreso) values ("
                        + numeroFormulario
                        + ", "
                        + valor
                        + ", '"
                        + documento
                        + "', '"
                        + fecha_consignacion
                        + "', '"
                        + fecha_ingreso
                        + "';";
                ps.executeUpdate(query);
                query = "delete from pago_ where codigo = " + codigo;
                ps.execute(query);
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        }
        return valido;
    }

    public String obtieneResolucionRechazo() {
        String resolucionRechazo = "";
        try {
            Connection con = ds.getConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select seq('resolucion');");
            while (rs.next()) {
                resolucionRechazo = rs.getString(1);
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error generando el consecutivo de resolucion de rechazo.");
            e.printStackTrace();
        }
        return resolucionRechazo;
    }

    public int getSequence(String name) {
        int sequence = 0;
        String sequenceDate = "";
        try {
            Connection con = ds.getConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select seq('" + name + "');");
            while (rs.next()) {
                sequenceDate = rs.getString(1);
            }
            sequence = Integer.parseInt(sequenceDate.split(",")[0]);
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error generando el consecutivo de "
                            + name);
            e.printStackTrace();
        }
        return sequence;
    }

    public String obtieneRadicadoP() {
        String resolucionRechazo = "";
        try {
            Connection con = ds.getConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select seq('expediente');");
            while (rs.next()) {
                resolucionRechazo = rs.getString(1);
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error generando el consecutivo de radicado.");
            e.printStackTrace();
        }
        return resolucionRechazo;
    }

    public String obtieneRadicado() {
        String resolucionRechazo = "";
        try {
            Connection con = ds.getConnection();
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("select seq('radicado');");
            while (rs.next()) {
                resolucionRechazo = rs.getString(1);
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error generando el consecutivo de radicado.");
            e.printStackTrace();
        }
        return resolucionRechazo;
    }

    public int verificarEvaluacion(long solicitud_id) {
        int total = 0;
        try {
            String query = "SELECT COUNT(*) total FROM evaluacion WHERE solicitud_id = ?";
            Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setLong(0, solicitud_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt("total");
            }
            closeResources(con, rs);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        }
        return total;
    }

    public String getRadicado(String documento) {
        String valores = "";
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT concat(radicado, ',', fecha) as valores FROM solicitud_radicado WHERE documento = '"
                    + documento + "'";
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                valores = rs.getString("valores");
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return valores;

    }

    public long getMatricula(String documento) {
        long matricula = 0;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT matricula FROM solicitud_radicado  WHERE documento = '"
                    + documento + "'";
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                matricula = rs.getLong("matricula");
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return matricula;

    }

    public long getResolucion(String solicitudId) {
        long resolucion = 0;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT codigo as resolucion FROM resolucion WHERE c_solicitud = '"
                    + solicitudId + "'";
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resolucion = rs.getLong("resolucion");
            }
        } catch (SQLException e) {
            System.out.println("Se presento un error obteniendo ");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return resolucion;

    }

    public long getResolucionByDocumento(String documento) {
        long resolucion = 0;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT codigo as resolucion FROM resolucion WHERE numero = '"
                    + documento + "'";
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resolucion = rs.getLong("resolucion");
            }
        } catch (SQLException e) {
            System.out.println("Se presento un error obteniendo ");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return resolucion;

    }

    public Date getFechaResolucion(String solicitudId) {
        Date resolucion = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT fecha FROM resolucion WHERE c_solicitud = '"
                    + solicitudId + "'";
            System.out.println("Ejecutando query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resolucion = rs.getDate("fecha");
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return resolucion;
    }

    public Date getFechaMatricula(String documento) {
        Date resolucion = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT fecha_expedicion FROM tecnico WHERE documento = '"
                    + documento + "'";
            System.out.println("Ejecutando query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resolucion = rs.getDate("fecha_expedicion");
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return resolucion;
    }

    public Date getFechaResolucionByDocumento(String documento) {
        Date resolucion = null;
        Connection con = null;
        ResultSet rs = null;
        try {
            String query = "SELECT fecha FROM resolucion WHERE numero = '"
                    + documento + "'";
            System.out.println("Ejecutando query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resolucion = rs.getDate("fecha");
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo la referencia del pago realizado.");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return resolucion;

    }

    public void insertarRadicado(String solicitud_id, String radicado,
            String fecha, String documento) {
        try {
            String query = "INSERT INTO `solicitud_radicado`(`solicitud_id`, `radicado`, `fecha`, `documento`) VALUES ("
                    + solicitud_id
                    + ","
                    + radicado
                    + ", curdate(), '"
                    + documento + "')";
            System.out.println("Insertando radicado " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
    }

    public void actualizaFechaRadicado(String solicitud_id, String fecha) {
        try {
            String query = "update solicitud set fecha_radicacion = '" + fecha
                    + "' where radicado = " + solicitud_id;
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de actualizacion del estado del proceso es: "
                            + rs);

        } catch (SQLException e) {
            System.out
                    .println("Se presento un error EN actualizaFechaRadicado");
            e.printStackTrace();
        }
    }

    public List<Evaluacion> getClassActivityItem(String solicitudId) {
        String query = "SELECT  cg.`codigo` AS class_id , ag.`codigo` AS activity,  cg.`nombre` AS clase, ag.`nombre` AS actividad, i.`name` AS item, i.id AS item_id,"
                + "("
                + "SELECT  COUNT(cg.`codigo`)  FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`solicitud_id` = "
                + solicitudId
                + " AND cg.`codigo` = class_id "
                + ") AS total_class_id, "
                + "("
                + "SELECT  COUNT(cg.`codigo`)  FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`activity_id` = activity AND e.`solicitud_id` = "
                + solicitudId
                + " AND cg.`codigo` = class_id "
                + ") AS total_activity_id "
                + "FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "INNER JOIN `item` i ON i.`id` = e.`item_id` "
                + "WHERE e.`solicitud_id` = "
                + solicitudId
                + " ORDER BY cg.`codigo`, ag.`codigo`, i.`id`, i.`id` ";

        Connection con = null;
        ResultSet rs = null;
        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        try {
            con = ds.getConnection();

            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            Evaluacion evaluacion;

            int conta = 0;
            while (rs.next()) {
                evaluacion = new Evaluacion();
                evaluacion.setActivityId(rs.getInt("activity"));
                evaluacion.setActivityName(rs.getString("actividad")
                        .toLowerCase());
                evaluacion.setClassId(rs.getInt("class_id"));
                evaluacion.setClassName(rs.getString("clase"));
                evaluacion.setItemName(rs.getString("item").toLowerCase());
                evaluacion.setSumActivity(rs.getInt("total_activity_id"));
                evaluacion.setSumClass(rs.getInt("total_class_id"));
                evaluacion.setItemId(rs.getInt("item_id"));
                // if(evaluacion.getClassId()==1){
                conta++;
                System.out.println(conta + " " + evaluacion.getClassId() + "-"
                        + +evaluacion.getActivityId() + "-"
                        + evaluacion.getItemName());
				// }
                // System.out.println(evaluacion);
                evaluaciones.add(evaluacion);

            }
            // Collections.sort(evaluaciones, new EvaluacionComparator());
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el resultado de la evaluacion");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return evaluaciones;
    }

    public List<Evaluacion> getClassActivityItemByDocumento(String documento) {
        String query = "SELECT  cg.`codigo` AS class_id , ag.`codigo` AS activity,  cg.`nombre` AS clase, ag.`nombre` AS actividad, i.`name` AS item, i.id AS item_id,"
                + "("
                + "SELECT  COUNT(cg.`codigo`)  FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`documento` = '"
                + documento
                + "' AND cg.`codigo` = class_id "
                + ") AS total_class_id, "
                + "("
                + "SELECT  COUNT(cg.`codigo`)  FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "WHERE e.`activity_id` = activity AND e.`documento` = '"
                + documento
                + "' AND cg.`codigo` = class_id "
                + ") AS total_activity_id "
                + "FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON ag.`codigo` = e.`activity_id` "
                + "INNER JOIN `clase_generica` cg ON ag.`c_clase` = cg.`codigo` "
                + "INNER JOIN `item` i ON i.`id` = e.`item_id` "
                + "WHERE e.`documento` = '"
                + documento
                + "' ORDER BY cg.`codigo`, i.`id`, ag.`codigo`";

        Connection con = null;
        ResultSet rs = null;
        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        try {
            System.out.println("Ejecutando Query " + query);
            //con = ds.getConnection();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            con
                    = DriverManager.getConnection("jdbc:mysql://localhost/sgm_conte",
                            "root", "");
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            Evaluacion evaluacion;

            int conta = 0;
            while (rs.next()) {
                evaluacion = new Evaluacion();
                evaluacion.setActivityId(rs.getInt("activity"));
                evaluacion.setActivityName(rs.getString("actividad")
                        .toLowerCase());
                evaluacion.setClassId(rs.getInt("class_id"));
                evaluacion.setClassName(rs.getString("clase"));
                evaluacion.setItemName(rs.getString("item").toLowerCase());
                evaluacion.setSumActivity(rs.getInt("total_activity_id"));
                evaluacion.setSumClass(rs.getInt("total_class_id"));
                evaluacion.setItemId(rs.getInt("item_id"));

                conta++;
                System.out.println(conta + " " + evaluacion.getClassId() + "-"
                        + evaluacion.getActivityId() + "-"
                        + evaluacion.getItemName());

                evaluaciones.add(evaluacion);

            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el resultado de la evaluacion");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return evaluaciones;
    }

    public String getClasesIniciales(String documento) {
        String clases = "";
        String query = "SELECT DISTINCT(cg.nombre), (e.`solicitud_id`) as fecha FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN clase_generica cg ON cg.`codigo` = ag.`c_clase` "
                + "WHERE e.documento = "
                + documento
                + " ORDER BY e.`solicitud_id`";

        Connection con = null;
        ResultSet rs = null;
        try {
            System.out.println("Ejecutando Query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            String fecha = "";
            String fechaAnt = "";
            while (rs.next()) {
                fecha = rs.getString("fecha");
                if (fechaAnt.equals("")) {
                    fechaAnt = fecha;
                }
                if (fecha.equalsIgnoreCase(fechaAnt)) {
                    clases = clases + rs.getString("nombre") + ",";
                } else {
                    break;
                }
                fechaAnt = fecha;

            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el resultado de la evaluacion");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return clases;
    }

    public String getClasesNuevas(String solicitudId) {
        String clases = "";
        String query = "SELECT DISTINCT(cg.nombre), `fecha` FROM `evaluacion` e "
                + "INNER JOIN `actividad_generica` ag ON e.`activity_id` = ag.`codigo` "
                + "INNER JOIN clase_generica cg ON cg.`codigo` = ag.`c_clase` "
                + "WHERE e.`solicitud_id` = "
                + solicitudId
                + " ORDER BY e.`fecha` DESC";

        Connection con = null;
        ResultSet rs = null;
        try {
            System.out.println("Ejecutando Query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            String fecha = "";
            String fechaAnt = "";
            while (rs.next()) {
                fecha = rs.getString("fecha");
                if (fechaAnt.equals("")) {
                    fechaAnt = fecha;
                }
                if (fecha.equalsIgnoreCase(fechaAnt)) {
                    clases = clases + rs.getString("nombre") + ",";
                } else {
                    break;
                }
                fechaAnt = fecha;

            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el resultado de la evaluacion");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }
        return clases;
    }

    public Asociacion getAsociacionBySigla(String sigla) {
        Asociacion asociacion = new Asociacion();
        String clases;
        String query = "SELECT a.`direccion`, a.`presidente`, a.`telefono`, c.nombre ciudad, d.nombre departamento "
                + "FROM asociacion a INNER JOIN  ciudad c ON a.`c_ciudad` = c.`codigo` INNER JOIN departamento d ON c.`c_depto` = d.`codigo` "
                + "WHERE sigla = '" + sigla + "'";

        Connection con = null;
        ResultSet rs = null;
        try {
            System.out.println("Ejecutando Query " + query);
            con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            String fecha = "";
            String fechaAnt = "";
            while (rs.next()) {
                asociacion.setCiudad(rs.getString("ciudad"));
                asociacion.setDepartamento(rs.getString("departamento"));
                asociacion.setDireccion(rs.getString("direccion"));
                asociacion.setPresidente(rs.getString("presidente"));
                asociacion.setTelefono(rs.getString("telefono"));
                asociacion.setSigla(sigla);
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error obteniendo el resultado de la evaluacion");
            e.printStackTrace();
        } finally {
            try {
                closeResources(con, rs);
            } catch (SQLException e) {
                System.out
                        .println("Se presento un error al cerrar los recursos");
                e.printStackTrace();
            }
        }

        return asociacion;

    }

    /**
     * El m�todo solo trae las clases por solicitud pero no por documento
     *
     * @param solicitud
     * @return
     */
    @Deprecated
    public String generarTextoClases(String solicitud) {
        String r = "";
        List<String> resultado = new ArrayList<String>();
        List<Evaluacion> evaluaciones = getClassActivityItem(solicitud);
        int cantidadClases = 0;
        int cantidadActividades = 0;
        boolean inicia = true;
        String result = "";
        for (Evaluacion e : evaluaciones) {

            if (cantidadClases < 1) {
                inicia = true;
                resultado.add(result);
            } else {
                inicia = false;
                if (cantidadActividades <= 0) {
                    cantidadActividades = e.getSumActivity();
                }
            }

            if (inicia) {
                if (e.getClassId() == 7) {
                    result = "\n\n"
                            + e.getClassName()
                            + "\n\n"
                            + "Para adelantar actividades y labores relacionadas "
                            + "con el estudio y las aplicaciones de la electricidad en cuyo "
                            + "ejercicio requiere la direcci�n, coordinaci�n y responsabilidad "
                            + "de Ingenieros Electricistas.";
                    continue;
                } else {
                    result = "\n\n"
                            + e.getClassName()
                            + "\n\n"
                            + "Para adelantar actividades relacionadas con el estudio aplicado al ";
                    cantidadClases = e.getSumClass();
                    cantidadActividades = e.getSumActivity();
                }

            }

            if (cantidadClases == 1) {
                result = result + " as� como al ";
            }

            if (cantidadActividades > 0) {
                if (cantidadActividades <= 1) {
                    result = result + " " + e.getItemName() + " de "
                            + e.getActivityName()
                            + (cantidadClases <= 1 ? "; " : ".");
                } else {
                    result = result + " " + e.getItemName() + ", ";
                }
                cantidadActividades--;
            }
            --cantidadClases;
        }
        resultado.add(result);

        for (String s : resultado) {
            r = r + s + "\n\n";
        }

        return r;
    }

    public String generarTextoClasesByDocumento(String documento) {
        String r = "";
        List<String> resultado = new ArrayList<String>();
        List<Evaluacion> evaluaciones = getClassActivityItemByDocumento(documento);
        int cantidadClases = 0;
        int cantidadActividades = 0;
        boolean inicia = true;
        String result = "";
        String itemAnterior = "";
        String itemActual = "";
        boolean itemEnParrafo = false;
        for (Evaluacion e : evaluaciones) {
            if (cantidadClases < 1) {
                inicia = true;
                itemAnterior = e.getItemName();
                itemActual = e.getItemName();
                resultado.add(result);
            } else {
                inicia = false;
                itemAnterior = itemActual;
                itemActual = e.getItemName();
                if (cantidadActividades <= 0) {
                    cantidadActividades = e.getSumActivity();
                }
            }

            if (inicia) {
                if (e.getClassId() == 7) {
                    result = "\n\n"
                            + e.getClassName()
                            + "\n\n"
                            + "Para adelantar actividades y labores relacionadas "
                            + "con el estudio y las aplicaciones de la electricidad en cuyo "
                            + "ejercicio requiere la direcci�n, coordinaci�n y responsabilidad "
                            + "de Ingenieros Electricistas.";
                    continue;
                } else {
                    result = "\n\n"
                            + e.getClassName()
                            + "\n\n"
                            + "Para adelantar actividades relacionadas con el estudio aplicado al ";
                    cantidadClases = e.getSumClass();
                    cantidadActividades = e.getSumActivity();
                    itemEnParrafo = false;
                }

            }

            if (cantidadClases == 1) {
                if (!itemActual.equalsIgnoreCase(itemAnterior)) {
                    result = result + " as� como al ";
                }
            }

            if (e.getClassId() == 1) {
                if (itemActual.equalsIgnoreCase(itemAnterior)) {
                    result = result
                            + (itemEnParrafo ? ", "
                            : e.getItemName()
                            + " de circuitos el�ctricos de todo tipo de salidas para ")
                            + e.getActivityName()
                            + (cantidadClases <= 1 ? " de instalaciones el�ctricas residenciales y comerciales."
                            : "");
                    itemEnParrafo = true;
                } else {
                    result = result
                            + "; "
                            + e.getItemName()
                            + " de circuitos el�ctricos de todo tipo de salidas para "
                            + e.getActivityName()
                            + (cantidadClases <= 1 ? " de instalaciones el�ctricas residenciales y comerciales."
                            : "");
                }
            } else {
                if (itemActual.equalsIgnoreCase(itemAnterior)) {
                    result = result
                            + (itemEnParrafo ? ", " : e.getItemName() + " de ")
                            + e.getActivityName()
                            + (cantidadClases <= 1 ? "." : "");
                    itemEnParrafo = true;
                } else {
                    result = result + "; " + e.getItemName() + " de "
                            + e.getActivityName()
                            + (cantidadClases <= 1 ? "." : "");
                }
            }

            if (cantidadActividades > 0) {
                if (cantidadActividades <= 1) {
                } else {
                }
                cantidadActividades--;
            }
            --cantidadClases;
        }
        resultado.add(result);

        for (String s : resultado) {
            r = r + s + "\n\n";
        }

        return r;
    }

    public void ingresarPago(String solicitudId, String documento, Date fecha,
            String valor) {
        try {
            String query = "";
            query = "INSERT INTO `pago`(`codigo`,`c_solicitud`,`documento`,`fecha_consignacion`,`fecha_ingreso`,`valor`) "
                    + "VALUES(NULL,'"
                    + solicitudId
                    + "','"
                    + documento
                    + "','"
                    + new SimpleDateFormat("yyyy-MM-dd").format(fecha)
                    + "',curdate(),'" + valor + "')";
            System.out.println("Se ejecuta query de ingreso de pago " + query);
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de ingreso de pago es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }
    }

    private void closeResources(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    private void closeResources(Connection con, ResultSet resultSet)
            throws SQLException {
        closeResources(con);
        if (resultSet != null) {
            resultSet.close();
        }
    }
    
    public BigDecimal compruebaPago2(String solicitudId, String documento) {
        // TODO Auto-generated method stub
        return null;
    }

    public void insertarFechaRadicacion(String solicitudId, String fecha) {
        try {

            String query = "insert into fecha_radicacion(solicitud_id, fecha) values('"
                    + solicitudId + "', '" + fecha + "')";

            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            closeResources(con);
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error consultando el usario asignado para este nit.");
            e.printStackTrace();
        }

    }

    public void insertarFechaEnvioTecnico(String solicitudId, String fecha) {
        try {

            String query = "insert into notificacion(solicitud_id, envio) values('"
                    + solicitudId + "', '" + fecha + "')";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error insertando notificacion para la solicitud "
                            + solicitudId);
            e.printStackTrace();
        }
    }

    public void actualizarFechaRecepcionTecnico(String solicitudId, String fecha) {

        try {

            String query = "update notificacion set recepcion = '" + fecha
                    + "' where solicitud_id ='" + solicitudId + "'";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitudId);
            e.printStackTrace();
        }

    }

    public void insertarAviso(String solicitud, String aviso) {

        try {

            String query = "insert into aviso(solicitud_id, tipo) values('"
                    + solicitud + "', '" + aviso + "')";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de asignacion es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitud);
            e.printStackTrace();
        }

    }

    public void insertarRecurso(String solicitud, String fecha, String tipo) {
        try {

            String query = "insert into recurso(solicitud_id, creado, tipo) values('"
                    + solicitud + "', '" + fecha + "','" + tipo + "')";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de recurso es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitud);
            e.printStackTrace();
        }
    }

    public void actualizarRecurso(String solicitudId, String estado) {
        try {

            String query = "update recurso set estado = '" + estado
                    + "' where solicitud_id =" + solicitudId;
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de recurso es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitudId);
            e.printStackTrace();
        }

    }

    public void insertarInadmitido(String solicitud, String causa, String fecha) {
        try {

            String query = "insert into inadmitido (solicitud_id, causa,fecha_notificacion) values ("
                    + solicitud + ",'" + causa + "','" + fecha + "')";
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de recurso es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitud);
            e.printStackTrace();
        }
    }

    public void actualizarInadmitido(String solicitud, String fecha) {
        try {

            String query = "update inadmitido set fecha fecha_responde = '"
                    + fecha + "' where solicitud_id = " + solicitud;
            Connection con = ds.getConnection();
            Statement ps = con.createStatement();
            int rs = ps.executeUpdate(query);
            System.out
                    .println("El resultado del proceso de insercion o actualizacion de recurso es: "
                            + rs);
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out
                    .println("Se presento un error actualizando notificacion para la solicitud "
                            + solicitud);
            e.printStackTrace();
        }

    }

    public void actualizaPQRS(List<Object> registro, String opcion) {
        try {
            String query = "";
            if (registro.size() == 10) {
                query = "insert into pqrs(fecharecepcion, nombres, apellidos, documento, direccion, c_ciudad, telefono, email, solicitud) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                Connection con = ds.getConnection();
                PreparedStatement ps = con.prepareStatement(query);
                ps.setDate(1, (java.sql.Date) registro.get(1));
                ps.setString(2, (String) registro.get(2));
                ps.setString(3, (String) registro.get(3));
                ps.setString(4, (String) registro.get(4));
                ps.setString(5, (String) registro.get(5));
                ps.setString(6, (String) registro.get(6));
                ps.setString(7, (String) registro.get(7));
                ps.setString(8, (String) registro.get(8));
                ps.setString(9, (String) registro.get(9));
                System.out.println("El resultado del proceso de insercion es: "
                        + ps.executeUpdate());
                if (con != null) {
                    con.close();
                }
            } else if (registro.size() == 2) {
                if (opcion.contains("A")) {
                    query = "update pqrs set c_responsable = ? where consecutivo = ?";
                    Connection con = ds.getConnection();
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setString(1, (String) registro.get(0));
                    ps.setString(2, (String) registro.get(1));
                    System.out
                            .println("El resultado del proceso de actualizacion es: "
                                    + ps.executeUpdate());
                    if (con != null) {
                        con.close();
                    }
                } else if (opcion.contains("R")) {
                    query = "update pqrs set fecharespuesta = ? where consecutivo = ?";
                    Connection con = ds.getConnection();
                    PreparedStatement ps = con.prepareStatement(query);
                    java.sql.Date fecha = new java.sql.Date(
                            ((Date) registro.get(1)).getTime());
                    ps.setDate(1, fecha);
                    ps.setString(2, (String) registro.get(0));
                    System.out
                            .println("El resultado del proceso de actualizacion es: "
                                    + ps.executeUpdate());
                    if (con != null) {
                        con.close();
                    }
                } else if (opcion.contains("NA")) {
                    query = "update pqrs set fecharespuesta = ? where consecutivo = ?";
                    Connection con = ds.getConnection();
                    PreparedStatement ps = con.prepareStatement(query);
                    java.sql.Date fecha = new java.sql.Date(
                            ((Date) registro.get(1)).getTime());
                    ps.setDate(1, fecha);
                    ps.setString(2, (String) registro.get(0));
                    System.out
                            .println("El resultado del proceso de actualizacion es: "
                                    + ps.executeUpdate());
                    if (con != null) {
                        con.close();
                    }
                } else if (opcion.contains("C")) {
                    query = "update pqrs set fechacierre = ? where consecutivo = ?";
                    Connection con = ds.getConnection();
                    PreparedStatement ps = con.prepareStatement(query);
                    java.sql.Date fecha = new java.sql.Date(
                            ((Date) registro.get(1)).getTime());
                    ps.setDate(1, fecha);
                    ps.setString(2, (String) registro.get(0));
                    System.out
                            .println("El resultado del proceso de actualizacion es: "
                                    + ps.executeUpdate());
                    if (con != null) {
                        con.close();
                    }
                }
            }

        } catch (SQLException e) {
            System.out
                    .println("Se presento un error contactando a la tabla pqrs.");
            e.printStackTrace();
        }
    }

    public Conexion(boolean b) {
    }

    public static void main(String args[]) throws IOException {
        //generarResolucionPrimerVezDesdeArchivo();
        Conexion c = new Conexion();
        System.out.print(c.generarTextoClasesByDocumento("1085250822"));

    }

    public static void generarResolucionAmpliacionAprobadaDesdeArchivo()
            throws IOException {
        File archivo = new File("C:\\tmp\\reso4.csv");
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);

        String linea;
        int clinea = 0;
        String tareaA = "";
        while ((linea = br.readLine()) != null) {
            String[] tmp = linea.split(";");
            String directorioAlfresco = "CONTE/" + tmp[2] + " " + tmp[7] + " "
                    + tmp[3] + " " + tmp[4].trim() + "/Documentos operativos";
            String resolucion = tmp[0];
            Conexion conn = new Conexion(true);
            System.out.println(directorioAlfresco);

            ConexionBase conexion = new ConexionBase();
            GeneracionBase generador2 = new GeneracionBase();
            generador2.parametros = new HashMap<String, Object>();
            generador2.setConsecutivo(tmp[2]);
            Date fecha = new Date();
            fecha.setDate(15);
            generador2.parametros.put("numeroResolucion", resolucion);
            generador2.parametros.put("fechaExpedicion", fecha);// verificar
            // cual es la
            // fecha de
            // expedicion
            generador2.parametros.put("numeroRadicacion", tmp[2]);
            generador2.parametros.put("nombreSolicitante", tmp[3]);
            generador2.parametros.put("apellidoSolicitante", tmp[4]);
            generador2.parametros.put("numeroFolios", tmp[5]);// (String)
            // arg0.getVariable("qswfr_folios"));//
            generador2.parametros.put("numeroActa", tmp[6]);
            generador2.parametros.put("numeroCedula", tmp[7]);
            generador2.parametros.put("nombreConsejero", tmp[8]);

            generador2.parametros.put("clasesDobles",
                    conn.generarTextoClasesByDocumento(tmp[7]));
            generador2.parametros.put("fechaSolicitud", new Date());
            generador2.setNombreFormato("resolucionAmpliacionAprobada");
            generador2
                    .setPlantillaFormato(GeneracionAutoApertura.class
                            .getResourceAsStream("resolucionAmpliacionAprobada.jasper"));

            generador2.setExtension("docx");
            generador2.generaFormato();

            conexion.setRutaArchivo(generador2.getRutaArchivo());
            conexion.setNombreArchivo(generador2.getNombreArchivo());
            conexion.setTipoArchivo("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            conexion.setDirectorioAlfresco(directorioAlfresco);
            conexion.subeArchivo();
        }
    }    
}