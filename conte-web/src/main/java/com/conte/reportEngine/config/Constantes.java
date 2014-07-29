package com.conte.reportEngine.config;

import com.vaadin.terminal.ThemeResource;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Clase de Constantes Genericas para Infoval
 */
public final class Constantes {
	public static final String NOMBRE_APLICACION = "InfoValmer";

	// Iconos para mantenedores
	public static final ThemeResource ICONO_CONSULTAR =  new ThemeResource("../infovalmer/icons/consultar.png");
	public static final ThemeResource ICONO_PRIMERO =  new ThemeResource("../infovalmer/icons/primero.png");
	public static final ThemeResource ICONO_ANTERIOR =  new ThemeResource("../infovalmer/icons/anterior.png");
	public static final ThemeResource ICONO_SIGUIENTE =  new ThemeResource("../infovalmer/icons/siguiente.png");
	public static final ThemeResource ICONO_ULTIMO =  new ThemeResource("../infovalmer/icons/ultimo.png");
	public static final ThemeResource ICONO_AGREGAR =  new ThemeResource("../infovalmer/icons/agregar.png");
	public static final ThemeResource ICONO_EDITAR =  new ThemeResource("../infovalmer/icons/editar.png");
	public static final ThemeResource ICONO_ELIMINAR =  new ThemeResource("../infovalmer/icons/eliminar.png");
	public static final ThemeResource ICONO_IMPRIMIR =  new ThemeResource("../infovalmer/icons/imprimir.png");
	public static final ThemeResource ICONO_DESHACER =  new ThemeResource("../infovalmer/icons/deshacer.png");
	public static final ThemeResource ICONO_GUARDAR =  new ThemeResource("../infovalmer/icons/guardar.png");
	public static final ThemeResource ICONO_IR =  new ThemeResource("../infovalmer/icons/ir.png");
	// Tooltips para botones de mantenedores - maestro

	public static final String TOOLTIP_PRIMERO =  "Primero";
	public static final String TOOLTIP_ANTERIOR =  "Anterior";
	public static final String TOOLTIP_SIGUIENTE =  "Siguiente";
	public static final String TOOLTIP_ULTIMO =  "�ltimo";
	public static final String TOOLTIP_AGREGAR =  "Agregar";

	public static final String TOOLTIP_CONSULTAR =  "Consultar";
	public static final String TOOLTIP_EDITAR =  "Editar";
	public static final String TOOLTIP_ELIMINAR =  "Eliminar";
	public static final String TOOLTIP_IMPRIMIR =  "Imprimir";
	public static final String TOOLTIP_DESHACER =  "Deshacer";
	public static final String TOOLTIP_GUARDAR =  "Guardar";

	public static final String TOOLTIP_IR =  "Ir a Registro";
	public static final String TOOLTIP_RACTUAL =  "Registro Actual";
	public static final String TOOLTIP_RACTUALD =  "Registro Detalle Actual";

	// Tooltips para botones de mantenedores - detalle
	public static final String TOOLTIP_CONSULTARD =  "Consultar Detalle";
	public static final String TOOLTIP_EDITARD =     "Editar Detalle";
	public static final String TOOLTIP_ELIMINARD =  "Eliminar Detalle";
	public static final String TOOLTIP_IMPRIMIRD =  "Imprimir Detalle";
	public static final String TOOLTIP_DESHACERD =  "Deshacer Detalle";
	public static final String TOOLTIP_GUARDARD =  "Guardar Detalle";
	public static final String TOOLTIP_AGREGARD =  "Agregar Detalle";

	// tipos de datos para formas
	public static final int TIPOC_DATAFIELD = 1;
	public static final int TIPOC_CHECKBOX = 2;
	public static final int TIPOC_RADIOBUTTON = 3;
	public static final int TIPOC_LISTBOX = 4;
	public static final int TIPOC_MULTILINE = 5;

	public static final String TIPOD_STRING = "String";
	public static final String TIPOD_DATE = "Date";
	public static final String TIPOD_INTEGER = "Integer";
	public static final String TIPOD_DOUBLE = "Double";
	public static final String TIPOD_DECIMAL= "Decimal";

	// Mensajes de error para validaciones
	public static final String ERROR_R_STRING = "Campo requerido: ";
	public static final String ERROR_R_DATE = "Fecha requerida :";
	public static final String ERROR_R_DOUBLE = "Valor requerido :";
	public static final String ERROR_R_INT = "Valor requerido :";

	// Tipos de combos
	public static final int TIPOCOMBO_SQL = 1;
	public static final int TIPOCOMBO_ARREGLO = 2;

	// Tipos de alineacion
	public static enum Alineacion {
	    IZQUIERDA, CENTRO, DERECHA, NINGUNA
	}
    // Formatos para fechas y numeros por defectos (se aplican cuando no se envian como parametros de columna/campo)

  public static final HashMap <String,String > FORMATOS  = new HashMap <String,String> () {{
		put("DATE",      "yyyy-MM-dd");
		put("TIME",      "HH:mm:ss");
		put("TIMESTAMP", "yyyy-MM-dd HH:mm:ss");
		put("DOUBLE",    "###########.##");
		put("FLOAT",     "###########.##");
		put("INTEGER",   "###########");
                put("DECIMAL",   "###########.##");
                
                put("DOUBLE0",    "###########");
                put("DOUBLE1",    "###########.#");
                put("DOUBLE2",    "###########.##");
                put("DOUBLE3",    "###########.###");
                put("DOUBLE4",    "###########.####");
                put("DOUBLE5",    "###########.#####");
                put("DOUBLE6",    "###########.######");
                put("DOUBLE7",    "###########.#######");
                put("DOUBLE8",    "###########.########");
                put("DOUBLE9",    "###########.#########");
                put("FLOAT0",    "###########");
                put("FLOAT1",    "###########.#");
                put("FLOAT2",    "###########.##");
                put("FLOAT3",    "###########.###");
                put("FLOAT4",    "###########.####");
                put("FLOAT5",    "###########.#####");
                put("FLOAT6",    "###########.######");
                put("FLOAT7",    "###########.#######");
                put("FLOAT8",    "###########.########");
                put("FLOAT9",    "###########.#########");

                put("DECIMAL0",    "###########");
                put("DECIMAL1",    "###########.#");
                put("DECIMAL2",    "###########.##");
                put("DECIMAL3",    "###########.###");
                put("DECIMAL4",    "###########.####");
                put("DECIMAL5",    "###########.#####");
                put("DECIMAL6",    "###########.######");
                put("DECIMAL7",    "###########.#######");
                put("DECIMAL8",    "###########.########");
                put("DECIMAL9",    "###########.#########");
                
	}};

	public static final HashMap <String,Format > FORMATEADORES  = new HashMap <String,Format> () {{
		put("DATE",      new SimpleDateFormat(FORMATOS.get("DATE")));
		put("TIME",      new SimpleDateFormat(FORMATOS.get("TIME")));
		put("TIMESTAMP", new SimpleDateFormat(FORMATOS.get("TIMESTAMP")));
		put("DOUBLE",    new DecimalFormat(FORMATOS.get("DOUBLE")));
		put("FLOAT",     new DecimalFormat(FORMATOS.get("FLOAT")));
		put("INTEGER",   new DecimalFormat(FORMATOS.get("INTEGER")));
		put("INT",   new DecimalFormat(FORMATOS.get("INTEGER")));
                put("DECIMAL",   new DecimalFormat(FORMATOS.get("DECIMAL")));
		put("DOUBLE0",    new DecimalFormat(FORMATOS.get("DOUBLE0")));
		put("DOUBLE1",    new DecimalFormat(FORMATOS.get("DOUBLE1")));
		put("DOUBLE2",    new DecimalFormat(FORMATOS.get("DOUBLE2")));
		put("DOUBLE3",    new DecimalFormat(FORMATOS.get("DOUBLE3")));
		put("DOUBLE4",    new DecimalFormat(FORMATOS.get("DOUBLE4")));
		put("DOUBLE5",    new DecimalFormat(FORMATOS.get("DOUBLE5")));
		put("DOUBLE6",    new DecimalFormat(FORMATOS.get("DOUBLE6")));
		put("DOUBLE7",    new DecimalFormat(FORMATOS.get("DOUBLE7")));
		put("DOUBLE8",    new DecimalFormat(FORMATOS.get("DOUBLE8")));
		put("DOUBLE9",    new DecimalFormat(FORMATOS.get("DOUBLE9")));                

                put("FLOAT0",    new DecimalFormat(FORMATOS.get("FLOAT0")));
		put("FLOAT1",    new DecimalFormat(FORMATOS.get("FLOAT1")));
		put("FLOAT2",    new DecimalFormat(FORMATOS.get("FLOAT2")));
		put("FLOAT3",    new DecimalFormat(FORMATOS.get("FLOAT3")));
		put("FLOAT4",    new DecimalFormat(FORMATOS.get("FLOAT4")));
		put("FLOAT5",    new DecimalFormat(FORMATOS.get("FLOAT5")));
		put("FLOAT6",    new DecimalFormat(FORMATOS.get("FLOAT6")));
		put("FLOAT7",    new DecimalFormat(FORMATOS.get("FLOAT7")));
		put("FLOAT8",    new DecimalFormat(FORMATOS.get("FLOAT8")));
		put("FLOAT9",    new DecimalFormat(FORMATOS.get("FLOAT9")));                
                
                put("DECIMAL0",    new DecimalFormat(FORMATOS.get("DECIMAL0")));
		put("DECIMAL1",    new DecimalFormat(FORMATOS.get("DECIMAL1")));
		put("DECIMAL2",    new DecimalFormat(FORMATOS.get("DECIMAL2")));
		put("DECIMAL3",    new DecimalFormat(FORMATOS.get("DECIMAL3")));
		put("DECIMAL4",    new DecimalFormat(FORMATOS.get("DECIMAL4")));
		put("DECIMAL5",    new DecimalFormat(FORMATOS.get("DECIMAL5")));
		put("DECIMAL6",    new DecimalFormat(FORMATOS.get("DECIMAL6")));
		put("DECIMAL7",    new DecimalFormat(FORMATOS.get("DECIMAL7")));
		put("DECIMAL8",    new DecimalFormat(FORMATOS.get("DECIMAL8")));
		put("DECIMAL9",    new DecimalFormat(FORMATOS.get("DECIMAL9")));                
                
                
	}};

	public static final HashMap <String,Constantes.Alineacion > ALINEACIONES  = new HashMap <String,Constantes.Alineacion> () {{
		put("DATE",      Constantes.Alineacion.IZQUIERDA);
		put("TIME",      Constantes.Alineacion.IZQUIERDA);
		put("TIMESTAMP", Constantes.Alineacion.IZQUIERDA);
		put("DOUBLE",    Constantes.Alineacion.DERECHA);
		put("FLOAT",     Constantes.Alineacion.DERECHA);
		put("INT",   Constantes.Alineacion.DERECHA);
		put("DECIMAL",   Constantes.Alineacion.DERECHA);
	}};

	// Tama�os de campos globales
	public static final int SIZEPIX_DATES = 100;
	public static final int SIZEGRILLA_MAESTRO = 12;
	public static final int SIZEGRILLA_DETALLE = 8;
	public static final int SIZEGRILLA_CONSULTAS =  12;
	public static final int NUMEROCAMPOS_GRILLA =  4;
	public static final int NUMEROCAMPOS_GRILLADETALLE =  10;
	public static final int COL_ZOOM =  10;
        public static final int COL_MAX =  15;


	//Mensajes para confimracion (Dialog yes/no)

	public static final String MSGCONFIRMA_TITULO = "Por favor confirmar ..";
	public static final String MSGELIMINAR_PREGUNTA= "Desea eliminar el registro actual: ";
	public static final String MSGDESHACER_PREGUNTA= "Desea deshacer los cambios al registro actual: ";
	public static final String MSGEXITO_ELIMINACION= "Registro eliminado ";
	public static final String MSGEXITO_DESHACER= "Cambios en Registro han sido Descartados  ";
	public static final String MSGEXITO_ACTUALIZACION= "Cambios en Registro han sido Actualizados  ";
	public static final String MSGEXITO_CREACION= "Nuevo Registro ha sido creado  ";
	public static final String MSGERROR_ELIMINACION= "Error al eliminar el registro - contacte a su administrador ";
	public static final String MSGERROR_ELIMINACIONVACIO= "No hay registros para eliminar ";
	public static final String MSGERROR_EDICIONVACIO= "No hay registros para editar ";
	public static final String MSGERROR_ACTUALIZACION= "Error al actualizar el registro - contacte a su administrador ";
	public static final String MSGERROR_CREACION= "Error al crear el registro - contacte a su administrador ";
        public static final String MSGERROR_CREACION_PK= "Registro ya existe - para llave: ";
	public static final String MSGERROR_VALIDACION_MAESTRO= "Validaci�n de registro maestro no fu� existosa ";
	public static final String MSGERROR_VALIDACION_DETALLE= "Validaci�n de registro detalle no fu� existosa ";
}