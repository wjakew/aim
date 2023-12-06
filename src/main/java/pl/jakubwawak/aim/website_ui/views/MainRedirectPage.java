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
import pl.jakubwawak.aim.aim_dataengine.aim_objects_viewers.UserDashboardLayout;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.CommandSuggestionWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Object for showing welcome view
 */
@PageTitle("your aim")
@Route(value = "aim")
public class MainRedirectPage extends VerticalLayout {

    public TextField terminal_field;


    VerticalLayout upperLayout,bottomLayout,footerLayout;
    public VerticalLayout centerLayout;

    Button terminalmode_button, normalmode_button;

    HorizontalLayout headerLayout;

    /**
     * Constructor
     */
    public MainRedirectPage(){
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
     * Function for preparing components
     */
    void prepare_components(){
        headerLayout = new HorizontalLayout();

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

        terminalmode_button = new Button("Terminal Mode",VaadinIcon.TERMINAL.create(),this::terminalmodebutton_action);
        normalmode_button = new Button("Normal Mode",VaadinIcon.TASKS.create(),this:: normalmodebutton_action);

        terminalmode_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        normalmode_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

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

        MenuItem subItems6 = subItems.addItem(new HorizontalLayout(VaadinIcon.USER.create(),new H6("Glance")));
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
                System.out.println("Glance");
                getUI().ifPresent(ui -> ui.navigate("/home"));
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
        center_layout.add(headerMenuBar);


        FlexLayout left_layout = new FlexLayout();
        left_layout.setSizeFull();
        left_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        left_layout.setAlignItems(Alignment.CENTER);
        left_layout.setWidth("80%");
        left_layout.add(new H6(LocalDateTime.now().toString().split("T")[0]));

        FlexLayout right_layout = new FlexLayout();
        right_layout.setSizeFull();
        right_layout.setJustifyContentMode(JustifyContentMode.CENTER);
        right_layout.setAlignItems(Alignment.CENTER);
        right_layout.setWidth("80%");
        right_layout.add(new H6(AimApplication.loggedUser.aim_user_email));

        headerLayout = new HorizontalLayout(left_layout,center_layout,right_layout);
        headerLayout.setWidth("80%");
        headerLayout.setMargin(true);
        headerLayout.getStyle().set("background-color","gray");
        headerLayout.getStyle().set("color","black");
        headerLayout.getStyle().set("border-radius","15px");

        headerLayout.setMargin(true);
        headerLayout.setAlignItems(Alignment.CENTER);
        headerLayout.setVerticalComponentAlignment(Alignment.CENTER);
    }

    /**
     * Function for preparing layout
     */
    void prepare_layout(){
        prepare_header();
        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
        });
        Image logo = new Image(res,"aim logo");
        logo.setHeight("10rem");
        logo.setWidth("10rem");

        upperLayout.add(logo);
        UserDashboardLayout udl = new UserDashboardLayout(0);
        centerLayout.add(udl.main_dashboard_layout,headerLayout);

        add(upperLayout,centerLayout,bottomLayout);
    }

    /**
     * Function for preparing view and components
     */
    void prepare_view(){
        if (AimApplication.loggedUser != null){
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

    //--button action functions

    /**
     * temrinalmode_button
     * @param ex
     */
    private void terminalmodebutton_action(ClickEvent ex){
        terminalmode_button.getUI().ifPresent(ui ->
                ui.navigate("/terminal"));
    }

    /**
     * normalmode_button
     * @param ex
     */
    private void normalmodebutton_action(ClickEvent ex){
        normalmode_button.getUI().ifPresent(ui ->
                ui.navigate("/home"));
    }
}