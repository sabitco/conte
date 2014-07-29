/*
 * Informacion de Valoracion INFOVALMER, Bolsa de Valores de Colombia BVC
 * Copyright (C) 2012 Quasar Software Ltda.
 */
package com.conte.reportEngine.formatter;

import java.util.Map;

/**
 *
 * @author Alejandro Riveros Cruz
 * @author Roger Padilla Camacho
 */
public class DefaultCampoFormatter implements CampoFormatter {

  @Override
  public String format(String campo, Map<String, Object> row) {
    Object val = row.get(campo);
    return val == null ? "" : val.toString();
  }
}
