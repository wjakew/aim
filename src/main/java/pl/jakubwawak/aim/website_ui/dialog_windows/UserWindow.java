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
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;


/**
 * Window for logging user to the app
 */
public class UserWindow {

    // variables for setting x and y of window
    public String width = "40%";
    public String height = "60%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    Button applicationsettings_button,removeaccount_button, changepassword_button, adminconsole_button, datamanagment_button, apimanagment_button;

    /**
     * Constructor
     */
    public UserWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        removeaccount_button = new Button("Remove Account", VaadinIcon.USER.create());
        new ButtonStyler().primaryButtonStyle(removeaccount_button,"100%","");

        applicationsettings_button = new Button("Application Settings", VaadinIcon.CHART_TIMELINE.create(),this::applicationsettingsbutton_actionc);
        new ButtonStyler().primaryButtonStyle(applicationsettings_button,"100%","");

        changepassword_button = new Button("Change Password", VaadinIcon.PENCIL.create(),this::changepasswordbutton);
        new ButtonStyler().primaryButtonStyle(changepassword_button,"100%","");

        adminconsole_button = new Button("Admin Console", VaadinIcon.UMBRELLA.create(),this::adminconsolebutton_action);
        new ButtonStyler().primaryButtonStyle(adminconsole_button,"100%","");

        datamanagment_button = new Button("Data Managment", VaadinIcon.DATABASE.create(),this::datamanagmentbutton_action);
        new ButtonStyler().primaryButtonStyle(datamanagment_button,"100%","");

        apimanagment_button = new Button("API Managment", VaadinIcon.PLUG.create(),this::apimanagmentbutton_action);
        new ButtonStyler().primaryButtonStyle(apimanagment_button,"100%","");
    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(VaadinIcon.USER.create());
        main_layout.add(new H6(AimApplication.loggedUser.aim_user_id.toString()));
        main_layout.add(new H6(AimApplication.loggedUser.aim_user_email));
        main_layout.add(applicationsettings_button);
        main_layout.add(removeaccount_button);
        main_layout.add(changepassword_button);
        main_layout.add(apimanagment_button);

        if(AimApplication.loggedUser.aim_user_type.equals("SERVERADM")){
            main_layout.add(adminconsole_button);
            main_layout.add(datamanagment_button);
        }

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
     * changepassword_button action
     * @param ex
     */
    private void changepasswordbutton(ClickEvent ex){
        ResetUserPasswordWindow rupw = new ResetUserPasswordWindow(1);
        main_layout.add(rupw.main_dialog);
        rupw.main_dialog.open();
    }

    /**
     * adminconsole_button action
     * @param ex
     */
    private void adminconsolebutton_action(ClickEvent ex){
        AdminConsoleWindow acw = new AdminConsoleWindow();
        main_layout.add(acw.main_dialog);
        acw.main_dialog.open();
    }

    /**
     * datamanagment_button action
     * @param ex
     */
    private void datamanagmentbutton_action(ClickEvent ex){
        AdminDataManagmentWindow admw = new AdminDataManagmentWindow();
        main_layout.add(admw.main_dialog);
        admw.main_dialog.open();
    }

    /**
     * apimanagment_button action
     * @param ex
     */
    private void apimanagmentbutton_action(ClickEvent ex){
        APIManagerWindow amw = new APIManagerWindow();
        main_layout.add(amw.main_dialog);
        amw.main_dialog.open();
    }

    /**
     * applicationsettings_button action
     * @param ex
     */
    private void applicationsettingsbutton_actionc(ClickEvent ex){
        UserOptionsWindow uow = new UserOptionsWindow();
        main_layout.add(uow.main_dialog);
        uow.main_dialog.open();
    }
}
