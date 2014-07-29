/*
 * Informaci�n de Valoraci�n INFOVALMER, Bolsa de Valores de Colombia BVC
 * Copyright (C) 2012 Quasar Software Ltda.
 */

package co.org.conte.sgm.ui;

import co.org.conte.sgm.entity.RolPagina;
import com.vaadin.data.Item;
import com.vaadin.ui.Tree;
import java.util.Set;

/**
 * Clase que se encarga de construir el men� de cada perfil.
 *
 * @author Andr�s Mise Olivera [amise@quasarbi.com]
 */
public final class MenuSection extends Tree {

  /**
   * Atributos de MenuSection.
   */
//  Logger logger = LoggerFactory.getLogger(getClass());
//  @Resource
//  protected MessageSource messageSource;
  public static final Object MENU_PROPERTY_NAME = "name";
  private Set<RolPagina> rolPaginas;

  /**
   * Constructor del men� de un perfil dado como par�metro.
   * @param perfil
   */
  public MenuSection(Set<RolPagina> rolPaginas) {
    this.rolPaginas = rolPaginas;
    init();
  }

  /**
   * M�todo que llama al constructor del men� y establece propiedades del contenedor.
   */
  public void init() {
    mainMenu();
    setItemCaptionPropertyId(MENU_PROPERTY_NAME);
    setSelectable(false);
    setNullSelectionAllowed(false);
  }

  /**
   * M�todo que construye el men� principal.
   */
  public void mainMenu() {
    Item item = null;
    addContainerProperty(MENU_PROPERTY_NAME, String.class, null);
    for (RolPagina opcionModulo : rolPaginas) {        
        item = addItem(opcionModulo.getPagina());
        item.getItemProperty(MENU_PROPERTY_NAME).setValue(opcionModulo.getPagina().getNombre());
    }
  }

  /**
   * Agrega los hijos que tiene un �tem padre.
   * @param opcionPadre
   */
//  public void addChilds(OpcionModulo opcionPadre) {
//    for (OpcionModulo opcionHijo : opcionesModuloMenu) {
//      if (opcionHijo.getOpcionDepende()!= null && opcionHijo.getOpcionDepende().equals(opcionPadre.getId()) && !containsId(opcionHijo)) {
//        Item hijo = addItem(opcionHijo);
//        hijo.getItemProperty(MENU_PROPERTY_NAME).setValue(opcionHijo.getNombre());
//        setParent(opcionHijo, opcionPadre);
//        if (!isParent(opcionHijo)) {
////          logger.info("MenuSection| ------ Item hijo: {}", opcionHijo.getNombre());
//          setChildrenAllowed(opcionHijo, false);
//        } else {
////          logger.info("MenuSection| ---- Item padre : {}, e hijo de: {}" ,opcionHijo.getNombre(), opcionPadre.getNombre());
//          addChilds(opcionHijo);
//        }
//      }
//    }
//  }

  /**
   * Indica si el �tem es padre.
   * @param opcionPadre
   * @return true si el �tem tiene hijos, false en caso contrario.
   */
//  public boolean isParent(OpcionModulo opcionPadre) {
//    for (OpcionModulo opciones : opcionesModuloMenu) {
//      if (opcionPadre.getId().equals(opciones.getOpcionDepende())) {
//        return true;
//      }
//    }
//    return false;
//  }

  /**
   * Indica si el �tem es hijo.
   * @param opcionHijo
   * @return true si el �tem es hijo, false en caso contrario.
   */
//  public boolean isChild(OpcionModulo opcionHijo) {
//    if (opcionHijo.getOpcionDepende() != null) {
//      return true;
//    }
//    return false;
//  }

  /**
   * M�todo que colapsa todos los �tems raices del men�.
   */
  public void collapseItems(){
//    for (OpcionModulo opcionModulo : opcionesModuloMenu) {
//      if (opcionModulo.getOpcionDepende() == null) {
//        collapseItemsRecursively(opcionModulo);
//      }
//    }
  }
}