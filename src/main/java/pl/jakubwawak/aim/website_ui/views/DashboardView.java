/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.UserDashboardLayout;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

/**
 * Object for showing welcome view
 */
@PageTitle("aim dashboard")
@Route(value = "dashboard")
public class DashboardView extends VerticalLayout {

    HorizontalLayout headerLayout;
    Button home_button, terminal_button, addelement_button,logout_button,user_button;


    /**
     * Constructor
     */
    public DashboardView(){
        this.getElement().setAttribute("theme", Lumo.DARK);
        prepare_view();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().set("background-image","linear-gradient(black, white)");
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing header objects
     */
    void prepare_header(){
        // prepare window layout and components
        FlexLayout center_layout = new FlexLayout();
        center_layout.setSizeFull();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        center_layout.add(new H6("AIM"));

        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.END);
        left_layout.setAlignItems(FlexComponent.Alignment.START);
        left_layout.setWidth("80%");
        left_layout.add(home_button,terminal_button,user_button,addelement_button);

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.START);
        right_layout.setAlignItems(FlexComponent.Alignment.END);
        right_layout.add(logout_button);
        right_layout.setWidth("80%");

        headerLayout = new HorizontalLayout(left_layout,center_layout,right_layout);
        headerLayout.setWidth("70%");
        headerLayout.setMargin(true);
        headerLayout.getStyle().set("background-color","gray");
        headerLayout.getStyle().set("color","black");
        headerLayout.getStyle().set("border-radius","15px");

        headerLayout.setMargin(true);
        headerLayout.setAlignItems(Alignment.CENTER);
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        home_button = new Button("Home", VaadinIcon.HOME.create(),this::homebutton_action);
        new ButtonStyler().primaryButtonStyle(home_button,"20%","");
        terminal_button = new Button("Term",VaadinIcon.TERMINAL.create(),this::terminalbutton_action);
        new ButtonStyler().primaryButtonStyle(terminal_button,"20%","");
        terminal_button.setEnabled(false);

        addelement_button= new Button("Add", VaadinIcon.PLUS.create(),this::addelementbutton_action);
        new ButtonStyler().primaryButtonStyle(addelement_button,"20%","");

        user_button= new Button("You", VaadinIcon.USER.create(),this::userbutton_action);
        new ButtonStyler().primaryButtonStyle(user_button,"20%","");

        logout_button = new Button("Log out",VaadinIcon.EXIT.create(),this::logoutbutton_action);
        new ButtonStyler().primaryButtonStyle(logout_button,"80%","");
    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
            prepare_components();
            prepare_header();
            add(headerLayout);
            UserDashboardLayout udl = new UserDashboardLayout();
            add(udl.main_dashboard_layout);
        }
        else{
            // user not logged
            Notification.show("User not logged!");
            add(new H6("User not logged!"));
            add(new H6("aim"));
        }
    }

    //--button action functions

    /**
     * home_button action
     * @param ex
     */
    private void homebutton_action(ClickEvent ex){
        home_button.getUI().ifPresent(ui ->
                ui.navigate("/home"));
    }

    /**
     * logout_button action
     * @param ex
     */
    private void logoutbutton_action(ClickEvent ex){
        home_button.getUI().ifPresent(ui ->
                ui.navigate("/"));
        Notification.show("User ("+AimApplication.loggedUser.aim_user_email+") logged out!");
        AimApplication.database.log("DB-AIMUSER-LOGOUT","User logged out from the app ("+AimApplication.loggedUser.aim_user_email+")");
        AimApplication.loggedUser = null;
    }

    /**
     * addelement_button action
     * @param ex
     */
    private void addelementbutton_action(ClickEvent ex){
        AddElementWindow aew = new AddElementWindow();
        add(aew.main_dialog);
        aew.main_dialog.open();
    }

    /**
     * user_button action
     * @param ex
     */
    private void userbutton_action(ClickEvent ex){
        UserWindow uw = new UserWindow();
        add(uw.main_dialog);
        uw.main_dialog.open();
    }

    /**
     * terminal_button action
     * @param ex
     */
    private void terminalbutton_action(ClickEvent ex){
        terminal_button.getUI().ifPresent(ui ->
                ui.navigate("/terminal"));
    }

}