/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.StreamResource;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;
import pl.jakubwawak.aim.website_ui.views.WelcomeView;
import pl.jakubwawak.database_engine.Database_AIMUser;
import pl.jakubwawak.maintanance.Password_Validator;

import java.awt.*;

/**
 * Window for logging user to the app
 */
public class LoginWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "40%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    TextField login_field;
    PasswordField password_field;

    Button login_button, createaccount_button;

    /**
     * Constructor
     */
    public LoginWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        login_field = new TextField();
        login_field.setPlaceholder("login");
        login_field.setPrefixComponent(VaadinIcon.USER.create());
        login_field.setWidth("100%");

        password_field = new PasswordField();
        password_field.setPlaceholder("password");
        password_field.setPrefixComponent(VaadinIcon.LOCK.create());
        password_field.setWidth("100%");

        login_button = new Button("Login",VaadinIcon.ARROW_RIGHT.create(),this::loginbutton_action);
        createaccount_button = new Button("",VaadinIcon.PLUS.create(),this::createaccoutnbutton_action);

        // styling buttons
        new ButtonStyler().primaryButtonStyle(login_button,"80%"," 100%");
        new ButtonStyler().primaryButtonStyle(createaccount_button,"10%","100%");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout

        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
        });

        HorizontalLayout button_layout = new HorizontalLayout(createaccount_button,login_button);
        button_layout.setWidth("100%");

        VerticalLayout vl = new VerticalLayout();
        vl.add(login_field,password_field,button_layout);
        vl.setSizeFull();
        vl.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        vl.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        vl.getStyle().set("text-align", "center");

        Image logo = new Image(res,"aim logo");
        logo.setHeight("70%");
        logo.setWidth("70%");

        HorizontalLayout mainHL = new HorizontalLayout();
        mainHL.setSizeFull();
        mainHL.setAlignItems(FlexComponent.Alignment.CENTER);

        mainHL.add(logo,vl);

        Span header = new Span("welcome to AIM");
        header.getStyle().set("--lumo-font-family","Monospace");

        main_layout.add(header,mainHL);

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
     * createaccount_button action
     * @param ex
     */
    private void createaccoutnbutton_action(ClickEvent ex){
        CreateAccountWindow caw = new CreateAccountWindow();
        main_layout.add(caw.main_dialog);
        caw.main_dialog.open();
        main_dialog.close();
    }

    /**
     * login_button action
     * @param ex
     */
    private void loginbutton_action(ClickEvent ex){
        if (!login_field.getValue().isEmpty() && !password_field.getValue().isEmpty()){
            // try to login
            try {
                Database_AIMUser daim = new Database_AIMUser(AimApplication.database);
                Password_Validator pv = new Password_Validator(password_field.getValue());
                int ans = daim.loginAIMUser(login_field.getValue(),pv.hash());
                if ( ans == 1 && AimApplication.loggedUser != null){
                    // logged successfully
                    Notification.show("Welcome back "+AimApplication.loggedUser.aim_user_email+"!");
                    login_button.getUI().ifPresent(ui ->
                            ui.navigate("/home"));
                }
                else if ( ans == 0 ){
                    Notification.show("Wrong login or password!");
                }
                else{
                    Notification.show("Application error, check log!");
                }
            }catch(Exception e){
                Notification.show("Error: "+e.toString());
            }
        }
    }
}
