package com.conte.reportEngine;

import com.conte.db.DbUtil;
import com.conte.common.service.ReportDAO;
import static com.conte.reportEngine.config.Constantes.*;
import com.conte.reportEngine.config.Constantes.Alineacion;
import com.vaadin.Application;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class GrillaService implements Button.ClickListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private ReportDAO reportDAO = new ReportDAO();
  private ResultSet resultados = null;
  private Select cCampos = new Select("Filtrar por Campo: ");
  private Select cFormatosSalida = new Select("Formato Salida ");
  private TextField editor = new TextField("que contiene: ");
  private Button bFiltrar = new Button("Filtrar");
  private Button bReiniciar = new Button("Reiniciar");
  private Button bExportar = new Button("Exportar");
  private String query = "";
  private IndexedContainer contenedor = new IndexedContainer();
  private Panel panel;// Para lograr el scroll horizontal
  private Table table = new Table();
  ArrayList<ConfigCampoVO> configCamposInterna = null;
  private Application app;
  private Window w;
  private String titulo;
  private ConfigCampoVO campo;
  private ArrayList<ConfigCampoVO> configCampos;

  /**
   * Crea la consulta (tabla vaadin) a partir de los parametros recibidos, donde se permite filtrar por campos = a un texto, y exportar a multiples
   * formato, lo que se filtro y ordeno
   *
   * @param app
   * @param w
   * @param tLayout
   * @param configCampos
   * @param titulo
   * @param query
   */
  public void creaGrilla(Application app, Window w, VerticalLayout tLayout,
          ArrayList<ConfigCampoVO> configCampos, String titulo, String query,
          final Label rActual, int tamanoGrilla, boolean muestraFiltros) {

    // Obtiene el objeto DAO a traves de spring
    this.query = query;
    this.configCampos = configCampos;
    this.app = app;
    this.w = w;
    this.titulo = titulo;
    cCampos.setItemCaptionMode(Select.ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID); // activa de id explicitos para el select
    configCamposInterna = configCampos;
    cFormatosSalida.addItem("PDF");
    cFormatosSalida.addItem("XLS");
    cFormatosSalida.addItem("XML");
    cFormatosSalida.addItem("CSV");
    cFormatosSalida.setNullSelectionAllowed(false);
    cFormatosSalida.setValue("PDF");
    cFormatosSalida.setWidth("120px");

    actualizaDatos();

    cCampos.setImmediate(true);
    cCampos.setNullSelectionAllowed(false);
    bReiniciar.addListener(this);
    bFiltrar.addListener(this);
    bExportar.addListener(this);

    //Crea el panel
    panel = new Panel(titulo);
    panel.setWidth("700px"); // we want scrollbars
    panel.setHeight("75%");
    panel.setScrollable(true);
    panel.setImmediate(true);

    //table.setCaption(titulo);
    table.setStyleName("tabla");
    table.setContainerDataSource(contenedor);
    table.setSelectable(true);
    table.setMultiSelect(false);
    table.setColumnReorderingAllowed(true); // Habilita ordenamiento y
    // colapso de columnas
    table.setColumnCollapsingAllowed(true);
    table.setPageLength(tamanoGrilla); // tamaño por defecto de la pagina
    campo = null; // Configura titulos y tamaños de columnas

    for (int i = 0; i < configCampos.size(); i++) {
      campo = configCampos.get(i);
      table.setColumnHeader(campo.getCampo(), campo.getTitulo());
      // logger.debug("Tama�o del campo "+ campo.getCampo());
      if (campo.getTamano() <= COL_MAX) {
        table.setColumnWidth(campo.getCampo(), campo.getTamano() * COL_ZOOM);
      } else {
        table.setColumnWidth(campo.getCampo(), COL_MAX * COL_ZOOM);
      }
    }

    table.setNullSelectionAllowed(false);
    table.setHeight("100%");
    table.setImmediate(true);
    table.setWidth("100%");


    // Maneja selecciones
    table.addListener(new Property.ValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent event) {
        String value = String.valueOf(table.getValue());

        if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value.trim())) {
          value = "0";
        }

        rActual.setValue(value + " de " + table.getContainerDataSource().size());
        logger.debug("Seleccionado: " + value);
      }
    });
    tLayout.removeAllComponents();
    panel.removeAllComponents();
    panel.addComponent(table);
    tLayout.addComponent(panel);

    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setSpacing(true);
    hLayout.setStyleName("sfiltro");
    hLayout.addComponent(cCampos);
    hLayout.addComponent(editor);
    hLayout.addComponent(bFiltrar);
    hLayout.addComponent(bReiniciar);
    hLayout.addComponent(bExportar);
    hLayout.addComponent(cFormatosSalida);

    tLayout.addComponent(hLayout);
    // oculta o muestra filtros segun se deseaa
    hLayout.setVisible(muestraFiltros);
    reportDAO.closeConnection();

  }

  /**
   * Lee/actualiza datos desde la base de datos hacia el contenedor
   */
  public void actualizaDatos() {

    contenedor = new IndexedContainer();
    resultados = reportDAO.consultaBaseDatos(query);  	// efectua consulta de datos
    try {   	// leee datos de la BD y pobla el contenedor

      // Primero obtiene informacion de las columnas del resultset (numero vs nombre)
      ResultSetMetaData rsm = resultados.getMetaData();
      HashMap<String, Integer> infoCols = Utiles.getInfoColsResulset(rsm);

      campo = null;  	// leee datos de la BD y pobla el contenedor

      int cols = configCampos.size();

      Map<Integer, String> camposTipos = new HashMap<Integer, String>(cols);

      for (int i = 0; i < cols; i++) {

        campo = configCampos.get(i);
        contenedor.addContainerProperty(campo.getCampo(), String.class, "");
        cCampos.addItem(campo.getCampo());
        cCampos.setItemCaption(campo.getCampo(), campo.getTitulo());

        int numCampo = infoCols.get(campo.getCampo());
        String tipo = rsm.getColumnTypeName(numCampo);

        camposTipos.put(i, tipo);

        Alineacion alinea = null;
        // revisa alineaciones de cada columna si son enviadas, si no las deduce por defulat desde las constantes por defecto
        if (!(campo.getAlineacion().equals(Alineacion.NINGUNA))) // si trae valores de alineacion por campo
        {
          alinea = campo.getAlineacion();
        } else {   // si no se paso alineaciones como parametro la deduce por tipo de datos
          alinea = ALINEACIONES.get(tipo);
          if (alinea == null) {
            alinea = Alineacion.IZQUIERDA;
          }
          // logger.debug("campo:"+campo.getCampo()+"-#-"+numCampo+"-"+tipo+"-"+alinea);
        }
        if (alinea.equals(Alineacion.DERECHA)) {
          table.setColumnAlignment(campo.getCampo(), Table.ALIGN_RIGHT);
          // logger.debug("campo:"+campo.getCampo()+"-Alineacion-"+Alineacion.DERECHA);
        } else if (alinea.equals(Alineacion.CENTRO)) {
          table.setColumnAlignment(campo.getCampo(), Table.ALIGN_CENTER);
          // logger.debug("campo:"+campo.getCampo()+"-Alineacion-"+Alineacion.CENTRO);
        } else {
          table.setColumnAlignment(campo.getCampo(), Table.ALIGN_LEFT);
          // logger.debug("campo:"+campo.getCampo()+"-Alineacion-"+Alineacion.IZQUIERDA);
        }
      }

      List<Map<String, Object>> data = DbUtil.extractData(resultados);

      for (Map<String, Object> row : data) {
        Object itemId = contenedor.addItem();
        Item item = contenedor.getItem(itemId);
        for (int i = 0; i < cols; i++) // columna por columna
        {
          campo = configCampos.get(i);
          String campoName = campo.getCampo().trim();
          //logger.debug("Campo:"+campo.getCampo());
          //logger.debug("Tipo de campo:"+ rsm.getColumnTypeName(numCampo));
          //logger.debug("Campo : "+ campo.getCampo()+" Valor "+ resultados.getObject(campo.getCampo().trim()));
          //String valorCampo =  resultados.getString(campo.getCampo());
          String valorCampo = Utiles.formateaDatos(row.get(campoName), camposTipos.get(i),
                  null, campo.getTamano(), null,campo.getPrecision());
          row.put(campo.getCampo(), valorCampo);
          item.getItemProperty(campo.getCampo())
                  .setValue(campo.getCampoFormatter().format(campo.getCampo(), row));
        }
      }
    } catch (SQLException e) {
      logger.debug(e.getMessage());
    }
    getTable().setContainerDataSource(contenedor);
  }

  @Override
  /**
   * Maneja eventos de botones
   */
  public void buttonClick(ClickEvent event) {
    if (event.getButton().getCaption().equalsIgnoreCase("Filtrar")) {
      if (cCampos.getValue() != null
              && (!((String) editor.getValue()).equalsIgnoreCase(""))) {
        Filter filtro = new SimpleStringFilter(
                ((String) cCampos.getValue()).trim(),
                ((String) editor.getValue()).trim(), true, false);
        contenedor.removeAllContainerFilters();
        contenedor.addContainerFilter(filtro);
      }
    } else if (event.getButton().getCaption().equalsIgnoreCase("Reiniciar")) {
      contenedor.removeAllContainerFilters();
      cCampos.setValue(null);
      editor.setValue("");
      table.requestRepaintAll();
    } else if (event.getButton().getCaption().equalsIgnoreCase("Exportar")) {
      if (cFormatosSalida.getValue() != null) {
        creaReporteObjetos((String) cFormatosSalida.getValue());
      }
    }

  }

  /**
   * Invoca al motor de reportes, enviando como fuente de datos una coleccion de maps (filas)
   *
   */
  @SuppressWarnings("unchecked")
  private void creaReporteObjetos(String formatoSalida) {

    @SuppressWarnings("rawtypes")
    ArrayList<Map<String, ?>> cole = new ArrayList();
    Map<String, ?> fila = null;
    Iterator<?> ite = contenedor.getItemIds().iterator();

    // arma la coleccion que se le enviara al motor de reportes para
    // exportar datos
    //Clonar Campos Interna
    ArrayList<ConfigCampoVO> configCamposEnviar = new ArrayList<ConfigCampoVO>(configCamposInterna.size());
    for (ConfigCampoVO item : configCamposInterna) {
      ConfigCampoVO campo = new ConfigCampoVO();
      campo.setCampo(item.getCampo());
      campo.setTitulo(item.getTitulo());
      campo.setTamano(item.getTamano());
      campo.setFormato(item.getFormato());
      campo.setCampoFormatter(item.getCampoFormatter());
      campo.setAlineacion(item.getAlineacion());
      configCamposEnviar.add(campo);
    }




    while (ite.hasNext()) {
      Integer id = (Integer) ite.next();
      @SuppressWarnings("rawtypes")
      HashMap filat = new HashMap<String, String>();
      fila = new HashMap<String, String>();
      for (int i = 0; i < configCamposEnviar.size(); i++) {
        ConfigCampoVO campo = configCamposEnviar.get(i);
        ConfigCampoVO campoOriginal = configCamposInterna.get(i);
        campo.setTamano(campoOriginal.getTamano() * COL_ZOOM);    // amplia tamaño por multiplicador para reportes a exportar
        Item im = (Item) contenedor.getItem(id);
        String valor = (String) im.getItemProperty(campo.getCampo())
                .getValue();
        filat.put(campo.getCampo().intern(), valor);
        fila = filat;
      }
      cole.add(fila);
    }

    ReportService reporte = new ReportService(); // coleccion esta lista
    // para envio, aqui arma
    // el reporte

    reporte.setTitulo(titulo);
    reporte.setSubtitulo("");
    reporte.setFormatoSalida(formatoSalida);
    reporte.setArchivoSalida("conte." + formatoSalida.toLowerCase());
    reporte.setConfigCampos(configCamposInterna);
    reporte.setColeccion(cole);
    reporte.creaReporteObjetos("WEB", app, w);
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }
}
