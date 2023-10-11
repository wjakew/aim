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
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects.AIM_User;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMBoard;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;


/**
 * Window for changing board owner
 */
public class ChangeBoardOwnerWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "30%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;
    DetailsBoardWindow dbw;

    TextField email_field;
    Button change_button;

    /**
     * Constructor
     */
    public ChangeBoardOwnerWindow(DetailsBoardWindow dbw){
        this.dbw = dbw;
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        email_field = new TextField("New Owner Mail");
        email_field.setPlaceholder("Aim User Email...");
        email_field.setWidth("100%");
        email_field.setMaxLength(50);

        change_button = new Button("Change Owner", VaadinIcon.USER_CARD.create(),this::changebutton_action);
        change_button.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);
        change_button.setWidth("100%");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        main_layout.add(new H4("Owner change for "+dbw.board.board_name));
        main_layout.add(email_field);
        main_layout.add(new H6("WARNING! THERE IS NO UNDO OF THIS OPERATION WITHOUT NEW OWNER ACTION!"));
        main_layout.add(change_button);

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
     * change_button action
     * @param ex
     */
    private void changebutton_action(ClickEvent ex){
        if ( email_field.getValue().contains("@") ){

            Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
            Database_AIMBoard dab = new Database_AIMBoard(AimApplication.database);

            AIM_User newOwner = dau.getAIMUser(email_field.getValue());
            if ( newOwner != null ){
                int ans = dab.changeBoardOwner(dbw.board,newOwner.prepareDocument());
                if ( ans == 1 ){
                    Notification.show("Owner for board ("+dbw.board.board_id.toString()+") changed!");
                    dbw.main_dialog.close();
                    main_dialog.close();
                    if (AimApplication.session_cbc != null){
                        AimApplication.session_cbc.updateLayout(0);
                    }

                }
                else{
                    Notification.show("Application error, check log");
                }
            }
            else{
                Notification.show("Cannot find user with given email!");
            }
        }
        else{
            Notification.show("Wrong email, check user input!");
        }
    }
}
