/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
public class LoginBean extends LoginBaseBean{
    @EJB
    private UserFacade userFacade;
    @EJB
    private ProjectFacade projectFacade;
    private User user;
    List<String> project;
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
    
    public List<String> getProyect(){
        project = new ArrayList<String>();
        int i;
        
        for (Project p : user.getProjects()) {
            project.add(p.getName());
        }
        
        return project;
    }
    
    public String doLogin() {
        FacesMessage msg;
        if (email == null || email.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
            
            msg = new FacesMessage("Empty fields");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        user = userFacade.findByEmail(email);
        if (user != null) {
            
            if (user.getPassword().trim().equals(Util.md5(password.trim()).trim())) {
                
                ProjectListBean projectListBean = new ProjectListBean();
                projectListBean.setActiveProjects(projectFacade.findActiveProjectByUser(user.getIdUser()));
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("projectListBean", projectListBean);
                return "main?faces-redirect=true";
            }
            
            msg = new FacesMessage("Wrong password");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        
        msg = new FacesMessage("User not found");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }
    
    public String doLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(true);
        session.invalidate();
        return "index?faces-redirect=true";
    }
}
