/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.LoginBaseBean;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;
import org.inftel.scrum.util.Util;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class LoginBean extends LoginBaseBean{
    @EJB
    private UserFacade userFacade;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }
    
    public String doLogin() {
        FacesMessage msg;
        if (email == null || email.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
            msg = new FacesMessage("Empty fields");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        User u = userFacade.findByEmail(email);
        if (u != null) {
            if (u.getPassword().trim().equals(Util.md5(password.trim()).trim())) {
                return "main";
            }
            msg = new FacesMessage("Wrong password");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        }
        msg = new FacesMessage("User not found");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return null;
    }
}
