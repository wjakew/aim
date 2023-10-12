/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.maintanance.GridElement;
import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class BoardListGlanceWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Grid<AIM_Board> boardGrid;

    /**
     * Constructor
     */
    public BoardListGlanceWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        boardGrid = new Grid<>(AIM_Board.class,false);
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        ArrayList<AIM_Board> boardCollection = dab.getUserBoardList();

        boardGrid.addColumn(AIM_Board::getBoard_name).setHeader("Board Name");
        boardGrid.addColumn(AIM_Board::ownerLabel).setHeader("Owner");
        boardGrid.setItems(boardCollection);
        boardGrid.setSizeFull();

        boardGrid.addItemClickListener(e->{
            for(AIM_Board selected : boardGrid.getSelectedItems()){
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", selected.board_name);
                Notification.show(selected.board_name+" copied to clipboard!");
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
        main_layout.add(new H6("Your Current Boards"));
        main_layout.add(boardGrid);

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
