/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.ProjectBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.entity.Project;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class ProjectBean extends ProjectBaseBean {
    
    private static final Logger LOGGER = Logger.getLogger(ProjectBean.class.getName());
    
    @EJB
    private ProjectFacade projectFacade;

    /**
     * Creates a new instance of ProjectBean
     */
    public ProjectBean() {
    }
    
    public String createProject() {
        
        // Obtenemos el usuario de la sesión
        FacesContext context = FacesContext.getCurrentInstance();      
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        LoginBean loginBean = (LoginBean) sessionMap.get("loginBean");
        
        if (initialDate.after(endDate)) {
            
            String errorMessage = "Initial date is after than end date";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
            return null;
        }
        
        if (projectFacade.findByName(name) == null) {
            
            Project p = persistProject(loginBean.getUser().getIdUser());

            ProjectListBean projectListBean = (ProjectListBean) sessionMap.get("projectListBean");
            projectListBean.getActiveProjects().add(p);
            
            SelectedProjectBean selectedProjectBean = (SelectedProjectBean) sessionMap.get("selectedProjectBean");
            selectedProjectBean.setIdProject(p.getIdProject());
            selectedProjectBean.setName(name);
            selectedProjectBean.setDescription(description);
            selectedProjectBean.setInitialDate(initialDate);
            selectedProjectBean.setEndDate(endDate);
            selectedProjectBean.setOwner(loginBean.getUser());
            selectedProjectBean.setSprints(p.getSprints());
            selectedProjectBean.setSelected(true);

            String message = name + " created";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
            context.addMessage(null, msg);
            
            name = "";
            description = "";
            initialDate = null;
            endDate = null;
        }
        else {
            String errorMessage = name + " already exists";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage);
            context.addMessage(null, msg);
        }
        
        return null;
    }
    
    public Project persistProject(int idUser) {
        
        return projectFacade.createProject(name, description, initialDate, endDate, idUser);
    }
    
    public void initEJB() {
        
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/ProjectFacade");
            this.projectFacade =
                    (ProjectFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, ProjectFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
}
