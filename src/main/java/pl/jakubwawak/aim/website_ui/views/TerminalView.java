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
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.server.StreamResource;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.CommandSuggestionWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

import java.util.Date;

/**
 * Object for showing welcome view
 */
@PageTitle("aim terminal")
@Route(value = "terminal")
public class TerminalView extends VerticalLayout {

    public TextField terminal_field;

    AIMInputParser aip;

    Button normalmode_button;

    HorizontalLayout headerLayout;

    Button runcommand_button,help_button;
    H6 simpleViewHeader;

    VerticalLayout upperLayout,bottomLayout,footerLayout;

    Button logout_button;
    public VerticalLayout centerLayout;

    /**
     * Constructor
     */
    public TerminalView(){
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
        getStyle().set("color","black");
        getStyle().set("--lumo-font-family","Monospace");
    }

    /**
     * Function for preparing header objects
     */
    void prepare_header(){
        // prepare menu
        logout_button = new Button("Log out",VaadinIcon.EXIT.create(),this::logoutbutton_action);
        new ButtonStyler().primaryButtonStyle(logout_button,"80%","");

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

        MenuItem subItems6 = subItems.addItem(new HorizontalLayout(VaadinIcon.CODE.create(),new H6("Coding")));
        subItems6.setCheckable(false);
        subItems6.setChecked(false);

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
        left_layout.add(headerMenuBar);

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
     * Function for preparing components
     */
    void prepare_components(){
        simpleViewHeader = new H6("AIM BY JAKUB WAWAK");
        simpleViewHeader.setVisible(false);
        aip = new AIMInputParser(this);
        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("let's create something...");
        terminal_field.setWidth("50%");

        terminal_field.addKeyPressListener(e->
        {
            if (e.getKey().equals(Key.ENTER)){
                aip.setUserInput(terminal_field.getValue());
                aip.parse();
                if ( aip.successParsingFlag == 1)
                    terminal_field.setValue("");
                if ( aip.simpleViewFlagNeed == 1){
                    centerLayout.setVisible(false);
                    footerLayout.setVisible(false);
                    upperLayout.setVisible(false);
                    simpleViewHeader.setVisible(true);
                }
                else{
                    centerLayout.setVisible(true);
                    footerLayout.setVisible(true);
                    upperLayout.setVisible(true);
                    simpleViewHeader.setVisible(false);
                }
            }
        });

        normalmode_button = new Button("Go to standard mode",VaadinIcon.NOTEBOOK.create(),this::normalmodebutton_action);
        normalmode_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        normalmode_button.getStyle().set("background-color","white");
        normalmode_button.getStyle().set("color","black");

        runcommand_button = new Button("Run Command",VaadinIcon.COMPILE.create(),this::runcommandbutton_action);
        runcommand_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        runcommand_button.setWidth("50%");

        help_button = new Button("",VaadinIcon.QUESTION_CIRCLE.create(),this::helpbutton_action);
        help_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        upperLayout = new VerticalLayout();
        upperLayout.setSizeFull();
        upperLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        upperLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        bottomLayout = new VerticalLayout();
        bottomLayout.setSizeFull();
        bottomLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        bottomLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        centerLayout = new VerticalLayout();
        centerLayout.setSizeFull();
        centerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centerLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        footerLayout = new VerticalLayout();
        footerLayout.setSizeFull();
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        footerLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

    }

    /**
     * Function for preparing layout
     */
    void prepare_layout(){
        upperLayout.add(headerLayout);
        centerLayout.add(new H6(AimApplication.loggedUser.aim_user_email));
        centerLayout.add(help_button);

        bottomLayout.add(simpleViewHeader,terminal_field,runcommand_button,aip.currentGlanceLayout);

        add(upperLayout,centerLayout,bottomLayout,footerLayout);
    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
            prepare_header();
            prepare_components();
            prepare_layout();
        }
        else{
            // user not logged
            Notification.show("User not logged!");
            add(new H6("User not logged!"));
            add(new H6("aim"));
        }
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

    //--button action functions

    /**
     * normalmode_button action
     * @param ex
     */
    private void normalmodebutton_action(ClickEvent ex){
        normalmode_button.getUI().ifPresent(ui ->
                ui.navigate("/home"));
    }

    /**
     * runcommand_button action
     * @param ex
     */
    private void runcommandbutton_action(ClickEvent ex){
        aip.setUserInput(terminal_field.getValue());
        aip.parse();
        if ( aip.successParsingFlag == 1)
            terminal_field.setValue("");
    }

    /**
     * helpbutton_action
     * @param ex
     */
    private void helpbutton_action(ClickEvent ex){
        CommandSuggestionWindow csw = new CommandSuggestionWindow(terminal_field.getValue(),this);
        add(csw.main_dialog);
        csw.main_dialog.open();
    }
}