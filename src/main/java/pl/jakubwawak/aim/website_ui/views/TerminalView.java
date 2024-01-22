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
import pl.jakubwawak.aim.website_ui.PageHeader;
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

    PageHeader pageHeader;

    AIMInputParser aip;

    Button normalmode_button;

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

        pageHeader = new PageHeader();

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
        upperLayout.add(pageHeader);
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