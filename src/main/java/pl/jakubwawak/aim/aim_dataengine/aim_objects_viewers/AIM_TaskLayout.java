/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers;

import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;

/**
 * Object for creating layout for presenting AIM_Task data
 * task colors depends on task status
 * NEW #48b869
 * IN PROGRESS #4891b8
 * DONE #a8964f
 */
public class AIM_TaskLayout {
    public VerticalLayout main_layout;

    AIM_Task taskObject;

    /**
     * Constructor
     * @param taskObject
     */
    public AIM_TaskLayout(AIM_Task taskObject){
        main_layout = new VerticalLayout();
        this.taskObject = taskObject;
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        main_layout.add(new H6(taskObject.aim_task_name));

        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setWidth("100%");
        main_layout.setHeight("100%");
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");

        if ( taskObject.status.equals("NEW")){
            main_layout.getStyle().set("background-color","#48b869");
        }
        else if ( taskObject.status.equals("IN PROGRESS")){
            main_layout.getStyle().set("background-color","#4891b8");
        }
        else if ( taskObject.status.equals("DONE")){
            main_layout.getStyle().set("background-color","#a8964f");
        }
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
    }
}
