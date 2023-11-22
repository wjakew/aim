/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;
import pl.jakubwawak.aim.website_ui.widgets.widgets.widgets.CreateTaskWidget;
import pl.jakubwawak.aim.website_ui.widgets.widgets.widgets.TaskDetailsWidget;

import java.util.ArrayList;

/**
 * Layout panel for storing widgets
 */
public class WidgetPanel {

    public HorizontalLayout mainLayout;
    Widget widget1;

    /**
     * Constructor
     */
    public WidgetPanel(){
        mainLayout = new HorizontalLayout();
        preparePanel();
    }

    /**
     * Function for preparing widgets
     */
    void prepareWidgets(){
        widget1 = new CreateTaskWidget(60,60,"");
    }

    /**
     * Function for preparing panel
     */
    void preparePanel(){
        prepareWidgets();
        mainLayout.setSizeFull();
        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-image","radial-gradient(purple,gray)");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");
        mainLayout.add(widget1.widget);
    }
}
