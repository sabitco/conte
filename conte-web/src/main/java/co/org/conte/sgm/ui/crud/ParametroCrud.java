package co.org.conte.sgm.ui.crud;

import co.org.conte.sgm.entity.Parametro;
import co.org.conte.sgm.entity.Usuario;
import com.vaadin.ui.TextField;
import java.util.List;

/**
 *
 * @author J4M0
 */
public class ParametroCrud extends GenericCrud<Parametro> {

    private TextField descripcion;
    private TextField parametro;
    private TextField valor;
        
    public ParametroCrud(Usuario usuario) {
        super(Parametro.class);
        init();
    }

    @Override
    public void createForm() {
        form.addField("parametro", parametro);
        form.addField("valor", valor);        
        form.addField("descripcion", descripcion);
    }

    @Override
    public void createTable() {
        table.addContainerProperty("Codigo", Integer.class,  null);     
        table.addContainerProperty("Parametro", String.class,  null);     
        table.addContainerProperty("Valor", String.class,  null);   
        table.addContainerProperty("Descripción", String.class,  null);   
     
        List<Parametro> parametros = getList();
        if(parametros != null && !parametros.isEmpty()){
            for(Parametro p : parametros){
                table.addItem(new Object[]{
                    p.getCodigo(),
                    p.getParametro(),
                    p.getValor(),
                    p.getDescripcion(),
                }, p);
            }            
        }      
    }

    @Override
    public void onClickClear() {
        descripcion.setValue("");
        valor.setValue("");
    }

    @Override
    public void onClickCreate() {
        /**
         * No se pueden crear mas parametros
         */
    }

    @Override
    public void onClickDelete() {
        /**
         * No se pueden eliminar parametros
         */
    }

    @Override
    public void onClickEdit() {
        descripcion.setValue(entity.getDescripcion());
        parametro.setValue(entity.getParametro());
        valor.setValue(entity.getValor());
        parametro.setReadOnly(true);
    }

    @Override
    public void process() {
        entity.setDescripcion(getValue(descripcion));
        entity.setValor(getValue(valor));
    }

    @Override
    public void setRequired(boolean required) {
        valor.setRequired(required);        
        descripcion.setRequired(required);
    }

    @Override
    public boolean validate() {
        return (descripcion.isValid() &&
                valor.isValid()
                );
    }   
    
    private void init(){
        descripcion = new TextField("Descripcion");
        parametro = new TextField("Parametro");
        valor = new TextField("Valor");
        createForm();
        removeComponent(create);
        removeComponent(delete);
        setRequired(true);
    }

    @Override
    public void addValidators() {    }

}