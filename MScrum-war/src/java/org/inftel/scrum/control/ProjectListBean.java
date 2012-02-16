/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.scrum.bean.ProjectListBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;

/**
 *
 * @author Jorge
 */
@ManagedBean
@SessionScoped
public class ProjectListBean extends ProjectListBaseBean {
    @EJB
    private ProjectFacade projectFacade;

    /**
     * Creates a new instance of ProjectListBean
     */
    public ProjectListBean() {
    }
    
    public void loadProjects() {
        
    }
}
