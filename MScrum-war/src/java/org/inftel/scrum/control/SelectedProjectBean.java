/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Collection;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.ProjectBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class SelectedProjectBean extends ProjectBaseBean {
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private SprintFacade sprintFacade;

    private int idProject;
    private Collection<Sprint> sprints;
    private HtmlDataTable projectTable;
    private boolean selected;
    /**
     * Creates a new instance of SelectedProjectBean
     */
    public SelectedProjectBean() {
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public Collection<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(Collection<Sprint> sprints) {
        this.sprints = sprints;
    }

    public HtmlDataTable getProjectTable() {
        return projectTable;
    }

    public void setProjectTable(HtmlDataTable projectTable) {
        this.projectTable = projectTable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String select() {
        Project p = (Project) projectTable.getRowData();
        
        this.idProject = p.getIdProject();
        this.name = p.getName();
        this.description = p.getDescription();
        this.initialDate = p.getInitialDate();
        this.endDate = p.getEndDate();
        this.owner = p.getScrumMaster();
        this.sprints = sprintFacade.findByProject(idProject);
        
        this.selected = true;
        
        return null;
    }
    
    public String deleteProject() {
        
        FacesContext context = FacesContext.getCurrentInstance();      
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        
        Project p = projectFacade.removeProject(this.idProject);
        
        if (p != null) {
            
            ProjectListBean projectListBean = (ProjectListBean) sessionMap.get("projectListBean");
            projectListBean.removeActiveProject(p);
            
            String delete = name + " deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, delete, delete);
            context.addMessage(null, msg);
            
            this.owner = null;
            this.sprints = null;
            this.selected = false;
        }
        else {
            
            String delete = name + " can not be deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, delete, delete);
            context.addMessage(null, msg);
        }
        return null;
    }
    
    public String updateProject() {
        
        FacesContext context = FacesContext.getCurrentInstance();      
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        Project project;
        
        if (initialDate.after(endDate)) {
            
            String errorMessage = "Initial date is after than end date";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
            context.addMessage(null, msg);
            return null;
        }
        
        project = projectFacade.updateProject(idProject, name, description, initialDate, endDate);
        
        if (project != null) {
            
            ProjectListBean projectListBean = (ProjectListBean) sessionMap.get("projectListBean");
            projectListBean.removeActiveProject(project);
            projectListBean.addActiveProject(project);
            
            SelectedProjectBean selectedProjectBean = (SelectedProjectBean) sessionMap.get("selectedProjectBean");
            selectedProjectBean.setName(name);
            selectedProjectBean.setDescription(description);
            selectedProjectBean.setInitialDate(initialDate);
            selectedProjectBean.setEndDate(endDate);
            
            String update = name + " updated";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, update, update);
            context.addMessage(null, msg);
        }
        else {
            
            String update = name + " can not be updated";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, update, update);
            context.addMessage(null, msg);
        }
        
        return null;
    }
}