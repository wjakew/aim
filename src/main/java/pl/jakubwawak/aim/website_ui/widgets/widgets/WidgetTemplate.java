/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import pl.jakubwawak.aim.AimApplication;

import java.io.Serializable;

/**
 * Widget for
 */
public class WidgetTemplate extends Widget implements Serializable {

    String contentString;

    String widgetDesc = ""; // widget desc for widget picker window
    boolean contentStringCorrect; // flag for checking if string is correct

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public WidgetTemplate(int width,int height, String contentString){
        super(width,height);
        this.contentString = contentString;
        contentStringCorrect = checkContentStringCorrect();
        if ( contentStringCorrect )
            prepareWidget();
        else
            AimApplication.database.log("WIDGET","Widget empty! Wrong contentString");
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
    }

    /**
     * Function for preparing widget layout
     */
    void prepareWidget(){
        prepareContent();
        super.reloadBackground();
    }

    /**
     * Function for preparing demo content of the widget
     */
    public void prepareDemo(){
        // prepare demo content
        super.widget.removeAll();
        addComponent(new H6("DEMO"));
    }
}
