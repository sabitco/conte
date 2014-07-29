/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.UsuarioDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Tecnico;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.ui.GenericContent;
import co.org.conte.sgm.util.HibernateUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author jam
 */
public class ConsultarEstadoSolicitud extends GenericContent{
    
    private Table table;
    private Label message;
    private String solicitudId;
    private Button generar;
    private Tecnico tecnico;
    private Link link;
    private HorizontalLayout buttons;
    
    public ConsultarEstadoSolicitud(Usuario usuario) {
        super(usuario);
        UsuarioDao dao = new UsuarioDao();
        try {
            this.setUsuario(dao.findById(usuario.getCodigo()));
        } catch (DaoException ex) {
            Logger.getLogger(ConsultarEstadoSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }
        table = new Table("Solicitudes");
        table.addContainerProperty("Consecutivo", Integer.class,  null);
        table.addContainerProperty("Fecha",  java.util.Date.class, null);
        table.addContainerProperty("Estado",   String.class, null);
        table.setSelectable(true);
        table.setImmediate(true);
        generar = new Button("generar");
        buttons = new HorizontalLayout();
        buttons.addComponent(generar);
        addComponent(table);
        addComponent(buttons);
        createTable();
        addListener();
    }
    
    private void createTable(){
        if(usuario.getSolicituds()!=null){
                if(usuario.getSolicituds().size()>0){
                    int aux = 0;
                    for(Solicitud solicitud : usuario.getSolicituds()){
                        if(solicitud.getEstado().getCodigo()!=17 || solicitud.getEstado().getCodigo()!=7){
                            aux++;
                            table.addItem(new Object[] {
                                solicitud.getRadicado(),
                                solicitud.getFechaCreacion(), 
                                solicitud.getEstado().getNombre()                       
                            }, new Integer(aux));                            
                        }
                    }
                } else {
                    super.removeAllComponents();
                    message.setCaption("No tiene solicitudes");
                    super.addComponent(message);

                }
        } else {
            message.setCaption("No tiene solicitudes");
            super.addComponent(message);
        }
    }
    
    private void addListener(){
        table.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setSolicitud(table.getValue());
            }
        });
        
        this.generar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(link!=null){
                    buttons.removeComponent(link);
                }
                link = generateReport(Long.valueOf(solicitudId));
                buttons.addComponent(link);
            }
        });
    }
    
    private void setSolicitud(Object solicitud){
       solicitudId = table.getItem(solicitud).getItemProperty("Consecutivo").toString().trim();
    }
    
    private Link generateReport(Long codSolicitud){
        SolicitudDao sdao = new SolicitudDao();
        for(Solicitud solicitud_aux : sdao.findBySolicitud(solicitudId)){
            tecnico = solicitud_aux.getTecnico();
        }
        final Connection con = HibernateUtil.getConnection();
        final HashMap map = new HashMap();
        map.put("id", tecnico.getCodigo());
        map.put("codSolicitud", codSolicitud);
        try {
            StreamResource.StreamSource source = new StreamResource.StreamSource() {     
                @Override
                public InputStream getStream() {
                    byte[] b = null;
                    try {
                        if(usuario.getTipoDocumento().equalsIgnoreCase("NIT")){
                            
                            System.out.print("*************"+getServletContext().getRealPath("resources/ConteSolicitudEmpresa.jasper"));
                            b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/ConteSolicitudEmpresa.jasper"), map, con);                            
                            
                        } else {
                            b = JasperRunManager.runReportToPdf(getServletContext().getRealPath("resources/ConteSolicitud.jasper"), map, con);                            
                        }
                        
                    } catch (Exception ex) {
                        Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return new ByteArrayInputStream(b);
                }
            };
            Double r = (Math.random()*1000);
            StreamResource resource = new StreamResource(source, 
                    tecnico.getDocumento() + "_" +  
                    new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date())+".pdf", getApplication());

            resource.getStream().setParameter("Content-Type", "application/x-unknown");
            resource.setCacheTime(0);
            Link l = new Link("Descargar", resource);
            l.setTargetName("_blank");
            l.setResource(resource);
            return l;

        } catch (Exception ex) {
            Logger.getLogger(CrearSolicitud.class.getName()).log(Level.SEVERE, null, ex);
        }       
        return null;
    }
}