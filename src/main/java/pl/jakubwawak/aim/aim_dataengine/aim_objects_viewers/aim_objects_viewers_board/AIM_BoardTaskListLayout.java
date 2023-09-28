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
        currentPage = 0;
        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::previousbutton_action);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::nextbutton_action);
        prepareLayout();
    }

    /**
     * Function for reloading task board collection
     */
    void reloadTaskBoardCollection(){
        // todo bug - 10 task in board not showing another page onlu the first
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        boardTaskContent.clear();
        boardTaskContent = dab.getBoard(board.board_id).getTaskList();
        for(AIM_BoardTask abtObject : boardTaskContent){
            currentCollection.add(new AIM_BoardTaskLayout(abtObject,board));
        }

        // creating pages
        int pageIndex = 1;
        boolean addFlag = false;
        BoardTaskListPage btlp = new BoardTaskListPage(pageIndex,board);

        for(AIM_BoardTaskLayout abtl : currentCollection){
            int ans = btlp.addLayout(abtl);
            if ( ans == 10 ){
                btlp.prepareLayout();
                pages.add(btlp);
                pageIndex++;
                btlp = new BoardTaskListPage(pageIndex,board);
                addFlag = true;
            }
            else{
                addFlag = false;
            }
        }
        if (!addFlag){
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
        // todo bug after showing another next page page number increases by ^2
        reloadTaskBoardCollection();
        main_layout.removeAll();
        main_layout.add(pages.get(pageIndex).main_layout);
        main_layout.add(new HorizontalLayout(previous_button,new H6(currentPage+1 + "/" + pages.size()),next_button));

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

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        reloadView();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setSizeFull();
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color","black");
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
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
