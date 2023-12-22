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
import java.time.LocalDateTime;

/**
 * Window for adding comment to the task
 */
public class AddCommentCTaskWindow {

    // variables for setting x and y of window
    public String width = "";
    public String height = "";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextArea comment_area;
    Button addcomment_button;

    InsertCTaskWindow ictw;


    /**
     * Constructor
     */
    public AddCommentCTaskWindow(InsertCTaskWindow ictw){
        this.ictw = ictw;
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
        comment_area.setWidth("100%");
        addcomment_button = new Button("Add comment", VaadinIcon.COMMENT.create(),this::setAddcomment_button);
        addcomment_button.setWidth("100%");
        addcomment_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
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
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
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
            ictw.act.aim_codingtask_comments.add(comment);
            ictw.reloadCommentContent();
            main_dialog.close();
        }
        else{
            Notification.show("Comment window cannot be empty!");
        }
    }
}
