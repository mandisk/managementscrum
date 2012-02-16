/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.validation.constraints.NotNull;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author inftel
 */
@ManagedBean
@RequestScoped
public class RegisterBaseBean {

    @NotNull 
    protected String name;
    
    @NotNull
    protected String surname;
    
    @NotNull
    protected String password;
    protected String dni;
        
    protected String email;
    protected String photo;  
    protected int telephone;
   
    /**
    * Creates a new instance of RegisterBaseBean
    */
    public RegisterBaseBean() {
        name=null;
        surname=null;
        password=null;
        email=null;
        photo=null; 
        dni=null;
    }
    
     public int getTelephone() {
        return telephone;
    }

    public void setTelephone(int telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
      
}
