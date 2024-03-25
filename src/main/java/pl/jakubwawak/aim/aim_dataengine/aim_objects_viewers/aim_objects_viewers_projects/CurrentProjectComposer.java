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
        mainLayout.addClassName("current-project-composer");
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
        //mainLayout.getStyle().set("margin","75px");
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
