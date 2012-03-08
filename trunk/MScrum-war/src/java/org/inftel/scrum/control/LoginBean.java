/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.bean.LoginBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.User;
import org.inftel.scrum.util.Util;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class LoginBean extends LoginBaseBean {
    
    private final static Logger LOGGER = Logger.getLogger(LoginBean.class.getName());

    @EJB
    private UserFacade userFacade;
    @EJB
    private ProjectFacade projectFacade;
    private User user;
    List<String> project;
    List<Project> projects;
    private Collection<Project> activeProjects;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getProyect() {
        project = new ArrayList<String>();
        int i;

        for (Project p : user.getProjects()) {
            project.add(p.getName());
        }

        return project;
    }

    public String doLogin() {
        FacesMessage msg;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            msg = new FacesMessage("Empty fields");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        user = userFacade.findByEmail(email);
        if (user != null) {

            if (user.getPassword().trim().equals(Util.md5(password.trim()).trim())) {

                Collection<Project> projects = projectFacade.findActiveProjectByUser(user.getIdUser());
                if (facesContext != null) {
                    ProjectListBean projectListBean = new ProjectListBean();
                    projectListBean.setActiveProjects(activeProjects);
                    facesContext.getExternalContext().getSessionMap().put("projectListBean", projectListBean);
                }
                else {
                    activeProjects = projects;
                }
                return "main?faces-redirect=true";
            }
            
            if (facesContext != null) {
                msg = new FacesMessage("Wrong password");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        }

        if (facesContext != null) {
            msg = new FacesMessage("User not found");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        
        return null;
    }

    public String doLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.invalidate();
        return "index?faces-redirect=true";
    }

    public void initEJB() {
        
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/UserFacade");
            this.userFacade =
                    (UserFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UserFacade.class);
            ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/ProjectFacade");
            this.projectFacade = 
                    (ProjectFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, ProjectFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
    
    public Collection<Project> getActiveProjects() {
        return this.activeProjects;
    }
}
