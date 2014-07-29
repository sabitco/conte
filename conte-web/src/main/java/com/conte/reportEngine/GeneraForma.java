package com.conte.reportEngine;

import com.conte.common.service.ReportDAO;
import static com.conte.reportEngine.config.Constantes.*;
import com.vaadin.data.validator.DoubleValidator;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Form;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * Crea las formas de vaadin de forma automatica a partir del tipo de datos y
 * vectores recibidos
 *
 * @author pedrorozo
 *
 */
@SuppressWarnings("rawtypes")
public class GeneraForma implements Serializable {

    private static final long serialVersionUID = -2356296183287373959L;
    private Form forma = new Form();
    private ReportDAO reportDAO = new ReportDAO();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Crea formas parametricas para maestro o detalle
     *
     * @param titulo
     * @param contenidos
     * @param titulos
     * @param tipoDato
     * @param tipoCampo
     * @param longitud
     * @param requerido
     * @param combosMaestro
     * @param tiposCombosMaestro
     * @param valorCombosMaestro
     * @param radioMaestro
     * @return
     */
    public Form creaForma(String titulo, Vector contenidos, String[] titulos, String[] tipoDato, int[] tipoCampo,
            int[] longitud, int[] requerido, Vector combosMaestro, Vector tiposCombosMaestro,
            Vector valorCombosMaestro, Vector radioMaestro, int pk, boolean edicion,
            boolean maestro, Vector pkMaes, Vector pkDet, String[] valorespkMaestro, String[] camposTabla) {
        forma.removeAllProperties();
        forma.setCaption(titulo);
        forma.setWriteThrough(false);  // habilita buffer de datos
        Object[] pkDetalle=new Object[0];
        Object[] pkMaestro=new Object[0];
        if (!maestro) {
            pkMaestro=pkMaes.toArray();
            pkDetalle=pkDet.toArray();
            logger.debug("PK Maestro=" + StringUtils.arrayToDelimitedString(pkMaestro, " "));
            logger.debug("PK Detalle=" + StringUtils.arrayToDelimitedString(pkDetalle, " "));
            logger.debug("PK Valores=" + StringUtils.arrayToDelimitedString(valorespkMaestro, " "));
        }
        int i = 0;

        for (Object contenido : contenidos) {
            int posicionLlave=-1;
            if (!maestro) {
                posicionLlave = this.buscarCampoLLave(camposTabla[i], pkDetalle);
               if (posicionLlave != -1 && !edicion) {
                   contenido = valorespkMaestro[posicionLlave];
               }
            }

            if (tipoCampo[i] == TIPOC_DATAFIELD) // crea campos
            {
                if (tipoDato[i].equalsIgnoreCase(TIPOD_DATE)) // crea campos para fechas
                {
                    PopupDateField t = new PopupDateField(titulos[i]);  // titulo

                    if (edicion && contenido != null) {

                        if (contenido.toString().length() > 8) {
                            contenido = Date.valueOf(Utiles.corrigeDatos(contenido.toString().substring(0, 10)));
                        }
                        t.setValue(contenido);
                    }
                    t.setResolution(PopupDateField.RESOLUTION_DAY);
                    t.setRequired(Utiles.esRequerido(requerido[i]));
                    t.setRequiredError(ERROR_R_DATE + titulos[i]);
                    t.setWidth(SIZEPIX_DATES, com.vaadin.terminal.Sizeable.UNITS_PIXELS);
                    t.setDateFormat(FORMATOS.get("DATE"));
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }


                    forma.addField(i, t);
                } else if (tipoDato[i].equalsIgnoreCase(TIPOD_STRING)) // crea campos para strings
                {
                    TextField t = new TextField(titulos[i]);  // titulo
                    t.setColumns(longitud[i]);   // tamaño
                    t.setMaxLength(longitud[i]);
                    t.setValue(Utiles.corrigeDatos(contenido));   // limpia blancos innecesarios
                    t.setRequired(Utiles.esRequerido(requerido[i]));
                    t.setRequiredError(ERROR_R_STRING + titulos[i]);
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }

                    t.addValidator(new StringLengthValidator("El campo " + titulos[i] + " debe tener entre 1 y " + longitud[i] + " de longitud", 1, longitud[i], false));
                    forma.addField(i, t);
                } else if (tipoDato[i].equalsIgnoreCase(TIPOD_INTEGER)) // crea campos para enteros
                {
                    TextField t = new TextField(titulos[i]);  // titulo
                    t.setColumns(longitud[i]);   // tamaño
                    t.setMaxLength(longitud[i]);
                    t.setValue(contenido);
                    t.setRequired(Utiles.esRequerido(requerido[i]));
                    t.setRequiredError(ERROR_R_STRING + titulos[i]);
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }

                    t.addValidator(new IntegerValidator("El campo " + titulos[i] + " debe ser numerico"));
                    forma.addField(i, t);
                } else if (tipoDato[i].equalsIgnoreCase(TIPOD_DOUBLE)) // crea campos para dobles
                {
                    TextField t = new TextField(titulos[i]);  // titulo
                    t.setColumns(longitud[i]);   // tamaño
                    t.setMaxLength(longitud[i]);
                    t.setValue(contenido);
                    t.setRequired(Utiles.esRequerido(requerido[i]));
                    t.setRequiredError(ERROR_R_STRING + titulos[i]);
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }

                    t.addValidator(new DoubleValidator("El campo " + titulos[i] + " debe ser numerico"));
                    forma.addField(i, t);
                }  else if (tipoDato[i].equalsIgnoreCase(TIPOD_DECIMAL)) // crea campos para dobles
                {
                    TextField t = new TextField(titulos[i]);  // titulo
                    t.setColumns(longitud[i]);   // tamaño
                    t.setMaxLength(longitud[i]);
                    t.setValue(contenido);
                    t.setRequired(Utiles.esRequerido(requerido[i]));
                    t.setRequiredError(ERROR_R_STRING + titulos[i]);
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }

                    t.addValidator(new DoubleValidator("El campo " + titulos[i] + " debe ser numerico"));
                    forma.addField(i, t);

                }
            } else if (tipoCampo[i] == TIPOC_LISTBOX) // aqui procesa los combos
               {
                Select t = new Select(titulos[i]);    // define combo
                t.setItemCaptionMode(Select.ITEM_CAPTION_MODE_EXPLICIT_DEFAULTS_ID);   // activa uso de id explicitos para el select
                t.setNullSelectionAllowed(false);
                t.setRequired(Utiles.esRequerido(requerido[i]));
                t.setRequiredError(ERROR_R_STRING + titulos[i]);

                if (((Integer) tiposCombosMaestro.get(i)) == TIPOCOMBO_SQL) {
                    //logger.debug("Tipo Combo SQL: "+i);
                    Vector infoSQL = (Vector) combosMaestro.get(i);
                    String tabla = (String) infoSQL.get(0);
                    String muestra = (String) infoSQL.get(1);
                    String almacena = (String) infoSQL.get(2);
                    String tipoAlmacena = (String) infoSQL.get(3);
                    String query = "SELECT " + muestra + "," + almacena + " FROM " + tabla + " ORDER BY " + muestra;
                    //logger.debug(query);
                    try {
                        ResultSet r1 = reportDAO.consultaBaseDatos(query);  	// efectua consulta de datos

                        while (r1.next()) {
                            if (tipoAlmacena.equalsIgnoreCase("1")) {
                                t.addItem(r1.getString(almacena));            //adicional el valor a almacenar si es tipo string
                                t.setItemCaption(r1.getString(almacena), r1.getString(muestra));   //adiciona el valor a mostrar
                            }
                            if (tipoAlmacena.equalsIgnoreCase("2")) {
                                t.addItem(r1.getInt(almacena));            //adicional el valor a almacenar si es tipo entero
                                t.setItemCaption(r1.getInt(almacena), r1.getString(muestra));   //adiciona el valor a mostrar
                            }
                        }

                    } catch (SQLException e) {
                        logger.debug(e.getMessage());
                    }
                    t.setValue(contenido);
                    t.setImmediate(true);
                    if (edicion) {
                        if (i < pk) {
                            t.setEnabled(false);
                        }
                    }
                    if (posicionLlave != -1 && !edicion && !maestro) {
                        t.setEnabled(false);
                    }

                    forma.addField(i, t);

                } else if (((Integer) tiposCombosMaestro.get(i)) == TIPOCOMBO_ARREGLO) {
                    Vector iMostrar = (Vector) combosMaestro.get(i);  // valores que se visualizan
                    Vector iAlmacenar = (Vector) valorCombosMaestro.get(i);
                    for (int j = 0; j < iMostrar.size(); j++) {
                        t.addItem((String) iAlmacenar.get(j));            //adicional el valor a almacenar
                        t.setItemCaption((String) iAlmacenar.get(j), (String) iMostrar.get(j));   //adiciona el valor a mostrar
                    }
                }
                t.setValue(String.valueOf(contenido));

                t.setImmediate(true);
                if (edicion) {
                    if (i < pk) {
                        t.setEnabled(false);
                    }
                }
                if (posicionLlave != -1 && !edicion && !maestro) {
                    t.setEnabled(false);
                }

                forma.addField(i, t);
            } else if (tipoCampo[i] == TIPOC_RADIOBUTTON) // aqui procesa los radiobuttons
            {

                OptionGroup t = new OptionGroup(titulos[i]);    // define combo
                t.setNullSelectionAllowed(false);
                t.setRequired(Utiles.esRequerido(requerido[i]));
                t.setRequiredError(ERROR_R_STRING + titulos[i]);

                Vector iMostrar = (Vector) radioMaestro.get(i);  // valores que se visualizan
                //Vector iAlmacenar = (Vector) valorCombosMaestro.get(i);
                for (int j = 0; j < iMostrar.size(); j++) {
                    t.addItem((String) iMostrar.get(j));            //adicional el valor a almacenar
                    t.setItemCaption((String) iMostrar.get(j), (String) iMostrar.get(j));   //adiciona el valor a mostrar
                }
                t.setValue(contenido);
                t.setImmediate(true);
                if (edicion) {
                    if (i < pk) {
                        t.setEnabled(false);
                    }
                }
                if (posicionLlave != -1 && !edicion && !maestro) {
                    t.setEnabled(false);
                }

                forma.addField(i, t);
            } else if (tipoCampo[i] == TIPOC_MULTILINE) // campos multi - linea
            {
                TextArea t = new TextArea(titulos[i]);  // titulo
                t.setColumns(longitud[i]);   // tamaño
                t.setMaxLength(longitud[i]);
                t.setRows(3);
                t.setValue(contenido);
                t.setRequired(Utiles.esRequerido(requerido[i]));
                t.setRequiredError(ERROR_R_STRING + titulos[i]);
                if (edicion) {
                    if (i < pk) {
                        t.setEnabled(false);
                    }
                }
                if (posicionLlave != -1 && !edicion && !maestro) {
                    t.setEnabled(false);
                }

                forma.addField(i, t);
            }
            i++;
        }

        forma.setImmediate(true);
        forma.setValidationVisible(true);



        return forma;
    }

    private int buscarCampoLLave(String campoActual, Object[] llaveDetalle) {
        int hallar = -1;
        for (int i = 0; i < llaveDetalle.length; i++) {
            if (campoActual.equals(llaveDetalle[i].toString())) {
                logger.debug("Hallo la llave del campo" + campoActual + " en la posicion " + i + " campo" + llaveDetalle[i].toString());
                hallar = i;
            }
        }
        return hallar;
    }
}
