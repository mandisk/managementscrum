/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Sprint;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardModel;

/**
 *
 * @author inftel
 */
@ManagedBean
@SessionScoped
public class SprintBean extends SprintBaseBean {

    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private SprintFacade sprintFacade;
    private DashboardModel model;
    private Sprint sprint;

//    //PRUEBAS
//    private String text1 = "10";
//    private String text2 = "20";
//    private String text3 = "30";
//
//    public String getText1() {
//        return text1;
//    }
//
//    public void setText1(String text1) {
//        this.text1 = text1;
//    }
//
//    public String getText2() {
//        return text2;
//    }
//
//    public void setText2(String text2) {
//        this.text2 = text2;
//    }
//
//    public String getText3() {
//        return text3;
//    }
//
//    public void setText3(String text3) {
//        this.text3 = text3;
//    }
//    @PostConstruct
//    private void init() {
//
//        model = new DefaultDashboardModel();
//
//        DashboardColumn column1 = new DefaultDashboardColumn();
//        DashboardColumn column2 = new DefaultDashboardColumn();
//        DashboardColumn column3 = new DefaultDashboardColumn();
//
//        column1.addWidget("pnl1");
//        column2.addWidget("pnl2");
//        column3.addWidget("pnl3");
//
//        for (Task t : tasks) {
//            column1.addWidget("pnl5");
//        }
//
//        column1.addWidget("pnl4");
//        column2.addWidget("pnl5");
//        column3.addWidget("pnl6");
//        model.addColumn(column1);
//        model.addColumn(column2);
//        model.addColumn(column3);
    /**
     * Creates a new instance of SprintBean
     */
    public SprintBean() {
//        Task task = null;
//        Project project = null;
//
//        setSprintNumber(sprint.getSprintNumber());
//        setInitialDate(sprint.getInitialDate());
//        setEndDate(sprint.getEndDate());
//
//        nameProject = sprint.getProject().getName();
//
//        List<Task> tasks = taskFacade.findBySprint(sprint);
    }

    public DashboardModel getModel() {
        return model;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
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
    
    public String selectSprint() {
        sprint = (Sprint) sprintTable.getRowData();
        return null;
    }
    
    public String deleteSprint() {
        
//        sprint = (Sprint) sprintTable.getRowData();
        
        sprintFacade.remove(sprint);
        
        FacesContext context = FacesContext.getCurrentInstance();      
        String delete = "Sprint " + sprint.getSprintNumber() + " deleted";
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, delete, delete);
        context.addMessage(null, msg);
        
        return null;
    }
}
