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
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

   private final static Logger LOGGER = Logger.getLogger(ChartBean.class .getName());
    // @EJB
    private HistorialTareasFacade historialTareasFacade = null;
   // @EJB
    private TaskFacade taskFacade = null;
   // @EJB
    private ProjectFacade projectFacade = null;
    //private CartesianChartModel categoryModel;
    private TaskSimpleMap tareas = new TaskSimpleMap();
    private TaskSimpleMap tareas2 = new TaskSimpleMap();
    private Tareas tarea, tarea2;
    //Sprint por defecto en sprint.getIdSprint()
    private final int sprint = 1;
    
    
    private CartesianChartModel linearModel;

    public ChartBean() {
         
//        createLinearModel();
    }
    
    @PostConstruct
    public void init() {
         //Carga de las fachadas
         try {
            InitialContext ic = new InitialContext();
            java.lang.Object ejbHome = ic.lookup("java:global/MScrum/MScrum-ejb/TaskFacade");
            taskFacade = (TaskFacade)javax.rmi.PortableRemoteObject.narrow(ejbHome, TaskFacade.class);
            ejbHome = ic.lookup("java:global/MScrum/MScrum-ejb/HistorialTareasFacade");
            historialTareasFacade = (HistorialTareasFacade)javax.rmi.PortableRemoteObject.narrow(ejbHome, HistorialTareasFacade.class);
            ejbHome = ic.lookup("java:global/MScrum/MScrum-ejb/ProjectFacade");
            projectFacade = (ProjectFacade)javax.rmi.PortableRemoteObject.narrow(ejbHome, ProjectFacade.class);
        }
        catch (NamingException e) { // Error al obtener la interfaz de inicio
            LOGGER.severe(e.getMessage());
        }
        createLinearModel();
       
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    private void createLinearModel() {
        linearModel = new CartesianChartModel();
        
         //Extract user data
    // Obtenemos el usuario de la sesión
    FacesContext context = FacesContext.getCurrentInstance();
    Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
    LoginBean loginBean = (LoginBean) sessionMap.get("loginBean");
    String name = loginBean.getUser().getName();
    
    SelectedProjectBean selectedProjectBean = (SelectedProjectBean) sessionMap.get("selectedProjectBean");
    //Obtenemos proyecto de la sesión
//    FacesContext context2 = FacesContext.getCurrentInstance();
//    Map<String, Object> sessionMap2 = context2.getExternalContext().getSessionMap();
//    SelectedProjectBean projectBean = (SelectedProjectBean) sessionMap2.get("selectedProjectBean");
    Project p = projectFacade.findByIdProject(selectedProjectBean.getIdProject());


        LineChartSeries[] series = new LineChartSeries[10];
        int num = 1;
        Collection<User> users1 = p.getUsers();   //Usuarios del mismo proyecto   
        Collection<Sprint> sprints1 = p.getSprints();
        for (Iterator<User> it1 = users1.iterator(); it1.hasNext();) {
            User user = it1.next(); 
            List<Task> findByUser = taskFacade.findByUserSprint(user.getIdUser(), sprint);
            for (Iterator<Task> it2 = findByUser.iterator(); it2.hasNext();) {
                Task task = it2.next();
                List<Integer> listaPesos = historialTareasFacade.findByTask(task.getIdTask());
                //String graph= "series"+String.valueOf(num);
               // series[num].setLabel(name);
            }
        }


//        LineChartSeries series1 = new LineChartSeries();
//        series1.setLabel(name);
//        tarea = tareas.findbyUser("1");
//        Integer[] peso = tarea.getPeso();
//        int tam = tarea.getPeso().length;
//        for (int i = 0; i < tam; i++) {
//            series1.set(i, peso[i]);
//        }
      
        try{
      LineChartSeries series1 = new LineChartSeries();
        series1.setLabel(name);
        tarea = tareas.findbyUser("1");
        Integer[] peso = tarea.getPeso();
        int tam = tarea.getPeso().length;
        for (int i = 0; i < tam; i++) {
            series1.set(i, peso[i]);
        }
       }
       catch(Exception e){
       LOGGER.severe(e.getMessage());
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

        linearModel.addSeries(series[num]);
        linearModel.addSeries(series2);
    }
}
