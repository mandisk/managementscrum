/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.inftel.scrum.entity.Project;
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

    public Task createTask(String description, int time, int idProject) {

        Project project;
        Task task;

        try {

            project = em.find(Project.class, idProject);

            if (project == null) {
                return null;
            }

            task = new Task(-1, 't', description, time, project, null, null);
            em.persist(task);

        } catch (Exception ex) {
            throw new EJBException(ex);
        }

        return task;
    }

    public List<Task> findBySprint(int idSprint) {

        Query query = em.createQuery("select t from Task t where t.sprint.idSprint = :sprint").setParameter("sprint", idSprint);
        List lista = query.getResultList();
        List<Task> listT = (List<Task>) lista;
        return listT;
    }

    public List<Task> findInSprintByState(int idSprint, char state) {
        List<Task> tasks = null;
        
        try {
            tasks = em.createQuery("SELECT t FROM Task t WHERE t.sprint.idSprint = :idSprint AND t.state = :state")
                    .setParameter("idSprint", idSprint)
                    .setParameter("state", state)
                    .getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return tasks;
    }
    
    public List<Task> findTaskNotSprint(int idProject) {

        Query query = em.createQuery("select t from Task t where t.project.idProject = :project and t.sprint is null").setParameter("project", idProject);
        List lista = query.getResultList();
        List<Task> listT = (List<Task>) lista;
        return listT;
    }

    public Task findById(int id) {
        Task t = (Task) em.createQuery("select t from Task t where t.idTask = :idTask").setParameter("idTask", id);
        return t;
    }

    public void setSprint(int idSprint, Object[] idTasks, Object[] sourceTasks) {

        Sprint sprint;

        try {

            sprint = em.find(Sprint.class, idSprint);
            if (sprint != null) {

                for (Object o : idTasks) {
                    String s = (String) o;
                    int idTask = Integer.parseInt(s);

                    Task task = em.find(Task.class, idTask);

                    task.setSprint(sprint);
                }

                for (Object o : sourceTasks) {
                    String s = (String) o;
                    int idTask = Integer.parseInt(s);

                    Task task = em.find(Task.class, idTask);

                    task.setSprint(null);
                    task.setUser(null);
                }
            }
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    public List<Task> findByUserSprint(int user, int sprint) {
        User u = em.find(User.class, user);

        if (u == null) {
            return null;
        }

        List<Task> tasks = em.createQuery("select t from Task t where t.user = :user and t.sprint.idSprint = :sprint").setParameter("user", u).setParameter("sprint", sprint).getResultList();

        return tasks;

//        Query query = em.createQuery("select t from Task t where t.user = :user").setParameter("user", u);
//        List lista = query.getResultList();
//        List<Task> listT = (List<Task>) lista;
//        return listT;
    }

    public Task removeTask(int idTask) {

        Task task;

        try {

            task = em.find(Task.class, idTask);

            if (task != null) {

                em.remove(task);
                return task;
            }

            return null;

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
