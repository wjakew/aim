/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for showing count task
 * contentString values:
 * task-done
 * task-new
 * task-inprogress
 */
public class CounterWidget extends Widget implements Serializable {

    String contentString;

    H1 counter_header;
    H6 counter_desc;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public CounterWidget(int width,int height, String contentString){
        super(width,height);
        super.widgetDesc = "Widget for showing number of tasks, options: task-done, task-new, task-inprogress"; // widget desc for widget picker window
        this.contentString = contentString;
        contentStringCorrect = checkContentStringCorrect();
        if (contentString.isEmpty()){
            prepareDemo();
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
        return contentString.equals("task-done") || contentString.equals("task-new") || contentString.equals("task-inprogress");
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
            case "task-done":
            {
                counter_header = new H1(Integer.toString(dat.getDoneTaskCollection().size()));
                counter_desc = new H6("DONE TASKS");
                break;
            }
            case "task-new":
            {
                counter_header = new H1(Integer.toString(dat.getNewTaskCollection().size()));
                counter_desc = new H6("NEW TASKS");
                break;
            }
            case "task-inprogress":
            {
                counter_header = new H1(Integer.toString(dat.getInProgressTaskCollection().size()));
                counter_desc = new H6("IN PROGRESS TASKS");
                break;
            }
            default:
            {
                counter_header = new H1("NaN");
                counter_desc = new H6("NaN");
                break;
            }
        }
    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.backgroundStyle = "black";
        super.backgroundType = "background-color";
        addComponent(counter_header);
        addComponent(counter_desc);
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        widget.removeAll();
        // prepare demo content
        counter_header = new H1("69");
        counter_desc = new H6("ITEMS LOL");
        addComponent(counter_header);
        addComponent(counter_desc);
    }
}
