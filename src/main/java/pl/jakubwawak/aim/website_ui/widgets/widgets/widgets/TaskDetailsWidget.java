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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bson.Document;
import org.bson.types.ObjectId;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMProject;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows.DetailsTaskWindow;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for showing task details
 */
public class TaskDetailsWidget extends Widget implements Serializable {

    String contentString;

    AIM_Task taskToShow;

    TextArea taskDetails_area;

    Button changestatus_button, details_button;


    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public TaskDetailsWidget(int width,int height, String contentString,int widgetID){
        super(width,height,widgetID);
        super.widgetDesc = "Widget for showing task details. Insert task ID to view!";
        super.widgetName = "task-details";
        this.contentString = contentString;
        contentStringCorrect = checkContentStringCorrect();
        if ( contentString.isEmpty() ){
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
        try{
            ObjectId task_id = new ObjectId(contentString);
            Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
            return dat.getTask(task_id) != null;
        }catch(Exception ex){
            return false;
        }

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
        ObjectId taskID = new ObjectId(contentString);
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        taskToShow = dat.getTask(taskID);

        taskDetails_area = new TextArea();

        changestatus_button = new Button("", VaadinIcon.ARROW_CIRCLE_RIGHT.create(),this::setChangestatus_button);
        details_button = new Button("",VaadinIcon.INFO.create(),this::detailsbutton_action);

        if ( taskToShow != null ) {
            taskDetails_area.setValue(taskToShow.aim_task_desc);
            taskDetails_area.setReadOnly(true);
            taskDetails_area.setWidth("100%");
            if (!taskToShow.status.equals("DONE")) {
                changestatus_button.setVisible(true);
            } else {
                changestatus_button.setVisible(false);
            }
            details_button.setVisible(true);
        }

        changestatus_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        details_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        if (taskToShow != null ){
            addComponent(new H6(taskToShow.aim_task_name));
            addComponent(new H6(taskToShow.aim_task_timestamp.toString()));
            addComponent(taskDetails_area);
            addComponent(new HorizontalLayout(details_button,changestatus_button));
        }
        else{
            addComponent(new H6("TASK OBJECT NULL"));
        }
    }

    /**
     * details_button action
     * @param ex
     */
    private void detailsbutton_action(ClickEvent ex){
        DetailsTaskWindow dtw = new DetailsTaskWindow(taskToShow);
        super.widget.add(dtw.main_dialog);
        dtw.main_dialog.open();
    }

    /**
     * changestatus_button action
     * @param ex
     */
    private void setChangestatus_button(ClickEvent ex){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        int ans = 0;
        String newStatus = "";
        switch(taskToShow.status){
            case "NEW":
            {
                ans = dat.updateAIMTaskStatus(taskToShow,"IN PROGRESS");
                newStatus = "IN PROGRESS";
                break;
            }
            case "IN PROGRESS":
            {
                ans = dat.updateAIMTaskStatus(taskToShow,"DONE");
                newStatus = "DONE";
                break;
            }
        }
        if (ans!=0){
            Notification.show("("+taskToShow.aim_task_id.toString()+") set to: "+newStatus);
            if ( AimApplication.session_ctc!= null ){
                taskToShow.status = newStatus;
                if ( taskToShow.status.equals("DONE")){
                    changestatus_button.setVisible(false);
                }
            }

        }
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        changestatus_button = new Button("", VaadinIcon.ARROW_CIRCLE_RIGHT.create(),this::setChangestatus_button);
        details_button = new Button("",VaadinIcon.INFO.create(),this::detailsbutton_action);

        changestatus_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        details_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        // prepare demo content
        super.widget.removeAll();
        taskDetails_area = new TextArea("");
        taskDetails_area.setValue("test");

        addComponent(new H6("test task"));
        addComponent(new H6("task_time"));
        addComponent(new HorizontalLayout(details_button,changestatus_button));
        details_button.setEnabled(false);
        changestatus_button.setEnabled(false);
    }
}
