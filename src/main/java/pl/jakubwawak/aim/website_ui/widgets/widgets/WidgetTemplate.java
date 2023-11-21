/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.Serializable;

/**
 * Widget for
 */
public class WidgetTemplate extends Widget implements Serializable {

    String contentString;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public WidgetTemplate(int width,int height, String contentString){
        super(width,height);
        this.contentString = contentString;
        prepareWidget();
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
    }
}
