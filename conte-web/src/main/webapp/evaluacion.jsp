<%-- 
    Document   : evaluacion
    Created on : 21/02/2013, 03:36:52 PM
    Author     : J4M0
--%>

<%@page import="co.org.conte.sgm.entity.Evaluacion"%>
<%@page import="co.org.conte.sgm.dao.EvaluacionDao"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="co.org.conte.sgm.service.FormatoEstudioService"%>
<%@page import="co.org.conte.sgm.entity.FormatoEstudio"%>
<%@page import="co.org.conte.sgm.entity.Item"%>
<%@page import="co.org.conte.sgm.entity.FormatoActividad"%>
<%@page import="co.org.conte.sgm.service.FormatoService"%>
<%@page import="co.org.conte.sgm.entity.ActividadGenerica"%>
<%@page import="co.org.conte.sgm.entity.ClaseGenerica"%>
<%@page import="java.util.List"%>
<%@page import="co.org.conte.sgm.service.ClaseGenericaService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
ClaseGenericaService serviceClaseGenerica = new ClaseGenericaService();
FormatoService servicefFormato = new FormatoService();
FormatoEstudioService serviceFormatoEstudio = new FormatoEstudioService();

List<ClaseGenerica> clasesGenericas = serviceClaseGenerica.findAll();
String[] valores = request.getParameter("solicitud").split("-");
FormatoEstudio formatoEstudio = serviceFormatoEstudio.findById(Integer.parseInt(valores[1])) ;
if(formatoEstudio == null){
    formatoEstudio = new FormatoEstudio(Integer.parseInt(valores[1]));
}
List<FormatoActividad> formatoActividades = (servicefFormato.getFormatoActividadByFormatoEstudio(formatoEstudio));
EvaluacionDao daoEvaluacion = new EvaluacionDao();
List<Evaluacion> evaluaciones = daoEvaluacion.findByDocument(valores[2]);
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title></title>
        <meta charset="utf-8"/>
        <meta name="description" content="Bienvenidos a Quasar Software"/>
        <meta name="viewport" content="width=device-width,initial-scale=1"/>
        <link rel="shortcut icon" type="image/x-icon" href="favicon.ico"/>
        <link rel="stylesheet" href="VAADIN/themes/conte/estilos.css"/>		
        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js">>/script> 
        <![endif]-->
    </head>
    <body>
        <div id="">
            <form action="guardarEvaluacion" method="post">
                <%
                for(ClaseGenerica cg : clasesGenericas){
                    //
                %>                
                <fieldset>
                    <legend>
                        <%out.print(cg.getNombre());%>
                    </legend>
                    <table border="1">  
                <%
                    boolean b = false;
                    boolean marcar = false;
                    for(ActividadGenerica ag : cg.getActividadGenericas()){                                                
                        out.print("<tr><td>"+ag.getDescripcion()+"</td>");
                        for(Item i : ag.getItems()){
                            for(FormatoActividad fa : formatoActividades){
                                if(fa.getId().getActivityId() == ag.getCodigo()){
                                    if(fa.getId().getItemId() == i.getId()){
                                        marcar = true;
                                        break;
                                    }
                                }   
                            }
                            
                            if(evaluaciones!=null && !marcar){
                                for(Evaluacion e : evaluaciones){
                                    if(e.getActivity().getCodigo() == ag.getCodigo()){
                                        if(e.getItem().getId() == i.getId()){
                                            marcar = true;
                                            break;
                                        }
                                    }
                                }                                                                                   
                            }
                            
                            if(marcar){
                                out.print("<td>"+i.getName()+ "<input type=\"checkbox\" name=\"actividad[]\" value=\""+ag.getCodigo() 
                                                + "-"+ i.getId() +"\" checked onclick=\"javascript: return false;\"/></td>");                                                                                                        
                            } else {
                                out.print("<td>"+i.getName()+ "<input type=\"checkbox\" name=\"actividad[]\" value=\""+ag.getCodigo() 
                                        + "-"+ i.getId() +"\" /></td>");
                            }
                            marcar = false;                            
                        }
                        out.print("</tr>");                         
                    }
                %>
                    </table>
                </fieldset>
                <%}%>
                <input type="hidden" name="solicitud" value="<%out.print(valores[0]);%>"/>
                <input type="hidden" name="formato" value="<%out.print(valores[1]);%>"/>
                <input type="submit"/>
            </form>
        </div><!--container-->        
    </body>
</html>