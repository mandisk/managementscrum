/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class SprintBaseBean {

    protected Date initialDate;
    protected Date endDate;
    protected int sprintNumber;
    protected HtmlDataTable sprintTable;
    /**
     * Creates a new instance of SprintBaseBean
     */
    public SprintBaseBean() {
        this.initialDate = null;
        this.endDate = null;
    }

    public int getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(int sprintNumber) {
        this.sprintNumber = sprintNumber;
    }
       
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public HtmlDataTable getSprintTable() {
        return sprintTable;
    }

    public void setSprintTable(HtmlDataTable sprintTable) {
        this.sprintTable = sprintTable;
    }
    
}
