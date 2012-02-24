/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Task;
import org.primefaces.event.DashboardReorderEvent;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import java.util.logging.Logger;
import org.inftel.scrum.ejb.HistorialTareasFacade;
import org.inftel.scrum.entity.HistorialTareas;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class DashboardBean {
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private HistorialTareasFacade historialTareasFacade;
    
    private final static int TO_DO_COLUMN = 0;
    private final static int DOING_COLUMN = 1;
    private final static int DONE_COLUMN = 2;
    
    private final static char TO_DO_STATE = 't';
    private final static char DOING_STATE = 'i';
    private final static char DONE_STATE = 'd';
    
    private final static Logger LOGGER = Logger.getLogger(DashboardBean.class.getName());
    
    private DashboardModel model;
    private List<Task> todoTaskList;
    private List<Task> doingTaskList;
    private List<Task> doneTaskList;
    
    private Task task;
    private Integer time;

    /**
     * Creates a new instance of DashboardBean
     */
    public DashboardBean() {
    }

    @PostConstruct
    public void init() {

        model = new DefaultDashboardModel();

        DashboardColumn toDo = new DefaultDashboardColumn();
        DashboardColumn doing = new DefaultDashboardColumn();
        DashboardColumn done = new DefaultDashboardColumn();

        model.addColumn(toDo);
        model.addColumn(doing);
        model.addColumn(done);

        loadTaskList();
    }

    public DashboardModel getModel() {
        return model;
    }

    public void setModel(DashboardModel model) {
        this.model = model;
    }

    public List<Task> getDoingTaskList() {
        return doingTaskList;
    }

    public void setDoingTaskList(List<Task> doingTaskList) {
        this.doingTaskList = doingTaskList;
    }

    public List<Task> getDoneTaskList() {
        return doneTaskList;
    }

    public void setDoneTaskList(List<Task> doneTaskList) {
        this.doneTaskList = doneTaskList;
    }

    public List<Task> getTodoTaskList() {
        return todoTaskList;
    }

    public void setTodoTaskList(List<Task> todoTaskList) {
        this.todoTaskList = todoTaskList;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void handleReorder(DashboardReorderEvent event) {
        String widgetId = event.getWidgetId();
        int columnIndex = event.getColumnIndex();

        String[] strings = widgetId.split("_");
        int idTask = Integer.parseInt(strings[strings.length - 1]);

        task = taskFacade.find(idTask);

        LOGGER.info("Moving task: " + task.getDescription() + "...");

        switch (columnIndex) {
            case 0:
                task.setState(TO_DO_STATE);
                break;
            case 1:
                task.setState(DOING_STATE);
                break;
            case 2:
                task.setState(DONE_STATE);
                task.setTime(0);
                insertStatistic();
                break;
        }

        taskFacade.edit(task);

        for (DashboardColumn column : model.getColumns()) {
            column.getWidgets().clear();
        }

        loadTaskList();

        LOGGER.info(task.getDescription() + " moved");
    }

    public String modify() {

        task.setTime(time);

        if (time <= 0) {
            task.setState(DONE_STATE);
        }

        taskFacade.edit(task);
        insertStatistic();

        for (DashboardColumn column : model.getColumns()) {
            column.getWidgets().clear();
        }

        loadTaskList();
        time = null;
        
        return null;
    }

    private void loadTaskList() {

        // Get selected sprint
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        SelectedSprintBean selectedSprintBean = (SelectedSprintBean) sessionMap.get("selectedSprintBean");

        int idSprint = selectedSprintBean.getIdSprint();

        todoTaskList = taskFacade.findInSprintByState(idSprint, TO_DO_STATE);
        doingTaskList = taskFacade.findInSprintByState(idSprint, DOING_STATE);
        doneTaskList = taskFacade.findInSprintByState(idSprint, DONE_STATE);

        for (Task task : todoTaskList) {
            model.getColumn(TO_DO_COLUMN).addWidget("t_" + task.getIdTask());
        }

        for (Task task : doingTaskList) {
            model.getColumn(DOING_COLUMN).addWidget("t_" + task.getIdTask());
        }

        for (Task task : doneTaskList) {
            model.getColumn(DONE_COLUMN).addWidget("t_" + task.getIdTask());
        }
    }
    
    private void insertStatistic() {
        
        HistorialTareas historialTareas = new HistorialTareas();
        
        historialTareas.setDate(new Date());
        historialTareas.setHours(task.getTime());
        historialTareas.setTask(task);
        
        historialTareasFacade.create(historialTareas);
    }
}
