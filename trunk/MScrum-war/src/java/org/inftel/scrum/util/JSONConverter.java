/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.util;

import java.util.List;
import org.inftel.scrum.entity.Project;
import org.inftel.scrum.entity.Sprint;
import org.inftel.scrum.entity.Task;
import org.inftel.scrum.entity.User;

/**
 *
 * @author Jorge
 */
public class JSONConverter {
    
    private static final String PROJECTS =              formatString("projects");
    private static final String SESSION =               formatString("session");
    private static final String SPRINTS =               formatString("sprints");
    private static final String TASKS =                 formatString("tasks");
    private static final String USERS =                 formatString("users");
    private static final String USERS_NOT_IN_PROJECT =  formatString("usersnotinproject");
    
    private static final String DESCRIPTION =           formatString("description");
    private static final String EMAIL =                 formatString("email");
    private static final String END_DATE =              formatString("enddate");
    private static final String ID =                    formatString("id");
    private static final String ID_PROJECT =            formatString("idproject");
    private static final String INITIAL_DATE =          formatString("initialdate");
    private static final String NAME =                  formatString("name");
    private static final String PHONE =                 formatString("phone");
    private static final String SCRUM_MASTER =          formatString("scrummaster");
    private static final String SURNAME =               formatString("surname");
    private static final String SPRINT_NUMBER =         formatString("sprintnumber");
    private static final String STATE =                 formatString("state");
    private static final String TIME =                  formatString("time");
    private static final String USER =                  formatString("user");
    
    /**
     * Build Project List and Session id JSON String to send to client
     * @param sessionId
     * @param projectList
     * @return 
     */
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
     * Build Sprint List JSON String to send to client
     * @param sprintList
     * @return 
     */
    public static String buildJSONSprintList(List<Sprint> sprintList) {
        StringBuilder builder = new StringBuilder();
        
        String jsonSprintList = sprintListToJSONString(sprintList);
        
        builder .append("{")
                    .append(SPRINTS).append(":").append(jsonSprintList)
                .append("}");
        
        return builder.toString();
    }
    
    /**
     * Build Task List JSON String to send to client
     * @param taskList
     * @return 
     */
    public static String buildJSONTaskList(List<Task> taskList) {
        StringBuilder builder = new StringBuilder();
        
        String jsonTaskList = taskListToJSONString(taskList);
        
        builder .append("{")
                    .append(TASKS).append(":").append(jsonTaskList)
                .append("}");
        
        return builder.toString();
    }
    
    public static String buildJSONUserList(List<User> userList) {
        return buildJSONUserList(userList, null);
    }
    
    /**
     * Build User List JSON String to send to client
     * @param userList
     * @return 
     */
    public static String buildJSONUserList(List<User> userList, List<User> userListNotInProject) {
        StringBuilder builder = new StringBuilder();
        
        String jsonUserList = userListToJSONString(userList);
        
        builder .append("{")
                    .append(USERS).append(":").append(jsonUserList);
        if (userListNotInProject != null) {
            builder .append(",")
                    .append(USERS_NOT_IN_PROJECT).append(":").append(userListToJSONString(userListNotInProject));
        }
        builder.append("}");
        
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
     * Convert Sprint List to JSON String
     * @param sprintList
     * @return 
     */
    private static String sprintListToJSONString(List<Sprint> sprintList) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("[");
        int size = sprintList.size();
        for (int i = 0;  i < size; i++) {
            String jsonSprint = sprintToJSONString(sprintList.get(i));
            builder.append(jsonSprint);
            
            if (i < size - 1)
                builder.append(",");
        }
        builder.append("]");
        
        return builder.toString();
    }
    
    /**
     * Convert Task List to JSON String
     * @param taskList
     * @return 
     */
    private static String taskListToJSONString(List<Task> taskList) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("[");
        int size = taskList.size();
        for (int i = 0;  i < size; i++) {
            String jsonTask = taskToJSONString(taskList.get(i));
            builder.append(jsonTask);
            
            if (i < size - 1)
                builder.append(",");
        }
        builder.append("]");
        
        return builder.toString();
    }
    
    /**
     * Convert User List to JSON String
     * @param userList
     * @return 
     */
    private static String userListToJSONString(List<User> userList) {
        StringBuilder builder = new StringBuilder();
        
        builder.append("[");
        int size = userList.size();
        for (int i = 0;  i < size; i++) {
            String jsonUser = userToJSONString(userList.get(i));
            builder.append(jsonUser);
            
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
        String initialDate = formatString(String.valueOf(project.getInitialDate().getTime()));
        String endDate = formatString(String.valueOf(project.getEndDate().getTime()));
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
        
        String idUser = formatString(user.getIdUser().toString());
        String name = formatString(user.getName());
        String surname = formatString(user.getSurname());
        String email = formatString(user.getEmail());
        String phone = formatString(user.getTelephone().toString());
        
        builder .append("{")
                    .append(ID).append(":").append(idUser).append(",")
                    .append(NAME).append(":").append(name).append(",")
                    .append(SURNAME).append(":").append(surname).append(",")
                    .append(EMAIL).append(":").append(email).append(",")
                    .append(PHONE).append(":").append(phone)
                .append("}");
        
        return builder.toString();
    }
    
    /**
     * Convert Sprint to JSON String
     * @param sprint
     * @return 
     */
    public static String sprintToJSONString(Sprint sprint) {
        StringBuilder builder = new StringBuilder();
        
        String id = formatString(sprint.getIdSprint().toString());
        String sprintNumber = formatString(sprint.getSprintNumber().toString());
        String initialDate = formatString(String.valueOf(sprint.getInitialDate().getTime()));
        String endDate = formatString(String.valueOf(sprint.getEndDate().getTime()));
        String idProject = formatString(sprint.getProject().getIdProject().toString());
        
        builder .append("{")
                    .append(ID).append(":").append(id).append(",")
                    .append(SPRINT_NUMBER).append(":").append(sprintNumber).append(",")
                    .append(INITIAL_DATE).append(":").append(initialDate).append(",")
                    .append(END_DATE).append(":").append(endDate).append(",")
                    .append(ID_PROJECT).append(":").append(idProject) // No definitivo
                .append("}");
        
        return builder.toString();
    }
    
    /**
     * Convert Task to JSON String
     * @param task
     * @return 
     */
    public static String taskToJSONString(Task task) {
        StringBuilder builder = new StringBuilder();
        
        String id = formatString(task.getIdTask().toString());
        String state = formatString(String.valueOf(task.getState()));
        String description = formatString(task.getDescription());
        String time = formatString(String.valueOf(task.getTime()));
        String user = formatString("null");
        
        if (task.getUser() != null)
             user = userToJSONString(task.getUser());
        
        builder .append("{")
                    .append(ID).append(":").append(id).append(",")
                    .append(STATE).append(":").append(state).append(",")
                    .append(DESCRIPTION).append(":").append(description).append(",")
                    .append(TIME).append(":").append(time).append(",")
                    .append(USER).append(":").append(user)
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
