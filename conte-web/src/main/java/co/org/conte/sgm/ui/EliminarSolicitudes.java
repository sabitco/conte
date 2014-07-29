//package co.org.conte.sgm.ui;
//
//import co.org.conte.sgm.entity.Solicitud;
//import co.org.conte.sgm.service.SolicitudService;
//import com.vaadin.data.Property;
//import com.vaadin.data.Validator.InvalidValueException;
//import com.vaadin.data.validator.NullValidator;
//import com.vaadin.ui.DateField;
//import java.util.Date;
//import java.util.List;
//
///**
// *
// * @author Andrés Mise Olivera
// */
//public class EliminarSolicitudes extends GenericForm {
//    
//    private DateField date;
//    private SolicitudService service;
//
//    public EliminarSolicitudes() {
//        date = new DateField("Solicitud sin actividad desde");
//        date.setDescription("Fecha de Inactividad");
//        date.setResolution(DateField.RESOLUTION_DAY);
//        service = new SolicitudService();
//        init();
//    }
//    
//    private void init(){
//        date.setDateFormat("dd/MM/yyyy");         
//        createForm();
//    }
//            
//            
//    
//    @Override
//    protected void addListeners() {
//        date.setImmediate(true);
//        date.addListener(new  Property.ValueChangeListener() {
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//                message.setCaption("");
//                if(date.getValue() != null){
//                    Date d = (Date)(date.getValue());
//                    System.out.print(d);
//                    if(d.getTime() > new Date().getTime()){
//                        message.setCaption("La fecha seleccionada no puede ser "
//                                + "mayor a la actual");
//                        submit.setEnabled(false);
//                    } else {
//                        submit.setEnabled(true);
//                    }
//                }
//             }
//        });
//    }
//
//    @Override
//    protected void addValidators() {        
//        date.addValidator(new NullValidator("Debe seleccionar una fecha", true));
//    }
//
//    @Override
//    protected void createForm() {
//        form.addField("date", date);
//        addListeners();
//        setRequired(true);
//    }
//
//    @Override
//    protected void onClickReset() {
//        message.setCaption("");
//        date.setValue(null);
//    }
//
//    @Override
//    protected void process() {
//        try{
//            validate();
//            List<Solicitud> inactivos = service.eliminarSolicitudes((Date)
//                    ((Date)date.getValue()).clone());
//            if(inactivos != null){
//                message.setCaption("Se han eliminado " +
//                        inactivos.size() + (inactivos.size()>1 ? 
//                        " solicitudes" : " solicitud"));
//            } else {
//                message.setCaption("Se han eliminado 0");
//            }
//        } catch (InvalidValueException ex){
//            message.setCaption("Existen campos invalidos");
//        }
//    }
//
//    @Override
//    protected void setRequired(boolean required) {
//        date.setRequired(required);
//    }
//    
//    @Override
//    protected void setMaxLength(){
//        
//    }
//
//    @Override
//    protected void validate() throws InvalidValueException {
//        date.validate();
//    }
//}
