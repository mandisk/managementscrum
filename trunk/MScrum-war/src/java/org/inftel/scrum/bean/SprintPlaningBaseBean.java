/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import java.io.Serializable;
import java.util.List;
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
    
    //Variables que me pasaran por sesion
    protected int idProject;
    protected int idSprint;
    
  
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


    public List<Task> getTareasSource() {
        return tareasSource;
    }

    public void setTareasSource(List<Task> tareasSource) {
        this.tareasSource = tareasSource;
    }

    public List<Task> getTareasTarget() {
        return tareasTarget;
    }

    public void setTareasTarget(List<Task> tareasTarget) {
        this.tareasTarget = tareasTarget;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(int idSprint) {
        this.idSprint = idSprint;
    }

     
}
