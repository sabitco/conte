//package com.conte.ui;
//
////import com.conte.servicios.matricula.GestionTipoSolicitud;
//import com.vaadin.Application;
//import com.vaadin.data.Property.ValueChangeEvent;
//import com.vaadin.data.Property.ValueChangeListener;
//import com.vaadin.data.validator.IntegerValidator;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.Field;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.PasswordField;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.Window;
//import com.vaadin.ui.Window.Notification;
//import javax.ejb.EJB;
//import javax.ejb.Stateful;
//
///**
// *
// * @author Andrés Mise Olivera
// */
//@Stateful
//public class Main extends Application /*extends FormLayout */{
//    
//    private static final long serialVersionUID = 1L;
//    
////    @EJB
////    private GestionTipoSolicitud gtd;
//    
//    private final TextField documento = new TextField("Documento");
////    private  ComboBox tipoDocumento = new ComboBox("Tipo de Documento", gtd.obtenerTipoSolicitud());//Arrays.asList(new String[] {"CC","TI","PA" }));
//    private final TextField nombres = new TextField("Nombres");
//    private final TextField apellidos = new TextField("Apellidos");
//    private final TextField correo = new TextField("Apellidos");
//    private final PasswordField passwordField = new PasswordField("Password");
//    private final PasswordField confirmPasswordField = new PasswordField("Confirm Password");
//    private Button validate;
//    private Button submit ;
//    private Button resetButton ;
//    Window mainWindow = new Window("SGM .:. Conte");
//       
//    public Main() {            
//        this.initializeData();
//        VerticalLayout vl = new VerticalLayout();
////        vl.addComponent(this.tipoDocumento);
//        vl.addComponent(this.documento);
//        vl.addComponent(this.nombres);
//        vl.addComponent(this.apellidos);
//        vl.addComponent(this.correo);
//        vl.addComponent(this.passwordField);
//        vl.addComponent(this.confirmPasswordField);
//        HorizontalLayout hl = new HorizontalLayout();
//        hl.addComponent(this.submit);
//        hl.addComponent(this.validate);
//        hl.addComponent(this.resetButton);
//        vl.addComponent(hl);
//        mainWindow.addComponent(vl);
//    }
//    
//    @Override
//    public void init(){
//        setMainWindow(mainWindow);
//    }
//
//    private void initializeData() {
//        this.addListners(); 
//        this.setVisibleFields(false);
//        this.setRequiredFields();
//    }
//    
//    private void addListners() {
//        
//        submit  = new Button("Guardar", new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                
//            }
//        });
//            
//        resetButton  = new Button("Limpiar", new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//
//            }
//        });
//
//        validate  = new Button("Continuar", new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent event) {
//                //VALIDAR QUE NO EXISTA EN LA BD
//                if(getValue(documento)!=null){
//                    setVisibleFields(true);
//                }
//            }
//        });
//
////        tipoDocumento.addListener(new ValueChangeListener() {
////
////            @Override
////            public void valueChange(ValueChangeEvent event) {
////                String selected;
////                selected =  event.getProperty().toString();
////                
////                documento.setVisible(true);
////                if(true){
////                    documento.addValidator(new IntegerValidator("Solo puede ingresar numeros"));
////                }
////             }
////            });            
//    }
//    
//    private void setRequiredFields() {
//        
//        this.documento.setRequired(true);
////        this.tipoDocumento.setRequired(true);
//        this.nombres.setRequired(true);
//        this.apellidos.setRequired(true);
//        this.correo.setRequired(true);
//        this.passwordField.setRequired(true);
//        this.confirmPasswordField.setRequired(true);
//
//                       
//    }
//
//    private void setVisibleFields(boolean visible){
//        this.documento.setVisible(visible);  
//        
//        this.nombres.setVisible(visible);
//        this.apellidos.setVisible(visible);
//        this.correo.setVisible(visible);
//        this.passwordField.setVisible(visible);
//        this.confirmPasswordField.setVisible(visible);
//        
//        this.submit.setVisible(visible);
//    }
//
//    private String getValue(Field field) {
//        if (field.getValue() == null || field.getValue().toString().trim().isEmpty()) {
//                mainWindow.getWindow().showNotification(field.getCaption() + " es un campo requerido", Notification.TYPE_ERROR_MESSAGE);
//                return null;
//        }
//        return field.getValue().toString();
//    }
//
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
//    
////    
////    
////    private static final long serialVersionUID = 1L;
////    Window mainWindow = new Window("SGM .:. Conte");
////    private TabSheet t;		
////    Long ld = 1L;
////    Usuario usuario;
////    Person person;
////	
////    @Override
////    public void init() {
////        
////        VerticalLayout vl = new VerticalLayout();
////        vl.setMargin(true);
////        
////        usuario = new Usuario();
////        usuario.setNombres("");
////        
////        BeanItem<Usuario> usuarioItem = new BeanItem<Usuario>(usuario);
////        
////        final Form usuarioForm = new Form();
////        usuarioForm.setCaption("Crear Cuenta");
////        usuarioForm.setWriteThrough(false); // we want explicit 'apply'
////        usuarioForm.setInvalidCommitted(false); // no invalid values in datamodel
////        
////        usuarioForm.setFormFieldFactory(new UsuarioFieldFactory());
////        usuarioForm.setItemDataSource(usuarioItem); // bind to POJO via BeanItem
////        
////        usuarioForm.setVisibleItemProperties(Arrays.asList(new String[] {
////                "tipoDocumento","documento" }));
////        
////        vl.addComponent(usuarioForm);
////        
////        
////        
////        HorizontalLayout buttons = new HorizontalLayout();
////        buttons.setSpacing(true);
////        Button discardChanges = new Button("Discard changes",
////                new Button.ClickListener() {
////                    public void buttonClick(ClickEvent event) {
////                        usuarioForm.discard();
////                    }
////                });
////        discardChanges.setStyleName(BaseTheme.BUTTON_LINK);
////        buttons.addComponent(discardChanges);
////        buttons.setComponentAlignment(discardChanges, Alignment.MIDDLE_LEFT);
////
////        Button continuar = new Button("Continuar", new Button.ClickListener() {
////            @Override
////            public void buttonClick(ClickEvent event) {
////                try {
////                    usuario.setDocumento(usuarioForm.getField("documento").toString().trim());
////                    usuarioForm.getField("documento").setReadOnly(true);
////                    
////                    //HACER BUSQUEDAS
////                    usuarioForm.setVisibleItemProperties(Arrays.asList(new String[] {
////                        "tipoDocumento", "documento",
////                "usuario","contrasena","nombres","apellidos","email" }));
////                    
////                } catch (Exception e) {
////                    
////                }
////            }
////        });
////        
////        buttons.addComponent(continuar);
////        usuarioForm.getFooter().addComponent(buttons);
////        
////        usuarioForm.getFooter().addComponent(buttons);
////        usuarioForm.getFooter().setMargin(false, false, true, true);      
////        
////        t = new TabSheet();
////        t.setHeight("100%");
////        t.setWidth("100%");
////
////        t.addTab(vl, "Crear Cuenta");
////        
////
////        mainWindow.addComponent(t);
////	setMainWindow(mainWindow);
////    }
////}
////
////class UsuarioFieldFactory extends DefaultFieldFactory {
////    
////    private Field f;
////      
////    public UsuarioFieldFactory() {
////        
////    }
////
////    @Override
////    public Field createField(Item item, Object propertyId,
////            Component uiContext) {
////        
////        final Form form = (Form) uiContext; 
////        f = super.createField(item, propertyId, uiContext);
////        
////        if ("tipoDocumento".equals(propertyId)) {            
////            final ComboBox documentos = new ComboBox("Tipo de Documento", Arrays.asList(new String[] {
////                "CC","TI","PA" }));
////            
////            documentos.setWidth("25%");
////            documentos.setRequired(true);
////            documentos.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
////            
////            documentos.addListener(new ValueChangeListener() {
////                private static final long serialVersionUID = -4085124327898850666L;
////                      
////                 @Override
////                 public void valueChange(ValueChangeEvent event) {
////                     String selected;
////                     selected =  event.getProperty().toString();
////                     TextField t= (TextField) form.getField("documento");
////                     t.setReadOnly(false);
////                     t.setVisible(true);
////                     t.setRequired(true);
////                     t.setRequiredError("Por favor ingrese el número de documento");
////                     if(!selected.equals("PA")){
////                         t.addValidator(new IntegerValidator("Solo puede ingresar numeros"));
////                     }
////                 }
////                });
////
////            return documentos;
////            }
////        
////         if ("documento".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setReadOnly(true);
////        }
////         
////        if ("usuario".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setRequired(true);
////            tf.setRequiredError("Por favor ingrese el Nombre de Usuario");
////        } 
////        
////        if ("contrasena".equals(propertyId)) {
////            f = createPasswordField(propertyId);
////            PasswordField pf = (PasswordField) f;
////            pf.setRequired(true);
////            pf.setRequiredError("Please enter a password");
////            pf.setWidth("10em");
////            pf.addValidator(new StringLengthValidator(
////                    "Password must be 6-20 characters", 6, 20, false));
////            
////            f = createPasswordField("a");
////            
////            PasswordField pf2 = (PasswordField) f;
////            pf2.setRequired(true);
////            pf2.setRequiredError("Please enter a password");
////            pf2.setWidth("10em");
////            pf2.addValidator(new StringLengthValidator(
////                    "Password must be 6-20 characters", 6, 20, false));
////        }
////        
////        if ("nombres".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setRequired(true);
////            tf.setRequiredError("Por favor ingrese los Nombres");
////        }
////        
////        if ("apellidos".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setRequired(true);
////            tf.setRequiredError("Por favor ingrese los Apellidos");
////        }
////        
//////        if ("direccion".equals(propertyId)) {
//////            TextField tf = (TextField) f;
//////            tf.setRequired(true);
//////            tf.setRequiredError("Por favor ingrese la direccion");
//////        }
//////        
//////        if ("telefono".equals(propertyId)) {
//////            TextField tf = (TextField) f;
//////            tf.setRequired(true);
//////            tf.setRequiredError("Por favor ingrese la direccion");
//////        }
//////        
//////        if ("celular".equals(propertyId)) {
//////            TextField tf = (TextField) f;
//////            tf.setRequired(true);
//////            tf.setRequiredError("Por favor ingrese la direccion");
//////        }
////        
////        if ("email".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setRequired(true);
////            tf.setRequiredError("Por favor ingrese la direccion");
////        }
////         return f;
////    }   
////    
////    private PasswordField createPasswordField(Object propertyId) {
////        PasswordField pf = new PasswordField();
////        pf.setCaption(DefaultFieldFactory
////                .createCaptionByPropertyId(propertyId));
////        return pf;
////    }
////}
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////class PersonFieldFactory extends DefaultFieldFactory {
////
////    final ComboBox countries = new ComboBox("Country");
////
////    public PersonFieldFactory() {
//////        countries.setWidth(20);
//////        countries.setContainerDataSource(ExampleUtil.getISO3166Container());
//////        countries
//////                .setItemCaptionPropertyId(ExampleUtil.iso3166_PROPERTY_NAME);
//////        countries.setItemIconPropertyId(ExampleUtil.iso3166_PROPERTY_FLAG);
//////        countries.setFilteringMode(ComboBox.FILTERINGMODE_STARTSWITH);
////    }
////
////    @Override
////    public Field createField(Item item, Object propertyId,
////            Component uiContext) {
////        Field f;
////        if ("countryCode".equals(propertyId)) {
////            // filtering ComboBox w/ country names
////            return countries;
////        } else if ("password".equals(propertyId)) {
////            // Create a password field so the password is not shown
////            f = createPasswordField(propertyId);
////        } else {
////            // Use the super class to create a suitable field base on the
////            // property type.
////            f = super.createField(item, propertyId, uiContext);
////        }
////
////        if ("nombres".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setValue("");
////            tf.setRequired(true);
////            tf.setRequiredError("Please enter a First Name");
////            tf.setWidth(200);
////            tf.addValidator(new StringLengthValidator(
////                    "First Name must be 3-25 characters", 3, 25, false));
////        } else if ("lastName".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setRequired(true);
////            tf.setRequiredError("Please enter a Last Name");
////            tf.setWidth(20);
////            tf.addValidator(new StringLengthValidator(
////                    "Last Name must be 3-50 characters", 3, 50, false));
////        } else if ("password".equals(propertyId)) {
////            PasswordField pf = (PasswordField) f;
////            pf.setRequired(true);
////            pf.setRequiredError("Please enter a password");
////            pf.setWidth("10em");
////            pf.addValidator(new StringLengthValidator(
////                    "Password must be 6-20 characters", 6, 20, false));
////        } else if ("shoesize".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setNullRepresentation("");
////            tf.setNullSettingAllowed(true);
////            tf.addValidator(new IntegerValidator(
////                    "Shoe size must be an Integer"));
////            tf.setWidth("2em");
////        } else if ("uuid".equals(propertyId)) {
////            TextField tf = (TextField) f;
////            tf.setWidth("20em");
////        }
////
////        return f;
////    }
////
////    private PasswordField createPasswordField(Object propertyId) {
////        PasswordField pf = new PasswordField();
////        pf.setCaption(DefaultFieldFactory
////                .createCaptionByPropertyId(propertyId));
////        return pf;
////    }
////}
////
////class Person implements Serializable {
////
////    private String nombre = "";
////    private String lastName = "";
////    private Date birthdate;
////    private Integer shoesize = 42;
////    private String password = "";
////    private UUID uuid;
////    private String countryCode = "";
////
////    public Person() {
////        uuid = UUID.fromString("3856c3da-ea56-4717-9f58-85f6c5f560a5");
////    }
////
////
////    public String getLastName() {
////        return lastName;
////    }
////
////    public void setLastName(String lastName) {
////        this.lastName = lastName;
////    }
////
////    public Date getBirthdate() {
////        return birthdate;
////    }
////
////    public void setBirthdate(Date birthdate) {
////        this.birthdate = birthdate;
////    }
////
////    public Integer getShoesize() {
////        return shoesize;
////    }
////
////    public void setShoesize(Integer shoesize) {
////        this.shoesize = shoesize;
////    }
////
////    public String getPassword() {
////        return password;
////    }
////
////    public void setPassword(String password) {
////        this.password = password;
////    }
////
////    public UUID getUuid() {
////        return uuid;
////    }
////
////    public String getCountryCode() {
////        return countryCode;
////    }
////
////    public void setCountryCode(String countryCode) {
////        this.countryCode = countryCode;
////    }
////
////
////	public String getNombre() {
////		return nombre;
////	}
////
////
////	public void setNombre(String nombre) {
////		this.nombre = nombre;
////	}
//}

package com.conte.ui;

import java.util.Date;
import java.util.Iterator;

import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Runo;

public class Main extends Application {

    @SuppressWarnings("deprecation")
    private static final Date DATEFIELD_VALUE = new Date(2010 - 1900, 7 - 1,
            19, 14, 46, 00);
    private TabSheet right = new TabSheet();
    private TabSheet styles = new TabSheet();
    private TabSheet samples = new TabSheet();

    @Override
    public void init() {
        setTheme(Runo.themeName());

        VerticalLayout root = new VerticalLayout();
        root.setMargin(true);
        root.setSizeFull();
        Window main = new Window("Vaadin Runo Theme", root);
        setMainWindow(main);

        Label title = new Label("Vaadin Runo Theme");
        title.addStyleName(Runo.LABEL_H1);
        title.setSizeUndefined();
        root.addComponent(title);
        root.setComponentAlignment(title, Alignment.TOP_CENTER);

        Label slogan = new Label(
                "Presenting a Stylish Alternative for the Traditional Desktop-Look");
        slogan.addStyleName(Runo.LABEL_SMALL);
        slogan.setSizeUndefined();
        root.addComponent(slogan);
        root.setComponentAlignment(slogan, Alignment.TOP_CENTER);

        Label spacer = new Label("");
        spacer.setHeight("20px");
        root.addComponent(spacer);

        final HorizontalSplitPanel split = new HorizontalSplitPanel();
        split.setStyleName(Runo.SPLITPANEL_REDUCED);
        split.setSplitPosition(1, Sizeable.UNITS_PIXELS);
        split.setLocked(true);
        root.addComponent(split);
        root.setExpandRatio(split, 1);

        Panel left = new Panel("Example Sidebar");
        left.setSizeFull();
        split.setFirstComponent(left);

        Accordion accordion = new Accordion();
        accordion.setSizeFull();
        left.setContent(accordion);

        accordion.addTab(buildTree(), "Pages");
        accordion.addTab(new Label(""), "Preferences");
        accordion.addTab(new Label(""), "Quick Search");

        right.setSizeFull();
        // right.addStyleName(Runo.TABSHEET_SMALL);
        split.setSecondComponent(right);
        split.setLocked(true);

        right.addTab(buildWelcomeScreen(), "Welcome");
        right.addTab(buildSamples(), "Sample Layouts");
        right.addTab(buildStyleReference(), "Style Reference");

        right.addListener(new TabSheet.SelectedTabChangeListener() {
            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (right.getSelectedTab() == samples) {
                    split.setSplitPosition(25, Sizeable.UNITS_PERCENTAGE);
                    split.setLocked(false);
                } else {
                    split.setSplitPosition(1, Sizeable.UNITS_PIXELS);
                    split.setLocked(true);
                }
            }
        });
    }

    Layout buildTree() {
        CssLayout margin = new CssLayout();
        margin.setWidth("100%");
        margin.setMargin(true, false, true, true);

        Tree t = new Tree();

        t.addItem("Archive");
        t.select("Archive");
        t.setItemIcon("Archive", new ThemeResource("icons/16/calendar.png"));
        createTreeItem(t, "January", "Archive");
        createTreeItem(t, "February", "Archive");
        createTreeItem(t, "March", "Archive");
        createTreeItem(t, "April", "Archive");
        createTreeItem(t, "May", "Archive");
        createTreeItem(t, "June", "Archive");
        createTreeItem(t, "July", "Archive");
        createTreeItem(t, "August", "Archive");
        createTreeItem(t, "September", "Archive");
        createTreeItem(t, "October", "Archive");
        createTreeItem(t, "November", "Archive");
        createTreeItem(t, "December", "Archive");

        t.addItem("Personal Files");
        t.setItemIcon("Personal Files", new ThemeResource(
                "icons/16/document.png"));
        createTreeItem(t, "Photos", "Personal Files");
        createTreeItem(t, "Videos", "Personal Files");
        createTreeItem(t, "Audio", "Personal Files");
        createTreeItem(t, "Documents", "Personal Files");
        t.expandItem("Personal Files");

        t.addItem("Company Storage");
        t.setItemIcon("Company Storage", new ThemeResource(
                "icons/16/folder.png"));
        createTreeItem(t, "Books", "Company Storage");
        createTreeItem(t, "Development", "Company Storage");
        createTreeItem(t, "Staff Peripherals", "Company Storage");
        createTreeItem(t, "Photo Enthusiasts", "Company Storage");
        t.expandItem("Company Storage");

        t.setItemIcon("Photo Enthusiasts", new ThemeResource(
                "icons/16/users.png"));
        t.setChildrenAllowed("Photo Enthusiasts", false);
        margin.addComponent(t);

        // Spacing
        margin.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
        Label text = new Label(
                "The above tree and the example screens on the right don't actually do anything, they are here purely for show.");
        text.addStyleName(Runo.LABEL_SMALL);
        margin.addComponent(text);
        text.setWidth("90%");

        return margin;
    }

    void createTreeItem(Tree tree, String caption, String parent) {
        tree.addItem(caption);
        if (parent != null) {
            tree.setChildrenAllowed(parent, true);
            tree.setParent(caption, parent);
            if (parent.equals("Archive")) {
                tree.setChildrenAllowed(caption, false);
            }
        }
    }

    Layout buildWelcomeScreen() {
        VerticalLayout l = new VerticalLayout();
        l.setSizeFull();
        l.setCaption("Welcome");

        Panel welcome = new Panel("Runo Theme");
        welcome.setSizeFull();
        welcome.addStyleName(Runo.PANEL_LIGHT);
        l.addComponent(welcome);
        l.setExpandRatio(welcome, 1);

        CssLayout margin = new CssLayout();
        margin.setMargin(true);
        margin.setWidth("100%");
        welcome.setContent(margin);

        Label title = new Label("Runo Theme");
        title.addStyleName(Runo.LABEL_H1);
        // margin.addComponent(title);

        HorizontalLayout texts = new HorizontalLayout();
        texts.setSpacing(true);
        texts.setWidth("100%");
        margin.addComponent(texts);

        Label text = new Label(
                "<h3>A Complete Theme</h3><p>The Runo theme is a complete, general purpose theme suitable for many types of applications.</p><p>The name Runo is a Finnish word, meaning \"a poem\" in English. It is also used to refer to a very particular kind of female reindeer.</p>",
                Label.CONTENT_XHTML);
        texts.addComponent(text);
        texts.setExpandRatio(text, 1);

        // Spacer
        text = new Label("");
        text.setWidth("20px");
        texts.addComponent(text);

        text = new Label(
                "<h3>Everything You Need Is Here</h3><p>Everything you see inside this application, all the different styles, are provided by the Runo theme, out-of-the-box. That means you don't necessarily need to create any custom CSS for your application: you can build a cohesive result writing plain Java code.</p><p>A little creativity, good organization and careful typography carries a long way.</p>",
                Label.CONTENT_XHTML);
        texts.addComponent(text);
        texts.setExpandRatio(text, 1);

        // Spacer
        text = new Label("");
        text.setWidth("20px");
        texts.addComponent(text);

        text = new Label(
                "<h3>The Names of The Styles</h3><p>Look for a class named <code>Runo</code> inside the Vaadin JAR (<code>com.vaadin.ui.themes.Runo</code>).</p><p>All the available style names are documented and available there as constants, prefixed by component names, e.g. <code>Runo.BUTTON_SMALL</code>.</p>",
                Label.CONTENT_XHTML);
        texts.addComponent(text);
        texts.setExpandRatio(text, 1);

        l.addComponent(new Label("<hr />", Label.CONTENT_XHTML));

        texts = new HorizontalLayout();
        texts.addStyleName(Runo.LAYOUT_DARKER);
        texts.setSpacing(true);
        texts.setWidth("100%");
        texts.setMargin(true);
        l.addComponent(texts);

        text = new Label(
                "<h4>About This Application</h4>In addition to this welcome screen, you'll find the style name reference and sample views within the two other main tabs.",
                Label.CONTENT_XHTML);
        text.addStyleName(Runo.LABEL_SMALL);
        texts.addComponent(text);
        texts.setExpandRatio(text, 1);

        // Spacer
        text = new Label("");
        text.setWidth("20px");
        texts.addComponent(text);

        Button next = new Button("View Samples Â»", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                right.setSelectedTab(samples);
            }
        });
        next.setWidth("100%");
        next.addStyleName(Runo.BUTTON_DEFAULT);
        next.addStyleName(Runo.BUTTON_BIG);
        texts.addComponent(next);
        texts.setComponentAlignment(next, Alignment.BOTTOM_LEFT);
        texts.setExpandRatio(next, 1);

        // Spacer
        text = new Label("");
        text.setWidth("20px");
        texts.addComponent(text);

        next = new Button("Style Reference Â»", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                right.setSelectedTab(styles);
            }
        });
        next.setWidth("100%");
        next.addStyleName(Runo.BUTTON_DEFAULT);
        next.addStyleName(Runo.BUTTON_BIG);
        texts.addComponent(next);
        texts.setComponentAlignment(next, Alignment.BOTTOM_LEFT);
        texts.setExpandRatio(next, 1);

        return l;
    }

    ComponentContainer buildStyleReference() {
        styles.addStyleName(Runo.TABSHEET_SMALL);
        styles.setSizeFull();

        styles.addTab(buildLabels());
        styles.addTab(buildButtons());
        styles.addTab(buildTextFields());
        styles.addTab(buildSelects());
        styles.addTab(buildDateFields());
        styles.addTab(buildSliders());
        styles.addTab(buildTabSheets());
        styles.addTab(buildAccordions());
        styles.addTab(buildPanels());
        styles.addTab(buildSplitPanels());
        styles.addTab(buildTables());
        styles.addTab(buildWindows());
        styles.addTab(buildNotifications());
        styles.addTab(buildLayouts());

        return styles;
    }

    Layout buildLabels() {
        GridLayout l = new GridLayout(2, 1);
        l.setWidth("560px");
        l.setSpacing(true);
        l.setMargin(true);
        l.setCaption("Labels");

        l.addComponent(new Label("Header Style (<code>Runo.LABEL_H1</code>)",
                Label.CONTENT_XHTML));
        Label label = new Label("Lorem Ipsum");
        label.addStyleName(Runo.LABEL_H1);
        l.addComponent(label);

        l.addComponent(new Label(
                "Sub-header Style (<code>Runo.LABEL_H2</code>)",
                Label.CONTENT_XHTML));
        label = new Label("Lorem Ipsum Dolor");
        label.addStyleName(Runo.LABEL_H2);
        l.addComponent(label);

        l.addComponent(new Label("Normal Label", Label.CONTENT_XHTML));
        l.addComponent(new Label(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."));

        l.addComponent(new Label("Small Style (<code>Runo.LABEL_SMALL</code>)",
                Label.CONTENT_XHTML));
        label = new Label(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
        label.addStyleName(Runo.LABEL_SMALL);
        l.addComponent(label);

        return l;
    }

    Layout buildButtons() {
        GridLayout l = new GridLayout(2, 1);
        l.setCaption("Buttons");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("500px");
        l.setColumnExpandRatio(0, 1);

        l.addComponent(new Label("Normal Button", Label.CONTENT_XHTML));

        Button b = new Button("Normal Button");
        l.addComponent(b);

        l.addComponent(new Label(
                "\"Default\" Style<br />(<code>Runo.BUTTON_DEFAULT</code>)",
                Label.CONTENT_XHTML));

        b = new Button("Default Button");
        b.setStyleName(Runo.BUTTON_DEFAULT);
        l.addComponent(b);

        l.addComponent(new Label(
                "Big Style<br />(<code>Runo.BUTTON_BIG</code>)",
                Label.CONTENT_XHTML));
        b = new Button("Big Button");
        b.setStyleName(Runo.BUTTON_BIG);
        l.addComponent(b);

        l.addComponent(new Label(
                "Big Default<br />(<code>Runo.BUTTON_BIG & Runo.BUTTON_DEFAULT</code>)",
                Label.CONTENT_XHTML));
        b = new Button("Big Default");
        b.setStyleName(Runo.BUTTON_BIG);
        b.addStyleName(Runo.BUTTON_DEFAULT);
        l.addComponent(b);

        l.addComponent(new Label(
                "Small Style<br />(<code>Runo.BUTTON_SMALL</code>)",
                Label.CONTENT_XHTML));

        b = new Button("Small Button");
        b.setStyleName(Runo.BUTTON_SMALL);
        l.addComponent(b);
        l.addComponent(new Label(
                "Small Default<br />(<code>Runo.BUTTON_SMALL & Runo.BUTTON_DEFAULT</code>)",
                Label.CONTENT_XHTML));

        b = new Button("Small Default");
        b.setStyleName(Runo.BUTTON_SMALL);
        b.addStyleName(Runo.BUTTON_DEFAULT);
        l.addComponent(b);
        l.addComponent(new Label(
                "Disabled Button<br />(<code>Button.setEnabled(false)</code>)",
                Label.CONTENT_XHTML));
        b = new Button("Disabled Button");
        b.setEnabled(false);
        l.addComponent(b);

        l.addComponent(new Label(
                "Link Style<br />(<code>Runo.BUTTON_LINK</code>)",
                Label.CONTENT_XHTML));
        b = new Button("Link Button");
        b.setStyleName(Runo.BUTTON_LINK);
        l.addComponent(b);

        return l;
    }

    Layout buildTextFields() {
        GridLayout l = new GridLayout(2, 1);
        l.setCaption("Text fields");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("400px");
        l.setColumnExpandRatio(0, 1);

        l.addComponent(new Label("Normal TextField", Label.CONTENT_XHTML));
        TextField tf = new TextField();
        tf.setInputPrompt("Enter text");
        l.addComponent(tf);

        l.addComponent(new Label(
                "Small Style (<code>Runo.TEXTFIELD_SMALL</code>)",
                Label.CONTENT_XHTML));

        tf = new TextField();
        tf.setStyleName(Runo.TEXTFIELD_SMALL);
        tf.setInputPrompt("Enter text");
        l.addComponent(tf);

        l.addComponent(new Label("Normal TextArea", Label.CONTENT_XHTML));

        tf = new TextField();
        tf.setHeight("5em");
        tf.setInputPrompt("Enter text");
        l.addComponent(tf);

        l.addComponent(new Label(
                "Small Style TextArea (<code>Runo.TEXTFIELD_SMALL</code>)",
                Label.CONTENT_XHTML));

        tf = new TextField();
        tf.setHeight("5em");
        tf.setStyleName(Runo.TEXTFIELD_SMALL);
        tf.setInputPrompt("Enter text");
        l.addComponent(tf);

        return l;
    }

    Layout buildSelects() {
        VerticalLayout l = new VerticalLayout();
        l.setCaption("Selects");
        l.setMargin(true);
        l.setSpacing(true);

        l.addComponent(new Label(
                "Selects don't currently have any additional style names."));

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setMargin(true, false, false, false);
        l.addComponent(hl);

        AbstractSelect cb = new ComboBox();
        AbstractSelect nat = new NativeSelect();
        AbstractSelect list = new ListSelect();
        AbstractSelect twincol = new TwinColSelect();

        for (int i = 0; i < 25; i++) {
            cb.addItem("Item " + i);
            nat.addItem("Item " + i);
            list.addItem("Item " + i);
            twincol.addItem("Item " + i);
        }

        hl.addComponent(cb);
        hl.addComponent(nat);
        hl.addComponent(list);
        hl.addComponent(twincol);

        return l;
    }

    Layout buildDateFields() {
        VerticalLayout l = new VerticalLayout();
        l.setCaption("Date fields");
        l.setMargin(true);
        l.setSpacing(true);

        l.addComponent(new Label(
                "Date fields don't currently have any additional style names."));

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.setMargin(true, false, false, false);
        l.addComponent(hl);

        DateField df = new DateField();
        df.setValue(DATEFIELD_VALUE);
        df.setResolution(DateField.RESOLUTION_MIN);
        hl.addComponent(df);

        df = new InlineDateField();
        df.setValue(DATEFIELD_VALUE);
        df.setResolution(DateField.RESOLUTION_DAY);
        hl.addComponent(df);

        df = new InlineDateField();
        df.setValue(DATEFIELD_VALUE);
        df.setResolution(DateField.RESOLUTION_YEAR);
        hl.addComponent(df);

        return l;
    }

    Layout buildTabSheets() {
        GridLayout l = new GridLayout(2, 1);
        l.setCaption("Tabs");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("700px");
        l.setColumnExpandRatio(0, 3);
        l.setColumnExpandRatio(1, 5);

        HorizontalLayout checks = new HorizontalLayout();
        checks.setSpacing(true);

        CheckBox closable = new CheckBox("Closable tabs");
        closable.setImmediate(true);
        checks.addComponent(closable);

        l.addComponent(checks, 1, 0);
        l.setCursorX(0);
        l.setCursorY(1);

        l.addComponent(new Label("Normal Tabs", Label.CONTENT_XHTML));

        final TabSheet ts = new TabSheet();
        ts.setHeight("100px");
        l.addComponent(ts);

        l.addComponent(new Label(
                "Small Style (<code>Runo.TABSHEET_SMALL</code>)",
                Label.CONTENT_XHTML));

        final TabSheet ts2 = new TabSheet();
        ts2.setStyleName(Runo.TABSHEET_SMALL);
        l.addComponent(ts2);

        for (int i = 1; i < 10; i++) {
            ts.addTab(new Label(), "Tab " + i);
            ts2.addTab(new Label(), "Tab " + i);
        }

        closable.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                Iterator<Component> it = ts.getComponentIterator();
                Iterator<Component> it2 = ts2.getComponentIterator();
                for (; it.hasNext();) {
                    Component c = it.next();
                    ts.getTab(c).setClosable(event.getButton().booleanValue());
                }
                for (; it2.hasNext();) {
                    Component c = it2.next();
                    ts2.getTab(c).setClosable(event.getButton().booleanValue());
                }
            }
        });

        return l;
    }

    Layout buildPanels() {
        GridLayout l = new GridLayout(2, 1);
        l.setCaption("Panels");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("700px");
        l.setColumnExpandRatio(0, 2);
        l.setColumnExpandRatio(1, 5);

        l.addComponent(new Label("Normal Panel", Label.CONTENT_XHTML));

        Panel p = new Panel("Normal Panel");
        p.addComponent(new Label("Panel content"));
        l.addComponent(p);

        l.addComponent(new Label("Light Style (<code>Runo.PANEL_LIGHT</code>)",
                Label.CONTENT_XHTML));

        Panel p2 = new Panel("Light Style Panel");
        p2.setStyleName("light");
        p2.addComponent(new Label("Panel content"));
        l.addComponent(p2);

        return l;
    }

    Layout buildTables() {
        GridLayout l = new GridLayout(2, 1);
        l.setCaption("Tables");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("700px");
        l.setColumnExpandRatio(0, 3);
        l.setColumnExpandRatio(1, 5);

        for (int i = 0; i < 4; i++) {

            Table t = new Table();
            t.setWidth("100%");
            t.setPageLength(3);
            t.setSelectable(true);
            t.setColumnCollapsingAllowed(true);
            t.setColumnReorderingAllowed(true);

            if (i == 2) {
                t.setStyleName(Runo.TABLE_SMALL);
                l.addComponent(new Label(
                        "Small Style (<code>Runo.TABLE_SMALL</code>)",
                        Label.CONTENT_XHTML));
            } else if (i == 1) {
                t.setStyleName(Runo.TABLE_BORDERLESS);
                l.addComponent(new Label(
                        "Borderless Style (<code>Runo.TABLE_BORDERLESS</code>)",
                        Label.CONTENT_XHTML));
            } else if (i == 3) {
                t.setStyleName(Runo.TABLE_BORDERLESS);
                t.addStyleName(Runo.TABLE_SMALL);
                l.addComponent(new Label(
                        "Borderless & Small (<code>Runo.TABLE_BORDERLESS & Runo.TABLE_SMALL</code>)",
                        Label.CONTENT_XHTML));
            } else {
                l.addComponent(new Label("Normal Table", Label.CONTENT_XHTML));
            }

            t.addContainerProperty("First", String.class, null);
            t.addContainerProperty("Second", String.class, null);
            t.addContainerProperty("Third", String.class, null);

            for (int j = 0; j < 100; j++) {
                t.addItem(new Object[] { "Foo " + j, "Bar value " + j,
                        "Last column value " + j }, j);
            }

            l.addComponent(t);
        }
        return l;
    }

    Layout buildWindows() {
        final CssLayout l = new CssLayout();
        l.setCaption("Windows");

        final Window w = new Window("Normal Window");
        w.setWidth("250px");
        w.setHeight("180px");
        w.setPositionX(120);
        w.setPositionY(200);

        final Window w2 = new Window("Dialog Window");
        w2.setWidth("250px");
        w2.setHeight("180px");
        w2.setStyleName("dialog");
        w2.setPositionX(400);
        w2.setPositionY(200);
        w2.addComponent(new Label("<code>Runo.WINDOW_DIALOG</code>",
                Label.CONTENT_XHTML));
        w2.getContent().addStyleName(Runo.LAYOUT_DARKER);

        TabSheet.SelectedTabChangeListener reveal = new TabSheet.SelectedTabChangeListener() {
            public void selectedTabChange(SelectedTabChangeEvent event) {
                if (right.getSelectedTab() == styles
                        && styles.getSelectedTab() == l) {
                    getMainWindow().addWindow(w);
                    getMainWindow().addWindow(w2);
                } else {
                    getMainWindow().removeWindow(w);
                    getMainWindow().removeWindow(w2);
                }
            }
        };
        styles.addListener(reveal);
        right.addListener(reveal);

        return l;
    }

    Layout buildSplitPanels() {
        final GridLayout l = new GridLayout(2, 1);
        l.setCaption("Split panels");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("400px");
        l.setColumnExpandRatio(0, 1);

        HorizontalLayout checks = new HorizontalLayout();
        checks.setSpacing(true);

        CheckBox locked = new CheckBox("Locked");
        locked.setDescription("Prevent split dragging");
        locked.setImmediate(true);
        checks.addComponent(locked);

        l.addComponent(checks, 1, 0);
        l.setCursorX(0);
        l.setCursorY(1);

        l.addComponent(new Label("Normal SplitPanel", Label.CONTENT_XHTML));

        final HorizontalSplitPanel sp = new HorizontalSplitPanel();
        sp.setWidth("100px");
        sp.setHeight("120px");
        l.addComponent(sp);

        l.addComponent(new Label(
                "Reduced Style (<code>Runo.SPLITPANEL_REDUCED</code>)",
                Label.CONTENT_XHTML));

        final HorizontalSplitPanel sp2 = new HorizontalSplitPanel();
        sp2.setStyleName(Runo.SPLITPANEL_REDUCED);
        sp2.setWidth("100px");
        sp2.setHeight("120px");
        l.addComponent(sp2);

        l.addComponent(new Label(
                "Small Style (<code>Runo.SPLITPANEL_SMALL</code>)",
                Label.CONTENT_XHTML));

        final HorizontalSplitPanel sp3 = new HorizontalSplitPanel();
        sp3.setStyleName(Runo.SPLITPANEL_SMALL);
        sp3.setWidth("100px");
        sp3.setHeight("120px");
        l.addComponent(sp3);

        locked.addListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                sp.setLocked(event.getButton().booleanValue());
                sp2.setLocked(event.getButton().booleanValue());
                sp3.setLocked(event.getButton().booleanValue());
            }
        });

        return l;
    }

    Layout buildAccordions() {
        final GridLayout l = new GridLayout(2, 1);
        l.setCaption("Accordions");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("600px");
        l.setColumnExpandRatio(0, 1);
        l.setColumnExpandRatio(1, 2);

        l.addComponent(new Label("Normal Accordion", Label.CONTENT_XHTML));
        Accordion a = new Accordion();
        a.setWidth("100%");
        a.setHeight("170px");
        l.addComponent(a);

        l.addComponent(new Label(
                "Light Style<br />(<code>Runo.ACCORDION_LIGHT</code>)",
                Label.CONTENT_XHTML));
        Accordion a2 = new Accordion();
        a2.setWidth("100%");
        a2.setHeight("170px");
        a2.addStyleName(Runo.ACCORDION_LIGHT);
        l.addComponent(a2);

        for (int i = 1; i < 4; i++) {
            a.addTab(new Label(), "Sheet " + i);
            a2.addTab(new Label(), "Sheet " + i);
        }

        return l;
    }

    Layout buildSliders() {
        final GridLayout l = new GridLayout(2, 1);
        l.setCaption("Sliders");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("400px");
        l.setColumnExpandRatio(0, 1);

        l.addComponent(new Label("Normal Slider", Label.CONTENT_XHTML));
        Slider s = new Slider();
        s.setWidth("200px");
        try {
            s.setValue(50);
        } catch (ValueOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        l.addComponent(s);

        return l;
    }

    Layout buildNotifications() {
        final GridLayout l = new GridLayout(2, 1);
        l.setCaption("Notifications");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("400px");
        l.setColumnExpandRatio(0, 1);

        final TextField title = new TextField("Notification caption");
        title.setValue("Brown Fox!");
        final TextField message = new TextField("Notification description");
        message.setValue("Jumped over the lazy dog.");
        message.setWidth("15em");

        l.addComponent(new Label("<h3>Type</h3>", Label.CONTENT_XHTML));
        l.addComponent(new Label("<h3>Preview</h3>", Label.CONTENT_XHTML));

        l.addComponent(new Label("Humanized", Label.CONTENT_XHTML));
        Button show = new Button("Humanized Notification",
                new Button.ClickListener() {
                    public void buttonClick(ClickEvent event) {
                        event.getButton()
                                .getWindow()
                                .showNotification((String) title.getValue(),
                                        (String) message.getValue());

                    }
                });
        l.addComponent(show);

        l.addComponent(new Label("Warning", Label.CONTENT_XHTML));
        show = new Button("Warning Notification", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                event.getButton()
                        .getWindow()
                        .showNotification((String) title.getValue(),
                                (String) message.getValue(),
                                Notification.TYPE_WARNING_MESSAGE);

            }
        });
        l.addComponent(show);

        l.addComponent(new Label("Error", Label.CONTENT_XHTML));
        show = new Button("Error Notification", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                event.getButton()
                        .getWindow()
                        .showNotification((String) title.getValue(),
                                (String) message.getValue(),
                                Notification.TYPE_ERROR_MESSAGE);

            }
        });
        l.addComponent(show);

        l.addComponent(new Label("Tray", Label.CONTENT_XHTML));
        show = new Button("Tray Notification", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                event.getButton()
                        .getWindow()
                        .showNotification((String) title.getValue(),
                                (String) message.getValue(),
                                Notification.TYPE_TRAY_NOTIFICATION);

            }
        });
        l.addComponent(show);

        l.addComponent(title);
        l.addComponent(message);

        return l;
    }

    Layout buildLayouts() {
        final GridLayout l = new GridLayout(2, 1);
        l.setCaption("Layouts");
        l.setMargin(true);
        l.setSpacing(true);
        l.setWidth("550px");
        l.setColumnExpandRatio(0, 1);

        l.addComponent(new Label(
                "Box Shadow<br />(<code>Runo.CSSLAYOUT_SHADOW</code>)",
                Label.CONTENT_XHTML));
        CssLayout layout = new CssLayout();
        layout.addStyleName(Runo.CSSLAYOUT_SHADOW);
        Label text = new Label("Content");
        text.setSizeUndefined();
        VerticalLayout align = new VerticalLayout();
        align.addStyleName(Runo.LAYOUT_DARKER);
        align.setWidth("100px");
        align.setHeight("100px");
        align.addComponent(text);
        align.setComponentAlignment(text, Alignment.MIDDLE_CENTER);
        layout.addComponent(align);
        l.addComponent(layout);

        l.addComponent(new Label(
                "Selectable<br />(<code>Runo.CSSLAYOUT_SELECTABLE</code> & <code>Runo.CSSLAYOUT_SELECTABLE_SELECTED</code>)",
                Label.CONTENT_XHTML));
        layout = new CssLayout();
        layout.addStyleName(Runo.CSSLAYOUT_SELECTABLE);
        layout.addStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
        layout.addListener(new LayoutClickListener() {
            public void layoutClick(LayoutClickEvent event) {
                if (event.getComponent().getStyleName()
                        .contains(Runo.CSSLAYOUT_SELECTABLE_SELECTED)) {
                    event.getComponent().removeStyleName(
                            Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                } else {
                    event.getComponent().addStyleName(
                            Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
            }
        });
        text = new Label("Click here");
        text.setSizeUndefined();
        align = new VerticalLayout();
        align.setWidth("100px");
        align.setHeight("100px");
        align.addComponent(text);
        align.setComponentAlignment(text, Alignment.MIDDLE_CENTER);
        layout.addComponent(align);
        l.addComponent(layout);

        return l;
    }

    ComponentContainer buildSamples() {
        samples.addStyleName(Runo.TABSHEET_SMALL);
        samples.setSizeFull();

        samples.addTab(buildLibraryScreen());
        samples.addTab(buildBillingScreen());

        return samples;
    }

    Layout buildBillingScreen() {
        AbsoluteLayout root = new AbsoluteLayout();
        root.setSizeFull();
        root.setCaption("Time Tracking");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);

        Button b = new Button("+ Add Hours", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                Window w = new Window("Add Hours");
                w.addStyleName(Runo.WINDOW_DIALOG);
                w.setModal(true);
                w.setResizable(false);
                w.setCloseShortcut(KeyCode.ESCAPE, null);
                FormLayout l = new FormLayout();
                l.setSizeUndefined();
                w.setContent(l);
                l.setMargin(true);
                l.setSpacing(true);
                NativeSelect s = new NativeSelect("Hour Type:");
                s.addItem("Billed");
                s.addItem("Not billed");
                s.setNullSelectionAllowed(false);
                s.select("Billed");
                l.addComponent(s);
                s.focus();
                l.addComponent(new ComboBox("Description:"));
                l.addComponent(new TextField("Notes:"));
                HorizontalLayout buttons = new HorizontalLayout();
                buttons.setSpacing(true);
                Button b = new Button("Add");
                b.addStyleName(Runo.BUTTON_DEFAULT);
                buttons.addComponent(b);
                buttons.addComponent(new Button("Cancel"));
                l.addComponent(buttons);
                event.getButton().getWindow().addWindow(w);
            }
        });
        b.addStyleName(Runo.BUTTON_DEFAULT);
        buttons.addComponent(b);
        b = new Button("Generate Report", new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                Window w = new Window("Generate Workhours Report");
                w.setPositionX(50);
                w.setPositionY(70);
                w.setResizable(false);
                w.setCloseShortcut(KeyCode.ESCAPE, null);
                GridLayout root = new GridLayout(2, 3);
                root.setMargin(true);
                root.setSpacing(true);
                w.setContent(root);
                CssLayout format = new CssLayout();
                OptionGroup opt = new OptionGroup("1. Select output format");
                opt.addItem("Excel sheet");
                opt.addItem("CSV plain text");
                opt.select("CSV plain text");
                format.addComponent(opt);
                ComboBox csv = new ComboBox();
                csv.setWidth("170px");
                csv.addItem("Separate by comma (,)");
                csv.addItem("Separate by colon (:)");
                csv.addItem("Separate by semicolon (;)");
                csv.setNullSelectionAllowed(false);
                csv.select("Separate by comma (,)");
                CssLayout margin = new CssLayout();
                margin.setMargin(false, false, true, true);
                margin.addComponent(csv);
                format.addComponent(margin);
                root.addComponent(format);

                CssLayout res = new CssLayout();
                res.setCaption("Output resolution");
                Slider slider = new Slider();
                try {
                    slider.setValue(30);
                } catch (ValueOutOfBoundsException e) {
                    // Ignore
                }
                res.addComponent(slider);
                slider.setWidth("200px");
                HorizontalLayout labels = new HorizontalLayout();
                labels.setWidth("200px");
                Label text = new Label("Coarse info");
                text.setSizeUndefined();
                text.addStyleName(Runo.LABEL_SMALL);
                labels.addComponent(text);
                text = new Label("Fine details");
                text.setSizeUndefined();
                text.addStyleName(Runo.LABEL_SMALL);
                labels.addComponent(text);
                labels.setComponentAlignment(text, Alignment.TOP_RIGHT);
                res.addComponent(labels);
                root.addComponent(res);

                ComboBox rec = new ComboBox("2. Select recepient");
                rec.addItem("john.doe@example.org");
                rec.addItem("harry.driver@example.org");
                rec.addItem("guybrush.threepwood@melee.mi");
                rec.setNewItemsAllowed(true);
                root.addComponent(rec);
                rec.setWidth("188px");

                HorizontalLayout buttons = new HorizontalLayout();
                buttons.setSpacing(true);
                Button b = new Button("Generate");
                b.addStyleName(Runo.BUTTON_DEFAULT);
                buttons.addComponent(b);
                b = new Button("Cancel");
                buttons.addComponent(b);
                root.addComponent(buttons, 0, 2, 1, 2);
                root.setComponentAlignment(buttons, Alignment.TOP_RIGHT);

                event.getButton().getWindow().addWindow(w);
            }
        });
        buttons.addComponent(b);
        root.addComponent(buttons, "top: 11px; right: 18px; z-index:1;");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        root.addComponent(content);

        Panel top = new Panel("Browse & Edit Workhours", new VerticalLayout());
        top.setSizeFull();
        top.addStyleName(Runo.PANEL_LIGHT);
        top.getContent().setSizeFull();
        content.addComponent(top);
        content.setExpandRatio(top, 1);

        Table table = new Table();
        table.setSizeFull();
        table.setSelectable(true);
        table.setColumnReorderingAllowed(true);
        table.addStyleName(Runo.TABLE_BORDERLESS);
        top.addComponent(table);
        table.addContainerProperty("info", Embedded.class, null);
        table.addContainerProperty("check", CheckBox.class, null);
        table.addContainerProperty("locked", Embedded.class, null);
        table.addContainerProperty("hours", String.class, "00:00:00");
        table.addContainerProperty("cost", String.class, "$0.00");
        table.addContainerProperty("start", String.class, "00/00/0000");
        table.addContainerProperty("end", String.class, "00/00/0000");
        table.addContainerProperty("note", Embedded.class, new Embedded(null,
                new ThemeResource("icons/16/note.png")));
        table.addContainerProperty("desc", String.class, null);
        table.setColumnHeaders(new String[] { "", "", "", "Hours", "Cost",
                "Start Date", "End Date", "Note", "Description" });
        table.setColumnAlignment("info", Table.ALIGN_CENTER);
        table.setColumnAlignment("check", Table.ALIGN_CENTER);
        table.setColumnAlignment("note", Table.ALIGN_CENTER);

        for (int j = 1; j < 6; j++) {
            Item i = table.addItem(j);
            i.getItemProperty("info").setValue(
                    create16pxIcon("icons/16/attention.png"));
            i.getItemProperty("check").setValue(new CheckBox(null, true));
            i.getItemProperty("note").setValue(
                    create16pxIcon("icons/16/note.png"));
        }
        table.select(1);
        Item i = table.getItem(1);
        i.getItemProperty("hours").setValue("07:02:18");
        i.getItemProperty("cost").setValue("$703.83");
        i.getItemProperty("start").setValue("1/17/10");
        i.getItemProperty("end").setValue("1/17/10");
        i.getItemProperty("desc").setValue("More revisions");

        i = table.getItem(2);
        i.getItemProperty("hours").setValue("04:00:00");
        i.getItemProperty("cost").setValue("$360.00");
        i.getItemProperty("start").setValue("1/14/10");
        i.getItemProperty("end").setValue("1/14/10");
        i.getItemProperty("desc").setValue("Algorithm selection");
        i.getItemProperty("note").setValue(null);

        i = table.getItem(3);
        i.getItemProperty("hours").setValue("02:34:45");
        i.getItemProperty("cost").setValue("$160.00");
        i.getItemProperty("start").setValue("1/13/10");
        i.getItemProperty("end").setValue("1/13/10");
        i.getItemProperty("desc").setValue("New features implementation");
        i.getItemProperty("note").setValue(null);

        i = table.getItem(4);
        i.getItemProperty("hours").setValue("0:14:00");
        i.getItemProperty("cost").setValue("$60.00");
        i.getItemProperty("start").setValue("1/6/10");
        i.getItemProperty("end").setValue("1/6/10");

        i = table.getItem(5);
        i.getItemProperty("hours").setValue("03:07:23");
        i.getItemProperty("cost").setValue("$560.00");
        i.getItemProperty("start").setValue("1/5/10");
        i.getItemProperty("end").setValue("1/5/10");
        i.getItemProperty("desc").setValue("More revisions");
        i.getItemProperty("note").setValue(null);

        content.addComponent(new Label("<hr />", Label.CONTENT_XHTML));

        VerticalLayout bottom = new VerticalLayout();
        bottom.setMargin(true);
        bottom.setSpacing(true);
        bottom.addStyleName(Runo.LAYOUT_DARKER);
        content.addComponent(bottom);

        HorizontalLayout line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
                c.setSizeUndefined();
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        Label first = new Label("Item Type:");
        line.addComponent(first);
        first.setWidth("80px");
        NativeSelect select = new NativeSelect();
        select.addItem("Timed");
        select.addItem("Not billable");
        select.setNullSelectionAllowed(false);
        select.select("Timed");
        line.addComponent(select);
        line.addComponent(new Label("Customer Hourly Rate:"));
        TextField tf = new TextField();
        tf.setInputPrompt("$45.00");
        line.addComponent(tf);
        tf.setWidth("100%");
        line.setExpandRatio(tf, 1);
        line.addComponent(new Button("Remove"));
        CheckBox cb = new CheckBox("Taxable");
        cb.setValue(true);
        line.addComponent(cb);
        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
                c.setSizeUndefined();
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        first = new Label("Hours:");
        line.addComponent(first);
        first.setWidth("80px");
        first = new Label("11:56:10 from 1 timing session.");
        line.addComponent(first);
        line.setExpandRatio(first, 1);
        line.addComponent(new Button("Timing Sessions"));
        line.addComponent(new Button("Quick Add"));
        line.addComponent(new Button("Quick Modify"));
        cb = new CheckBox("Included");
        cb.setValue(true);
        line.addComponent(cb);
        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_RIGHT);
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        first = new Label("Description:");
        line.addComponent(first);
        first.setWidth("80px");
        ComboBox combo = new ComboBox();
        combo.setInputPrompt("Add a description");
        combo.setNewItemsAllowed(true);
        line.addComponent(combo);
        combo.setWidth("100%");
        line.setExpandRatio(combo, 1);
        bottom.addComponent(line);

        line = new HorizontalLayout() {
            @Override
            public void addComponent(Component c) {
                super.addComponent(c);
                setComponentAlignment(c, Alignment.TOP_RIGHT);
            }
        };
        line.setWidth("100%");
        line.setSpacing(true);
        first = new Label("Notes:");
        line.addComponent(first);
        first.setWidth("80px");
        tf = new TextField();
        line.addComponent(tf);
        tf.setWidth("100%");
        tf.setHeight("4em");
        line.setExpandRatio(tf, 1);
        bottom.addComponent(line);

        return root;
    }

    private Embedded create16pxIcon(String iconid) {
        Embedded em = new Embedded(null, new ThemeResource(iconid));
        em.setStyleName("icon-16");
        return em;
    }

    Layout buildLibraryScreen() {
        AbsoluteLayout root = new AbsoluteLayout();
        root.setSizeFull();
        root.setCaption("Media Library");

        HorizontalLayout size = new HorizontalLayout();
        size.setSpacing(true);
        size.addComponent(new Label("-"));
        Slider slider = new Slider();
        try {
            slider.setValue(70);
        } catch (ValueOutOfBoundsException e) {
            // Ignore
        }
        slider.setWidth("200px");
        size.addComponent(slider);
        size.addComponent(new Label("+"));
        root.addComponent(size, "top: 16px; right: 18px; z-index:1;");

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        root.addComponent(content);

        final GridLayout grid = new GridLayout(4, 1);
        Panel top = new Panel("My Book Collection", grid);
        top.setSizeFull();
        top.addStyleName(Runo.PANEL_LIGHT);
        grid.setWidth("100%");
        grid.setMargin(true);
        grid.addStyleName(Runo.LAYOUT_DARKER);
        content.addComponent(top);
        content.setExpandRatio(top, 1);

        grid.addListener(new LayoutClickListener() {
            public void layoutClick(LayoutClickEvent event) {
                for (Iterator<Component> it = grid.getComponentIterator(); it
                        .hasNext();) {
                    Component c = it.next();
                    c.removeStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
                if (event.getChildComponent() != null) {
                    event.getChildComponent().addStyleName(
                            Runo.CSSLAYOUT_SELECTABLE_SELECTED);
                }
            }
        });

        final String[] covers = new String[] { "designing-interfaces.png",
                "comics.png", "gdtnb.png", "new-mind.png", "simplicity.png",
                "upod.png", "designing-interactions.png", "rogue-leaders.png",
                "tcss.png", "wfd.png", "new-type.png" };

        for (String cover : covers) {
            CssLayout select = new CssLayout();
            select.addStyleName(Runo.CSSLAYOUT_SELECTABLE);
            CssLayout book = new CssLayout();
            book.addStyleName(Runo.CSSLAYOUT_SHADOW);
            book.addComponent(new Embedded(null, new ThemeResource(
                    "../demo/book-covers/" + cover)));
            select.addComponent(book);
            grid.addComponent(select);
            grid.setComponentAlignment(select, Alignment.MIDDLE_CENTER);
            if (cover.equals("gdtnb.png")) {
                select.addStyleName(Runo.CSSLAYOUT_SELECTABLE_SELECTED);
            }
        }

        Label text = new Label(
                "Note: the book cover images are not included in the Runo theme, they are provided extenally for this example. The shadow for the books is supplied by the theme.");
        text.addStyleName(Runo.LABEL_SMALL);
        text.setWidth("90%");
        grid.addComponent(text);
        grid.setComponentAlignment(text, Alignment.MIDDLE_CENTER);

        text = new Label("");
        text.addStyleName("hr");
        content.addComponent(text);

        HorizontalLayout bottom = new HorizontalLayout();
        bottom.setWidth("100%");
        content.addComponent(bottom);

        VerticalLayout side = new VerticalLayout();
        side.setMargin(true);
        side.setSpacing(true);
        side.setWidth("170px");
        bottom.addComponent(side);

        CssLayout book = new CssLayout();
        book.addStyleName(Runo.CSSLAYOUT_SHADOW);
        book.addComponent(new Embedded(null, new ThemeResource(
                "../demo/book-covers/gdtnb.png")));
        side.addComponent(book);
        NativeSelect read = new NativeSelect("Mark the book as:");
        read.setWidth("130px");
        read.addItem("Not Read");
        read.addItem("Read");
        read.setNullSelectionAllowed(false);
        read.select("Read");
        side.addComponent(read);
        read = new NativeSelect();
        read.setWidth("130px");
        read.addItem("Mine");
        read.addItem("Loaned");
        read.setNullSelectionAllowed(false);
        read.select("Loaned");
        side.addComponent(read);

        CssLayout details = new CssLayout();
        details.setWidth("100%");
        details.setMargin(false, true, false, false);
        bottom.addComponent(details);
        bottom.setExpandRatio(details, 1);

        Label title = new Label(
                "<h3>Graphic Design &ndash; The New Basics</h3>",
                Label.CONTENT_XHTML);
        details.addComponent(title);
        title.setSizeUndefined();

        TabSheet tabs = new TabSheet();
        tabs.addStyleName(Runo.TABSHEET_SMALL);
        tabs.setWidth("100%");
        tabs.setHeight("180px");
        details.addComponent(tabs);

        FormLayout l = new FormLayout();
        tabs.addTab(l, "Info");
        text = new Label("248 pages");
        text.setCaption("Hardcover:");
        l.addComponent(text);
        text = new Label(
                "Princeton Architectural Press; 1 edition (May 1, 2008)");
        text.setCaption("Publisher:");
        l.addComponent(text);
        text = new Label("English");
        text.setCaption("Language:");
        l.addComponent(text);
        text = new Label("1568987706");
        text.setCaption("ISBN-10:");
        l.addComponent(text);
        text = new Label("978-1568987705");
        text.setCaption("ISBN-13:");
        l.addComponent(text);
        text = new Label("9.1 x 8.1 x 1.1 inches");
        text.setCaption("Product Dimensions:");
        l.addComponent(text);
        text = new Label("2.2 pounds");
        text.setCaption("Shipping Weight:");
        l.addComponent(text);

        tabs.addTab(new Label(), "Reviews");
        tabs.addTab(new Label(), "Personal");

        return root;
    }
}
