/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.board_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;

/**
 * Window for logging user to the app
 */
public class AddMemberBoardWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    InsertBoardWindow boardInsertWindow;

    TextField email_field;
    Button add_button;

    /**
     * Constructor
     */
    public AddMemberBoardWindow(InsertBoardWindow boardInsertWindow){
        this.boardInsertWindow = boardInsertWindow;
        main_dialog = new Dialog();
        main_dialog.addClassName("aim-window-normal");
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        email_field = new TextField("E-Mail");
        email_field.setPlaceholder("Your friend email...");
        email_field.setWidth("100%");
        email_field.setMaxLength(30);
        email_field.addClassName("aim-inputfield-bright");

        add_button = new Button("Add Member", VaadinIcon.PLUS.create(),this::addbutton_action);
        add_button.addClassName("aim-button-black");
        add_button.setWidth("100%");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Add member"));
        main_layout.add(new H6(boardInsertWindow.boardToAdd.board_name));
        main_layout.add(email_field,add_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * add_button action
     * @param ex
     */
    private void addbutton_action(ClickEvent ex){
        if ( !email_field.getValue().isEmpty() && email_field.getValue().contains("@")){
            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            AIM_User user = dau.getAIMUser(email_field.getValue());
            if ( user != null ){
                boardInsertWindow.boardToAdd.board_members.add(user.prepareDocument());
                boardInsertWindow.reloadMembersGrid();
                Notification.show("Add "+user.aim_user_email+" as member");
                main_dialog.close();
            }
            else{
                Notification.show("Cannot find user with given e-mail");
            }
        }
        else{
            Notification.show("Wrong user email");
        }
    }
}
