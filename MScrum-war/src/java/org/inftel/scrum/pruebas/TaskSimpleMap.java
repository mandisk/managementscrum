/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.pruebas;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author inftel
 */
public class TaskSimpleMap implements TaskFacade {
private Map<String,Tareas> tareas;
public TaskSimpleMap(){
tareas = new HashMap<String,Tareas>();
//String idTask, String tarea, String usuario, String[] peso
Integer[] peso = {16,16,16,16,16,16,16,16,16,4,0};
Integer[] peso2 = {14,14,14,14,14,14,14,14,14,14,14,10};
Integer[] peso3 = {19,7,3,0};
Integer[] peso4 = {30,22,15,5};
Integer[] peso5 = {20,20,10,0};
 addTask(new Tareas("1", "Crear Login", "Antonio", peso));
 addTask(new Tareas("2", "Diseñar Base de datos", "Agustín", peso2));
 addTask(new Tareas("3", "Prototipo  vistas", "Manolo", peso3));
 addTask(new Tareas("4", "Registro usuarios", "Alejandro", peso4));
 addTask(new Tareas("5", "Vista Sprint", "Jorge", peso5));
}

 private void addTask(Tareas tarea) {
    tareas.put(tarea.getIdTask(), tarea);
  }

    @Override
    public Tareas findbyUser(String id) {
       if (id!=null) {
      return(tareas.get(id));
    } else {
      return(null);
    }
    }
}
