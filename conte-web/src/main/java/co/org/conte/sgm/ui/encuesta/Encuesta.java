package co.org.conte.sgm.ui.encuesta;

import co.org.conte.sgm.dao.PreguntaDao;
import co.org.conte.sgm.dao.RespuestaDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Opcion;
import co.org.conte.sgm.entity.Pregunta;
import co.org.conte.sgm.entity.Respuesta;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class Encuesta extends GenericForm{

    private Label mensaje;
    private PreguntaDao dao = new PreguntaDao();
    private RespuestaDao daoRespuesta = new RespuestaDao();
    List<Pregunta> preguntas;
        
    public Encuesta(Usuario usuario) {
        super(usuario);
        mensaje = new Label("Para el CONTE es muy importante contar con su opinión, por favor califique el servicio prestado por nuestros funcionarios");        
        init();
    }
    
    @Override
    protected void addListeners() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void addValidators() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void createForm() {       
        for(Pregunta p : preguntas){
            if(p.getEstado()){
                if(p.getAbierta()){
                    TextArea ta = new TextArea(p.getDescripcion());
                    form.addField(p.getCodigo(), ta);
                } else {
                    OptionGroup og = new OptionGroup(p.getDescripcion());
                    for(Opcion o : p.getOpcions()){
                        og.addItem(o);
                        og.setItemCaption(o, o.getDescripcion());
                    }                        
                    form.addField(p.getCodigo(), og);
                }
            }
        }                                
    }

    @Override
    protected void onClickReset() {
    
        for(Pregunta p : preguntas){
            if(p.getEstado()){
                form.getField(p.getCodigo()).setValue(null);                            
            }
        }            
    
    }

    @Override
    protected void process() {
        try {
            
            Respuesta respuesta;
            for(Pregunta p : preguntas){
                if(p.getEstado()){
                    if(p.getAbierta()){
                        respuesta = new Respuesta();
                        respuesta.setPregunta(p);
                        respuesta.setRespuesta(form.getField(p.getCodigo()).toString());        
                        daoRespuesta.insert(respuesta);
                    } else {
                        respuesta = new Respuesta();
                        respuesta.setPregunta(p);
                        respuesta.setOpcion((Opcion)form.getField(p.getCodigo()).getValue());                        
                        daoRespuesta.insert(respuesta);
                    }
                }
            }
//            ConfirmDialog.show(getWindow(), "", "Gracias por darnos su opinión",
//                    "Aceptar", "", new ConfirmDialog.Listener() {
//                        
//                        @Override
//                        public void onClose(ConfirmDialog dialog) {
//                            if (dialog.isConfirmed()) {
//                                getApplication().setUser(null);
//                            } 
//                        }
//                    });      
            launchSubwindow("Gracias por darnos su opinión");
        } catch (DaoException ex) {
            Logger.getLogger(Encuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void setRequired(boolean required) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void setMaxLength() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void validate() throws InvalidValueException {
        //throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    private void init(){
        try {
            preguntas = dao.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(Encuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
        createForm();
        removeAllComponents();
        addComponent(mensaje);
        addComponent(form);
    }
}