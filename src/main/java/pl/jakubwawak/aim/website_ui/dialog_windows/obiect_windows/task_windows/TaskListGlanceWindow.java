/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.maintanance.GridElement;
import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class TaskListGlanceWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> task_grid;

    /**
     * Constructor
     */
    public TaskListGlanceWindow(){
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
        task_grid = new Grid<>(GridElement.class,false);
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        ArrayList<AIM_Task> taskCollection = dat.getTaskCollection();
        ArrayList<GridElement> data = new ArrayList<>();
        for(AIM_Task task : taskCollection){
            data.add(new GridElement(task.aim_task_name,0,task.aim_task_timestamp.toString()));
        }

        task_grid.addColumn(GridElement::getGridelement_text).setHeader("Task Name");
        task_grid.addColumn(GridElement::getGridelement_details).setHeader("Time");
        task_grid.addClassName("aim-grid");
        task_grid.setItems(data);
        task_grid.setSizeFull();

        task_grid.addItemClickListener(e->{
            for(GridElement selected : task_grid.getSelectedItems()){
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", selected.getGridelement_text());
                Notification.show(selected.getGridelement_text()+" copied to clipboard!");
                break;
            }
        });
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Your Current Tasks"));
        main_layout.add(task_grid);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}
