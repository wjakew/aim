/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.coding_task_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.codingproject.AIM_CodingTask;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;
import pl.jakubwawak.maintanance.GridElement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Window for logging user to the app
 */
public class InsertCTaskWindow {

    // variables for setting x and y of window
    public String width = "80%";
    public String height = "80%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    VerticalLayout left_layout, right_layout;
    public AIM_CodingTask act;

    TextField ctaskname_field, ctasktag_field;

    TextArea desc_area;

    Grid<GridElement> comment_grid;
    ArrayList<GridElement> gridContent;

    Button addcomment_button;

    Button addtask_button;

    /**
     * Constructor
     */
    public InsertCTaskWindow(AIM_CodingTask act){
        this.act = act;
        if ( act == null ){
            this.act = new AIM_CodingTask();
        }
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        left_layout = new VerticalLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        left_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        left_layout.getStyle().set("text-align", "center");

        right_layout = new VerticalLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        right_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        right_layout.getStyle().set("text-align", "center");

        ctaskname_field = new TextField();
        ctaskname_field.setWidth("100%");
        ctaskname_field.setPlaceholder("my dear task...");

        ctasktag_field = new TextField();
        ctasktag_field.setWidth("100%");
        ctasktag_field.setPlaceholder("tag1,tag2,tag69...");

        gridContent = new ArrayList<>();

        comment_grid = new Grid<>(GridElement.class,false);
        comment_grid.setSizeFull();
        comment_grid.addColumn(GridElement::getGridelement_text).setHeader("Comments");
        reloadCommentContent();
        comment_grid.setItems(gridContent);

        desc_area = new TextArea("Description");
        desc_area.setSizeFull();
        desc_area.setPlaceholder("my dear coding task...");

        addtask_button = new Button("Create", VaadinIcon.PLUS.create(),this::setAddtask_button);
        addtask_button.setWidth("100%");
        addtask_button.addThemeVariants(ButtonVariant.LUMO_SUCCESS,ButtonVariant.LUMO_PRIMARY);

        addcomment_button = new Button("Add Comment", VaadinIcon.COMMENT.create(),this::setAddcomment_button);
        addcomment_button.setWidth("100%");
        addcomment_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        if ( !this.act.isEmpty() ){
            // object to update - updating fields, setting values for update
            addtask_button.setText("Update");
        }
        else{
            addtask_button.setText("Create");
        }

        //populate UI with object data
        if ( !act.isEmpty() ){
            ctaskname_field.setValue(act.aim_codingtask_name);
            desc_area.setValue(act.aim_codingtask_desc);
            ctasktag_field.setValue(act.aim_codingtask_tag);
        }
    }

    /**
     * Function for reloading comment content
     */
    public void reloadCommentContent(){
        gridContent.clear();
        for(Document comment : act.aim_codingtask_comments){
            gridContent.add(new GridElement(comment.getString("comment")));
        }
        comment_grid.getDataProvider().refreshAll();
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        if ( !this.act.isEmpty() ){
            left_layout.add(new H6("UPDATE CODING"));
        }
        else{
            left_layout.add(new H6("NEW CODING"));
        }
        left_layout.add(new H6("TASK"));
        left_layout.add(ctaskname_field);
        left_layout.add(ctasktag_field);
        left_layout.add(comment_grid);
        left_layout.add(addcomment_button);

        right_layout.add(desc_area);

        HorizontalLayout main_hl_layout = new HorizontalLayout();
        main_hl_layout.setSizeFull();
        main_hl_layout.add(left_layout,right_layout);

        main_layout.add(main_hl_layout,addtask_button);
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
     * Function for validating tags
     * @return boolean
     */
    boolean isSimilarToTags(String str) {
        Pattern pattern = Pattern.compile("(\\w+,\\s*)+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        }
        Notification.show("Tags cannot be set, it should resolve pattern tag1,tag2,tag3");
        return false;
    }

    /**
     * Function for validating fields
     * @return boolean
     */
    boolean validateFields(){
        return !ctaskname_field.isEmpty() && !ctasktag_field.isEmpty() && !desc_area.isEmpty()
                && isSimilarToTags(ctasktag_field.getValue());
    }

    /**
     * Function for adding comment to the coding task
     * @param ex
     */
    private void setAddcomment_button(ClickEvent ex){
        AddCommentCTaskWindow acctw = new AddCommentCTaskWindow(this);
        main_layout.add(acctw.main_dialog);
        acctw.main_dialog.open();
    }

    /**
     * addtask_button
     * @param ex
     */
    private void setAddtask_button(ClickEvent ex){
        if ( validateFields() ){
            if ( addtask_button.getText().contains("Create")){
                // creating coding task
                act.aim_codingtask_desc = desc_area.getValue();
                act.aim_codingtask_name = ctaskname_field.getValue();
                act.aim_codingtask_tag = ctasktag_field.getValue();
                Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
                int ans = dact.insertCodingTask(act);
                if ( ans == 1 ){
                    Notification.show("New coding task ("+act.aim_codingtask_name+") added!");
                    main_dialog.close();
                }
                else{
                    Notification.show("Failed to add coding task, check application logs!");
                }
            }
            else{
                // updating coding task
                // creating coding task
                act.aim_codingtask_desc = desc_area.getValue();
                act.aim_codingtask_name = ctaskname_field.getValue();
                act.aim_codingtask_tag = ctasktag_field.getValue();
                Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
                int ans = dact.updateCodingTask(act);
                if ( ans == 1 ){
                    Notification.show("Task updated!");
                    main_dialog.close();
                }
                else{
                    Notification.show("Failed to update coding task, check application logs!");
                }
            }

        }
        else{
            Notification.show("Wrong user input. Check data!");
        }
    }
}
