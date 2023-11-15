/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Layout panel for storing widgets
 */
public class WidgetPanel {

    public HorizontalLayout mainLayout;

    /**
     * Constructor
     */
    public WidgetPanel(){
        mainLayout = new HorizontalLayout();
        preparePanel();
    }

    /**
     * Function for preparing panel
     */
    void preparePanel(){
        mainLayout.setWidth("80%");mainLayout.setHeight("80%");
        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-image","radial-gradient(purple,gray)");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");
    }
}
