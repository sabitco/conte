///*
// * Informacion de Valoracion INFOVALMER, Bolsa de Valores de Colombia BVC
// * Copyright (C) 2012 Quasar Software Ltda.
// */
//package com.conte.common.ui.util;
//
//import com.conte.common.ui.GenericTab;
//import com.conte.common.ui.content.GenericContent;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.annotation.PostConstruct;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// *
// * @author Alejandro Riveros Cruz
// */
//
//@Component
//public class VaadinComponentFactoryImpl /*implements VaadinComponentFactory */{
///*
//  private final Logger logger = LoggerFactory.getLogger(getClass());
//  private Map<OpcionModulo, Constructor<? extends GenericContent>> genericContentConstuctorMap;
//  @Autowired
//  private OpcionModuloService opcionModuloServiceImpl;
//
//  public VaadinComponentFactoryImpl() {
//    logger.debug("Constructor VaadinComponentFactoryImpl");
//    genericContentConstuctorMap = new HashMap<OpcionModulo, Constructor<? extends GenericContent>>();
//  }
//
//  @Override
//  @PostConstruct
//  public void initFactory() {
//    logger.debug("initFactory VaadinComponentFactoryImpl");
//    List<OpcionModulo> opcionModuloList = opcionModuloServiceImpl.findOpcionesModulos();
//    for (OpcionModulo opcionModulo : opcionModuloList) {
//      try {
//        Class<? extends GenericContent> class1 = (Class<? extends GenericContent>) Class.forName(opcionModulo.getPagina().getNombre());
//        Constructor<? extends GenericContent> constructor = class1.getConstructor(GenericTab.class);
//        genericContentConstuctorMap.put(opcionModulo, constructor);
//      } catch (NoSuchMethodException nsme) {
//        logger.error("Error al obtener instancia de clase", nsme);
//      } catch (SecurityException se) {
//        logger.error("Error al obtener instancia de clase", se);
//      } catch (ClassNotFoundException cnfe) {
//        logger.error("Error al obtener instancia de clase", cnfe);
//      }
//    }
//  }
//
//  @Override
//  public GenericContent getVaadinComponent(OpcionModulo opcionModulo, GenericTab parentTab) {
//    try {
//      logger.debug("Call componentFactory for opcion modulo {}", opcionModulo);
//      GenericContent genericContent;
//      if ((genericContent = genericContentConstuctorMap.get(opcionModulo).newInstance(parentTab)) != null) {
//        genericContent.initForm();
//        genericContent.setOpcionModuloId(opcionModulo.getId());
//        return genericContent;
//      }
//    } catch (InstantiationException ie) {
//      logger.error("Error al obtener instancia de clase", ie);
//    } catch (IllegalAccessException iae) {
//      logger.error("Error al obtener instancia de clase", iae);
//    } catch (IllegalArgumentException iae) {
//      logger.error("Error al obtener instancia de clase", iae);
//    } catch (InvocationTargetException ite) {
//      logger.error("Error al obtener instancia de clase", ite);
//    } catch (NullPointerException npe) {
//      logger.error("Error al obtener instancia de clase", npe);
//      throw new InfovalmerSistemaException(npe.getMessage(), npe);
//    }
//    return new BienvenidaContent(parentTab);
//  }
//
//  public void setOpcionModuloServiceImpl(OpcionModuloService opcionModuloService) {
//    this.opcionModuloServiceImpl = opcionModuloService;
//  }*/
//}
