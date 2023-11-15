/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets;

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
     * Function for preparing widget content
     */
    public void prepareContent(){
        // prepare content layout
    }

    /**
     * Function for preparing widget layout
     */
    public void prepareWidget(){
        prepareContent();
        super.createEmptyWidget();
    }
}
