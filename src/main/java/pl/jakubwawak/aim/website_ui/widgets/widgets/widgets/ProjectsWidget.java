/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H6;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.CurrentProjectComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.ProjectHorizontalColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.ProjectVerticalColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for
 */
public class ProjectsWidget extends Widget implements Serializable {

    String contentString;

    boolean contentStringCorrect; // flag for checking if string is correct

    ProjectVerticalColumnLayout pvcl;


    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public ProjectsWidget(int width,int height, String contentString,int widgetID){
        super(width,height,widgetID);
        super.widgetName = "projects-list";
        super.widgetDesc = "Widget for presenting current projects, type projects to add!";
        this.contentString = contentString;
        contentStringCorrect = checkContentStringCorrect();
        if (contentString.isEmpty()){
            prepareDemo();
            AimApplication.database.log("WIDGET","Prepared demo!");
        }
        else{
            if (contentStringCorrect)
                prepareWidget();
            else
                AimApplication.database.log("WIDGET","Widget empty! Wrong contentString");
        }
    }

    /**
     * Function for checking contentstring value
     * @return boolean
     */
    public boolean checkContentStringCorrect(){
        // logic for checking content string logic
        return true;
    }

    /**
     * Function for adding component
     * @param component
     */
    void addComponent(Component component){
        super.addToWidget(component);
    }


    /**
     * Function for preparing widget content
     */
    void prepareContent(){
        // prepare content layout
        Database_AIMProject dat = new Database_AIMProject(AimApplication.database);
        pvcl = new ProjectVerticalColumnLayout(dat.getUserProjects(),widget);

    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        addComponent(pvcl.projectVerticalLayout);
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        prepareContent();
        super.widget.removeAll();
        addComponent(new H6("VIEW YOUR LATEST PROJECTS"));
    }
}
