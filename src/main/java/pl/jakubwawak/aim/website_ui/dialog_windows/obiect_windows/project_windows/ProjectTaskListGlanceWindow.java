/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;

/**
 * Window for showing tasks connected to project
 */
public class ProjectTaskListGlanceWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<AIM_Task> projectTask_grid;

    AIM_Project projectToShow;

    /**
     * Constructor
     */
    public ProjectTaskListGlanceWindow(AIM_Project projectToShow){
        this.projectToShow = projectToShow;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        projectTask_grid = new Grid<>(AIM_Task.class,false);
        projectTask_grid.addColumn(AIM_Task::getAim_task_name).setHeader("Task Name");
        projectTask_grid.addColumn(AIM_Task::getAim_task_timestamp).setHeader("Timestamp");

        projectTask_grid.setItems(projectToShow.getTaskCollection());
        projectTask_grid.setSizeFull();
        projectTask_grid.addClassName("aim-grid");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("TASK LIST FOR PROJECT "+projectToShow.aim_project_name));
        main_layout.add(projectTask_grid);
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}
