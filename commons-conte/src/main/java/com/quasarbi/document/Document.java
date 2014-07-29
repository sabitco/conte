package com.quasarbi.document;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;
import com.quasarbi.exception.DocumentException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author J4M0
 */
public enum Document implements Serializable {

    HOJA_ESTUDIO("Hoja de Estudio", "HojaEstudioSolicitud", "Documentos operativos",
            DocType.PDF, "getDataHojaEstudio", Conexion.getConnection()
    ),
    MATRICULA("Matricula", "anexo27Sec1", "Documentos operativos",
            DocType.DOCX, "getDataMatricula"
    ),
    RESOLUCION_AMPLIACION_APROBADA("Resolucion Ampliacion Aprobada",
            "resolucionAmpliacionAprobada", "Documentos operativos",
            DocType.DOCX, "getDataResolucionAmpliacionAprobada"
    ),
    RESOLUCION_PRIMER_VEZ("Resolución primer vez", "resolucionPrimeraVez",
            "Documentos operativos",
            DocType.DOCX, "getDataResolucionPrimerVez"
    );

    private Connection connection;
    private final Conexion conexion = new Conexion();
    private final Logger logger = LoggerFactory.getLogger(Document.class);
    private final DocType docType;
    private final String folder;
    private final String methodName;
    private final String name;
    private final String nameFormat;

    /**
     *
     * @param name
     * @param nameFormat Nombre del .jasper
     * @param folder Carpeta donde se dejara el documento generado, los posibles
     * valores son: <ul><li>Documentos operativos</li><li>4</li><li>5</li></ul>
     * @param docType <code>Enum</code> que contiene la extención y el MIME del
     * documento a generar
     * @param methodName Nombre del método que devuelve los datos, este debe
     * estar en la clase <code>Conexion</code>
     */
    Document(String name, String nameFormat,
            String folder, DocType docType,
            String methodName) {
        this.name = name;
        this.nameFormat = nameFormat;
        this.folder = folder;
        this.docType = docType;
        this.methodName = methodName;
    }

    /**
     * Constructor para los formatos que para generarse es necesario una
     * conexión a la base de datos
     *
     * @param name
     * @param nameFormat Nombre del .jasper
     * @param folder Carpeta donde se dejara el documento generado, los posibles
     * valores son: <ul><li>Documentos operativos</li><li>4</li><li>5</li></ul>
     * @param docType <code>Enum</code> que contiene la extención y el MIME del
     * documento a generar
     * @param methodName Nombre del método que devuelve los datos, este debe
     * estar en la clase <code>Conexion</code>
     * @param connection
     */
    Document(String name, String nameFormat,
            String folder, DocType docType,
            String methodName, Connection connection) {
        this.connection = connection;
        this.name = name;
        this.nameFormat = nameFormat;
        this.folder = folder;
        this.docType = docType;
        this.methodName = methodName;
    }

    /**
     * Genera el documento
     *
     * @param document documento del técnico al que se le generara el documento
     * @throws DocumentException
     */
    public void generete(String document) throws DocumentException {
        String directorioAlfresco = null;
        try {
            Method method = conexion.getClass().getDeclaredMethod(methodName, document.getClass());
            Map<String, Object> data = (Map<String, Object>) method.invoke(conexion, document);
            if (data.isEmpty()) {
                throw new DocumentException("No existen datos para este documento");
            }
            logger.debug("Se va a generar documento con los datos {}", data);
            ConexionBase conexionBase = new ConexionBase();
            directorioAlfresco = "CONTE/" + data.get("numeroRadicacion") + " "
                    + document + " " + data.get("nombreSolicitante") + " "
                    + data.get("apellidoSolicitante") + "/" + folder;
            GeneracionBase generador = new GeneracionBase();
            generador.parametros = data;
            generador.setNombreFormato(nameFormat);
            generador.setPlantillaFormato(this.getClass().getResourceAsStream(
                    "/jasper/" + nameFormat + ".jasper"));
            generador.setExtension(docType.getExtension());
            generador.setConsecutivo((String) data.get("numeroRadicacion"));

            if (connection == null) {
                generador.generaFormato();
            } else {
                generador.generaFormato(connection);
            }

            conexionBase.setRutaArchivo(generador.getRutaArchivo());
            conexionBase.setNombreArchivo(generador.getNombreArchivo());
            conexionBase.setTipoArchivo(docType.getMime());
            conexionBase.setDirectorioAlfresco(directorioAlfresco);
            conexionBase.subeArchivo();
        } catch (CmisObjectNotFoundException confe) {
            logger.error("No se encontro repositorio {}", confe.getMessage());
            throw new DocumentException("No se encontro repositorio del técnico " + directorioAlfresco);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage());
            throw new DocumentException("Error al intentar generar el documento");
        } catch (SecurityException e) {
            logger.error(e.getMessage());
            throw new DocumentException("Error al intentar generar el documento");
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
            throw new DocumentException("Error al intentar generar el documento");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            throw new DocumentException("Error al intentar generar el documento");
        } catch (InvocationTargetException e) {
            logger.error("Invocation target exception caused by ", e.getCause());
            throw new DocumentException("Error al intentar generar el documento");
        } catch (JRException jre) {
            logger.error("JRException ", jre.getCause());
            throw new DocumentException("Error al generar el documento");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
