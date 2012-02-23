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
import org.inftel.scrum.entity.HistorialTareas;

/**
 *
 * @author agustinjf
 */
@Stateless
public class HistorialTareasFacade extends AbstractFacade<HistorialTareas> {
    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

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
    
}
