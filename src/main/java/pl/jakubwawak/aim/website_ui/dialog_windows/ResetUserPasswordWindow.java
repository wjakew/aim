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
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.Password_Validator;

/**
 * Window for resetting user password
 */
public class ResetUserPasswordWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    PasswordField oldpassword_field;

    PasswordField newpassword_field, confirmpassword_field;

    Button resetpassword_button;



    /**
     * Constructor
     */
    public ResetUserPasswordWindow(int mode){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        oldpassword_field = new PasswordField("Old Password");
        oldpassword_field.setWidth("100%");
        oldpassword_field.setMaxLength(70);

        newpassword_field = new PasswordField("New Password");
        newpassword_field.setMaxLength(70);
        confirmpassword_field = new PasswordField("Confirm Password");
        confirmpassword_field.setMaxLength(70);
        resetpassword_button = new Button("Change Password",this::resetpasswordbutton_action);
        resetpassword_button.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_PRIMARY);
        resetpassword_button.setWidth("100%");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(new H2("Change Password"));
        main_layout.add(oldpassword_field);
        main_layout.add(new HorizontalLayout(newpassword_field,confirmpassword_field));
        main_layout.add(resetpassword_button);

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
     * Function for validating fields
     * @return Boolean
     */
    boolean validateFileds(){
        return !oldpassword_field.getValue().isEmpty() && !newpassword_field.getValue().isEmpty() && !confirmpassword_field.getValue().isEmpty();
    }

    boolean validateSimilarity(){
        return !newpassword_field.getValue().equals(confirmpassword_field.getValue());
    }

    /**
     * resetpassword_button action
     * @param ex
     */
    private void resetpasswordbutton_action(ClickEvent ex){
        if (validateFileds()){
            try{
                Password_Validator pv = new Password_Validator(oldpassword_field.getValue());
                if ( pv.hash().equals(AimApplication.loggedUser.aim_user_password)){
                    // password is correct
                    if ( validateSimilarity()){
                        // password match
                        pv= new Password_Validator(confirmpassword_field.getValue());
                        Database_AIMUser dau = new Database_AIMUser(AimApplication.database);
                        int ans = dau.resetAIMUserPassword(AimApplication.loggedUser.aim_user_id, pv.hash());
                        if ( ans > 0 ){
                            Notification.show("Password changed! "+AimApplication.loggedUser.aim_user_email+" logged out!");
                            AimApplication.loggedUser = null;
                            resetpassword_button.getUI().ifPresent(ui ->
                                    ui.navigate("/login"));
                        }
                        else{
                            Notification.show("Password's doesn't match!");
                        }
                    }
                    else{

                    }
                }
                else{
                    // password is wrong
                }
            }catch(Exception e){
                Notification.show("Error changing password, check log!");
            }
        }
        else{
            Notification.show("Wrong fields input!");
        }
    }
}
