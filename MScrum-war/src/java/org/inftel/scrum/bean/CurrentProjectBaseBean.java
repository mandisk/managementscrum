/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.util.List;
import javax.faces.component.html.HtmlDataTable;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author Jorge
 */
public class CurrentProjectBaseBean {
    protected Project currentProject;
    protected HtmlDataTable dataTable;
    protected DualListModel<User> users;
    protected List<User> usersSource;
    protected List<User> usersTarget;
    protected List<Sprint> sprints;
    protected HtmlDataTable dataSprintTable;
    protected Sprint currentSprint;

    public HtmlDataTable getDataSprintTable() {
        return dataSprintTable;
    }

    public void setDataSprintTable(HtmlDataTable dataSprintTable) {
        this.dataSprintTable = dataSprintTable;
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public DualListModel<User> getUsers() {
        return users;
    }

    public void setUsers(DualListModel<User> users) {
        this.users = users;
    }

    public List<User> getUsersSource() {
        return usersSource;
    }

    public void setUsersSource(List<User> usersSource) {
        this.usersSource = usersSource;
    }

    public List<User> getUsersTarget() {
        return usersTarget;
    }

    public void setUsersTarget(List<User> usersTarget) {
        this.usersTarget = usersTarget;
    }
    
    public CurrentProjectBaseBean() {
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public HtmlDataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(HtmlDataTable dataTable) {
        this.dataTable = dataTable;
    }
}
