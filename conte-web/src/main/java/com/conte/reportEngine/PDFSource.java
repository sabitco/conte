package com.conte.reportEngine;

import com.vaadin.terminal.StreamResource.StreamSource;
import java.io.InputStream;

/**
 * Maneja streams (datos binarios) para transmision desde el servidor hacia el navegador
 * @author pedrorozo
 *
 */
public class PDFSource implements StreamSource {
	
	private static final long serialVersionUID = -6256723077925948776L;
	 InputStream input = null; 
	
	public PDFSource(InputStream mStream) {
		input = mStream;
	}
	int reloads = 0;
	
	@Override
	public InputStream getStream() {
		  
	return input;
	}

}

