/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.UserListBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class MyUserListBean extends UserListBaseBean {
    
    private static final Logger LOGGER = Logger.getLogger(MyUserListBean.class.getName());
    
    @EJB
    private ProjectFacade projectFacade;

    /**
     * Creates a new instance of MyUserListBean
     */
    public MyUserListBean() {
    }
    
    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> session = context.getExternalContext().getSessionMap();
        SelectedProjectBean selectedProjectBean = 
                (SelectedProjectBean) session.get("selectedProjectBean");

        User scrumMaster = selectedProjectBean.getOwner();

        List<User> usersSource = selectedProjectBean.getUserNotInProject();
        List<User> usersTarget = selectedProjectBean.getUsersInProject();

        usersTarget.remove(scrumMaster);
        usersSource.remove(scrumMaster);

        this.idProject = selectedProjectBean.getIdProject();
        this.users = new DualListModel<User>(usersSource, usersTarget);
    }
    
    public String insert() {
        
        Object[] idUsers = users.getTarget().toArray();
        projectFacade.addUsers(this.idProject, idUsers);
        
        return "main?faces-redirect=true";
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
