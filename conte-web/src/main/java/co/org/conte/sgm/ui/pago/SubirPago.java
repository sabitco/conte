/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui.pago;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.service.PagoService;
import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author jam
 */
public class SubirPago extends GenericContent {
    
    private Upload upload;
    private Label label;
    public File file;

    public SubirPago(Usuario usuario) {
        super(usuario);
//        
        final String inPath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
                  
        label = new Label("", Label.CONTENT_XHTML);
        upload = new Upload(getMessage("file.upload.caption"), new Upload.Receiver() {           
            
             
            
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                 FileOutputStream fos = null;
                 
                try {
                    file =new File (inPath + filename);
                    fos = new  FileOutputStream(file);
                } catch (IOException ex) {
                    logger.error(getMessage("error.file_upload_failed", filename), ex);
                    file = null;
                } 
                return fos;
            }
        });
        
        upload.addListener(new Upload.SucceededListener() {
            
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {                
                label.setValue(readFile());
            }
        });
        
        upload.addListener(new Upload.FailedListener() {
            
            @Override
            public void uploadFailed(Upload.FailedEvent event) {
                label.setCaption(getMessage("file.upload.error", event.getFilename()));
            }
        });
        
        upload.setStyleName("upload-vaadin clearfix");
        addComponent(upload);
        addComponent(label);
        
    }
    
    public String readFile(){
        String result = "Se ha agregado el pago para los siguientes documentos:</br>";
        String noEncontrados = "NO se han encontrado solicitudes para los siguientes documentos:</br>";
        
        FileReader fr = null;
        BufferedReader br = null;
        PagoService servicePago = new PagoService();
        
        try {
            fr = new FileReader (file);            
            br = new BufferedReader(fr);
                      
            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!=null) {
                System.out.println("SE ENVIA => "+linea);
                String[] split = linea.split(";");
//                System.out.println("\n\n");
                String document = split[6].replaceFirst("[0]", "");
                while(document.startsWith("0")){
                    document = document.replaceFirst("[0]", "");
                }
                if(servicePago.addPago(split)){                    
                    result = result + (document + "<br>" );
                } else {
                    noEncontrados = noEncontrados + (document + "<br>");
                }
            }                    
        } catch(Exception e){
            e.printStackTrace();
        } finally {
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
        return result + "<br>" + noEncontrados;
    }   
}