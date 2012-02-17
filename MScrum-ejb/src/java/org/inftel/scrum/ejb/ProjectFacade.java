/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {
    @EJB
    private UserFacade userFacade;
    private final static Logger LOGGER = Logger.getLogger(ProjectFacade.class .getName());

    @PersistenceContext(unitName = "MScrum-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }
    
    public void AddUsers(int project, Vector<User> usuarios) {
        
        
        em.createNativeQuery("delete from scrum_team where idproject="+project).executeUpdate();
        Project p = em.find(Project.class, project);
//        p.getUsers().clear();
//        em.merge(p);
//        em.flush();
        LOGGER.info("ProjectFacade: " + p.getName() + " --- "+ usuarios);
        for (Iterator<User> it = usuarios.iterator(); it.hasNext();) {
            User user = it.next();
            User u = userFacade.find(user.getIdUser());
//            p.getUsers().add(u);
////            user.getProjects().add(p);
            em.createNativeQuery("insert into scrum_team values("+project+","+u.getIdUser()+")").executeUpdate();
////            Collection<Project> lista = user.getProjects();
////            for (Iterator<Project> it1 = lista.iterator(); it1.hasNext();) {
////                Project project1 = it1.next();
////                LOGGER.info(project1.getName().toString());
////            }
        }
////        p.setUsers(u);
//        
//        em.merge(p);
//        em.flush();
    }
    
    public List<User> selectUsersNotIn(int project){
        Project pro = em.find(Project.class, project);
        LOGGER.info(pro.getName());
        
        return em.createQuery("select u from User u, Project p where p.idProject = :project and u not member of p.users").setParameter("project", project).getResultList();
    }
    
    public List<Project> findActiveProjectByUser(int idUser) {
        User u = (User) em.find(User.class, idUser);
        return em.createQuery("SELECT p FROM Project p WHERE p.finalized = false AND :user MEMBER OF p.users")
                .setParameter("user", u).getResultList();
    }
}
