/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows.AddTaskBoardWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.DetailsTaskWindow;

/**
 * Object for showing board task layout data
 */
public class AIM_BoardTaskLayout{

    public VerticalLayout main_layout;

    AIM_BoardTask taskObject;

    AIM_Board boardObject;

    Button showDetails_button;

    Button addtask_button;
    AIM_BoardTaskListLayout currentBoardTaskList;


    /**
     * Constructor
     */
    public AIM_BoardTaskLayout(AIM_BoardTask taskObject, AIM_Board boardObject){
        this.taskObject = taskObject;
        this.boardObject = boardObject;
        main_layout = new VerticalLayout();
        main_layout.addClassName("task-layout");
        prepareLayout();
    }

    /**
     * Function for checking if board task layout is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return taskObject == null;
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        if ( taskObject != null ){
            showDetails_button = new Button("", VaadinIcon.INFO_CIRCLE.create(),this::showdetailsbutton_action);
            showDetails_button.addClassName("aim-button-black");

            Icon statusIcon = VaadinIcon.ERASER.create();
            Icon ownerIcon = VaadinIcon.ERASER.create();

            if ( taskObject.aim_user_assigned == null){
                // task assigned to no one
                ownerIcon = VaadinIcon.CALENDAR_USER.create();
            }
            else if ( taskObject.aim_user_assigned.equals(AimApplication.loggedUser.prepareDocument())){
                // task assigned to logged user
                ownerIcon = VaadinIcon.USER_CHECK.create();
            }
            else {
                // task assigned to other user
                ownerIcon = VaadinIcon.USERS.create();
            }

            switch(taskObject.status){
                case "NEW":
                {
                    statusIcon = VaadinIcon.CHECK_SQUARE_O.create();
                    break;
                }
                case "IN PROGRESS":
                {
                    statusIcon = VaadinIcon.CHECK_SQUARE.create();
                    break;
                }
                case "DONE":
                {
                    statusIcon = VaadinIcon.BAN.create();
                    break;
                }
            }

            statusIcon.getStyle().set("color","white");
            ownerIcon.getStyle().set("color","white");

            FlexLayout left_layout = new FlexLayout();
            left_layout.setSizeFull();
            left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
            left_layout.setAlignItems(FlexComponent.Alignment.START);
            left_layout.add(new H3(taskObject.aim_task_name));

            FlexLayout right_layout = new FlexLayout();
            right_layout.setSizeFull();
            right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            right_layout.setAlignItems(FlexComponent.Alignment.END);
            right_layout.add(showDetails_button);

            HorizontalLayout hl_center = new HorizontalLayout(left_layout,right_layout);

            hl_center.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            hl_center.setWidth("100%");hl_center.setHeight("10%");

            main_layout.add(new HorizontalLayout(statusIcon,ownerIcon),hl_center);

        }
        else{
            addtask_button = new Button("", VaadinIcon.PLUS.create(),this::addtaskbutton_action);
            addtask_button.addClassName("aim-button-black");
            main_layout.add(addtask_button);
        }
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setWidth("100%");
        main_layout.setHeight("100%");
    }

    /**
     * showdetails_button action
     * @param ex
     */
    private void showdetailsbutton_action(ClickEvent ex){
        DetailsTaskWindow dtw = new DetailsTaskWindow(taskObject,boardObject);
        main_layout.add(dtw.main_dialog);
        dtw.main_dialog.open();
    }

    /**
     * addtask_button
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        AddTaskBoardWindow atbw = new AddTaskBoardWindow(boardObject,null);
        main_layout.add(atbw.main_dialog);
        atbw.main_dialog.open();
    }


}
