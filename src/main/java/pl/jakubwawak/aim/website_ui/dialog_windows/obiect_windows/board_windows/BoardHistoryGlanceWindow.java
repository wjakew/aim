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
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class BoardHistoryGlanceWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> taskhistory_grid;

    AIM_Board boardToShow;

    /**
     * Constructor
     */
    public BoardHistoryGlanceWindow(AIM_Board boardToShow){
        this.boardToShow = boardToShow;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-inputfield-bright");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskhistory_grid = new Grid<>(GridElement.class,false);
        taskhistory_grid.addClassName("aim-grid");
        ArrayList<GridElement> data = new ArrayList<>();
        for(String historyElement : boardToShow.board_history){
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
        main_layout.add(new H6(boardToShow.board_name+" history"));
        main_layout.add(taskhistory_grid);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}
