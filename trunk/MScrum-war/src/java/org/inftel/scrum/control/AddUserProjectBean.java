/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.bean.AddUserProjectBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.UserFacade;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.User;
import org.primefaces.model.DualListModel;

/**
 *
 * @author agustinjf
 */
@ManagedBean
@RequestScoped
public class AddUserProjectBean extends AddUserProjectBaseBean{
    
    private final static Logger LOGGER = Logger.getLogger(AddUserProjectBean.class .getName());
    
    @EJB
    private ProjectFacade projectFacade;
    @EJB
    private UserFacade userFacade;
    
    @PostConstruct
    public void init() {
        loadLists();
    }
    private void loadLists() {
        usersSource = new ArrayList<User>();
        usersTarget = new ArrayList<User>();
//        usersSource = userFacade.findAll();
//        usersSource = userFacade.findUsersNotInProject((Project)projectFacade.findById(2));
//        
//        usersTarget = userFacade.findUsersByProject((Project)projectFacade.findById(2));
//        usersTarget = (List<User>)((Project)projectFacade.findById(2).getUsers());
        Project p = projectFacade.find(2);
        usersTarget = (List<User>) p.getUsers();
        usersSource = projectFacade.selectUsersNotIn(2);
        users = new DualListModel<User>(usersSource, usersTarget);
    }

    public void insert() {
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
        projectFacade.AddUsers(2, lista);
//        int i = 0;
    }
    
    public void preRenderView() {
      HttpSession session = ( HttpSession ) FacesContext.getCurrentInstance().getExternalContext().getSession( true );
      //tune session params, eg. session.setMaxInactiveInterval(..);

      //perform other pre-render stuff, like setting user context...
   }
}
