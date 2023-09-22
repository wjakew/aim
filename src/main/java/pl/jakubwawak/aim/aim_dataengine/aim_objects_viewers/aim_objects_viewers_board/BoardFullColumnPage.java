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

    /**
     * Constructor
     */
    public BoardFullColumnPage() {
        this.main_layout = new VerticalLayout();
        this.boardLayout1 = null;
        this.boardLayout2 = null;
        this.boardLayout3 = null;
        this.boardLayout4 = null;
    }

}
