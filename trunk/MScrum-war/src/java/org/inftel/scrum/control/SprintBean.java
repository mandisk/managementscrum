/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class SprintBean extends SprintBaseBean implements Serializable {
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private SprintFacade sprintFacade;

    private DashboardModel model;
    private Sprint sprint;
    private int idSprint;
    
    private String text1="2";
    private String text2="8";
    private String text3="40";

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }
    
    
    /**
     * Creates a new instance of SprintBean
     */
    public SprintBean() {
        
//        List<Task> tasks = taskFacade.findBySprint(sprint);
        
        model = new DefaultDashboardModel();
        
        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
        
        column1.addWidget("pnl1");
        column2.addWidget("pnl2");
        column3.addWidget("pnl3");
        
//        for (Task t : tasks) {
//            column1.addWidget("pnl5");
//        }
          
        column1.addWidget("pnl4");
        column2.addWidget("pnl5");
        column3.addWidget("pnl6");
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public void handleReorder(DashboardReorderEvent event) {
        FacesMessage message = new FacesMessage();
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        message.setSummary("Reordered: " + event.getWidgetId());
        message.setDetail("Item index: " + event.getItemIndex() + ", Column index: " + event.getColumnIndex() + ", Sender index: " + event.getSenderColumnIndex());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public String viewSprint() {
        sprint = (Sprint) sprintTable.getRowData();
        return "sprint";
    }
}