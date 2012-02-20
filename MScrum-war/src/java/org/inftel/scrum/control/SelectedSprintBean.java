/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.entity.Sprint;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class SelectedSprintBean extends SprintBaseBean {
    @EJB
    private SprintFacade sprintFacade;

    private HtmlDataTable sprintTable;
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
        Sprint sprint = (Sprint) this.sprintTable.getRowData();
        
        this.idSprint = sprint.getIdSprint();
        this.sprintNumber = sprint.getSprintNumber();
        this.initialDate = sprint.getInitialDate();
        this.endDate = sprint.getEndDate();
        
        return null;
    }
    
    public String deleteSprint() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        
        Sprint sprint = sprintFacade.removeSprint(this.idSprint);
        
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
    
    public String createTask() {
        Sprint sprint = (Sprint) this.sprintTable.getRowData();
        
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        
        requestMap.put("sprint", sprint);
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
}
