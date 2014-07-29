package com.quasarbi.Generadores;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import com.quasarbi.ConexionBD.Conexion;
import java.sql.Connection;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporterParameter;

public class GeneracionBase {

    private String nombreFormato;
    private String nombreArchivo;
    private String consecutivo;
    private String rutaArchivo;
    private String extension;
    private File folder;
    public Map<String, Object> parametros;
    private InputStream plantillaFormato;
    private JasperPrint jasperPrint;
    private JRDocxExporter exporter;
    private JRPdfExporter exporterpdf;

    @SuppressWarnings("rawtypes")
    public void muestraParametros() {
        try {
            Iterator it = this.parametros.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                System.out.println(e.getKey() + ": " + e.getValue());
            }
        } catch (Exception e) {
            System.out.println("Se presento una excepcion recorriendo los parametros del archivo.");
        }
    }

    public void generaFormato() {
        this.nombreArchivo = this.nombreFormato + "-" + this.consecutivo + "." + this.extension;
        System.out.println("Generamos el archivo: " + this.nombreArchivo);
        muestraParametros();
        try {
            this.folder = new File(System.getProperty("java.io.tmpdir") + "/CONTE");
            if (!this.folder.exists()) {
                this.folder.mkdir();
            }
            this.rutaArchivo = this.folder.getAbsolutePath() + "/" + this.nombreArchivo;
            if (plantillaFormato == null) {
                System.out.println("El archivo se generara en la ruta: " + this.rutaArchivo);
            }

            this.jasperPrint = JasperFillManager.fillReport(plantillaFormato, (Map<String, Object>) this.parametros, new JREmptyDataSource());
            if (this.extension.equals("pdf")) {
                this.exporterpdf = new JRPdfExporter();
                this.exporterpdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporterpdf.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporterpdf.exportReport();
            } else {
                this.exporter = new JRDocxExporter();
                this.exporter.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporter.setParameter(JRDocxExporterParameter.FLEXIBLE_ROW_HEIGHT, Boolean.TRUE);
                this.exporter.setParameter(JRDocxExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporter.exportReport();
            }
            System.out.println("Se genero el archivo " + this.nombreArchivo + " correctamente.");
        } catch (JRException e) {
            System.out.println("Se presento un error generando el archivo: " + this.nombreArchivo + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void generaFormato(Connection c) throws JRException{
        this.nombreArchivo = this.nombreFormato + "-" + this.consecutivo + "." + this.extension;
        System.out.println("Generamos el archivo: " + this.nombreArchivo);
        muestraParametros();
        try {
            this.folder = new File(System.getProperty("java.io.tmpdir") + "/CONTE");
            if (!this.folder.exists()) {
                this.folder.mkdir();
            }
            this.rutaArchivo = this.folder.getAbsolutePath() + "/" + this.nombreArchivo;
            if (plantillaFormato == null) {
                System.out.println("El archivo se generara en la ruta: " + this.rutaArchivo);
            }

            this.jasperPrint = JasperFillManager.fillReport(plantillaFormato, (Map<String, Object>) this.parametros, c);
            if (this.extension.equals("pdf")) {
                this.exporterpdf = new JRPdfExporter();
                this.exporterpdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporterpdf.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporterpdf.exportReport();
            } else {
                this.exporter = new JRDocxExporter();
                this.exporter.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporter.setParameter(JRDocxExporterParameter.FLEXIBLE_ROW_HEIGHT, Boolean.TRUE);
                this.exporter.setParameter(JRDocxExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporter.exportReport();
            }
            System.out.println("Se genero el archivo " + this.nombreArchivo + " correctamente.");
        } catch (JRException e) {            
            System.out.println("Se presento un error generando el archivo: " + this.nombreArchivo + ": " + e.getMessage());            
            throw e;
        }
    }

    public void generaFormatoConConexion() {
        this.nombreArchivo = this.nombreFormato + "-" + this.consecutivo + "." + this.extension;
        System.out.println("Generamos el archivo: " + this.nombreArchivo);
        muestraParametros();
        try {
            this.folder = new File(System.getProperty("java.io.tmpdir") + "/CONTE");
            if (!this.folder.exists()) {
                this.folder.mkdir();
            }
            this.rutaArchivo = this.folder.getAbsolutePath() + "/" + this.nombreArchivo;
            System.out.println("El archivo se generara en la ruta: " + this.rutaArchivo);
            this.jasperPrint = JasperFillManager.fillReport(plantillaFormato, (Map<String, Object>) this.parametros, Conexion.getConnection());
            if (this.extension.equals("pdf")) {
                this.exporterpdf = new JRPdfExporter();
                this.exporterpdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporterpdf.setParameter(JRPdfExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporterpdf.exportReport();
            } else {
                this.exporter = new JRDocxExporter();
                this.exporter.setParameter(JRDocxExporterParameter.JASPER_PRINT, jasperPrint);
                this.exporter.setParameter(JRDocxExporterParameter.FLEXIBLE_ROW_HEIGHT, Boolean.TRUE);
                this.exporter.setParameter(JRDocxExporterParameter.OUTPUT_FILE_NAME, this.rutaArchivo);
                this.exporter.exportReport();
            }
            System.out.println("Se genero el archivo " + this.nombreArchivo + " correctamente.");
        } catch (Exception e) {
            System.out.println("Se presento un error generando el archivo: " + this.nombreArchivo + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getNombreFormato() {
        return nombreFormato;
    }

    public void setNombreFormato(String nombreFormato) {
        this.nombreFormato = nombreFormato;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public InputStream getPlantillaFormato() {
        return plantillaFormato;
    }

    public void setPlantillaFormato(InputStream plantillaFormato) {
        this.plantillaFormato = plantillaFormato;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
