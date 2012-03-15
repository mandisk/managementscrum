/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import org.inftel.scrum.entity.Task;

/**
 *
 * @author Jorge
 */
public class SprintBaseBean implements Serializable {
    protected int idSprint;
    protected int sprintNumber;
    protected Date initialDate;
    protected Date endDate;
    protected String projectPath;
    
    protected Collection<Task> taskList;

    public SprintBaseBean() {
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(int idSprint) {
        this.idSprint = idSprint;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public int getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(int sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public Collection<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(Collection<Task> taskList) {
        this.taskList = taskList;
    }
    
    public String getProjectPath() {
        return projectPath;
    }
    
    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    
    public void addTask(Task task) {
        this.taskList.add(task);
    }
    
    public void removeTask(Task task) {
        this.taskList.remove(task);
    }
}
