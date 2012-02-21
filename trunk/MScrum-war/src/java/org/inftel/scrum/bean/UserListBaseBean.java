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
    protected int idProject;

    public DualListModel<User> getUsers() {
        return users;
    }

    public void setUsers(DualListModel<User> users) {
        this.users = users;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    
}
