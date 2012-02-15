/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import com.sun.faces.taglib.html_basic.DataTableTag;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.inftel.scrum.bean.CurrentProjectBaseBean;
import org.inftel.scrum.entity.Project;

/**
 *
 * @author Jorge
 */
@ManagedBean
@RequestScoped
public class CurrentProjectBean extends CurrentProjectBaseBean{

    /**
     * Creates a new instance of CurrentProjectBean
     */
    public CurrentProjectBean() {
    }
    
    public String viewInfo() {
        currentProject = (Project) dataTable.getRowData();
        return null;
    }
}
