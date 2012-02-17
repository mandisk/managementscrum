/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.List;
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
    
    public List<Sprint> findByProject(Project project) {
        return em.createQuery("SELECT s FROM Sprint s WHERE s.project = :project")
                .setParameter("project", project)
                .getResultList();
    }   
    
}
