package co.org.conte.sgm.document;

import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericContent;
import com.quasarbi.document.Document;
import com.quasarbi.exception.DocumentException;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author J4M0
 */
public class GenerateDocument extends GenericContent {
    
    private Button button;
    private ComboBox comboBox;
    private List<Document> documents;
    private TextField textField;

    public GenerateDocument(Usuario usuario) {        
        super(usuario);
        init();
    }
    
    private void addListeners(){   
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (comboBox.getValue() != null) {
                    if (textField.getValue() != null && !textField.getValue().toString().trim().isEmpty()) {
                        try {
                            Document d = ((Document) comboBox.getValue());
                            d.generete(textField.getValue().toString());
                            launchSubwindow("Se ha generado correctamente el documento");
                        } catch (DocumentException ex) {
                            launchSubwindow(ex.getMessage());
                        }
                    } else {
                        launchSubwindow("El valor de Documento técnico no puede estar vacio");
                    }
                } else {
                    launchSubwindow("El valor de Documento a generar no puede estar vacio");
                }
            }
        });
    }
    
    private void addComponents(){
        addComponent(textField);
        addComponent(comboBox);
        addComponent(button);
    }
    
    private void init(){
        documents = Arrays.asList(Document.values());  
        button = new Button("Generar");
        comboBox = new ComboBox("Documento a generar", documents);
        textField = new TextField("Documento técnico");
        addComponents();
        addListeners();        
    }       
    
    
}