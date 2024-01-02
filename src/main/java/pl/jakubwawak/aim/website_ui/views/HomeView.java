/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_board.CurrentBoardComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_projects.CurrentProjectComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.aim_objects_viewers_task.CurrentTaskComposer;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.FloatingWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

import java.util.Date;

/**
 * Object for showing welcome view
 */
@PageTitle("aim home")
@Route(value = "/home")
public class HomeView extends VerticalLayout {

    HorizontalLayout headerLayout, navigationLayout;
    Button home_button, addelement_button,logout_button,user_button;

    Button taskview_button, projectview_button, boardview_button,terminal_button;

    TextField terminal_field;

    Button floatingwindow_button;



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
        if ( AimApplication.loggedUser!=null){
            if ( AimApplication.loggedUser.aim_user_configuration2.equals("blank") ){
                getStyle().set("background-image","radial-gradient(white,gray)");
            }
        }
        else{
            getStyle().set("background-image","radial-gradient("+AimApplication.loggedUser.aim_user_configuration2+")");
        }
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing header objects
     */
    void prepare_header(){
        // prepare menu

        MenuBar headerMenuBar= new MenuBar();
        headerMenuBar.addThemeVariants(MenuBarVariant.LUMO_CONTRAST,MenuBarVariant.LUMO_PRIMARY);

        MenuItem aimItem = headerMenuBar.addItem("Aim");
        SubMenu subItems = aimItem.getSubMenu();

        MenuItem subItems1 = subItems.addItem(new HorizontalLayout(VaadinIcon.DASHBOARD.create(),new H6("Dashboard")));
        subItems1.setCheckable(false);
        subItems1.setChecked(false);

        MenuItem subItems2 = subItems.addItem(new HorizontalLayout(VaadinIcon.TERMINAL.create(),new H6("Terminal")));
        subItems2.setCheckable(false);
        subItems2.setChecked(false);

        MenuItem subItems3 = subItems.addItem(new HorizontalLayout(VaadinIcon.PLUS.create(),new H6("Add Element")));
        subItems3.setCheckable(false);
        subItems3.setChecked(false);

        MenuItem subItems4 = subItems.addItem(new HorizontalLayout(VaadinIcon.USER.create(),new H6("Your Account")));
        subItems4.setCheckable(false);
        subItems4.setChecked(false);

        MenuItem subItems5 = subItems.addItem(new HorizontalLayout(VaadinIcon.USER.create(),new H6("My Space")));
        subItems5.setCheckable(false);
        subItems5.setChecked(false);

        MenuItem subItems6 = subItems.addItem(new HorizontalLayout(VaadinIcon.BOMB.create(),new H6("Glance")));
        subItems6.setCheckable(false);
        subItems6.setChecked(false);

        MenuItem subItems7 = subItems.addItem(new HorizontalLayout(VaadinIcon.CODE.create(),new H6("Coding")));
        subItems7.setCheckable(false);
        subItems7.setChecked(false);

        ComponentEventListener<ClickEvent<MenuItem>> listener = event -> {
            MenuItem selectedItem = event.getSource();
            if ( selectedItem.equals(subItems1)){
                System.out.println("Dashboard");
                getUI().ifPresent(ui -> ui.navigate("/dashboard"));
            }
            else if ( selectedItem.equals(subItems2)){
                System.out.println("Terminal");
                getUI().ifPresent(ui -> ui.navigate("/terminal"));
            }
            else if ( selectedItem.equals(subItems3)){
                System.out.println("Add Element");
                AddElementWindow aew = new AddElementWindow();
                add(aew.main_dialog);
                aew.main_dialog.open();
            }
            else if ( selectedItem.equals(subItems4)){
                System.out.println("Your Account");
                UserWindow uw = new UserWindow();
                add(uw.main_dialog);
                uw.main_dialog.open();
            }
            else if ( selectedItem.equals(subItems5)){
                System.out.println("My Space");
                getUI().ifPresent(ui -> ui.navigate("/widgets"));
            }
            else if ( selectedItem.equals(subItems6)){
                System.out.println("Glance");
                getUI().ifPresent(ui -> ui.navigate("/home"));
            }
            else if ( selectedItem.equals(subItems7)){
                System.out.println("Coding");
                getUI().ifPresent(ui -> ui.navigate("/coding"));
            }
        };

        subItems1.addClickListener(listener);
        subItems2.addClickListener(listener);
        subItems3.addClickListener(listener);
        subItems4.addClickListener(listener);
        subItems5.addClickListener(listener);
        subItems6.addClickListener(listener);


        // prepare floating window button
        floatingwindow_button = new Button("",VaadinIcon.ADJUST.create(),this::floatingbutton_action);
        floatingwindow_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);


        // prepare window layout and components
        FlexLayout center_layout = new FlexLayout();
        center_layout.setSizeFull();
        center_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        center_layout.setAlignItems(FlexComponent.Alignment.CENTER);
        center_layout.add(new H6(new Date().toString()));


        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidth("80%");
        left_layout.add(headerMenuBar,floatingwindow_button);

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
        headerLayout.setVerticalComponentAlignment(Alignment.CENTER);
    }

    /**
     * Function for preparing navigation bar
     */
    void prepareNavigationBar(){
        taskview_button = new Button("Tasks",VaadinIcon.TASKS.create(),this::taskviewbutton_action);
        projectview_button = new Button("Projects",VaadinIcon.BOOK.create(),this::projectviewbutton_action);
        boardview_button = new Button("Boards", VaadinIcon.DASHBOARD.create(),this::boardviewbutton_action);

        projectview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        boardview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);
        taskview_button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_CONTRAST);

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
        add(terminal_field);
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
                AimApplication.session_cbc = new CurrentBoardComposer();
                add(AimApplication.session_cbc.mainLayout);
                Notification.show("Reload page view - boards");
                break;
            }
        }
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        home_button = new Button("", VaadinIcon.DASHBOARD.create(),this::homebutton_action);
        new ButtonStyler().primaryButtonStyle(home_button,"20%","");

        addelement_button= new Button("", VaadinIcon.PLUS.create(),this::addelementbutton_action);
        new ButtonStyler().primaryButtonStyle(addelement_button,"20%","");

        user_button= new Button("", VaadinIcon.USER.create(),this::userbutton_action);
        new ButtonStyler().primaryButtonStyle(user_button,"20%","");

        logout_button = new Button("Log out",VaadinIcon.EXIT.create(),this::logoutbutton_action);
        new ButtonStyler().primaryButtonStyle(logout_button,"40%","");

        terminal_button = new Button("",VaadinIcon.TERMINAL.create(),this::terminalbutton_aciton);
        new ButtonStyler().primaryButtonStyle(terminal_button,"20%","");

        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("type command...");
        terminal_field.setWidth("50%");

        terminal_field.addKeyPressListener(Key.ENTER, e->
        {
            AIMInputParser aip = new AIMInputParser(this);
            aip.setUserInput(terminal_field.getValue());
            aip.parse();
            terminal_field.setValue("");
        });
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

    private void floatingbutton_action(ClickEvent ex){
        FloatingWindow fw = new FloatingWindow();
        add(fw.main_dialog);
        fw.main_dialog.open();
    }

    /**
     * home_button action
     * @param ex
     */
    private void homebutton_action(ClickEvent ex){
        home_button.getUI().ifPresent(ui ->
                ui.navigate("/dashboard"));
    }

    /**
     * logout_button action
     * @param ex
     */
    private void logoutbutton_action(ClickEvent ex){
        logout_button.getUI().ifPresent(ui ->
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
    private void terminalbutton_aciton(ClickEvent ex){
        terminal_button.getUI().ifPresent(ui ->
                ui.navigate("/terminal"));
    }

}