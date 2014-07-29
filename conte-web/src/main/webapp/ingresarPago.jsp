<%@page import="co.org.conte.sgm.dao.exception.DaoException"%>
<%@page import="co.org.conte.sgm.dao.PagoDao"%>
<%
String valor = request.getParameter("valor");
String anio = request.getParameter("anio");
String mes = request.getParameter("mes");
String dia = request.getParameter("dia");
String documento = request.getParameter("documento");
String solicitud = request.getParameter("valor");
boolean ingresar = true;
if(valor!=null && anio!=null && mes!=null && dia!=null){
    try{
        Integer.parseInt(valor);
    } catch (NumberFormatException nfe){
        ingresar = false;
        out.print(valor + " No es un valor valido para Valor<br/>");
    }
    try{
        Integer.parseInt(anio);
        if(anio.length()<4 || anio.length()>5){
            out.print(anio + " No es un valor valido para Año<br/>");            
            ingresar = false;
        }        
    } catch (NumberFormatException nfe){
        ingresar = false;
        out.print(anio + " No es un valor valido para Año<br/>");
    }
    try{
        Integer.parseInt(mes);
        if(mes.length()<2){
            mes = "0" + mes;            
        }        
    } catch (NumberFormatException nfe){
        ingresar = false;
        out.print(mes + " No es un valor valido para Mes<br/>");
    }
    try{
        Integer.parseInt(dia);
        if(dia.length()<2){
            dia = "0" + dia;            
        }        
    } catch (NumberFormatException nfe){
        out.print(dia + " No es un valor valido para Día<br/>");
        ingresar = false;
    }    
    if(ingresar){
        PagoDao dao= new PagoDao();
        try{
            if(dao.save(solicitud,anio+mes+dia,valor,documento)>0){
                out.print("Se ha ingresado el pago correctamente<br/>");                       
            } else {
                out.print("Ha ocurrido un error al ingresar el pago<br/>");                       
            }
        } catch(DaoException de){
            out.print("Ha ocurrido un error al ingresar el pago<br/>");                                              
        }
    }
} else {
    out.print("Diligencie los campos");
}
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Igresar Pago</title>
    </head>
    <body>        
        <form method="post" action="ingresarPago">
            <table>
                <tr>
                    <td><label for="valor">Valor:</label></td>
                    <td><input type="number" name="valor" placeholder="Valor" id="valor" required/></td>
                </tr>
                <tr>
                    <td><label for="valor">Fecha Consignacion:</label></td>
                    <td><input type="number" name="anio" placeholder="Año" maxlength="4" required/><br/></td>
                    <td><input type="number" name="mes" placeholder="Mes" maxlength="2" required/><br/></td>
                    <td><input type="number" name="dia" placeholder="Día" maxlength="2" required /><br/></td>
                </tr>
                <tr>
                    <td>Entidad</td>
                    <td>
                        <select>
                            <option>Bancolombia</option>
                            <option>Caja Social</option>                
                        </select>   
                    </td>
                </tr>
                <tr>
                    <td><input type="hidden" name="solicitud" value="2"/></td>
                    <td><input type="hidden" name="documento" value="2"/></td>
                    <td><input type="submit" name="fecha" value="Ingresar"/></td>            
                </tr>
            </table>            
        </form>
    </body>
</html>