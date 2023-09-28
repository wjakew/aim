/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;

import java.util.ArrayList;

public class BoardTaskListPage {

    public VerticalLayout main_layout;
    AIM_Board board;
    public ArrayList<AIM_BoardTaskLayout> currentPageTasks;
    int page;


    /**
     * Constructor
     * @param page
     */
    public BoardTaskListPage(int page, AIM_Board board){
        currentPageTasks = new ArrayList<>();
        main_layout = new VerticalLayout();
        this.board = board;
        this.page = page;
        loadLayout();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setSizeFull();
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for loading layout with empty data
     */
    void loadLayout(){
        for (int i = 0 ; i < 9 ; i++){
            currentPageTasks.add(new AIM_BoardTaskLayout(null,board));
        }
    }

    /**
     * Function for adding layout
     * @param boardTaskLayoutToAdd
     */
    public int addLayout(AIM_BoardTaskLayout boardTaskLayoutToAdd){
        int index = 0;
        for(AIM_BoardTaskLayout abtl : currentPageTasks){
            if ( abtl.isEmpty() ){
                currentPageTasks.set(index,boardTaskLayoutToAdd);
                return index;
            }
            index++;
        }
        return 10;
    }

    /**
     * Function for preparing layout
     */
    public void prepareLayout(){
        int counter = 0;
        boolean isEmpty = true;
        HorizontalLayout hl = new HorizontalLayout();
        hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl.setSizeFull();
        for(AIM_BoardTaskLayout abtl : currentPageTasks){
            if ( counter < 3 ){
                hl.add(abtl.main_layout);
                counter++;
            }
            else{
                main_layout.add(hl);
                hl = new HorizontalLayout();
                hl.add(abtl.main_layout);
                hl.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
                hl.setSizeFull();
                counter = 1;
            }
        }
        main_layout.add(hl);

    }
}
