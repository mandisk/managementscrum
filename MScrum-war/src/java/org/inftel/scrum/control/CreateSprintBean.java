/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.bean.CreateSprintBaseBean;
import org.inftel.scrum.ejb.ProjectFacade;
import org.inftel.scrum.ejb.SprintFacade;
import org.inftel.scrum.entity.Sprint;

/**
 *
 * @author antonio
 */
@ManagedBean
@RequestScoped
public class CreateSprintBean extends CreateSprintBaseBean {

    @EJB
    private SprintFacade sprintFacade;
    @EJB
    private ProjectFacade projectFacade;

    public CreateSprintBean() {
    }

    @PostConstruct
    public void init() {
        p = projectFacade.find(1);
    }

    public Date getActualDate() {
        Date d = new Date();

        return d;

    }

    public Date getMaxDate() {

        return p.getEndDate();
    }

    public String createNewSprint() {

        int sNumber;

        //Check field is empty
        if (sprintNumber == null || sprintNumber.isEmpty() || initDate == null || endDate == null) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Check the fields again please, because there are some empty fields.", "sprintNumber");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return null;
        } else {
            //Check Date
            if (endDate.compareTo(initDate) < 0) {
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "End date cannot greater than initial date", "EndDate");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }

            sNumber = Integer.parseInt(sprintNumber);
            //Check Sprint Number
            for (Sprint s : p.getSprints()) {

                if (s.getSprintNumber() == sNumber) {
                    msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Sprint Number already exist", "sprintNumber");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    return null;
                }
            }
        }


        System.out.println(p.toString());
        Sprint s = new Sprint(1, sNumber, projectPath, initDate, endDate, null, p);
        sprintFacade.create(s);

        return "main?faces-redirect=true";
    }
}