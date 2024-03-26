/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.AIM_BoardTaskListLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class AddTaskBoardWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    AIM_Board board;
    AIM_BoardTask task;

    TextField taskname_field;

    TextArea taskdesc_field;

    ComboBox<GridElement> status_combobox;

    ComboBox<GridElement> assignedmember_combobox;

    Button add_button;

    int newTask;


    /**
     * Constructor
     */
    public AddTaskBoardWindow(AIM_Board board, AIM_BoardTask task){
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        this.board = board;
        this.task = task;
        newTask = 0;
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskname_field = new TextField("Task Name");
        taskname_field.setPlaceholder("Your New Board Task!");
        taskname_field.setWidth("100%");
        taskname_field.setMaxLength(30);
        taskname_field.addClassName("aim-inputfield-bright");

        taskdesc_field = new TextArea("Task Description");
        taskdesc_field.setPlaceholder("Tell me something about that task...");
        taskdesc_field.setWidth("100%");
        taskdesc_field.setMaxLength(150);
        taskdesc_field.addClassName("aim-inputfield-bright");

        status_combobox = new ComboBox<>("Task Status");
        status_combobox.addClassName("aim-inputfield-bright");

        ArrayList<GridElement> statusData = new ArrayList<>();
        statusData.add(new GridElement("NEW"));
        statusData.add(new GridElement("IN PROGRESS"));
        statusData.add(new GridElement("DONE"));

        status_combobox.setItems(statusData);
        status_combobox.setItemLabelGenerator(GridElement::getGridelement_text);

        assignedmember_combobox = new ComboBox<>("Assigned User");
        assignedmember_combobox.addClassName("aim-inputfield-bright");

        ArrayList<GridElement> membersData = new ArrayList<>();

        membersData.add(new GridElement("All"));

        for(Document member: board.board_members){
            membersData.add(new GridElement(member.getString("aim_user_email")));
        }

        membersData.add(new GridElement(AimApplication.loggedUser.aim_user_email));

        assignedmember_combobox.setItems(membersData);
        assignedmember_combobox.setItemLabelGenerator(GridElement::getGridelement_text);

        add_button = new Button("Add Task to Board", VaadinIcon.PLUS.create(),this::addbutton_action);
        add_button.addClassName("aim-button-black");
        add_button.setWidth("100%");

        if ( task!= null){
            // set task data
            taskname_field.setValue(task.aim_task_name);
            taskdesc_field.setValue(task.aim_task_desc);
            status_combobox.setValue(new GridElement(task.status));
            status_combobox.setValue(new GridElement(task.aim_user_assigned.getString("aim_user_email")));
            add_button.setText("Update Task from Board");
        }
        else{
            status_combobox.setValue(new GridElement("NEW"));
            assignedmember_combobox.setValue(new GridElement("All"));
            this.task = new AIM_BoardTask();
            newTask = 1;
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        HorizontalLayout mainhorizontal_layout = new HorizontalLayout();
        mainhorizontal_layout.setSizeFull();
        mainhorizontal_layout.setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout hl_left = new VerticalLayout();
        hl_left.setWidth("50%");hl_left.setHeight("50%");
        hl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_left.getStyle().set("text-align", "center");
        hl_left.add(taskname_field,taskdesc_field);

        VerticalLayout hl_right = new VerticalLayout();
        hl_right.setWidth("50%");hl_left.setHeight("50%");
        hl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_right.getStyle().set("text-align", "center");
        hl_right.add(status_combobox,assignedmember_combobox);

        mainhorizontal_layout.add(hl_left,hl_right);

        main_layout.add(new H3("New Task for "+board.board_name));
        main_layout.add(mainhorizontal_layout);
        main_layout.add(add_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * add_button action
     * @param ex
     */
    private void addbutton_action(ClickEvent ex){
        if (!taskname_field.getValue().isEmpty() && !taskdesc_field.getValue().isEmpty()){

            // fields correct

            // setting task data, name and desc
            task.aim_task_name = taskname_field.getValue();
            task.aim_task_desc = taskdesc_field.getValue();
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);

            // setting status
            task.status = status_combobox.getValue().getGridelement_text();

            // setting assigned user
            try{
                task.aim_user_assigned = dau.getAIMUser(assignedmember_combobox.getValue().getGridelement_text()).prepareDocument();
            }catch(Exception e){
                if ( assignedmember_combobox.getValue().getGridelement_text().equals("All") ){
                    task.aim_user_assigned = null;
                }
            }

            // database
            if ( newTask == 0 ){
                // update task

            }
            else{
                // new task
                Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
                int ans = dab.insertTaskToBoard(board,task);
                if ( ans == 1 ){
                    Notification.show("Task added to board ("+board.board_id.toString()+")");
                    AimApplication.currentBoardTaskList.reloadView(); // updating task view
                    main_dialog.close();
                }
                else{
                    Notification.show("Cannot add task, check log!");
                }
            }
        }
        else{
            Notification.show("Wrong user input!");
        }
    }

}
