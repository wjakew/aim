/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.CurrentProjectComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.CurrentTaskComposer;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

/**
 * Object for showing welcome view
 */
@PageTitle("aim home")
@Route(value = "home")
public class HomeView extends VerticalLayout {

    HorizontalLayout headerLayout, navigationLayout;
    Button home_button, terminal_button, addelement_button,logout_button,user_button;

    Button taskview_button, projectview_button, boardview_button;



    /**
     * Constructor
     */
    public HomeView(){
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
     * Function for preparing navigation bar
     */
    void prepareNavigationBar(){
        taskview_button = new Button("Tasks",VaadinIcon.TASKS.create(),this::taskviewbutton_action);
        projectview_button = new Button("Projects",VaadinIcon.BOOK.create(),this::projectviewbutton_action);
        boardview_button = new Button("Boards", VaadinIcon.DASHBOARD.create(),this::boardviewbutton_action);

        taskview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        projectview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        boardview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);

        navigationLayout = new HorizontalLayout(taskview_button,projectview_button,boardview_button);
        navigationLayout.setMargin(true);
        navigationLayout.setAlignItems(Alignment.CENTER);
    }

    /**
     * Function for loading main page component
     */
    void loadMainPageComponent(int viewIndex){
        removeAll();
        prepare_components();
        prepare_header();
        prepareNavigationBar();

        add(headerLayout);
        add(navigationLayout);

        switch(viewIndex){
            case 0: {
                // task component
                // creating new task composer on main page
                AimApplication.session_ctc = new CurrentTaskComposer();
                add(AimApplication.session_ctc.mainLayout);
                Notification.show("Reload page view - tasks");
                break;
            }
            case 1:{
                // projects component
                AimApplication.session_cpc = new CurrentProjectComposer();
                add(AimApplication.session_cpc.mainLayout);
                Notification.show("Reload page view - projects");
                break;
            }
            default:{
                add(new H6("VIEW ENGINE ERROR, WRONG ARGUMENT"));
                Notification.show("Wrong argument, cannot reload page ("+viewIndex+")");
                break;
            }
        }
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        home_button = new Button("", VaadinIcon.HOME.create(),this::homebutton_action);
        new ButtonStyler().primaryButtonStyle(home_button,"20%","");
        terminal_button = new Button("",VaadinIcon.TERMINAL.create(),this::terminalbutton_action);
        new ButtonStyler().primaryButtonStyle(terminal_button,"20%","");

        addelement_button= new Button("", VaadinIcon.PLUS.create(),this::addelementbutton_action);
        new ButtonStyler().primaryButtonStyle(addelement_button,"20%","");

        user_button= new Button("", VaadinIcon.USER.create(),this::userbutton_action);
        new ButtonStyler().primaryButtonStyle(user_button,"20%","");

        logout_button = new Button("Log out",VaadinIcon.EXIT.create(),this::logoutbutton_action);
        new ButtonStyler().primaryButtonStyle(logout_button,"80%","");

    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
            // loading selected object on main page
            loadMainPageComponent(0);
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
     * taskview_button action
     * @param ex
     */
    private void taskviewbutton_action(ClickEvent ex){
        loadMainPageComponent(0);
    }

    private void projectviewbutton_action(ClickEvent ex){
        loadMainPageComponent(1);
    }

    private void boardviewbutton_action(ClickEvent ex){
        loadMainPageComponent(2);
    }

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