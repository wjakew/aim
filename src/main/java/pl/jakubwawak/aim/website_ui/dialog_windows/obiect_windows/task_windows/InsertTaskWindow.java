/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Window for logging user to the app
 */
public class InsertTaskWindow {

    // variables for setting x and y of window
    public String width = "30%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField taskname_field;
    TextArea taskdesc_field;

    DatePicker taskdeadline_picker;

    AIM_Task taskToUpdate;
    AIM_Project projectToInsert;

    Button addtask_button;



    /**
     * Constructor
     */
    public InsertTaskWindow(AIM_Task taskToUpdate){
        main_dialog = new Dialog();
        this.taskToUpdate = taskToUpdate;
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Constructor with project
     * @param taskToUpdate
     * @param projectToInsert
     */
    public InsertTaskWindow(AIM_Task taskToUpdate, AIM_Project projectToInsert){
        main_dialog = new Dialog();
        this.taskToUpdate = taskToUpdate;
        this.projectToInsert = projectToInsert;
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskname_field = new TextField("Task Name");
        taskname_field.setWidth("100%");
        taskname_field.setPlaceholder("My New Amazing Task");
        taskdeadline_picker = new DatePicker("Task Deadline");
        taskdeadline_picker.setWidth("100%");
        taskdeadline_picker.setPlaceholder("29.11.1996");
        taskdesc_field = new TextArea("Task Description");
        taskdesc_field.setWidth("100%");
        taskdesc_field.setPlaceholder("Tell me something about this task!");
        addtask_button = new Button("",this::addtaskbutton_action);
        if ( taskToUpdate != null ){
            addtask_button.setText("Update Task");
            taskname_field.setValue(taskToUpdate.aim_task_name);
            taskdesc_field.setValue(taskToUpdate.aim_task_desc);
            taskdeadline_picker.setValue(taskToUpdate.aim_task_deadline.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        else{
            addtask_button.setText("Create Task");
        }
        new ButtonStyler().primaryButtonStyle(addtask_button,"100%","");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("New task"));
        main_layout.add(taskname_field,taskdeadline_picker,taskdesc_field,addtask_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * Function for loading task object
     * @return AIM_Task
     */
    AIM_Task loadTaskObject(){
        AIM_Task aimTask = new AIM_Task();
        if ( taskToUpdate!= null ){
            aimTask.aim_task_id = taskToUpdate.aim_task_id;
        }
        aimTask.aim_task_name = taskname_field.getValue();
        aimTask.aim_task_desc = taskdesc_field.getValue();
        aimTask.aim_task_deadline = Date.from(taskdeadline_picker.getValue().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return aimTask;
    }

    /**
     * Function for validating window components
     * @return boolean
     */
    boolean validateWindow(){
        try{
            LocalDate date = taskdeadline_picker.getValue();
            return !taskname_field.getValue().isEmpty() && !taskdesc_field.isEmpty() && date.isAfter(LocalDate.now());
        }catch(Exception ex){
            return false;
        }
    }

    /**
     * addtask_button action
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        if (validateWindow()){
            Database_AIMTask dait = new Database_AIMTask(AimApplication.database);
            Database_AIMProject daip = new Database_AIMProject(AimApplication.database);
            if ( addtask_button.getText().equals("Update Task")){
                // update task without project
                if ( projectToInsert == null ){
                    int ans = dait.updateAIMTask(loadTaskObject());
                    if (ans == 1){
                        Notification.show("Task updated!");
                        AimApplication.session_ctc.updateLayout();
                        main_dialog.close();

                    }
                    else{
                        Notification.show("Error updating task, check log!");
                    }
                }
                // update task in the project
                else{
                    //todo add updating task on project
                }
            }
            else{
                // add task without project
                if ( projectToInsert == null ){
                    int ans = dait.insertAIMTask(loadTaskObject());
                    if ( ans == 1 ){
                        Notification.show("Task added!");
                        AimApplication.session_ctc.updateLayout();
                        main_dialog.close();
                    }
                    else{
                        Notification.show("Error updating task, check log!");
                    }
                }
                else{
                    // add task to the project
                    int ans = daip.insertTaskToProject(projectToInsert,loadTaskObject());
                    if (ans == 1){
                        Notification.show("Task updated!");
                        AimApplication.session_ctc.updateLayout();
                        main_dialog.close();

                    }
                    else{
                        Notification.show("Error updating task, check log!");
                    }
                }
            }
        }
        else{
            Notification.show("Wrong user input. Check window!");
        }
    }
}
