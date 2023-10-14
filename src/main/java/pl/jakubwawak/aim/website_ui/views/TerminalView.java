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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import pl.jakubwawak.aim.AimApplication;
import pl.jakubwawak.aim.aim_dataengine.aim_terminal_engine.AIMInputParser;
import pl.jakubwawak.aim.website_ui.dialog_windows.AddElementWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.UserWindow;
import pl.jakubwawak.aim.website_ui.dialog_windows.obiect_windows.CommandSuggestionWindow;
import pl.jakubwawak.aim.website_ui.style.ButtonStyler;

import java.lang.management.GarbageCollectorMXBean;

/**
 * Object for showing welcome view
 */
@PageTitle("aim terminal")
@Route(value = "terminal")
public class TerminalView extends VerticalLayout {

    TextField terminal_field;

    AIMInputParser aip;

    Button normalmode_button;

    Button runcommand_button,help_button;

    VerticalLayout upperLayout, centerLayout,bottomLayout,footerLayout;


    /**
     * Constructor
     */
    public TerminalView(){
        prepare_view();

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
        getStyle().set("background-image","radial-gradient(white,gray)");
        getStyle().set("color","black");
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

        terminal_field.addKeyPressListener(e->
        {
            if (e.getKey().equals(Key.ENTER)){
                aip.setUserInput(terminal_field.getValue());
                aip.parse();
                if ( aip.successParsingFlag == 1)
                    terminal_field.setValue("");
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
        StreamResource res = new StreamResource("aim_logo.png", () -> {
            return WelcomeView.class.getClassLoader().getResourceAsStream("images/aim_logo.png");
        });
        Image logo = new Image(res,"aim logo");
        logo.setHeight("15rem");
        logo.setWidth("15rem");

        upperLayout.add(logo);
        centerLayout.add(new H6("WELCOME TO AIM"));
        centerLayout.add(new H6(AimApplication.loggedUser.aim_user_email));
        centerLayout.add(help_button);

        bottomLayout.add(terminal_field,runcommand_button);
        footerLayout.add(normalmode_button);

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