/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.servlet;

import java.io.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.control.*;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
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
    private static final int ACTION_REGISTER = 1;
    private static final int ACTION_REQUEST_LIST_SPRINTS = 2;
    private static final int ACTION_REQUEST_LIST_TASKS = 3;
    private static final int ACTION_REQUEST_LIST_USERS = 4;
    private static final int ACTION_ADD_PROJECT = 5;
    
    // RESPONSES CONSTANTS
    private static final String ERROR_ADD_PROJECT = "ERROR_ADD_PROJECT";
    private static final String ERROR_UNKNOWN_ACTION = "UNKNOWN_ACTION";
    private static final String ERROR_LOGIN = "ERROR_LOGIN";
    private static final String ERROR_REGISTER = "ERROR_REGISTER";
    private static final String REGISTER_OK = "REGISTER_OK";
    private static final String SESSION_EXPIRED = "SESSION_EXPIRED";

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
        
        String result;
        
        switch(action) {
            case ACTION_LOGIN:
                
                result = actionLogin(dis, request);
                break;
            case ACTION_REGISTER:
                
                result = actionRegister(dis);
                break;
            case ACTION_REQUEST_LIST_SPRINTS:
                
                result = actionRequestSprintList(dis, request);
                break;
            case ACTION_REQUEST_LIST_TASKS:
                
                result = actionRequestTaskList(dis, request);
                break;
            case ACTION_ADD_PROJECT:
                
                result = actionAddProject(dis, request);
                break;
            case ACTION_REQUEST_LIST_USERS:
                
                result = actionRequestUserList(dis, request);
                break;
            default:
                
                result = ERROR_UNKNOWN_ACTION;
                break;
        }
        
        dos.writeUTF(result);
        
        dis.close();
        dos.close();
    }
    //<editor-fold defaultstate="collapsed" desc="Action methods">
    private String actionLogin(DataInputStream dis, HttpServletRequest request) throws IOException {
        
        String sEmail = dis.readUTF();
        String sPassword = dis.readUTF();
        
        LoginBean loginBean = new LoginBean();
        loginBean.initEJB();
        loginBean.setEmail(sEmail);
        loginBean.setPassword(sPassword);

        if (loginBean.doLogin() != null) {
            HttpSession session = request.getSession(true);

            List<Project> projectList = (List<Project>) loginBean.getActiveProjects();
            ProjectListBean projectListBean = new ProjectListBean();
            projectListBean.setActiveProjects(projectList);
            
            session.setAttribute("loginBean", loginBean);
            session.setAttribute("projectListBean", projectListBean);

            String sessionId = session.getId();

            return JSONConverter.buildJSONProjectList(sessionId, projectList);
        }
        else {
            return ERROR_LOGIN;
        }
    }
    
    private String actionRegister(DataInputStream dis) throws IOException {
        
        String sName = dis.readUTF();
        String sSurname = dis.readUTF();
        String sEmail = dis.readUTF();
        String sPassword = dis.readUTF();

        RegisterBean registerBean = new RegisterBean();
        registerBean.initEJB();
        registerBean.setName(sName);
        registerBean.setSurname(sSurname);
        registerBean.setEmail(sEmail);
        registerBean.setPassword(sPassword);

        if (registerBean.doRegister() != null)
            return REGISTER_OK;
        else
            return ERROR_REGISTER;
    }
    
    private String actionRequestSprintList(DataInputStream dis, HttpServletRequest request) 
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idProject = Integer.valueOf(dis.readUTF());

            SelectedProjectBean selectedProjectBean =  new SelectedProjectBean();
            selectedProjectBean.initEJB();
            ProjectListBean projectListBean = (ProjectListBean) session.getAttribute("projectListBean");
            
            for (Project p : projectListBean.getActiveProjects()) {
                if (p.getIdProject() == idProject) {
                    selectedProjectBean.setIdProject(p.getIdProject());
                    selectedProjectBean.setName(p.getName());
                    selectedProjectBean.setDescription(p.getDescription());
                    selectedProjectBean.setInitialDate(p.getInitialDate());
                    selectedProjectBean.setEndDate(p.getEndDate());
                    selectedProjectBean.setOwner(p.getScrumMaster());
                    
                    selectedProjectBean.select();
                }
            }
            
            session.setAttribute("selectedProjectBean", selectedProjectBean);
            result = JSONConverter.buildJSONSprintList((List<Sprint>)selectedProjectBean.getSprints());
        }
        
        return result;
    }
    
    private String actionRequestTaskList(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idSprint = Integer.valueOf(dis.readUTF());

            SelectedSprintBean selectedSprintBean = new SelectedSprintBean();
            selectedSprintBean.initEJB();
        
            SelectedProjectBean selectedProjectBean = 
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            
            Collection<Sprint> sprintList = selectedProjectBean.getSprints();
            for (Sprint s : sprintList) {
                if (s.getIdSprint() == idSprint) {
                    
                    selectedSprintBean.setIdSprint(s.getIdSprint());
                    selectedSprintBean.setSprintNumber(s.getSprintNumber());
                    selectedSprintBean.setInitialDate(s.getInitialDate());
                    selectedSprintBean.setEndDate(s.getEndDate());
                    
                    selectedSprintBean.selectSprint();
                }
            }
            
            session.setAttribute("selectedSprintBean", selectedSprintBean);
            result = JSONConverter.buildJSONTaskList((List<Task>)selectedSprintBean.getTaskList());
        }
        
        return result;
    }
    
    private String actionAddProject(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            Calendar calendarInitial = new GregorianCalendar();
            Calendar calendarEnd = new GregorianCalendar();

            String sName = dis.readUTF();
            String sDescription = dis.readUTF();
            String sInitialDay = dis.readUTF();
            String sInitialMonth = dis.readUTF();
            String sInitialYear = dis.readUTF();
            String sEndDay = dis.readUTF();
            String sEndMonth = dis.readUTF();
            String sEndYear = dis.readUTF();

            int iInitialDay = Integer.parseInt(sInitialDay);
            int iInitialMonth = Integer.parseInt(sInitialMonth);
            int iInitialYear = Integer.parseInt(sInitialYear);

            int iEndDay = Integer.parseInt(sEndDay);
            int iEndMonth = Integer.parseInt(sEndMonth);
            int iEndYear = Integer.parseInt(sEndYear);

            calendarInitial.set(iInitialYear, iInitialMonth, iInitialDay);
            calendarEnd.set(iEndYear, iEndMonth, iEndDay);

            ProjectBean projectBean = new ProjectBean();
            projectBean.initEJB();
            projectBean.setName(sName);
            projectBean.setDescription(sDescription);
            projectBean.setInitialDate(calendarInitial.getTime());
            projectBean.setEndDate(calendarEnd.getTime());
            
            LoginBean loginBean = (LoginBean) session.getAttribute("loginBean");
            
            Project p = projectBean.persistProject(loginBean.getUser().getIdUser());
            if (p != null) {
                ProjectListBean projectListBean = 
                        (ProjectListBean) session.getAttribute("projectListBean");
                projectListBean.addActiveProject(p);
                
                SelectedProjectBean selectedProjectBean = new SelectedProjectBean();
                selectedProjectBean.setIdProject(p.getIdProject());
                selectedProjectBean.setName(p.getName());
                selectedProjectBean.setDescription(p.getDescription());
                selectedProjectBean.setInitialDate(p.getInitialDate());
                selectedProjectBean.setEndDate(p.getEndDate());
                selectedProjectBean.setOwner(p.getScrumMaster());
                
                session.setAttribute("selectedProjectBean", selectedProjectBean);
                
                result = JSONConverter.buildJSONProjectList(
                            session.getId(),
                            (List<Project>) projectListBean.getActiveProjects());
            }
            else {
                result = ERROR_ADD_PROJECT;
            }
        }

        return result;
    }
    
    private String actionRequestUserList(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idProject = Integer.valueOf(dis.readUTF());

            SelectedProjectBean selectedProjectBean =  new SelectedProjectBean();
            selectedProjectBean.initEJB();
            ProjectListBean projectListBean = (ProjectListBean) session.getAttribute("projectListBean");
            
            for (Project p : projectListBean.getActiveProjects()) {
                if (p.getIdProject() == idProject) {
                    selectedProjectBean.setIdProject(p.getIdProject());
                    selectedProjectBean.setName(p.getName());
                    selectedProjectBean.setDescription(p.getDescription());
                    selectedProjectBean.setInitialDate(p.getInitialDate());
                    selectedProjectBean.setEndDate(p.getEndDate());
                    selectedProjectBean.setOwner(p.getScrumMaster());
                    
                    selectedProjectBean.select();
                }
            }
            
            session.setAttribute("selectedProjectBean", selectedProjectBean);
            
            MyUserListBean myUserListBean = new MyUserListBean();
            myUserListBean.init();
            
            result = JSONConverter.buildJSONUserList(myUserListBean.getUsersInProject(idProject));
        }
        
        return result;
    }
    //</editor-fold>

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
