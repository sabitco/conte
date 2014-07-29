package com.quasarbi.service;

import com.quasarbi.ConexionBD.Conexion;
import com.quasarbi.Generadores.ConexionBase;
import com.quasarbi.Generadores.GeneracionBase;
import com.quasarbi.exception.DocumentException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author J4M0
 */
public class DocumentGenerator implements Serializable {

    public static final int RESOLUCION_PRIMER_VEZ = 0;
    private final Conexion conexion;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DocumentGenerator() {
        conexion = new Conexion();
    }

    public void genereteDocument(int documentType, String document)
            throws DocumentException {
        switch (documentType) {
            case 0:
                genereteResolucionPrimerVez(document);
                break;
            default:
                throw new DocumentException("No se encuentra el tipo de documento");
        }
    }

    public void genereteResolucionPrimerVez(String document) throws DocumentException {
        String directorioAlfresco = null;
        try {
            Map<String, Object> data = conexion.getDataResolucionPrimerVez(document);
            if (data.isEmpty()) {
                throw new DocumentException("No existen datos para este documento");
            }
            logger.debug("Se va a generar resolucion primer vez con los datos {}", data);
            ConexionBase conexionBase = new ConexionBase();

            directorioAlfresco = "CONTE/" + data.get("numeroRadicacion") + " "
                    + document + " " + data.get("nombreSolicitante") + " "
                    + data.get("apellidoSolicitante") + "/Documentos operativos";

            GeneracionBase generador2 = new GeneracionBase();
            generador2.parametros = data;
            generador2.setNombreFormato("resolucionPrimeraVez");
            generador2.setPlantillaFormato(this.getClass().getResourceAsStream(
                    "/jasper/resolucionPrimeraVez.jasper"));
            generador2.setExtension("docx");
            generador2.generaFormato();

            conexionBase.setRutaArchivo(generador2.getRutaArchivo());
            conexionBase.setNombreArchivo(generador2.getNombreArchivo());
            conexionBase.setTipoArchivo("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            conexionBase.setDirectorioAlfresco(directorioAlfresco);

            conexionBase.subeArchivo();
        } catch (CmisObjectNotFoundException confe) {
            logger.error("No se encontro repositorio {}", confe.getMessage());
            throw new DocumentException("No se encontro repositorio del técnico " + directorioAlfresco);
        } catch (SQLException e) {
            logger.error("Error al consultar base de datos {}", e.getMessage());
            throw new DocumentException("Error al consultar base de datos");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new DocumentException("Error al intentar generar el documento");
        }
    }
}
