/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.website_ui.dialog_windows.MessageComponent;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.Set;

/**
 * Layout for showing task details for sharing
 */
public class AIM_TaskLayoutShare {

    public VerticalLayout layout;

    AIM_Task taskObject;

    H1 taskname_header;
    TextArea taskdesc_area;

    Grid<GridElement> history_grid;

    /**
     * Constructor
     * @param taskObject
     */
    public AIM_TaskLayoutShare(AIM_Task taskObject){
        this.taskObject = taskObject;
        layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        taskname_header = new H1(taskObject.aim_task_name);
        history_grid = new Grid<>(GridElement.class,false);
        ArrayList<GridElement> data = new ArrayList<>();

        for(String element : taskObject.aim_task_history){
            data.add(new GridElement(element));
        }

        history_grid.addColumn(GridElement::getGridelement_text).setHeader("Task History");
        history_grid.setItems(data);
        history_grid.setSizeFull();

        taskdesc_area = new TextArea("Task description");
        taskdesc_area.setValue(taskObject.aim_task_desc);
        taskdesc_area.setReadOnly(true);
        taskdesc_area.setWidth("100%");

        history_grid.addSelectionListener(e->{
            Set<GridElement> selected = history_grid.getSelectedItems();
            for(GridElement element : selected){
                MessageComponent mc = new MessageComponent(element.getGridelement_text());
                layout.add(mc.main_dialog);
                mc.main_dialog.open();
                break;
            }
        });
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        prepareComponents();
        layout.add(taskname_header);

        layout.add(taskdesc_area);

        HorizontalLayout hl_down_layout = new HorizontalLayout();

        VerticalLayout vl_left, vl_right;

        vl_left = new VerticalLayout();
        vl_right = new VerticalLayout();

        vl_left.setSizeFull();
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");

        vl_right.setSizeFull();
        vl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_right.getStyle().set("text-align", "center");
        vl_left.add(history_grid);

        vl_right.add(new H6("Created: "+taskObject.aim_task_timestamp),new H6("Deadline: "+taskObject.aim_task_deadline));

        hl_down_layout.add(vl_left,vl_right);
        hl_down_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_down_layout.setSizeFull();

        layout.add(hl_down_layout);

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
