/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_objects.AIM_User;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;
import pl.jakubwawak.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.Password_Validator;

import java.util.regex.Pattern;

/**
 * Window for creating user account
 */
public class CreateAccountWindow {

    // variables for setting x and y of window
    public String width = "60%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField email_field, name_field, surname_field, telephone_field;
    PasswordField password_field, confirmpassword_field;

    Button create_button;

    /**
     * Constructor
     */
    public CreateAccountWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        email_field = new TextField("User Mail");
        email_field.setPlaceholder("mail");
        email_field.setWidth("100%");

        name_field = new TextField("Name");
        name_field.setPlaceholder("your name");
        name_field.setWidth("100%");

        surname_field = new TextField("Surname");
        surname_field.setPlaceholder("your surname");
        surname_field.setWidth("100%");

        telephone_field = new TextField("Telephone");
        telephone_field.setPlaceholder("your telephone");
        telephone_field.setWidth("100%");

        password_field = new PasswordField("Account Password");
        password_field.setPlaceholder("secret password");
        password_field.setWidth("100%");

        confirmpassword_field = new PasswordField("Confirm Password");
        confirmpassword_field.setPlaceholder("secret password 2");
        confirmpassword_field.setWidth("100%");

        create_button = new Button("Create Account!",this::createbutton_action);
        new ButtonStyler().primaryButtonStyle(create_button,"200px","75px");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H6("Create AIM account!"));
        main_layout.add(email_field);
        HorizontalLayout hl1 = new HorizontalLayout(name_field,surname_field);
        hl1.setWidth("100%");
        main_layout.add(hl1);
        main_layout.add(telephone_field);
        HorizontalLayout hl2 = new HorizontalLayout(password_field,confirmpassword_field);
        hl2.setWidth("100%");
        main_layout.add(hl2);
        main_layout.add(create_button);

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
     * Function for verify user input
     * @return boolean
     */
    boolean verifyFields(){
        return !email_field.getValue().isEmpty() && !name_field.getValue().isEmpty() && !surname_field.getValue().isEmpty() &&
                !telephone_field.getValue().isEmpty() && !password_field.getValue().isEmpty() && !confirmpassword_field.getValue().isEmpty();
    }

    /**
     * Function for verify email input
     * @return boolean
     */
    boolean verifyEmail(){
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(email_field.getValue())
                .matches();
    }

    /**
     * create_button action
     * @param ex
     */
    private void createbutton_action(ClickEvent ex){
        try{
            if ( verifyFields() ){
                // fields corrected
                if ( verifyEmail() ){
                    // email corrected
                    AIM_User aimuser = new AIM_User();
                    aimuser.aim_user_email = email_field.getValue();
                    aimuser.aim_user_name = name_field.getValue();
                    aimuser.aim_user_surname = surname_field.getValue();
                    aimuser.aim_user_telephone = telephone_field.getValue();
                    aimuser.aim_user_type = "NRLUSER";

                    if ( password_field.getValue().equals(confirmpassword_field.getValue()) ){
                        // password confirmed
                        Password_Validator pv = new Password_Validator(password_field.getValue());
                        aimuser.aim_user_password = pv.hash();
                        Database_AIMUser daiu = new Database_AIMUser(AimApplication.database);
                        int ans = daiu.createAIMUser(aimuser);
                        if ( ans == 1 ){
                            Notification.show("User created!");
                            main_dialog.close();
                        }
                        else if (ans == 0){
                            Notification.show("User with this email exists!");
                            email_field.setValue("");
                        }
                        else{
                            Notification.show("Application error, check log!");
                        }
                    }
                    else{
                        Notification.show("Passwords didn't match!");
                        password_field.setValue("");
                        confirmpassword_field.setValue("");
                    }
                }
                else{
                    Notification.show("Wrong email address!");
                    email_field.setValue("");
                }
            }
            else{
                Notification.show("Wrong user input!");
            }
        }catch(Exception e){
            Notification.show("Error: "+e.toString());
        }
    }
}
