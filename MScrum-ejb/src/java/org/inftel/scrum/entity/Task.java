/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author agustinjf
 */
@Entity
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findByIdTask", query = "SELECT t FROM Task t WHERE t.idTask = :idTask"),
    @NamedQuery(name = "Task.findByState", query = "SELECT t FROM Task t WHERE t.state = :state"),
    @NamedQuery(name = "Task.findByDescription", query = "SELECT t FROM Task t WHERE t.description = :description"),
    @NamedQuery(name = "Task.findByTime", query = "SELECT t FROM Task t WHERE t.time = :time")})
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTask")
    private Integer idTask;
    @Basic(optional = false)
    @NotNull
    @Column(name = "state")
    private char state;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Column(name = "time")
    private int time;
    @JoinColumn(name = "project", referencedColumnName = "idProject")
    @ManyToOne(optional = false)
    private Project project;
    @JoinColumn(name = "sprint", referencedColumnName = "idSprint")
    @ManyToOne
    private Sprint sprint;
    @JoinColumn(name = "user", referencedColumnName = "idUser")
    @ManyToOne
    private User user;
    

    public Task() {
    }

    public Task(Integer idTask) {
        this.idTask = idTask;
    }

    public Task(Integer idTask, char state, String description, int time) {
        this.idTask = idTask;
        this.state = state;
        this.description = description;
        this.time = time;
    }

    public Task(Integer idTask, char state, String description, int time, Project project, Sprint sprint, User user) {
        this.idTask = idTask;
        this.state = state;
        this.description = description;
        this.time = time;
        this.project = project;
        this.sprint = sprint;
        this.user = user;
    }

    public Integer getIdTask() {
        return idTask;
    }

    public void setIdTask(Integer idTask) {
        this.idTask = idTask;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTask != null ? idTask.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.idTask == null && other.idTask != null) || (this.idTask != null && !this.idTask.equals(other.idTask))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idTask.toString();
    }
    
}
