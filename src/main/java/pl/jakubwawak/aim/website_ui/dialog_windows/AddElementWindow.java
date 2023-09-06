/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.InsertTaskWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

/**
 * Window for logging user to the app
 */
public class AddElementWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button addboard_button, addproject_button, addtask_button;

    /**
     * Constructor
     */
    public AddElementWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        addboard_button = new Button("Add Board", VaadinIcon.PLUS.create());
        new ButtonStyler().primaryButtonStyle(addboard_button,"100%","");

        addproject_button = new Button("Add Project", VaadinIcon.PLUS.create());
        new ButtonStyler().primaryButtonStyle(addproject_button,"100%","");

        addtask_button = new Button("Add Task", VaadinIcon.PLUS.create(),this::addtaskbutton_action);
        new ButtonStyler().primaryButtonStyle(addtask_button,"100%","");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Element creator"));

        main_layout.add(addtask_button,addproject_button,addboard_button);

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

    /**
     * addtask_button
     * @param ex
     */
    private void addtaskbutton_action(ClickEvent ex){
        InsertTaskWindow itw = new InsertTaskWindow(null);
        main_layout.add(itw.main_dialog);
        itw.main_dialog.open();
        main_dialog.close();
    }
}
