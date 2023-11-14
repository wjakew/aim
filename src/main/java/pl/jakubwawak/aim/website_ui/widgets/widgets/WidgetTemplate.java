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
public class WidgetTemplate implements Serializable {

    // widget layout
    public VerticalLayout widget;

    // max widget size
    final int widthMAX = 60;
    final int heightMAX = 60;

    // styling widget
    final String backgroundType = "background-color";
    final String backgroundStyle = "black";

    // set size
    int width,height;

    // value for changing template data
    String contentString;

    /**
     * Constructor
     * @param width
     * @param height
     * @param contentString
     */
    public WidgetTemplate(int width,int height, String contentString){
        if ( widthMAX > width )
            this.width = width;
        else
            this.width = widthMAX;
        if ( heightMAX > height )
            this.height = height;
        else
            this.height = height;
        widget = new VerticalLayout();
        this.contentString = contentString;
        prepareWidget();
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
        // prepare widget layout

        widget.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        widget.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        widget.getStyle().set("text-align", "center");

        widget.getStyle().set("border-radius","25px");
        widget.getStyle().set(backgroundType,backgroundStyle);
        widget.getStyle().set("--lumo-font-family","Monospace");
    }
}
