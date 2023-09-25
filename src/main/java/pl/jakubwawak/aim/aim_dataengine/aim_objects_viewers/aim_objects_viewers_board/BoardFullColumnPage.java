/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;

/**
 * Object for storing single page of boards
 */
public class BoardFullColumnPage {

    public VerticalLayout main_layout;

    public AIM_BoardGlanceLayout boardLayout1;
    public AIM_BoardGlanceLayout boardLayout2;
    public AIM_BoardGlanceLayout boardLayout3;
    public AIM_BoardGlanceLayout boardLayout4;

    public int page;

    /**
     * Constructor
     */
    public BoardFullColumnPage(int page) {
        this.main_layout = new VerticalLayout();
        this.page = page;
        this.boardLayout1 = null;
        this.boardLayout2 = null;
        this.boardLayout3 = null;
        this.boardLayout4 = null;
    }

    /**
     * Function for adding object to page
     */
    public int addBoard(AIM_Board boardToAdd){
        if ( boardLayout1 == null ){
            boardLayout1 = new AIM_BoardGlanceLayout(boardToAdd);
            return 1;
        }
        else if ( boardLayout2 == null ){
            boardLayout2 = new AIM_BoardGlanceLayout(boardToAdd);
            return 2;
        }
        else if ( boardLayout3 == null ){
            boardLayout3 = new AIM_BoardGlanceLayout(boardToAdd);
            return 3;
        }
        else if ( boardLayout4 == null ){
            boardLayout4 = new AIM_BoardGlanceLayout(boardToAdd);
            return 4;
        }
        return 5;
    }

    /**
     * Function for checking if object is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return boardLayout1 == null && boardLayout2 == null && boardLayout3 == null && boardLayout4 == null;
    }
}
