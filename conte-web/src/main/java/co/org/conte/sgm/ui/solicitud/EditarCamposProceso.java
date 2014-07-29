package co.org.conte.sgm.ui.solicitud;

import co.org.conte.sgm.container.ProcesoContainer;
import co.org.conte.sgm.dao.ProcesoDao;
import co.org.conte.sgm.dao.RelacionesBDACTDao;
import co.org.conte.sgm.dao.SolicitudDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.RelacionesBDACT;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.Usuario;
import co.org.conte.sgm.entity.activiti.Proceso;

import co.org.conte.sgm.ui.GenericContent;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author J4M0
 */
public class EditarCamposProceso extends GenericContent {

    private Table table;
    private ProcesoDao procesoDao;
    private TextField filter;
    ProcesoContainer pc = new ProcesoContainer();
    private Button editar;
    private VerticalLayout camposA;
    private Button guardar;
    private String radicadoId;
    private String codigoProceso;
    private String documento;
    private SolicitudDao solicitudes = new SolicitudDao();
    List<RelacionesBDACT> relaciones_campo = new ArrayList<RelacionesBDACT>();
    Map<Integer, Integer> campos = new HashMap<Integer, Integer>();

    public EditarCamposProceso(Usuario usuario) {
        super(usuario);
        procesoDao = new ProcesoDao();
        filter = new TextField();
        filter.setWidth("400px");
        filter.setInputPrompt("Ingrese radicado del usuario a buscar");
        editar = new Button("editar");
        guardar = new Button("guardar");
        camposA = new VerticalLayout();
        addComponent(filter);
        addListeners();
    }

    private void createTable(String u) {
        try {
            List<Proceso> procesos = procesoDao.findAll();
            for (Proceso proceso : procesos) {
                Label url = new Label("", Label.CONTENT_XHTML);
                Label urlRepo = new Label("", Label.CONTENT_XHTML);
                String[] strings = proceso.getTitulo().split(" ");
                Integer radicado = 0;
                try {
                    radicado = Integer.parseInt(strings[0]);
                    strings[0] = "";
                } catch (NumberFormatException nfe) {
                }
                String repo = proceso.getTitulo().replace("Matricula Profesional", "");
                repo = repo.replace("Solicitud Tarjeta", "");
                repo = repo.replace("Duplicado Matricula", "");
                repo = repo.replace("Duplicado Tarjeta", "");
                repo = repo.replace("Ampliacion", "");
                repo = repo.replace("Licencia Especial", "");
                repo = repo.replace("  ", " ");
                urlRepo.setValue("<a href='" + u + "share/page/repository#filter=path|%2FCONTE%2F" + repo + "' target='_blank' >" + proceso.getTitulo() + "</a>");
                url.setValue("<a href='" + u + "share/page/workflow-details?workflowId=activiti$" + proceso.getCodigo() + "' target='_blank' >" + radicado + "</a>");
                pc.addProceso(proceso.getCodigo(), radicado, proceso.getDocumento(), urlRepo, proceso.getTarea(), url);
            }
            table = new Table("", pc);
            table.setImmediate(true);
            table.setSelectable(true);
            table.addListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    setSolicitud(table.getValue());
                }
            });
        } catch (DaoException ex) {
            table = new Table();
            Logger.getLogger(ConsultarUrlProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addListeners() {
        filter.addListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(final FieldEvents.TextChangeEvent event) {
                pc.removeAllContainerFilters();
                pc.addContainerFilter(new Container.Filter() {

                    @Override
                    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
                        if (event.getText() == null
                                || event.getText().equals("")) {
                            return true;
                        }

                        return filterByProperty("Radicado", item,
                                event.getText())
                                || filterByProperty("Documento", item,
                                        event.getText()
                                ) || filterByProperty("Tarea Actual", item,
                                        event.getText());
                    }

                    @Override
                    public boolean appliesToProperty(Object propertyId) {
                        if (propertyId.equals("Radicado")
                                || propertyId.equals("Documento")
                                || propertyId.equals("Tarea Actual")) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        this.editar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                agregarCamposActualizacion();
            }
        });
        this.guardar.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                guardarCambios();
            }
        });
    }

    private boolean filterByProperty(String prop, Item item, String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.startsWith(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }

    private Integer getDocumento(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }

    public EditarCamposProceso(Table table, ProcesoDao procesoDao, TextField filter, Usuario usuario) {
        super(usuario);
        this.table = table;
        this.procesoDao = procesoDao;
        this.filter = filter;
    }

    @Override
    public void attach() {
        String url = getURL();
        createTable(url);
        addComponent(table);
        addComponent(editar);
    }

    private void agregarCamposActualizacion() {
        if (components.contains(camposA)) {
            removeComponent(camposA);
        }
        camposA.removeAllComponents();
        SolicitudDao soldao = new SolicitudDao();
        try {
            RelacionesBDACTDao rel = new RelacionesBDACTDao();
            List<RelacionesBDACT> relaciones = rel.findAll();
            relaciones_campo.clear();
            campos.clear();
            for (RelacionesBDACT relacion : relaciones) {
                Object valor = rel.obtenerValorCampo(relacion, radicadoId, codigoProceso);
                relacion.setValor(valor);
                if (relacion.getTipo().trim().equalsIgnoreCase("String")) {
                    TextField campo = new TextField(relacion.getLabel());
                    campo.setValue((valor == null) ? "" : valor.toString());
                    camposA.addComponent(campo);
                    campos.put(relacion.getCodigo(), camposA.getComponentIndex(campo));
                } else if (relacion.getTipo().trim().equalsIgnoreCase("Date")) {
                    DateField campo = new DateField(relacion.getLabel());
                    campo.setResolution(DateField.RESOLUTION_DAY);
                    campo.setDateFormat("dd/MM/yyyy");
                    campo.setValue(valor);
                    camposA.addComponent(campo);
                    campos.put(relacion.getCodigo(), camposA.getComponentIndex(campo));
                }
                relaciones_campo.add(relacion);
            }
            camposA.addComponent(guardar);
            addComponent(camposA);
        } catch (DaoException ex) {
            Logger.getLogger(EditarCamposProceso.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarCambios() {
        RelacionesBDACTDao reldao = new RelacionesBDACTDao();
        RelacionesBDACT apellidos = new RelacionesBDACT();
        apellidos.setTablaAct("ACT_HI_DETAIL,ACT_RU_VARIABLE");
        apellidos.setCampoAct("qswfr_apellidos,qswfgd_apellidos");
        apellidos.setTipoRelacion("3");
        apellidos.setTipo("String");
        int resultado = 0;
        int errores = 0;
        boolean actualizarApellidos = false;
        for (RelacionesBDACT rel : relaciones_campo) {
            String valor = "";
            int idxCampo = campos.get(rel.getCodigo());
            if (rel.getTipo().trim().equalsIgnoreCase("String")) {
                TextField campo = (TextField) camposA.getComponent(idxCampo);
                valor = campo.getValue().toString();
                if (rel.getLabel().equalsIgnoreCase("primer apellido")) {
                    apellidos.setValor(rel.getValor());
                    apellidos.setNuevoValor(valor);
                } else if (rel.getLabel().equalsIgnoreCase("segundo apellido")) {
                    apellidos.setValor(apellidos.getValor() + " " + rel.getValor());
                    apellidos.setNuevoValor(apellidos.getNuevoValor() + " " + valor);
                    actualizarApellidos = true;
                }
            } else if (rel.getTipo().trim().equalsIgnoreCase("Date")) {
                DateField campo = (DateField) camposA.getComponent(idxCampo);
                try {
                    Date fecha = (Date) campo.getValue();
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    valor = formato.format(fecha);
                } catch (Exception e) {
                    valor = "";
                }
            }
            System.out.println("El nuevo valor es: " + valor);
            rel.setNuevoValor(valor);
            if (actualizarApellidos) {
                apellidos.setNuevoValor(String.valueOf(apellidos.getNuevoValor()).trim());
                resultado = reldao.actualizarCampos(apellidos, radicadoId, codigoProceso);
                if (resultado < 0) {
                    errores++;
                }
            }
            resultado = reldao.actualizarCampos(rel, radicadoId, codigoProceso);
            if (resultado < 0) {
                errores++;
            }
            if ((rel.getLabel().equalsIgnoreCase("documento")
                    || rel.getLabel().equalsIgnoreCase("nombres")
                    || rel.getLabel().equalsIgnoreCase("primer apellido")
                    || rel.getLabel().equalsIgnoreCase("segundo apellido")
                    || rel.getLabel().equalsIgnoreCase("email"))) {
                resultado = reldao.actualizaTabla("usuario", rel.getCampoDb(), valor, 1, "documento", documento, 1);
                if (rel.getLabel().equalsIgnoreCase("documento")) {
                    if (resultado > 0) {
                        documento = valor;
                    }
                }
                if (resultado < 0) {
                    errores++;
                }
            }
        }
        if (errores == 0) {
            launchSubwindow("Se han realizado los cambios correctamente.");
        }
        if (errores > 0 && errores != relaciones_campo.size()) {
            launchSubwindow("No se realizo la totalidad de los cambios.");
        }
        if (errores == relaciones_campo.size()) {
            launchSubwindow("No se realizo ningun cambio.");
        }
    }

    private void setSolicitud(Object registro) {
        this.documento = table.getItem(registro).getItemProperty("Documento").toString().trim();
        this.codigoProceso = table.getItem(registro).getItemProperty("Process Id").toString().trim();
        for (BigInteger s : this.solicitudes.findByDocumento(documento)) {
            this.radicadoId = String.valueOf(s);
        }
        if (components.contains(camposA)) {
            camposA.removeAllComponents();
            removeComponent(camposA);
        }
    }
}
