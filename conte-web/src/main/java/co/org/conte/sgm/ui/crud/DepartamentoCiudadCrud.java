package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.DepartamentoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Ciudad;
import co.org.conte.sgm.entity.Departamento;
import co.org.conte.sgm.entity.Pais;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.TextField;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public class DepartamentoCiudadCrud extends GenericCrudMasterDetail<Departamento, Ciudad> {

    
    private TextField nombreCiudad;
    private TextField nombreDepartamento;
    
    public DepartamentoCiudadCrud(Usuario usuario) {
        super(Departamento.class, Ciudad.class);
        dao = new DepartamentoDao();
        nombreCiudad = new TextField("Nombre");
        nombreDepartamento = new TextField("Nombre");
        nombreCiudad.setMaxLength(60);
        nombreDepartamento.setMaxLength(60);
        createForm();
        setRequired(true);
    }

    @Override
    public void createDetailForm() {
        detailForm.addField("nombreCiudad", nombreCiudad);
    }

    @Override
    public void createTableDetail(){
        detailTable.addContainerProperty("Codigo", Integer.class,  null);     
        detailTable.addContainerProperty("Nombre", String.class,  null);     
        
        Set<Ciudad> titulos = entity.getCiudads();        
        if(titulos != null && !titulos.isEmpty()){
            for(Ciudad c : titulos){
                detailTable.addItem(new Object[]{
                    c.getCodigo(),
                    c.getNombre()               
                }, c);
            }            
        }        
    }
    
    @Override
    public void onClickClearDetail() {
        nombreCiudad.setValue("");
    }

    @Override
    public void processDetail() {
        entityDetail.setDepartamento(entity);
        entityDetail.setNombre(getValue(nombreCiudad));
    }

    @Override
    public void setDetailFields() {
        if(entityDetail!=null){
            nombreCiudad.setValue(entityDetail.getNombre());
        }
    }

    @Override
    public boolean validateDetail() {
        return nombreCiudad.isValid();
    }

    @Override
    public void addValidators() {
        
    }

    @Override
    public void createForm() {
        form.addField("nombreDepartamento", nombreDepartamento);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);  
        table.addContainerProperty("Nombre", String.class,  null);     
        List<Departamento> departamentos = getList();
        if(departamentos != null && !departamentos.isEmpty()){
            for(Departamento d : departamentos){
                table.addItem(new Object[]{                    
                    d.getCodigo(),                   
                    d.getNombre()
                }, d);
            }            
        }   
    }

    @Override
    public void onClickClear() {
        nombreDepartamento.setValue("");
    }

    @Override
    public void onClickCreate() {
        
    }

    @Override
    public void onClickDelete() {       
        
        ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea eliminar el"
                + " Departamento "+entity.getNombre()+ "?",
                "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                            try {
                                dao.delete(entity);
                                onClickReset();
                            } catch (DaoException ex) {
                                Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                    }
            });
       
    }

    @Override
    public void onClickEdit() {
        try {
            nombreDepartamento.setValue(entity.getNombre());
            entity = dao.findById(entity.getCodigo());
        } catch (DaoException ex) {
            Logger.getLogger(DepartamentoCiudadCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void process() {
        entity.setNombre(getValue(nombreDepartamento));
        entity.setPais(new Pais(57));
    }

    @Override
    public void setRequired(boolean required) {
        nombreCiudad.setRequired(required);
        nombreDepartamento.setRequired(required);
    }

    @Override
    public boolean validate() {
        return nombreDepartamento.isValid();
    }
    
}
