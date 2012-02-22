/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintPlaningBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author antonio
 */
@ManagedBean
@RequestScoped
public class SprintPlaningBean extends SprintPlaningBaseBean {

    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private TaskFacade taskFacade;
    private Collection<Task> tasks;
    private HtmlDataTable taskTable;
    private int idTask;
    private List<User> users;
    public User user;
    public Task task;
    public int user2;

    public int getUser2() {
        return user2;
    }

    public void setUser2(int user2) {
        this.user2 = user2;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public HtmlDataTable getTaskTable() {
        return taskTable;
    }

    public void setTaskTable(HtmlDataTable taskTable) {
        this.taskTable = taskTable;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public SprintPlaningBean() {
    }

    @PostConstruct
    void init() {

        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> session = context.getExternalContext().getSessionMap();
        SelectedSprintBean selectedSprintBean =
                (SelectedSprintBean) session.get("selectedSprintBean");
        SelectedProjectBean selectedProjectBean =
                (SelectedProjectBean) session.get("selectedProjectBean");

        this.idProject = selectedProjectBean.getIdProject();
        this.idSprint = selectedSprintBean.getIdSprint();

        tareasSource = taskFacade.findTaskNotSprint(this.idProject);
        tareasTarget = taskFacade.findBySprint(this.idSprint);
        tareas = new DualListModel<Task>(tareasSource, tareasTarget);

        tasks = taskFacade.findBySprint(this.idSprint);
        users = (List<User>) projectFacade.find(selectedProjectBean.getIdProject()).getUsers();
    }

    public String addTaskUser() {
        FacesContext context = FacesContext.getCurrentInstance();
        task.setUser(user);
        taskFacade.edit(task);
        context.addMessage(null, new FacesMessage("Successful", "Change Saved"));
        return null;
    }

    public String deleteTask() {

        FacesContext context = FacesContext.getCurrentInstance();
        task = taskFacade.removeTask(task.getIdTask());
        tasks.remove(task);
        tareas.getTarget().remove(task);
        if (task != null) {

            String delete = "Task " + task.getDescription() + " deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, delete, delete);
            context.addMessage(null, msg);

            return null;
        } else {
            String error = "Task " + task.getDescription()  + " can not be deleted";
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            context.addMessage(null, msg);
        }
        return null;
    }

    public String addlist(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();

        if (descripcion.isEmpty() || descripcion == null || time == 0) {

            context.addMessage(null, new FacesMessage("Advise", "Please chek the empty field"));
            return null;
        }

        Task t = taskFacade.createTask(descripcion, time, this.idProject);
        tareasSource.add(t);

        context.addMessage(null, new FacesMessage("Successful", "Task Created"));
        return null;
    }

    public String modify() {
        FacesContext context = FacesContext.getCurrentInstance();

        Object[] listaId = tareas.getTarget().toArray();
        Object[] sourceTasks = tareas.getSource().toArray();
        taskFacade.setSprint(this.idSprint, listaId, sourceTasks);
        context.addMessage(null, new FacesMessage("Successful", "Change Saved"));

        return null;
    }

    public String returnMain() {
        return "main?faces-redirect=true";
    }
}
