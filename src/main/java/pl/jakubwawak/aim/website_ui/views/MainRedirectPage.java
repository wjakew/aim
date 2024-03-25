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
import pl.jakubwawak.aim.website_ui.PageHeader;
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

    VerticalLayout upperLayout,bottomLayout,footerLayout;
    public VerticalLayout centerLayout;

    Button terminalmode_button, normalmode_button;

    PageHeader pageHeader;

    /**
     * Constructor
     */
    public MainRedirectPage(){
        addClassName("mainredirect-page");
        prepare_view();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

    }


    /**
     * Function for preparing components
     */
    void prepare_components(){
        pageHeader = new PageHeader();

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
     * Function for preparing layout
     */
    void prepare_layout(){
        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
        });
        Image logo = new Image(res,"aim logo");
        logo.setHeight("5rem");
        logo.setWidth("5rem");

        upperLayout.add(logo);
        UserDashboardLayout udl = new UserDashboardLayout(0);
        centerLayout.add(udl.main_dashboard_layout,pageHeader);

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