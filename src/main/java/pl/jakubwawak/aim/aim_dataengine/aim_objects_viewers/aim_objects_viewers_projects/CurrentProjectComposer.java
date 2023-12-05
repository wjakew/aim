/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;

/**
 * Object for creating current view of tasks
 */
public class CurrentProjectComposer {

    public VerticalLayout mainLayout;

    public  ProjectHorizontalColumnLayout currentProjectSelectionLayout;


    /**
     * Constructor
     */
    public CurrentProjectComposer(){
        mainLayout = new VerticalLayout();
        Database_AIMProject dat = new Database_AIMProject(AimApplication.database);
        currentProjectSelectionLayout = new ProjectHorizontalColumnLayout(dat.getUserProjects(),this);
        prepareLayout();
    }

    /**
     * Function for preparing layout
     */
    void prepareLayout(){
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setSizeFull();
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");
        //mainLayout.getStyle().set("margin","75px");
        mainLayout.getStyle().set("background-image","radial-gradient(#90e9e4,#cca8b2)");
        mainLayout.getStyle().set("--lumo-font-family","Monospace");
        updateLayout();
    }

    /**
     * Function for updating view
     */
    public void updateLayout(AIM_ProjectLayout currentProjectLayout){
        mainLayout.removeAll();
        Database_AIMProject dat = new Database_AIMProject(AimApplication.database);
        currentProjectSelectionLayout = new ProjectHorizontalColumnLayout(dat.getUserProjects(),this);
        mainLayout.add(currentProjectSelectionLayout.projectHorizontalColumnLayout);
        mainLayout.add(currentProjectLayout.projectLayout);
        Notification.show("Updated current projects view!");
    }

    /**
     * Function for updating view
     */
    public void updateLayout(){
        mainLayout.removeAll();
        Database_AIMProject dat = new Database_AIMProject(AimApplication.database);
        currentProjectSelectionLayout = new ProjectHorizontalColumnLayout(dat.getUserProjects(),this);
        mainLayout.add(currentProjectSelectionLayout.projectHorizontalColumnLayout);
        mainLayout.add(currentProjectSelectionLayout.getDefaultLayout());
        Notification.show("Updated current projects view!");
    }

}
