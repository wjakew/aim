/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_Task;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;

/**
 * Window for logging user to the app
 */
public class DetailsTaskWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    AIM_Task taskObject;

    H1 taskname_header;
    TextArea taskdesc_area;

    Grid<GridElement> history_grid;

    ComboBox<GridElement> status_combobox;

    Button update_button, changeowner_button, delete_button;


    /**
     * Constructor
     */
    public DetailsTaskWindow(AIM_Task taskObject){
        this.taskObject = taskObject;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        taskname_header = new H1(taskObject.aim_task_name);

        taskdesc_area = new TextArea("Task description");
        taskdesc_area.setValue(taskObject.aim_task_desc);
        taskdesc_area.setReadOnly(true);
        taskdesc_area.setWidth("100%");

        history_grid = new Grid<>(GridElement.class,false);
        ArrayList<GridElement> data = new ArrayList<>();

        for(String element : taskObject.aim_task_history){
            data.add(new GridElement(element));
        }

        history_grid.addColumn(GridElement::getGridelement_text).setHeader("Task History");
        history_grid.setItems(data);
        history_grid.setSizeFull();

        status_combobox = new ComboBox<>("Task Status");

        ArrayList<GridElement> statusdata = new ArrayList<>();
        statusdata.add(new GridElement("NEW"));
        statusdata.add(new GridElement("IN PROGRESS"));
        statusdata.add(new GridElement("DONE"));

        status_combobox.setItems(statusdata);
        status_combobox.setItemLabelGenerator(GridElement::getGridelement_text);
        status_combobox.setValue(new GridElement(taskObject.status));
        status_combobox.setWidth("100%");
        status_combobox.setAllowCustomValue(false);

        update_button = new Button("Update", VaadinIcon.PENCIL.create(),this::updatebutton_action);
        update_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        update_button.setWidth("100%");

        changeowner_button = new Button("Change Owner", VaadinIcon.USER.create());
        changeowner_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        changeowner_button.setWidth("100%");

        delete_button = new Button("Remove Task", VaadinIcon.TRASH.create(),this::deletebutton_action);
        delete_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_ERROR);
        delete_button.setWidth("100%");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        main_layout.removeAll();
        prepare_components();
        // set layout
        main_layout.add(taskname_header);
        main_layout.add(new H6(taskObject.aim_task_id.toString()));
        main_layout.add(taskdesc_area);

        HorizontalLayout hl_down_layout = new HorizontalLayout();

        VerticalLayout vl_left, vl_right;

        vl_left = new VerticalLayout();
        vl_right = new VerticalLayout();

        vl_left.setSizeFull();
        vl_left.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_left.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_left.getStyle().set("text-align", "center");

        vl_right.setSizeFull();
        vl_right.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl_right.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl_right.getStyle().set("text-align", "center");

        vl_left.add(history_grid);

        vl_right.add(new H6("Created: "+taskObject.aim_task_timestamp),new H6("Deadline: "+taskObject.aim_task_deadline),status_combobox,update_button,changeowner_button);

        hl_down_layout.add(vl_left,vl_right);
        hl_down_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        hl_down_layout.setSizeFull();

        main_layout.add(hl_down_layout);
        main_layout.add(delete_button);

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

    /**
     * update_button action
     * @param ex
     */
    private void updatebutton_action(ClickEvent ex){
        InsertTaskWindow itw = new InsertTaskWindow(taskObject);
        main_layout.add(itw.main_dialog);
        itw.main_dialog.open();
    }

    /**
     * delete_button action
     * @param ex
     */
    private void deletebutton_action(ClickEvent ex){
        Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
        String data = dat.remove(taskObject);
        Notification.show("Removed ("+data+")");
        AimApplication.session_ctc.updateLayout();
        main_dialog.close();
    }
}
