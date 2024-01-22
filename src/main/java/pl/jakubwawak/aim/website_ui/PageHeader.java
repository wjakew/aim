/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
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
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.FloatingWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.LogViewerWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;

import java.util.Date;

/**
 * Object for creating header layout for every page
 */
public class PageHeader extends HorizontalLayout{

    Button logout_button,floatingwindow_button, log_button;

    public MenuBar headerMenuBar;

    /**
     * Constructor
     */
    public PageHeader(){
        prepare_header();
    }


    /**
     * Function for preparing header objects
     */
    void prepare_header(){
        // prepare menu
        headerMenuBar = new MenuBar();
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

        MenuItem subItems5 = subItems.addItem(new HorizontalLayout(VaadinIcon.SQUARE_SHADOW.create(),new H6("Widgets")));
        subItems5.setCheckable(false);
        subItems5.setChecked(false);

        MenuItem subItems6 = subItems.addItem(new HorizontalLayout(VaadinIcon.LIST_OL.create(),new H6("Glance")));
        subItems6.setCheckable(false);
        subItems6.setChecked(false);

        MenuItem subItems7 = subItems.addItem(new HorizontalLayout(VaadinIcon.CODE.create(),new H6("Coding")));
        subItems7.setCheckable(false);
        subItems7.setChecked(false);

        MenuItem subItems8 = subItems.addItem(new HorizontalLayout(VaadinIcon.WORKPLACE.create(),new H6("Workspace")));
        subItems8.setCheckable(false);
        subItems8.setChecked(false);

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
            else if ( selectedItem.equals(subItems8)){
                System.out.println("Workspace");
                getUI().ifPresent(ui -> ui.navigate("/workspace"));
            }
        };
        subItems1.addClickListener(listener);
        subItems2.addClickListener(listener);
        subItems3.addClickListener(listener);
        subItems4.addClickListener(listener);
        subItems5.addClickListener(listener);
        subItems6.addClickListener(listener);
        subItems7.addClickListener(listener);
        subItems8.addClickListener(listener);

        // prepare floating window button
        floatingwindow_button = new Button("",VaadinIcon.ADJUST.create(),this::floatingbutton_action);
        floatingwindow_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        // prepare logout button
        logout_button = new Button("",VaadinIcon.EXIT.create(),this::logoutbutton_action);
        logout_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        logout_button.setWidth("40%");

        // prepare log button
        log_button = new Button("",VaadinIcon.INFO.create(),this::setLog_button);
        log_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        log_button.setWidth("20%");

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
        right_layout.add(log_button,logout_button);
        right_layout.setWidth("80%");

        add(left_layout,center_layout,right_layout);
        setWidth("70%");
        setMargin(true);
        getStyle().set("background-color","gray");
        getStyle().set("color","black");
        getStyle().set("border-radius","15px");

        setMargin(true);
        setAlignItems(Alignment.CENTER);
        setVerticalComponentAlignment(Alignment.CENTER);
    }

    /**
     * floating_button action
     * @param ex
     */
    private void floatingbutton_action(ClickEvent ex){
        FloatingWindow fw = new FloatingWindow();
        add(fw.main_dialog);
        fw.main_dialog.open();
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
     * log_button action
     * @param ex
     */
    private void setLog_button(ClickEvent ex){
        LogViewerWindow lvw = new LogViewerWindow();
        add(lvw.main_dialog);
        lvw.main_dialog.open();
    }

}
