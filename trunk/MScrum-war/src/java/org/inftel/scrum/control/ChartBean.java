/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import org.inftel.scrum.ejb.HistorialTareasFacade;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.inftel.scrum.pruebas.Tareas;
import org.inftel.scrum.pruebas.TaskSimpleMap;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class ChartBean implements Serializable {
    @EJB
    private HistorialTareasFacade historialTareasFacade;
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private ProjectFacade projectFacade;

    //private CartesianChartModel categoryModel;
    private TaskSimpleMap tareas = new TaskSimpleMap();
    private TaskSimpleMap tareas2 = new TaskSimpleMap();
    private Tareas tarea, tarea2;
    //Extract user data
    // Obtenemos el usuario de la sesión
    FacesContext context = FacesContext.getCurrentInstance();
    Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
    LoginBean loginBean = (LoginBean) sessionMap.get("loginBean");
    String name = loginBean.getUser().getName();
    
    //Obtenemos proyecto de la sesión
    SelectedProjectBean projectBean = (SelectedProjectBean) sessionMap.get("SelectedProjectBean");
    int idProject = projectBean.getIdProject();
    Project p = projectFacade.findByIdProject(idProject);
    
    private CartesianChartModel linearModel;
    
    
        
    
     
    public ChartBean() {
        createLinearModel();
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    private void createLinearModel() {
        linearModel = new CartesianChartModel();
        /*
         Collection<User> users1 = p.getUsers();   //Usuarios del mismo proyecto   
     Collection<Sprint> sprints1 = p.getSprints();
            for (Iterator<User> it1 = users1.iterator(); it1.hasNext();) {
                User user = it1.next();
                List<Task> findByUser = taskFacade.findByUserSprint(user.getIdUser(), sprint.getIdSprint());
                for (Iterator<Task> it2 = findByUser.iterator(); it2.hasNext();) {
                    Task task = it2.next();
                    List<Integer> listaPesos = historialTareasFacade.findByTask(task.getIdTask());
                }
            }
         * 
         */

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel(name);
        tarea = tareas.findbyUser("1");
        Integer[] peso = tarea.getPeso();
        int tam = tarea.getPeso().length;
        for (int i = 0; i < tam; i++) {
            series1.set(i, peso[i]);
        }
        /* series1.set(0, peso[0]);
        series1.set(1, peso[1]);
        series1.set(2, peso[2]);
        series1.set(3, peso[3]);*/
        /*series1.set(0, 5);
        series1.set(1, 10);
        series1.set(2, 8);
        series1.set(3, 3);*/

        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Agustín");
        series2.setMarkerStyle("diamond");
        /*tarea2 = tareas2.findbyUser("2");
        Integer[] peso2 = tarea2.getPeso();
        int tam2 = tarea2.getPeso().length;
        for(int j=0; j<tam2; j++){
        series2.set(j, peso[j]);
        }*/
        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);

        linearModel.addSeries(series1);
        linearModel.addSeries(series2);
    }
}
