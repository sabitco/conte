package com.conte.reportEngine;

import com.vaadin.terminal.StreamResource.StreamSource;
import java.io.InputStream;

/**
 * Clase para el manejo de streams web en vaadin
 *
 * @author pedrorozo
 *
 */
public class ReportSource implements StreamSource {

  private static final long serialVersionUID = -6256723077925948776L;
  InputStream input = null;

  public ReportSource(InputStream mStream) {
    input = mStream;
  }
  int reloads = 0;

  @Override
  public InputStream getStream() {

    return input;
  }
}
