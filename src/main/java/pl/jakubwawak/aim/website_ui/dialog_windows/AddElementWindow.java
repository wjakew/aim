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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows.InsertBoardWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows.InsertProjectWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.InsertTaskWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

/**
 * Window for logging user to the app
 */
public class AddElementWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "60%";
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
        addboard_button = new Button("Add Board", VaadinIcon.PLUS.create(),this::addboardbutton_action);
        new ButtonStyler().primaryButtonStyle(addboard_button,"200px","200px");

        addproject_button = new Button("Add Project", VaadinIcon.PLUS.create(),this::addprojectbutton_action);
        new ButtonStyler().primaryButtonStyle(addproject_button,"200px","200px");

        addtask_button = new Button("Add Task", VaadinIcon.PLUS.create(),this::addtaskbutton_action);
        new ButtonStyler().primaryButtonStyle(addtask_button,"200px","200px");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Element creator"));

        HorizontalLayout buttonLayout = new HorizontalLayout(addtask_button,addproject_button,addboard_button);

        buttonLayout.setWidth("100");
        buttonLayout.setMargin(true);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        main_layout.add(buttonLayout);

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

    /**
     * paddproject_button
     * @param ex
     */
    private void addprojectbutton_action(ClickEvent ex){
        InsertProjectWindow ipw = new InsertProjectWindow(null);
        main_layout.add(ipw.main_dialog);
        ipw.main_dialog.open();
        main_dialog.close();
    }

    /**
     * addboard_button
     * @param ex
     */
    private void addboardbutton_action(ClickEvent ex){
        InsertBoardWindow ibw = new InsertBoardWindow(null);
        main_layout.add(ibw.main_dialog);
        ibw.main_dialog.open();
        main_dialog.close();
    }
}
