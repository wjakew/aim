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
import org.bson.Document;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.maintanance.GridElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * Window for showing full coding task window
 */
public class ShowFullHistoryWindow {

    // variables for setting x and y of window
    public String width = "90%";
    public String height = "90%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    AIM_CodingTask act;

    Grid<GridElement> grid;
    ArrayList<GridElement> gridContent;
    /**
     * Constructor
     */
    public ShowFullHistoryWindow(AIM_CodingTask act){
        this.act = act;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        grid = new Grid<>(GridElement.class,false);
        grid.addColumn(GridElement::getGridelement_details2).setHeader("Time");
        grid.addColumn(GridElement::getGridelement_details).setHeader("Category");
        grid.addColumn(GridElement::getGridelement_text).setHeader("Description");

        gridContent = new ArrayList<>();
        for(Document document : act.aim_codingtask_history){
            /*
                document layout
                history_category: CATEGORY
                history_time: TIME
                history_text: TEXT
                history_user: EMAIL
            */
            gridContent.add(new GridElement(document.getString("history_text"),
                    document.getString("history_category"),document.get("history_time", Date.class).toString()));
        }
        grid.setItems(gridContent);
        grid.setSizeFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("HISTORY"));
        main_layout.add(new H6(act.aim_codingtask_name));
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
