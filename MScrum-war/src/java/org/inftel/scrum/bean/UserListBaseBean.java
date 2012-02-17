/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.List;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author agustinjf
 */
public class UserListBaseBean implements Serializable{
    protected DualListModel<User> users;
    protected List<User> usersSource;
    protected List<User> usersTarget;
    protected Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    
}
