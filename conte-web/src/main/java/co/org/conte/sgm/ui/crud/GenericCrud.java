package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.dao.BaseDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.BaseEntity;
import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author jam
 */
public abstract class GenericCrud<M extends BaseEntity> extends GenericContent {
    
    protected char action;
    protected HorizontalLayout buttons;
    protected Class<M> clase; 
    protected Button clear;
    protected Button create;
    protected BaseDao<M> dao;       
    protected Button delete;    
    protected Button edit;    
    protected M entity;    
    protected Form form;    
    private List<M> list;    
    protected Label message;
    protected Button reset;
    protected Button submit;    
    protected Table table;
    
    public GenericCrud(Class<M> clase){
        super(null);
        buttons = new HorizontalLayout();
        dao = new BaseDao<M>(clase);
        create = new Button("Crear");
        
        clear = new Button("Limpiar");
        create = new Button("Crear");
        dao = new BaseDao<M>(clase);
        delete = new Button("Borrar");        
        edit = new Button("Editar");
        form = new  Form();
        form.setImmediate(true);
        reset = new Button("Volver");
        submit = new Button("Guardar");
        table = new Table(clase.getSimpleName());
        this.clase = clase;        
        table.setSelectable(true);
        table.setImmediate(true);
        message = new Label("");
        form.getFooter().addComponent(message);
        form.getFooter().addComponent(reset);
        form.getFooter().addComponent(clear);
        form.getFooter().addComponent(submit);
          
        initForm();       
        addListeners();
    }
    
    public abstract void addValidators();    
    public abstract void createForm();
    public abstract void createTable();
    public abstract void onClickClear();
    public abstract void onClickCreate();
    public abstract void onClickDelete();
    public abstract void onClickEdit();
    public abstract void process();
    public abstract void setRequired(boolean required);
    public abstract boolean validate();
    
    public void createTableDetail(){}
    
    public String getValue(Field field) {
        if (field.getValue() == null || field.getValue().toString().trim().isEmpty()) {                
                return "";
        }
        return field.getValue().toString().trim();
    }
    
    public void setEntity(M entity){
        this.entity = entity;
    }
    
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
                        
        edit.setImmediate(true);
        this.edit.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                message.setCaption("");
                if(entity==null){
                    message.setCaption("Debe seleccionar un " + clase.getSimpleName());
                } else {
                    removeAllComponents();
                    action = 'U';
                    addComponent(form);
                    onClickEdit();
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
    
    public void onClickReset(){
        initForm();
    }
    
    public List<M> getList(){
        try {
            list = dao.findAll();
        } catch (DaoException ex) {
            Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    protected void initForm(){
        message.setCaption("");
        removeAllComponents();
        
        table.removeAllItems();
        entity = null;
        
        addComponent(table);
        addComponent(message);
       
        
             
        if(!(this instanceof ParametroCrud)){             
            buttons.addComponent(create);
            buttons.addComponent(edit);
            buttons.addComponent(delete);  
             
             
        } else {
             buttons.addComponent(edit);
        }
        addComponent(buttons);
        createTable();        
    }   
    
    protected void onClickSubmit(){
        removeComponent(message);
        form.getFooter().addComponent(message);
        if(validate()){
            process();
            try {
                switch(action){
                    case 'C':
                        dao.insert(entity);
                        ConfirmDialog.show(getWindow(), "", "Se ha creado correctamente",
                        "Aceptar", "Crear Nuevo", new ConfirmDialog.Listener() {
                            @Override
                            public void onClose(ConfirmDialog dialog) {
                                if (dialog.isConfirmed()) {
                                    onClickClear();
                                    onClickReset();
                                } else {
                                    onClickClear();
                                    try {
                                        entity = clase.newInstance();
                                        onClickClear();
                                    } catch (InstantiationException ex) {
                                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (IllegalAccessException ex) {
                                        Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        });
                        break;
                    case 'U':
                        dao.update(entity);
                        launchSubwindow("Se ha guardado correctamente");
                        break;
                }
                
            } catch (DaoException ex) {
                launchSubwindow("Ha ocurrido un error al intentar guardar");
                Logger.getLogger(GenericCrud.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            launchSubwindow(getMessage("error.form"));
        }
        
    }
}