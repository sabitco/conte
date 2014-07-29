/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.org.conte.sgm.servlet;

import co.org.conte.sgm.dao.SolicitudFormatoDao;
import co.org.conte.sgm.dao.exception.DaoException;
import co.org.conte.sgm.entity.Evaluacion;
import co.org.conte.sgm.entity.EvaluacionId;
import co.org.conte.sgm.entity.FormatoEstudio;
import co.org.conte.sgm.entity.Solicitud;
import co.org.conte.sgm.entity.SolicitudFormato;
import co.org.conte.sgm.entity.SolicitudFormatoId;
import co.org.conte.sgm.service.EvaluacionService;
import co.org.conte.sgm.service.SolicitudService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author J4M0
 */
public class GuardarEvaluacion extends HttpServlet {
    
    private SolicitudService serviceSolicitud = new SolicitudService();

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            String v = "<br>";
            long idSolicitud = Long.parseLong(request.getParameter("solicitud"));            
            guardarEvaluacion(request.getParameterValues("actividad[]"), idSolicitud, Integer.parseInt(request.getParameter("formato")));
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" href=\"VAADIN/themes/conte/estilos.css\"/>	");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Se ha guardado correctamente la evaluacion</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    
    private void guardarEvaluacion(String[] actividadItems, long idSolicitud, int idFormato){
        String documento = "";
        Solicitud solicitud = serviceSolicitud.findById(idSolicitud);            
        if(solicitud!=null){
            documento = solicitud.getTecnico().getDocumento();
        }
        List<Evaluacion> evaluaciones = new ArrayList<Evaluacion>();
        Evaluacion evaluacion;
        EvaluacionId id;
        for(String s : actividadItems){
            evaluacion = new Evaluacion();
            String[] split = s.split("-");
            id = new EvaluacionId();
            id.setActivityId(Integer.parseInt(split[0]));
            id.setItemId(Integer.parseInt(split[1]));
            id.setSolicitudId(idSolicitud);
            evaluacion.setId(id);
            evaluacion.setDocumento(documento);
            evaluacion.setFecha(new Date());
            evaluaciones.add(evaluacion);
        }     
        EvaluacionService service = new EvaluacionService();
        service.save(evaluaciones);
        try {            
            SolicitudFormato solicitudFormato = new SolicitudFormato();            
            SolicitudFormatoId solicitudFormatoId = new SolicitudFormatoId();
            solicitudFormatoId.setCFormato(idFormato);
            solicitudFormatoId.setCSolicitud(idSolicitud);
            solicitudFormato.setId(solicitudFormatoId);
            solicitudFormato.setFechaEvaluacion(new Date());
            solicitudFormato.setFormatoEstudio(new FormatoEstudio(idFormato));
            solicitudFormato.setSolicitud(solicitud);
            solicitudFormato.setUsuario(null);            
            SolicitudFormatoDao daoSolicitudFormato = new SolicitudFormatoDao();
            if(solicitudFormato !=null){
                daoSolicitudFormato.insert(solicitudFormato);
            }                       
        } catch (DaoException ex) {
            Logger.getLogger(GuardarEvaluacion.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}