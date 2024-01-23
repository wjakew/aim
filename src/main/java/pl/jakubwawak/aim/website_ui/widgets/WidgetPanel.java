/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.widgets;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_WidgetPanel;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;
import pl.jakubwawak.aim.website_ui.widgets.widgets.widgets.*;

import java.util.ArrayList;

/**
 * Layout panel for storing widgets
 */
public class WidgetPanel {

    public HorizontalLayout mainLayout;
    Widget widget1,widget2,widget3,widget4;

    int widgetCounter;

    /**
     * Constructor
     */
    public WidgetPanel(){
        widgetCounter = 0;
        mainLayout = new HorizontalLayout();
        preparePanel();
    }

    /**
     * Constructor
     * @param dwp
     */
    public WidgetPanel(AIM_WidgetPanel dwp){
        try{
            mainLayout = new HorizontalLayout();
            widget1 = prepareWidget(dwp.widget1Content);
            widget2 = prepareWidget(dwp.widget2Content);
            widget3 = prepareWidget(dwp.widget3Content);
            widget4 = prepareWidget(dwp.widget4Content);
            AimApplication.database.log("WIDGET-PANEL-LOADER","Loaded panel data to the application!");
            preparePanel();
        }catch(Exception ex){
            AimApplication.database.log("WIGET-PANEL-LOADER","Widget panel failed to load ("+ex.toString()+")");
        }
    }

    /**
     * Function for preparing widget from document
     * @param widgetDocument
     * @return Widget
     */
    Widget prepareWidget(Document widgetDocument){
        widgetCounter++;
        if (widgetDocument == null){
            return new Widget(100,100,widgetCounter);
        }
        else{
            switch(widgetDocument.getString("widgetType")){
                case "counter":
                {
                    return new CounterWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "create-task":
                {
                    return new CreateTaskWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "task-details":
                {
                    return new TaskDetailsWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "terminal":
                {
                    return new TerminalWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "task-list":
                {
                    return new TaskListWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "projects-list":
                {
                    return new ProjectsWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "notes":
                {
                    return new NotesWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
                case "codingtask-widget":
                {
                    return new CodingTaskWidget(100,100,widgetDocument.getString("widgetContentString"),widgetCounter);
                }
            }
            return null;
        }
    }

    /**
     * Function for preparing widgets
     */
    void prepareWidgets(){
        widget1 = new Widget(100,100,1);
        widget2 = new Widget(100,100,2);
        widget3 = new Widget(100,100,3);
        widget4 = new Widget(100,100,4);
    }

    /**
     * Function for preparing panel
     */
    void preparePanel(){
        mainLayout.setSizeFull();
        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-image","radial-gradient(purple,gray)");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");

        HorizontalLayout hl_main = new HorizontalLayout();
        hl_main.setWidth("100%"); hl_main.setHeight("100%");
        hl_main.setAlignItems(FlexComponent.Alignment.CENTER);
        hl_main.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout vl_left = new VerticalLayout();
        VerticalLayout vl_right = new VerticalLayout();

        vl_left.setSizeFull();
        vl_left.getStyle().set("text-align", "center");
        vl_left.getStyle().set("border-radius","25px");

        vl_right.setSizeFull();
        vl_right.getStyle().set("text-align", "center");
        vl_right.getStyle().set("border-radius","25px");

        vl_left.add(widget1.widget,widget2.widget);
        vl_right.add(widget3.widget,widget4.widget);
        hl_main.add(vl_left,vl_right);
        mainLayout.add(hl_main);
    }

    /**
     * Function for reloading panel
     */
    public void reloadPanel(){
        mainLayout.removeAll();
        mainLayout.setSizeFull();
        mainLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.getStyle().set("background-image","radial-gradient(purple,gray)");
        mainLayout.getStyle().set("text-align", "center");
        mainLayout.getStyle().set("border-radius","25px");


        HorizontalLayout hl_main = new HorizontalLayout();
        hl_main.setWidth("100%"); hl_main.setHeight("100%");
        hl_main.setAlignItems(FlexComponent.Alignment.CENTER);
        hl_main.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        VerticalLayout vl_left = new VerticalLayout();
        VerticalLayout vl_right = new VerticalLayout();

        vl_left.setSizeFull();
        vl_left.getStyle().set("text-align", "center");
        vl_left.getStyle().set("border-radius","25px");

        vl_right.setSizeFull();
        vl_right.getStyle().set("text-align", "center");
        vl_right.getStyle().set("border-radius","25px");

        vl_left.add(widget1.widget,widget2.widget);
        vl_right.add(widget3.widget,widget4.widget);
        hl_main.add(vl_left,vl_right);
        mainLayout.add(hl_main);
    }
}
