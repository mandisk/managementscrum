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
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;

/**
 *
 * @author agustinjf
 */
@ManagedBean
@RequestScoped
public class UserConverter implements Converter{
    @EJB
    private UserFacade userFacade;

    /** Creates a new instance of UserConverter */
    public UserConverter() {
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        User u = (User)userFacade.find(Integer.parseInt(value));
        return u;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((User)value).getIdUser().toString();
    }
}
