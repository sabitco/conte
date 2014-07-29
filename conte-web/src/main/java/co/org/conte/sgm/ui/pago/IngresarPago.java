package co.org.conte.sgm.ui.pago;

import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.entity.Pago;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericForm;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jam
 */
public class IngresarPago extends GenericForm {

    private TextField documento;
    private TextField valor;
    private DateField fechaConsignacion;
    
    private SolicitudDao daoSolicitud;
    
    public IngresarPago(Usuario usuario) {
        super(usuario);
        init();
    }

    @Override
    protected void addListeners() {
     //   throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void addValidators() {
        valor.addValidator(new IntegerValidator("Solo puede ingresar números"));
    }

    @Override
    protected void createForm() {
        form.addField("documento", documento);
        form.addField("valor",  valor);
        form.addField("fecha", fechaConsignacion);
    }

    @Override
    protected void onClickReset() {
        documento.setValue("");
        valor.setValue(0);
        fechaConsignacion.setValue(new Date());
    }

    @Override
    protected void process() {
        try {
            validate();
            List<BigInteger> solicitudes = daoSolicitud.findByDocument(getValue(documento));
            if(solicitudes==null || solicitudes.isEmpty()){
                launchSubwindow("No se encontro ninguna solicitud con el número de documento " + getValue(documento));
            } else {
                Pago pago = new Pago();
                pago.setDocumento(getValue(documento));
                pago.setFechaConsignacion((Date)fechaConsignacion.getValue());
                pago.setValor(new Double(getValue(valor)));
                launchSubwindow("Se ingreso correctamente el pago");
                onClickReset();
            }
        } catch (InvalidValueException ive) {
            launchSubwindow(getMessage("error.form"));
        }
    }

    @Override
    protected void setRequired(boolean required) {
        documento.setRequired(required);
        valor.setRequired(required);
        fechaConsignacion.setRequired(required);
    }

    @Override
    protected void setMaxLength() {
        
    }

    @Override
    protected void validate() throws InvalidValueException {
        documento.validate();
        valor.validate();
        fechaConsignacion.validate();
    }   

    private void init() {
        documento = new TextField("Documento");
        valor = new TextField("Valor");
        fechaConsignacion = new DateField("Fecha Consignacion");
        
        daoSolicitud = new SolicitudDao();
        createForm();
        addValidators();
        setRequired(true);
    }
}