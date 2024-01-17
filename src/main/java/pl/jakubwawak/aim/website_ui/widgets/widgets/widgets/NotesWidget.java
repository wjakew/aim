/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMWidgetPanel;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;

import java.io.Serializable;

/**
 * Widget for
 */
public class NotesWidget extends Widget implements Serializable {

    String contentString;

    boolean contentStringCorrect; // flag for checking if string is correct

    TextArea notesarea;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public NotesWidget(int width,int height, String contentString,int widgetID){
        super(width,height,widgetID);
        super.widgetName = "notes";
        super.widgetDesc = "Simply press enter in the note widget to save the note. Type notes to add widget!";
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
        notesarea = new TextArea("");
        notesarea.setWidth("90%");notesarea.setHeight("70%");
        notesarea.setValue(contentString);

        notesarea.addBlurListener(e->{
            // update note value on the widget
            Database_AIMWidgetPanel dawp = new Database_AIMWidgetPanel(AimApplication.database);
            int ans = dawp.updateWidgetContentString("notes",notesarea.getValue(),widgetID);
            if (ans == 1){
                Notification.show("Notes widget updated!");
            }
            else{
                Notification.show("Cannot update widget! Check application log!");
            }
        });
    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
        addComponent(new H1("Note"));
        addComponent(notesarea);
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        prepareContent();
        super.widget.removeAll();
        addComponent(new H1("Notes"));
        addComponent(notesarea);
        notesarea.setValue("Simply press enter in the note widget to save the note. Type notes to add widget!");
        notesarea.setReadOnly(true);
    }
}
