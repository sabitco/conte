package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.BaseDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.BaseEntity;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.Table;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jam
 */
public abstract class GenericCrudMasterDetail<M extends BaseEntity, D extends BaseEntity> extends GenericCrud<M> {

    protected Button createDetail;
    private Class<D> classDetail;
    protected BaseDao<D> daoDetail; 
    protected Button deleteDetail;
    protected Table detailTable;
    protected Button editDetail;
    protected D entityDetail;
    protected Form detailForm;
    private List<D> listDetail;
    
    public GenericCrudMasterDetail(Class<M> clase, Class<D> classDetail) {
        super(clase);        
        this.classDetail = classDetail;
        daoDetail = new BaseDao<D>(classDetail);         
    }
    
    public final void createDetail(){
        try {
            entityDetail = classDetail.newInstance();
            createTableDetail();
            createDetailForm(); 
            addDetail();
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericCrudMasterDetail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericCrudMasterDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public abstract void createDetailForm();
    public abstract void onClickClearDetail();
    public abstract void processDetail();
    public abstract void setDetailFields();
    public abstract boolean validateDetail();
    
    public void addDetail(){
        addComponent(detailTable);
        addComponent(detailForm);
    }
    
    @Override
    protected void addListeners(){        
        clear.setImmediate(true);
        this.clear.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                onClickClear();
            }
        });
        
        create.setImmediate(true);
        this.create.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    entity = (M)clase.newInstance();
                    message.setCaption("");
                    action = 'C';
                    removeAllComponents();
                    addComponent(form);
                    onClickClear();
                } catch (InstantiationException ex) {
                    Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        createDetail.setImmediate(true);
        this.createDetail.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                if(validateDetail()){                    
                    try {                    
                        entityDetail = (D) classDetail.newInstance();
                        processDetail();
                        daoDetail.insert(entityDetail);
                        entity = dao.findById(entity.findId());
                        detailTable.removeAllItems();
                        onClickClearDetail();
                        createTableDetail();
                        launchSubwindow("Se ha creado correctamente");
                    } catch (InstantiationException ex) {
                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (DaoException ex) {
                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    launchSubwindow("Faltan campos por llenar");
                }
            }
        });
        
        delete.setImmediate(true);
        this.delete.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                if(entity==null){
                    message.setCaption("Debe seleccionar un " + clase.getSimpleName());
                } else {
                    onClickDelete();
                }
            }
        });
        
        deleteDetail.setImmediate(true);
        this.deleteDetail.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                if(entityDetail==null){
                    launchSubwindow("Debe seleccionar uno");
                } else {
                    try {
                        daoDetail.delete(entityDetail);
                        entity = dao.findById(entity.findId());
                        detailTable.removeAllItems();
                        onClickClearDetail();
                        createTableDetail();
                        launchSubwindow("Se ha eliminado correctamente");
                    } catch (DaoException ex) {
                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
        detailTable.setImmediate(true);
        detailTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setEntityDetail((D)detailTable.getValue());
                setDetailFields();
            }
        }); 
        
        edit.setImmediate(true);
        this.edit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                if(entity==null){
                    message.setCaption("Debe seleccionar un " + clase.getSimpleName());
                } else {
                    onClickEdit();
                    removeAllComponents();
                    action = 'U';
                    addComponent(form);
                    createDetail();       
                    
                }
            }
        });
        
        editDetail.setImmediate(true);
        this.editDetail.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(validateDetail()){
                    message.setCaption("");
                    if(entityDetail==null){
                        launchSubwindow("Debe seleccionar uno");
                    } else {
                        try {
                            processDetail();
                            daoDetail.update(entityDetail);
                            entity = dao.findById(entity.findId());
                            detailTable.removeAllItems();
                            onClickClearDetail();
                            createTableDetail();
                            launchSubwindow("Se ha actualizado correctamente");
                        } catch (DaoException ex) {
                            Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    launchSubwindow("Faltan campos por llenar");
                }               
            }
        });
        
        reset.setImmediate(true);
        this.reset.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                onClickReset();
            }
        });
        
        submit.setImmediate(true);
        this.submit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                onClickSubmit();                
            }
        });
        
        table.setImmediate(true);
        table.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setEntity((M)table.getValue());
            }
        });       
    }
    
    public List<D> getListDetail(){
        try {
            listDetail = daoDetail.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listDetail;
    }
    
    public void setEntityDetail(D entityDetail){
        this.entityDetail = entityDetail;        
    }
    
    @Override
    protected void initForm(){
        
        if(createDetail == null){
            createDetail = new Button("Crear");
        }
        if(detailForm == null){
            detailForm = new Form();
        }
        if(editDetail==null){
            editDetail = new Button("Editar");       
        }
        if(deleteDetail==null){
            deleteDetail = new Button("Borrar");
        }
        
        detailForm.getFooter().addComponent(createDetail);
        detailTable = new Table();
        detailTable.setSelectable(true);
        detailTable.setImmediate(true);
        
        
        detailForm = new Form();        
        detailForm.setImmediate(true);
        detailTable = new Table();
        detailTable.setSelectable(true);
        detailTable.setImmediate(true);        
        
        detailForm.getFooter().addComponent(deleteDetail);
        detailForm.getFooter().addComponent(editDetail);
        detailForm.getFooter().addComponent(createDetail);
        message.setCaption("");
        removeAllComponents();
        
        table.removeAllItems();
        detailTable.removeAllItems();
        entity = null;
        
        addComponent(table);
        addComponent(message);
       
        buttons.addComponent(delete);
        buttons.addComponent(edit);
        buttons.addComponent(create);
        
        addComponent(buttons);
             
        createTable();        
//        addListeners();
    } 

}
