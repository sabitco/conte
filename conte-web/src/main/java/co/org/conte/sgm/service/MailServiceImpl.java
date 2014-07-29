package co.org.conte.sgm.service;

import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 *
 * @author Andrés Mise Olivera
 */
@Service
public class MailServiceImpl implements MailService {
    
    private static final File[] NO_ATTACHMENTS = null;  
    private String from;  
    private final Logger logger;
    private JavaMailSenderImpl mailSender;  
    
    public MailServiceImpl() {
        logger = LoggerFactory.getLogger(getClass());
    }
    
    /**
     * Envío de email  
     * @param to correo electrónico del destinatario
     * @param subject asunto del mensaje
     * @param text cuerpo del mensaje 
     */
    public void send(String to, String subject, String text) {
        send(to, subject, text, NO_ATTACHMENTS); 
    }

    public void send(String to, String subject, String text, File... attachments) {
        // chequeo de parámetros   
        Assert.hasLength(to, "email 'to' needed");  
        Assert.hasLength(subject, "email 'subject' needed");  
        Assert.hasLength(text, "email 'text' needed");  
  
        // asegurando la trazabilidad  
        if (logger.isDebugEnabled()) {  
            final boolean usingPassword = !"".equals(mailSender.getPassword());  
            logger.debug("Sending email to: '" + to + "' [through host: '" + mailSender.getHost() + ":"  
                    + mailSender.getPort() + "', username: '" + mailSender.getUsername() + "' usingPassword:"  
                    + usingPassword + "].");  
        }  
          
        // plantilla para el envío de email  
        final MimeMessage message = mailSender.createMimeMessage();  
  
        try {  
            // el flag a true indica que va a ser multipart  
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);  
              
            // settings de los parámetros del envío  
            helper.setTo(to);  
            helper.setSubject(subject);  
            helper.setFrom(getFrom());  
            //helper.setText(text);  
            helper.setText(text, true);  
            
            // adjuntando los ficheros  
            if (attachments != null) {  
                for (int i = 0; i < attachments.length; i++) {  
                    FileSystemResource file = new FileSystemResource(attachments[i]);  
                    helper.addAttachment(attachments[i].getName(), file);  
                    if (logger.isDebugEnabled()) {  
                        logger.debug("File '" + file + "' attached.");  
                    }  
                }  
            }  
  
        } catch (MessagingException e) {  
            logger.error("Error enviando correo electronico", e);
        }            
        // el envío  
        this.mailSender.send(message);
    }
    
    public String getFrom() {  
        return from;  
    }  
    
    public void setFrom(String from) {  
        this.from = from;  
    }  
  
    public void setMailSender(JavaMailSenderImpl mailSender) {  
        this.mailSender = mailSender;  
    } 
}