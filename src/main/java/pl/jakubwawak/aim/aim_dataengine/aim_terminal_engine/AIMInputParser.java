/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.PictureViewerWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.TaskHistoryGlanceWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.TaskListGlanceWindow;

import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Function for parsing user input
 * Based on document: aim_functionality.drawio
 */
public class AIMInputParser {

    public String userInput;

    public ArrayList<String> userInputHistory;

    VerticalLayout secondaryLayout;

    /**
     * Constructor
     */
    public AIMInputParser(VerticalLayout secondaryLayout){
        this.secondaryLayout = secondaryLayout;
        userInputHistory = new ArrayList<>();
    }

    /**
     * Function for setting userInput
     * @param userInput
     */
    public void setUserInput(String userInput){
        this.userInput = userInput;
    }

    /**
     * Function for parsing user input
     */
    public void parse(){
        String[] user_word_collection = userInput.split(" ");
        if ( user_word_collection.length > 0 ){
            switch(user_word_collection[0]){
                case "task":
                {
                    task_mind_creator(userInput);
                    break;
                }
            }
        }
        else{
            response_nonKeyWord();
        }
    }

    /**
     * Function for task operation
     * @param user_input
     */
    public void task_mind_creator(String user_input){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        String[] user_word_collection = user_input.split(" ");
        // task -help
        if ( user_input.contains("-help")){
            PictureViewerWindow pvw = new PictureViewerWindow("images/task_schema.png","Task Terminal Command");
            secondaryLayout.add(pvw.main_dialog);
            pvw.main_dialog.open();
        }
        // task -viewer
        if ( user_input.contains("-viewer") ){
            if ( user_word_collection.length == 2 ){
                TaskListGlanceWindow tgw = new TaskListGlanceWindow();
                secondaryLayout.add(tgw.main_dialog);
                tgw.main_dialog.open();
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
            }
        }
        // task -create
        if ( user_input.contains("-create")){
            // task -create
            if( user_word_collection.length == 2 ){
                InsertTaskWindow itw = new InsertTaskWindow(null);
                secondaryLayout.add(itw.main_dialog);
                itw.main_dialog.open();
            }
            // task -create -n name -d description
            else if (user_word_collection.length > 6){
                AIM_Task task = new AIM_Task();
                task.aim_task_name = StringUtils.substringBetween(user_input,"-n","-d").strip().stripLeading().stripTrailing();
                String []  subarray = IntStream.range(5, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                task.aim_task_desc = String.join(" ",subarray);
                int ans = dat.insertAIMTask(task);
                if (ans == 1){
                    createNotificationResponse("Task "+ task.aim_task_name+ " created!",2);
                    AimApplication.session_ctc.updateLayout();
                }
                else{
                    createNotificationResponse("Failed to create task, check log",1);
                }
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
            }
        }
        // task -link -t task_name -s board/project
        else if (user_input.contains("-link")){
            //todo bug with searching tasks - cannot find data
            if ( user_word_collection.length == 6 ){
                String sourceObject = user_word_collection[5];
                String taskName = StringUtils.substringBetween(user_input,"-t","-s").strip().stripLeading().stripTrailing();
                AIM_Board board = dab.getBoardByName(sourceObject);
                AIM_Project project = dap.getProjectByName(sourceObject);
                AIM_Task task = dat.getTask(taskName);
                if (task!=null){
                    if ( board != null ){
                        // board is selected
                        int ans = dab.linkTaskToBoard(board,task);
                        if ( ans == 1 ){
                            createNotificationResponse("Task linked to board ("+board.board_id.toString()+")",2);
                        }
                        else{
                            createNotificationResponse("Cannot linked, check log!",1);
                        }
                    }
                    else if (project != null){
                        // project is selected
                        int ans = dap.linkTaskProject(project,task);
                        if ( ans == 1 ){
                            createNotificationResponse("Task linked to project ("+project.aim_project_id.toString()+")",2);
                        }
                        else{
                            createNotificationResponse("Cannot linked, check log!",1);
                        }
                    }
                    else{
                        // nothing is selected
                        createNotificationResponse("Cannot find task named "+taskName,3);
                    }
                }
                else{
                    createNotificationResponse("No task named ("+taskName+")",3);
                }
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
            }
        }
        //task -status -t task_name -st new/in progress/done
        else if (user_input.contains("-status")){
            if ( user_word_collection.length >= 5 ){
                String taskName = StringUtils.substringBetween(user_input,"-t","-st");
                String []  subarray = IntStream.range(4, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskStatus = String.join(" ",subarray);
                ArrayList<String> statusCollection = new ArrayList<>();
                statusCollection.add("NEW");
                statusCollection.add("IN PROGRESS");
                statusCollection.add("DONE");
                if (statusCollection.contains(taskStatus)){
                    AIM_Task task = dat.getTask(taskName);
                    if (task!=null){
                        // task found
                        int ans = dat.changeOwnerAIMTask(task,taskStatus);
                        if ( ans == 1 ){
                            createNotificationResponse("Task ("+task.aim_task_id.toString()+") status updated to"+taskStatus,1);
                        }
                        else{
                            createNotificationResponse("No task found with name "+taskName,3);
                        }
                    }
                }
                else{
                    createNotificationResponse("Status "+taskStatus+" is not accepted!",3);
                }
            }
            else{
                createNotificationResponse("Wrong command usage, check -help",3);
            }
        }
        // task -remove -t task_name
        else if (user_input.contains("-remove")){
            String []  subarray = IntStream.range(3, user_word_collection.length)
                    .mapToObj(i -> user_word_collection[i])
                    .toArray(String[]::new);
            String taskName = String.join(" ",subarray);
            if ( user_word_collection.length >= 4 ){
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    String ans = dat.remove(task);
                    if (ans!=null){
                        createNotificationResponse("Removed sources: "+ans,1);
                    }
                    else{
                        createNotificationResponse("Error removing task, check help",2);
                    }
                }
                else{
                    createNotificationResponse("Cannot find task ("+taskName+")",3);
                }
            }
        }
        //task -list
        else if (user_input.contains("-list")){
            if ( user_word_collection.length == 2 ){
                TaskListGlanceWindow tgw = new TaskListGlanceWindow();
                secondaryLayout.add(tgw.main_dialog);
                tgw.main_dialog.open();
            }
            else{
                createNotificationResponse("Wrong input for the task, check help!",3);
            }
        }
        // task -history -t task_name
        else if (user_input.contains("-history")){
            if ( user_word_collection.length >= 3 ){
                String []  subarray = IntStream.range(3, user_word_collection.length)
                        .mapToObj(i -> user_word_collection[i])
                        .toArray(String[]::new);
                String taskName = String.join(" ",subarray);
                AIM_Task task = dat.getTask(taskName);
                if ( task != null ){
                    TaskHistoryGlanceWindow thgw = new TaskHistoryGlanceWindow(task);
                    secondaryLayout.add(thgw.main_dialog);
                    thgw.main_dialog.open();
                }
                else{
                    createNotificationResponse("Cannot find task ("+taskName+")",3);
                }
            }
        }
    }

    /**
     * Function for creating notification response
     * notificationState
     * 1 - error
     * 2 - success
     * 3 - contrast ( information )
     * @param notificationMessage
     * @param notificationState
     */
    void createNotificationResponse(String notificationMessage, int notificationState){

        Notification notification = Notification.show(notificationMessage,5000, Notification.Position.MIDDLE);
        if (notificationState == 1){
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else if (notificationState == 2){
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
        else if (notificationState == 3){
            notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        }
    }

    /**
     * Response function for
     * @return String
     */
    public String response_nonKeyWord(){
        return "nothing to procedure - no key words!";
    }
}
