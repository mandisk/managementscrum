/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 *
 * @author agustinjf
 */
@ManagedBean
@SessionScoped
public class AboutUsBean {

    private TagCloudModel model; 
    
    /** Creates a new instance of AboutUsBean */
    public AboutUsBean() {model = new DefaultTagCloudModel();  
        model.addTag(new DefaultTagCloudItem("Agustin", "https://plus.google.com/102442138479670873284/posts", 3));  
        model.addTag(new DefaultTagCloudItem("Jorge", "https://plus.google.com/111152787427229530171/posts", 3));  
        model.addTag(new DefaultTagCloudItem("Antonio", "http://www.facebook.com/profile.php?id=1491805156", 3));  
        model.addTag(new DefaultTagCloudItem("Manuel", "https://plus.google.com/106651815557941198326/posts", 3));  
        model.addTag(new DefaultTagCloudItem("Alejandro", "https://plus.google.com/116039294163832179330/posts", 3));  
    }  
  
    public TagCloudModel getModel() {  
        return model;  
    } 
}
