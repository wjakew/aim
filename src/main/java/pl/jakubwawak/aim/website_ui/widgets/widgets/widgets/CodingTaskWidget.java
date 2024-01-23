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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for
 */
public class CodingTaskWidget extends Widget implements Serializable {

    String contentString;

    boolean contentStringCorrect; // flag for checking if string is correct

    TextField codingtaskName_field;
    TextArea codingtaskDesc_area;

    Button addtask_button;

    AIM_CodingTask act;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public CodingTaskWidget(int width, int height, String contentString, int widgetID){
        super(width,height,widgetID);
        act = new AIM_CodingTask();
        // edit name and desc
        super.widgetName = "codingtask-widget";
        super.widgetDesc = "Widget for creating new coding task. Type 'code' to add widget!";
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
        codingtaskName_field = new TextField("");
        codingtaskName_field.setPlaceholder("coding task name");
        codingtaskName_field.setWidth("80%");

        codingtaskDesc_area = new TextArea("");
        codingtaskDesc_area.setPlaceholder("coding task desc");
        codingtaskDesc_area.setWidth("80%");
        codingtaskDesc_area.setHeight("100%");

        addtask_button = new Button("Add Coding Task", VaadinIcon.PLUS.create(),this::setAddtask_button);
        addtask_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        addtask_button.setWidth("80%");

    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        addComponent(new H6("CREATE NEW CODING TASK"));
        addComponent(codingtaskName_field);
        addComponent(codingtaskDesc_area);
        addComponent(addtask_button);
        super.reloadBackground();
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        prepareContent();
        super.widget.removeAll();
        addComponent(new H6("CREATE NEW CODING TASK"));
        addComponent(codingtaskName_field);
        addComponent(codingtaskDesc_area);
        addComponent(addtask_button);
        codingtaskDesc_area.setEnabled(false);
        codingtaskName_field.setEnabled(false);
        addtask_button.setEnabled(false);
    }

    /**
     * addtask_button action
     * @param ex
     */
    private void setAddtask_button(ClickEvent ex){
        if ( !codingtaskName_field.getValue().isEmpty() && !codingtaskDesc_area.getValue().isEmpty() ){
            act.aim_codingtask_name = codingtaskName_field.getValue();
            act.aim_codingtask_desc = codingtaskDesc_area.getValue();
            act.aim_codingtask_tag = "new,widget,";
            Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
            int ans = dact.insertCodingTask(act);
            if ( ans == 1 ){
                Notification.show("Coding task added!");
                codingtaskName_field.setValue("");
                codingtaskDesc_area.setValue("");
            }
            else{
                Notification.show("Failed to add coding task, check application log");
            }
        }
    }
}
