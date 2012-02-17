/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.bean.UserListBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.User;

/**
 *
 * @author agustinjf
 */
@ManagedBean
@SessionScoped
public class UserListBean  extends UserListBaseBean{
    private final static Logger LOGGER = Logger.getLogger(AddUserProjectBean.class .getName());
    
    private ProjectFacade projectFacade = null;
   
    
    private UserFacade userFacade = null;

    /** Creates a new instance of UserListBean */
    public UserListBean() {
        LOGGER.info("Creando UserListBean ...");
    }
    
    @PostConstruct
    public void init () {
        LOGGER.info("Inicializando UserListBean ...");
        
        try {
            InitialContext ic = new InitialContext();
            java.lang.Object ejbHome = ic.lookup("java:global/MScrum/MScrum-ejb/UserFacade");
            userFacade = (UserFacade)javax.rmi.PortableRemoteObject.narrow(ejbHome, UserFacade.class);
            ejbHome = ic.lookup("java:global/MScrum/MScrum-ejb/ProjectFacade");
            projectFacade = (ProjectFacade)javax.rmi.PortableRemoteObject.narrow(ejbHome, ProjectFacade.class);
        }
        catch (NamingException e) { // Error al obtener la interfaz de inicio
            LOGGER.severe(e.getMessage());
        }
    }
            
   
    public String insert() {

        
        if (userFacade == null)
            init();
        LOGGER.info("AddUserProjectBean users.target().tostring = "+users.getTarget().toString());
        Object[] listaId = users.getTarget().toArray();
        Vector<User> lista = new Vector<User>();
        for (int i = 0; i < listaId.length; i++) {
            String sid = (String)listaId[i];
            User user = userFacade.find(Integer.parseInt(sid));
            
            lista.add(user);
        }
        for (Iterator<User> it = lista.iterator(); it.hasNext();) {
            User user = it.next();
            LOGGER.info(user.getName());
        }
        projectFacade.AddUsers(project.getIdProject(), lista);
//        int i = 0;
        return "main?faces-redirect=true";
    }

}
