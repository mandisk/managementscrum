/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author inftel
 */
@ManagedBean
@SessionScoped
public class ChartsBean {

    @EJB
    private TaskFacade taskFacade;
    @EJB
    private UserFacade userFacade;
    private CartesianChartModel linearModel;

    @PostConstruct
    void init() {

        linearModel = new CartesianChartModel();

        List<User> users = userFacade.findAll();
        List<Task> tasks = taskFacade.findAll();
        //ArrayList<LineChartSeries> series = null;

        for (User u : users) {

            LineChartSeries serie = new LineChartSeries();

            if (u != null) {
                
                serie.setLabel(u.getName());
                
                for (Task t : tasks) {

                    if (t != null) {
                        //if (t.getUser().getIdUser() == u.getIdUser()) {
                        if (t.getState() == 'd') {
                            serie.set(t.getIdTask(), t.getTime());
                        }
                        //}
                    }
                }

                linearModel.addSeries(serie);

            }

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
