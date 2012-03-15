/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Date;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.entity.Sprint;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class SprintBean extends SprintBaseBean{
    
    private static final Logger LOGGER = Logger.getLogger(SprintBean.class.getName());
    
    @EJB
    private SprintFacade sprintFacade;

    /**
     * Creates a new instance of SprintBean
     */
    public SprintBean() {
    }
    
    public Date getActualDate() {
        return new Date();
    }
    
    public String createSprint() {
        Sprint sprint;
        FacesMessage msg;
        FacesContext context = FacesContext.getCurrentInstance();
        SelectedProjectBean selectedProjectBean = 
                (SelectedProjectBean) context.getExternalContext().getSessionMap().get("selectedProjectBean");
        
        if (sprintNumber < 1 || initialDate == null || endDate == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Check the fields again please, because there are some empty fields.", "sprintNumber");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        else {
            //Check Date
            if (endDate.compareTo(initialDate) < 0) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "End date cannot greater than initial date", "EndDate");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
            
            int idProject = selectedProjectBean.getIdProject();
            sprint = persistSprint(idProject);
            
            if (sprint == null) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Sprint Number already exist", "sprintNumber");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        }
        
        selectedProjectBean.addSprint(sprint);
        return "main?faces-redirect=true";
    }
    
    public Sprint persistSprint(int idProject) {
        return sprintFacade.createSprint(sprintNumber, initialDate, endDate, projectPath, idProject);
    }
    
    public void initEJB() {
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/SprintFacade");
            this.sprintFacade =
                    (SprintFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, SprintFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
}
