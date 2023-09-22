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
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Board;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_BoardTask;

public class AIM_BoardGlanceLayout {

    VerticalLayout main_layout;

    public AIM_Board boardObject;

    Button showDetails_button;

    /**
     * Constructor
     */
    public AIM_BoardGlanceLayout(AIM_Board boardObject){
        this.boardObject = boardObject;
        main_layout = new VerticalLayout();
        prepareLayout();
    }

    /**
     * Function for preparing layout data
     */
    void prepareLayout(){
        showDetails_button = new Button("", VaadinIcon.INFO_CIRCLE.create());
        showDetails_button.getStyle().set("background-color","grey");
        showDetails_button.getStyle().set("color","white");

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        left_layout.setAlignItems(FlexComponent.Alignment.START);
        left_layout.add(new H6(boardObject.board_name));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.add(new H4(boardObject.ownerLabel()),showDetails_button);

        HorizontalLayout hl_center = new HorizontalLayout(left_layout,right_layout);

        hl_center.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        hl_center.setWidth("100%");
        hl_center.addClassNames("py-0", "px-m");

        main_layout.add(hl_center);

        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setWidth("100%");
        main_layout.setHeight("100%");
        main_layout.getStyle().set("text-align", "center");
        main_layout.getStyle().set("border-radius","25px");

        main_layout.getStyle().set("background-color","#48b869");
        main_layout.getStyle().set("color","#FFFFFF");
        main_layout.getStyle().set("--lumo-font-family","Monospace");
    }
}