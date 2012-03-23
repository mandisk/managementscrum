/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.inftel.scrum.entity.*;

/**
 *
 * @author Jorge, Antonio
 */
@Stateless
public class TaskFacade extends AbstractFacade<Task> {
    
    private static final Logger LOGGER = Logger.getLogger(TaskFacade.class.getName());
    
    private static final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;

    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskFacade() {
        super(Task.class);
    }

    public Task createTask(String description, int time, int idProject, int idSprint) {

        Project project;
        Sprint sprint = null;
        Task task = null;
        HistorialTareas historialTareas;

        try {

            project = em.find(Project.class, idProject);

            if (project == null) {
                return null;
            }
            
            if (idSprint > 0) {
                sprint = em.find(Sprint.class, idSprint);
                
                if (sprint == null) {
                    return null;
                }
            }

            task = new Task(-1, 't', description, time, project, sprint, null);
            em.persist(task);
            
            Date initialDate = sprint.getInitialDate();
            Date endDate = sprint.getEndDate();
            
            LOGGER.info("Initial date: " + initialDate);
            LOGGER.info("End date: " + endDate);
            
            int days = dateDiff(endDate, initialDate);
            
            LOGGER.info("Diferencia de dias: " + days);
            
            for (int i = 0; i < days + 1; i++) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(initialDate);
                calendar.add(Calendar.DAY_OF_MONTH, i);
                
                
                historialTareas = new HistorialTareas(task, calendar.getTime(), task.getTime());
                em.persist(historialTareas);
            }
            
            em.flush();

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
    
    public Task updateTask(int idTask, int idUser, String description, int time, char state) {
        
        Task task;
        User user;
        
        try {
            
            user = em.find(User.class, idUser);
            
            if (user == null) {
                return null;
            }
            
            task = em.find(Task.class, idTask);
            task.setDescription(description);
            task.setState(state);
            task.setTime(time);
            task.setUser(user);
            
            Date today = new Date();
            
            List<HistorialTareas> historialTareasList =
                    em.createQuery("SELECT h FROM HistorialTareas h WHERE h.task = :task AND h.date >= :date")
                    .setParameter("task", task)
                    .setParameter("date", today)
                    .getResultList();
            
            for (HistorialTareas historialTareas : historialTareasList) {
                historialTareas.setHours(time);
            }
            
            em.flush();
            
            return task;
        } catch (NoResultException ex) {
            LOGGER.info(ex.getMessage());
            return null;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    private int dateDiff(Date date1, Date date2) {
        long days = ((date1.getTime() - date2.getTime()) / MILLSECS_PER_DAY);
        
        return (int) days + 1;
    }
}
