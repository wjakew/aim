/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;

/**
 * Object for creating current view of tasks
 */
public class CurrentTaskComposer {

    public HorizontalLayout mainLayout;

    public TaskColumnLayout newTaskColumn, inProgressTaskColumn, doneTaskColumn;



    /**
     * Constructor
     */
    public CurrentTaskComposer(){
        mainLayout = new HorizontalLayout();
        mainLayout.addClassName("current-task-composer");
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        newTaskColumn = new TaskColumnLayout(dat.getNewTaskCollection(),"black","NEW TASKS",null,"","");
        inProgressTaskColumn = new TaskColumnLayout(dat.getInProgressTaskCollection(),"black","IN PROGRESS",null,"","");
        doneTaskColumn = new TaskColumnLayout(dat.getDoneTaskCollection(),"black","DONE",null,"","");
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){

        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setSizeFull();
        mainLayout.add(newTaskColumn.columnLayout,inProgressTaskColumn.columnLayout,doneTaskColumn.columnLayout);
    }

    /**
     * Function for updating view
     */
    public void updateLayout(){
        mainLayout.removeAll();
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        newTaskColumn = new TaskColumnLayout(dat.getNewTaskCollection(),"black","NEW TASKS",null,"","");
        inProgressTaskColumn = new TaskColumnLayout(dat.getInProgressTaskCollection(),"black","IN PROGRESS",null,"","");
        doneTaskColumn = new TaskColumnLayout(dat.getDoneTaskCollection(),"black","DONE",null,"","");
        mainLayout.add(newTaskColumn.columnLayout,inProgressTaskColumn.columnLayout,doneTaskColumn.columnLayout);
        Notification.show("Updated current task view!");
    }

}
