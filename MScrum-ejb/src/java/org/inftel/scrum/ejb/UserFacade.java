/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    public User findByEmail(String email) {
        
        User user;
        
        try {
            user = (User) em.createNamedQuery("User.findByEmail")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }
        
        return user;
    }
    
    public User findByDNI(String dni) {
        
        User user;
        
        try {
            user = (User) em.createNamedQuery("User.findByDni")
                    .setParameter("dni", dni)
                    .getSingleResult();
        } catch (Exception ex) {
            return null;
        }    
        
        return user;
    }
    
    public List<User> findByProject(int idProject) {
        List<User> users = null;
        
        try {
            users = em.createQuery("SELECT p.users FROM Project p WHERE p.idProject = :idProject")
                    .setParameter("idProject", idProject)
                    .getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return users;
    }
    
    public List<User> findNotInProject(int idProject){
        Project project = null;
        List<User> users = null;
        
        try {
            project = em.find(Project.class, idProject);
            
            if (project == null) {
                return null;
            }
            
            users = em.createQuery("select u from User u, Project p where p.idProject = :idProject and u not member of p.users")
                    .setParameter("idProject", idProject)
                    .getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return users;
    }
    
}
