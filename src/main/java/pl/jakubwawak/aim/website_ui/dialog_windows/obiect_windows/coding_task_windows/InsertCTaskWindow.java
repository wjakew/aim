/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;

/**
 * Window for logging user to the app
 */
public class InsertCTaskWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    VerticalLayout left_layout, right_layout;
    AIM_CodingTask act;

    TextField ctaskname_field, ctasktag_field;

    /**
     * Constructor
     */
    public InsertCTaskWindow(AIM_CodingTask act){
        this.act = act;
        if ( this.act == null ){
            this.act = new AIM_CodingTask();
        }
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        left_layout = new VerticalLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        left_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        left_layout.getStyle().set("text-align", "center");

        right_layout = new VerticalLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        right_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        right_layout.getStyle().set("text-align", "center");

        ctaskname_field = new TextField();
        ctaskname_field.setWidth("100%");
        ctaskname_field.setPlaceholder("task name");

        ctasktag_field = new TextField();
        ctasktag_field.setWidth("100%");
        ctasktag_field.setPlaceholder("tags");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        left_layout.add(new H6("NEW CODING"));
        left_layout.add(new H6("TASK"));
        left_layout.add(ctaskname_field);
        left_layout.add(ctasktag_field);


        main_layout.add(left_layout,right_layout);
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
