/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.DetailsTaskWindow;

/**
 * Object for creating layout for presenting AIM_Task data
 * task colors depends on task status
 * NEW #48b869
 * IN PROGRESS #4891b8
 * DONE #a8964f
 */
public class AIM_TaskLayout {
    public VerticalLayout main_layout;

    AIM_Task taskObject;
    AIM_Project projectLinked;

    Button changestatus_button;

    Button showDetails_button;

    /**
     * Constructor
     * @param taskObject
     */
    public AIM_TaskLayout(AIM_Task taskObject){
        main_layout = new VerticalLayout();
        this.taskObject = taskObject;
        prepareLayout();
    }

    /**
     * Constructor with project
     * @param taskObject
     * @param projectLinked
     */
    public AIM_TaskLayout(AIM_Task taskObject, AIM_Project projectLinked){
        main_layout = new VerticalLayout();
        this.taskObject = taskObject;
        this.projectLinked = projectLinked;
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        changestatus_button = new Button("", VaadinIcon.ARROW_RIGHT.create(),this::changestatusbutton_action);
        changestatus_button.getStyle().set("background-color","grey");
        changestatus_button.getStyle().set("color","white");

        showDetails_button = new Button("", VaadinIcon.INFO_CIRCLE.create(),this::showdetailsbutton_action);
        showDetails_button.getStyle().set("background-color","grey");
        showDetails_button.getStyle().set("color","white");

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left_layout.setAlignItems(FlexComponent.Alignment.START);
        left_layout.add(new H6(taskObject.aim_task_name));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.add(showDetails_button,changestatus_button);

        HorizontalLayout hl_center = new HorizontalLayout(left_layout,right_layout);

        hl_center.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_center.setWidth("100%");
        hl_center.addClassNames("py-0", "px-m");

        main_layout.add(hl_center);

        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setWidth("100%");
        main_layout.setHeight("100%");
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");

        if ( taskObject.status.equals("NEW")){
            main_layout.getStyle().set("background-color","#48b869");
        }
        else if ( taskObject.status.equals("IN PROGRESS")){
            main_layout.getStyle().set("background-color","#4891b8");
        }
        else if ( taskObject.status.equals("DONE")){
            main_layout.getStyle().set("background-color","#a8964f");
            changestatus_button.setVisible(false);
        }
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * changestatus_button action
     * @param ex
     */
    private void changestatusbutton_action(ClickEvent ex){
        if (projectLinked == null){
            Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
            int ans = 0;
            String newStatus = "";
            switch(taskObject.status){
                case "NEW":
                {
                    ans = dat.updateAIMTaskStatus(taskObject,"IN PROGRESS");
                    newStatus = "IN PROGRESS";
                    break;
                }
                case "IN PROGRESS":
                {
                    ans = dat.updateAIMTaskStatus(taskObject,"DONE");
                    newStatus = "DONE";
                    break;
                }
            }
            if (ans!=0){
                Notification.show("("+taskObject.aim_task_id.toString()+") set to: "+newStatus);
                AimApplication.session_ctc.updateLayout();
            }
        }
        else{
            Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
            int ans = 0;
            String newStatus = "";
            switch(taskObject.status){
                case "NEW":
                {
                    ans = dap.updateTaskStatus(projectLinked,taskObject,"IN PROGRESS");
                    newStatus = "IN PROGRESS";
                    break;
                }
                case "IN PROGRESS":
                {
                    ans = dap.updateTaskStatus(projectLinked,taskObject,"DONE");
                    newStatus = "DONE";
                    break;
                }
            }
            if (ans!=0){
                Notification.show("("+taskObject.aim_task_name+") set to: "+newStatus);
                AimApplication.session_cpc.updateLayout();
            }
        }

    }

    /**
     * showdetails_button action
     * @param ex
     */
    private void showdetailsbutton_action(ClickEvent ex){
        if ( projectLinked == null ){
            DetailsTaskWindow dtw = new DetailsTaskWindow(taskObject);
            main_layout.add(dtw.main_dialog);
            dtw.main_dialog.open();
        }
        else{
            DetailsTaskWindow dtw = new DetailsTaskWindow(taskObject,projectLinked);
            main_layout.add(dtw.main_dialog);
            dtw.main_dialog.open();
        }
    }
}
