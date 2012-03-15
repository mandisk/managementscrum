/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.inftel.scrum.bean.ProjectBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class SelectedProjectBean extends ProjectBaseBean {
    
    private static final Logger LOGGER = Logger.getLogger(SelectedProjectBean.class.getName());
    
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private SprintFacade sprintFacade;
    @EJB
    private UserFacade userFacade;

    private int idProject;
    private Collection<Sprint> sprints;
    private HtmlDataTable projectTable;
    private boolean selected;
    private DualListModel<User> users;
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
    
    public void addSprint(Sprint sprint) {
        this.sprints.add(sprint);
    }
    
    public void removeSprint(Sprint sprint) {
        this.sprints.remove(sprint);
    }

    public DualListModel<User> getUsers() {
        return users;
    }

    public void setUsers(DualListModel<User> users) {
        this.users = users;
    }
    
    public String select() {
        if (projectTable != null) {
            Project p = (Project) projectTable.getRowData();
            this.idProject = p.getIdProject();
            this.name = p.getName();
            this.description = p.getDescription();
            this.initialDate = p.getInitialDate();
            this.endDate = p.getEndDate();
            this.owner = p.getScrumMaster();
            this.selected = true;   
        }
        
        this.sprints = sprintFacade.findByProject(idProject);
        
        return null;
    }
    
    public String deleteProject() {
        
        FacesContext context = FacesContext.getCurrentInstance();      
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        
        Project p = removeProject();
        
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
    
    public Project removeProject() {
        return projectFacade.removeProject(this.idProject);
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
    
    public String addUsers(){
        
        return "addUsersProject?faces-redirect=true";
    }
    
    public void initEJB() {
        
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/SprintFacade");
            this.sprintFacade =
                    (SprintFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, SprintFacade.class);
            ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/ProjectFacade");
            this.projectFacade = 
                    (ProjectFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, ProjectFacade.class);
            ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/UserFacade");
            this.userFacade = 
                    (UserFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UserFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
    
    public List<User> getUsersInProject() {
        return userFacade.findByProject(idProject);
    }
    
    public List<User> getUserNotInProject() {
        return userFacade.findNotInProject(idProject);
    }
}
