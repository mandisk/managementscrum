/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.pruebas;

/**
 *
 * @author inftel
 */
public class Tareas {

    private final String tarea, usuario;
    private final String idTask;
    private final Integer[] peso;

    public Tareas(String idTask, String tarea, String usuario, Integer[] peso) {
        this.idTask = idTask;
        this.tarea = tarea;
        this.usuario = usuario;
        this.peso = peso;
    }

    public String getIdTask() {
        return idTask;
    }

    public Integer[] getPeso() {
        return peso;
    }

    public String getTarea() {
        return tarea;
    }

    public String getUsuario() {
        return usuario;
    }
    
}
