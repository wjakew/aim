/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.project_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;

/**
 * Window for showing prompt for closing project window
 */
public class CloseProjectWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    AIM_Project projectToRemove;

    Button closeproject_button, close_button;

    /**
     * Constructor
     */
    public CloseProjectWindow(AIM_Project projectToRemove){
        this.projectToRemove = projectToRemove;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        closeproject_button = new Button("Close project", VaadinIcon.TRASH.create(),this::closeprojectbutton_action);
        closeproject_button.setWidth("100%");
        closeproject_button.addClassName("aim-button-black");
        close_button = new Button("Close",this::close_button);
        close_button.setWidth("100%");close_button.addClassName("aim-button-black");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H3("Project Closing"));
        main_layout.add(new H6(projectToRemove.aim_project_name));
        main_layout.add(new H6("Opened Tasks: "+projectToRemove.openTaskAmount()));
        main_layout.add(new H6("All task amount: "+projectToRemove.getTaskCollection().size()));
        main_layout.add(closeproject_button,close_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * closeproject_button
     * @param ex
     */
    private void closeprojectbutton_action(ClickEvent ex){
        Database_AIMProject dap = new Database_AIMProject(AimApplication.database);
        if ( dap.removeProject(projectToRemove) == 1){
            Notification.show("Project removed!");
            if ( AimApplication.session_cpc!= null ){
                AimApplication.session_cpc.updateLayout();
            }
            main_dialog.close();
        }
        else{
            Notification.show("Failed to remove project, check log");
        }
    }

    /**
     *
     * @param ex
     */
    private void close_button(ClickEvent ex){
        main_dialog.close();
    }
}
