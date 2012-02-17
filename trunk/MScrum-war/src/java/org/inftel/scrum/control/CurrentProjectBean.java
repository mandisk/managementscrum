/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import com.sun.faces.taglib.html_basic.DataTableTag;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.bean.CurrentProjectBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class CurrentProjectBean extends CurrentProjectBaseBean{
    @EJB
    private SprintFacade sprintFacade;

    private final static Logger LOGGER = Logger.getLogger(AddUserProjectBean.class .getName());
    
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private UserFacade userFacade;
    
    
    /**
     * Creates a new instance of CurrentProjectBean
     */
    public CurrentProjectBean() {
    }
    
    public String viewInfo() {
        currentProject = (Project) dataTable.getRowData();
        return null;
    }
    
    private void loadLists() {
        LOGGER.info("Entro en el load");
        usersSource = new ArrayList<User>();
        usersTarget = new ArrayList<User>();
        LOGGER.info("loadList: " + currentProject.getName());
        usersTarget = (List<User>) currentProject.getUsers();
        for (Iterator<User> it = usersTarget.iterator(); it.hasNext();) {
            User user = it.next();
            LOGGER.info("usersTarget: " + user.getName());
        }
        
        usersSource = projectFacade.selectUsersNotIn(currentProject.getIdProject());
        users = new DualListModel<User>(usersSource, usersTarget);
    }

//    public void insert() {
//        LOGGER.info("AddUserProjectBean users.target().tostring = "+users.getTarget().toString());
//        LOGGER.info(currentProject.getIdProject().toString());
//        Object[] listaId = users.getTarget().toArray();
//        Vector<User> lista = new Vector<User>();
//        for (int i = 0; i < listaId.length; i++) {
//            String sid = (String)listaId[i];
//            User user = userFacade.find(Integer.parseInt(sid));
//            
//            lista.add(user);
//        }
//        for (Iterator<User> it = lista.iterator(); it.hasNext();) {
//            User user = it.next();
//            LOGGER.info(user.getName());
//        }
//        
//        projectFacade.AddUsers(currentProject.getIdProject(), lista);
////        int i = 0;
//    }
    
    public String addUsers(){
//        currentProject = (Project) dataTable.getRowData();
        LOGGER.info(currentProject.getDescription());
//        currentProject = projectFacade.findAll().get(0);
        loadLists();
        UserListBean userListBean = new UserListBean();
        userListBean.setProject(currentProject);
        userListBean.setUsers(users);
        userListBean.setUsersSource(users.getSource());
        userListBean.setUsersTarget(users.getTarget());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userListBean", userListBean);
        return "addUsersProject?faces-redirect=true";
    }
    
    
    public String CRUDSprint(){
        currentProject = (Project) dataTable.getRowData();
//        loadSprint();
        return "CRUDSprint";
    }

    private void loadSprint() {
        sprints = (List<Sprint>) currentProject.getSprints();
        for (Iterator<Sprint> it = sprints.iterator(); it.hasNext();) {
            Sprint sprint = it.next();
            LOGGER.info(sprint.getIdSprint().toString());
        }
    }
    
    
    public String deleteSprint(){
        LOGGER.info("dfsfd");
        currentSprint = (Sprint) dataSprintTable.getRowData();
        LOGGER.info(currentSprint.getIdSprint().toString());
//        sprintFacade.remove(currentSprint);
        return "main?faces-redirect=true";
    }
    
    public String addTaskToSprint(){
        return "addTask"; //Not implemented yet
    }
    
    public String modifySprint(){
        return "modifySprint";
    }
    
    public void addPickList(ActionEvent event){
        addUsers();
    }
    
    @Override
    public String toString(){
        return currentProject.getDescription();
    }
    
    public String redirige(){
        return "index?faces-redirect=true";
    }
}
