/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.servlet;

import java.io.*;
import java.util.*;
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
import org.inftel.scrum.entity.User;
import org.inftel.scrum.util.JSONConverter;

/**
 *
 * @author Jorge
 */
@WebServlet(name = "Dispatcher", urlPatterns = {"/Dispatcher"})
public class Dispatcher extends HttpServlet {
    
    private final static Logger LOGGER = Logger.getLogger(Dispatcher.class.getName());
    
    // ACTIONS CONSTANTS
    private static final int ACTION_LOGIN =                     0;
    private static final int ACTION_REGISTER =                  1;
    private static final int ACTION_REQUEST_LIST_SPRINTS =      2;
    private static final int ACTION_REQUEST_LIST_TASKS =        3;
    private static final int ACTION_REQUEST_LIST_USERS =        4;
    private static final int ACTION_ADD_PROJECT =               5;
    private static final int ACTION_ADD_SPRINT =                6;
    private static final int ACTION_ADD_TASK =                  7;
    private static final int ACTION_DELETE_PROJECT =            8;
    private static final int ACTION_DELETE_SPRINT =             9;
    private static final int ACTION_DELETE_TASK =               10;
    private static final int ACTION_EDIT_TASK_ASK =             11;
    private static final int ACTION_REQUEST_CHART =             12;
    private static final int ACTION_EDIT_PROJECT     =          13;
    private static final int ACTION_EDIT_USER_PROJECT_ASK =     14;
    private static final int ACTION_EDIT_USER_PROJECT_SEND =    15;
    private static final int ACTION_EDIT_TASK_SEND =            16;
    private static final int ACTION_CLOSE_SESSION =             17;
    
    
    // RESPONSES CONSTANTS
    private static final String ERROR_ADD_PROJECT =         "ERROR_ADD_PROJECT";
    private static final String ERROR_ADD_SPRINT =          "ERROR_ADD_SPRINT";
    private static final String ERROR_ADD_TASK =            "ERROR_ADD_TASK";
    private static final String ERROR_DELETE_PROJECT =      "ERROR_DELETE_PROJECT";
    private static final String ERROR_DELETE_SPRINT =       "ERROR_DELETE_SPRINT";
    private static final String ERROR_DELETE_TASK =         "ERROR_DELETE_TASK";
    private static final String ERROR_EDIT_PROJECT =        "ERROR_EDIT_PROJECT";
    private static final String ERROR_EDIT_TASK =           "ERROR_EDIT_TASK";
    private static final String ERROR_LOGIN =               "ERROR_LOGIN";
    private static final String ERROR_REGISTER =            "ERROR_REGISTER";
    private static final String ERROR_UNKNOWN_ACTION =      "UNKNOWN_ACTION";
    private static final String REGISTER_OK =               "REGISTER_OK";
    private static final String SESSION_EXPIRED =           "SESSION_EXPIRED";
    private static final String SESSION_CLOSED =            "SESSION_CLOSED";

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
            case ACTION_REQUEST_LIST_USERS:
                
                result = actionRequestUserList(dis, request);
                break;
            case ACTION_ADD_PROJECT:
                
                result = actionAddProject(dis, request);
                break;
            case ACTION_ADD_SPRINT:
                
                result = actionAddSprint(dis, request);
                break;
            case ACTION_ADD_TASK:
                
                result = actionAddTask(dis, request);
                break;
            case ACTION_DELETE_PROJECT:
                
                result = actionDeleteProject(dis, request);
                break;
            case ACTION_DELETE_SPRINT:
                
                result = actionDeleteSprint(dis, request);
                break;
            case ACTION_DELETE_TASK:
                
                result = actionDeleteTask(dis, request);
                break;
            case ACTION_EDIT_TASK_ASK:
                
                result = actionEditTaskAsk(dis, request);
                break;
            case ACTION_EDIT_PROJECT:
                
                result = actionEditProject(dis, request);
                break;
            case ACTION_EDIT_USER_PROJECT_ASK:
                
                result = actionEditUserProjectAsk(dis, request);
                break;
            case ACTION_EDIT_USER_PROJECT_SEND:
                
                result = actionEditUserProjectSend(dis, request);
                break;
            case ACTION_EDIT_TASK_SEND:
                
                result = actionEditTaskSend(dis, request);
                break;
            case ACTION_REQUEST_CHART:
                
                result = actionStatistics(dis, request);
                break;
            case ACTION_CLOSE_SESSION:
                
                result = actionCloseSession(dis, request);
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
                    LOGGER.info(p.toString() + " selected");
                }
            }
            
            session.setAttribute("selectedProjectBean", selectedProjectBean);
            
            result = JSONConverter.buildJSONUserList(selectedProjectBean.getUsersInProject());
        }
        
        return result;
    }
    
    private String actionAddProject(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            Calendar calendar = new GregorianCalendar();
            
            String name = dis.readUTF();
            String description = dis.readUTF();
            
            int initialDay = Integer.valueOf(dis.readUTF());
            int initialMonth = Integer.valueOf(dis.readUTF());
            int initialYear = Integer.valueOf(dis.readUTF());
            calendar.set(initialYear, initialMonth, initialDay);
            Date initialDate = calendar.getTime();
            
            int endDay = Integer.valueOf(dis.readUTF());
            int endMonth = Integer.valueOf(dis.readUTF());
            int endYear = Integer.valueOf(dis.readUTF());
            calendar.set(endYear, endMonth, endDay);
            Date endDate = calendar.getTime();
            
            ProjectBean projectBean = new ProjectBean();
            projectBean.initEJB();
            projectBean.setName(name);
            projectBean.setDescription(description);
            projectBean.setInitialDate(initialDate);
            projectBean.setEndDate(endDate);

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
    
    private String actionAddSprint(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            SelectedProjectBean selectedProjectBean =
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            
            int iSprintNumber = Integer.valueOf(dis.readUTF());
            int initialDay = Integer.valueOf(dis.readUTF());
            int initialMonth = Integer.valueOf(dis.readUTF());
            int initialYear = Integer.valueOf(dis.readUTF());
            int endDay = Integer.valueOf(dis.readUTF());
            int endMonth = Integer.valueOf(dis.readUTF());
            int endYear = Integer.valueOf(dis.readUTF());
            
            Calendar calendar = new GregorianCalendar();
            calendar.set(initialYear, initialMonth, initialDay);
            Date initialDate = calendar.getTime();
            calendar.set(endYear, endMonth, endDay);
            Date endDate = calendar.getTime();
            
            SprintBean sprintBean =  new SprintBean();
            sprintBean.setSprintNumber(iSprintNumber);
            sprintBean.setInitialDate(initialDate);
            sprintBean.setEndDate(endDate);
            
            sprintBean.initEJB();
            
            int idProject = selectedProjectBean.getIdProject();
            Sprint sprint = sprintBean.persistSprint(idProject);
            
            if (sprint == null) {
                result = ERROR_ADD_SPRINT;
            }
            else {
                
                selectedProjectBean.addSprint(sprint);
                result = JSONConverter.buildJSONSprintList((List<Sprint>)selectedProjectBean.getSprints());
            }
        }
        return result;
    }
    
    private String actionAddTask(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            SelectedProjectBean selectedProjectBean =
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            
            SelectedSprintBean selectedSprintBean =
                    (SelectedSprintBean) session.getAttribute("selectedSprintBean");
            
            int idProject = selectedProjectBean.getIdProject();
            int idSprint = selectedSprintBean.getIdSprint();
            
            String description = dis.readUTF();
            int time = Integer.valueOf(dis.readUTF());
            
            if (description == null) {
                LOGGER.warning("Null description");
            }
            
            SprintPlaningBean sprintPlaningBean = new SprintPlaningBean();
            sprintPlaningBean.initEJB();
            
            sprintPlaningBean.setDescripcion(description);
            sprintPlaningBean.setTime(time);
            
            Task task = sprintPlaningBean.persistTask(idProject, idSprint);
            if (task == null) {
                
                result = ERROR_ADD_TASK;
            }
            else {
                
                selectedSprintBean.addTask(task);
                result = JSONConverter.buildJSONTaskList((List<Task>)selectedSprintBean.getTaskList());
            }
        }
        return result;
    }
    
    public String actionDeleteProject(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idProject = Integer.parseInt(dis.readUTF());
            SelectedProjectBean selectedProjectBean =  new SelectedProjectBean();
            selectedProjectBean.initEJB();
            ProjectListBean projectListBean =
                    (ProjectListBean) session.getAttribute("projectListBean");
            
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
            
            Project p = selectedProjectBean.removeProject();
            if (p == null) {
                result = ERROR_DELETE_PROJECT;
            }
            else {
                projectListBean.removeActiveProject(p);
                
//                selectedProjectBean.setIdProject(-1);
//                selectedProjectBean.setName(null);
//                selectedProjectBean.setDescription(null);
//                selectedProjectBean.setInitialDate(null);
//                selectedProjectBean.setEndDate(null);
//                selectedProjectBean.setOwner(null);
//                selectedProjectBean.setSprints(null);
                selectedProjectBean = null;
                
                result = JSONConverter.buildJSONProjectList(
                        session.getId(), 
                        (List<Project>) projectListBean.getActiveProjects());
            }
        }
        
        return result;
    }
    
    public String actionDeleteSprint(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idSprint = Integer.valueOf(dis.readUTF());
            
            SelectedProjectBean selectedProjectBean =
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            
            SelectedSprintBean selectedSprintBean = new SelectedSprintBean();
            selectedSprintBean.initEJB();
            
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
            
            Sprint sprint = selectedSprintBean.removeSprint();
            if (sprint == null) {
                result = ERROR_DELETE_SPRINT;
            }
            else {
                
                selectedProjectBean.removeSprint(sprint);
                
                selectedSprintBean = null;
                result = JSONConverter.buildJSONSprintList((List<Sprint>)selectedProjectBean.getSprints());
            }
        }
        
        return result;
    }
    
    public String actionDeleteTask(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idTask = Integer.valueOf(dis.readUTF());
            Task task = new Task(idTask);
            
            SelectedSprintBean selectedSprintBean =
                    (SelectedSprintBean) session.getAttribute("selectedSprintBean");
            
            SprintPlaningBean sprintPlaningBean = new SprintPlaningBean();
            sprintPlaningBean.initEJB();
            sprintPlaningBean.setTask(task);
            
            task = sprintPlaningBean.removeTask();
            if (task == null) {
                result = ERROR_DELETE_TASK;
            }
            else {
                
                selectedSprintBean.removeTask(task);
                result = JSONConverter.buildJSONTaskList((List<Task>)selectedSprintBean.getTaskList());
            }
        }
        
        return result;
    }
    
    public String actionEditTaskAsk(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            SelectedProjectBean selectedProjectBean =
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            
            List<User> userList = selectedProjectBean.getUsersInProject();
            result = JSONConverter.buildJSONUserList(userList);
        }
        
        return result;
    }
    
    public String actionEditProject(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        // Check session
        HttpSession session = request.getSession(false);
        if (session == null) {
            return SESSION_EXPIRED;
        }
        
        // Get data
        Calendar calendar = new GregorianCalendar();
            
        String name = dis.readUTF();
        String description = dis.readUTF();

        int initialDay = Integer.valueOf(dis.readUTF());
        int initialMonth = Integer.valueOf(dis.readUTF());
        int initialYear = Integer.valueOf(dis.readUTF());
        calendar.set(initialYear, initialMonth, initialDay);
        Date initialDate = calendar.getTime();

        int endDay = Integer.valueOf(dis.readUTF());
        int endMonth = Integer.valueOf(dis.readUTF());
        int endYear = Integer.valueOf(dis.readUTF());
        calendar.set(endYear, endMonth, endDay);
        Date endDate = calendar.getTime();

        int idProject = Integer.valueOf(dis.readUTF());
        
        // Edit Project
        SelectedProjectBean selectedProjectBean = new SelectedProjectBean();
        selectedProjectBean.initEJB();

        selectedProjectBean.setIdProject(idProject);
        selectedProjectBean.setName(name);
        selectedProjectBean.setDescription(description);
        selectedProjectBean.setInitialDate(initialDate);
        selectedProjectBean.setEndDate(endDate);

        Project project = selectedProjectBean.editProject();

        if (project == null) {
            return ERROR_EDIT_PROJECT;
        }
        
        ProjectListBean projectListBean =
                    (ProjectListBean) session.getAttribute("projectListBean");
        Collection<Project> projectList = projectListBean.getActiveProjects();

        for (Project p : projectList) {
            if (p.getIdProject() == selectedProjectBean.getIdProject()) {
                p.setName(name);
                p.setDescription(description);
                p.setInitialDate(initialDate);
                p.setEndDate(endDate);
            }
        }

        return JSONConverter.buildJSONProjectList(session.getId(), (List<Project>) projectList);
        
    }
    
    public String actionEditUserProjectAsk(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idProject = Integer.parseInt(dis.readUTF());
            SelectedProjectBean selectedProjectBean = new SelectedProjectBean();
            selectedProjectBean.initEJB();
            ProjectListBean projectListBean =
                    (ProjectListBean) session.getAttribute("projectListBean");
            
            for (Project p : projectListBean.getActiveProjects()) {
                if (p.getIdProject() == idProject) {
                    selectedProjectBean.setIdProject(p.getIdProject());
                    selectedProjectBean.setName(p.getName());
                    selectedProjectBean.setDescription(p.getDescription());
                    selectedProjectBean.setInitialDate(p.getInitialDate());
                    selectedProjectBean.setEndDate(p.getEndDate());
                    selectedProjectBean.setOwner(p.getScrumMaster());
                    
                    selectedProjectBean.select();
                    
                    session.setAttribute("selectedProjectBean", selectedProjectBean);
                }
            }
            
            List<User> userList = selectedProjectBean.getUsersInProject();
            List<User> userListNotInProject = selectedProjectBean.getUserNotInProject();
            
            result = JSONConverter.buildJSONUserList(userList, userListNotInProject);
        }
        
        return result;
    }
    
    public String actionEditUserProjectSend(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            SelectedProjectBean selectedProjectBean =
                    (SelectedProjectBean) session.getAttribute("selectedProjectBean");
            String scrumMasterId = String.valueOf(selectedProjectBean.getOwner().getIdUser());
            
            // Get number of id's
            int size = dis.readInt();
            // Get array of id's
            List<String> idUsers = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
                String id = dis.readUTF();
                if (!scrumMasterId.equals(id)) {
                    idUsers.add(id);
                }
            }
            
            MyUserListBean myUserListBean = new MyUserListBean();
            myUserListBean.initEJB();
            myUserListBean.setIdProject(selectedProjectBean.getIdProject());
            myUserListBean.addUsers(idUsers.toArray());
            
            ProjectListBean projectListBean = 
                    (ProjectListBean) session.getAttribute("projectListBean");
            
            result = JSONConverter.buildJSONProjectList(session.getId(), (List<Project>)projectListBean.getActiveProjects());
        }
        
        return result;
    }
    
    public String actionEditTaskSend(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idTask = Integer.valueOf(dis.readUTF());
            char state = dis.readUTF().toCharArray()[0];
            int idUser = Integer.valueOf(dis.readUTF());
            int time = Integer.valueOf(dis.readUTF());
            String description = dis.readUTF();
            
            TaskBean taskBean = new TaskBean();
            taskBean.initEJB();
            
            taskBean.setIdTask(idTask);
            taskBean.setDescription(description);
            taskBean.setState(state);
            taskBean.setTime(time);
            
            Task task = taskBean.updateTask(idUser);
            
            if (task == null) {
                return ERROR_EDIT_TASK;
            }
            
            SelectedSprintBean selectedSprintBean =
                    (SelectedSprintBean) session.getAttribute("selectedSprintBean");
            
            selectedSprintBean.updateTask(task);
            
            result = JSONConverter.buildJSONTaskList((List<Task>)selectedSprintBean.getTaskList());
        }
        
        return result;
    }
    
    public String actionStatistics(DataInputStream dis, HttpServletRequest request)
            throws IOException {
        
        String result = SESSION_EXPIRED;
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            
            int idSprint = Integer.valueOf(dis.readUTF());
            
            ChartsBean chartsBean = new ChartsBean();
            chartsBean.initEJB();
            
            Date date = new Date();
            List<Long> times = chartsBean.getStatistics(idSprint, date);
            
            for (Long l : times) {
                LOGGER.info("Sum hours: " + l);
            }
            
            result = JSONConverter.buildJSONHourList(times);
        }
        
        return result;
    }
    
    public String actionCloseSession(DataInputStream dis, HttpServletRequest request) {
        String result = SESSION_CLOSED;
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
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
