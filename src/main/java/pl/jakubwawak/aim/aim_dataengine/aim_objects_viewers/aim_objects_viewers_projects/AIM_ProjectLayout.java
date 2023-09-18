/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnPage;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.InsertProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Object for creating layout for AIM_Project object
 */
public class AIM_ProjectLayout {
    AIM_Project projectObject;
    public HorizontalLayout projectLayout;

    Grid<GridElement> grid_history;

    Button update_button, closeproject_button;

    Button addtask_button;

    TaskColumnLayout tcl;

    /**
     * Constructor
     */
    public AIM_ProjectLayout(AIM_Project projectObject){
        this.projectObject = projectObject;
        this.projectLayout = new HorizontalLayout();

        grid_history = new Grid<>(GridElement.class,false);
        grid_history.addColumn(GridElement::getGridelement_text).setHeader("Project History");
        grid_history.setSizeFull();
        tcl = new TaskColumnLayout(projectObject.getTaskCollection(),"green","Linked Tasks");

        ArrayList<GridElement> historycontent = new ArrayList<>();

        for(String element : projectObject.project_history){
            historycontent.add(new GridElement(element));
        }
        grid_history.setItems(historycontent);
        addtask_button = new Button("Add Task",this::addtaskbutton_action);
        addtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        update_button = new Button("Update Project");
        update_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        closeproject_button = new Button("Close Project");
        closeproject_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        update_button.setWidth("100%");closeproject_button.setWidth("100%"); addtask_button.setWidth("100%");
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        projectLayout.setSizeFull();

        VerticalLayout vl_left = new VerticalLayout();
        vl_left.add(new H6(projectObject.aim_project_name));
        vl_left.add(grid_history);
        vl_left.add(addtask_button);
        vl_left.add(update_button,closeproject_button);
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");

        vl_left.setSizeFull();
        projectLayout.add(vl_left,tcl.columnLayout);

        projectLayout.setSizeFull();
        projectLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    }

    /**
     * addtask_button
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        InsertTaskWindow itw = new InsertTaskWindow(null,projectObject);
        projectLayout.add(itw.main_dialog);
        itw.main_dialog.open();
    }
}
