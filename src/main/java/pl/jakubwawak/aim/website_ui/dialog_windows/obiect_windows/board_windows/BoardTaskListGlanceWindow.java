/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;

/**
 * Window for showing tasks connected to project
 */
public class BoardTaskListGlanceWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<AIM_BoardTask> boardTask_grid;

    AIM_Board boardToShow;

    /**
     * Constructor
     */
    public BoardTaskListGlanceWindow(AIM_Board boardToShow){
        this.boardToShow = boardToShow;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        boardTask_grid = new Grid<>(AIM_BoardTask.class,false);
        boardTask_grid.addColumn(AIM_BoardTask::getAim_task_name).setHeader("Task Name");
        boardTask_grid.addColumn(AIM_BoardTask::getAim_task_timestamp).setHeader("Timestamp");
        boardTask_grid.addColumn(AIM_BoardTask::getAssignedUserGlance).setHeader("Assigned User");

        boardTask_grid.setItems(boardToShow.getTaskList());
        boardTask_grid.setSizeFull();

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("TASK LIST FOR BOARD "+boardToShow.board_name));
        main_layout.add(boardTask_grid);
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
}
