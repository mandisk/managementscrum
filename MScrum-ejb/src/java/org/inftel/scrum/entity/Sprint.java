/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author agustinjf
 */
@Entity
@Table(name = "sprint")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sprint.findAll", query = "SELECT s FROM Sprint s"),
    @NamedQuery(name = "Sprint.findByIdSprint", query = "SELECT s FROM Sprint s WHERE s.idSprint = :idSprint"),
    @NamedQuery(name = "Sprint.findByProjectPath", query = "SELECT s FROM Sprint s WHERE s.projectPath = :projectPath"),
    @NamedQuery(name = "Sprint.findByInitialDate", query = "SELECT s FROM Sprint s WHERE s.initialDate = :initialDate"),
    @NamedQuery(name = "Sprint.findByEndDate", query = "SELECT s FROM Sprint s WHERE s.endDate = :endDate")})
public class Sprint implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idSprint")
    private Integer idSprint;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sprintNumber")
    private Integer sprintNumber;
    @Size(max = 200)
    @Column(name = "projectPath")
    private String projectPath;
    @Basic(optional = false)
    @NotNull
    @Column(name = "initialDate")
    @Temporal(TemporalType.DATE)
    private Date initialDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @OneToMany(mappedBy = "sprint", cascade= CascadeType.REMOVE,fetch= FetchType.EAGER)
    private Collection<Task> taskCollection;
    @JoinColumn(name = "project", referencedColumnName = "idProject")
    @ManyToOne(optional = false)
    private Project project;

    public Sprint() {
    }

    public Sprint(Integer idSprint) {
        this.idSprint = idSprint;
    }

    public Sprint(Integer idSprint, Date initialDate, Date endDate) {
        this.idSprint = idSprint;
        this.initialDate = initialDate;
        this.endDate = endDate;
    }

    public Sprint(Integer idSprint, Integer sprintNumber, String projectPath, Date initialDate, Date endDate, Collection<Task> taskCollection, Project project) {
        this.idSprint = idSprint;
        this.sprintNumber = sprintNumber;
        this.projectPath = projectPath;
        this.initialDate = initialDate;
        this.endDate = endDate;
        this.taskCollection = taskCollection;
        this.project = project;
    }
    
    

    public Integer getIdSprint() {
        return idSprint;
    }

    public void setIdSprint(Integer idSprint) {
        this.idSprint = idSprint;
    }

    public Integer getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(Integer sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @XmlTransient
    public Collection<Task> getTaskCollection() {
        return taskCollection;
    }

    public void setTaskCollection(Collection<Task> taskCollection) {
        this.taskCollection = taskCollection;
    }
    
    public void addTask(Task task) {
        this.taskCollection.add(task);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSprint != null ? idSprint.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sprint)) {
            return false;
        }
        Sprint other = (Sprint) object;
        if ((this.idSprint == null && other.idSprint != null) || (this.idSprint != null && !this.idSprint.equals(other.idSprint))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Sprint[ idSprint=" + idSprint + " ]";
    }
    
}
