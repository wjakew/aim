/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;

import java.util.ArrayList;

/**
 * Object for creating layout for showing list of boards
 */
public class CurrentBoardComposer {

    ArrayList<AIM_Board> userBoard_collection;

    public VerticalLayout mainLayout;

    ArrayList<BoardFullColumnPage> layoutPages;

    Button next_button, previous_button;

    public int currentPage;

    /**
     * Constructor
     */
    public CurrentBoardComposer(){
        currentPage = 0;
        mainLayout = new VerticalLayout();
        userBoard_collection = new ArrayList<>();
        layoutPages = new ArrayList<>();
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setSizeFull();
        mainLayout.addClassName("current-board-composer");
        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);
        updateLayout(currentPage);
    }

    /**
     * Function for reloading board collection
     */
    void reloadBoardCollection(){
        int flag = 0;
        int page = 1;
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        userBoard_collection.clear();
        userBoard_collection.addAll(dab.getUserBoardList());
        layoutPages.clear();
        BoardFullColumnPage bfcp = new BoardFullColumnPage(page);

        for (AIM_Board board : userBoard_collection){
            flag = 0;
            int ans = bfcp.addBoard(board);
            if ( ans == 5 ){
                layoutPages.add(bfcp);
                page++;
                bfcp = new BoardFullColumnPage(page);
                bfcp.addBoard(board);
                flag = 1;
            }
        }
        if ( flag == 1 ){
            layoutPages.add(bfcp);
        }
        if (!bfcp.isEmpty() && flag != 1)
            layoutPages.add(bfcp);
    }

    /**
     * Function for updating layout data
     */
    public void updateLayout(int index){
        reloadBoardCollection();
        mainLayout.removeAll();
        if ( layoutPages.size() > 0 ){
            mainLayout.add(new H6("Current boards"));
            BoardFullColumnPage current_bfcp = layoutPages.get(index);
            addToLayout(current_bfcp.boardLayout1);
            addToLayout(current_bfcp.boardLayout2);
            addToLayout(current_bfcp.boardLayout3);
            addToLayout(current_bfcp.boardLayout4);
            mainLayout.add(new HorizontalLayout(previous_button,new H6(current_bfcp.page+"/"+layoutPages.size()),next_button));

            if ( index < layoutPages.size()-1 ){
                next_button.setEnabled(true);
            }
            else{
                next_button.setEnabled(false);
            }

            if (index == 0){
                previous_button.setEnabled(false);
            }
            else{
                previous_button.setEnabled(true);
            }
        }
        else{
            mainLayout.add(new H6("NO BOARD DATA"));
        }
    }

    /**
     * Function for adding layout
     * @param boardLayout
     */
    void addToLayout(AIM_BoardGlanceLayout boardLayout){
        if ( boardLayout!=null ){
            mainLayout.add(boardLayout.main_layout);
        }
    }

    /**
     * next_button
     * @param ex
     */
    public void nextbutton_action(ClickEvent ex){
        currentPage++;
        updateLayout(currentPage);
    }

    /**
     * previous_button
     * @param ex
     */
    public void previousbutton_action(ClickEvent ex){
        currentPage--;
        updateLayout(currentPage);
    }
}
