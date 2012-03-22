/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.inftel.scrum.bean.TaskBaseBean;
import org.inftel.scrum.ejb.TaskFacade;
import org.inftel.scrum.entity.Task;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class TaskBean extends TaskBaseBean{
    
    private static final Logger LOGGER = Logger.getLogger(TaskBean.class.getName());
    
    @EJB
    private TaskFacade taskFacade;
    

    public TaskBean() {
    }
    
    public Task updateTask(int idUser) {
        return taskFacade.updateTask(idTask, idUser, description, time, state);
    }
    
    public void initEJB() {
        try {
            
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome =
                    initialContext.lookup("java:global/MScrum/MScrum-ejb/TaskFacade");
            this.taskFacade =
                    (TaskFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, TaskFacade.class);
        } catch (NamingException e) {
            LOGGER.severe("NamingException: " + e.getMessage());
        }
    }
}
