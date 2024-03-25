/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_WidgetPanel;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMWidgetPanel;
import pl.jakubwawak.aim.website_ui.widgets.widgets.Widget;
import pl.jakubwawak.aim.website_ui.widgets.widgets.widgets.*;

/**
 * Window for creating
 */
public class FloatingWindow {

    // variables for setting x and y of window
    public String width = "70%";
    public String height = "70%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Widget floatingwidget;
    Button close_button;

    /**
     * Constructor
     */
    public FloatingWindow(){
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window");
        main_dialog.setModal(false);
        main_dialog.setResizable(true);
        main_dialog.setDraggable(true);
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        Database_AIMWidgetPanel dawp = new Database_AIMWidgetPanel(AimApplication.database);
        AIM_WidgetPanel awp = dawp.getPanelData();
        if ( awp.widget5Content == null ){
            floatingwidget = new Widget(100,100,5);
        }
        else{
            Document widgetDocument = awp.widget5Content;
            switch(widgetDocument.getString("widgetType")){
                case "counter":
                {
                    floatingwidget =  new CounterWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "create-task":
                {
                    floatingwidget = new CreateTaskWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "task-details":
                {
                    floatingwidget = new TaskDetailsWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "terminal":
                {
                    floatingwidget = new TerminalWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "task-list":
                {
                    floatingwidget = new TaskListWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "projects-list":
                {
                    floatingwidget = new ProjectsWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
                case "notes":
                {
                    floatingwidget = new NotesWidget(100,100,widgetDocument.getString("widgetContentString"),5);
                    break;
                }
            }
        }
        close_button = new Button("", VaadinIcon.CLOSE.create(),this::setClose_button);
        close_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(floatingwidget.widget);
        main_layout.add(close_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    private void setClose_button(ClickEvent ex){
        main_dialog.close();
    }
}
