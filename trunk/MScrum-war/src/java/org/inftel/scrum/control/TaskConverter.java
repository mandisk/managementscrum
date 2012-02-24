/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
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
        Integer id = Integer.valueOf(value);
        try {
            return taskFacade.find(id);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(String.format("Cannot convert %s to Task", value)), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Task task = (Task) value;
        return String.valueOf(task.getIdTask());
    }
}
