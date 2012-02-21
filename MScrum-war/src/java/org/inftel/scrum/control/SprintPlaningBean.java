/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.SprintPlaningBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Task;
import org.primefaces.model.DualListModel;

/**
 *
 * @author antonio
 */
@ManagedBean
@RequestScoped
public class SprintPlaningBean extends SprintPlaningBaseBean {

    @EJB
    private TaskFacade taskFacade;
    
     
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
    
    public void modify() {
//     
        Object[] listaId = tareas.getTarget().toArray();
        
        taskFacade.setSprint(this.idSprint, listaId);
    }
}
