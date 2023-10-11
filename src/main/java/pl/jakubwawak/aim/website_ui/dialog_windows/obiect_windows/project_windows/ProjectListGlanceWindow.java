/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;

/**
 * Window for logging user to the app
 */
public class ProjectListGlanceWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    Grid<AIM_Project> project_grid;

    /**
     * Constructor
     */
    public ProjectListGlanceWindow(){
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
        project_grid = new Grid<>(AIM_Project.class,false);
        project_grid.addColumn(AIM_Project::getAim_project_name).setHeader("Project Name");
        project_grid.addColumn(AIM_Project::openTaskAmount).setHeader("Opened Tasks");
        project_grid.setItems(dap.getUserProjects());
        project_grid.setSizeFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Your Projects"));
        main_layout.add(project_grid);

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
