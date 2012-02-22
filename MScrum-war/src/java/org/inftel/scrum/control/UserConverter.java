/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
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
        Integer id = Integer.valueOf(value);
        System.out.println("id: "+ id);
        try {
            System.out.println("value: "+value);
            return userFacade.find(id);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(String.format("Cannot convert %s to User", value)), e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        User user = (User) value;
        return String.valueOf(user.getIdUser());
    }
}
