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
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Project;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;

/**
 * Window for logging user to the app
 */
public class InsertProjectWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField projectname_field;
    TextArea projectdesc_field;

    Button addproject_button;

    AIM_Project projectToUpdate;

    /**
     * Constructor
     */
    public InsertProjectWindow(AIM_Project projectToUpdate){
        this.projectToUpdate = projectToUpdate;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_dialog.setDraggable(true);
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        projectname_field = new TextField("Project Name");
        projectname_field.setPlaceholder("My New Project");
        projectname_field.setWidth("100%");
        projectname_field.setMaxLength(40);
        projectname_field.addClassName("aim-inputfield-bright");

        projectdesc_field = new TextArea("Project Description");
        projectdesc_field.setPlaceholder("That's gonna be awesome!");
        projectdesc_field.setWidth("100%");
        projectname_field.setMaxLength(40);
        projectdesc_field.addClassName("aim-inputfield-bright");

        addproject_button = new Button("Create Project", VaadinIcon.PLUS.create(),this::addprojectbutton_action);
        addproject_button.setWidth("100%");
        addproject_button.addClassName("aim-button-black");

        if ( projectToUpdate != null ){
            projectname_field.setValue(projectToUpdate.aim_project_name);
            projectdesc_field.setValue(projectToUpdate.aim_project_desc);
            addproject_button.setText("Update Project");
        }
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(new H6("New Project"));
        main_layout.add(projectname_field);
        main_layout.add(projectdesc_field);
        main_layout.add(addproject_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * Function for preparing project from window content
     * @return AIM_Project
     */
    AIM_Project prepareProject(){
        AIM_Project project;
        if ( projectToUpdate == null ){
            project = new AIM_Project();
            project.aim_project_name = projectname_field.getValue();
            project.aim_project_desc = projectdesc_field.getValue();
            return project;
        }
        else{
            project = projectToUpdate;
            project.aim_project_name = projectname_field.getValue();
            project.aim_project_desc = projectdesc_field.getValue();
            return project;
        }
    }

    /**
     * addproject_button
     * @param ex
     */
    private void addprojectbutton_action(ClickEvent ex){
        Database_AIMProject dip = new Database_AIMProject(AimApplication.database);
        if (!projectname_field.getValue().isEmpty() && !projectdesc_field.getValue().isEmpty()){
            if ( projectToUpdate != null ){
                // update project data
                int ans = dip.updateProject(prepareProject());
                if (ans == 1){
                    Notification.show("Project updated!");
                    main_dialog.close();
                    if ( AimApplication.session_cpc!= null ){
                        AimApplication.session_cpc.updateLayout();
                    }
                }
            }
            else{
                // add new project
                int ans = dip.insertProject(prepareProject());
                if (ans == 1){
                    Notification.show("Project added!");
                    main_dialog.close();
                    if ( AimApplication.session_cpc!= null ){
                        AimApplication.session_cpc.updateLayout();
                    }
                }
                else{
                    Notification.show("Error adding project, check application log!");
                }
            }
        }
        else{
            Notification.show("Wrong user input, check window!");
        }
    }


}
