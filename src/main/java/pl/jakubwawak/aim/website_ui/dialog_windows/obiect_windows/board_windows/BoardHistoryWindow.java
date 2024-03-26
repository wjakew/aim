/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class BoardHistoryWindow {

    // variables for setting x and y of window
    public String width = "70%";
    public String height = "70%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<GridElement> boardhistory_grid;

    AIM_Board board;

    /**
     * Constructor
     */
    public BoardHistoryWindow(AIM_Board board){
        this.board = board;
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
        boardhistory_grid = new Grid<>(GridElement.class,false);
        boardhistory_grid.addClassName("aim-grid");
        ArrayList<GridElement> data = new ArrayList<>();
        for(String board_history_element : board.board_history){
            data.add(new GridElement(board_history_element));
        }
        boardhistory_grid.addColumn(GridElement::getGridelement_text).setHeader("Board History");
        boardhistory_grid.setItems(data);
        boardhistory_grid.setSizeFull();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H2(board.board_name+" history."));
        main_layout.add(new H6("All board history in one place"));
        main_layout.add(boardhistory_grid);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }
}
