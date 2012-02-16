/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Project;
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
@SessionScoped
public class SprintBean extends SprintBaseBean {

    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private SprintFacade sprintFacade;
    private DashboardModel model;
    
    private String nameProject;
    private Sprint sprint;
    private String text1="10";
    private String text2="20";
    private String text3="30";

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
    
    public List<Task> getDoTasks() {
        return doTasks;
    }

    public void setDoTasks(List<Task> doTasks) {
        this.doTasks = doTasks;
    }

    public List<Task> getDoingTasks() {
        return doingTasks;
    }

    public void setDoingTasks(List<Task> doingTasks) {
        this.doingTasks = doingTasks;
    }

    public List<Task> getToDoTasks() {
        return toDoTasks;
    }

    public void setToDoTasks(List<Task> toDoTasks) {
        this.toDoTasks = toDoTasks;
    }
    private List<Task> doingTasks;
    private List<Task> doTasks;
    private List<Task> toDoTasks;
    
    @PostConstruct
    private void init() {


        Task task = null;
        Project project = null;
        Sprint sp = null;

        List<Project> projects = projectFacade.findAll();

        for (Project p : projects) {
            if (p.getIdProject() == 1) {
                project = p;
            }
        }

        setNameProject(project.getName());

        List<Sprint> sprints = sprintFacade.findByProject(project);

        for (Sprint s : sprints) {
            if (s.getSprintNumber() == 1) {
                sp = s;
            }
        }

        setSprintNumber(sp.getSprintNumber());
        setInitialDate(sp.getInitialDate());
        setEndDate(sp.getEndDate());

        List<Task> tasks = taskFacade.findBySprint(sprint);

        for (Task t : tasks) {
            if (t.getState() == 'd') {
                doTasks.add(t);
            } else if (t.getState() == 'i') {
                doingTasks.add(t);
            } else if (t.getState() == 't') {
                toDoTasks.add(t);
            }
        }

        model = new DefaultDashboardModel();

        DashboardColumn column1 = new DefaultDashboardColumn();
        DashboardColumn column2 = new DefaultDashboardColumn();
        DashboardColumn column3 = new DefaultDashboardColumn();
//
//        for (Task tk : doTasks) {
//            column1.addWidget("pnl1");
//        }
        column1.addWidget("pnl1");
        column2.addWidget("pnl2");
        column3.addWidget("pnl3");

        for (Task t : tasks) {
            column1.addWidget("pnl5");
        }

        column1.addWidget("pnl4");
        column2.addWidget("pnl5");
        column3.addWidget("pnl6");
        model.addColumn(column1);
        model.addColumn(column2);
        model.addColumn(column3);
    }

    /**
     * Creates a new instance of SprintBean
     */
    public SprintBean() {
        this.doingTasks = null;
        this.doTasks = null;
        this.toDoTasks = null;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
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
