/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Object for showing project data on layout
 */
public class AIM_ProjectLayoutShare {

    AIM_Project projectObject;

    public VerticalLayout layout;

    Grid<GridElement> grid_history;

    TaskColumnLayout projectTCL;

    /**
     * Constructor
     * @param projectObject
     */
    public AIM_ProjectLayoutShare(AIM_Project projectObject){
        this.projectObject = projectObject;
        this.layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareCompontents(){
        grid_history = new Grid<>(GridElement.class,false);
        grid_history.addColumn(GridElement::getGridelement_text).setHeader("Project History");
        grid_history.setHeight("100%");grid_history.setWidth("100%");

        projectTCL = new TaskColumnLayout(projectObject.getTaskCollection(),"gray","Linked Tasks",projectObject,"60%","90%");

        ArrayList<GridElement> historycontent = new ArrayList<>();

        for(String element : projectObject.project_history){
            historycontent.add(new GridElement(element));
        }
        grid_history.setItems(historycontent);
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        prepareCompontents();

        VerticalLayout vl_left = new VerticalLayout();
        vl_left.add(grid_history);
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");
        vl_left.setWidth("30%");vl_left.setHeight("90%");

        HorizontalLayout mainhorizontal_layout = new HorizontalLayout();
        mainhorizontal_layout.add(vl_left,projectTCL.columnLayout);
        mainhorizontal_layout.setSizeFull();
        mainhorizontal_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        layout.add(new H4(projectObject.aim_project_name),mainhorizontal_layout);

        layout.setSizeFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.getStyle().set("text-align", "center");
        layout.getStyle().set("--lumo-font-family","Monospace");

        layout.getStyle().set("border-radius","25px");
        layout.getStyle().set("background-image","radial-gradient(#eeaeca,#94bbe9)");
        layout.getStyle().set("color","#FFFFFF");
        layout.setWidth("100%");layout.setHeight("70%");
    }

}
