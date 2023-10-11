/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com
 */
package pl.jakubwawak.aim.website_ui.views;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

/**
 * Object for showing welcome view
 */
@PageTitle("aim terminal")
@Route(value = "terminal")
public class TerminalView extends VerticalLayout {

    TextField terminal_field;

    AIMInputParser aip;

    Button normalmode_button;


    /**
     * Constructor
     */
    public TerminalView(){
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
     * Function for preparing components
     */
    void prepare_components(){
        aip = new AIMInputParser(this);
        terminal_field = new TextField();
        terminal_field.setPrefixComponent(VaadinIcon.TERMINAL.create());
        terminal_field.setPlaceholder("let's create something...");
        terminal_field.setWidth("50%");

        terminal_field.addKeyPressListener(Key.ENTER, e->
        {
            aip.setUserInput(terminal_field.getValue());
            aip.parse();
            if ( aip.successParsingFlag == 1)
                terminal_field.setValue("");
        });

        normalmode_button = new Button("Return to normal mode...",VaadinIcon.NOTEBOOK.create(),this::normalmodebutton_action);
        normalmode_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
    }

    /**
     * Function for preparing layout
     */
    void prepare_layout(){
        add(new H1("Welcome to AIM"));
        add(new H6(AimApplication.loggedUser.aim_user_email));
        add(terminal_field);
        add(normalmode_button);
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
     * normalmode_button action
     * @param ex
     */
    private void normalmodebutton_action(ClickEvent ex){
        normalmode_button.getUI().ifPresent(ui ->
                ui.navigate("/home"));
    }
}