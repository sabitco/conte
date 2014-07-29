package com.conte.reportEngine;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.conte.common.service.ReportDAO;
import com.conte.reportEngine.exception.ReportExecutionException;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Window;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.conte.reportEngine.config.Constantes.*;

/**
 * Motor/Servicio de reportes y consultas multiformato (web, y batch)
 *
 * @author pedrorozo - http://www.smartjsp.com
 */
public class ReportService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private List<ConfigCampoVO> configCampos = null;
  private String campoOrdenar = null;
  private String filtro = null;
  private String query = null;
  private String formatoSalida = null;
  private String mimeType = null;
  private Connection conexion = null;
  private ResultSet resultados = null;
  private int maximoResultados = 0;
  private ArrayList<Map<String, ?>> coleccion = null;   // cuando recibe una coleccion de beans
  private ByteArrayOutputStream out = null;
  private ReportDAO reportDAO = null;
  private JasperReport jr = null;
  private JasperPrint jasperPrint = null;
  private String archivoSalida = null;
  private String titulo = "";
  private String subtitulo = "";
  private int paginaActual = 0;
  private Object[] args;
  private String fieldDelimiter = ",";

  public ReportService() {
    reportDAO = new ReportDAO();
  }

  public QueryContainer creaConsultaWeb20(Application app, Window w) {
    ConsultaWeb20 c = new ConsultaWeb20();
    String tituloCompleto = getSubtitulo().length() > 0 ? getTitulo().trim() + " - " + getSubtitulo().trim() : getTitulo().trim();
    c.setFieldDelimiter(fieldDelimiter);
    return c.creaConsulta(app, w, getConfigCampos(), tituloCompleto, query, args);
  }

  public QueryContainer creaConsultaWeb20(Application app, Window w, List<Map<String, Object>> data) {
    ConsultaWeb20 c = new ConsultaWeb20();
    String tituloCompleto = getSubtitulo().length() > 0 ? getTitulo().trim() + " - " + getSubtitulo().trim() : getTitulo().trim();
    c.setFieldDelimiter(fieldDelimiter);
    return c.creaConsulta(app, w, getConfigCampos(), tituloCompleto, data);
  }

  /**
   * Crea la definicion del reporte jasper, luego ejecuta la consulta a traves de la DAO y por ultima pobla el reporte dejandolo listo para conversion
   * al formato destino (objeto JasperPrint)
   *
   * @param tipo
   * @param app
   * @param w
   * @author pedrorozo - http://www.smartjsp.com
   */
  public void creaReporte(String tipo, Application app, Window w) {
    try {
      Style detailStyle = new Style();
      Style headerStyle = new Style();
      Style titleStyle = new Style();
      Style subtitleStyle = new Style();
      Style amountStyle = new Style();
      amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);

      // poblacion de informacion via jdbc normal
//      if (tipo.equalsIgnoreCase("WEB")) {     //usar el helper de vaadin cuando la invoacion es desde web
//        //reportDAO = (ReportDAO) ApplicationHelper.getApplicationContext().getBean("reportDAO");
//        //}
//        //else                                   //usa el proveedor estandar de spring cuando la invoacion es batch
//        //{
//        //ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
//        //reportDAO = (ReportDAO) context.getBean("reportDAO");
//      }

      resultados = reportDAO.consultaBaseDatos(query, args);

      DynamicReportBuilder drb = new DynamicReportBuilder();
      drb.setTitle(getTitulo()) //Titulo
              .setSubtitle(getSubtitulo()) // Subtitulo
              //.setDetailHeight(15)						//altura para cada fila
              //.setMargins(30, 20, 30, 15)					//define margenes (arriba, abajo, izqueirda, derecha
              .setPageSizeAndOrientation(Page.Page_Legal_Landscape()) // tama�o carta apaisado
              .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle) // estilos de secciones
              .setPrintBackgroundOnOddRows(true).setColumnsPerPage(1);

      for (int i = 0; i < getConfigCampos().size(); i++) // iterativamente crea las columnas
      {
        AbstractColumn columnState = ColumnBuilder.getNew().setColumnProperty(getConfigCampos().get(i).getCampo(), String.class.getName()).setTitle(getConfigCampos().get(i).getTitulo()).setWidth(getConfigCampos().get(i).getTamano()).build();
        drb.addColumn(columnState);

      }
      DynamicReport dr = drb.build();	// crea el reporte

      Map<String, Object> parametros = new HashMap<String, Object>();
      JRResultSetDataSource jrRs = new JRResultSetDataSource(resultados);
      jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
      jasperPrint = JasperFillManager.fillReport(jr, parametros, jrRs);  	    //deja la el reporte listo en un objeto JasperPrint generico
      exportaReporte();  //invoca exportacion a formato destino

      if (tipo.equalsIgnoreCase("BATCH")) {   // si es batch genera automaticamente el archivo en el servidor
        generaReporteBatch();
      } else if (tipo.equalsIgnoreCase("WEB")) {
        getStreamWeb(app, w);
      }
    } catch (JRException e) {
      throw new ReportExecutionException(e.getMessage(), e);
    } finally {
      reportDAO.closeConnection();
    }
  }

  /**
   * Crea la definicion del reporte jasper, luego ejecuta la consulta a traves de la DAO y por ultima pobla el reporte desde una coleccionde maps
   * dejandolo listo para conversion al formato destino (objeto JasperPrint)
   *
   * @param tipo
   * @param app
   * @param w
   */
  public void creaReporteObjetos(String tipo, Application app, Window w) {
    try {
      Style detailStyle = new Style();
      Style headerStyle = new Style();
      Style titleStyle = new Style();
      Style subtitleStyle = new Style();
      Style amountStyle = new Style();
      amountStyle.setHorizontalAlign(HorizontalAlign.RIGHT);

      DynamicReportBuilder drb = new DynamicReportBuilder();
      drb.setTitle(getTitulo()) //Titulo
              .setSubtitle(getSubtitulo()) // Subtitulo
              .setPageSizeAndOrientation(Page.Page_Legal_Landscape()) // tama�o carta apaisado
              .setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle) // estilos de secciones
              .setPrintBackgroundOnOddRows(true).setColumnsPerPage(1);

      for (int i = 0; i < getConfigCampos().size(); i++) // iterativamente crea las columnas
      {
        ConfigCampoVO campo = getConfigCampos().get(i);
        int tamano = campo.getTamano();
        int multiplier = (COL_ZOOM * COL_MAX) / 2;
        if (tamano < multiplier) {
          tamano = Math.min(multiplier, tamano * multiplier);
        }
        AbstractColumn columnState = ColumnBuilder.getNew()
                .setColumnProperty(campo.getCampo(), String.class.getName())
                .setTitle(campo.getTitulo())
                .setWidth(tamano)
                .build();
        drb.addColumn(columnState);
      }
      DynamicReport dr = drb.build();	// crea el reporte
      Map<String, Object> parametros = new HashMap<String, Object>();
      if ("XLS".equalsIgnoreCase(getFormatoSalida())
              || "CSV".equalsIgnoreCase(getFormatoSalida())) {
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
      }
      Collection<Map<String, ?>> col = getColeccion();

      JRMapCollectionDataSource jrRs = new JRMapCollectionDataSource(col);
      jr = DynamicJasperHelper.generateJasperReport(dr, new ClassicLayoutManager(), parametros);
      jasperPrint = JasperFillManager.fillReport(jr, parametros, jrRs);  	    //deja el reporte listo en un objeto JasperPrint generico
      exportaReporte();  	    //invoca exportacion a formato destino

      if (tipo.equalsIgnoreCase("BATCH")) {  	    // si es batch genera automaticamente el archivo en el servidor
        generaReporteBatch();
      } else if (tipo.equalsIgnoreCase("WEB")) {
        getStreamWeb(app, w);
      }

    } catch (JRException e) {
      throw new ReportExecutionException(e.getMessage(), e);
    }
  }

  /**
   * Convierte el reporte creado (JasperPrint) al formato deseado (PDF, CSV, HTML,etc) el resultado lo deja en un ByteArrayOutputStream() listo para
   * su envio web o batch;
   *
   * @author pedrorozo - http://www.smartjsp.com
   */
  public void exportaReporte() {

    JRPdfExporter pdfExporta = new JRPdfExporter();
    JRCsvExporter csvExporta = new JRCsvExporter();
    JRHtmlExporter htmlExporta = new JRHtmlExporter();
    JRXmlExporter xmlExporta = new JRXmlExporter();
    JRXlsExporter xlsExporta = new JRXlsExporter();

    try {
      out = new ByteArrayOutputStream();
      if (getFormatoSalida().equalsIgnoreCase("PDF")) {
        mimeType = "application/octet-stream";
        pdfExporta.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        pdfExporta.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        pdfExporta.exportReport();
      } else if (getFormatoSalida().equalsIgnoreCase("CSV")) {
        mimeType = "application/octet-stream";
        csvExporta.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, this.getFieldDelimiter());
        csvExporta.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        csvExporta.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        csvExporta.exportReport();

      } else if (getFormatoSalida().equalsIgnoreCase("HTML")) {
        mimeType = "application/octet-stream";
        htmlExporta.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        htmlExporta.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        htmlExporta.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, false);

        // Ejemplos de paramentros adicionales: m�s en:
        //htmlExporta.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "XXXXXXXXXX");
        //htmlExporta.setParameter(JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR, Boolean.TRUE);
        //htmlExporta.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME, "/tmp/images/");
        //htmlExporta.setParameter(JRHtmlExporterParameter.IMAGES_URI, "images/");
        //htmlExporta.setParameter(JRHtmlExporterParameter.HTML_HEADER, "<table width=\"100%\" cellpadding=\"1\" cellspacing=\"0\" border=\"0\" bgcolor=\"#000000\"><tr><td>");
        //htmlExporta.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "</td></tr></table>");

        //Read more: http://kickjava.com/src/net/sf/jasperreports/ebank/ReportBean.java.htm#ixzz20kmq4VkP

        htmlExporta.exportReport();
      } else if (getFormatoSalida().equalsIgnoreCase("XML")) {
        mimeType = "application/octet-stream";
        xmlExporta.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xmlExporta.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        xmlExporta.exportReport();
      } else if (getFormatoSalida().equalsIgnoreCase("XLS")) {
        mimeType = "application/octet-stream";
        xlsExporta.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xlsExporta.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        xlsExporta.exportReport();
      }
    } catch (JRException e) {
      throw new ReportExecutionException(e.getMessage(), e);
    }
  }

  /**
   * Retorna al navegador de origen como un archivo binario el reporte construido
   *
   * @param app (Aplicacion vaadin)
   * @param w (ventana actual vaadin)
   */
  public void getStreamWeb(Application app, Window w) {
    StreamSource reporte = new ReportSource(new BufferedInputStream(new ByteArrayInputStream(out.toByteArray())));
    StreamResource streamResource = new StreamResource(reporte, archivoSalida, app);
    streamResource.setCacheTime(5000); // no cache (<=0) does not work with IE8
    streamResource.setMIMEType(mimeType);
    streamResource.getStream().setParameter("Content-Length", Integer.toString(out.size()));
    w.open(streamResource, "_top");
  }

  /**
   * Genera un archivo en el servidor para el el reporte construido pro el proceso batch
   */
  public void generaReporteBatch() {
    try {
      ByteArrayInputStream in = new ByteArrayInputStream(getOut().toByteArray());         // Datos provenientes de reporte son enviados a buffer por performance
      BufferedInputStream bEntrada = new BufferedInputStream(in);         // datos antes de enviarse a archivo, son enviados a buffer por performance
      FileOutputStream archivo = new FileOutputStream(new File(getArchivoSalida()));
      BufferedOutputStream bArchivo = new BufferedOutputStream(archivo);
      byte[] buffer = new byte[1024];
      int leidos;
      while ((leidos = bEntrada.read(buffer, 0, 1024)) != -1) {
        bArchivo.write(buffer, 0, leidos);
      }
      bArchivo.close();  		// Cierra los archivos
      bEntrada.close();
      // Logger.log("Reporte enviado a archivo existosamente al servidor");
    } catch (IOException e) {
      throw new ReportExecutionException(e.getMessage(), e);
    }
  }

  public String getCampoOrdenar() {
    return campoOrdenar;
  }

  public void setCampoOrdenar(String campoOrdenar) {
    this.campoOrdenar = campoOrdenar;
  }

  public String getFiltro() {
    return filtro;
  }

  public void setFiltro(String filtro) {
    this.filtro = filtro;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public void setQuery(String query, Object... args) {
    this.query = query;
    this.args = args;
  }

  public Connection getConexion() {
    return conexion;
  }

  public void setConexion(Connection conexion) {
    this.conexion = conexion;
  }

  public String getFormatoSalida() {
    return formatoSalida;
  }

  public void setFormatoSalida(String formatoSalida) {
    this.formatoSalida = formatoSalida;
  }

  public ResultSet getResultados() {
    return resultados;
  }

  public void setResultados(ResultSet resultados) {
    this.resultados = resultados;
  }

  public JasperReport getJr() {
    return jr;
  }

  public void setJr(JasperReport jr) {
    this.jr = jr;
  }

  public JasperPrint getJasperPrint() {
    return jasperPrint;
  }

  public void setJasperPrint(JasperPrint jasperPrint) {
    this.jasperPrint = jasperPrint;
  }

  public String getArchivoSalida() {
    return archivoSalida;
  }

  public void setArchivoSalida(String archivoSalida) {
    this.archivoSalida = archivoSalida;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getSubtitulo() {
    return subtitulo;
  }

  public void setSubtitulo(String subtitulo) {
    this.subtitulo = subtitulo;
  }

  public ByteArrayOutputStream getOut() {
    return out;
  }

  public void setOut(ByteArrayOutputStream out) {
    this.out = out;
  }

  public int getPaginaActual() {
    return paginaActual++;
  }

  public void setPaginaActual(int paginaActual) {
    this.paginaActual = paginaActual;
  }

  public List<ConfigCampoVO> getConfigCampos() {
    return configCampos;
  }

  public void setConfigCampos(List<ConfigCampoVO> configCampos) {
    this.configCampos = configCampos;
  }

  public ArrayList<Map<String, ?>> getColeccion() {
    return coleccion;
  }

  public void setColeccion(ArrayList<Map<String, ?>> coleccion) {
    this.coleccion = coleccion;
  }

  public int getMaximoResultados() {
    return maximoResultados;
  }

  public void setMaximoResultados(int maximoResultados) {
    this.maximoResultados = maximoResultados;
  }

  public String getFieldDelimiter() {
    return fieldDelimiter;
  }

  public void setFieldDelimiter(String fieldDelimiter) {
    this.fieldDelimiter = fieldDelimiter;
  }
}
