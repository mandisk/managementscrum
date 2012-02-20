/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.ejb;

import java.util.*;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        
        User user = null;
        List<Project> projects = null;
        
        try {
            user = em.find(User.class, idUser);
            
            if (user == null) {
                return null;
            }
            
            projects = em.createQuery("SELECT p FROM Project p WHERE p.finalized = false AND :user MEMBER OF p.users")
                                .setParameter("user", user).getResultList();
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return projects;
    }
    
    public Project findByName(String name) {
        Project p;
        try {
            p = (Project) em.createNamedQuery("Project.findByName")
                    .setParameter("name", name).getSingleResult();
        } catch (NoResultException ex) {
            p = null;
        }
        return p;
    }
    
    public Project createProject(
            String name, 
            String description, 
            Date initialDate,
            Date endDate,
            int idScrumMaster) {
        
        User scrumMaster = null;
        Project project = null;
        
        try {
            
            scrumMaster = em.find(User.class, idScrumMaster);
            
            if (scrumMaster != null) {
            
                project = new Project(-1, name, description, initialDate, endDate, false);
                em.persist(project);
                project.setScrumMaster(scrumMaster);

                scrumMaster.addProject(project);
            }
            
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
        
        return project;
    }
    
    public Project removeProject(int idProject) {
        
        Project project = null;
        
        try {
            
            project = em.find(Project.class, idProject);
            
            if (project != null) {
                
                em.remove(project);
                return project;
            }
            
            return null;
            
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
    
    public Project updateProject(
            int idProject,
            String name,
            String description,
            Date initialDate,
            Date endDate) {
        
        Project project;
        
        try {
            
            project = em.find(Project.class, idProject);
            
            if (project == null) {
                return null;
            }
            
            project.setName(name);
            project.setDescription(description);
            project.setInitialDate(initialDate);
            project.setEndDate(endDate);
            
//            em.merge(project);
//            em.flush();
            
            return project;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }
}
