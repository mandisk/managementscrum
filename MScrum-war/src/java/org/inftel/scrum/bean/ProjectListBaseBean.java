/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.Collection;
import org.inftel.scrum.entity.Project;

/**
 *
 * @author Jorge
 */
public class ProjectListBaseBean implements Serializable {
    protected Collection<Project> activeProjects;

    public ProjectListBaseBean() {
    }

    public Collection<Project> getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(Collection<Project> activeProjects) {
        this.activeProjects = activeProjects;
    }
    
    public void addActiveProject(Project project) {
        this.activeProjects.add(project);
    }
    
    public void removeActiveProject(Project project) {
        this.activeProjects.remove(project);
    }
    
}
