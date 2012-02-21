/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
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
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private UserFacade userFacade;

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
        List<User> usersSource = projectFacade.selectUsersNotIn(selectedProjectBean.getIdProject());
        List<User> usersTarget = userFacade.findByProject(selectedProjectBean.getIdProject());
        usersTarget.remove(scrumMaster);
        
        this.idProject = selectedProjectBean.getIdProject();
        this.users = new DualListModel<User>(usersSource, usersTarget);
    }
    
    public String insert() {
        
        Object[] idUsers = users.getTarget().toArray();
        
        projectFacade.addUsers(this.idProject, idUsers);
        
        return "main?faces-redirect=true";
    }
}
