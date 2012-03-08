/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.servlet;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.inftel.scrum.control.LoginBean;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.util.JSONConverter;

/**
 *
 * @author Jorge
 */
@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
public class Dispatcher extends HttpServlet {
    
    private final static Logger LOGGER = Logger.getLogger(Dispatcher.class.getName());
    
    // ACTIONS CONSTANTS
    private static final int ACTION_LOGIN = 0;
    
    // RESPONSES CONSTANTS
    private static final String ERROR_UNKNOWN_ACTION = "UNKNOWN_ACTION";
    private static final String ERROR_LOGIN = "ERROR_LOGIN";

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
        
        
        // Send to client
        ServletOutputStream out = response.getOutputStream();
        DataOutputStream dos = new DataOutputStream(out);
        
        // Receive from client
        ServletInputStream in = request.getInputStream();
        DataInputStream dis = new DataInputStream(in);
        
        int action = dis.readInt();
        
        String result = null;
        
        switch(action) {
            case ACTION_LOGIN:
                
                String sEmail = dis.readUTF();
                String sPassword = dis.readUTF();
                
                LoginBean loginBean = new LoginBean();
                loginBean.initEJB();
                loginBean.setEmail(sEmail);
                loginBean.setPassword(sPassword);
                
                if (loginBean.doLogin() != null) {
                    List<Project> projectList = (List<Project>) loginBean.getActiveProjects();
                    String sessionId = request.getSession(true).getId();
                    
                    result = JSONConverter.buildJSONProjectList(sessionId, projectList);
                }
                else {
                    result = ERROR_LOGIN;
                }
                
                break;
            default:
                result = ERROR_UNKNOWN_ACTION;
                break;
        }
        
        dos.writeUTF(result);
        
        dis.close();
        dos.close();
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
