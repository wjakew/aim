/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;

/**
 * Object for showing board task layout data
 */
public class AIM_BoardTaskLayout{

    public VerticalLayout main_layout;

    AIM_BoardTask taskObject;

    AIM_Board boardObject;

    Button showDetails_button;

    Button addtask_button;

    /**
     * Constructor
     */
    public AIM_BoardTaskLayout(AIM_BoardTask taskObject, AIM_Board boardObject){
        this.taskObject = taskObject;
        this.boardObject = boardObject;
        main_layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for checking if board task layout is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return taskObject == null;
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        if ( taskObject != null ){
            showDetails_button = new Button("", VaadinIcon.INFO_CIRCLE.create());
            showDetails_button.getStyle().set("background-color","grey");
            showDetails_button.getStyle().set("color","white");

            FlexLayout left_layout = new FlexLayout();
            left_layout.setSizeFull();
            left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
            left_layout.setAlignItems(FlexComponent.Alignment.START);
            left_layout.add(new H4(taskObject.aim_task_name));

            FlexLayout right_layout = new FlexLayout();
            right_layout.setSizeFull();
            right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
            right_layout.setAlignItems(FlexComponent.Alignment.END);
            right_layout.add(showDetails_button);

            HorizontalLayout hl_center = new HorizontalLayout(left_layout,right_layout);

            hl_center.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
            hl_center.setWidth("100%");
            hl_center.addClassNames("py-0", "px-m");

            main_layout.add(hl_center);

            if ( taskObject.aim_user_assigned == null){
                main_layout.getStyle().set("background-color","green");
            }
            else if ( taskObject.aim_user_assigned.equals(AimApplication.loggedUser.prepareDocument())){
                main_layout.getStyle().set("background-color","blue");
            }
            else {
                main_layout.getStyle().set("background-color","grey");
            }
        }
        else{
            addtask_button = new Button("", VaadinIcon.PLUS.create());
            addtask_button.getStyle().set("background-color","red");
            addtask_button.getStyle().set("color","white");
            main_layout.getStyle().set("background-color","white");
            main_layout.add(addtask_button);
        }
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setWidth("100%");
        main_layout.setHeight("100%");
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");

        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
    }


}
