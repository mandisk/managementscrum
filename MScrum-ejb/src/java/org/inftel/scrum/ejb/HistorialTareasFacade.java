/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.inftel.scrum.entity.HistorialTareas;

/**
 *
 * @author agustinjf
 */
@Stateless
public class HistorialTareasFacade extends AbstractFacade<HistorialTareas> {
    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public HistorialTareasFacade() {
        super(HistorialTareas.class);
    }
    
    public List<Integer> findByTask(int task){
        Query query = em.createQuery("select ht.hours from HistorialTareas ht where ht.task.idTask = :task").setParameter("task", task);
        List lista = query.getResultList();
        List<Integer> listT = (List<Integer>) lista;
        return listT;
    }
    
    public List<Date> findDateByTask(int task){
        Query query = em.createQuery("select ht.date from HistorialTareas ht where ht.task.idTask = :task").setParameter("task", task);
        List lista = query.getResultList();
        List<Date> listT = (List<Date>) lista;
        return listT;
    }
    
    public List<Long> getStatistics(int idSprint, Date date) {
        
        try {
            String query = "SELECT SUM(h.hours) FROM HistorialTareas h " +
                    "WHERE h.date <= :date AND h.task.sprint.idSprint = :idSprint " +
                    "GROUP BY h.date " +
                    "ORDER BY h.date";
            List<Long> times = em.createQuery(query)
                    .setParameter("date", date)
                    .setParameter("idSprint", idSprint)
                    .getResultList();
            
            return times;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
}
