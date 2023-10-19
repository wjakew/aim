/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows.DetailsBoardWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.DetailsProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.DetailsTaskWindow;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class AdminDataManagmentWindow {

    // variables for setting x and y of window
    public String width = "70%";
    public String height = "70%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    // task tab components
    Grid<AIM_Task> task_grid;
    Grid<AIM_Project> project_grid;
    Grid<AIM_Board> board_grid;

    /**
     * Constructor
     */
    public AdminDataManagmentWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components

        // task_tab
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        ArrayList<AIM_Task> taskCollection = dat.getAllTaskCollection();
        task_grid = new Grid<>(AIM_Task.class,false);
        task_grid.addColumn(AIM_Task::getAim_task_name).setHeader("Task Name");
        task_grid.addColumn(AIM_Task::getAim_task_owner_glance).setHeader("Task Owner");
        task_grid.addColumn(AIM_Task::getAim_task_timestamp).setHeader("Time Created");
        task_grid.addColumn(AIM_Task::getStatus).setHeader("Task Status");
        task_grid.setItems(taskCollection);
        task_grid.setWidth("100%");task_grid.setHeight("100%");

        // projects_tab
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        ArrayList<AIM_Project> projectCollection = dap.getAllProjects();
        project_grid = new Grid<>(AIM_Project.class,false);
        project_grid.addColumn(AIM_Project::getAim_project_name).setHeader("Project Name");
        project_grid.addColumn(AIM_Project::getAim_owner_glance).setHeader("Project Owner");
        project_grid.setItems(projectCollection);
        project_grid.setWidth("100%");project_grid.setHeight("100%");

        // board_tab
        Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);
        ArrayList<AIM_Board> boardCollection = dab.getAllBoardList();
        board_grid = new Grid<>(AIM_Board.class,false);
        board_grid.addColumn(AIM_Board::getBoard_name).setHeader("Board Name");
        board_grid.addColumn(AIM_Board::getBoard_owner_glance).setHeader("Board Owner");
        board_grid.addColumn(AIM_Board::getBoard_members_size).setHeader("Members Amount");
        board_grid.setItems(boardCollection);
        board_grid.setWidth("100%");board_grid.setHeight("100%");

        task_grid.addItemClickListener(e->{
            for(AIM_Task selected_task : task_grid.getSelectedItems()){
                DetailsTaskWindow dtw = new DetailsTaskWindow(selected_task);
                main_layout.add(dtw.main_dialog);
                dtw.main_dialog.open();
                break;
            }
        });

        project_grid.addItemClickListener(e->{
            for(AIM_Project selected_project : project_grid.getSelectedItems()){
                DetailsProjectWindow dpw = new DetailsProjectWindow(selected_project);
                main_layout.add(dpw.main_dialog);
                dpw.main_dialog.open();
                break;
            }
        });

        board_grid.addItemClickListener(e->{
            for(AIM_Board selected_board : board_grid.getSelectedItems()){
                DetailsBoardWindow dbw = new DetailsBoardWindow(selected_board);
                main_layout.add(dbw.main_dialog);
                dbw.main_dialog.open();;
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
        TabSheet tabSheet_center = new TabSheet();
        tabSheet_center.add("Task",task_grid);
        tabSheet_center.add("Project",project_grid);
        tabSheet_center.add("Board",board_grid);
        tabSheet_center.setSizeFull();

        main_layout.add(new H6("Data Manager"));
        main_layout.add(tabSheet_center);

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
