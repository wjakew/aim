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
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class ProjectHistoryGlanceWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> taskhistory_grid;

    AIM_Project projectToShow;

    /**
     * Constructor
     */
    public ProjectHistoryGlanceWindow(AIM_Project projectToShow){
        this.projectToShow = projectToShow;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskhistory_grid = new Grid<>(GridElement.class,false);
        ArrayList<GridElement> data = new ArrayList<>();
        for(String historyElement : projectToShow.project_history){
            data.add(new GridElement(historyElement));
        }
        taskhistory_grid.addColumn(GridElement::getGridelement_text).setHeader("History");
        taskhistory_grid.setItems(data);
        taskhistory_grid.setSizeFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6(projectToShow.aim_project_name+" history"));
        main_layout.add(taskhistory_grid);

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