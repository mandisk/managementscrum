/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Task;

/**
 *
 * @author agustinjf
 */
@ManagedBean
@RequestScoped
public class TaskConverter implements Converter{
    @EJB
    private TaskFacade taskFacade;
    

    /** Creates a new instance of TaskConverter */
    public TaskConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return taskFacade.find(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Task)value).getIdTask().toString();
    }
}
