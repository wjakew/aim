/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.widgets.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMWidgetPanel;
import pl.jakubwawak.aim.website_ui.widgets.WidgetPanel;
import pl.jakubwawak.aim.website_ui.widgets.widgets.widgets.*;

import java.util.ArrayList;

/**
 * Window for showing window for selecting widget
 */
public class WidgetPickerWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button previous_button, select_button, next_button;

    TextField configurationstring_field;

    TextArea content_field;


    int index;

    ArrayList<Widget> selectableWidgetCollection;
    int widgetID;
    VerticalLayout parent;

    /**
     * Constructor with widget support
     */
    public WidgetPickerWindow(int widgetID){
        this.widgetID = widgetID;
        this.parent = null;
        index = 0;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Constructor with workspace support
     * @param parent
     */
    public WidgetPickerWindow(VerticalLayout parent){
        this.widgetID = -99;
        this.parent = parent;
        index = 0;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        selectableWidgetCollection = new ArrayList<>();

        // add widgets
        selectableWidgetCollection.add(new CounterWidget(100,100,"",1));
        selectableWidgetCollection.add(new CreateTaskWidget(100,100,"demo",2));
        selectableWidgetCollection.add(new TaskDetailsWidget(100,100,"",3));
        selectableWidgetCollection.add(new TerminalWidget(100,100,"",4));
        selectableWidgetCollection.add(new TaskListWidget(100,100,"",4));
        selectableWidgetCollection.add(new ProjectsWidget(100,100,"",4));
        selectableWidgetCollection.add(new NotesWidget(100,100,"",5));
        selectableWidgetCollection.add(new CodingTaskWidget(100,100,"",5));
        // running in demo mode

        previous_button = new Button("", VaadinIcon.ARROW_LEFT.create(),this::setPrevious_button);
        select_button = new Button("Add Widget",VaadinIcon.PLUS.create(),this::setSelect_button);
        next_button = new Button("",VaadinIcon.ARROW_RIGHT.create(),this::setNext_button);

        previous_button.addClassName("aim-button-black");
        select_button.addClassName("aim-button-black");
        next_button.addClassName("aim-button-black");

        configurationstring_field = new TextField();
        configurationstring_field.setPlaceholder("configuration string...");
        configurationstring_field.setWidth("70%");
        configurationstring_field.addClassName("aim-inputfield-bright");

        content_field = new TextArea("Description");
        content_field.setSizeFull();
        content_field.addClassName("aim-inputfield-bright");

    }

    /**
     * Function for updating window content
     * @param widget
     */
    void putWindowContent(Widget widget){
        main_layout.removeAll();
        main_layout.add(new H3("Pick Your Widget"));

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
        content_field.setValue(widget.widgetDesc);

        vl_left.add(widget.widget);
        vl_right.add(content_field);

        hl_main.add(vl_left,vl_right);

        main_layout.add(hl_main);

        HorizontalLayout hl_buttons = new HorizontalLayout(previous_button,configurationstring_field,next_button);
        hl_buttons.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.add(hl_buttons);
        main_layout.add(select_button);


    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        putWindowContent(selectableWidgetCollection.get(index));
        previous_button.setEnabled(false);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * previous_button action
     * @param ex
     */
    private void setPrevious_button(ClickEvent ex){
        if ( index != 0 ){
            index--;
            putWindowContent(selectableWidgetCollection.get(index));
            next_button.setEnabled(true);
        }
        else{
            putWindowContent(selectableWidgetCollection.get(index));
            previous_button.setEnabled(false);
        }
    }

    /**
     * next_button action
     * @param ex
     */
    private void setNext_button(ClickEvent ex){
        if ( index < selectableWidgetCollection.size()-1 ){
            index++;
            putWindowContent(selectableWidgetCollection.get(index));
            previous_button.setEnabled(true);
        }
        else{
            putWindowContent(selectableWidgetCollection.get(index));
            next_button.setEnabled(false);
        }
    }

    /**
     * select_button action
     * @param ex
     */
    private void setSelect_button(ClickEvent ex){
        if (widgetID >= 0){
            String contentString = configurationstring_field.getValue();
            String widgetName = selectableWidgetCollection.get(index).widgetName;
            // got ID Name and contentString
            Database_AIMWidgetPanel dawp = new Database_AIMWidgetPanel(AimApplication.database);
            int ans = dawp.updatePanelData(widgetName,contentString,widgetID);
            if ( ans == 1 ){
                AimApplication.currentWidgetPanel = new WidgetPanel(dawp.getPanelData());
                UI.getCurrent().getPage().reload();
                Notification.show("Added new widget!");
            }
            else{
                Notification.show("Failed to update panel data, check application log!");
            }
        }
        else{
            String contentString = configurationstring_field.getValue();
            String widgetName = selectableWidgetCollection.get(index).widgetName;
            Widget widget = null;
            switch(widgetName){
                case "counter":
                {
                    widget = new CounterWidget(100,100,contentString,0);
                    break;
                }
                case "create-task":
                {
                    widget = new CreateTaskWidget(100,100,contentString,0);
                    break;
                }
                case "task-details":
                {
                    widget = new TaskDetailsWidget(100,100,contentString,0);
                    break;
                }
                case "terminal":
                {
                    widget = new TerminalWidget(100,100,contentString,0);
                    break;
                }
                case "task-list":
                {
                    widget = new TaskListWidget(100,100,contentString,0);
                    break;
                }
                case "projects-list":
                {
                    widget = new ProjectsWidget(100,100,contentString,0);
                    break;
                }
                case "notes":
                {
                    widget = new NotesWidget(100,100,contentString,0);
                    break;
                }
                case "codingtask-widget":
                {
                    widget = new CodingTaskWidget(100,100,contentString,0);
                }
            }

            if ( widget != null ){
                // create modable window for showing widget data
                WidgetFloatingWindow wfw = new WidgetFloatingWindow(widget);
                parent.add(wfw.main_dialog);
                wfw.main_dialog.open();
                main_dialog.close();
            }
        }
    }
}
