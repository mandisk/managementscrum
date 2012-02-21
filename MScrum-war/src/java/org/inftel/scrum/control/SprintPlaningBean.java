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
    @EJB
    private SprintFacade sprintFacade;
    @EJB
    private ProjectFacade projectFacade;
    private FacesMessage msg;
    private HtmlDataTable sprintTable;
     SelectedSprintBean selectedSprintBean;
     
    public SprintPlaningBean() {
    }

    @PostConstruct
    void init() {
//         s = (Sprint)   FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("sprint");
//        loadlist();
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> session = context.getExternalContext().getSessionMap();
        SelectedSprintBean selectedSprintBean = 
                (SelectedSprintBean) session.get("selectedSprintBean");
        SelectedProjectBean selectedProjectBean = 
                (SelectedProjectBean) session.get("selectedProjectBean");
        
       tareasSource = taskFacade.findTaskNotSprint(selectedProjectBean.getIdProject());
         tareasTarget = taskFacade.findBySprint(selectedSprintBean.getIdSprint());
        tareas = new DualListModel<Task>(tareasSource, tareasTarget);
        
    }
     
//    public String selectSprint() {
//        s = (Sprint) this.sprintTable.getRowData();
//        
//        return null;
//    }

//    private void loadlist() {
//        tareasSource = new ArrayList<Task>();
//        tareasTarget = new ArrayList<Task>();
//   
       // s = (Sprint) this.sprintTable.getRowData();
        //Esto se debe pasar por sesion <<<<--------------------------------------
      //  p = projectFacade.find(1);
       // s = sprintFacade.find(1);

       // List<Task> lista = taskFacade.findTaskNotSprint(s.getProject().getIdProject());

        //Tareas sin sprint seleccionado
//        for (Iterator<Task> it = lista.iterator(); it.hasNext();) {
//            Task t = it.next();
//            tareasSource.add(t);
//        }
//
//        //Tareas del sprint seleccionado
//       // lista = (List<Task>) s.getTaskCollection();
//        lista = taskFacade.findBySprint(s.getIdSprint());
//        for (Iterator<Task> it = lista.iterator(); it.hasNext();) {
//            Task t = it.next();
//            tareasTarget.add(t);
//        }
//        tareas = new DualListModel<Task>(tareasSource, tareasTarget);

//    }
    public String addlist(ActionEvent actionEvent) {
        FacesContext context = FacesContext.getCurrentInstance();
        
        if (descripcion.isEmpty() || descripcion == null || time == 0) {

            context.addMessage(null, new FacesMessage("Advise", "Please chek the empty field"));
            return null;
        }
        Task t = new Task(1, 't', descripcion, time, p, null, null);
        taskFacade.create(t);
        tareasSource.add(t);
        
        context.addMessage(null, new FacesMessage("Successful", "Task Created"));
        return null;
    }
    
    public void modify() {
        Object[] listaId = tareas.getTarget().toArray();
        Vector<Task> lista = new Vector<Task>();

        for (int i = 0; i < listaId.length; i++) {
            String sid = (String) listaId[i];
            Task t = taskFacade.find(Integer.parseInt(sid));
            lista.add(t);
        }

        taskFacade.setSprint(s, lista);
    }
}
