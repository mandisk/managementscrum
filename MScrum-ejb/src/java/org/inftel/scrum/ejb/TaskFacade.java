/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge, Antonio
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
    
    public List<Task> findTaskNotSprint(int idProject) {

        Query query = em.createQuery("select t from Task t where t.project.idProject = :project and t.sprint is null").setParameter("project", idProject);
        List lista = query.getResultList();
        List<Task> listT = (List<Task>) lista;
        return listT;
    }

    public void setSprint(Sprint s, List<Task> tasks) {
        
        Sprint saux = em.find(Sprint.class, s.getIdSprint());

        for (Task t : tasks) {
           
            Task taux = em.find(Task.class, t.getIdTask());
            taux.setSprint(saux);
        }

        em.flush();
    }
    
    public List<Task> findByUserSprint(int user, int sprint){
        User u = em.find(User.class, user);
        
        if (u == null) {
            return null;
        }
        
        List<Task> tasks = em.createQuery("select t from Task t where t.user = :user and t.sprint = :sprint")
                .setParameter("user", u)
                .setParameter("sprint", sprint)
                .getResultList();
        
        return tasks;
        
//        Query query = em.createQuery("select t from Task t where t.user = :user").setParameter("user", u);
//        List lista = query.getResultList();
//        List<Task> listT = (List<Task>) lista;
//        return listT;
    }
}
