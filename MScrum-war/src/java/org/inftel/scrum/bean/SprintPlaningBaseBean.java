/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.primefaces.model.DualListModel;

/**
 *
 * @author antonio
 */

public class SprintPlaningBaseBean implements Serializable{


   
    //Picklist
    protected DualListModel<Task> tareas;
    protected List<Task> tareasSource;
    protected List<Task> tareasTarget;
    //Atributos Nueva tarea
    protected String descripcion;
    protected int time;
    //Lista de Sprint asociadas al proyecto
    protected  Vector<Integer> sprint;
    //Variables que me pasaran por sesion
    protected Project p;
    protected Sprint s;
    
  
    public SprintPlaningBaseBean() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Task> gettareasSource() {
        return tareasSource;
    }

    public void settareasSourcee(List<Task> tareasSource) {
        this.tareasSource = tareasSource;
    }

    public DualListModel<Task> getTareas() {
        return tareas;
    }

    public void setTareas(DualListModel<Task> tareas) {
        this.tareas = tareas;
    }

     
}
