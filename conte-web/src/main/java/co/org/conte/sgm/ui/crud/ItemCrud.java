package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.entity.Item;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.TextField;
import java.util.List;

/**
 *
 * @author jam
 */
public class ItemCrud extends GenericCrud<Item> {
    
    private TextField name;

    public ItemCrud(Usuario usuario) {
        super(Item.class);
        name = new TextField("Nombre");
        setRequired(true);
        createForm();
    }
    
    @Override
    public void addValidators() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createForm() {
        form.addField("name", name);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);     
        table.addContainerProperty("Nombre", String.class,  null);   
        List<Item> its = getList();
        for(Item i : its){
            table.addItem(new Object[]{                    
                    i.getId(),
                    i.getName(),
                }, i);
            
        }
    }

    @Override
    public void onClickClear() {
        name.setValue("");
    }

    @Override
    public void onClickCreate() {
       
    }

    @Override
    public void onClickDelete() {
        
    }

    @Override
    public void onClickEdit() {
        name.setValue(entity.getName());
    }

    @Override
    public void process() {
        if(validate()){
            entity.setName(getValue(name));            
        } else {
            launchSubwindow(getMessage("error.form"));
        }
    }

    @Override
    public void setRequired(boolean required) {
        name.setRequired(required);
    }

    @Override
    public boolean validate() {
        return name.isValid();
    }   
}