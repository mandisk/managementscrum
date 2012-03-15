/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.Collection;
import java.util.Date;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import javax.persistence.PersistenceContext;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge
 */
@Stateless
public class SprintFacade extends AbstractFacade<Sprint> {

    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SprintFacade() {
        super(Sprint.class);
    }
    
    public Collection<Sprint> findByProject(int idProject) {
        Collection<Sprint> sprints = null;
        Project project;
        try {
            
            project = em.find(Project.class, idProject);
            
            if (project == null) {
                return null;
            }
            
            sprints = em.createQuery("SELECT s FROM Sprint s WHERE s.project = :project ORDER BY s.sprintNumber")
                    .setParameter("project", project)
                    .getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return sprints;
    }
    
    public Sprint removeSprint(int idSprint) {
        
        Sprint sprint;
        
        try {
            
            sprint = em.find(Sprint.class, idSprint);
            
            if (sprint != null) {
                
                em.remove(sprint);
                return sprint;
            }
            
            return null;
            
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    public Sprint createSprint(
            int sprintNumber, 
            Date initialDate, 
            Date endDate, 
            String projectPath,
            int idProject) {
        
        Project project;
        Sprint sprint;
        
        try {
            
            project = em.find(Project.class, idProject);
            
            if (project == null) {
                return null;
            }
            
            try {
                sprint = (Sprint) em.createQuery("SELECT s FROM Sprint s WHERE s.project = :project AND s.sprintNumber = :sprintNumber")
                        .setParameter("project", project)
                        .setParameter("sprintNumber", sprintNumber)
                        .getSingleResult();    
            } catch (NoResultException ex) { 
                sprint = null;
            }
            
            if (sprint != null) {
                return null;
            }
            
            sprint = new Sprint(-1, sprintNumber, projectPath, initialDate, endDate, null, project);
            em.persist(sprint);
            
            em.flush();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return sprint;
    }
}
