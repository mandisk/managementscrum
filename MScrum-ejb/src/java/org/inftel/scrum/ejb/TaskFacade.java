/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;

/**
 *
 * @author Jorge
 */
@Stateless
public class TaskFacade extends AbstractFacade<Task> {

    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskFacade() {
        super(Task.class);
    }

    public List<Task> findBySprint(Sprint sprint) {
        return (List<Task>) em.createQuery("SELECT t FROM Task t WHERE t.sprint = :sprint").setParameter("sprint", sprint).getSingleResult();

    }
}
