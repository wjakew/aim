/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for
 */
public class CreateTaskWidget extends Widget implements Serializable {

    String contentString;

    TextField taskName_field, taskDesc_field;
    Button create_button;


    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public CreateTaskWidget(int width,int height, String contentString){
        super(width,height);
        super.widgetDesc = "Widget for creating task in a simple way. Type create to add this widget";
        this.contentString = contentString;
        contentStringCorrect = checkContentStringCorrect();
        if ( contentString.equals("demo") ){
            prepareDemo();
        }
        else{
            if ( contentStringCorrect )
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
        taskName_field = new TextField("");
        taskName_field.setPlaceholder("Task Name");
        taskName_field.setWidth("100%");

        taskDesc_field = new TextField("");
        taskDesc_field.setPlaceholder("Task Desc");
        taskDesc_field.setWidth("100%");

        create_button = new Button("Create",VaadinIcon.TASKS.create(),this::setCreate_button);
        create_button.setWidth("100%");
        create_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        addComponent(new H6("CREATE NEW TASK"));
        addComponent(taskName_field);
        addComponent(taskDesc_field);
        addComponent(create_button);
    }

    /**
     * create_button action
     * @param ex
     */
    private void setCreate_button(ClickEvent ex){
        if ( !taskName_field.getValue().isBlank() && !taskDesc_field.getValue().isBlank() ){
            Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
            AIM_Task task = new AIM_Task();
            task.aim_task_name = taskName_field.getValue();
            task.aim_task_desc = taskDesc_field.getValue();
            int ans = dat.insertAIMTask(task);
            if  (ans == 1){
                Notification.show("Task "+task.aim_task_name+" added!");
                taskName_field.setValue("");
                taskDesc_field.setValue("");
            }
            else{
                Notification.show("Failed to add task, check log!");
            }
        }
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        super.widget.removeAll();
        prepareWidget();
        taskName_field.setEnabled(false);
        taskDesc_field.setEnabled(false);
        create_button.setEnabled(false);
    }
}
