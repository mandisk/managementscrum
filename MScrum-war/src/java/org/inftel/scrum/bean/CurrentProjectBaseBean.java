/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import javax.faces.component.html.HtmlDataTable;
import org.inftel.scrum.entity.Project;

/**
 *
 * @author Jorge
 */
public class CurrentProjectBaseBean {
    protected Project currentProject;
    protected HtmlDataTable dataTable;

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
