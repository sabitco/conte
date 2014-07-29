package com.quasarbi.Generadores;

import com.quasarbi.ConexionBD.Conexion;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConexionBase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String nombreArchivo;
    private String rutaArchivo;
    private String directorioAlfresco;
    private String tipoArchivo;
    private Session session;
    private Folder root;

    public void obtieneSession() {
        Conexion c = new Conexion();
        Map<String, String> parameter = new HashMap<String, String>();
        SessionFactory sessionFactory = SessionFactoryImpl.newInstance();
        parameter.put(SessionParameter.USER, c.getParameter("user"));
        parameter.put(SessionParameter.PASSWORD, c.getParameter("password"));
        parameter.put(SessionParameter.ATOMPUB_URL, c.getParameter("url"));
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        logger.debug("parametros de conexión Alfresco", parameter);
        try {            
            Repository soleRepository = sessionFactory.getRepositories(parameter).get(0);
            logger.debug("parametros de conexión Alfresco", parameter);
            this.session = soleRepository.createSession();
            this.root = session.getRootFolder();
            logger.info("Se realiza conexión a la raiz del repositorio {}", root.getName());            
        } catch (Exception e) {            
            logger.error("No se pudo realizar la conexión al repositorio Alfresco {}", e.getMessage());
        }
    }

    public void subeArchivo() {
        obtieneSession();
        Document doc = buscaArchivo();
        InputStream pdfStream = null;
        Folder fol = null;
        if (doc == null) {

                fol = (Folder) session.getObjectByPath(root.getPath() + this.directorioAlfresco);
                System.out.println("directorio en Alfresco: " + fol.getPath());


          
        }
        System.out.println("Se inicia el proceso de enlace entre el archivo generado: " + this.nombreArchivo + " y el repositorio en alfresco.");
        try {
            Map<String, Object> propsdoc = new HashMap<String, Object>();
            propsdoc.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
            propsdoc.put(PropertyIds.NAME, this.nombreArchivo);

            System.out.println("Iniciamos la carga del flujo de datos del archivo en disco.");
            pdfStream = new FileInputStream(this.rutaArchivo);

            //BigInteger bi = BigInteger.valueOf(pdfStream.read());
            BigInteger bi = new BigInteger("2");
            ContentStream cm = new ContentStreamImpl(this.nombreArchivo, bi, this.tipoArchivo, pdfStream);

            try {
                if (doc != null) {
                    doc.checkIn(false, null, cm, VersioningState.MAJOR.toString());
                    System.out.println("Se actualizo el archivo en el repositorio correctamente.");
                } else {
                    if (fol != null) {
                        fol.createDocument(propsdoc, cm, VersioningState.MAJOR);
                        System.out.println("Se cargo el archivo en el repositorio correctamente.");
                    }
                }
            } catch (Exception e) {
                System.out.println("No se pudo realizar la operacion sobre el archivo: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Se presento un error leyendo el archivo en disco: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Se presento un error de lectura o escritura: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Se presento un error en el proceso: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Document buscaArchivo() {
        Document doc = null;
        System.out.println("Se verifica si existe el archivo: " + this.nombreArchivo + ".");
        try {
            doc = (Document) session.getObjectByPath(root.getPath() + this.directorioAlfresco + "/" + this.nombreArchivo);
            System.out.println("El archivo " + this.nombreArchivo + " ya existe en el repositorio.");
        } catch (Exception e) {
            if (e.getMessage().equals("No Encontrado")) {
                System.out.println("No se pudo encontrar el archivo: " + e.getMessage());
            } else {
                System.out.println("Se presento un error en la busqueda del archivo: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return doc;
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

    public String getDirectorioAlfresco() {
        return directorioAlfresco;
    }

    public void setDirectorioAlfresco(String directorioAlfresco) {
        this.directorioAlfresco = directorioAlfresco;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }
}