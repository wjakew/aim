/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_ApplicationLog;
import pl.jakubwawak.maintanance.GridElement;

/**
 * Window for viewing application log
 */
public class LogViewerWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    Grid<GridElement> log_grid;

    /**
     * Constructor
     */
    public LogViewerWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        log_grid = new Grid<>(GridElement.class,false);
        log_grid.addColumn(GridElement::getGridelement_details).setHeader("Category");
        log_grid.addColumn(GridElement::getGridelement_text).setHeader("Log");
        log_grid.setSizeFull();
        log_grid.setItems(AimApplication.database.getLog());
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        main_layout.add(new H6("APPLICATION LOG"));
        main_layout.add(log_grid);
        // set layout

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
