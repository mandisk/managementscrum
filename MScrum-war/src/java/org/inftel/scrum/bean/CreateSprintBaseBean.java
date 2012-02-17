/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.application.FacesMessage;
import org.inftel.scrum.entity.Project;

/**
 *
 * @author antonio
 */

public class CreateSprintBaseBean implements Serializable{

    protected String sprintNumber;
    protected Date initDate;
    protected Date endDate;
    protected String projectPath;
    protected Project p;
    protected FacesMessage msg;
    
    public CreateSprintBaseBean() {
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(String sprintNumber) {
        this.sprintNumber = sprintNumber;
    }
    
    
}
