package com.conte.reportEngine;

import com.conte.db.DbUtil;
import com.conte.db.Persistente;
import static com.conte.reportEngine.config.Constantes.*;
import com.conte.reportEngine.formatter.CampoFormatter;
import com.conte.reportEngine.formatter.DefaultCampoFormatter;
import com.conte.util.DateUtil;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


/**
 * Clase principal para la visualizacion de mantenedores paramétricos
 *
 * @author pedrorozo
 *
 */
@SuppressWarnings("serial,rawtypes")
public class JFormMD extends CustomComponent implements Button.ClickListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  // propiedades genericas para todas las formas
  private int colsGrilla;   //SMJ2
  private CustomLayout cLayout = new CustomLayout("forma2");
  private VerticalLayout layMaster = new VerticalLayout();
  private VerticalLayout layDetalle = new VerticalLayout();
  private HorizontalLayout barraBotones = new HorizontalLayout();
  private HorizontalLayout barraBotonesDetalle = new HorizontalLayout();
  private GrillaService grilla;
  private GrillaService grillaDetalle;
  private TextField registroIr = new TextField();
  private Form iFormaMaestro;
  private Form iFormaDetalle;
  private Button botonConsultar = new Button();
  private Button botonPrimero = new Button();
  private Button botonAnterior = new Button();
  private Button botonSiguiente = new Button();
  private Button botonUltimo = new Button();
  private Button botonAgregar = new Button();
  private Button botonEditar = new Button();
  private Button botonEliminar = new Button();
  private Button botonImprimir = new Button();
  private Button botonDeshacer = new Button();
  private Button botonGuardar = new Button();
  private Vector VRegistro = new Vector();
  private GeneraForma mForma = new GeneraForma();
//  private boolean enFormaEdicion = false;
  private boolean enFormaEdicionDetalle = false;
  private boolean agregando = false;
  // para pantalla de detalle
  private Button botonConsultarD = new Button();
  private Button botonAgregarD = new Button();
  private Button botonEditarD = new Button();
  private Button botonEliminarD = new Button();
  private Button botonImprimirD = new Button();
  private Button botonDeshacerD = new Button();
  private Button botonGuardarD = new Button();
  private Vector VRegistroD = new Vector();
  private GeneraForma mFormaD = new GeneraForma();
  private boolean agregandoDetalle = false;
  private GeneraForma mFormaDetalle = new GeneraForma();
  private Label lIra = new Label(TOOLTIP_IR);              // esta forma de detalle tiene menos botones y funcionalidad
  private Button botonIr = new Button();
  private Label lRactual = new Label(TOOLTIP_RACTUAL);
  private Label rActual = new Label("");
  private Label lRactualD = new Label(TOOLTIP_RACTUALD);
  private Label rActualD = new Label("");
  private ConfirmDialog dialog;
  // Seccion especifica para esta entidad                                        // Plantilla a personalizar - JSP
  private Persistente tabla;
  private Persistente tablaDetalle;
  //  ***************************  Datos de configuracion basica de la JSP ORIGEN  *********************************+
  private String tituloMaestro;
  private String misAlias[];
  private Map<String, CampoFormatter> aliasFormatters;
  private String misTipos[];
  private int miPresentacion[];
  private int longitudMaestro[];
  private int requerido[];
  private String tituloDetalle;
  private String aliasDetalle[];
  private String tiposDetalle[];
  private int presentacionDetalle[];
  private int longitudDetalle[];
  private int requeridoDetalle[];    /// insertado no presenta antes
  // Variables auxiliares
  int tipo;
  int tipoR;
  String forma = new String("");
  //-------------- Declaracion de llaves -----------
  private Vector llaveMaestro;   //Declaración Vector de elementos de la llave maestro
  private Vector llaveDetalle;   //Declaración Vector de elementos de la llave detalle
  //---------- Declaración de Combos ----------
  private Vector combosMaestro;   //Declaración Vector de elementos combo maestro
  private Vector tiposCombosMaestro;   //Declaración Vector de tipos de combo maestro
  private Vector valorCombosMaestro;    //Declaración Vector de valores de combo maestro Si es cadena
  private Vector combosDetalle;   //Declaración Vector de elementos combo detalle
  private Vector tiposCombosDetalle;   //Declaración Vector de tipos de combo detalle
  private Vector valorCombosDetalle;   //Declaración Vector de valores de combo detalle Si es cadena
  //---------- Declaración de RadioButton --------
  private Vector radioMaestro;   //Declaración Vector de elementos radio maestro
  private Vector tiposRadioMaestro;   //Declaración Vector de tipos de radio maestro
  private Vector radioDetalle;   //Declaración Vector de elementos radios detalle
  private Vector tiposRadioDetalle;   //Declaración Vector de tipos de radio detalle

  /* ****  Fin de declaraciones basicas- importadas desde la JSP  *********************************/
  public JFormMD() {
    // inicializaBasicos();
    Locale.setDefault(Locale.ENGLISH);
    setCompositionRoot(cLayout);
    //this.cLayout = cLayout;
    barraBotones.setSpacing(true);
    barraBotones.setMargin(false, true, false, false);
    botonConsultar.addListener(this);
    botonConsultar.setStyleName(BaseTheme.BUTTON_LINK);
    botonConsultar.setIcon(ICONO_CONSULTAR);
    botonConsultar.setDescription(TOOLTIP_CONSULTAR);
    barraBotones.addComponent(botonConsultar);

    botonPrimero.addListener(this);
    botonPrimero.setStyleName(BaseTheme.BUTTON_LINK);
    botonPrimero.setIcon(ICONO_PRIMERO);
    botonPrimero.setDescription(TOOLTIP_PRIMERO);
    barraBotones.addComponent(botonPrimero);

    botonAnterior.addListener(this);
    botonAnterior.setStyleName(BaseTheme.BUTTON_LINK);
    botonAnterior.setIcon(ICONO_ANTERIOR);
    botonAnterior.setDescription(TOOLTIP_ANTERIOR);
    barraBotones.addComponent(botonAnterior);

    botonSiguiente.addListener(this);
    botonSiguiente.setStyleName(BaseTheme.BUTTON_LINK);
    botonSiguiente.setIcon(ICONO_SIGUIENTE);
    botonSiguiente.setDescription(TOOLTIP_SIGUIENTE);
    barraBotones.addComponent(botonSiguiente);

    botonUltimo.addListener(this);
    botonUltimo.setStyleName(BaseTheme.BUTTON_LINK);
    botonUltimo.setIcon(ICONO_ULTIMO);
    botonUltimo.setDescription(TOOLTIP_ULTIMO);
    barraBotones.addComponent(botonUltimo);

    botonAgregar.addListener(this);
    botonAgregar.setStyleName(BaseTheme.BUTTON_LINK);
    botonAgregar.setIcon(ICONO_AGREGAR);
    botonAgregar.setDescription(TOOLTIP_AGREGAR);
    barraBotones.addComponent(botonAgregar);

    botonEditar.addListener(this);
    botonEditar.setStyleName(BaseTheme.BUTTON_LINK);
    botonEditar.setIcon(ICONO_EDITAR);
    botonEditar.setDescription(TOOLTIP_EDITAR);
    barraBotones.addComponent(botonEditar);

    botonEliminar.addListener(this);
    botonEliminar.setStyleName(BaseTheme.BUTTON_LINK);
    botonEliminar.setIcon(ICONO_ELIMINAR);
    botonEliminar.setDescription(TOOLTIP_ELIMINAR);
    barraBotones.addComponent(botonEliminar);

    botonImprimir.addListener(this);
    botonImprimir.setStyleName(BaseTheme.BUTTON_LINK);
    botonImprimir.setIcon(ICONO_IMPRIMIR);
    botonImprimir.setDescription(TOOLTIP_IMPRIMIR);
    barraBotones.addComponent(botonImprimir);

    botonDeshacer.addListener(this);
    botonDeshacer.setStyleName(BaseTheme.BUTTON_LINK);
    botonDeshacer.setIcon(ICONO_DESHACER);
    botonDeshacer.setDescription(TOOLTIP_DESHACER);
    barraBotones.addComponent(botonDeshacer);

    botonGuardar.addListener(this);
    botonGuardar.setStyleName(BaseTheme.BUTTON_LINK);
    botonGuardar.setIcon(ICONO_GUARDAR);
    botonGuardar.setDescription(TOOLTIP_GUARDAR);
    barraBotones.addComponent(botonGuardar);
    // deja estos botones deshabilitados por defecto
    botonConsultar.setVisible(false);
    botonImprimir.setVisible(false);
    botonDeshacer.setVisible(false);
    botonGuardar.setVisible(false);

    barraBotones.addComponent(lIra);

    registroIr.setMaxLength(4);
    registroIr.setWidth(40, UNITS_PIXELS);
    barraBotones.addComponent(registroIr);

    botonIr.addListener(this);
    botonIr.setStyleName(BaseTheme.BUTTON_LINK);
    botonIr.setIcon(ICONO_IR);
    botonIr.setDescription(TOOLTIP_IR);
    barraBotones.addComponent(botonIr);

    barraBotones.addComponent(lRactual);
    barraBotones.addComponent(rActual);
    cLayout.addComponent(barraBotones, "barraBotones");

    layMaster.setStyleName("ctabla");  		// layout contenedor para tabla de datos Web 2.0
    cLayout.addComponent(layMaster, "tabla1");
  }

  @Override
  // ejecutado siempre antes del rendering - por en tiempo de contruccion no siempre se conoce el objeto app y derivados (windows)
  public void attach() {
    //  super.attach(); // Must call.
    logger.debug(" ************ JFormFD attach - GetWindow en attach  =" + getWindow());

    grilla(); 		// grilla de navegacion
    posicionaGrilla(grilla, 1);  		// deja seleccionado el primer registro por defecto
    iniciaDetalle();
  }

  /**
   * Logica de inicializacion de grilla y botones para forma de detalle
   */
  private void iniciaDetalle() {
    barraBotonesDetalle.setSpacing(true);
    barraBotonesDetalle.setMargin(false, true, false, false);

    botonConsultarD.addListener(this);
    botonConsultarD.setStyleName(BaseTheme.BUTTON_LINK);
    botonConsultarD.setIcon(ICONO_CONSULTAR);
    botonConsultarD.setDescription(TOOLTIP_CONSULTARD);
    barraBotonesDetalle.addComponent(botonConsultarD);

    botonAgregarD.addListener(this);
    botonAgregarD.setStyleName(BaseTheme.BUTTON_LINK);
    botonAgregarD.setIcon(ICONO_AGREGAR);
    botonAgregarD.setDescription(TOOLTIP_AGREGARD);
    barraBotonesDetalle.addComponent(botonAgregarD);

    botonEditarD.addListener(this);
    botonEditarD.setStyleName(BaseTheme.BUTTON_LINK);
    botonEditarD.setIcon(ICONO_EDITAR);
    botonEditarD.setDescription(TOOLTIP_EDITARD);
    barraBotonesDetalle.addComponent(botonEditarD);

    botonEliminarD.addListener(this);
    botonEliminarD.setStyleName(BaseTheme.BUTTON_LINK);
    botonEliminarD.setIcon(ICONO_ELIMINAR);
    botonEliminarD.setDescription(TOOLTIP_ELIMINARD);
    barraBotonesDetalle.addComponent(botonEliminarD);

    botonDeshacerD.addListener(this);
    botonDeshacerD.setStyleName(BaseTheme.BUTTON_LINK);
    botonDeshacerD.setIcon(ICONO_DESHACER);
    botonDeshacerD.setDescription(TOOLTIP_DESHACERD);
    barraBotonesDetalle.addComponent(botonDeshacerD);

    botonGuardarD.addListener(this);
    botonGuardarD.setStyleName(BaseTheme.BUTTON_LINK);
    botonGuardarD.setIcon(ICONO_GUARDAR);
    botonGuardarD.setDescription(TOOLTIP_GUARDARD);
    barraBotonesDetalle.addComponent(botonGuardarD);
    // deja estos botones deshabilitados por defecto
    botonConsultarD.setVisible(false);
    botonDeshacerD.setVisible(false);
    botonGuardarD.setVisible(false);

    barraBotonesDetalle.addComponent(lRactualD);     // barra de botones de la forma de detalle - oculta por defecto
    barraBotonesDetalle.addComponent(rActualD);
    cLayout.addComponent(barraBotonesDetalle, "barraBotonesDetalle");
    barraBotonesDetalle.setVisible(false);
    //layDetalle.setStyleName("ctabla");  		// layout contenedor para tabla de datos Web 2.0 - detalle pro defecto oculto
    layDetalle.setVisible(false);
    cLayout.addComponent(layDetalle, "tabla2");
  }

  @Override
  /**
   * Procesa todos los eventos de la barra de botones
   */
  public void buttonClick(ClickEvent event) {
      int actualMaestro=0;
      try {

          if (((Integer) grilla.getTable().getContainerDataSource().size()).intValue() > 0) {
              actualMaestro = getActual(grilla);
          } else {
              actualMaestro = 0;
          }
      } catch (Exception e) {
          logger.error("No se obtuvo tabla", e);
      }
    int totalMaestro = ((Integer) grilla.getTable().getContainerDataSource().size()).intValue();

    String opcion = event.getButton().getDescription();

    if (opcion.equalsIgnoreCase(TOOLTIP_PRIMERO)) {
      posicionaGrilla(grilla, 1);
    } else if (opcion.equalsIgnoreCase(TOOLTIP_ULTIMO)) {
      posicionaGrilla(grilla, totalMaestro);
    } else if (opcion.equalsIgnoreCase(TOOLTIP_SIGUIENTE)) {
      if (++actualMaestro <= totalMaestro) {
        posicionaGrilla(grilla, actualMaestro);
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_ANTERIOR)) {
      if (--actualMaestro >= 1) {
        posicionaGrilla(grilla, actualMaestro);
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_GUARDAR)) {
      guardarMaestro();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_AGREGAR)) {
      agregarMaestro();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_DESHACER)) {
      deshacerMaestro();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_IMPRIMIR)) {
      getWindow().executeJavaScript("print();");
    } else if (opcion.equalsIgnoreCase(TOOLTIP_ELIMINAR)) {
      if (totalMaestro > 0) {
        eliminaMaestro();
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_EDITAR)) {
      if (totalMaestro > 0) {
        editarMaestro(Boolean.TRUE);
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_CONSULTAR)) {
      consultarMaestro();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_IR)) {
      int destino = Integer.parseInt((String) registroIr.getValue());
      if (destino >= 1 && destino <= totalMaestro) {
        posicionaGrilla(grilla, destino);
      }
      // Seccion de eventos para ventana de detalle
    } else if (opcion.equalsIgnoreCase(TOOLTIP_EDITARD)) {
      int totalDetalle = ((Integer) grillaDetalle.getTable().getContainerDataSource().size()).intValue();
      if (totalDetalle > 0) {
        editarDetalle(Boolean.TRUE);
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_IMPRIMIRD)) {
      getWindow().executeJavaScript("print();");
    } else if (opcion.equalsIgnoreCase(TOOLTIP_CONSULTARD)) {
      consultarDetalle();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_ELIMINARD)) {
      int totalDetalle = ((Integer) grillaDetalle.getTable().getContainerDataSource().size()).intValue();
      if (totalDetalle > 0) {
        eliminaDetalle();
      }
    } else if (opcion.equalsIgnoreCase(TOOLTIP_AGREGARD)) {
      agregarDetalle();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_DESHACERD)) {
      deshacerDetalle();
    } else if (opcion.equalsIgnoreCase(TOOLTIP_GUARDARD)) {
      guardarDetalle();
    }

    String value = String.valueOf(grilla.getTable().getValue());

    if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value.trim())) {
      value = "0";
    }

    rActual.setValue(value + " de " + grilla.getTable().getContainerDataSource().size());
    grilla.getTable().requestRepaint();
  }

  /**
   * Refresca la posicion de la grilla en registro y pagina
   */
  private void posicionaGrilla(GrillaService grilla, int actual) {

    grilla.getTable().select(actual);
    grilla.getTable().setCurrentPageFirstItemIndex((actual - 1));

    String value = String.valueOf(grilla.getTable().getValue());

    if (value == null || value.isEmpty() || "null".equalsIgnoreCase(value.trim())) {
      value = "0";
    }

    rActual.setValue(value + " de " + grilla.getTable().getContainerDataSource().size());
    grilla.getTable().requestRepaint();
    logger.debug("Posicion ACTUAL :" + actual);
  }

  /**
   * Agregar registro a tabla maestra y la deja en modo de edicion
   */
  private void agregarMaestro() {
    logger.debug("Clase de plantilla maestro: " + tabla.getClass().getName());
    Persistente nuevo = null;
    try {
      nuevo = tabla.getClass().newInstance();         // obtiene una instancia vacia del mismo javabean del maestro
    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (IllegalAccessException e) {

      e.printStackTrace();
    }

    //Persistente nuevo = plantillaMaestro;  //  crea un nuevo javabena (con datos por defecto)       //********  CODIGO PLANTILLA  JSP a reemplazar con macro*********//
    agregando = true;
    posicionaGrilla(grilla, 1);
    tabla.setContenido(ajustaDatos(nuevo.getContenido(),misTipos,longitudMaestro,nuevo.getPrecision()));  //carga el javabean temporal en actual (tabla) para su edicion)      //SMJ2
    nuevo.cerrarConexiones();
    editarMaestro(Boolean.FALSE);
    iFormaMaestro.setReadOnly(false);
    botonDeshacer.setVisible(true);
    botonGuardar.setVisible(true);
    botonAgregar.setVisible(false);
    iFormaMaestro.requestRepaint();
    this.requestRepaintAll();
  }

  /**
   * Almacena cambios de edicion en la base de datos usando capa de persistencia existente
   */
  private void guardarMaestro() {
    int actual=0;
    try {
    if (tabla.getSize() > 0) {
       actual = Integer.parseInt(grilla.getTable().getValue().toString().replace("[", "").replace("]", ""));
    }
    } catch (Exception e) {
        logger.error("No pudo obtener la tabla",e);
    }
    iFormaMaestro.commit();
    Iterator<?> ite = iFormaMaestro.getItemPropertyIds().iterator();
    Vector registro = new Vector();
    // Convierte fechas java.util.date a java.sql.Date - requisito para capa de persistencia
    while (ite.hasNext()) {
      Integer pro = (Integer) ite.next();
      Object oProp = iFormaMaestro.getItemProperty(pro).getValue();
      String tipoC = "";
      if (oProp != null) {
        tipoC = oProp.getClass().getName().toString();
      }
      logger.debug("GuardaCorrige:" + tipoC);
      if (!tipoC.equalsIgnoreCase("java.util.Date")) {
        registro.add(iFormaMaestro.getItemProperty(pro).getValue());
        logger.debug("Propiedad salida:" + pro + "=" + iFormaMaestro.getItemProperty(pro).getValue() + "-tipo=" + tipoC);
      } else // es fecha corrige de formato largo en java.util.Date a java.sql.Date
      {
        String fechac = DateUtil.getInstance().formatDateAsString((java.util.Date) iFormaMaestro.getItemProperty(pro).getValue());
        registro.add(fechac);
        logger.debug("Propiedad salidaF:" + pro + "=" + fechac + "-tipofechaN=" + fechac.getClass().getName());
      }
    }
    tabla.setContenido(ajustaDatos(registro,misTipos,longitudMaestro,tabla.getPrecision()));
    // Validacion de datos usando logica del javabean existente  (Maestro)
    // sugerencias - revisar para enviar mensaje mas diciente.
      /*
     if( !(tabla.validaContenido(registro,misTipos , requerido) &&  Utiles.VerificaNulos(registro, requerido))) {    // datos validos para guardarse
     getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_VALIDACION_MAESTRO,Notification.TYPE_ERROR_MESSAGE);
     }
     else   // datos fueron validos se procede a actualizar ó insertar segun sea el caso
     {
     */

    if (!agregando) {   // acualizando: update
      boolean actualizo = tabla.actualizar();
      if (actualizo) {
        getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_ACTUALIZACION);
        grilla.actualizaDatos();
        posicionaGrilla(grilla, actual);
      } else {
        getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_ACTUALIZACION, Notification.TYPE_ERROR_MESSAGE);
      }  // actualizacion
    } else //  agregando (insert)
    {
      boolean inserto=false;
      boolean existe=false;
      String  pk="";
      for (int i=0;i<tabla.getAtributosLLave().size();i++){
          pk+=tabla.getAtributosLLave().elementAt(i)+" = "+tabla.getContenido().elementAt(i).toString() + " ";
      }
        try {

            tabla.consultaP();
            if (!tabla.rs.next()) {
                inserto = tabla.insertar();
                existe = false;
            } else {
                inserto = false;
                existe = true;
            }
            if (inserto) {
                getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_CREACION);
                grilla.actualizaDatos();
                posicionaGrilla(grilla, 1);
            } else {
                if (existe) {
                    getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_CREACION_PK+pk, Notification.TYPE_ERROR_MESSAGE);
                } else {
                    getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_CREACION, Notification.TYPE_ERROR_MESSAGE);
                }

            }  // actualizacion
        } catch (SQLException e) {
            logger.error("Error consultando llave primaria para maestro" + pk, e);
        } finally {
            DbUtil.closeResultSet(tabla.rs);
        }
    }
    agregando = false;
    consultarMaestro();

    // }  //   validacion

  }

  /**
   * Cambia a modo grilla en el maestro, restaurando el estado de los botones
   */
  private void consultarMaestro() {
    int actual = getActual(grilla);
    logger.debug("reg Actual al consultar :" + actual);

    iFormaMaestro.removeAllProperties();

    layDetalle.setVisible(false);           //oculta panel de detalle al pasar a grilla de master
    barraBotonesDetalle.setVisible(false);

    cLayout.removeComponent(iFormaMaestro);
    cLayout.addComponent(layMaster, "tabla1");

    botonAgregar.setVisible(true);   		// habilita botones que no aplican
    botonPrimero.setVisible(true);
    botonSiguiente.setVisible(true);
    botonAnterior.setVisible(true);
    botonUltimo.setVisible(true);
    botonAgregar.setVisible(true);

    lIra.setVisible(true);
    registroIr.setVisible(true);
    botonIr.setVisible(true);
    lRactual.setVisible(true);

    botonConsultar.setVisible(false);  		// deshabilita botones que no aplican
    botonDeshacer.setVisible(false);
    botonGuardar.setVisible(false);

    if (iFormaMaestro != null) {
      iFormaMaestro.setReadOnly(true);
    }
    posicionaGrilla(grilla, actual);
  }

  /**
   * Crea forma de edición, con los campos correspondientes al registro actual
   */
  private void editarMaestro(boolean editar) {

      String[] props = tabla.getAtributos();
      String[] pk = new String[tabla.getAtributosLLave().size()];
      tabla.inicializar();
      try {
        if (!agregando) {
          int actual = getActual(grilla);
          // pk = grilla.getTable().getItem(actual).getItemProperty(props[0]).getValue().toString();  // obtiene valor de la llave primaria (siempre es el primer atributo)
          // obtiene llave maestra para luego usarse en grillaDetalle como fuente del join

          if (llaveMaestro != null) {         //SMJ2
            for (int i=0; i<tabla.getAtributosLLave().size();i++) {
               String CampoLlaveMaestra = llaveMaestro.get(i).toString();
               pk[i] = grilla.getTable().getItem(actual).getItemProperty(CampoLlaveMaestra).getValue().toString(); // obtiene el valor de la llave maestra
            }
          } else {    // es una forma sin detalle, en ese caso se asumen que el primer atributo es la llave maestra

            for (int i=0; i<tabla.getAtributosLLave().size();i++) {
               pk[i] = grilla.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString();
            }

          }

          logger.debug("PK=" + StringUtils.arrayToDelimitedString(pk, " ") + "-actual=" + actual);

          String where="WHERE 1=1 " ;
          for (int i=0; i<tabla.getAtributosLLave().size();i++){
              where=where + " and " + props[i] + "= ?";
          }
          tabla.consultaP(where,pk);  //obtiene todos los campos del campo seleccionado en la BD
          tabla.rs.next();                // carga datos en RS
          tabla.setContenido();                                       // tranfiere los datos del resulset a un vector
        }
        VRegistro = tabla.getContenido();
        Iterator ite = VRegistro.iterator();
        int j = 0;
        while (ite.hasNext()) {
          logger.debug("Propiedad entrada:" + props[j] + "=" + ite.next());
          j++;
        }
        cLayout.removeComponent(layMaster);                             // remueve grilla de pantalla
        iFormaMaestro = mForma.creaForma(tituloMaestro, VRegistro, misAlias, misTipos, miPresentacion, longitudMaestro, requerido, combosMaestro, tiposCombosMaestro, valorCombosMaestro, radioMaestro,tabla.getElementosLLave(),editar, true,llaveMaestro,llaveDetalle,pk, tabla.getAtributos());
        iFormaMaestro.setReadOnly(true);

        if (!agregando) {
          // crea y habilita la segunda grilla para detalle
          if (llaveMaestro != null) // valida si el maestro tine detalle
          {
            grillaDetalle(pk);
            layDetalle.setVisible(true);
            barraBotonesDetalle.setVisible(true);
          }
        }
        cLayout.addComponent(iFormaMaestro, "tabla1");

        // oculta botones que no aplican
        botonAgregar.setVisible(false);
        botonPrimero.setVisible(false);
        botonSiguiente.setVisible(false);
        botonAnterior.setVisible(false);
        botonUltimo.setVisible(false);
        botonAgregar.setVisible(false);
        botonGuardar.setVisible(false);
        botonDeshacer.setVisible(false);

        lIra.setVisible(false);
        registroIr.setVisible(false);
        botonIr.setVisible(false);

        // habilita botones que si aplican
        botonConsultar.setVisible(true);
        botonImprimir.setVisible(true);

        iFormaMaestro.setReadOnly(false);
        botonDeshacer.setVisible(true);
        botonGuardar.setVisible(true);
        iFormaMaestro.requestRepaint();
        this.requestRepaintAll();

      } catch (SQLException e) {
        logger.debug(e.getMessage());
      }
  }

  /**
   * Deshace cmabios (carga informacion anterior) en registro actual de codigo maestro
   */
  public void deshacerMaestro() {
    dialog = new ConfirmDialog(getWindow(), MSGCONFIRMA_TITULO, MSGDESHACER_PREGUNTA, new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        if (ConfirmDialog.BUTTON_OK_CAPTION.equals(event.getButton().getCaption())) {
          getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_DESHACER);
          editarMaestro(Boolean.TRUE);    // recarga datos para evitar problemas de sincronizacion y deja registro en modo de lectura
          consultarMaestro();
        } else {
          logger.debug("No desea descartar cambios del registro ");
        } // dialog
        dialog.hide();
      } // on Close
    });   // listener
    dialog.show();
  }

  /**
   * Eliminar el registro indicado por la llave primaria en la tabla actual
   *
   * @param primaryKey
   */
    public void eliminaMaestro() {
        final String[] props = tabla.getAtributos();
        final int actual = Integer.parseInt(grilla.getTable().getValue().toString().replace("[", "").replace("]", ""));

        final Object[] args = new Object[tabla.getElementosLLave()];
        final String[] pk = new String[tabla.getElementosLLave()];
        for (int i = 0; i < tabla.getElementosLLave(); i++) {
            pk[i] = grilla.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString();
        }



        dialog = new ConfirmDialog(getWindow(), MSGCONFIRMA_TITULO, MSGELIMINAR_PREGUNTA + " " + StringUtils.arrayToDelimitedString(pk, " "), new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                if (ConfirmDialog.BUTTON_OK_CAPTION.equals(event.getButton().getCaption())) {

                    logger.debug("Si desea eliminar el registro con llave primaria: " +StringUtils.arrayToDelimitedString(pk, " "));
                    boolean borrado = false;
                    try {
                            String nombreLlave = "";
                            for (int i = 0; i < tabla.getElementosLLave(); i++) {
                                nombreLlave = nombreLlave + " and " + props[i] + " =? ";
                                args[i] = grilla.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString() ;
                            }
                            borrado = tabla.eliminar(nombreLlave, args);
                    } catch (SQLException e) {
                        logger.debug("Error al borrar registro con llave:" +StringUtils.arrayToDelimitedString(pk, " ") + "En tabla:" + tabla.getNombreTabla() + e.getMessage());
                        borrado = false;
                    }
                    if (borrado) {
                        getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_ELIMINACION);
                        grilla.actualizaDatos();
                        grilla.getTable().select(1);
                        posicionaGrilla(grilla, 1);
                        // grilla.getTable().requestRepaintAll();
                    } else {
                        getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_ELIMINACION, Notification.TYPE_ERROR_MESSAGE);
                    }  // borrado

                } else {
                    logger.debug("No desea borrar el registro ");
                } // dialog
                dialog.hide();
            } // on Close
        });   // listener

        dialog.show();

        consultarMaestro();

    }

  /**
   *
   * Crear una grilla de visualizacion, tomando como columnas los 4 primeros campos (atributos de la tabla) Permite ordenamiento y filtraje de datos
   */
  public void grilla() {
    //SMJ2
    grilla = new GrillaService();
    ArrayList<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();
    ConfigCampoVO cfgCampo;
    StringBuilder query = new StringBuilder("SELECT ");
    int cols = getColsGrilla();
    String[] attrs = tabla.getAtributos();
    for (int j = 0; j < cols; j++) {
      CampoFormatter formatter = obtainCampoFormatter(misAlias[j]);
      cfgCampo = new ConfigCampoVO(formatter);
      cfgCampo.setCampo(attrs[j]);
      cfgCampo.setTitulo(misAlias[j]);
      cfgCampo.setTamano(longitudMaestro[j]);
      if (tabla.getPrecision() != null) {
          if (misTipos[j].equalsIgnoreCase("DOUBLE") || misTipos[j].equalsIgnoreCase("FLOAT") || misTipos[j].equalsIgnoreCase("DECIMAL")) {
              if (j<(tabla.getPrecision().length)) {
                 cfgCampo.setFormato(misTipos[j]+tabla.getPrecision()[j]);
                 cfgCampo.setPrecision(tabla.getPrecision()[j]);
              }

          }
      }

      configCampos.add(cfgCampo);
      if (j < (cols - 1)) {
        query.append(attrs[j]).append(",");
      } else {
        query.append(attrs[j]);
      }
    }
    query.append(" FROM ").append(tabla.getNombreTabla()).append(" ORDER BY ").append(tabla.getAtributos()[0]);   // ordenados por llave primario (0)
    logger.debug(query.toString());
    grilla.creaGrilla(getApplication(), getWindow(), layMaster, configCampos, tituloMaestro, query.toString(),
            rActual, SIZEGRILLA_MAESTRO, true);
  }

  /**
   *
   * Crear una grilla de visualizacion para la tabla de detalla, tomando como columnas los 3 primeros campos (atributos de la tabla) Permite
   * ordenamiento y filtraje de datos
   */
  public void grillaDetalle(String[] pk) {
    grillaDetalle = new GrillaService();
    ConfigCampoVO cfgCampo = null;
    ArrayList<ConfigCampoVO> configCampos = new ArrayList<ConfigCampoVO>();
    String query = "SELECT ";
    int control=NUMEROCAMPOS_GRILLADETALLE;
    if (tablaDetalle.getAtributos().length<NUMEROCAMPOS_GRILLADETALLE) {
        control=tablaDetalle.getAtributos().length;
    }
    for (int j = 0; j < control ; j++) {

      cfgCampo = new ConfigCampoVO();
      cfgCampo.setCampo(tablaDetalle.getAtributos()[j]);
      cfgCampo.setTitulo(aliasDetalle[j]);
      cfgCampo.setTamano(longitudDetalle[j]);
      if (tablaDetalle.getPrecision() != null) {
          if (tiposDetalle[j].equalsIgnoreCase("DOUBLE") || tiposDetalle[j].equalsIgnoreCase("FLOAT") || tiposDetalle[j].equalsIgnoreCase("DECIMAL")) {
              if (j<(tablaDetalle.getPrecision().length)) {
                 cfgCampo.setFormato(tiposDetalle[j]+tablaDetalle.getPrecision()[j]);
                 cfgCampo.setPrecision(tablaDetalle.getPrecision()[j]);
              }

          }
      }

      configCampos.add(cfgCampo);
      if (j != (control - 1)) {
        query += tablaDetalle.getAtributos()[j] + ",";
      } else {
        query += tablaDetalle.getAtributos()[j];
      }



    }
    query += " FROM " + tablaDetalle.getNombreTabla()
            + " WHERE 1=1";
    for (int i=0;i<llaveDetalle.size();i++){
        query += " and " + llaveDetalle.get(i) + " = '" + pk[i] + "'";
    }
    query += " ORDER BY " ;
    for (int i=0;i<llaveDetalle.size();i++) {
        query +=  tablaDetalle.getAtributosLLave().elementAt(i);
    }   // ordenados por llave primario (0)

    logger.debug(query);
    grillaDetalle.creaGrilla(getApplication(), getWindow(), layDetalle, configCampos, tituloDetalle, query, rActualD, SIZEGRILLA_DETALLE, true);
    posicionaGrilla(grillaDetalle, 1);  		// deja seleccionado el primer registro por defecto

  }

  /**
   * Crea forma de edición, con los campos correspondientes al registro actual de detalle
   */
  private void editarDetalle(boolean editar) {
    boolean hayDatos = false;
    if (grillaDetalle.getTable().size() > 0 || agregandoDetalle) {
      hayDatos = true;
    }
    if (enFormaEdicionDetalle && hayDatos) // si ya esta en detalle solo le habilita el acceso
    {
      iFormaDetalle.setReadOnly(false);
      botonDeshacerD.setVisible(true);
      botonGuardarD.setVisible(true);
      iFormaDetalle.requestRepaint();
    } else {
      String[] props = tablaDetalle.getAtributos();
      String[] pk  = new String[tablaDetalle.getAtributosLLave().size()];
      tablaDetalle.inicializar();
      try {
        if (editar) {

          int actual = getActual(grillaDetalle);
          for (int i=0;i<tablaDetalle.getAtributosLLave().size();i++) {
              pk[i] = grillaDetalle.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString();  // obtiene valor de la llave primaria (siempre es el primer atributo)
          }

          logger.debug("PK=" + StringUtils.arrayToDelimitedString(pk," ") + "-actual=" + actual);
          String where ="WHERE 1=1 ";
          for (int i=0;i<tablaDetalle.getAtributosLLave().size();i++) {
              where+=" and " + props[i] + "=?";
          }
          tablaDetalle.consultaP(where,pk);  //obtiene todos los campos del campo seleccionado en la BD
          tablaDetalle.rs.next();                // carga datos en RS
          tablaDetalle.setContenido();                                       // tranfiere los datos del resulset a un vector
          VRegistroD = tablaDetalle.getContenido();
        } else // esta agregando un registro nuevo
        {
          VRegistroD = tablaDetalle.getContenido();
          // aqui se le inserta la llave primario del maestro por defecto, a la primera posicion del vector
          //VRegistroD.set(0, VRegistro.get(0));
        }

        Iterator ite = VRegistroD.iterator();
        int j = 0;
        while (ite.hasNext()) {
          logger.debug("Propiedad entrada:" + props[j] + "=" + ite.next());
          j++;
        }
        cLayout.removeComponent(layDetalle);                             // remueve grilla de pantalla
        iFormaDetalle = mFormaDetalle.creaForma(tituloDetalle, VRegistroD, aliasDetalle, tiposDetalle, presentacionDetalle, longitudDetalle, requeridoDetalle, combosDetalle, tiposCombosDetalle, valorCombosDetalle, radioDetalle,tablaDetalle.getElementosLLave(),editar,false,llaveMaestro,llaveDetalle,obtienePkMaestro(),tablaDetalle.getAtributos());
        iFormaDetalle.setReadOnly(true);

        cLayout.addComponent(iFormaDetalle, "tabla2");

        // oculta botones que no aplican
        botonAgregarD.setVisible(false);
        botonGuardarD.setVisible(false);
        botonDeshacerD.setVisible(false);

        lRactualD.setVisible(false);
        rActualD.setVisible(false);

        // habilita botones que si aplican
        botonConsultarD.setVisible(true);
        botonImprimirD.setVisible(true);
        enFormaEdicionDetalle = true;

      } catch (SQLException e) {
        logger.debug(e.getMessage());
      }
    }  // enFormaEdicionDetalle
/*
     } // if tamaño de grilla > 0
     else {
     getWindow().showNotification(NOMBRE_APLICACION,MSGERROR_EDICIONVACIO,Notification.TYPE_WARNING_MESSAGE);
     }
     */
  }

  /**
   * Cambia a modo grilla en el detalle, restaurando el estado de los botones
   */
  private void consultarDetalle() {
    int actual = getActual(grillaDetalle);
    logger.debug("reg Actual al consultar :" + actual);
    if (enFormaEdicionDetalle) {
      iFormaDetalle.removeAllProperties();
    }

    cLayout.removeComponent(iFormaDetalle);
    cLayout.addComponent(layDetalle, "tabla2");

    botonAgregarD.setVisible(true);   		// habilita botones que no aplican

    rActualD.setVisible(true);
    lRactualD.setVisible(true);

    botonConsultarD.setVisible(false);  		// deshabilita botones que no aplican
    botonDeshacerD.setVisible(false);
    botonGuardarD.setVisible(false);
    enFormaEdicionDetalle = false;
    if (iFormaDetalle != null) {
      iFormaDetalle.setReadOnly(true);
    }
    posicionaGrilla(grillaDetalle, actual);
  }

  /**
   * Eliminar el registro indicado por la llave primaria en la tabla de detalle
   *
   * @param primaryKey
   */
    public void eliminaDetalle() {


        if (grillaDetalle.getTable().size() > 0) {

            final String[] props = tablaDetalle.getAtributos();
            final int actual = Integer.parseInt(grillaDetalle.getTable().getValue().toString().replace("[", "").replace("]", ""));

            final Object[] args = new Object[tablaDetalle.getElementosLLave()];
            final String[] pk = new String[tablaDetalle.getElementosLLave()];
            for (int i = 0; i < tablaDetalle.getElementosLLave(); i++) {
                pk[i] = grillaDetalle.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString() ;
            }

            dialog = new ConfirmDialog(getWindow(), MSGCONFIRMA_TITULO, MSGELIMINAR_PREGUNTA + " " + StringUtils.arrayToDelimitedString(pk, " ") , new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    boolean borrado = false;

                    if (ConfirmDialog.BUTTON_OK_CAPTION.equals(event.getButton().getCaption())) {
                        logger.debug("Si desea eliminar el registro con llave primaria: " +StringUtils.arrayToDelimitedString(pk, " "));
                        try {
                            String nombreLlave = "";
                            for (int i = 0; i < tablaDetalle.getElementosLLave(); i++) {
                                nombreLlave = nombreLlave + " and " + props[i] + " =? ";
                                args[i] = grillaDetalle.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString() ;
                            }
                            borrado = tablaDetalle.eliminar(nombreLlave, args);
                        } catch (Exception e) {
                            logger.debug("Error al borrar registro con llave:" + pk + "En tabla:" + tablaDetalle.getNombreTabla() + e.getMessage());
                            borrado = false;
                        }
                        if (borrado) {
                            getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_ELIMINACION);
                            grillaDetalle.actualizaDatos();
                            grillaDetalle.getTable().select(1);
                            posicionaGrilla(grillaDetalle, 1);
                            // grilla.getTable().requestRepaintAll();
                        } else {
                            getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_ELIMINACION, Notification.TYPE_ERROR_MESSAGE);
                        }  // borrado
                    }
                    dialog.hide();
                }
            });

            dialog.show();

            consultarDetalle();
        } // if tamaño de grilla > 0
        else {
            getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_ELIMINACIONVACIO, Notification.TYPE_WARNING_MESSAGE);
        }

    }

  /**
   * Deshace cmabios (carga informacion anterior) en registro actual de tabla detalle
   */
  public void deshacerDetalle() {
    dialog = new ConfirmDialog(getWindow(), MSGCONFIRMA_TITULO, MSGDESHACER_PREGUNTA, new Button.ClickListener() {
      @Override
      public void buttonClick(ClickEvent event) {
        if (ConfirmDialog.BUTTON_OK_CAPTION.equals(event.getButton().getCaption())) {
          getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_DESHACER);
          editarDetalle(Boolean.TRUE);    // recarga datos para evitar problemas de sincronizacion y deja registro en modo de lectura
          consultarDetalle();
        } else {
          logger.debug("No desea descartar cambios del registro ");
        } // dialog
        dialog.hide();
      } // on Close
    });   // listener
    dialog.show();
  }

  /**
   * Almacena cambios de edicion en la base de datos usando capa de persistencia existente (tabla detalle)
   */
  private void guardarDetalle() {

    iFormaDetalle.commit();
    Iterator<?> ite = iFormaDetalle.getItemPropertyIds().iterator();
    Vector registro = new Vector();
    // Convierte fechas java.util.date a java.sql.Date - requisito para capa de persistencia
    while (ite.hasNext()) {
      Integer pro = (Integer) ite.next();

      Object oProp = iFormaDetalle.getItemProperty(pro).getValue();
      String tipoC = "";
      if (oProp != null) {
        tipoC = oProp.getClass().getName().toString();
      }

      //logger.debug("GuardaCorrige:"+tipoC);
      if (!tipoC.equalsIgnoreCase("java.util.Date")) {
        registro.add(iFormaDetalle.getItemProperty(pro).getValue());
        logger.debug("Propiedad salida:" + pro + "=" + iFormaDetalle.getItemProperty(pro).getValue() + "-tipo=" + tipoC);
      } else // es fecha corrige de formato largo en java.util.Date a java.sql.Date
      {
        java.sql.Date fechac = Utiles.corrigeFecha((java.util.Date) iFormaDetalle.getItemProperty(pro).getValue());
        registro.add(fechac);
        logger.debug("Propiedad salidaF:" + pro + "=" + fechac + "-tipofechaN=" + fechac.getClass().getName());
      }

    }
    tablaDetalle.setContenido(ajustaDatos(registro,tiposDetalle,longitudDetalle,tablaDetalle.getPrecision()));

    // Validacion de datos usando logica del javabean existente  (detalle)
    // sugerencias - revisar para enviar mensaje mas diciente.
		/*
     if( !(tabla.validaContenido(registro,tiposDetalle , requeridoDetalle) &&  Utiles.VerificaNulos(registro, requeridoDetalle))) {    // datos validos para guardarse
     getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_VALIDACION_DETALLE,Notification.TYPE_ERROR_MESSAGE);
     }
     else   // datos fueron validos se procede a actualizar ó insertar segun sea el caso
     {
     */


    if (!agregandoDetalle) {   // acualizando: update
      int actual = Integer.parseInt(grillaDetalle.getTable().getValue().toString().replace("[", "").replace("]", ""));
      boolean actualizo = tablaDetalle.actualizar();
      if (actualizo) {
        getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_ACTUALIZACION);
        grillaDetalle.actualizaDatos();
        posicionaGrilla(grillaDetalle, actual);
      } else {
        getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_ACTUALIZACION, Notification.TYPE_ERROR_MESSAGE);
      }  // actualizacion
    } else //  agregando (insert)
    {
      boolean inserto=false;
      boolean existe=false;
      String  pk="";
      for (int i=0;i<tablaDetalle.getAtributosLLave().size();i++){
          pk+=tablaDetalle.getAtributosLLave().elementAt(i) +" = "+tablaDetalle.getContenido().elementAt(i).toString() + " ";
      }
        try {
            tablaDetalle.consultaP();
            if (!tablaDetalle.rs.next()) {
                inserto = tablaDetalle.insertar();
                existe = false;
            } else {
                inserto = false;
                existe = true;
            }
            if (inserto) {
                getWindow().showNotification(NOMBRE_APLICACION, MSGEXITO_CREACION);
                grillaDetalle.actualizaDatos();
                posicionaGrilla(grillaDetalle, 1);
            } else {
                if (existe) {
                    getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_CREACION_PK + pk, Notification.TYPE_ERROR_MESSAGE);
                } else {
                    getWindow().showNotification(NOMBRE_APLICACION, MSGERROR_CREACION, Notification.TYPE_ERROR_MESSAGE);
                }
            }  // actualizacion
        } catch (SQLException e) {
             logger.error("Error consultando llave primaria para detalle"+pk,e);
        } finally {
            DbUtil.closeResultSet(tablaDetalle.rs);
        }

    }
    agregandoDetalle = false;
    enFormaEdicionDetalle = false;
    consultarDetalle();
  }
  // } // valdiacion  del detalle

  /**
   * Agregar registro a tabla maestra y la deja en modo de edicion
   */
  private void agregarDetalle() {
    //Persistente nuevo = plantillaDetalle;  //  crea un nuevo javabena (con datos por defecto)       //********  CODIGO PLANTILLA  JSP a reemplazar con macro*********//

    logger.debug("Clase de plantilla detalle: " + tablaDetalle.getClass().getName());
    Persistente nuevo = null;
    try {
      nuevo = tablaDetalle.getClass().newInstance();         // obtiene una instancia vacia del mismo javabean del maestro
    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (IllegalAccessException e) {

      e.printStackTrace();
    }
    agregandoDetalle = true;
    posicionaGrilla(grillaDetalle, 1);
    tablaDetalle.setContenido(ajustaDatos(nuevo.getContenido(),tiposDetalle,longitudDetalle,nuevo.getPrecision()));  //carga el javabean temporal en actual (tabla) para su edicion)
    editarDetalle(Boolean.FALSE);
    enFormaEdicionDetalle = true;
    iFormaDetalle.setReadOnly(false);
    botonDeshacerD.setVisible(true);
    botonGuardarD.setVisible(true);
    botonAgregarD.setVisible(false);
    iFormaDetalle.requestRepaint();
    this.requestRepaintAll();     // en estudio si es necesario
  }

  @Override
  // ejecutado siempre antes del rendering - por en tiempo de contruccion no siempre se conoce el objeto app y derivados (windows)
  public void detach() {
    logger.debug(" ************ JFormFD detach ");
    if(tabla!=null){
      DbUtil.closeRecurso(tabla);
    }

    if(tablaDetalle!=null){
       DbUtil.closeRecurso(tablaDetalle);
    }
    super.detach(); // Must call.
  }

  /**
   * Returna la posicion (fila) actual de la grilla enviada
   *
   * @param grilla
   * @return
   */
  int getActual(GrillaService grilla) {
    int act=0;

      if (((Integer) grilla.getTable().getContainerDataSource().size()).intValue() > 0) {
          act = Integer.parseInt(grilla.getTable().getValue().toString().replace("[", "").replace("]", ""));
          rActual.setValue(grilla.getTable().getValue() + " de " + grilla.getTable().getContainerDataSource().size());
      } else {
          act = 0;
          rActual.setValue( "0" + " de " + grilla.getTable().getContainerDataSource().size());
      }
    if (act <= 0) {
      act = 1;
    }
    return act;
  }

  public void setcLayout(CustomLayout cLayout) {
    this.cLayout = cLayout;
  }

  public VerticalLayout getLayMaster() {
    return layMaster;
  }

  public VerticalLayout getLayDetalle() {
    return layDetalle;
  }

  public HorizontalLayout getBarraBotones() {
    return barraBotones;
  }

  public HorizontalLayout getBarraBotonesDetalle() {
    return barraBotonesDetalle;
  }

  public GrillaService getGrilla() {
    return grilla;
  }

  public GrillaService getGrillaDetalle() {
    return grillaDetalle;
  }

  public void setTituloMaestro(String tituloMaestro) {
    this.tituloMaestro = tituloMaestro;
  }

  public void setMisAlias(String[] misAlias) {
    this.misAlias = misAlias;
  }

  public void setMisTipos(String[] misTipos) {
    this.misTipos = misTipos;
  }

  public void setMiPresentacion(int[] miPresentacion) {
    this.miPresentacion = miPresentacion;
  }

  public void setLongitudMaestro(int[] longitudMaestro) {
    this.longitudMaestro = longitudMaestro;
  }

  public void setRequerido(int[] requerido) {
    this.requerido = requerido;
  }

  public void setTituloDetalle(String tituloDetalle) {
    this.tituloDetalle = tituloDetalle;
  }

  public void setAliasDetalle(String[] aliasDetalle) {
    this.aliasDetalle = aliasDetalle;
  }

  public void setTiposDetalle(String[] tiposDetalle) {
    this.tiposDetalle = tiposDetalle;
  }

  public void setPresentacionDetalle(int[] presentacionDetalle) {
    this.presentacionDetalle = presentacionDetalle;
  }

  public void setLongitudDetalle(int[] longitudDetalle) {
    this.longitudDetalle = longitudDetalle;
  }

  public void setRequeridoDetalle(int[] requeridoDetalle) {
    this.requeridoDetalle = requeridoDetalle;
  }

  public String getForma() {
    return forma;
  }

  public void setLlaveMaestro(Vector llaveMaestro) {
    this.llaveMaestro = llaveMaestro;
  }

  public void setLlaveDetalle(Vector llaveDetalle) {
    this.llaveDetalle = llaveDetalle;
  }

  public void setCombosMaestro(Vector combosMaestro) {
    this.combosMaestro = combosMaestro;
  }

  public void setTiposCombosMaestro(Vector tiposCombosMaestro) {
    this.tiposCombosMaestro = tiposCombosMaestro;
  }

  public void setValorCombosMaestro(Vector valorCombosMaestro) {
    this.valorCombosMaestro = valorCombosMaestro;
  }

  public void setCombosDetalle(Vector combosDetalle) {
    this.combosDetalle = combosDetalle;
  }

  public void setTiposCombosDetalle(Vector tiposCombosDetalle) {
    this.tiposCombosDetalle = tiposCombosDetalle;
  }

  public void setValorCombosDetalle(Vector valorCombosDetalle) {
    this.valorCombosDetalle = valorCombosDetalle;
  }

  public void setRadioMaestro(Vector radioMaestro) {
    this.radioMaestro = radioMaestro;
  }

  public void setTiposRadioMaestro(Vector tiposRadioMaestro) {
    this.tiposRadioMaestro = tiposRadioMaestro;
  }

  public void setRadioDetalle(Vector radioDetalle) {
    this.radioDetalle = radioDetalle;
  }

  public void setTiposRadioDetalle(Vector tiposRadioDetalle) {
    this.tiposRadioDetalle = tiposRadioDetalle;
  }

  public Persistente getTabla() {
    return tabla;
  }

  public void setTabla(Persistente tabla) {
    this.tabla = tabla;
  }

  public Persistente getTablaDetalle() {
    return tablaDetalle;
  }

  public void setTablaDetalle(Persistente tablaDetalle) {
    this.tablaDetalle = tablaDetalle;
  }

  public void setColsGrilla(int colsGrilla) {
    //this.colsGrilla = colsGrilla;
      this.colsGrilla = tabla.getElementos();
  }

  public int getColsGrilla() {
    return colsGrilla;
  }

    public String[] obtienePkMaestro() {
        String[] props = tabla.getAtributos();
        String[] pk = new String[tabla.getAtributosLLave().size()];
        tabla.inicializar();
        int actual = getActual(grilla);

        if (llaveMaestro != null) {         //SMJ2
            for (int i = 0; i < tabla.getAtributosLLave().size(); i++) {
                String CampoLlaveMaestra = llaveMaestro.get(i).toString();
                pk[i] = grilla.getTable().getItem(actual).getItemProperty(CampoLlaveMaestra).getValue().toString(); // obtiene el valor de la llave maestra
            }
        } else {

            for (int i = 0; i < tabla.getAtributosLLave().size(); i++) {
                pk[i] = grilla.getTable().getItem(actual).getItemProperty(props[i]).getValue().toString();
            }

        }

        logger.debug("PK=" + StringUtils.arrayToDelimitedString(pk, " ") + "-actual=" + actual);
        return pk;
    }

    private Vector ajustaDatos(Vector entrada,String [] misTipos, int [] longitud, int [] precision) {
        Vector resultado=new Vector();

        for (int i = 0; i < entrada.size(); i++) {
            int preci = 0;
            if (precision!=null) {
                if (i < precision.length) {
                    preci = precision[i];
                }
            }
            resultado.add(Utiles.formateaDatos( entrada.elementAt(i),misTipos[i], null,longitud[i],null,preci));
        }
        return resultado;
    }

    public void setAliasFormatter(String alias, CampoFormatter formatter) {
       if (this.aliasFormatters == null) {
         this.aliasFormatters = new HashMap<String, CampoFormatter>(2);
       }
       this.aliasFormatters.put(alias, formatter);
    }

    protected CampoFormatter obtainCampoFormatter(String tituloCampo) {
      if (this.aliasFormatters == null || !this.aliasFormatters.containsKey(tituloCampo)) {
        return new DefaultCampoFormatter();
      }
      return this.aliasFormatters.get(tituloCampo);
    }



}
