/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;

import java.util.ArrayList;

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
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        newTaskColumn = new TaskColumnLayout(dat.getNewTaskCollection(),"black","NEW TASKS");
        inProgressTaskColumn = new TaskColumnLayout(dat.getInProgressTaskCollection(),"black","IN PROGRESS");
        doneTaskColumn = new TaskColumnLayout(dat.getDoneTaskCollection(),"black","DONE");

        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){

        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setWidth("80%");
        mainLayout.setHeight("80%");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");
        mainLayout.getStyle().set("margin","75px");
        mainLayout.getStyle().set("background-color","gray");
        mainLayout.getStyle().set("--lumo-font-family","Monospace");

        mainLayout.add(newTaskColumn.columnLayout,inProgressTaskColumn.columnLayout,doneTaskColumn.columnLayout);
    }


}
