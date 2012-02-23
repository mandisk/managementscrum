/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.scrum.ejb.HistorialTareasFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author inftel
 */
@ManagedBean
@SessionScoped
public class ChartsBean implements Serializable {
    
    @EJB
    private HistorialTareasFacade historialTareasFacade;
    @EJB
    private TaskFacade taskFacade;
    @EJB
    private UserFacade userFacade;
    private CartesianChartModel linearModel;
    private Long maxDate = new Date().getTime();
    
    public Long getMaxDate() {
        return maxDate;
    }
    
    public void setMaxDate(Long maxDate) {
        this.maxDate = maxDate;
    }
    
    @PostConstruct
    void init() {
        
        linearModel = new CartesianChartModel();
        
        List<User> users = userFacade.findAll();
        List<Task> tasks = taskFacade.findAll();
        ArrayList<LineChartSeries> series = new ArrayList<LineChartSeries>();

//        for (User u : users) {
//            
//            LineChartSeries serie = new LineChartSeries();
//
//            if (u != null) {
//
//                serie.setLabel(u.getName());
//                List<Task> tareas = (List<Task>) u.getAssignedTasks();
//                int sum=0;
////                for (Task task : tareas) {
////                    sum+=task.getTime();
////                }
//                for (Task t : tasks) {
//                    
//                    if (t != null) {
//                        if (t.getUser().getIdUser() == u.getIdUser()) {
//                            if (t.getState() == 'd') {
////                                System.out.println(u.getName());
////                                System.out.println(t.getDescription());
//                                
//                                List<Integer> horas = historialTareasFacade.findByTask(t.getIdTask());
//                                List<Date> dates = historialTareasFacade.findDateByTask(t.getIdTask());
//                                int total = dates.size();
//                                for (int i = 0; i < total; i++) {
////                                    System.out.println(dates.get(i) + " : " + horas.get(i));
//                                    DateFormat dataformat = DateFormat.getDateInstance(DateFormat.LONG);
//                                    String s4 = dataformat.format(dates.get(i));
////                                    System.out.println(s4);
////                                    serie.set(dates.get(i).getTime(), horas.get(i));
//                                    serie.set(s4, horas.get(i));
//                                }
////                                serie.set(t.getIdTask(), t.getTime());
//                            }
//                        }
//                    }
//                }
//
//                series.add(serie);
//
//            }
//
//        }

        for (User u : users) {
            LineChartSeries serie = new LineChartSeries();
            List<Integer> horas = new ArrayList<Integer>();
            List<Date> dates = new ArrayList<Date>();
            ArrayList<Integer> totales = null;
            if (u != null) {
                
                serie.setLabel(u.getName());
                
                totales = new ArrayList<Integer>();
                int ini = 0;
                
                List<Task> tareas = (List<Task>) u.getAssignedTasks();
                for (Task t : tareas) {
                    horas = historialTareasFacade.findByTask(t.getIdTask());
                    dates = historialTareasFacade.findDateByTask(t.getIdTask());
                    if (ini == 0) {
                        for (Integer h : horas) {
                            totales.add(h);
                        }
                        ini = 1;
                    } else {
                        for (int i = 0; i < totales.size(); i++) {
                            totales.set(i, totales.get(i) + horas.get(i));
                        }
                    }                    
                }
                
                int total = dates.size();
                for (int i = 0; i < total; i++) {
                    
                    DateFormat dataformat = DateFormat.getDateInstance(DateFormat.LONG);
                    String s4 = dataformat.format(dates.get(i));
                    
                    serie.set(s4, totales.get(i));
                }
                series.add(serie);
            }
        }
        
        for (Iterator<LineChartSeries> it = series.iterator(); it.hasNext();) {
            LineChartSeries lineChartSeries = it.next();
            linearModel.addSeries(lineChartSeries);
        }
        
    }
//        for (User u : users) {
//            series.add(new LineChartSeries());
//        }
//        int i = 0;
//        
//        Iterator<LineChartSeries> itL = series.iterator();
//        Iterator<User> itU = users.iterator();
//        
//        while (itU.hasNext() && itL.hasNext()) {
//            
//            i++;
//            User u = itU.next();
//            LineChartSeries serie = itL.next();
//            
//            if (u != null) {
//
//                serie.setLabel(u.getName());
//
//                for (Task t : tasks) {
//                    if (t != null) {
//                        if (t.getUser().getIdUser() == u.getIdUser()) {
//                            if (t.getState() == 'd') {
//                                serie.set(t.getIdTask(), t.getTime());
//                            }
//                        }
//
//                    }
//                }
//                series.set(i, serie);
//                linearModel.addSeries(series.get(i));
//            }

    /**
     * Creates a new instance of StadisticsBean
     */
    public ChartsBean() {
    }
    
    public CartesianChartModel getLinearModel() {
        return linearModel;
    }
    
    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }
}
