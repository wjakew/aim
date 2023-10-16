/**
 * by Jakub Wawak
 * kubawawak@gmail.com
 * all rights reserved
 */
package pl.jakubwawak.aim.website_ui.dialog_windows;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.extern.java.Log;
import pl.jakubwawak.aim.website_ui.views.TerminalView;

/**
 * Window for logging user to the app
 */
public class CommandSuggestionInsertWindow {

    // variables for setting x and y of window
    public String width = "50%";
    public String height = "50%";
    public String backgroundStyle = "";

    // main login components
    public Dialog main_dialog;
    VerticalLayout main_layout;

    String selectedCommand, property1,property2;
    TerminalView terminalView;

    TextField property1_field,property2_field;

    Button createcommand_button;

    /**
     * Constructor
     */
    public CommandSuggestionInsertWindow(String selectedCommand, String property1, String property2, TerminalView terminalView){

        this.property1 = property1;
        this.property2 = property2;
        this.terminalView = terminalView;
        this.selectedCommand = selectedCommand;

        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepare_dialog();
    }

    /**
     * Function for preparing components
     */
    void prepare_components(){
        // set components
        if (!property1.isEmpty()){
            property1_field = new TextField(property1);
            property1_field.setWidth("100%");
            property1_field.setPlaceholder("value");
        }
        if (!property2.isEmpty()){
            property2_field = new TextField(property2);
            property2_field.setWidth("100%");
            property2_field.setPlaceholder("value");
        }

        createcommand_button = new Button("Create Command", VaadinIcon.ARROW_RIGHT.create());
        createcommand_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
        createcommand_button.setWidth("100%");

    }

    /**
     * Function for preparing layout
     */
    void prepare_dialog(){
        prepare_components();
        // set layout
        main_layout.add(VaadinIcon.QUESTION_CIRCLE.create());
        main_layout.add(new H6("CUSTOMIZE YOUR COMMAND"));
        main_layout.add(new H6(selectedCommand));
        if ( !property1.isEmpty() ){
            main_layout.add(property1_field);
        }
        if ( !property2_field.isEmpty() ){
            main_layout.add(property2_field);
        }

        main_layout.add(createcommand_button);

        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");

        main_layout.getStyle().set("border-radius","25px");
        main_layout.getStyle().set("background-color",backgroundStyle);
        main_layout.getStyle().set("--lumo-font-family","Monospace");
        main_dialog.add(main_layout);
        main_dialog.setWidth(width);main_dialog.setHeight(height);
    }

    /**
     * createcommand_button action
     * @param ex
     */
    private void createcommandbutton_action(ClickEvent ex){

    }
}
