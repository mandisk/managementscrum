/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author agustinjf
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByIdUser", query = "SELECT u FROM User u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
    @NamedQuery(name = "User.findBySurname", query = "SELECT u FROM User u WHERE u.surname = :surname"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByDni", query = "SELECT u FROM User u WHERE u.dni = :dni"),
    @NamedQuery(name = "User.findByPhoto", query = "SELECT u FROM User u WHERE u.photo = :photo"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByTelephone", query = "SELECT u FROM User u WHERE u.telephone = :telephone")})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "idUser")
    private Integer idUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Size(max = 45)
    @Column(name = "surname")
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "dni")
    private String dni;
    @Size(max = 200)
    @Column(name = "photo")
    private String photo;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private Integer telephone;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="scrum_team",
            joinColumns={@JoinColumn(name="idUser", referencedColumnName="idUser")},
      inverseJoinColumns={@JoinColumn(name="idProject", referencedColumnName="idProject")})
    private Collection<Project> projects;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="scrumMaster")
    private Collection<Project> managedProjects;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private Collection<Task> assignedTasks;
    
    public User() {
    }

    public User(Integer idUser) {
        this.idUser = idUser;
    }

    public User(Integer idUser, String name, String surname, String password, int telephone, String dni, String email, String photo) {
        this.idUser = idUser;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.dni = dni;
        this.email = email;
        this.photo = photo;
        this.telephone = telephone;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTelephone() {
        return telephone;
    }

    public void setTelephone(Integer telephone) {
        this.telephone = telephone;
    }
    
    public Collection<Project> getProjects() {
        return projects;
    }
    
    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }
    
    public Collection<Project> getManagedProjects() {
        return managedProjects;
    }
    
    public void setManajedProjects(Collection<Project> managedProjects) {
        this.managedProjects = managedProjects;
    }

    public Collection<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(Collection<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idUser.toString();
    }
    
}
