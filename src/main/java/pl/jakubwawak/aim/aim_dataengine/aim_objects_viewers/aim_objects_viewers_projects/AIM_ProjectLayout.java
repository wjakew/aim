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
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnPage;
import pl.jakubwawak.aim.website_ui.dialog_windows.MessageComponent;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.CloseProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.InsertProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Object for creating layout for AIM_Project object
 */
public class AIM_ProjectLayout {
    AIM_Project projectObject;
    public VerticalLayout projectLayout;

    Grid<GridElement> grid_history;

    Button update_button, closeproject_button;

    Button addtask_button;

    TaskColumnLayout tcl;

    /**
     * Constructor
     */
    public AIM_ProjectLayout(AIM_Project projectObject){
        this.projectObject = projectObject;
        this.projectLayout = new VerticalLayout();

        grid_history = new Grid<>(GridElement.class,false);
        grid_history.addColumn(GridElement::getGridelement_text).setHeader("Project History");
        grid_history.setSizeFull();

        tcl = new TaskColumnLayout(projectObject.getTaskCollection(),"green","Linked Tasks",projectObject,"60%","90%");

        ArrayList<GridElement> historycontent = new ArrayList<>();

        for(String element : projectObject.project_history){
            historycontent.add(new GridElement(element));
        }
        grid_history.setItems(historycontent);
        addtask_button = new Button("Add Task",this::addtaskbutton_action);
        addtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);
        update_button = new Button("Update Project",this::updateprojectbutton_action);
        update_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        closeproject_button = new Button("Close Project",this::closeprojectbutton_action);
        closeproject_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        update_button.setWidth("100%");closeproject_button.setWidth("100%"); addtask_button.setWidth("100%");

        grid_history.addItemClickListener(e ->{
            GridElement selected = e.getItem();
            MessageComponent mc = new MessageComponent(selected.getGridelement_text());
            projectLayout.add(mc.main_dialog);
            mc.main_dialog.open();
        });
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        projectLayout.setSizeFull();

        VerticalLayout vl_left = new VerticalLayout();
        vl_left.add(grid_history);
        vl_left.add(addtask_button);
        vl_left.add(update_button,closeproject_button);
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");
        vl_left.setWidth("30%");vl_left.setHeight("90%");

        HorizontalLayout mainhorizontal_layout = new HorizontalLayout();
        mainhorizontal_layout.add(vl_left,tcl.columnLayout);
        mainhorizontal_layout.setSizeFull();
        mainhorizontal_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        projectLayout.add(new H4(projectObject.aim_project_name),mainhorizontal_layout);
        projectLayout.setSizeFull();
        projectLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        projectLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        projectLayout.getStyle().set("text-align", "center");
        projectLayout.getStyle().set("--lumo-font-family","Monospace");

        projectLayout.getStyle().set("border-radius","25px");
        projectLayout.getStyle().set("background-color","black");
        projectLayout.getStyle().set("color","#FFFFFF");
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

    /**
     * updateproject_button
     * @param ex
     */
    private void updateprojectbutton_action(ClickEvent ex){
        InsertProjectWindow ipw = new InsertProjectWindow(projectObject);
        projectLayout.add(ipw.main_dialog);
        ipw.main_dialog.open();
    }

    /**
     * closeproject_button
     * @param ex
     */
    private void closeprojectbutton_action(ClickEvent ex){
        CloseProjectWindow cpw = new CloseProjectWindow(projectObject);
        projectLayout.add(cpw.main_dialog);
        cpw.main_dialog.open();
    }
}
