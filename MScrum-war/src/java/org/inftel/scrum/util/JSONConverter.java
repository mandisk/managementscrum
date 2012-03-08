/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.util;

import java.util.List;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge
 */
public class JSONConverter {
    
    private static final String PROJECTS =      formatString("projects");
    private static final String SESSION =       formatString("session");
    
    // Used by Project and User class
    private static final String DESCRIPTION =   formatString("description");
    private static final String EMAIL =         formatString("email");
    private static final String END_DATE =      formatString("enddate");
    private static final String ID =            formatString("id");
    private static final String INITIAL_DATE =  formatString("initialdate");
    private static final String NAME =          formatString("name");
    private static final String PHONE =         formatString("phone");
    private static final String SCRUM_MASTER =  formatString("scrummaster");
    private static final String SURNAME =       formatString("surname");
    
    public static String buildJSONProjectList(String sessionId, List<Project> projectList) {
        StringBuilder builder = new StringBuilder();
        
        String jsonProjectList = projectListToJSONString(projectList);
        
        builder .append("{")
                    .append(SESSION).append(":").append(sessionId).append(",")
                    .append(PROJECTS).append(":").append(jsonProjectList)
                .append("}");
        
        return builder.toString();
    }
            
    /**
     * Convert Project List to JSON String
     * @param projectList
     * @return 
     */
    private static String projectListToJSONString(List<Project> projectList) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("[");
        int size = projectList.size();
        for (int i = 0;  i < size; i++) {
            String jsonProject = projectToJSONString(projectList.get(i));
            builder.append(jsonProject);
            
            if (i < size - 1)
                builder.append(",");
        }
        builder.append("]");
        
        return builder.toString();
    }
    
    /**
     * Convert Project to JSON String
     * @param project
     * @return 
     */
    public static String projectToJSONString(Project project) {
        StringBuilder builder = new StringBuilder();
        
        String idProject = formatString(project.getIdProject().toString());
        String name = formatString(project.getName());
        String description = formatString(project.getDescription());
        String initialDate = String.valueOf(project.getInitialDate().getTime());
        String endDate = String.valueOf(project.getEndDate().getTime());
        String scrumMaster = userToJSONString(project.getScrumMaster());
        
        builder .append("{")
                    .append(ID).append(":").append(idProject).append(",")
                    .append(NAME).append(":").append(name).append(",")
                    .append(DESCRIPTION).append(":").append(description).append(",")
                    .append(INITIAL_DATE).append(":").append(initialDate).append(",")
                    .append(END_DATE).append(":").append(endDate).append(",")
                    .append(SCRUM_MASTER).append(":").append(scrumMaster)
                .append("}");
        
        return builder.toString();
    }
    
    /**
     * Convert User to JSON String
     * @param user
     * @return 
     */
    public static String userToJSONString(User user) {
        StringBuilder builder = new StringBuilder();
        
        String name = formatString(user.getName());
        String surname = formatString(user.getSurname());
        String email = formatString(user.getEmail());
        String phone = formatString(user.getTelephone().toString());
        
        builder .append("{")
                    .append(NAME).append(":").append(name).append(",")
                    .append(SURNAME).append(":").append(surname).append(",")
                    .append(EMAIL).append(":").append(email).append(",")
                    .append(PHONE).append(":").append(phone)
                .append("}");
        
        return builder.toString();
    }
    
    /**
     * Format string to be used in a JSON file
     * @param string
     * @return 
     */
    private static String formatString(String string) {
        return "\"" + string + "\"";
    }
}
