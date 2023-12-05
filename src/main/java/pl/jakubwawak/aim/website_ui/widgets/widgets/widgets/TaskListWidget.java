/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.TaskColumnLayout;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for
 */
public class TaskListWidget extends Widget implements Serializable {

    String contentString;

    boolean contentStringCorrect; // flag for checking if string is correct

    TaskColumnLayout tcl;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public TaskListWidget(int width,int height, String contentString,int widgetID){
        super(width,height,widgetID);
        super.widgetName = "task-list";
        super.widgetDesc = "Widget for showing list of tasks, options: task-new, task-inprogress, task-done!";
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
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        switch(contentString){
            case "task-new":
            {
                tcl = new TaskColumnLayout(dat.getNewTaskCollection(),"black","NEW TASKS",null,"","");
                break;
            }
            case "task-inprogress":
            {
                tcl = new TaskColumnLayout(dat.getInProgressTaskCollection(),"black","IN PROGRESS",null,"","");
                break;
            }
            case "task-done":
            {
                tcl = new TaskColumnLayout(dat.getDoneTaskCollection(),"black","DONE",null,"","");
                break;
            }
        }
    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        tcl.columnLayout.setSizeFull();
        addComponent(tcl.columnLayout);
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        prepareContent();
        super.widget.removeAll();
        addComponent(new H6("TASK LIST WIDGET"));
    }
}
