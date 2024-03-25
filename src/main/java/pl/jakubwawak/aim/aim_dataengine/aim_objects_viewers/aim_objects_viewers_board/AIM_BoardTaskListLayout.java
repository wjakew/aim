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
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;

import java.util.ArrayList;

/**
 * Object for creating board task view
 */
public class AIM_BoardTaskListLayout {

    public VerticalLayout main_layout;

    ArrayList<AIM_BoardTaskLayout> currentCollection;
    ArrayList<AIM_BoardTask> boardTaskContent;

    ArrayList<BoardTaskListPage> pages;

    int currentPage;
    Button previous_button, next_button;

    AIM_Board board;

    /**
     * Constructor
     * @param board
     */
    public AIM_BoardTaskListLayout(AIM_Board board){
        currentCollection = new ArrayList<>();
        boardTaskContent = new ArrayList<>();
        this.board = board;
        pages = new ArrayList<>();
        main_layout = new VerticalLayout();
        main_layout.addClassName("task-layout");
        currentPage = 0;
        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        previous_button.addClassName("aim-button-black");
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);
        next_button.addClassName("aim-button-black");
        prepareLayout();
    }

    /**
     * Function for reloading task board collection
     */
    void reloadTaskBoardCollection(){
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        boardTaskContent.clear();
        currentCollection.clear();
        pages.clear();
        boardTaskContent = dab.getBoard(board.board_id).getTaskList();
        for(AIM_BoardTask abtObject : boardTaskContent){
            currentCollection.add(new AIM_BoardTaskLayout(abtObject,board));
        }

        if ( currentCollection.size() > 0 ){
            // creating pages
            int pageIndex = 1;
            BoardTaskListPage btlp = new BoardTaskListPage(pageIndex,board);
            int ans = 0;
            for(AIM_BoardTaskLayout abtl : currentCollection){
                ans = btlp.addLayout(abtl);
                if ( ans == 10 ){
                    btlp.prepareLayout();
                    pages.add(btlp);
                    pageIndex++;
                    btlp = new BoardTaskListPage(pageIndex,board);
                    btlp.addLayout(abtl);
                }
            }

            btlp.prepareLayout();
            pages.add(btlp);
        }
    }

    /**
     * Function for reloading view
     */
    public void reloadView(){
        currentPage = 0;
        reloadView(currentPage);
    }

    /**
     * Function for reloading view for given page number
     * @param pageIndex
     */
    public void reloadView(int pageIndex){
        reloadTaskBoardCollection();
        if (pages.size() >0){
            main_layout.removeAll();
            main_layout.add(pages.get(pageIndex).main_layout);
            int pageIndexLabel = currentPage+1;
            main_layout.add(new HorizontalLayout(previous_button,new H6(pageIndexLabel + "/" + pages.size()),next_button));

            if ( currentPage == 0 ){
                previous_button.setEnabled(false);
            }
            else{
                previous_button.setEnabled(true);
            }

            if ( currentPage+1 == pages.size() ){
                next_button.setEnabled(false);
            }
            else{
                next_button.setEnabled(true);
            }
        }
        else{
            main_layout.add(new H6("NO BOARDS"));
        }
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        reloadView();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setSizeFull();
    }

    /**
     * previous_button action
     * @param ex
     */
    private void previousbutton_action(ClickEvent ex){
        currentPage--;
        reloadView(currentPage);
    }

    /**
     * next_button action
     * @param ex
     */
    private void nextbutton_action(ClickEvent ex){
        currentPage++;
        reloadView(currentPage);
    }

}
