/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import org.inftel.scrum.bean.RegisterBaseBean;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class RegisterBean extends RegisterBaseBean {

    @EJB
    private UserFacade userFacade;
    private User user;
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Creates a new instance of RegisterBean
     */
    public RegisterBean() {
        user = null;
    }

    public String doRegister() {

        if (name == null || name.trim().isEmpty()
                || surname == null || surname.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Check the fields again please because there are some empty fields.", "Fields");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;

        } else {

            user = userFacade.findByEmail(email);

            if (user == null) {

                user = userFacade.findByDNI(dni);

                if (user == null) {
                    user = new User(-1, name, surname, password, telephone, dni, email, photo);
                    userFacade.create(user);
                    return "index";
                } else {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Check the field 'DNI' please because it is already been used by other user.", "DNI");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return null;
                }
            } else {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Check the field 'Email' please because it is already been used by other user.", "Email");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        }
    }
}
