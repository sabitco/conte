package co.org.conte.sgm.service;

import java.io.File;

/**
 *
 * @author Andrés Mise Olivera
 */
public interface MailService {
    
    public void send(String to, String subject, String text);        
    public void send(String to, String subject, String text, File... attachments);  
    
}