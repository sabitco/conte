/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui.form;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 *
 * @author jam
 */
public class FileUploadForm extends GenericContent  {
    
    private Upload upload;
    private Label label;

    public FileUploadForm(Usuario usuario) {
        super(usuario);
        final String inPath = "C:\\xampp\\";
            
        label = new Label();
        upload = new Upload(getMessage("file.upload.caption"), new Upload.Receiver() {           
            
            @Override
            public OutputStream receiveUpload(String filename, String mimeType) {
                FileOutputStream fos = null;
                
                try {
                    new File(inPath).mkdirs();
                    fos = new FileOutputStream(new File(inPath + filename));
                    logger.debug(inPath);
                } catch (FileNotFoundException ex) {
                    logger.error(getMessage("error.file_upload_failed", filename), ex);
                }
                return fos;
            }
        });
        
//        upload.setButtonCaption(getMessage("file.upload.button"));
        upload.addListener(new Upload.SucceededListener() {
            
            @Override
            public void uploadSucceeded(Upload.SucceededEvent event) {
                label.setCaption(getMessage("file.upload.success", event.getFilename()));
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
}