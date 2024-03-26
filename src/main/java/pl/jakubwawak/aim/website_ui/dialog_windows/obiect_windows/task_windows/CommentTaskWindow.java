/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.task_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMTask;

/**
 * Window for logging user to the app
 */
public class CommentTaskWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    DetailsTaskWindow dtw;
    TextField comment_field;
    Button placecomment_button;

    /**
     * Constructor
     */
    public CommentTaskWindow(DetailsTaskWindow dtw){
        this.dtw = dtw;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_dialog.setDraggable(true);
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        comment_field = new TextField("");
        comment_field.setPlaceholder("type comment here...");
        comment_field.setWidth("100%");
        comment_field.addClassName("aim-inputfield-bright");

        placecomment_button = new Button("Place comment", VaadinIcon.COMMENT.create(),this::setPlacecomment_button);
        placecomment_button.setWidth("100%");
        placecomment_button.addClassName("aim-button-black");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H3("Place comment"));
        main_layout.add(new H6("task ID ("+dtw.taskObject.aim_task_id.toString()+")"));
        main_layout.add(comment_field);
        main_layout.add(placecomment_button);

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
     * placecomment_button
     * @param ex
     */
    private void setPlacecomment_button(ClickEvent ex){
        if ( !comment_field.getValue().isBlank() ){
            Database_AIMTask dat = new Database_AIMTask(AimApplication.database);
            dtw.taskObject.aim_task_history.add(AimApplication.loggedUser.aim_user_email+" commented: "+comment_field.getValue());
            int ans = dat.updateAIMTask(dtw.taskObject);
            if ( ans == 1 ){
                Notification.show("Comment placed!");
                dtw.historyReload();
                main_dialog.close();
            }
            else{
                Notification.show("Cannot place comment, check application log");
            }
        }
    }
}
