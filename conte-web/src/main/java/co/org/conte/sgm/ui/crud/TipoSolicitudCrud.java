package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.TipoSolicitud;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.util.List;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class TipoSolicitudCrud extends GenericCrud<TipoSolicitud> {
    
    private String activo;
    private NativeSelect estado;
    private String inactivo;
    private TextField nombre;
    private TextField porcentaje;
    
    public TipoSolicitudCrud(Usuario usuario) {
        super(TipoSolicitud.class);
        delete.setCaption("Inactivar");
        activo = "Activo";
        inactivo = "Inactivo";
        nombre = new TextField("Nombre:");
        porcentaje = new TextField("Porcentaje");
        estado = new NativeSelect("Estado:");        
        estado.addItem(activo);
        estado.addItem(inactivo);
        addValidators();
        createForm();
    }

    @Override
    public void addValidators() {
        porcentaje.setImmediate(true);
        porcentaje.addValidator(new DoubleValidator("Solo puede ingresar numeros enteros"));
    }

    @Override
    public void createForm() {
        form.addField("nombre", nombre);
        form.addField("porcenteje", porcentaje);
        form.addField("estado", estado);
        setRequired(true);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);     
        table.addContainerProperty("Nombre", String.class,  null);   
        table.addContainerProperty("Porcentaje", Float.class,  null);     
        table.addContainerProperty("Estado", String.class,  null);     
        List<TipoSolicitud> solicitudes = getList();
        if(solicitudes != null && !solicitudes.isEmpty()){
            for(TipoSolicitud ts : solicitudes){
                table.addItem(new Object[]{                    
                    ts.getCodigo(),
                    ts.getNombre(),
                    (ts.getPorcentaje()*100),
                    ts.getEstado() ? "Activo" : "Inactivo"
                }, ts);
            }            
        }    
    }

    @Override
    public void onClickClear() {
        nombre.setValue("");
        porcentaje.setValue(0);
    }

    @Override
    public void onClickCreate() {
    }

    @Override
    public void onClickDelete() {
        if(entity.getEstado()){
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar el Tipo de Solicitud?",
            "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                @Override
                public void onClose(ConfirmDialog dialog) {
                    if (dialog.isConfirmed()) {
                        try {
                            entity.setEstado(false);
                            dao.update(entity);
                            onClickReset();
                        } catch (DaoException ex) {
                            logger.error("Error al actualizar estado", ex);
                        }
                    } 
                }
            });       
        } else {
            launchSubwindow("Ya se encuentra Inactiva");
        }
    }

    @Override
    public void onClickEdit() {
        estado.setValue(entity.getEstado() ? activo : inactivo);
        nombre.setValue(entity.getNombre());
        porcentaje.setValue((entity.getPorcentaje()*100));
    }

    @Override
    public void process() {
        if(validate()){
            entity.setNombre(getValue(nombre));
            entity.setPorcentaje(Float.parseFloat(getValue(porcentaje))/100);
            entity.setEstado(getValue(estado).equals(activo));
        } else {
            launchSubwindow(getMessage("error.form"));
        }
    }

    @Override
    public void setRequired(boolean required) {
        estado.setRequired(required);
        nombre.setRequired(required);        
        porcentaje.setRequired(required);        
    }

    @Override
    public boolean validate() {
        return nombre.isValid() && porcentaje.isValid() && estado.isValid();
    }
}