/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.InstitucionEducativa;
import co.org.conte.sgm.entity.TituloAcademico;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.NativeSelect;
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
public class InstitucionEducativaCrud extends GenericCrudMasterDetail<InstitucionEducativa, 
        TituloAcademico> {

    private String activo;
    private TextField descripcionTitulo;
    private String inactivo;
    private TextField nombre;
    private TextField nombreTitulo;    
    private TextField registroMen;
    private TextField sigla;
    private NativeSelect estado;
    
    public InstitucionEducativaCrud(Usuario usuario) {
        super(InstitucionEducativa.class, TituloAcademico.class);
        super.delete.setCaption("Inactivar");
        init();
    }
    
    @Override
    public void addValidators(){
        
    }
    
//    @Override
//    public void createDetail(){
//        entityDetail = new TituloAcademico();
//        createTableDetail();
//        createDetailForm();
//        addDetail();        
//    }
    
    @Override
    public void createForm() {
        form.addField("sigla", sigla);
        form.addField("Nombre", nombre);
        form.addField("Registro MEN", registroMen);
        form.addField("Estado", estado);
    }
    
    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);     
        table.addContainerProperty("Sigla", String.class,  null);   
        table.addContainerProperty("Nombre", String.class,  null);     
        table.addContainerProperty("Registro MEN", String.class,  null);   
        table.addContainerProperty("Estado", String.class,  null);   
        List<InstitucionEducativa> instituciones = getList();
        if(instituciones != null && !instituciones.isEmpty()){
            for(InstitucionEducativa ie : instituciones){
                table.addItem(new Object[]{                    
                    ie.getCodigo(),
                    ie.getSigla(),
                    ie.getNombre(),
                    ie.getRegistroMen(),
                    (ie.getEstado()!=null ? (ie.getEstado() ? "Activo" : "Inactivo") : "")
                }, ie);
            }            
        }         
    }
    
    @Override
    public void createTableDetail(){
        detailTable.addContainerProperty("Codigo", Integer.class,  null);     
        detailTable.addContainerProperty("Nombre", String.class,  null);     
        detailTable.addContainerProperty("Descripción", String.class,  null);        
        
        Set<TituloAcademico> titulos = entity.getTituloAcademicos();        
        if(titulos != null && !titulos.isEmpty()){
            for(TituloAcademico ta : titulos){
                detailTable.addItem(new Object[]{
                    ta.getCodigo(),
                    ta.getNombre(),
                    ta.getDescripcion(),                    
                }, ta);
            }            
        }        
    }

    @Override
    public void onClickClear() {
        nombre.setValue("");
        registroMen.setValue("");     
        sigla.setValue("");
        estado.setValue(null);
    }

    @Override
    public void onClickCreate() {
        entity = new InstitucionEducativa();
    }

    @Override
    public void onClickDelete() {
        if(entity.getEstado()){           
            ConfirmDialog.show(getWindow(), "Confirmar", "¿Desea inactivar la"
                    + " Institucion "+entity.getNombre()+ "?",
                "Confirmar", "Cancelar", new ConfirmDialog.Listener() {
                    @Override
                    public void onClose(ConfirmDialog dialog) {
                        if (dialog.isConfirmed()) {
                             entity.setEstado(Boolean.FALSE);
                            try {
                                dao.update(entity);
                                onClickReset();
                            } catch (DaoException ex) {
                                Logger.getLogger(UsuarioCrud.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } 
                    }
            });
        } else {
            launchSubwindow("Ya se encuentra Inactivo");
        }
    }

    @Override
    public void onClickEdit() {
        nombre.setValue(entity.getNombre());
        registroMen.setValue(entity.getRegistroMen());
        sigla.setValue(entity.getSigla());
        estado.setValue(entity.getEstado() ? activo : inactivo);
                        
        
    }
    
    @Override
    public void onClickClearDetail(){
        descripcionTitulo.setValue("");
        nombreTitulo.setValue("");
    }

    @Override
    public void process() {
        entity.setNombre(getValue(nombre));
        entity.setRegistroMen(getValue(registroMen));
        entity.setSigla(getValue(sigla));
        entity.setEstado(getValue(estado).equalsIgnoreCase("Activo"));
    }
    
    @Override
    public void processDetail(){
        entityDetail.setDescripcion(getValue(descripcionTitulo));        
        entityDetail.setNombre(getValue(nombreTitulo));
        entityDetail.setInstitucionEducativa(entity);
    }

    @Override
    public void setDetailFields(){
        if(entityDetail!=null && entityDetail.getNombre() != null
                && entityDetail.getDescripcion() !=null){
            nombreTitulo.setValue(entityDetail.getNombre());
            descripcionTitulo.setValue(entityDetail.getDescripcion());
            
        }
        
    }
    
    @Override
    public void setRequired(boolean required) {
        descripcionTitulo.setRequired(required);
        estado.setRequired(required);
        nombre.setRequired(required);
        nombreTitulo.setRequired(required);
        sigla.setRequired(required);
    } 

    @Override
    public boolean validate() {
        return (nombre.isValid() && registroMen.isValid() && sigla.isValid()
                && estado.isValid());
    }
    
    @Override
    public boolean validateDetail(){
        return (nombreTitulo.isValid() && descripcionTitulo.isValid());
    }
    
    @Override
    public void createDetailForm(){
        detailForm.addField("nombre", nombreTitulo);
        detailForm.addField("descripcion", descripcionTitulo);        
    }
    
    private void init() {
        activo = "Activo";
        descripcionTitulo = new TextField("Descripcion:");        
        inactivo = "Inactivo";
        nombre = new TextField("Nombre:");
        nombreTitulo = new TextField("Nombre:");
        nombreTitulo.setWidth("280px");
        registroMen = new TextField("Registro MEN:");
        sigla = new TextField("Sigla:");
        estado = new NativeSelect("Estado");
        estado.addItem(activo);
        estado.addItem(inactivo);        
        createForm();
        setRequired(true);         
        setMaxLengthMaster();
        setMaxLengthDetail();
    }   
    
    private void setMaxLengthDetail(){
        nombreTitulo.setMaxLength(200);        
        descripcionTitulo.setMaxLength(255);
    }
    
    private void setMaxLengthMaster(){
        nombre.setMaxLength(50);
        registroMen.setMaxLength(45);
        sigla.setMaxLength(20);        
    }
}