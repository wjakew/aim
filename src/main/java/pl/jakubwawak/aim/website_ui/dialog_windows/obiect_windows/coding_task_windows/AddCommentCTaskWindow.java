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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import org.bson.Document;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_ctask.AIM_CTaskViewer;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMCodingTask;

import java.time.LocalDateTime;

/**
 * Window for adding comment to the task
 */
public class AddCommentCTaskWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextArea comment_area;
    Button addcomment_button;

    InsertCTaskWindow ictw;
    AIM_CTaskViewer actv;

    /**
     * Constructor with insert window support
     */
    public AddCommentCTaskWindow(InsertCTaskWindow ictw){
        this.ictw = ictw;
        this.actv = null;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Constructor with viewer layout support
     * @param actv
     */
    public AddCommentCTaskWindow(AIM_CTaskViewer actv){
        this.actv = actv;
        this.ictw = null;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        comment_area = new TextArea("Your Comment");
        comment_area.setPlaceholder("Tell me something exciting...");
        comment_area.setSizeFull();
        comment_area.addClassName("aim-inputfield-bright");
        addcomment_button = new Button("Add comment", VaadinIcon.COMMENT.create(),this::setAddcomment_button);
        addcomment_button.setWidth("100%");
        addcomment_button.addClassName("aim-button-black");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(comment_area);
        main_layout.add(addcomment_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * addcomment_button action
     * @param ex
     */
    private void setAddcomment_button(ClickEvent ex){
        if ( !comment_area.getValue().isEmpty() ){
            Document comment = new Document();
            comment.append("time", LocalDateTime.now().toString());
            comment.append("user", AimApplication.loggedUser.aim_user_id);
            comment.append("comment",comment_area.getValue());

            // add comment to new ctask in insert window
            if ( ictw != null ){
                ictw.act.aim_codingtask_comments.add(comment);
                ictw.reloadCommentContent();
                main_dialog.close();
            }

            // add comment to coding task in viewer
            else if ( actv != null ){
                actv.act.aim_codingtask_comments.add(comment);
                // add comment to database
                Database_AIMCodingTask dact = new Database_AIMCodingTask(AimApplication.database);
                int ans = dact.updateCodingTask(actv.act);
                if (ans == 1){
                    Notification.show("New comment added to "+actv.act.aim_codingtask_name);
                    actv.reloadCommentContent();
                    main_dialog.close();
                }
                else{
                    Notification.show("Failed to add comment, check application log!");
                }
            }
        }
        else{
            Notification.show("Comment window cannot be empty!");
        }
    }
}
