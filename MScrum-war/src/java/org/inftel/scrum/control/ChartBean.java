/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;  
import org.primefaces.model.chart.LineChartSeries;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.inftel.scrum.pruebas.Tareas;
import org.inftel.scrum.pruebas.TaskSimpleMap;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class ChartBean implements Serializable {

    //private CartesianChartModel categoryModel;
    private TaskSimpleMap tareas = new TaskSimpleMap();
    private Tareas tarea,tarea2;

    private CartesianChartModel linearModel;

	public ChartBean() {
        createLinearModel();
	}

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }


    private void createLinearModel() {
        linearModel = new CartesianChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Antonio");
        tarea = tareas.findbyUser("1");
        Integer[] peso = tarea.getPeso();
        int tam = tarea.getPeso().length;
        for(int i=0; i<tam; i++){
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
      /* tarea2 = tareas.findbyUser("2");
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
                    