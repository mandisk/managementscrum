/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.RegisterBaseBean;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;
import org.inftel.scrum.util.Util;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class RegisterBean extends RegisterBaseBean {
    
    private final static Logger LOGGER = Logger.getLogger(RegisterBean.class.getName());

    @EJB
    private UserFacade userFacade;
    private User user;
       
    /**
     * Creates a new instance of RegisterBean
     */
    public RegisterBean() {
        user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String doRegister() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (name == null || name.trim().isEmpty()
                || surname == null || surname.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || password == null || password.trim().isEmpty()) {

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Check the fields again please because there are some empty fields.", "Fields");
            facesContext.addMessage(null, msg);
            return null;

        } else {

            user = userFacade.findByEmail(email);

            if (user == null) {

                user = userFacade.findByDNI(dni);

                if (user == null) {
                    user = new User(-1, name, surname, Util.md5(password), telephone, dni, email, "0.png");
                    userFacade.create(user);
                    return "index";
                } else {
                    if (facesContext != null) {
                        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Check the field 'DNI' please because it is already been used by other user.", "DNI");
                        facesContext.addMessage(null, msg);
                    }
                }
            } else {
                if (facesContext != null) {
                    FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Check the field 'Email' please because it is already been used by other user.", "Email");
                    facesContext.addMessage(null, msg);
                }
            }
        }
        return null;
    }
    
    public String returnLogin(){        
        return "index";
    }
    
    public void initEJB() {
        
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/UserFacade");
            this.userFacade =
                    (UserFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UserFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
}
