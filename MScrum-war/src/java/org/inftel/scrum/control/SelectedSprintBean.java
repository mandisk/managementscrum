/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class SelectedSprintBean extends SprintBaseBean {
    
    private static final Logger LOGGER = Logger.getLogger(SelectedSprintBean.class.getName());
    
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private SprintFacade sprintFacade;

    private HtmlDataTable sprintTable;
    DualListModel<Task> tareas;
    /**
     * Creates a new instance of SelectedSprintBean
     */
    public SelectedSprintBean() {
    }

    public HtmlDataTable getSprintTable() {
        return sprintTable;
    }

    public void setSprintTable(HtmlDataTable sprintTable) {
        this.sprintTable = sprintTable;
    }
    
    public String selectSprint() {
        if (sprintTable != null) {
            Sprint sprint = (Sprint) this.sprintTable.getRowData();

            this.idSprint = sprint.getIdSprint();
            this.sprintNumber = sprint.getSprintNumber();
            this.initialDate = sprint.getInitialDate();
            this.endDate = sprint.getEndDate();
        }
        
        this.taskList = taskFacade.findBySprint(idSprint);
        
        return null;
    }
    
    public String deleteSprint() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        
        Sprint sprint = removeSprint();
        
        if (sprint != null) {
            
            SelectedProjectBean selectedProjectBean = (SelectedProjectBean) sessionMap.get("selectedProjectBean");
            selectedProjectBean.removeSprint(sprint);
            
            String delete = "Sprint " + this.sprintNumber + " deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, delete, delete);
            context.addMessage(null, msg);
            
            return null;
        }
        else {
            String error = "Sprint " + this.sprintNumber + " can not be deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            context.addMessage(null, msg);
        }
        
        return null;
    }
    
    public Sprint removeSprint() {
        return sprintFacade.removeSprint(this.idSprint);
    }
    
    public String createTask() {
//         Sprint currentSprint = (Sprint) sprintTable.getRowData();
//       
//         List<Task> tareasSource = new ArrayList<Task>();
//        List<Task> tareasTarget = new ArrayList<Task>();
//          
//       tareasTarget = (List<Task>) currentSprint.getTaskCollection();
//        
//        tareasSource = taskFacade.findTaskNotSprint(currentSprint.getProject().getIdProject());
//        tareas = new DualListModel<Task>(tareasSource, tareasTarget);
//      
//        SprintPlaningBean sprintPlaningBean = new SprintPlaningBean();
//        sprintPlaningBean.settareasSourcee(tareasSource);
//        sprintPlaningBean.setTareasTarget(tareasTarget);
//        sprintPlaningBean.setP(currentSprint.getProject());
//        sprintPlaningBean.setTareas(tareas);
//        sprintPlaningBean.setS(currentSprint);
//        
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sprintPlaningBean", sprintPlaningBean);
        Sprint sprint = (Sprint) this.sprintTable.getRowData();
        
        this.idSprint = sprint.getIdSprint();
        this.sprintNumber = sprint.getSprintNumber();
        this.initialDate = sprint.getInitialDate();
        this.endDate = sprint.getEndDate();
        
        return "sprintPlaning?faces-redirect=true";
    }
    
    public String viewPanel() {
        Sprint sprint = (Sprint) this.sprintTable.getRowData();
        
        this.idSprint = sprint.getIdSprint();
        this.sprintNumber = sprint.getSprintNumber();
        this.initialDate = sprint.getInitialDate();
        this.endDate = sprint.getEndDate();
        
        return "sprint?faces-redirect=true";
    }
    
    public void initEJB() {
        
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/SprintFacade");
            this.sprintFacade =
                    (SprintFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, SprintFacade.class);
            ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/TaskFacade");
            this.taskFacade = 
                    (TaskFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, TaskFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
}
