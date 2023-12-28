/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;

/**
 * Window for logging user to the app
 */
public class CTaskListGlanceWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    Grid<AIM_CodingTask> grid;

    /**
     * Constructor
     */
    public CTaskListGlanceWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        grid = new Grid<>(AIM_CodingTask.class,false);
        grid.addColumn(AIM_CodingTask::getAim_codingtask_name).setHeader("Coding Tasks");
        grid.setSizeFull();
        Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
        grid.setItems(dact.getCodingTaskList());

        grid.addItemClickListener(event ->{
            AIM_CodingTask selected = event.getItem();
            InsertCTaskWindow ictw = new InsertCTaskWindow(selected);
            main_layout.add(ictw.main_dialog);
            ictw.main_dialog.open();
        });

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(new H6("CODING TASK VIEWER"));
        main_layout.add(grid);

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
